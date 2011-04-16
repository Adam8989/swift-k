//----------------------------------------------------------------------
//This code is developed as part of the Java CoG Kit project
//The terms of the license can be found at http://www.cogkit.org/license
//This message may not be removed or altered.
//----------------------------------------------------------------------

/*
 * Created on Sep 26, 2008
 */
package org.globus.cog.abstraction.impl.file.coaster.handlers;

import java.io.File;

import org.globus.cog.karajan.workflow.service.ProtocolException;

public class MkdirHandler extends CoasterFileRequestHandler {

    public void requestComplete() throws ProtocolException {
    	File f = normalize(getInDataAsString(0));
    	
    	if (!f.mkdirs() && !f.exists()) {
    		sendError("Failed to create directory " + f.getPath());
    	}
    	else {
    		sendReply("OK");
    	}
    }

}