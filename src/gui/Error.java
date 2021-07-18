package gui;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class Error implements Initializable{

   public static String errorMessage ;

    @FXML
    private Label label;

    @FXML
    private Label message;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		message.setText(errorMessage);
		
	}


	

}
