package com.unitalk.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.unitalk.constants.ChatMessage;
import com.unitalk.constants.MessageConstants;

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
					this.controller.getClientLogs().appendText(newMessage.getMessage()+"\n");
					break;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}
