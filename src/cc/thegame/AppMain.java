package cc.thegame;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
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
			FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("AppInterface.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Scene scene = new Scene(page);
			primaryStage.setScene(scene);
			primaryStage.show();
                        
		} catch (IOException e) {
		}
	}
	
        public static void main(String[] args) {
		launch(args);
	}
}
