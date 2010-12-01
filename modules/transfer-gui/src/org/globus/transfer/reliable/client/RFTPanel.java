/*
 * RFTPanel.java
 *
 * 
 */

package org.globus.transfer.reliable.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.globus.ogce.beans.filetransfer.gui.monitor.QueuePanel;


public class RFTPanel extends javax.swing.JPanel {
    private int jobID = 0;
    private JFrame frame = null;
    /** Creates new form RFTPanel */
    public RFTPanel() {
        initComponents();
        actionListener = new RFTButtonActionListener(this);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rftQueuePanel = new org.globus.ogce.beans.filetransfer.gui.monitor.QueuePanel();
        rftQueuePanel.createHeader(new String[]{"Jobid",  "From", "To", "Status", "%", "Errors"});
        rftQueuePanel.addPopupItems(new String[]{"Info", "Cancel", "Delete"}, new ButtonActionListener());
    rFTOptions1 = new RFTOptionsPanel();
    rFTTransferParam1 = new RFTTransferParamPanel();
    startButton = new javax.swing.JButton();
    stopButton = new javax.swing.JButton();
    okButton = new javax.swing.JButton();

    setAutoscrolls(true);
    setName("Form"); // NOI18N

    rftQueuePanel.setName("rftQueuePanel"); // NOI18N

    rFTOptions1.setName("rFTOptions1"); // NOI18N

    rFTTransferParam1.setName("rFTTransferParam1"); // NOI18N

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.globus.transfer.reliable.client.GridFTPGUIApp.class).getContext().getResourceMap(RFTPanel.class);
    startButton.setText(resourceMap.getString("startButton.text")); // NOI18N
    startButton.setName("startButton"); // NOI18N
    startButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            startButtonActionPerformed(evt);
        }
    });

    stopButton.setText(resourceMap.getString("stopButton.text")); // NOI18N
    stopButton.setName("stopButton"); // NOI18N
    stopButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            stopButtonActionPerformed(evt);
        }
    });

    okButton.setText(resourceMap.getString("restartButton.text")); // NOI18N
    okButton.setName("restartButton"); // NOI18N
    okButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            okButtonActionPerformed(evt);
        }
    });
   
    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()          
            .add(rFTOptions1, 100, 300, 400)
            .addContainerGap())        
            .add(rFTTransferParam1, 100, 300, 400)
        .add(layout.createSequentialGroup()
            .add(200, 200, 200)
            .add(okButton)          
            )
    );

    layout.linkSize(new java.awt.Component[] {okButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

    layout.setVerticalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(layout.createSequentialGroup()
            .add(rFTOptions1, 80, 100, 130)           
            .add(rFTTransferParam1, 150, 200, 250)
            .add(18, 18, 18)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
            .add(okButton))
            .add(92, 92, 92))
    );
    

    }// </editor-fold>//GEN-END:initComponents

    
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
    	new Thread(new Runnable() {
    		public void run() {
    			RFTTransferParam param = new RFTTransferParam(rFTTransferParam1.getFrom(),
    					rFTTransferParam1.getTo(), rFTTransferParam1.getServerHost(),
    					rFTTransferParam1.getServerPort());
    			RFTOptions options = new RFTOptions(rFTTransferParam1.getConcurrent(),
    					rFTOptions1.getParallelStream(), rFTOptions1.getTcpBufferSize(),
    					rFTOptions1.getDestSN(), rFTOptions1.getSourceSN());
    	        RFTJob job = new RFTJob(++jobID, options, param);
    	        try {
					actionListener.startButtonAction(job, rftQueuePanel);
				} catch (Exception e) {
                    JOptionPane.showMessageDialog(null,e.getMessage(), "Error",
                            JOptionPane.WARNING_MESSAGE);
                    int index = rftQueuePanel.getRowIndex(Integer.toString(jobID));
                    rftQueuePanel.setColumnValue(index, 3, "Failed");
                    rftQueuePanel.setColumnValue(index, 5, e.getMessage());
					e.printStackTrace();
				}
    		}
    	}).start();
    }//GEN-LAST:event_startButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
    	new Thread(new Runnable() {
    		public void run() {
    			String jobID = rftQueuePanel.getSelectedJob();
    	        try {
					actionListener.stopButtonAction(jobID);
				} catch (Exception e) {
                    JOptionPane.showMessageDialog(null,e.getMessage(), "Error",
                            JOptionPane.WARNING_MESSAGE);
                    int index = rftQueuePanel.getRowIndex(jobID, 0);
                    rftQueuePanel.setColumnValue(index, 4, "Failed");
                    rftQueuePanel.setColumnValue(index, 6, e.getMessage());
					e.printStackTrace();
				}
    		}
    	}).start();
    }//GEN-LAST:event_stopButtonActionPerformed
    
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
    	Properties prop = new Properties();
    	try {
    		String globusDir = System.getProperty("user.home") + File.separator + ".globus";
    		File dir = new File(globusDir, "GridFTP_GUI");
    		if (!dir.exists() || !dir.isDirectory()) {
    			dir.mkdirs();
    		}
    		
    		File prop_file = new File(dir, "rft.properties");
    		
			prop.setProperty("rft_enabled", String.valueOf(rFTTransferParam1.isRFTEnabled()));
			prop.setProperty("host", rFTTransferParam1.getServerHost());
			prop.setProperty("port", rFTTransferParam1.getServerPort());
			prop.setProperty("concurrent", Integer.toString(rFTTransferParam1.getConcurrent()));
			prop.setProperty("parallelstream", Integer.toString(rFTOptions1.getParallelStream()));
			prop.setProperty("tcpbuffersize", Integer.toString(rFTOptions1.getTcpBufferSize()));
			prop.store(new FileOutputStream(prop_file), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.frame.setVisible(false);
    }//GEN-LAST:event_stopButtonActionPerformed
    
    public void setFrame(JFrame f) {
    	this.frame = f;
    }
    
    /**
     * delete a transfer from queue panel and cancel actually transfer
     * @param jobid
     */
    public void deleteTransfer(String jobid) {
    	try {
			actionListener.stopButtonAction(jobid);
		} catch (Exception e) {
			int index = rftQueuePanel.getRowIndex(jobid, 0);
            rftQueuePanel.setColumnValue(index, 3, "Failed");
            rftQueuePanel.setColumnValue(index, 5, e.getMessage());
			e.printStackTrace();
		}
        rftQueuePanel.deleteTransfer(jobid);
    }
    
    /**
     * clear queue panel and cancel all transfer
     */
    public void clear() {
        if (rftQueuePanel.tableSize() > 0) {
            Object aobj[] = {"Yes", "No"};
            int k = JOptionPane.showOptionDialog(null, " Do you wish to clear all the jobs and stop the unfinished jobs ?", "Cancellation Alert", -1, 2, null, aobj, aobj[0]);
            if (k != 1) {


            	int tableSize = rftQueuePanel.tableSize();
            	
            	//cancel all transfer
            	for (int i = 0; i < tableSize; i++) {
            		String jobID = rftQueuePanel.getColumnValue(i, 0);
            		try {
						actionListener.stopButtonAction(jobID);
					} catch (Exception e) {
						int index = rftQueuePanel.getRowIndex(jobID);
	                    rftQueuePanel.setColumnValue(index, 3, "Failed");
	                    rftQueuePanel.setColumnValue(index, 5, e.getMessage());
						e.printStackTrace();
					}
            	}
            	
            	//clear from queue panel
            	rftQueuePanel.clear();
            
        } 
    }
    }  
    
    public QueuePanel getQueuePanel() {
    	return rftQueuePanel;
    }
    
        public static void main(String[] args) {
        Integer i = org.globus.wsrf.security.Constants.SIGNATURE;
    	JFrame frame = new JFrame();
    	RFTPanel panel = new RFTPanel();
    	frame.getContentPane().add(panel);
    	frame.pack();
    	frame.setVisible(true);
    	
    }
        
    
    private RFTButtonActionListener actionListener;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel11;
    private RFTOptionsPanel rFTOptions1;
    private RFTTransferParamPanel rFTTransferParam1;
    private javax.swing.JButton okButton;    
    private org.globus.ogce.beans.filetransfer.gui.monitor.QueuePanel rftQueuePanel;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    // End of variables declaration//GEN-END:variables
    
    class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            String actionCommand = ae.getActionCommand();
            if (actionCommand.equals("Save")) {
                Thread saveThread = new Thread() {
                    public void run() {
                        if (rftQueuePanel.tableSize() > 0) {
                            //theApp.saveQueueToFile("rft");
                        }
                    }
                };
                saveThread.start();
            } else if (actionCommand.equals("Load")) {
                Thread loadThread = new Thread() {
                    public void run() {
                        //theApp.loadQueueFromFile("rft");
                    }
                };
                loadThread.start();
            } else if (actionCommand.equals("Stop")) {
                Thread controlThread = new Thread() {
                    public void run() {
                        if (rftQueuePanel.tableSize() > 0) {
                            //theApp.controlExecutionQueue(false, "rft");
                        }
                    }
                };
                controlThread.start();
            } else if (actionCommand.equals("Start")) {
                Thread controlThread = new Thread() {
                    public void run() {
                        if (rftQueuePanel.tableSize() > 0) {
                            //theApp.controlExecutionQueue(true, "rft");
                        }
                    }
                };
                controlThread.start();
            } else if (actionCommand.equals("Clear")) {
                Thread controlThread = new Thread() {
                    public void run() {
                        if (rftQueuePanel.tableSize() > 0) {
                            clear();
                        }
                    }
                };
                controlThread.start();
            } else if (actionCommand.equals("Info")) {
                String job = rftQueuePanel.getSelectedJob();
                int row = rftQueuePanel.getRowIndex(job, 0);
                String msg = "   Job ID : " +
                        rftQueuePanel.getColumnValue(row, 0)
                        + "\n   From : "
                        + rftQueuePanel.getColumnValue(row, 1) +
                        "\n   To : " +
                        rftQueuePanel.getColumnValue(row, 2) +
                        "\n   Status : " +
                        rftQueuePanel.getColumnValue(row, 3)
                        + "\n   Errors : " +
                        rftQueuePanel.getColumnValue(row, 5) +
                        "\n";

                JOptionPane.showMessageDialog(null,
                        msg,
                        "RFT Job Information",
                        JOptionPane.PLAIN_MESSAGE);

            } else if (actionCommand.equals("Cancel")) {
                Thread controlThread = new Thread() {
                    public void run() {
                        String jobID = rftQueuePanel.getSelectedJob();
                        try {
							actionListener.stopButtonAction(jobID);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null,e.getMessage(), "Error",
		                            JOptionPane.WARNING_MESSAGE);
							int index = rftQueuePanel.getRowIndex(jobID);
		                    rftQueuePanel.setColumnValue(index, 3, "Failed");
		                    rftQueuePanel.setColumnValue(index, 5, e.getMessage());
							e.printStackTrace();
						}
                    }
                };
                controlThread.start();
                
            }  else if (actionCommand.equals("Delete")) {
                String jobID = rftQueuePanel.getSelectedJob();
                int row = rftQueuePanel.getRowIndex(jobID, 0);
                if (!rftQueuePanel.getColumnValue(row, 3).equals("Finished")) {
                    Object aobj[] = {"Yes", "No"};
                    int k = JOptionPane.showOptionDialog(null, " This job is not Finished yet. Do you wish to cancel the job and delete it?", "Deletion Alert", -1, 2, null, aobj, aobj[0]);
                    if (k == 1) {
                        return;
                    } else {
                    	deleteTransfer(jobID);
                    }
                } else {
                	deleteTransfer(jobID);
                }

            }
        }

    }
    
    public class MyFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = getExtension(f);
            if (extension != null) {
                if (extension.equals("xfr")) {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        public String getDescription() {
            return ".xfr files";
        }

        public String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }
            return ext;
        }
    }
    
}