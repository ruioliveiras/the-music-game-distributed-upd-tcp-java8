package cc.thegame;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 *
 * @author paulo
 */

public class AppMain extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Event Handling");	
		try {
                    FXMLLoader loader;                        
                    loader = new FXMLLoader(getClass().getResource("AppInterface.fxml"));
                    AnchorPane page;
                    page = (AnchorPane) loader.load();
                    Scene scene = new Scene(page);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException ex) {
                    Logger.getLogger(AppMain.class.getName()).log(Level.SEVERE, null, ex);
                }

                        

	}
	
        public static void main(String[] args) {
		launch(args);
	}
}

/*            } catch (IOException ex) {
                System.out.println("Problema ao carregar a interface.");
            }*/