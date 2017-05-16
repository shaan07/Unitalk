package com.unitalk.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientMain extends Application{

public static Stage currentStage = new Stage();
public static AccountActionController controller;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		try {
			
			// loading clientlogin fxml
			VBox root = (VBox)FXMLLoader.load(getClass().getResource("clientlogin.fxml"));
			
			// setting clientLogin as primary stage
			Scene scene = new Scene(root,600,600);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Unitalk Chat");
			primaryStage.show();
			
			currentStage=primaryStage;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
	
}
