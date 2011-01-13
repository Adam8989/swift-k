//----------------------------------------------------------------------
//This code is developed as part of the Java CoG Kit project
//The terms of the license can be found at http://www.cogkit.org/license
//This message may not be removed or altered.
//----------------------------------------------------------------------

package org.globus.cog.gridface.impl.desktop.interfaces;


public interface MouseActionProxy extends ActionProxy {
	public void mouseClicked(Object source, int clickCount,boolean isLeftMouseClick,int xLoc, int yLoc);
}