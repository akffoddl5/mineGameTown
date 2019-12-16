package com.java.miniClient;

import java.awt.Frame;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameClient extends Frame {
	
	private Socket socket;
	
	public GameClient() {
		
		try {
			socket = new Socket("localhost", 10303);
			GameClientThread thread = new GameClientThread(socket);
			thread.start();
		} catch (UnknownHostException e) {
			System.out.println("?? 그런 서버 없는데");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("연결 안대는디..");
			e.printStackTrace();
		}
		
//		MakeLogin makeLogin = new MakeLogin(socket);
//		makeLogin.setSize(500,680);
		
		
		
		
		
		
		
		
		
		
//		makeLogin.setVisible(true);
		
		
	}
	
	
	public static void main(String[] args) {
		new GameClient();
	}
	

}
