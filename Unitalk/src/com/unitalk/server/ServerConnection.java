package com.unitalk.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import com.unitalk.constants.ChatMessage;
import com.unitalk.constants.ClientDetails;
import com.unitalk.constants.MessageConstants;

public class ServerConnection implements Runnable ,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<ClientDetails> connectedClients;
	private ServerController controller;
	private Socket remoteClient;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	public ServerConnection() {
		// TODO Auto-generated constructor stub
	}
	public ServerConnection(ServerController controller,Socket client,ArrayList<ClientDetails> connectedClients) {
		try {
			this.connectedClients=connectedClients;
			this.controller=controller;
			this.remoteClient=client;
			this.oos= new ObjectOutputStream(client.getOutputStream());
			this.ois= new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			this.controller.getServerLogs().appendText("Internal I/O error\n");
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
					ClientDetails newClient= new ClientDetails();
					newClient.setConnectedClient(this);
					newClient.setNickname(newMessage.getMessage());
					this.connectedClients.add(newClient);
					//Broadcast client
					ChatMessage registerBroadcast= new ChatMessage();
					registerBroadcast.setMessageType(MessageConstants.REGISTER_BROADCAST);

					this.controller.getServerLogs().appendText("Connected> "+newClient.getNickname()+"\n");
					broadcastRegisterMessage(registerBroadcast);

					//Rest of code
					break;

				case MessageConstants.CHAT_BROADCAST://Code for Broadcast 
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
				case MessageConstants.PRIVATE_MESSAGE://Code for PM
					String privateMessage= newMessage.getMessage();
					System.out.println(privateMessage);
					String recipientNickname= newMessage.getRecipientClient();
					ClientDetails recipientClientDetails = null;

					for(ClientDetails client:this.connectedClients){
						if(client.getNickname().equals(recipientNickname)){
							recipientClientDetails=client;
							client.getConnectedClient().oos.writeUnshared(newMessage);
						}
					}
					break;
				case MessageConstants.EXIT_MESSAGE://Code for exiting client
					String clientName= newMessage.getMessage();
					ArrayList<ClientDetails> allClientDetails=new ArrayList<ClientDetails>();
					ClientDetails exitClientDetails = null;
					allClientDetails.addAll(this.connectedClients);

					for(ClientDetails client:allClientDetails){
						if(client.getNickname().equals(clientName)){
							exitClientDetails=client;
							this.connectedClients.remove(client);
						}
					}
					ChatMessage exitMessage= new ChatMessage();
					exitMessage.setMessageType(MessageConstants.EXIT_MESSAGE);
					exitMessage.setMessage(exitClientDetails.getNickname());
					this.controller.getServerLogs().appendText("Disconnected> "+clientName+"\n");
					broadcastExitMessage(exitMessage);
					break;

				}


			}
		} catch (ClassNotFoundException | IOException e) {
			this.controller.getServerLogs().appendText("Internal error\n");
			e.printStackTrace();
		}


	}
	public void broadcastRegisterMessage(ChatMessage message){
		try {
			System.out.println(this.connectedClients.size());
			for(ClientDetails currentClient: this.connectedClients)
			{
				ArrayList<ClientDetails> allClientDetails= new ArrayList<ClientDetails>();
				allClientDetails.addAll(this.connectedClients);
				allClientDetails.remove(currentClient);
				String nickname= currentClient.getNickname();
				message.setMessage(nickname);
				for(ClientDetails peerClients: allClientDetails){
					System.out.println(nickname+"---->"+peerClients.getNickname());
					ServerConnection clientSocket=peerClients.getConnectedClient();
					if(nickname.equals("3")){
						System.out.println("sending from 3"+allClientDetails.size()+"++++++"+peerClients.getNickname());
					}
					clientSocket.oos.writeUnshared(message);
					oos.reset();
				}
			}				
		} catch (IOException e) {
			this.controller.getServerLogs().appendText("Internal I/O error\n");
			e.printStackTrace();
		}

	}
	public void broadcastExitMessage(ChatMessage message){
		try {
			System.out.println(this.connectedClients.size());
			for(ClientDetails otherClients: this.connectedClients)
			{
				if(!otherClients.getConnectedClient().equals(this)) // don't send the message to the client that sent the message in the first place
				{
					ServerConnection clientSocket=otherClients.getConnectedClient();	
					clientSocket.oos.writeUnshared(message);
					oos.reset();
			}
				
			}				
		} catch (IOException e) {
			this.controller.getServerLogs().appendText("Internal I/O error\n");
			e.printStackTrace();
		}	
	}
}
