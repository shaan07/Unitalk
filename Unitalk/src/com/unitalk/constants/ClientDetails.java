package com.unitalk.constants;

import com.unitalk.server.ServerConnection;

public class ClientDetails {
	
	private ServerConnection connectedClient;
	private String nickname;
	
	public ServerConnection getConnectedClient() {
		return connectedClient;
	}
	public void setConnectedClient(ServerConnection connectedClient) {
		this.connectedClient = connectedClient;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	

}
