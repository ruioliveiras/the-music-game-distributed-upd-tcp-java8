package cc.thegame;

import cc.model.Question;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author paulo
 */
public class AppController {

    @FXML
    private Button r1_button, r2_button, r3_button;
    
    @FXML
    private TextArea question_text;
    
    @FXML
    private ImageView question_image; //provavelmente vai ter de chamar-se mesmo com uma imagem e nao com um url
    
    @FXML
    private ProgressBar time_bar;
    
    private static Timer time;
    
    private static int time_elapsed = 0;
    
    public ImageView toImage(byte[] iArray) throws IOException{
        //byte[] bytearray = Base64.decode(base64String);
 
	BufferedImage bimage=ImageIO.read(new ByteArrayInputStream(iArray));
        Image image = SwingFXUtils.toFXImage(bimage, null);
	return new ImageView((Image) image);
    }
    
    public void createQuestion(Question quest){
        String[] answers = quest.getAnwser();
        
        r1_button.setText(answers[0]);
        r2_button.setText(answers[1]);
        r3_button.setText(answers[2]);
        
        question_text.setText(quest.getQuestion());
        try {
            question_image = toImage(quest.getImageArray());
        } catch (IOException ex) {
            System.out.println("Não foi possível converter a imagem.");
        }        
    }  
    
    /*Não esta a funcionar*/
    private void setBarStyleClass(ProgressBar bar,double t) {
        bar.getStyleClass().removeAll();

        if(t >= 0.5F){
            bar.getStyleClass().add("-fx-accent: green;");
        }
        if(t < 0.5F && t>0.25F){ 
            bar.getStyleClass().add("-fx-accent: yellow;");
        }
        if(t<=0.25F){
            bar.getStyleClass().add("-fx-accent: red;");
        } 
           
    }
    
    private void playMusic(){
                            
        /*File songfile = new File("./etc/musica/000001.mp3");
        Media media = new Media(songfile.toURI().toString());
        MediaPlayer mp = new MediaPlayer(media);
        mp.play();
        */
    }
    
    
    
    /**
     * Initializes the controller class.
     */
    public void initialize() {
        
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            double i = 1F;
            @Override
            public void run() {
                i-=0.01F;
                setBarStyleClass(time_bar, i);
                time_bar.setProgress(i);
                if (i <= 0)
                    timer.cancel();
            }
        }, 0, 1000);
   
        r1_button.setOnAction((event) -> {
            r1_button.setStyle("-fx-background-color: #3DA428; -fx-font-size: 14px;");
        });
        
        r2_button.setOnAction((event) -> {
            r2_button.setStyle("-fx-background-color: #3DA428; -fx-font-size: 14px;");
        });
                
        r3_button.setOnAction((event) -> {
            r3_button.setStyle("-fx-background-color: #3DA428; -fx-font-size: 14px;");
        });
    }    
}
