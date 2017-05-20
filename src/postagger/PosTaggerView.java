/*
 * PosTaggerView.java
 */

package postagger;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import com.malikalamgirian.fyp.*;
import java.io.*;


/**
 * The application's main frame.
 */
public class PosTaggerView extends FrameView {

    public PosTaggerView(SingleFrameApplication app) {
        super(app);

        /* Manual Initailizations */
        try{
               /* For tagger */
               tagger = new MSRPCTagger();

               /* For file chooser */
               fileChooser = new JFileChooser();
        }
        catch(Exception ex){
            tarDetail.setText( tarDetail.getText() + ex + "\n");
            tarDetail.setText( tarDetail.getText() + ex.getMessage() + "\n");
        }
     

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = PosTaggerApp.getApplication().getMainFrame();
            aboutBox = new PosTaggerAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        PosTaggerApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tarDetail = new javax.swing.JTextArea();
        pnlPascal = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnProcessPascalRteChallange = new javax.swing.JButton();
        btnSelectPascalRteChallangeFile = new javax.swing.JButton();
        pnlMSRPC = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnSelectMSRPCFile = new javax.swing.JButton();
        btnProcessMSRPC = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tarDetail.setColumns(20);
        tarDetail.setRows(5);
        tarDetail.setName("tarDetail"); // NOI18N
        jScrollPane1.setViewportView(tarDetail);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(postagger.PosTaggerApp.class).getContext().getResourceMap(PosTaggerView.class);
        pnlPascal.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("pnlPascal.border.title"))); // NOI18N
        pnlPascal.setName("pnlPascal"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        btnProcessPascalRteChallange.setText(resourceMap.getString("btnProcessPascalRteChallange.text")); // NOI18N
        btnProcessPascalRteChallange.setName("btnProcessPascalRteChallange"); // NOI18N

        btnSelectPascalRteChallangeFile.setText(resourceMap.getString("btnSelectPascalRteChallangeFile.text")); // NOI18N
        btnSelectPascalRteChallangeFile.setName("btnSelectPascalRteChallangeFile"); // NOI18N

        javax.swing.GroupLayout pnlPascalLayout = new javax.swing.GroupLayout(pnlPascal);
        pnlPascal.setLayout(pnlPascalLayout);
        pnlPascalLayout.setHorizontalGroup(
            pnlPascalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPascalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPascalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnProcessPascalRteChallange)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(btnSelectPascalRteChallangeFile, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))
        );
        pnlPascalLayout.setVerticalGroup(
            pnlPascalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPascalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPascalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(btnSelectPascalRteChallangeFile))
                .addGap(18, 18, 18)
                .addComponent(btnProcessPascalRteChallange)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlMSRPC.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("pnlMSRPC.border.title"))); // NOI18N
        pnlMSRPC.setName("pnlMSRPC"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        btnSelectMSRPCFile.setText(resourceMap.getString("btnSelectMSRPCFile.text")); // NOI18N
        btnSelectMSRPCFile.setName("btnSelectMSRPCFile"); // NOI18N
        btnSelectMSRPCFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectMSRPCFileActionPerformed(evt);
            }
        });

        btnProcessMSRPC.setText(resourceMap.getString("btnProcessMSRPC.text")); // NOI18N
        btnProcessMSRPC.setName("btnProcessMSRPC"); // NOI18N
        btnProcessMSRPC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessMSRPCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMSRPCLayout = new javax.swing.GroupLayout(pnlMSRPC);
        pnlMSRPC.setLayout(pnlMSRPCLayout);
        pnlMSRPCLayout.setHorizontalGroup(
            pnlMSRPCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMSRPCLayout.createSequentialGroup()
                .addGroup(pnlMSRPCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMSRPCLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSelectMSRPCFile))
                    .addGroup(pnlMSRPCLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(btnProcessMSRPC, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                        .addGap(74, 74, 74)))
                .addContainerGap())
        );
        pnlMSRPCLayout.setVerticalGroup(
            pnlMSRPCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMSRPCLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMSRPCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSelectMSRPCFile)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addComponent(btnProcessMSRPC)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(pnlMSRPC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(44, 44, 44)
                        .addComponent(pnlPascal, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlMSRPC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlPascal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(postagger.PosTaggerApp.class).getContext().getActionMap(PosTaggerView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 442, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSelectMSRPCFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectMSRPCFileActionPerformed

        /* Here we just show JfileChooser and select the file */
        tarDetail.setText(tarDetail.getText() + "\nbtnSelectMSRPCFile : Select File Pressed. \n");

        try {
            // Open a file dialog.
            int retval = fileChooser.showOpenDialog(mainPanel);

            if (retval == JFileChooser.APPROVE_OPTION) {
                //... The user selected a file, get it, use it.
                File file = fileChooser.getSelectedFile();

                // check if the selected file is .txt
                if( file.getName().toLowerCase().endsWith(".txt") ){

                    //... Update MSRPC_Input_File_URL for process Button.
                    MSRPC_Input_File_URL = file.getAbsolutePath();

                    tarDetail.setText(tarDetail.getText() + "File Choosen Successfully. Now press Process Button. \n");
                    
                    /* Set the tagger URL */
                    tagger.MSRPC_Input_File_URL = MSRPC_Input_File_URL;
                    tarDetail.setText( tarDetail.getText() + tagger.MSRPC_Input_File_URL + ", is the inputfile URL.\n");

                }
                else{
                    // ask user to select .txt file
                    tarDetail.setText(tarDetail.getText() + "Please choose proper .txt file to process. \n");
                  
                }
            }

        }
        catch(Exception ex){
            tarDetail.setText( tarDetail.getText() + ex + "\n");
            tarDetail.setText( tarDetail.getText() + ex.getMessage() + "\n");
        }
    }//GEN-LAST:event_btnSelectMSRPCFileActionPerformed

    private void btnProcessMSRPCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessMSRPCActionPerformed
       /* Here we call process method of MSRPCTagger 
        * 1. call the process()
        * 2. Handle Exceptions properly
        */

        tarDetail.setText(tarDetail.getText() + "\nbtnProcessMSRPC : Process File pressed. \n");

       try{
           tagger.Process();
       }
       catch(Exception ex){
            tarDetail.setText( tarDetail.getText() + ex + "\n");
            tarDetail.setText( tarDetail.getText() + ex.getMessage() + "\n");
        }

        tarDetail.setText( tarDetail.getText() + tagger.MSRPC_Output_XML_File_URL + ", is the outputfile URL.\n");

    }//GEN-LAST:event_btnProcessMSRPCActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProcessMSRPC;
    private javax.swing.JButton btnProcessPascalRteChallange;
    private javax.swing.JButton btnSelectMSRPCFile;
    private javax.swing.JButton btnSelectPascalRteChallangeFile;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel pnlMSRPC;
    private javax.swing.JPanel pnlPascal;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTextArea tarDetail;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;

    /* Manual Declarations */
    private JFileChooser    fileChooser;
    String MSRPC_Input_File_URL;
    String Pascal_Rte_Challange_Input_File_URL;
    MSRPCTagger tagger;
    
}
