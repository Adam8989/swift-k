/*
 * Copyright 2012 University of Chicago
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Created on Apr 19, 2015
 */
package org.globus.swift.parsetree;

import java.util.List;

public class IfStatement extends Statement {
    private Expression condition;
    private final StatementBlock thenScope;
    private final StatementBlock elseScope;
    
    public IfStatement() {
        thenScope = new StatementBlock();
        elseScope = new StatementBlock();
    }

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public StatementBlock getThenScope() {
        return thenScope;
    }

    public StatementBlock getElseScope() {
        return elseScope;
    }

    @Override
    public List<? extends Node> getSubNodes() {
        return list(condition, thenScope, elseScope);
    }
    
    @Override
    public String getNodeName() {
        return "if statement";
    }
}
