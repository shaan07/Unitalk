package com.unitalk.client;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AccountActionController {



	@FXML
	private TextField serverId;

	@FXML
	private TextField buttonLogin;

	@FXML
	private Button login;


	@FXML
	void buttonLoginAction(ActionEvent event) {

		//	String userName = textFieldUserName.getText();
		//System.out.println(userName);


		try {
			setLoginData();
			Stage stage = ClientMain.currentStage;
			//	System.out.println(stage.getUserData().toString());
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("clientlayout.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);	
			stage.show();


		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setLoginData(){
		ClientMain.controller=this;

	}

	public TextField getServerId() {
		return serverId;
	}

	public void setServerId(TextField serverId) {
		this.serverId = serverId;
	}

	public TextField getButtonLogin() {
		return buttonLogin;
	}

	public void setButtonLogin(TextField buttonLogin) {
		this.buttonLogin = buttonLogin;
	}

	public Button getLogin() {
		return login;
	}

	public void setLogin(Button login) {
		this.login = login;
	}
	
	

}
