package main;

import gui.ShowError;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Driver extends Application {

	public static Stage mainStage ;
	
	public static void main(String[] args) {

		Application.launch(args);
	}
	
	

	@Override
	public void start(Stage primaryStage) throws Exception {

		mainStage = primaryStage ;
		ShowError.stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("./../GUI.fxml"));
		Scene scene = new Scene(root, 695, 530);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
