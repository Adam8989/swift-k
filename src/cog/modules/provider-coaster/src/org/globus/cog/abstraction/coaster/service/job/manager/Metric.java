//----------------------------------------------------------------------
//This code is developed as part of the Java CoG Kit project
//The terms of the license can be found at http://www.cogkit.org/license
//This message may not be removed or altered.
//----------------------------------------------------------------------

/*
 * Created on Aug 10, 2009
 */
package org.globus.cog.abstraction.coaster.service.job.manager;

public interface Metric {

    Metric NULL_METRIC = new NullMetric();

    double blockSize(int slot, int cslots, double tsum);

    double getSize(Job j);

    double desiredSize(Job j);

    double size(int w, int h);
    
    public class NullMetric implements Metric {
        public double blockSize(int slot, int cslots, double tsum) {
            return 0;
        }

        public double desiredSize(Job j) {
            return 0;
        }

        public double getSize(Job j) {
            return 0;
        }

        public double size(int w, int h) {
            return 0;
        }
        
    }
}
