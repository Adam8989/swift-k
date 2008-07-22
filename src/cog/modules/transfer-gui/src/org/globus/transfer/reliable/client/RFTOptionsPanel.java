package org.globus.transfer.reliable.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class RFTOptionsPanel extends javax.swing.JPanel implements ItemListener {
    
    /** Creates new form RFTOptions */
    public RFTOptionsPanel() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

//        jLabel1 = new javax.swing.JLabel();
//        binary = new javax.swing.JCheckBox();
//        jLabel2 = new javax.swing.JLabel();
//        blockSize = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tcpBufferSize = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
//        jLabel6 = new javax.swing.JLabel();
//        noTpt = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        parallelStream = new javax.swing.JTextField();
//        jLabel8 = new javax.swing.JLabel();
//        dCAU = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        concurrent = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        sourceSN = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        destSN = new javax.swing.JTextField();
//        enable_rft = new javax.swing.JLabel();
//        enable_rft_checkbox = new javax.swing.JCheckBox();
//        jLabel12 = new javax.swing.JLabel();
//        allOrNone = new javax.swing.JCheckBox();
        //jLabel13 = new javax.swing.JLabel();
//        maxAttempts = new javax.swing.JTextField();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.globus.transfer.reliable.client.GridFTPGUIApp.class).getContext().getResourceMap(RFTOptionsPanel.class);
        setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("Form.border.title"))); // NOI18N
        setName("Form"); // NOI18N

//        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
//        jLabel1.setName("jLabel1"); // NOI18N
//
//        binary.setText(resourceMap.getString("binary.text")); // NOI18N
//        binary.setName("binary"); // NOI18N
//
//        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
//        jLabel2.setName("jLabel2"); // NOI18N
//
//        blockSize.setText(resourceMap.getString("blockSize.text")); // NOI18N
//        blockSize.setName("blockSize"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        tcpBufferSize.setText(resourceMap.getString("tcpBufferSize.text")); // NOI18N
        tcpBufferSize.setName("tcpBufferSize"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N
        
        //enable_rft.setText("enable RFT");

//        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
//        jLabel6.setName("jLabel6"); // NOI18N
//
//        noTpt.setText(resourceMap.getString("noTpt.text")); // NOI18N
//        noTpt.setName("noTpt"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        parallelStream.setText(resourceMap.getString("parallelStream.text")); // NOI18N
        parallelStream.setName("parallelStream"); // NOI18N

//        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
//        jLabel8.setName("jLabel8"); // NOI18N
//
//        dCAU.setText(resourceMap.getString("dCAU.text")); // NOI18N
//        dCAU.setName("dCAU"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        concurrent.setText(resourceMap.getString("concurrent.text")); // NOI18N
        concurrent.setName("concurrent"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        sourceSN.setText(resourceMap.getString("sourceSN.text")); // NOI18N
        sourceSN.setName("sourceSN"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        destSN.setText(resourceMap.getString("destSN.text")); // NOI18N
        destSN.setName("destSN"); // NOI18N

//        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
//        jLabel12.setName("jLabel12"); // NOI18N
//
//        allOrNone.setText(resourceMap.getString("allOrNone.text")); // NOI18N
//        allOrNone.setName("allOrNone"); // NOI18N

//        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
//        jLabel13.setName("jLabel13"); // NOI18N

//        maxAttempts.setText(resourceMap.getString("maxAttempts.text")); // NOI18N
//        maxAttempts.setName("maxAttempts"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        
        /////////////////////////////////////////////////////////////////////////////////////////////////

        layout.setHorizontalGroup(
        		layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()                		
                    .addContainerGap()
                    .add(64, 64, 64)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jLabel7)
                                //.add(jLabel9)
                                .add(jLabel3))
                            //.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(parallelStream, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                                //.add(concurrent, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                                .add(tcpBufferSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, Short.MAX_VALUE))
                                
                                //.add(jLabel4)
                            //.add(93, 93, 93)
//                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
////                                .add(layout.createSequentialGroup()
////                                    .add(enable_rft, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
////                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
////                                    .add(enable_rft_checkbox))
//                                )
//                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
//                            
//                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
//                            
//                            .addContainerGap()
                            
                        )
//                        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
////                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
//////                                .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
//////                                    //.add(jLabel10)
//////                                    //.add(34, 34, 34)
//////                                    //.add(sourceSN, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
//////                                    )
//////                                .add(layout.createSequentialGroup()
//////                                    //.add(jLabel11)
//////                                    //.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
//////                                    //.add(destSN, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
//////                                    )
////                                    )
//                            .add(262, 262, 262))
                            ))
            );
        layout.setVerticalGroup(
        		layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .add(26, 26, 26)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel7)
                        .add(parallelStream, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                        .add(jLabel3)
//                        .add(tcpBufferSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                        .add(jLabel4)
                        )
                    .add(11, 11, 11)
//                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
//                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
//                        		.add(jLabel9)
//                            .add(concurrent, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                            
//                            )
////                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
////                            .add(enable_rft)
////                            .add(enable_rft_checkbox))
//                            )
//                   .add(11, 11, 11)
              .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel3)
                            .add(tcpBufferSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            //.add(jLabel4)
                            )
//                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
//                            .add(enable_rft)
//                            .add(enable_rft_checkbox))
                            )
                    //.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
//                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
//                        //.add(jLabel10)
//                        //.add(sourceSN, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                    		)
                    //.addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
//                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
//                        //.add(jLabel11)
//                        //.add(destSN, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                    		)
                    //.addContainerGap(127, Short.MAX_VALUE)
                    )
            );
        

        ////////////////////////////////////////////////////////////////////////////////////////////////
//        layout.setHorizontalGroup(
//            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
//            .add(layout.createSequentialGroup()
//                .addContainerGap()
//                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
//                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel11)
//                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()                    		
//                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
////                            .add(layout.createSequentialGroup()
////                                .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
////                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
////                                .add(binary)
////                                .add(44, 44, 44)
////                                .add(jLabel6)
////                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
//                            .add(layout.createSequentialGroup()
//                                .add(jLabel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
//                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
//                                .add(parallelStream, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                                .add(18, 18, 18))
//                            .add(layout.createSequentialGroup()
//                                .add(jLabel10)
//                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
//                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
////                            .add(layout.createSequentialGroup()
////                                .add(noTpt)
////                                .add(38, 38, 38)
////                                .add(jLabel8)
////                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
////                                .add(dCAU)
////                                .add(31, 31, 31)
////                                .add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
////                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
////                                .add(allOrNone)
//                        		)
//                            .add(layout.createSequentialGroup()
//                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
//                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
//                                    .add(layout.createSequentialGroup()
////                                        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
////                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
////                                        .add(blockSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
////                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
//                                        .add(jLabel4)
//                                        .add(30, 30, 30)
//                                        .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
//                                        .add(tcpBufferSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
//                                        .add(jLabel5)
//                                        .add(32, 32, 32)
////                                        .add(jLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
//                                        .add(concurrent, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
////                                        .add(18, 18, 18)
////                                        .add(jLabel9)
//                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
//                                        //.add(maxAttempts, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                                        )
//                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
//                                        .add(org.jdesktop.layout.GroupLayout.LEADING, destSN)
//                                        .add(org.jdesktop.layout.GroupLayout.LEADING, sourceSN, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 723, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
//                       // .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                        ))
//                .add(23, 23, 23))
//        ;
//        layout.setVerticalGroup(
//            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
//            .add(layout.createSequentialGroup()
////                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
////                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
////                    .add(binary)
////                    .add(jLabel6)
////                    .add(noTpt)
////                    .add(jLabel8)
////                    .add(dCAU)
////                    .add(jLabel12)
////                    .add(allOrNone)
//            		)
////                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
//                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
//                    .add(jLabel7)
//                    .add(parallelStream, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                    //.add(blockSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                    .add(jLabel4)
//                    .add(jLabel3)
//                    .add(tcpBufferSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                    .add(jLabel5)
////                    .add(jLabel13)
//                    .add(concurrent, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                    //.add(jLabel9)
//                    //.add(maxAttempts, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                    //.add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                    )
//                //.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
//                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
//                    .add(sourceSN, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//                    .add(jLabel10))
//                .add(18, 18, 18)
//                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
//                    .add(jLabel11)
//                    .add(destSN, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
//                //.addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                    );
    }// </editor-fold>//GEN-END:initComponents


    
    public boolean getAllOrNone() {        
        return isAllorNone;
    }

    public boolean getBinary() {
        return isBinary;
    }

//    public int getBlockSize() {
//        String value = blockSize.getText();
//        int size = 16000;
//        if (null != value) {
//            try {
//               size = Integer.valueOf(value);
//            } catch (NumberFormatException e) {
//                size = 16000;
//            }            
//        }
//        
//        return size;        
//    }

//    public int getConcurrent() {
//        String value = concurrent.getText();
//        int con = 1;
//        if (null != value) {
//            try {
//                con = Integer.valueOf(value);
//            } catch (NumberFormatException e) {
//                con = 1;
//            }            
//        }
//        
//        return con;
//    }

    public boolean getDCAU() {
        return isDCAU;
    }

    public String getDestSN() {
        return destSN.getText();
    } 


//    public int getMaxAttempts() {
//        String value = maxAttempts.getText();
//        int max = 1;
//        if (null != value) {
//            try {
//                max = Integer.valueOf(value);
//            } catch (NumberFormatException e) {
//                max = 1;
//            }
//        }
//            
//        return max;
//    }

    public boolean getNoTpt() {
        return isNoTpt;
    }

    public int getParallelStream() {
        String value = parallelStream.getText();
        int ret = 1;
        if (null != value) {
            try {
                ret = Integer.valueOf(value);
            } catch (NumberFormatException e) {
                ret = 1;
            }
        }
        
        return ret;
    }

    public String getSourceSN() {
        return sourceSN.getText();
    }

    public int getTcpBufferSize() {
        String value = tcpBufferSize.getText();
        int ret = 16000;
        if (null != value) {
            try {
                ret = Integer.valueOf(value);
            } catch (NumberFormatException e) {
                ret = 16000;
            }
        }
        
        return ret;
    }
        
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.getContentPane().add(new RFTOptionsPanel());
        f.setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    //private javax.swing.JCheckBox allOrNone;
    //private javax.swing.JCheckBox binary;
    //private javax.swing.JTextField blockSize;
    private javax.swing.JTextField concurrent;
    //private javax.swing.JCheckBox dCAU;
    private javax.swing.JTextField destSN;
    //private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    //private javax.swing.JLabel jLabel12;
    //private javax.swing.JLabel jLabel13;
    //private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    //private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    //private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    //private javax.swing.JTextField maxAttempts;
    //private javax.swing.JCheckBox noTpt;
    private javax.swing.JTextField parallelStream;
    private javax.swing.JTextField sourceSN;
    private javax.swing.JTextField tcpBufferSize;
    // End of variables declaration//GEN-END:variables
    private boolean isBinary = true;
    private boolean isNoTpt = false;
    private boolean isDCAU = true;
    private boolean isAllorNone = false;
//    private javax.swing.JLabel enable_rft;
//    private javax.swing.JCheckBox enable_rft_checkbox;
    
    public void itemStateChanged(ItemEvent e) {
//        Object source = e.getItemSelectable();
//        if (source == binary) {
//            isBinary = !isBinary;
//        } else if (source == noTpt) {
//            isNoTpt = !isNoTpt;
//        } else if (source == dCAU) {
//            isDCAU = !isDCAU;
//        } else if (source == allOrNone) {
//            isAllorNone = !isAllorNone;
//        }        
    }
}
