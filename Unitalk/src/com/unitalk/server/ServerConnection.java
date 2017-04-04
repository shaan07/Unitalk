package com.unitalk.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.unitalk.constants.ChatMessage;
import com.unitalk.constants.ClientDetails;
import com.unitalk.constants.MessageConstants;

public class ServerConnection implements Runnable{

	private ArrayList<ClientDetails> connectedClients;
	private ServerController controller;
	private Socket remoteClient;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public ServerConnection(ServerController controller,Socket client,ArrayList<ClientDetails> connectedClients) {
		try {
			this.connectedClients=connectedClients;
			this.controller=controller;
			this.remoteClient=client;
			this.oos= new ObjectOutputStream(client.getOutputStream());
			this.ois= new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while(true){

				ChatMessage newMessage= (ChatMessage) ois.readObject();
				int messageType=newMessage.getMessageType();

				switch(messageType){

				case MessageConstants.REGISTER_CLIENT:
					
					this.controller.getServerLogs().appendText("Connected> "+newMessage.getClientDetails().getNickname()+"\n");
					//Rest of code
					break;
					
				case MessageConstants.CHAT_BROADCAST://Code for registering client
					String messageData= newMessage.getMessage();
					System.out.println(messageData);
					this.controller.getServerLogs().appendText(remoteClient.getInetAddress()+":"+remoteClient.getPort()+">"+messageData+"\n");
					for(ClientDetails otherClient: this.connectedClients)
					{
						if(!otherClient.getConnectedClient().equals(this)) // don't send the message to the client that sent the message in the first place
						{
							otherClient.getConnectedClient().oos.writeObject(newMessage);
						}
					}
					break;
				case MessageConstants.PRIVATE_MESSAGE://Code for registering client
					break;
				case MessageConstants.EXIT_MESSAGE://Code for registering client
					break;

				}


			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
