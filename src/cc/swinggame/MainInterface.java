/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.swinggame;

import cc.model.Question;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;

/**
 *
 * @author paulo
 */
public class MainInterface extends javax.swing.JFrame {

    private volatile int answerState;
    private final Object obj = new Object();
    /**
     * Creates new form MainInterface
     */
    public MainInterface() {
        initComponents();
        question_area.setLineWrap(true);
        answerState = 0;
        setQuestionTheme();
    }   
    
    public void refreshFrame(){
        invalidate();
        validate();
        repaint();
    }
    
    public synchronized void setQuestionTheme(){
        r1_button.setBackground(Color.blue);
        r2_button.setBackground(Color.blue);
        r3_button.setBackground(Color.blue);
        r1_button.setEnabled(true);
        r2_button.setEnabled(true);
        r3_button.setEnabled(true);
        
        //question_image = null;
        //if(music_player.getStatus()==PLAYING) music_player.stop();  
    }
    
    //falta tratar para o caso em que nao respondeu
    public void showResult(int given, int correctAnswer){
        if(correctAnswer == 0) r1_button.setBackground(Color.green); 
        else if(correctAnswer == 1) r2_button.setBackground(Color.green);
        else if(correctAnswer == 2) r3_button.setBackground(Color.green);
        
        if(correctAnswer != given-1){
            if(given == 1) r1_button.setBackground(Color.red); 
            else if(given == 2) r2_button.setBackground(Color.red); 
            else if(given == 3) r3_button.setBackground(Color.red);    
        }
    }
    
    public int createQuestion(Question quest){
        String[] answers = quest.getAnwser();
        String question_text = quest.getQuestion();

        answerState=0;
        
        r1_button.setText(answers[0]);
        r2_button.setText(answers[1]);
        r3_button.setText(answers[2]);           
 
        question_area.setText(question_text);
        
        setQuestionTheme();
           
        //setTimer(100);
        
        //while(answerState == 0);
        
        synchronized(obj){
            try {
                obj.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return answerState;
        /*
        try {
            question_image = toImage(quest.getImageArray());
        } catch (IOException ex) {
            System.out.println("Não foi possível converter a imagem.");
        } */     
    }  
    
    public void updateScore(int points){
        i_points.setText(Integer.toString(points));
    }
    
    public synchronized void setAnswerState(int state){
        answerState = state;
    }
    /*
    public void setTimer(double inicial_value){
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            double i = inicial_value;
            @Override
            public void run() {
                i-=1F;
                progress_bar.setValue((int) i);
                progress_bar.setStringPainted(true);
                if (i <= 1){
                    answerState=4;
                }
                if(answerState != 0 ) {
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }*/
    
    private void playMusic(){
                            
        /*File songfile = new File("./etc/musica/000001.mp3");
        Media media = new Media(songfile.toURI().toString());
        MediaPlayer music_player = new MediaPlayer(media);
        mp.play();
        */
    }
    
    public void notifyObject(){
         synchronized(obj){
            obj.notify(); 
        }
    }
    
    public ImageView toImage(byte[] iArray) throws IOException{
        //byte[] bytearray = Base64.decode(base64String);
 
	BufferedImage bimage=ImageIO.read(new ByteArrayInputStream(iArray));
        Image image = SwingFXUtils.toFXImage(bimage, null);
	return new ImageView((Image) image);
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
        r1_button = new javax.swing.JButton();
        r2_button = new javax.swing.JButton();
        r3_button = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        question_area = new javax.swing.JTextArea();
        progress_bar = new javax.swing.JProgressBar();
        image_panel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        i_points = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        r1_button.setText("Resposta 1");
        r1_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r1_buttonActionPerformed(evt);
            }
        });

        r2_button.setText("Resposta 2");
        r2_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r2_buttonActionPerformed(evt);
            }
        });

        r3_button.setText("Resposta 3");
        r3_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r3_buttonActionPerformed(evt);
            }
        });

        question_area.setEditable(false);
        question_area.setColumns(20);
        question_area.setLineWrap(true);
        question_area.setRows(5);
        jScrollPane1.setViewportView(question_area);

        progress_bar.setString("");

        javax.swing.GroupLayout image_panelLayout = new javax.swing.GroupLayout(image_panel);
        image_panel.setLayout(image_panelLayout);
        image_panelLayout.setHorizontalGroup(
            image_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 336, Short.MAX_VALUE)
        );
        image_panelLayout.setVerticalGroup(
            image_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel1.setText("Pontos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(r2_button, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addComponent(r1_button, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addComponent(r3_button, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(progress_bar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(i_points, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(image_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                        .addComponent(image_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(i_points, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(progress_bar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(r1_button, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(r2_button, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(r3_button, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void r1_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r1_buttonActionPerformed
        setAnswerState(1);
        r1_button.setBackground(Color.YELLOW);
        r2_button.setEnabled(false);
        r3_button.setEnabled(false);
        notifyObject();
    }//GEN-LAST:event_r1_buttonActionPerformed

    private void r2_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r2_buttonActionPerformed
        setAnswerState(2);
        r2_button.setBackground(Color.YELLOW);
        r1_button.setEnabled(false);
        r3_button.setEnabled(false);
        notifyObject();        
    }//GEN-LAST:event_r2_buttonActionPerformed

    private void r3_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r3_buttonActionPerformed
        setAnswerState(3);
        //answerState=3;
        r3_button.setBackground(Color.YELLOW);
        r1_button.setEnabled(false);
        r2_button.setEnabled(false);
        notifyObject();
    }//GEN-LAST:event_r3_buttonActionPerformed

    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
       /* try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }*/
        //</editor-fold>

        /* Create and display the form */
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainInterface().setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel i_points;
    private javax.swing.JPanel image_panel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JProgressBar progress_bar;
    private javax.swing.JTextArea question_area;
    private javax.swing.JButton r1_button;
    private javax.swing.JButton r2_button;
    private javax.swing.JButton r3_button;
    // End of variables declaration//GEN-END:variables
}