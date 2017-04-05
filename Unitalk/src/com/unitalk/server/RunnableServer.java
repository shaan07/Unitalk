package com.unitalk.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.unitalk.constants.ClientDetails;

public class RunnableServer implements Runnable{

	private ServerController controller;
	private ServerSocket serverSocket;
	private ArrayList<ClientDetails> connectedClients;
	
	public RunnableServer(ServerController controller) {
		
		this.connectedClients= new ArrayList<ClientDetails>();
		this.controller=controller;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.serverSocket=new ServerSocket(9888);
			this.controller.getServerLogs().appendText("Server started at 9888.Waiting for client\n");
			while(true){
				Socket remoteClient= this.serverSocket.accept();
				System.out.println("Client Connected");
				ServerConnection serverConnection= new ServerConnection(this.controller,remoteClient,this.connectedClients);
				Thread serverConnectionThread= new Thread(serverConnection);
				serverConnectionThread.start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
