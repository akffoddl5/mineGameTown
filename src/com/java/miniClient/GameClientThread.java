package com.java.miniClient;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.border.LineBorder;

public class GameClientThread extends Thread {
	
	
	public static ObjectInputStream ois;
	private Socket socket;
	
	public static MakeLogin makeLogin = null;
	public static MakeMenu makeMenu =null;
	public static MakeMineSweeper makeMineSweeper = null;
	public static MakeMineAvoider makeMineAvoider = null;
	
	public static String myId = null;
	public static String myPw = null;
	public static int canLogin = -1;
	
	public static boolean master = false;
	public static boolean canMineSweeper = false;
	public static boolean canMineAvoider = false;
	
	public static int myRoomNum = -1;
	
	
	
	private String[] icons = {"src/safe0.png","src/safe1.png","src/safe2.png","src/safe3.png","src/safe4.png","src/safe5.png",
			"src/safe6.png","src/safe7.png","src/safe8.png"
	};
	
	public GameClientThread(Socket socket1) {
		this.socket = socket1;
		this.makeLogin= new MakeLogin(socket);
		this.makeMenu = new MakeMenu();
		this.makeMineSweeper = new MakeMineSweeper();
		this.makeMineAvoider = new MakeMineAvoider();
		try {
			this.ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("이 오류..");
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		makeLogin.setSize(500,430);
		makeLogin.setVisible(true);
		try {
		
		
		while(true) {
			try {
				Object readObject= ois.readObject();
				ArrayList<Object> command = (ArrayList<Object>)readObject; 
				String protocol = (String)command.get(0);
				System.out.println(protocol);
				if(protocol.equals(ProtocolMsg.LOGIN_CHECK)) {
					System.out.println("login check complete");
					this.canLogin = (int)command.get(1);
					
					
				}else if(protocol.equals(ProtocolMsg.YOU_ARE_MINESWEEPER_MASTER)) {
					master= true;
					
					canMineSweeper=true;
					myRoomNum = (int)command.get(1);
					makeMineSweeper.roomNumLabel.setText(Integer.toString(myRoomNum+1)+"번 방");
					makeMineSweeper.startBt.setEnabled(true);
					
				}else if(protocol.equals(ProtocolMsg.YOU_CAN_MINESWEEPER)) {
					master=false;
					
					myRoomNum = (int)command.get(1);
					makeMineSweeper.roomNumLabel.setText(Integer.toString(myRoomNum+1)+"번 방");
					canMineSweeper =true;
				}else if(protocol.equals(ProtocolMsg.YOU_CANNOT_MINESWEEPER)) {
					System.out.println("시작못한다고 전령옴 ..");
					master = false;
					canMineSweeper=false;
				}else if(protocol.equals(ProtocolMsg.MINESWEEPER_MASTER_EXIT)) {
					makeMineSweeper.startBt.setEnabled(true);
				}else if(protocol.equals(ProtocolMsg.MINESWEEPER_PRESTART)) {
					System.out.println("시작 !! 지령받음.");
					makeMineSweeper.cnt=0;
					
					makeMineSweeper.makeDialog();
					makeMineSweeper.minesMap = (char[][])command.get(1);
					for(int i=0;i<16;i++) {
						for(int j=0;j<16;j++) {
							if(makeMineSweeper.minesMap[i][j]=='X') {
								makeMineSweeper.checkButtons[i][j]=1;
							}
						}
					}
					makeMineSweeper.myColor = (Color) command.get(2);
				}else if(protocol.equals(ProtocolMsg.CLICK_SAFEZONE)) {  // 여기서 0 처리 하면될듯
					int heClickRow = (int)command.get(1);
					int heClickCol = (int)command.get(2);
					Color hisColor = (Color)command.get(3);
					int heClickWhat = (char)command.get(4)-'0';
//					makeMineSweeper.buttons[heClickRow][heClickCol].setEnabled(false);
					makeMineSweeper.buttons[heClickRow][heClickCol].removeMouseListener(makeMineSweeper);
					makeMineSweeper.buttons[heClickRow][heClickCol].removeActionListener(makeMineSweeper);
					makeMineSweeper.buttons[heClickRow][heClickCol].setIcon(new ImageIcon(icons[heClickWhat]));
					makeMineSweeper.buttons[heClickRow][heClickCol].setBorder(new LineBorder(hisColor,5));
					makeMineSweeper.checkButtons[heClickRow][heClickCol]=1;
					
					
					
				}else if(protocol.equals(ProtocolMsg.MINESWEEPER_ALREADY_START)) {
					master = false;
					canMineSweeper=false;
				}else if(protocol.equals(ProtocolMsg.MINESWEEPER_GAME_END)) {
					System.out.println("마인 종료 알림받음.");
					canMineSweeper=true;
					
				}else if(protocol.equals(ProtocolMsg.USER_FINISH_MINESWEEPER)) {
					System.out.println("게임 종료 알림 받음.");
					
					List<Object> list = new ArrayList<Object>();
					list.add(ProtocolMsg.MINESWEEP_OVER_INFO);
					list.add(makeLogin.myId);
					list.add(makeLogin.myPw);
					list.add(makeMineSweeper.myColor);
					list.add(makeMineSweeper.cnt);
					list.add(GameClientThread.myRoomNum);
					makeLogin.oos.writeObject(list);   // curr
					
					
					
					
					String[] str = {"확인"};
					int confirm = JOptionPane.showOptionDialog(makeMineSweeper,"게임이 종료되었습니다. !!", "게임 오버", JOptionPane.OK_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, str, str[0]);
					if(confirm==JOptionPane.OK_OPTION) {
						makeMineSweeper.buttonReset();
						
					}
					
					
				}else if(protocol.equals(ProtocolMsg.MINESWEEP_ALL_FAIL)) {
					System.out.println("minesweeper all fail");
					
					List<Object> list = new ArrayList<Object>();
					list.add(ProtocolMsg.MINESWEEP_OVER_INFO);
					list.add(makeLogin.myId);
					list.add(makeLogin.myPw);
					list.add(makeMineSweeper.myColor);
					list.add(makeMineSweeper.cnt);
					list.add(GameClientThread.myRoomNum);
					makeLogin.oos.writeObject(list);   // curr
					
					
					String[] str = {"확인"};
					int confirm = JOptionPane.showOptionDialog(makeMineSweeper,"모두 실패하여 게임이 종료되었습니다.. 그것밖에 안됩니까들", "게임 오버", JOptionPane.OK_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, str, str[0]);
					if(confirm==JOptionPane.OK_OPTION) {
						makeMineSweeper.buttonReset();
					}
					
					
					
					
				}else if(protocol.equals(ProtocolMsg.MINESWEEP_OVER_ALLINFO)) {
					System.out.println("game over all info get");
					
					for(int i=0;i<16;i++) {
						for(int j=0;j<16;j++) {
							makeMineSweeper.buttons[i][j].removeActionListener(makeMineSweeper);
						}
					}
					
					System.out.println(">>" + command.get(1));
					makeMineSweeper.makeGameInfoDialog(command.get(1));
					
					
					
				}else if(protocol.equals(ProtocolMsg.VICTORY_NUM)) {
					System.out.println("get victory num");
					System.out.println(command.get(1));
					
					makeMenu.makeRanking(command.get(1));
					
				}else if(protocol.equals(ProtocolMsg.I_WANT_BATTLELOG)) {
					System.out.println("get battleLog");
					
					makeMenu.makeBattleLog(command.get(1));
				}else if(protocol.equals(ProtocolMsg.SOMEONE_IN)) {
					System.out.println("someone in");
					
					Vector<String> idlist2 = new Vector<>();
//					System.out.println("commandget1's size : " + (ArrayList<String>)command.get(1));
					for(int i=0;i<(int)command.get(1);i++) {
						idlist2.add((String)command.get(i+2));
					}
					System.out.println(idlist2);
					
					makeMineSweeper.setRight(idlist2);
				}else if(protocol.equals(ProtocolMsg.SOMEONE_EXIT)) {
					System.out.println("someone exit");
					
					Vector<String> idlist2 = new Vector<>();
//					System.out.println("commandget1's size : " + (ArrayList<String>)command.get(1));
					for(int i=0;i<(int)command.get(1);i++) {
						idlist2.add((String)command.get(i+2));
					}
					System.out.println(idlist2);
					
					makeMineSweeper.setRight(idlist2);
					
				}
				
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
	
	}
	


}
