//----------------------------------------------------------------------
//This code is developed as part of the Java CoG Kit project
//The terms of the license can be found at http://www.cogkit.org/license
//This message may not be removed or altered.
//----------------------------------------------------------------------

/*
 * Created on Sep 24, 2008
 */
package org.globus.cog.abstraction.impl.file.coaster.commands;

import org.globus.cog.karajan.workflow.service.commands.Command;

public class RmdirCommand extends Command {
    public static final String NAME = "RMDIR"; 

    public RmdirCommand(String name, boolean force) {
        super(NAME);
        addOutData(name);
        addOutData(force);
    }
}