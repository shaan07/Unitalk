package com.unitalk.constants;

import java.io.Serializable;
import java.util.ArrayList;

import com.unitalk.server.ServerConnection;

import javafx.collections.ObservableList;

public class ChatMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int messageType;
	private ClientDetails clientDetails; 
	private String recipientClient;
	private String message;
	private ArrayList<ClientDetails> connectedClients;

	
	public ArrayList<ClientDetails> getConnectedClients() {
		return connectedClients;
	}
	public void setConnectedClients(ArrayList<ClientDetails> connectedClients) {
		this.connectedClients = connectedClients;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
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
	public String getRecipientClient() {
		return recipientClient;
	}
	public void setRecipientClient(String recipientClient) {
		this.recipientClient = recipientClient;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	
}
