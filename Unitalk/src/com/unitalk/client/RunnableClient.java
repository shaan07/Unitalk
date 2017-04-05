package com.unitalk.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.unitalk.constants.ChatMessage;
import com.unitalk.constants.MessageConstants;

import javafx.application.Platform;

public class RunnableClient implements Runnable {

	private ClientController controller;
	private Socket serverSocket;
	public  ObjectInputStream ois;
	public  ObjectOutputStream oos;

	public RunnableClient(ClientController controller,Socket socket,ObjectInputStream ois,ObjectOutputStream oos) {

		this.controller=controller;
		this.serverSocket=socket;
		this.ois=ois;
		this.oos=oos;
	}

	@Override
	public void run() {
		while(true)
		{
			try {
				
				ChatMessage newMessage= (ChatMessage) this.ois.readObject();
				switch(newMessage.getMessageType())
				{
				case MessageConstants.CHAT_BROADCAST:
					this.controller.getClientLogs().appendText(newMessage.getClientDetails().getNickname() +">"+newMessage.getMessage()+"\n");
					break;
				case MessageConstants.REGISTER_BROADCAST:
					String newClient= newMessage.getMessage();
					System.out.println(newMessage.getMessage());
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							addClient(newClient);
						}
					});
					break;
				case MessageConstants.PRIVATE_MESSAGE:
					this.controller.getUserList().getSelectionModel().select(newMessage.getClientDetails().getNickname());
					this.controller.getClientLogs().appendText(newMessage.getClientDetails().getNickname() +" to You >"+newMessage.getMessage()+"\n");
					break;
				case MessageConstants.EXIT_MESSAGE:
					String leavingClient= newMessage.getMessage();
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							removeClient(leavingClient);
						}
					});
					break;				}
			}
			catch (Exception e)
			{
				this.controller.getClientLogs().appendText("Internal server error\n");
				e.printStackTrace();
			}
		}
	}
	public void addClient(String nickname){
		System.out.println("Adding new client :"+nickname);
		if(!this.controller.getUserList().getItems().contains(nickname))
		this.controller.getUserList().getItems().add(nickname);
		
		if(this.controller.getUserList().getItems().size()==1){
			this.controller.getUserList().getItems().add("Broadcast");
			this.controller.getUserList().getSelectionModel().select("Broadcast");
		}


	}
	public void removeClient(String nickname){
		System.out.println("Removing client :"+nickname);
		if(this.controller.getUserList().getItems().contains(nickname))
		this.controller.getUserList().getItems().remove(nickname);
	}
}
