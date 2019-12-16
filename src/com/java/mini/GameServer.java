package com.java.mini;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameServer {
	
	
	private ServerSocket ss;
	private List<GameServerThread> threadList = new ArrayList<>(); 
	private ArrayList<List<GameServerThread>> minesweeperList = new ArrayList<>();
	private static List<GameServerThread> mineavoiderList = new ArrayList<>();
	private ArrayList<Integer> gameAlreadyStartList = new ArrayList<Integer>(); 
	
	public static Map<Integer , List<ArrayList<Object>> > minesweepOverInfoList = new HashMap<>();
	public static List<ArrayList<Object> > mineavoidOverInfoList = new ArrayList<ArrayList<Object> >();
	
	public static List<ArrayList<String>> idList = new ArrayList<ArrayList<String>>();
	public GameServer() {
		
		try {
			ss = new ServerSocket(10303);
			minesweeperList.add(new ArrayList<GameServerThread>());
			idList.add(new ArrayList<String>());
		} catch (IOException e) {
			System.out.println("flag1");
			e.printStackTrace();
		}
		
		while(true) {
			
			System.out.println("server operationg.. current user num :" + threadList.size());
			try {
				Socket client = ss.accept();
				InetAddress inetAddress = client.getInetAddress();
				String address = inetAddress.getHostAddress();
				String name = inetAddress.getHostName();
				System.out.println(address+"("+name+") connected");
				GameServerThread thread = new GameServerThread(client,threadList,minesweeperList,mineavoiderList,gameAlreadyStartList,idList);
				thread.start();
				threadList.add(thread);
				System.out.println("서버의 끝");
			} catch (IOException e) {
				System.out.println("flag2");
				e.printStackTrace();
			}
			
			
			
		}
	}
	
	public static void main(String[] args) {
		
		new GameServer();
		
		

	}

}
