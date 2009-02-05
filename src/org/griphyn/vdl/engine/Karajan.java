package org.griphyn.vdl.engine;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlFloat;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.globus.swift.language.ActualParameter;
import org.globus.swift.language.ApplicationBinding;
import org.globus.swift.language.Array;
import org.globus.swift.language.Assign;
import org.globus.swift.language.BinaryOperator;
import org.globus.swift.language.Binding;
import org.globus.swift.language.Call;
import org.globus.swift.language.Dataset;
import org.globus.swift.language.Foreach;
import org.globus.swift.language.FormalParameter;
import org.globus.swift.language.Function;
import org.globus.swift.language.If;
import org.globus.swift.language.Iterate;
import org.globus.swift.language.LabelledBinaryOperator;
import org.globus.swift.language.Procedure;
import org.globus.swift.language.ProgramDocument;
import org.globus.swift.language.Range;
import org.globus.swift.language.Switch;
import org.globus.swift.language.TypeRow;
import org.globus.swift.language.TypeStructure;
import org.globus.swift.language.UnlabelledUnaryOperator;
import org.globus.swift.language.Variable;
import org.globus.swift.language.Dataset.Mapping;
import org.globus.swift.language.Dataset.Mapping.Param;
import org.globus.swift.language.If.Else;
import org.globus.swift.language.If.Then;
import org.globus.swift.language.Procedure;
import org.globus.swift.language.ProgramDocument.Program;
import org.globus.swift.language.StructureMember;
import org.globus.swift.language.Switch.Case;
import org.globus.swift.language.Switch.Default;
import org.globus.swift.language.TypesDocument.Types;
import org.globus.swift.language.TypesDocument.Types.Type;
import org.griphyn.vdl.karajan.CompilationException;
import org.safehaus.uuid.UUIDGenerator;
import org.w3c.dom.Node;

public class Karajan {
	public static final Logger logger = Logger.getLogger(Karajan.class);
	
	public static final String TEMPLATE_FILE_NAME = "Karajan.stg"; 

	Map stringInternMap = new HashMap();
	Map intInternMap = new HashMap();
	Map floatInternMap = new HashMap();
	Map proceduresMap = new HashMap();
	Map functionsMap = new HashMap();
	
	Types types = null;
	
	int internedIDCounter = 17000;

	/** an arbitrary statement identifier. Start at some high number to
	    aid visual distinction in logs, but the actual value doesn't
		matter. */
	int callID = 88000;

	StringTemplateGroup m_templates;

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Please provide a SwiftScript program file.");
			System.exit(1);
		}
		compile(args[0], System.out);
	}
	
	public static void compile(String in, PrintStream out) throws CompilationException {
		Karajan me = new Karajan();
		StringTemplateGroup templates;
		try {
			templates = new StringTemplateGroup(new InputStreamReader(
					Karajan.class.getClassLoader().getResource(TEMPLATE_FILE_NAME).openStream()));
		} catch(IOException ioe) {
			throw new CompilationException("Unable to load karajan source templates",ioe);
		}

		ProgramDocument programDoc;
		try {
			programDoc = parseProgramXML(in);
		} catch(Exception e) {
			throw new CompilationException("Unable to parse intermediate XML",e);
		}

		Program prog = programDoc.getProgram();

		try {
			me.setTemplateGroup(templates);
		} catch(IOException ioe) {
			throw new CompilationException("Unable to set source templates",ioe);
		}
		StringTemplate code = me.program(prog);
		out.println(code.toString());
	}

	public static ProgramDocument parseProgramXML(String defs) 
		throws XmlException, IOException {

		XmlOptions options = new XmlOptions();
		Collection errors = new ArrayList();
		options.setErrorListener(errors);
		options.setValidateOnSet();
		options.setLoadLineNumbers();

		ProgramDocument programDoc;
		programDoc  = ProgramDocument.Factory.parse(new File(defs), options);

		if(programDoc.validate(options)) {
			logger.info("Validation of XML intermediate file was successful");
		} else {
			logger.warn("Validation of XML intermediate file failed.");
				// these errors look rather scary, so output them at
				// debug level
			logger.debug("Validation errors:");
			Iterator i = errors.iterator();
			while(i.hasNext()) {
				XmlError error = (XmlError) i.next();
				logger.debug(error.toString());
			}
			System.exit(3);
		}
		return programDoc;
	}

	public Karajan() {
		// Built-in procedures		
		proceduresMap = ProcedureSignature.makeProcedureSignatures();
		// Built-in functions
		functionsMap = ProcedureSignature.makeFunctionSignatures();
	}

	void setTemplateGroup(StringTemplateGroup tempGroup) throws IOException {
		m_templates = tempGroup;
	}

	protected StringTemplate template(String name) {
		return m_templates.getInstanceOf(name);
	}

	public StringTemplate program(Program prog) throws CompilationException {
		VariableScope scope = new VariableScope(this, null);
		scope.bodyTemplate = template("program");

		types = prog.getTypes();
		if (types != null) {
			for (int i = 0; i < types.sizeOfTypeArray(); i++) {
				Type theType = types.getTypeArray(i);
				String typeName = theType.getTypename();
				String typeAlias = theType.getTypealias();
											
				StringTemplate st = template("typeDef");
				st.setAttribute("name", typeName);
				if (typeAlias != null && !typeAlias.equals("") && !typeAlias.equals("string")) {
					checkIsTypeDefined(typeAlias);
					st.setAttribute("type", typeAlias);
				}
										
				TypeStructure ts = theType.getTypestructure();
				for (int j = 0; j < ts.sizeOfMemberArray(); j++) {				
					TypeRow tr = ts.getMemberArray(j);
									
					StringTemplate stMember = template("memberdefinition");
					stMember.setAttribute("name", tr.getMembername());
					stMember.setAttribute("type", tr.getMembertype());
					
					st.setAttribute("members", stMember);
				}
				scope.bodyTemplate.setAttribute("types", st);
			}
		}
		
		// Keep track of declared procedures
		for (int i = 0; i < prog.sizeOfProcedureArray(); i++) {
			Procedure proc = prog.getProcedureArray(i);
			ProcedureSignature ps = new ProcedureSignature(proc.getName());
			ps.setInputArgs(proc.getInputArray());
			ps.setOutputArgs(proc.getOutputArray());			
			proceduresMap.put(proc.getName(), ps);
		}
		
		for (int i = 0; i < prog.sizeOfProcedureArray(); i++) {
			Procedure proc = prog.getProcedureArray(i);
			procedure(proc, scope);
		}

		statements(prog, scope);

		generateInternedConstants(scope.bodyTemplate);

		return scope.bodyTemplate;
	}

	public void procedure(Procedure proc, VariableScope containingScope) throws CompilationException {
		VariableScope outerScope = new VariableScope(this, null);
		VariableScope innerScope = new VariableScope(this, outerScope, VariableScope.ENCLOSURE_NONE);
		StringTemplate procST = template("procedure");
		containingScope.bodyTemplate.setAttribute("procedures", procST);
		procST.setAttribute("name", proc.getName());
		for (int i = 0; i < proc.sizeOfOutputArray(); i++) {
			FormalParameter param = proc.getOutputArray(i);
			StringTemplate paramST = parameter(param, innerScope);
			procST.setAttribute("outputs", paramST);
			if (!param.isNil())
				procST.setAttribute("optargs", paramST);
			else
				procST.setAttribute("arguments", paramST);
			checkIsTypeDefined(param.getType().getLocalPart());
			innerScope.addVariable(param.getName(), param.getType().getLocalPart());
		}
		for (int i = 0; i < proc.sizeOfInputArray(); i++) {
			FormalParameter param = proc.getInputArray(i);
			StringTemplate paramST = parameter(param, outerScope);
			procST.setAttribute("inputs", paramST);
			if (!param.isNil())
				procST.setAttribute("optargs", paramST);
			else
				procST.setAttribute("arguments", paramST);
			checkIsTypeDefined(param.getType().getLocalPart());
			outerScope.addVariable(param.getName(), param.getType().getLocalPart());
		}

		Binding bind;
		if ((bind = proc.getBinding()) != null) {
			binding(bind, procST, innerScope);
		}
		else {			
			VariableScope compoundScope = new VariableScope(this, innerScope);
			compoundScope.bodyTemplate = procST;
			statements(proc, compoundScope);		
		}			
	}

	public StringTemplate parameter(FormalParameter param, VariableScope scope) throws CompilationException {
		StringTemplate paramST = new StringTemplate("parameter");
		StringTemplate typeST = new StringTemplate("type");
		paramST.setAttribute("name", param.getName());
		typeST.setAttribute("name", param.getType().getLocalPart());
		typeST.setAttribute("namespace", param.getType().getNamespaceURI());
		paramST.setAttribute("type", typeST);
		paramST.setAttribute("isArray", new Boolean(param.getIsArray1()));
		if(!param.isNil())
			paramST.setAttribute("default",expressionToKarajan(param.getAbstractExpression(), scope));
		return paramST;
	}

	public void variable(Variable var, VariableScope scope) throws CompilationException {
		StringTemplate variableST = template("variable");
		scope.bodyTemplate.setAttribute("declarations", variableST);
		variableST.setAttribute("name", var.getName());
		variableST.setAttribute("type", var.getType().getLocalPart());
		variableST.setAttribute("isArray", Boolean.valueOf(var.getIsArray1()));

		checkIsTypeDefined(var.getType().getLocalPart());
		
		if(!var.isNil()) {
			StringTemplate exprST = expressionToKarajan(var.getAbstractExpression(),scope);
			if (!datatype(exprST).equals(var.getType().getLocalPart()))
				throw new CompilationException("Could not assign expression of type " + datatype(exprST) +
						" to a variable of type " + var.getType().getLocalPart());
			variableST.setAttribute("expr", exprST);
		} else {
			// add temporary mapping info
			StringTemplate mappingST = new StringTemplate("mapping");
			mappingST.setAttribute("descriptor", "concurrent_mapper");
			StringTemplate paramST = template("vdl_parameter");
			paramST.setAttribute("name", "prefix");
			paramST.setAttribute("expr", var.getName() + "-"
					+ UUIDGenerator.getInstance().generateRandomBasedUUID().toString());
			mappingST.setAttribute("params", paramST);
			variableST.setAttribute("mapping", mappingST);
			variableST.setAttribute("nil", Boolean.TRUE);
		}
		scope.addVariable(var.getName(), var.getType().getLocalPart());
	}

	public void dataset(Dataset dataset, VariableScope scope) throws CompilationException {
		StringTemplate datasetST = template("variable");
		datasetST.setAttribute("name", dataset.getName());
		datasetST.setAttribute("type", dataset.getType().getLocalPart());
		if (dataset.isSetIsArray1()) {
			datasetST.setAttribute("isArray", Boolean.valueOf(dataset.getIsArray1()));
		}
		
		checkIsTypeDefined(dataset.getType().getLocalPart());
		
		if (dataset.getFile() != null) {
			StringTemplate fileST = new StringTemplate("file");
			fileST.setAttribute("name", escapeQuotes(dataset.getFile().getName()));
			fileST.defineFormalArgument("params");
			datasetST.setAttribute("file", fileST);
		}
		Mapping mapping = dataset.getMapping();

		if (mapping != null) {
			StringTemplate mappingST = new StringTemplate("mapping");
			mappingST.setAttribute("descriptor", mapping.getDescriptor());
			for (int i = 0; i < mapping.sizeOfParamArray(); i++) {
				Param param = mapping.getParamArray(i);
				StringTemplate paramST = template("vdl_parameter");
				paramST.setAttribute("name", param.getName());
				Node expressionDOM = param.getAbstractExpression().getDomNode();
				String namespaceURI = expressionDOM.getNamespaceURI();
				String localName = expressionDOM.getLocalName();
				QName expressionQName = new QName(namespaceURI, localName);
				if(expressionQName.equals(VARIABLE_REFERENCE_EXPR))     {
					paramST.setAttribute("expr",expressionToKarajan(param.getAbstractExpression(),scope));
				} else {
					String parameterVariableName="swift#mapper#"+(internedIDCounter++);
					// make template for variable declaration (need to compute type of this variable too?)
					StringTemplate variableDeclarationST = template("variable");
					variableDeclarationST.setAttribute("waitfor","");
					// TODO factorise this and other code in variable()?
					StringTemplate pmappingST = new StringTemplate("mapping");
					pmappingST.setAttribute("descriptor", "concurrent_mapper");
					StringTemplate pparamST = template("vdl_parameter");
					pparamST.setAttribute("name", "prefix");
					pparamST.setAttribute("expr", parameterVariableName + "-" + UUIDGenerator.getInstance().generateRandomBasedUUID().toString());
					pmappingST.setAttribute("params", pparamST);
					variableDeclarationST.setAttribute("mapping", pmappingST);
					variableDeclarationST.setAttribute("nil", Boolean.TRUE);
					variableDeclarationST.setAttribute("name", parameterVariableName);
					scope.bodyTemplate.setAttribute("declarations",variableDeclarationST);
					StringTemplate paramValueST=expressionToKarajan(param.getAbstractExpression(),scope);
					String paramValueType = datatype(paramValueST);
					scope.addVariable(parameterVariableName, paramValueType);
					variableDeclarationST.setAttribute("type", paramValueType);
					StringTemplate variableReferenceST = template("id");
					variableReferenceST.setAttribute("var",parameterVariableName);
					StringTemplate variableAssignmentST = template("assign");
					variableAssignmentST.setAttribute("var",variableReferenceST);
					variableAssignmentST.setAttribute("value",paramValueST);
					scope.appendStatement(variableAssignmentST);
					paramST.setAttribute("expr",variableReferenceST);
				}
				mappingST.setAttribute("params", paramST);
			}
			datasetST.setAttribute("mapping", mappingST);
		}
		scope.bodyTemplate.setAttribute("declarations", datasetST);
		scope.addVariable(dataset.getName(), dataset.getType().getLocalPart());
	}
	
	void checkIsTypeDefined(String type) throws CompilationException {		
		while (type.length() > 2 && type.substring(type.length() - 2).equals("[]"))
			type = type.substring(0, type.length() - 2);		
		if (!type.equals("int") && !type.equals("float") && !type.equals("string") 
				&& !type.equals("boolean") && !type.equals("external")) {
			boolean typeDefined = false;
			if (types != null) {
				for (int i = 0; i < types.sizeOfTypeArray(); i++)
					if (types.getTypeArray(i).getTypename().equals(type)) {
						typeDefined = true;
						break;
					}
			}
			if (!typeDefined)
				throw new CompilationException("Type " + type + " is not defined.");
		}
	}

	public void assign(Assign assign, VariableScope scope) throws CompilationException {
		try {
			StringTemplate assignST = template("assign");
			StringTemplate varST = expressionToKarajan(assign.getAbstractExpressionArray(0),scope);
			StringTemplate valueST = expressionToKarajan(assign.getAbstractExpressionArray(1),scope);
			if (!datatype(varST).equals(datatype(valueST)))
				throw new CompilationException("You cannot assign value of type " + datatype(valueST) +
						" to a variable of type " + datatype(varST));
			assignST.setAttribute("var", varST);
			assignST.setAttribute("value", valueST);
			String rootvar = abstractExpressionToRootVariable(assign.getAbstractExpressionArray(0));
			scope.addWriter(rootvar, new Integer(callID++), rootVariableIsPartial(assign.getAbstractExpressionArray(0)));
			scope.appendStatement(assignST);
		} catch(CompilationException re) {
			throw new CompilationException("Compile error in assigment at "+assign.getSrc()+": "+re.getMessage(),re);
		}
	}

	public void statements(XmlObject prog, VariableScope scope) throws CompilationException {
		XmlCursor cursor = prog.newCursor();
		cursor.selectPath("*");
		while (cursor.toNextSelection()) {
			XmlObject child = cursor.getObject();
			statement(child, scope);
		}
	}

	public void statement(XmlObject child, VariableScope scope) throws CompilationException {
		if (child instanceof Variable) {
			variable((Variable) child, scope);
		}
		else if (child instanceof Dataset) {
			dataset((Dataset) child, scope);
		}
		else if (child instanceof Assign) {
			assign((Assign) child, scope);
		}
		else if (child instanceof Call) {
			call((Call) child, scope);
		}
		else if (child instanceof Foreach) {
			foreachStat((Foreach)child, scope);
		}
		else if (child instanceof Iterate) {
			iterateStat((Iterate)child, scope);
		}
		else if (child instanceof If) {
			ifStat((If)child, scope);
		} else if (child instanceof Switch) {
			switchStat((Switch) child, scope);
		} else if (child instanceof Procedure
			|| child instanceof Types
			|| child instanceof FormalParameter) {
			// ignore these - they're expected but we don't need to
			// do anything for them here
		} else {
			throw new CompilationException("Unexpected element in XML. Implementing class "+child.getClass()+", content "+child);
		}
	}

	public void call(Call call, VariableScope scope) throws CompilationException {
		try {
			StringTemplate callST = template("call");
			callST.setAttribute("func", call.getProc().getLocalPart());
			StringTemplate parentST = callST.getEnclosingInstance();
			
			// Check is called procedure declared previously 
			String procName = call.getProc().getLocalPart();
			if (proceduresMap.get(procName) == null)
				throw new CompilationException("Procedure " + procName + " is not declared.");
			
			// Check procedure arguments			
			int noOfOptInArgs = 0;
			Map inArgs = new HashMap();
			Map outArgs = new HashMap();
			
			ProcedureSignature proc = (ProcedureSignature)proceduresMap.get(procName);
			/* Does number of input arguments match */				
			for (int i = 0; i < proc.sizeOfInputArray(); i++) {
				if (proc.getInputArray(i).isOptional())
					noOfOptInArgs++;
				inArgs.put(proc.getInputArray(i).getName(), proc.getInputArray(i));
			}				
			if (!proc.getAnyNumOdInputArgs() && (call.sizeOfInputArray() < proc.sizeOfInputArray() - noOfOptInArgs ||
				                                 call.sizeOfInputArray() > proc.sizeOfInputArray()))
				throw new CompilationException("Wrong number of procedure input arguments: specified " + call.sizeOfInputArray() + 
						" and should be " + proc.sizeOfInputArray());
			
			/* Does number of output arguments match - no optional output args */
			for (int i = 0; i < proc.sizeOfOutputArray(); i++) {
				outArgs.put(proc.getOutputArray(i).getName(), proc.getOutputArray(i));
			}
			if (!proc.getAnyNumOdOutputArgs() && (call.sizeOfOutputArray() != proc.sizeOfOutputArray()))
				throw new CompilationException("Wrong number of procedure output arguments: specified " + call.sizeOfOutputArray() + 
						" and should be " + proc.sizeOfOutputArray());
			
	
			boolean keywordArgsInput = true;
			for (int i = 0; i < call.sizeOfInputArray(); i++) {
				if (!call.getInputArray(i).isSetBind()) {
					keywordArgsInput = false;
					break;
				}
			} 
			if (proc.getAnyNumOdInputArgs()) {
				/* If procedure can have any number of input args, we don't do typechecking */
				for (int i = 0; i < call.sizeOfInputArray(); i++) {					
					ActualParameter input = call.getInputArray(i);
					StringTemplate argST = actualParameter(input, scope);
					callST.setAttribute("inputs", argST);
				}
			} else if (keywordArgsInput) { 
				/* if ALL arguments are specified by name=value */			
				int noOfMandArgs = 0;
				for (int i = 0; i < call.sizeOfInputArray(); i++) {					
					ActualParameter input = call.getInputArray(i);
					StringTemplate argST = actualParameter(input, scope);
					callST.setAttribute("inputs", argST);
					
					if (!inArgs.containsKey(input.getBind()))
						throw new CompilationException("Formal argument " + input.getBind() + " doesn't exist");
					FormalArgumentSignature formalArg = (FormalArgumentSignature)inArgs.get(input.getBind());
					String formalType = formalArg.getType();
					String actualType = datatype(argST);
					if (!formalArg.isAnyType() && !actualType.equals(formalType))
						throw new CompilationException("Wrong type for parameter number " + i +
								", expected " + formalType + ", got " + actualType);
					
					if (!formalArg.isOptional())
						noOfMandArgs++;
				}
				if (!proc.getAnyNumOdInputArgs() && noOfMandArgs < proc.sizeOfInputArray() - noOfOptInArgs)
					throw new CompilationException("Mandatory argument missing");
			} else { /* Positional arguments */
				/* Checking types of mandatory arguments */
				for (int i = 0; i < proc.sizeOfInputArray() - noOfOptInArgs; i++) {
					ActualParameter input = call.getInputArray(i);
					StringTemplate argST = actualParameter(input, scope);
					callST.setAttribute("inputs", argST);
					
					FormalArgumentSignature formalArg = ((ProcedureSignature)proceduresMap.get(procName)).getInputArray(i);
					String formalType = formalArg.getType();						
					String actualType = datatype(argST);
					if (!formalArg.isAnyType() && !actualType.equals(formalType))
						throw new CompilationException("Wrong type for parameter number " + i +
								", expected " + formalType + ", got " + actualType);					
				}
				/* Checking types of optional arguments */
				for (int i = proc.sizeOfInputArray() - noOfOptInArgs; i < call.sizeOfInputArray(); i++) {
					ActualParameter input = call.getInputArray(i);
					StringTemplate argST = actualParameter(input, scope);
					callST.setAttribute("inputs", argST);
					
					String formalName = input.getBind();					
					if (!inArgs.containsKey(formalName))
						throw new CompilationException("Formal argument " + formalName + " doesn't exist");
					FormalArgumentSignature formalArg = (FormalArgumentSignature)inArgs.get(formalName);
					String formalType = formalArg.getType();
					String actualType = datatype(argST);
					if (!formalArg.isAnyType() && !actualType.equals(formalType))
						throw new CompilationException("Wrong type for parameter " + formalName +
								", expected " + formalType + ", got " + actualType);				
				}
			}
			
			boolean keywordArgsOutput = true;
			for (int i = 0; i < call.sizeOfOutputArray(); i++) {
				if (!call.getOutputArray(i).isSetBind()) {
					keywordArgsOutput = false;
					break;
				}
			} 
			if (proc.getAnyNumOdOutputArgs()) {
				/* If procedure can have any number of output args, we don't do typechecking */
				for (int i = 0; i < call.sizeOfOutputArray(); i++) {					
					ActualParameter output = call.getOutputArray(i);
					StringTemplate argST = actualParameter(output, scope);
					callST.setAttribute("outputs", argST);
					String rootvar = abstractExpressionToRootVariable(call.getOutputArray(i).getAbstractExpression());
					scope.addWriter(rootvar, new Integer(callID++), rootVariableIsPartial(call.getOutputArray(i).getAbstractExpression()));
				}
			}
			if (keywordArgsOutput) { 
				/* if ALL arguments are specified by name=value */			
				for (int i = 0; i < call.sizeOfOutputArray(); i++) {					
					ActualParameter output = call.getOutputArray(i);
					StringTemplate argST = actualParameter(output, scope);
					callST.setAttribute("outputs", argST);
					
					if (!outArgs.containsKey(output.getBind()))
						throw new CompilationException("Formal argument " + output.getBind() + " doesn't exist");
					FormalArgumentSignature formalArg = (FormalArgumentSignature)outArgs.get(output.getBind());
					String formalType = formalArg.getType();
					String actualType = datatype(argST);
					if (!formalArg.isAnyType() && !actualType.equals(formalType))
						throw new CompilationException("Wrong type for output parameter number " + i +
								", expected " + formalType + ", got " + actualType);
					
					String rootvar = abstractExpressionToRootVariable(call.getOutputArray(i).getAbstractExpression());
					scope.addWriter(rootvar, new Integer(callID++), rootVariableIsPartial(call.getOutputArray(i).getAbstractExpression()));
				}					
			} else { /* Positional arguments */
				for (int i = 0; i < call.sizeOfOutputArray(); i++) {
					ActualParameter output = call.getOutputArray(i);
					StringTemplate argST = actualParameter(output, scope);
					callST.setAttribute("outputs", argST);
					
					FormalArgumentSignature formalArg =((ProcedureSignature)proceduresMap.get(procName)).getOutputArray(i); 
					String formalType = formalArg.getType();
						
					/* type check positional output args */
					String actualType = datatype(argST);
					if (!formalArg.isAnyType() && !actualType.equals(formalType))
						throw new CompilationException("Wrong type for parameter number " + i +
								", expected " + formalType + ", got " + actualType);
						
					String rootvar = abstractExpressionToRootVariable(call.getOutputArray(i).getAbstractExpression());
					scope.addWriter(rootvar, new Integer(callID++), rootVariableIsPartial(call.getOutputArray(i).getAbstractExpression()));
				}
			}					
			
			scope.appendStatement(callST);
		} catch(CompilationException ce) {
			throw new CompilationException("Compile error in procedure invocation at "+call.getSrc()+": "+ce.getMessage(),ce);
		}
	}

	public void iterateStat(Iterate iterate, VariableScope scope) throws CompilationException {
		VariableScope loopScope = new VariableScope(this, scope, VariableScope.ENCLOSURE_LOOP);
		VariableScope innerScope = new VariableScope(this, loopScope, VariableScope.ENCLOSURE_LOOP);

		loopScope.addVariable(iterate.getVar(), "int");

		StringTemplate iterateST = template("iterate");

		XmlObject cond = iterate.getAbstractExpression();
		StringTemplate condST = expressionToKarajan(cond, loopScope);
		iterateST.setAttribute("cond", condST);
		iterateST.setAttribute("var", iterate.getVar());
		innerScope.bodyTemplate = iterateST;

		statements(iterate.getBody(), innerScope);

		Object statementID = new Integer(callID++);
		Iterator scopeIterator = innerScope.getVariableIterator();
		while(scopeIterator.hasNext()) {
			String v=(String) scopeIterator.next();
			scope.addWriter(v, statementID, false);
		}
		scope.appendStatement(iterateST);
	}

	public void foreachStat(Foreach foreach, VariableScope scope) throws CompilationException {
		try {
			VariableScope innerScope = new VariableScope(this, scope, VariableScope.ENCLOSURE_LOOP);

			StringTemplate foreachST = template("foreach");
			foreachST.setAttribute("var", foreach.getVar());
			
			XmlObject in = foreach.getIn().getAbstractExpression();
			StringTemplate inST = expressionToKarajan(in, scope);
			foreachST.setAttribute("in", inST);
			
			String inType = datatype(inST);
			if (inType.length() < 2 || !inType.substring(inType.length() - 2).equals("[]"))
				throw new CompilationException("You can iterate through an array structure only");
			String varType = inType.substring(0, inType.length() - 2);			
			innerScope.addVariable(foreach.getVar(), varType);
			foreachST.setAttribute("indexVar", foreach.getIndexVar());
			if(foreach.getIndexVar() != null) {
				innerScope.addVariable(foreach.getIndexVar(), "int");
			}			

			innerScope.bodyTemplate = foreachST;

			statements(foreach.getBody(), innerScope);

			Object statementID = new Integer(callID++);
			Iterator scopeIterator = innerScope.getVariableIterator();
			while(scopeIterator.hasNext()) {
				String v=(String) scopeIterator.next();
				scope.addWriter(v, statementID, true);
			}
			scope.appendStatement(foreachST);
		} catch(CompilationException re) {
			throw new CompilationException("Compile error in foreach statement at "+foreach.getSrc()+": "+re.getMessage(),re);
		}

	}

	public void ifStat(If ifstat, VariableScope scope) throws CompilationException {
		StringTemplate ifST = template("if");
		StringTemplate conditionST = expressionToKarajan(ifstat.getAbstractExpression(), scope);
		ifST.setAttribute("condition", conditionST.toString());
		if (!datatype(conditionST).equals("boolean"))
			throw new CompilationException ("Condition in if statement has to be of boolean type.");
		
		Then thenstat = ifstat.getThen();
		Else elsestat = ifstat.getElse();

		VariableScope innerThenScope = new VariableScope(this, scope);
		innerThenScope.bodyTemplate = template("sub_comp");
		ifST.setAttribute("vthen", innerThenScope.bodyTemplate);

		statements(thenstat, innerThenScope);

		Object statementID = new Integer(callID++);

		Iterator thenScopeIterator = innerThenScope.getVariableIterator();
		while(thenScopeIterator.hasNext()) {
			String v=(String) thenScopeIterator.next();
			scope.addWriter(v, statementID, true);
		}

		if (elsestat != null) {

			VariableScope innerElseScope = new VariableScope(this, scope);
			innerElseScope.bodyTemplate = template("sub_comp");
			ifST.setAttribute("velse", innerElseScope.bodyTemplate);

			statements(elsestat, innerElseScope);

			Iterator elseScopeIterator = innerElseScope.getVariableIterator();
			while(elseScopeIterator.hasNext()) {
				String v=(String) elseScopeIterator.next();
				scope.addWriter(v, statementID, true);
			}
		}
		scope.appendStatement(ifST);
	}

	public void switchStat(Switch switchstat, VariableScope scope) throws CompilationException {
		StringTemplate switchST = template("switch");
		Object statementID = new Integer(callID++);
		scope.bodyTemplate.setAttribute("statements", switchST);
		StringTemplate conditionST = expressionToKarajan(switchstat.getAbstractExpression(), scope);
		switchST.setAttribute("condition", conditionST.toString());
		
		/* Check if switch statement can be anything apart from int and float */
		if (!datatype(conditionST).equals("int") && !datatype(conditionST).equals("float"))
			throw new CompilationException("Condition in switch statements has to be of numeric type.");

		for (int i=0; i< switchstat.sizeOfCaseArray(); i++) {
			Case casestat = switchstat.getCaseArray(i);
			VariableScope caseScope = new VariableScope(this, scope);
			caseScope.bodyTemplate = new StringTemplate("case");
			switchST.setAttribute("cases", caseScope.bodyTemplate);
			
			caseStat(casestat, caseScope);

			Iterator caseScopeIterator = caseScope.getVariableIterator();
			while(caseScopeIterator.hasNext()) {
				String v=(String) caseScopeIterator.next();
				scope.addWriter(v, statementID, true);
			}

		}
		Default defaultstat = switchstat.getDefault();
		if (defaultstat != null) {
			VariableScope defaultScope = new VariableScope(this, scope);
			defaultScope.bodyTemplate = template("sub_comp");
			switchST.setAttribute("sdefault", defaultScope.bodyTemplate);
			statements(defaultstat, defaultScope);
			Iterator defaultScopeIterator = defaultScope.getVariableIterator();
			while(defaultScopeIterator.hasNext()) {
				String v=(String) defaultScopeIterator.next();
				scope.addWriter(v, statementID, true);
			}
		}
	}

	public void caseStat(Case casestat, VariableScope scope) throws CompilationException {
		StringTemplate valueST = expressionToKarajan(casestat.getAbstractExpression(), scope);
		scope.bodyTemplate.setAttribute("value", valueST.toString());
		statements(casestat.getStatements(), scope);
	}

	public StringTemplate actualParameter(ActualParameter arg, VariableScope scope) throws CompilationException {
		StringTemplate argST = template("call_arg");
		StringTemplate expST = expressionToKarajan(arg.getAbstractExpression(), scope);
		argST.setAttribute("bind", arg.getBind());
		argST.setAttribute("expr", expST);
		argST.setAttribute("datatype", datatype(expST));
		return argST;
	}

	public void binding(Binding bind, StringTemplate procST, VariableScope scope) throws CompilationException {
		StringTemplate bindST = new StringTemplate("binding");
		ApplicationBinding app;
		if ((app = bind.getApplication()) != null) {
			bindST.setAttribute("application", application(app, scope));
			procST.setAttribute("binding", bindST);
		} else throw new CompilationException("Unknown binding: "+bind);
	}

	public StringTemplate application(ApplicationBinding app, VariableScope scope) throws CompilationException {
		StringTemplate appST = new StringTemplate("application");
		appST.setAttribute("exec", app.getExecutable());
		for (int i = 0; i < app.sizeOfAbstractExpressionArray(); i++) {
			XmlObject argument = app.getAbstractExpressionArray(i);
			StringTemplate argumentST = expressionToKarajan(argument, scope);
			String type = datatype(argumentST);
			if(type.equals("string") || type.equals("string[]")
			 || type.equals("int") || type.equals("float") 
			 || type.equals("int[]") || type.equals("float[]") 
			 || type.equals("boolean") || type.equals("boolean[]")) {
				appST.setAttribute("arguments", argumentST);
			} else {
				throw new CompilationException("Cannot pass type '"+type+"' as a parameter to application '"+app.getExecutable()+"' at "+app.getSrc());
			}
		}
		if(app.getStdin()!=null)
			appST.setAttribute("stdin", expressionToKarajan(app.getStdin().getAbstractExpression(), scope));
		if(app.getStdout()!=null)
			appST.setAttribute("stdout", expressionToKarajan(app.getStdout().getAbstractExpression(), scope));
		if(app.getStderr()!=null)
			appST.setAttribute("stderr", expressionToKarajan(app.getStderr().getAbstractExpression(), scope));
		return appST;
	}

	/** Produces a Karajan function invocation from a SwiftScript invocation.
	  * The Karajan invocation will have the same name as the SwiftScript
	  * function, in the 'vdl' Karajan namespace. Parameters to the
	  * Karajan function will differ from the SwiftScript parameters in
	  * a number of ways - read the source for the exact ways in which
	  * that happens.
	  */

	public StringTemplate function(Function func, VariableScope scope) throws CompilationException {
		StringTemplate funcST = template("function");
		funcST.setAttribute("name", func.getName());
		ProcedureSignature funcSignature =  (ProcedureSignature) functionsMap.get(func.getName()); 
		if(funcSignature == null) {
			throw new CompilationException("Unknown function: @"+func.getName());
		}
		XmlObject[] arguments = func.getAbstractExpressionArray();
		int noOfOptInArgs = 0;
		for (int i = 0; i < funcSignature.sizeOfInputArray(); i++) {
			if (funcSignature.getInputArray(i).isOptional())
				noOfOptInArgs++;	
		}				
		if (!funcSignature.getAnyNumOdInputArgs() && 
			(arguments.length < funcSignature.sizeOfInputArray() - noOfOptInArgs ||
			 arguments.length > funcSignature.sizeOfInputArray()))
			throw new CompilationException("Wrong number of function input arguments: specified " + 
					arguments.length + " and should be " + funcSignature.sizeOfInputArray());
				
		for(int i = 0; i < arguments.length; i++ ) {
			StringTemplate exprST = expressionToKarajan(arguments[i], scope);
			funcST.setAttribute("args", exprST);
			
			/* Type check of function arguments */
			if (!funcSignature.getAnyNumOdInputArgs()) {
				String actualType = datatype(exprST);
				FormalArgumentSignature fas = funcSignature.getInputArray(i);
				if (!fas.isAnyType() && !fas.getType().equals(actualType)) 
					throw new CompilationException("Wrong type for parameter number " + i +
							", expected " + fas.getType() + ", got " + actualType);
				}
		}
		
		return funcST;
	}

	static final String SWIFTSCRIPT_NS = "http://ci.uchicago.edu/swift/2009/02/swiftscript";

	static final QName OR_EXPR = new QName(SWIFTSCRIPT_NS, "or");
	static final QName AND_EXPR = new QName(SWIFTSCRIPT_NS, "and");
	static final QName BOOL_EXPR = new QName(SWIFTSCRIPT_NS, "booleanConstant");
	static final QName INT_EXPR = new QName(SWIFTSCRIPT_NS, "integerConstant");
	static final QName FLOAT_EXPR = new QName(SWIFTSCRIPT_NS, "floatConstant");
	static final QName STRING_EXPR = new QName(SWIFTSCRIPT_NS, "stringConstant");
	static final QName COND_EXPR = new QName(SWIFTSCRIPT_NS, "cond");
	static final QName ARITH_EXPR = new QName(SWIFTSCRIPT_NS, "arith");
	static final QName UNARY_NEGATION_EXPR = new QName(SWIFTSCRIPT_NS, "unaryNegation");
	static final QName NOT_EXPR = new QName(SWIFTSCRIPT_NS, "not");
	static final QName VARIABLE_REFERENCE_EXPR = new QName(SWIFTSCRIPT_NS, "variableReference");
	static final QName ARRAY_SUBSCRIPT_EXPR = new QName(SWIFTSCRIPT_NS, "arraySubscript");
	static final QName STRUCTURE_MEMBER_EXPR = new QName(SWIFTSCRIPT_NS, "structureMember");
	static final QName ARRAY_EXPR = new QName(SWIFTSCRIPT_NS, "array");
	static final QName RANGE_EXPR = new QName(SWIFTSCRIPT_NS, "range");
	static final QName FUNCTION_EXPR = new QName(SWIFTSCRIPT_NS, "function");

	/** converts an XML intermediate form expression into a
	 *  Karajan expression.
	 */
	public StringTemplate expressionToKarajan(XmlObject expression, VariableScope scope) throws CompilationException
	{
		Node expressionDOM = expression.getDomNode();
		String namespaceURI = expressionDOM.getNamespaceURI();
		String localName = expressionDOM.getLocalName();
		QName expressionQName = new QName(namespaceURI, localName);

		if(expressionQName.equals(OR_EXPR))	{
			StringTemplate st = template("or");
			BinaryOperator o = (BinaryOperator)expression;
			StringTemplate leftST = expressionToKarajan(o.getAbstractExpressionArray(0), scope);
			StringTemplate rightST = expressionToKarajan(o.getAbstractExpressionArray(1), scope);
			st.setAttribute("left", leftST);
			st.setAttribute("right", rightST);
			if (datatype(leftST).equals("boolean") && datatype(rightST).equals("boolean"))
				st.setAttribute("datatype", "boolean");
			else 
				throw new CompilationException("Or operation can only be applied to parameters of type boolean.");
			return st;
		} else if (expressionQName.equals(AND_EXPR)) {
			StringTemplate st = template("and");
			BinaryOperator o = (BinaryOperator)expression;
			StringTemplate leftST = expressionToKarajan(o.getAbstractExpressionArray(0), scope);
			StringTemplate rightST = expressionToKarajan(o.getAbstractExpressionArray(1), scope);
			st.setAttribute("left", leftST);
			st.setAttribute("right", rightST);
			if (datatype(leftST).equals("boolean") && datatype(rightST).equals("boolean"))
				st.setAttribute("datatype", "boolean");
			else 
				throw new CompilationException("And operation can only be applied to parameters of type boolean.");
			return st;
		} else if (expressionQName.equals(BOOL_EXPR)) {
			XmlBoolean xmlBoolean = (XmlBoolean) expression;
			boolean b = xmlBoolean.getBooleanValue();
			StringTemplate st = template("bConst");
			st.setAttribute("value",""+b);
			st.setAttribute("datatype", "boolean");			
			return st;
		} else if (expressionQName.equals(INT_EXPR)) {
			XmlInt xmlInt = (XmlInt) expression;
			int i = xmlInt.getIntValue();
			Integer iobj = new Integer(i);
			String internedID;
			if(intInternMap.get(iobj) == null) {
				internedID = "swift#int#" + i;
				intInternMap.put(iobj, internedID);
			} else {
				internedID = (String)intInternMap.get(iobj);
			}
			StringTemplate st = template("id");
			st.setAttribute("var", internedID);
			st.setAttribute("datatype", "int");
			return st;
		} else if (expressionQName.equals(FLOAT_EXPR)) {
			XmlFloat xmlFloat = (XmlFloat) expression;
			float f = xmlFloat.getFloatValue();
			Float fobj = new Float(f);
			String internedID;
			if(floatInternMap.get(fobj) == null) {
				internedID = "swift#float#" + (internedIDCounter++);
				floatInternMap.put(fobj, internedID);
			} else {
				internedID = (String)floatInternMap.get(fobj);
			}
			StringTemplate st = template("id");
			st.setAttribute("var",internedID);
			st.setAttribute("datatype", "float");
			return st;
		} else if (expressionQName.equals(STRING_EXPR)) {
			XmlString xmlString = (XmlString) expression;
			String s = xmlString.getStringValue();
			String internedID;
			if(stringInternMap.get(s) == null) {
				internedID = "swift#string#" + (internedIDCounter++);
				stringInternMap.put(s,internedID);
			} else {
				internedID = (String)stringInternMap.get(s);
			}
			StringTemplate st = template("id");
			st.setAttribute("var",internedID);
			st.setAttribute("datatype", "string");
			return st;
		} else if (expressionQName.equals(COND_EXPR)) {
			StringTemplate st = template("binaryop");
			LabelledBinaryOperator o = (LabelledBinaryOperator) expression;
			StringTemplate leftST = expressionToKarajan(o.getAbstractExpressionArray(0), scope);
			StringTemplate rightST = expressionToKarajan(o.getAbstractExpressionArray(1), scope);
			st.setAttribute("op", o.getOp());
			st.setAttribute("left", leftST);
			st.setAttribute("right", rightST); 
			
			checkTypesInCondExpr(o.getOp(), datatype(leftST), datatype(rightST), st);			
			return st;
		} else if (expressionQName.equals(ARITH_EXPR)) {
			LabelledBinaryOperator o = (LabelledBinaryOperator) expression;
			StringTemplate st = template("binaryop");
			st.setAttribute("op", o.getOp());
			StringTemplate leftST = expressionToKarajan(o.getAbstractExpressionArray(0), scope);
			StringTemplate rightST = expressionToKarajan(o.getAbstractExpressionArray(1), scope);
			st.setAttribute("left", leftST);
			st.setAttribute("right", rightST);		
			
			checkTypesInArithmExpr(o.getOp(), datatype(leftST), datatype(rightST), st);			
			return st;
		} else if (expressionQName.equals(UNARY_NEGATION_EXPR)) {
			UnlabelledUnaryOperator e = (UnlabelledUnaryOperator) expression;
			StringTemplate st = template("unaryNegation");
			StringTemplate expST = expressionToKarajan(e.getAbstractExpression(), scope);
			st.setAttribute("exp", expST);
			if (!(datatype(expST).equals("float")) && !(datatype(expST).equals("int")))
				throw new CompilationException("Negation operation can only be applied to parameter of numeric types.");
			st.setAttribute("datatype", datatype(expST));
			return st;
		} else if (expressionQName.equals(NOT_EXPR)) {
			// TODO not can probably merge with 'unary'
			UnlabelledUnaryOperator e = (UnlabelledUnaryOperator)expression;
			StringTemplate st = template("not");
			StringTemplate expST = expressionToKarajan(e.getAbstractExpression(), scope);
			st.setAttribute("exp", expST);
			if (datatype(expST).equals("boolean"))
				st.setAttribute("datatype", "boolean");
			else 
				throw new CompilationException("Not operation can only be applied to parameter of type boolean.");
			return st;
		} else if (expressionQName.equals(VARIABLE_REFERENCE_EXPR)) {
			XmlString xmlString = (XmlString) expression;
			String s = xmlString.getStringValue();
			if(!scope.isVariableDefined(s)) {
				throw new CompilationException("Variable " + s + " is undefined.");
			}
			StringTemplate st = template("id");
			st.setAttribute("var", s);
			String actualType;
			
			actualType = scope.getVariableType(s);
			st.setAttribute("datatype", actualType);
			return st;
		} else if (expressionQName.equals(ARRAY_SUBSCRIPT_EXPR)) {
			BinaryOperator op = (BinaryOperator) expression;
			StringTemplate newst = template("extractarrayelement");
			StringTemplate arrayST = expressionToKarajan(op.getAbstractExpressionArray(1), scope);
			StringTemplate parentST = expressionToKarajan(op.getAbstractExpressionArray(0), scope);
			newst.setAttribute("arraychild", arrayST);
			newst.setAttribute("parent", parentST);
			
			String arrayType = datatype(parentST); 
			if (datatype(arrayST).equals("string")) {
				XmlString var = (XmlString) op.getAbstractExpressionArray(1);
				if (!var.getStringValue().equals("*"))
				   throw new CompilationException("Array index must be of type int, or *.");
				newst.setAttribute("datatype", arrayType);
			} else if (datatype(arrayST).equals("int")) {
				newst.setAttribute("datatype", arrayType.substring(0, arrayType.length()-2));
			} else {
				throw new CompilationException("Array index must be of type int.");
			}
			return newst;
		} else if (expressionQName.equals(STRUCTURE_MEMBER_EXPR)) {
			StructureMember sm = (StructureMember) expression;
			StringTemplate newst = template("extractarrayelement");
			StringTemplate parentST = expressionToKarajan(sm.getAbstractExpression(), scope);
			newst.setAttribute("parent", parentST);
			newst.setAttribute("memberchild", sm.getMemberName());
			
			String parentType = datatype(parentST);
			String actualType = null;					
			for (int i = 0; i < types.sizeOfTypeArray(); i++) {
				if (types.getTypeArray(i).getTypename().equals(parentType)) {
					TypeStructure ts = types.getTypeArray(i).getTypestructure();
					int j = 0;
					for (j = 0; j < ts.sizeOfMemberArray(); j++)			
						if (ts.getMemberArray(j).getMembername().equals(sm.getMemberName())) {
							actualType = ts.getMemberArray(j).getMembertype();
							break;
						}					
					if (j == ts.sizeOfMemberArray())
						throw new CompilationException("No member " + sm.getMemberName() 
								+ " in structure " + parentType);					
					break;
				}
			}			
			if (actualType == null) {
				throw new CompilationException("Type " + parentType + " is not defined.");
			}
			newst.setAttribute("datatype", actualType); 
			return newst;
			// TODO the template layout for this and ARRAY_SUBSCRIPT are
			// both a bit convoluted for historical reasons.
			// should be straightforward to tidy up.
		} else if (expressionQName.equals(ARRAY_EXPR)) {
			Array array = (Array)expression;
			StringTemplate st = template("array");
			String elemType = "";
			for (int i = 0; i < array.sizeOfAbstractExpressionArray(); i++) {
				XmlObject expr = array.getAbstractExpressionArray(i);
				StringTemplate elemST = expressionToKarajan(expr, scope);
				if (i == 0)
					elemType = datatype(elemST);
				else if (!elemType.equals(datatype(elemST)))
					throw new CompilationException("Wrong array element type.");
				st.setAttribute("elements", elemST);				
			}
			if (elemType.equals(""))
				System.err.println("WARNING: Empty array constant");
			st.setAttribute("datatype", elemType + "[]");
			return st;
		} else if (expressionQName.equals(RANGE_EXPR)) {
			Range range = (Range)expression;
			StringTemplate st = template("range");
			StringTemplate fromST = expressionToKarajan(range.getAbstractExpressionArray(0), scope);
			StringTemplate toST = expressionToKarajan(range.getAbstractExpressionArray(1), scope);
			st.setAttribute("from", fromST);
			st.setAttribute("to", toST);
			StringTemplate stepST = null;
			if(range.sizeOfAbstractExpressionArray() == 3) {// step is optional
				stepST = expressionToKarajan(range.getAbstractExpressionArray(2), scope);
				st.setAttribute("step", stepST);
			}
			
			String fromType = datatype(fromST);
			String toType = datatype(toST);
			if (stepST == null && (!fromType.equals("int") || !toType.equals("int")))
				throw new CompilationException("Step in range specification can be omitted only when from and to types are int");
			else if ((fromType.equals("int") && toType.equals("int")) && 
					(stepST == null || datatype(stepST).equals("int")))
				st.setAttribute("datatype", "int[]");
			else if (fromType.equals("float") && toType.equals("float") && 
					datatype(stepST).equals("float"))
				st.setAttribute("datatype", "float[]");
			else 
				throw new CompilationException("Range can only be specified with numeric types");
			return st;
		} else if (expressionQName.equals(FUNCTION_EXPR)) {
			Function f = (Function) expression;
			StringTemplate st = function(f, scope);
			/* Set function output type */
			String name = f.getName();
			ProcedureSignature funcSignature = (ProcedureSignature) functionsMap.get(name);
			if (funcSignature != null) {
				/* Functions have only one output parameter */
				st.setAttribute("datatype", funcSignature.getOutputArray(0).getType());
			} else
				throw new CompilationException("Function " + name + " is not defined.");
			return st;
		} else {
			throw new CompilationException("unknown expression implemented by class "+expression.getClass()+" with node name "+expressionQName +" and with content "+expression);
		}
		// perhaps one big throw catch block surrounding body of this method 
		// which shows Compiler Exception and line number of error
	}

	void checkTypesInCondExpr(String op, String left, String right, StringTemplate st)
			throws CompilationException {
		if (left.equals(right))
			st.setAttribute("datatype", "boolean");
		else 
			throw new CompilationException("Conditional operator can only be applied to parameters of same type.");
		
		if ((op.equals("==") || op.equals("!=")) && !left.equals("int") && !left.equals("float") 
				                                 && !left.equals("string") && !left.equals("boolean"))
			throw new CompilationException("Conditional operator " + op + 
					" can only be applied to parameters of type int, float, string and boolean.");
		
		if ((op.equals("<=") || op.equals(">=") || op.equals(">") || op.equals("<")) 
				&& !left.equals("int") && !left.equals("float") && !left.equals("string"))
			throw new CompilationException("Conditional operator " + op + 
			" can only be applied to parameters of type int, float and string.");
	}
	
	void checkTypesInArithmExpr(String op, String left, String right, StringTemplate st)
			throws CompilationException {
		if (left.equals(right))
			st.setAttribute("datatype", left);
		else 
			throw new CompilationException("Arithmetic operation can only be applied to parameters of same type.");
		
		if (op.equals("+") && !left.equals("int") && !left.equals("float") && !left.equals("string"))
				throw new CompilationException("Arithmetic operation + can only be applied to parameters of type int, float and string.");
		if ((op.equals("-") || op.equals("*")) && !left.equals("int") && !left.equals("float"))
			throw new CompilationException("Arithmetic operation " + op + 
					" can only be applied to parameters of type int and float.");
		if (op.equals("/") && !left.equals("float"))
				throw new CompilationException("Arithmetic operation / can only be applied to parameters of type float.");
		if (op.equals("%/") && !left.equals("int"))
			throw new CompilationException("Arithmetic operation %/ can only be applied to parameters of type int.");
		if (op.equals("%%") && !left.equals("int"))
			throw new CompilationException("Arithmetic operation %% can only be applied to parameters of type int.");
	}

	public String abstractExpressionToRootVariable(XmlObject expression) throws CompilationException {
		Node expressionDOM = expression.getDomNode();
		String namespaceURI = expressionDOM.getNamespaceURI();
		String localName = expressionDOM.getLocalName();
		QName expressionQName = new QName(namespaceURI, localName);
		if (expressionQName.equals(VARIABLE_REFERENCE_EXPR)) {
			XmlString xmlString = (XmlString) expression;
			String s = xmlString.getStringValue();
			return s;
		} else if (expressionQName.equals(ARRAY_SUBSCRIPT_EXPR)) {
			BinaryOperator op = (BinaryOperator) expression;
			return abstractExpressionToRootVariable(op.getAbstractExpressionArray(0));
		} else if (expressionQName.equals(STRUCTURE_MEMBER_EXPR)) {
			StructureMember sm = (StructureMember) expression;
			return abstractExpressionToRootVariable(sm.getAbstractExpression());
		} else {
			throw new CompilationException("Could not find root for abstract expression.");
		}
	}

	public boolean rootVariableIsPartial(XmlObject expression) {
		Node expressionDOM = expression.getDomNode();
		String namespaceURI = expressionDOM.getNamespaceURI();
		String localName = expressionDOM.getLocalName();
		QName expressionQName = new QName(namespaceURI, localName);
		if (expressionQName.equals(VARIABLE_REFERENCE_EXPR)) {
			return false;
		} else if (expressionQName.equals(ARRAY_SUBSCRIPT_EXPR)) {
			return true;
		} else if (expressionQName.equals(STRUCTURE_MEMBER_EXPR)) {
			return true;
		} else {
			throw new RuntimeException("Could not find root for abstract expression.");
		}
	}

	public void generateInternedConstants(StringTemplate programTemplate) {

		Iterator i;
		i = stringInternMap.keySet().iterator();
		while(i.hasNext()) {
			String str = (String)i.next();
			String variableName = (String)stringInternMap.get(str);
			StringTemplate st = template("sConst");
			st.setAttribute("innervalue",escapeQuotes(str));
			StringTemplate vt = template("globalConstant");
			vt.setAttribute("name",variableName);
			vt.setAttribute("expr",st);
			programTemplate.setAttribute("constants",vt);
		}

		i = intInternMap.keySet().iterator();
		while(i.hasNext()) {
			Integer key = (Integer)i.next();
			String variableName = (String)intInternMap.get(key);
			StringTemplate st = template("iConst");
			st.setAttribute("value",key);
			StringTemplate vt = template("globalConstant");
			vt.setAttribute("name",variableName);
			vt.setAttribute("expr",st);
			programTemplate.setAttribute("constants",vt);
		}

		i = floatInternMap.keySet().iterator();
		while(i.hasNext()) {
			Float key = (Float)i.next();
			String variableName = (String)floatInternMap.get(key);
			StringTemplate st = template("fConst");
			st.setAttribute("value",key);
			StringTemplate vt = template("globalConstant");
			vt.setAttribute("name",variableName);
			vt.setAttribute("expr",st);
			programTemplate.setAttribute("constants",vt);
		}


	}

	String escapeQuotes(String in) {
		return in.replaceAll("\"", "&quot;");
	}
	
	String datatype(StringTemplate st) {
		return st.getAttribute("datatype").toString();
	}

}
