/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package UIL;

import BLL.FaceDetector;
import BLL.GaborFeature;
import BLL.NeuralNet;
import BLL.Util;
import BLL.PreProcess;
import DBL.Customers;
import DBL.CustomersDB;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.VideoInputFrameGrabber;

/**
 *
 * @author User
 */
public class FrameRecognizer extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(FrameRecognizer.class.getName());
    FileHandler fh;
    FrameGrabber grabber;
    IplImage ipimg;
    OpenCVFrameConverter.ToIplImage converter=new OpenCVFrameConverter.ToIplImage();
    BufferedImage bImg,captured,detected,hist;
    File file;
    String name,address;int mobile,acc,outputs;float bal,with;
    
    class captureImage implements Runnable{
        protected volatile boolean runn = false;
        protected volatile FrameGrabber gr;
        Util uobj=new Util();
        @Override
        public void run() {
            try
            {
                //grabber=new VideoInputFrameGrabber(0);
                grabber=new  OpenCVFrameGrabber(0);
                grabber.start();
                while(runn)
                {
                    Frame frame=grabber.grab();
                    ipimg=converter.convertToIplImage(frame);
                    if(ipimg!=null)
                    {
                        //cvFlip(ipimg, ipimg, 1);
                        bImg=uobj.ipltoBuffered(ipimg);
                        Graphics g=jPanel1.getGraphics();
                        if (g.drawImage(bImg, 0, 0, getWidth(), getHeight() -150 , 0, 0, bImg.getWidth(), bImg.getHeight(), null))
                        if(runn == false)
                        {
                            System.out.println("Going to wait()");
			    this.wait();
                        }
                    }
                }
            }
            catch(FrameGrabber.Exception | InterruptedException e)
            {
                logger.log(Level.WARNING,e.getMessage(),e);
            }
        } 
        public Boolean getStop() {
            return runn;
        }
        public void setStop(Boolean runn) {
            this.runn = runn;
        } 
    }
    /**
     * Creates new form videoFrame
     */
    public FrameRecognizer(){
        initComponents();
        capture();
        try
        {
            fh = new FileHandler(".\\Loggers\\Logger.log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter); 
        }
        catch(IOException e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
            fh.close();
        }
        outputs=newUserid();
    }
    
    private void capture()
    {
        captureImage capt=new captureImage();
        Thread t=new Thread(capt);
        t.setDaemon(true);
        capt.runn=true;
        t.start();
    }

    private void openFile()//alternative to real time detection
    {
        final JFrame frame = new JFrame("Select image to be recognized");
        JFileChooser fc=new JFileChooser(".\\trainingset");
        int returnVal = fc.showOpenDialog(frame);
        if(returnVal==JFileChooser.APPROVE_OPTION)
        {
            file=fc.getSelectedFile();
            try
            {
                String filename=file.toString();
            }
            catch(Exception e)
            {
                logger.log(Level.WARNING, "Error in reading file {0}", e);
                fh.close();
            }
        }
    }
    
    private int newUserid()//gets user id for the new user
    {
        ResultSet rs=null;
        int usid=0;
        try
        {
            CustomersDB udobj=new CustomersDB();
            rs=udobj.getAlldetails();
            if(rs.next() == false)
            {
                usid=0;
            }
            else
            {
                do
                {
                    usid=rs.getInt(1);
                }
                while(rs.next());
            }
        }
        catch(SQLException e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
            fh.close();
        }
        int ids=usid;
        System.out.println(ids);
        return ids;
    }
    /**
     * this is the method to recognize the faces
     */
    private void recognizeFaces(BufferedImage newImage)//get the buffered image as parameter
    {
        NeuralNet nnet=new NeuralNet();
        GaborFeature gf=new GaborFeature();
        ResultSet rs;
        Customers cobj=new Customers();
        double feature[]=new double[80];
        int id,pin;
        feature=gf.getFeature(newImage);
        try
        {
            id=nnet.recognizeFaces(feature,outputs);//get the recognized id
            if(id==0)
            {
                JOptionPane.showMessageDialog(null,"Unauthorized User - Try again","ERROR",JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                pin=Integer.parseInt(txtPIN.getText());
                cobj.setID(id);
                cobj.setPin(pin);
                CustomersDB cdobj=new CustomersDB();
                rs=cdobj.getAccountdetails(cobj);
                if(rs.next()==false)
                {
                    JOptionPane.showMessageDialog(null,"Unauthorized User - Try again","ERROR",JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    new captureImage().setStop(true);
                    FrameAtm atmo=new FrameAtm(id, pin);
                    atmo.show(); 
                    grabber.stop();
                    grabber.close();
                    this.dispose();
                }
            }

        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null,"cannot retrive values","ERROR",JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Cannot retrive values {0}", e);
            fh.close();
        } catch (FrameGrabber.Exception ex) {
            logger.log(Level.WARNING, "Cannot retrive values {0}", ex);
            fh.close();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnVerify = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btnFileimage = new javax.swing.JButton();
        btnGotologin = new javax.swing.JButton();
        txtPIN = new javax.swing.JPasswordField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ABC Bank - ATM ");
        setBackground(new java.awt.Color(204, 204, 255));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setPreferredSize(new java.awt.Dimension(320, 240));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 299, Short.MAX_VALUE)
        );

        btnVerify.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnVerify.setText("VERIFY");
        btnVerify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerifyActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("ATM");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setText("RECOGNIZE YOURSELF");

        jToolBar1.setRollover(true);

        btnFileimage.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnFileimage.setText("File");
        btnFileimage.setFocusable(false);
        btnFileimage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFileimage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFileimage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFileimageActionPerformed(evt);
            }
        });
        jToolBar1.add(btnFileimage);

        btnGotologin.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnGotologin.setText("Admin");
        btnGotologin.setFocusable(false);
        btnGotologin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGotologin.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGotologin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGotologinActionPerformed(evt);
            }
        });
        jToolBar1.add(btnGotologin);

        txtPIN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPIN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPINKeyTyped(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(252, 252, 252)
                .addComponent(jLabel1))
            .addGroup(layout.createSequentialGroup()
                .addGap(208, 208, 208)
                .addComponent(jLabel2))
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(txtPIN, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(237, 237, 237)
                .addComponent(btnVerify, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(470, 470, 470)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel1)
                .addGap(6, 6, 6)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(txtPIN, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnVerify, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVerifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerifyActionPerformed

        PreProcess pobj=new PreProcess();
        
        if(txtPIN.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Please enter your PIN and try again","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            try 
            {
                FaceDetector obj=new FaceDetector();
                captured=bImg;
                detected=obj.detectFace(captured);
                if(detected==null)
                {
                    JOptionPane.showMessageDialog(null,"Please capture again","ERROR",JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    hist=pobj.histogramEqualization(detected);
                    try {
                        ImageIO.write(hist,"jpg",(new File(".\\recognized.jpg")));
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, ex.getMessage(), ex);
                        fh.close();
                    }
                    recognizeFaces(hist);
                }
                
            } 
            catch (HeadlessException e)
            {
                logger.log(Level.SEVERE, e.getMessage(), e);
                fh.close();
            }
        }
    }//GEN-LAST:event_btnVerifyActionPerformed

    private void btnFileimageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFileimageActionPerformed

        if(txtPIN.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Please enter your PIN and try again","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            try 
            {
                openFile();
                hist=ImageIO.read(file);
                recognizeFaces(hist);
            } 
            catch (IOException e) 
            {
                logger.log(Level.WARNING, "Error in reading image{0}", e);
                fh.close();
            }
        }
    }//GEN-LAST:event_btnFileimageActionPerformed

    private void btnGotologinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGotologinActionPerformed
        try {
                FrameLogin atmo=new FrameLogin();
                atmo.show(); 
                grabber.stop();
                grabber.close();
                this.dispose();
        } catch (FrameGrabber.Exception ex) {
             logger.log(Level.WARNING, "Error in reading image{0}", ex);
             fh.close();
        }
    }//GEN-LAST:event_btnGotologinActionPerformed

    private void txtPINKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPINKeyTyped
        
         char enter = evt.getKeyChar();
        if(!(Character.isDigit(enter) || enter==KeyEvent.VK_BACK_SPACE || enter==KeyEvent.VK_DELETE || enter==KeyEvent.VK_PERIOD))
        {
            evt.consume();
        }
    }//GEN-LAST:event_txtPINKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameRecognizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameRecognizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameRecognizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameRecognizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameRecognizer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFileimage;
    private javax.swing.JButton btnGotologin;
    private javax.swing.JButton btnVerify;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPasswordField txtPIN;
    // End of variables declaration//GEN-END:variables
}
