package com.unitalk.constants;

import java.io.Serializable;

import com.unitalk.server.ServerConnection;

public class ChatMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int messageType;
	private ClientDetails clientDetails; 
	private ServerConnection recipientClient;
	private String message;
	
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public ClientDetails getClientDetails() {
		return clientDetails;
	}
	public void setClientDetails(ClientDetails clientDetails) {
		this.clientDetails = clientDetails;
	}
	public ServerConnection getRecipientClient() {
		return recipientClient;
	}
	public void setRecipientClient(ServerConnection recipientClient) {
		this.recipientClient = recipientClient;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	
}
