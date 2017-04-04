package com.unitalk.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.unitalk.constants.ChatMessage;
import com.unitalk.constants.ClientDetails;
import com.unitalk.constants.MessageConstants;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ClientController {

	@FXML
	private TextField message;
	@FXML
	private Button buttonSend;
	@FXML
	private Button fileUpload;
	@FXML
	private Text filePath;
	@FXML
	private ListView<String> userList;
	@FXML
	private Text serverIp;
	@FXML
	private Text userName;
	@FXML
	private TextArea clientLogs;
	@FXML
	private Button logoutButton;

	private Socket serverSocket;
	public  ObjectInputStream ois;
	public  ObjectOutputStream oos;
	private ClientDetails clientdetails;

	@FXML
	public void initialize() {
		try {
			this.userName.setText(ClientMain.controller.getButtonLogin().getText());
			System.out.println(this.userName);
			this.serverIp.setText(ClientMain.controller.getServerId().getText());
			if(serverIp.getText()!=null && !serverIp.getText().isEmpty()){
				System.out.println("Hel");
				this.serverSocket= new Socket(this.serverIp.getText(),9888);}
			else
			{
				System.out.println("Hello");
				this.serverSocket= new  Socket("localhost",9888);
			}
			System.out.println(serverSocket);
			this.ois= new ObjectInputStream(this.serverSocket.getInputStream());
			this.oos= new ObjectOutputStream(this.serverSocket.getOutputStream());
			RunnableClient runnableClient= new RunnableClient(this, this.serverSocket, ois, oos);
			Thread runnableClientThread= new Thread(runnableClient);
			runnableClientThread.start();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			registerClient();
		}

	}

	@FXML
	void buttonSendAction(ActionEvent event) {

		try {
			ChatMessage newMessage= new ChatMessage();
			newMessage.setMessageType(MessageConstants.CHAT_BROADCAST);
			newMessage.setMessage(this.message.getText());
			newMessage.setClientDetails(clientdetails);
			this.clientLogs.appendText(this.userName.getText()+">"+this.message.getText()+"\n");
			oos.writeObject(newMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void buttonAttachFileAction(ActionEvent event) {

	}
	@FXML
	void buttonLogoutAction(ActionEvent event) {
		try {
			ChatMessage exitMessage= new ChatMessage();
			exitMessage.setMessageType(MessageConstants.EXIT_MESSAGE);
			exitMessage.setMessage(this.getUserName().getText());
			oos.writeObject(exitMessage);
			this.userList.getItems().remove(exitMessage.getMessage());
			Platform.exit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void registerClient(){
		try {
			ChatMessage registerMessage= new ChatMessage();
			registerMessage.setMessageType(MessageConstants.REGISTER_CLIENT);
			registerMessage.setMessage(this.getUserName().getText());
			this.clientdetails= new ClientDetails();
			this.clientdetails.setNickname(this.userName.getText());
			System.out.println(this.serverSocket);
			oos.writeObject(registerMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public Socket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(Socket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public  ClientDetails getClientdetails() {
		return clientdetails;
	}

	public  void setClientdetails(ClientDetails clientdetails) {
		this.clientdetails = clientdetails;
	}


	public TextField getMessage() {
		return message;
	}

	public void setMessage(TextField message) {
		this.message = message;
	}

	public Button getButtonSend() {
		return buttonSend;
	}

	public void setButtonSend(Button buttonSend) {
		this.buttonSend = buttonSend;
	}

	public Button getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(Button fileUpload) {
		this.fileUpload = fileUpload;
	}

	public Text getFilePath() {
		return filePath;
	}

	public void setFilePath(Text filePath) {
		this.filePath = filePath;
	}

	public ListView<String> getUserList() {
		return userList;
	}

	public void setUserList(ListView<String> userList) {
		this.userList = userList;
	}

	public Text getServerIp() {
		return serverIp;
	}

	public void setServerIp(Text serverIp) {
		this.serverIp = serverIp;
	}

	public Text getUserName() {
		return userName;
	}

	public void setUserName(Text userName) {
		this.userName = userName;
	}

	public TextArea getClientLogs() {
		return clientLogs;
	}

	public void setClientLogs(TextArea clientLogs) {
		this.clientLogs = clientLogs;
	}

	public Button getLogoutButton() {
		return logoutButton;
	}

	public void setLogoutButton(Button logoutButton) {
		this.logoutButton = logoutButton;
	}

}
