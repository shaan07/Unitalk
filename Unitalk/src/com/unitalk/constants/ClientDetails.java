package com.unitalk.constants;

import java.io.Serializable;

import com.unitalk.server.ServerConnection;

public class ClientDetails implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ServerConnection serverThread;
	private String nickname;
	
	public ServerConnection getConnectedClient() {
		return serverThread;
	}
	public void setConnectedClient(ServerConnection connectedClient) {
		this.serverThread = connectedClient;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	

}
