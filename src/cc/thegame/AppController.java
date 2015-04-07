package cc.thegame;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author paulo
 */
public class AppController {

    @FXML
    private Button r1_button;
    
    @FXML
    private TextArea outputTextArea;
    
    
    /**
     * Initializes the controller class.
     */
    public void initialize() {
        r1_button.setOnAction((event) -> {
	r1_button.setText("Hello, World.");
        });
    }    
    
}
