package com.java.mini;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameServerThread extends Thread {
	
	
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket client;
	private List<GameServerThread> threadList = null;
	private ArrayList<List<GameServerThread>> mineSweeperThreadList = null;
	private List<GameServerThread> mineAvoiderThreadList = null;
	private ArrayList<Integer> gameAlreadyStartList = null;
	public static boolean minesweeperAlreadyStart = false;
	
	private boolean iFailMinesweep= false;
	
	private List<ArrayList<String>> idList = null;
	private String myId = "";
	
	public GameServerThread(Socket client1 , List<GameServerThread> threadList1,ArrayList<List<GameServerThread>> mineSweeperThreadList1
			,List<GameServerThread> mineAvoiderThreadList1,ArrayList<Integer> gameAlreadyStartList1,
			List<ArrayList<String>> idList1) {
		this.client = client1;
		this.threadList=threadList1;
		this.mineSweeperThreadList=mineSweeperThreadList1;
		this.mineAvoiderThreadList=mineAvoiderThreadList1;
		this.gameAlreadyStartList=gameAlreadyStartList1;
		this.idList= idList1;
		
	}
	
	@Override
	public synchronized void run() {
		
		try {
//			br= new BufferedReader(new InputStreamReader(client.getInputStream()));
//			bw= new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			ois = new ObjectInputStream(client.getInputStream());
			oos = new ObjectOutputStream(client.getOutputStream());
			while(true) {
				System.out.println("커멘드 대기중..");
				
				Object readObject= ois.readObject();
				ArrayList<Object> command = (ArrayList<Object>)readObject; 
				String protocol = (String)command.get(0);
				if(protocol.equals(ProtocolMsg.LOGIN)) {
					System.out.println("login 요청 들어옴");
					int canLogin = ProtocolMsg.canLogin(command);
					
					ArrayList<Object> response = new ArrayList<Object>();
					response.add(ProtocolMsg.LOGIN_CHECK);
					response.add(canLogin);
					
					System.out.println(ProtocolMsg.LOGIN_CHECK + canLogin);
					
					
					oos.writeObject(response);
					myId = (String)command.get(1);
					
				}else if(protocol.contentEquals(ProtocolMsg.JUST_LOGIN)) {
					System.out.println("just login 요청");
					
					
					String id = (String)command.get(1);
					String pw = (String)command.get(2);
					
					ProtocolMsg.powerLogin(id,pw);
					myId = (String)command.get(1);
					
				}else if(protocol.equals(ProtocolMsg.I_WANT_MINESWEEPER)) {
					System.out.println("he want minesweeper");
					if(mineSweeperThreadList.size()==0) {
						mineSweeperThreadList.add(new ArrayList<GameServerThread>());
						idList.add(new ArrayList<String>());
					}
					int roomNum = mineSweeperThreadList.size();
					int i=0;
					for(i=0;i<roomNum ; i++) {
						if(mineSweeperThreadList.get(i).size()<4 && !gameAlreadyStartList.contains(i)) {
							break;
						}
					}
					
					if(i>=roomNum) {  // 여기가 핵심  오류의 근원 >= 이냐 > 이냐
						mineSweeperThreadList.add(new ArrayList<GameServerThread>());
						idList.add(new ArrayList<String>());
					}
					
					if(mineSweeperThreadList.get(i).size()==0) {
						gameAlreadyStartList.remove((Integer)i);  // 이거 제대로 될지 모름. 인덱스가 지워질지도 // 널 포인터뜰지
					}
					if(false) { // 이미 시작한 경우는 없어야 할것이야
//						System.out.println(gameAlreadyStartList[0] + " "+ gameAlreadyStartList[1]);
//						System.out.println("flag1");
//						ArrayList<Object> list = new ArrayList<Object>();
//						list.add(ProtocolMsg.MINESWEEPER_ALREADY_START);
//						oos.writeObject(list);
					}else if(mineSweeperThreadList.get(i).size()==0) {
						System.out.println("flag2");
						
						
						ArrayList<Object> list = new ArrayList<>();
						list.add(ProtocolMsg.YOU_ARE_MINESWEEPER_MASTER);
						list.add(i);
						oos.writeObject(list);
						mineSweeperThreadList.get(i).add(this);
							
						ArrayList<Object> list2 = new ArrayList<Object>();
						idList.get(i).add(this.myId);
						list2.add(ProtocolMsg.SOMEONE_IN);
						list2.add(idList.get(i).size());
						for(String s : idList.get(i)) {
							list2.add(s);
						}
//						System.out.println(idList.get(i));
						broadcasting(mineSweeperThreadList.get(i), list2);
						
					}else if(mineSweeperThreadList.get(i).size()<4) {
						System.out.println("flag3");
						
							ArrayList<Object> list = new ArrayList<>();
							list.add(ProtocolMsg.YOU_CAN_MINESWEEPER);
							list.add(i);
							oos.writeObject(list);
							mineSweeperThreadList.get(i).add(this);
							
							ArrayList<Object> list2 = new ArrayList<Object>();
							idList.get(i).add(this.myId);
							list2.add(ProtocolMsg.SOMEONE_IN);
							list2.add(idList.get(i).size());
							for(String s : idList.get(i)) {
								list2.add(s);
							}
//							System.out.println(list2);
							broadcasting(mineSweeperThreadList.get(i), list2);
						
						
					}else {
						System.out.println("flag4");
						ArrayList<Object> list = new ArrayList<>();
						list.add(ProtocolMsg.YOU_CANNOT_MINESWEEPER);
						oos.writeObject(list);
					}
					
					System.out.println("지뢰 찾기 방 개수 : " + mineSweeperThreadList.size());
					System.out.print("지뢰찾기 방 인원 : ");
					for(List<GameServerThread> p : mineSweeperThreadList ) {
						System.out.print(p.size()+ " 명 , ");
					}
					System.out.println();
					
				}else if(protocol.equals(ProtocolMsg.I_WANTTO_AVOID_MINESWEEPER)) {
					System.out.println("he wants to avoid minesweeper");
					boolean masterExit =false;
					int hisRoomNum = (int)command.get(1);
					if(mineSweeperThreadList.get(hisRoomNum).get(0)==this) {
						masterExit=true;
					}
					mineSweeperThreadList.get(hisRoomNum).remove(this);
					idList.get(hisRoomNum).remove(myId);
					if(!mineSweeperThreadList.get(hisRoomNum).isEmpty()) {
						List<Object> list = new ArrayList<Object>();
						list.add(ProtocolMsg.SOMEONE_EXIT);
						list.add(idList.get(hisRoomNum).size());
						for(String s : idList.get(hisRoomNum)) {
							list.add(s);
						}
						broadcasting(mineSweeperThreadList.get(hisRoomNum), list);
					}
					if(masterExit && !mineSweeperThreadList.get(hisRoomNum).isEmpty()) {
						
						
							List<Object> list = new ArrayList<Object>();
							list.add(ProtocolMsg.MINESWEEPER_MASTER_EXIT);
							mineSweeperThreadList.get(hisRoomNum).get(0).sendMsg(list);
							
					}
					
					if(mineSweeperThreadList.get(hisRoomNum).size()==0) {
						System.out.println("이제 아무도 없음");
						gameAlreadyStartList.remove((Integer)hisRoomNum);  // 오류 날 수 있음.
						System.out.println("그래서 얼레디리스트에서 없앰.");
//						List<Object> list = new ArrayList<Object>();
//						list.add(ProtocolMsg.MINESWEEPER_GAME_END);
//						broadcasting(threadList, list);   // 이거 이제 필요없을 듯?
						mineSweeperThreadList.remove((Integer)hisRoomNum);
						idList.remove((Integer)hisRoomNum);
						System.out.println("방 비어서  없애버림 ㄷㄷ");
					}
					
					System.out.println("지뢰찾기 방 개수 : "+mineSweeperThreadList.size());
				}else if(protocol.equals(ProtocolMsg.START_MINESWEEPER)) {
					System.out.println("they want to start Minesweeper");
					int hisRoomNum = (int)command.get(1);
//					minesweeperAlreadyStart = true;
					gameAlreadyStartList.add(hisRoomNum);
					System.out.println("방번호 : " +  hisRoomNum);
					
					GameServer.minesweepOverInfoList.remove(hisRoomNum);  // 이걸로 다 지워질랑가 몰라
					GameServer.minesweepOverInfoList.put(hisRoomNum, new ArrayList<ArrayList<Object> >());
//					GameServer.minesweepOverInfoList.get(hisRoomNum).removeAll(c)
					List<Object> list = new ArrayList<Object>();
					char[][] mineMap = MakeMine.makeMine();
					list.add(ProtocolMsg.MINESWEEPER_PRESTART);
					list.add(mineMap);
					Color[] colors = {Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN};
					int minesweeperNum = mineSweeperThreadList.get(hisRoomNum).size();
					System.out.println(minesweeperNum +" " + hisRoomNum);
					for(int i=0;i<minesweeperNum;i++) {
						mineSweeperThreadList.get(hisRoomNum).get(i).iFailMinesweep=false;
						
						list.add(colors[i]);
						
						mineSweeperThreadList.get(hisRoomNum).get(i).sendMsg(list);
						list.remove(2);
					}
					
					
					
				}else if(protocol.equals(ProtocolMsg.CLICK_SAFEZONE)) {
					System.out.println("he clicks safety zone");
					int hisRoomNum = (int)command.get(5);
//					int heClickRow = (int)command.get(1);
//					int heClickCol = (int)command.get(2);
//					Color hisColor = (Color)command.get(3);
//					List<Object> list = new ArrayList<Object>();
					broadcasting(mineSweeperThreadList.get(hisRoomNum), command);
					
					
				}else if(protocol.equals(ProtocolMsg.USER_FINISH_MINESWEEPER)) {
					System.out.println("user finish minesweeper");
					
					int hisRoomNum = (int)command.get(1);
					
					broadcasting(mineSweeperThreadList.get(hisRoomNum),command);
					if(!mineSweeperThreadList.get(hisRoomNum).isEmpty()) {
						List<Object> list3 = new ArrayList<>();
						list3.add(ProtocolMsg.YOU_ARE_MINESWEEPER_MASTER);
						list3.add(hisRoomNum);
						mineSweeperThreadList.get(hisRoomNum).get(0).sendMsg(list3);
					}
					
					gameAlreadyStartList.remove((Integer)hisRoomNum);
					
				}else if(protocol.equals(ProtocolMsg.I_FAIL_MINESWEEP)) {
					iFailMinesweep= true;
					int hisRoomNum = (int)command.get(1);
					boolean allFail = true;
					
					for(GameServerThread thread : mineSweeperThreadList.get(hisRoomNum)) {
						if(!thread.iFailMinesweep) {
							allFail=false;
						}
					}
					if(allFail) {
						List<Object> list = new ArrayList<Object>();
						list.add(ProtocolMsg.MINESWEEP_ALL_FAIL);
						
						broadcasting(mineSweeperThreadList.get(hisRoomNum),list);
						
						if(!mineSweeperThreadList.get(hisRoomNum).isEmpty()) {
							List<Object> list2 = new ArrayList<>();
							list2.add(ProtocolMsg.YOU_ARE_MINESWEEPER_MASTER);
							list2.add(hisRoomNum);
 							mineSweeperThreadList.get(hisRoomNum).get(0).sendMsg(list2);
						}
						
						gameAlreadyStartList.remove((Integer)hisRoomNum);
					}
					
				}else if(protocol.equals(ProtocolMsg.MINESWEEP_OVER_INFO)) {
					int hisRoomNum = (int)command.get(5);
					GameServer.minesweepOverInfoList.get(hisRoomNum).add(command);
					
					if(GameServer.minesweepOverInfoList.get(hisRoomNum).size()==mineSweeperThreadList.get(hisRoomNum).size()) {
						Collections.sort(GameServer.minesweepOverInfoList.get(hisRoomNum), new Comparator<ArrayList<Object> >() {
							@Override
							public int compare(ArrayList<Object> o1, ArrayList<Object> o2) {
								int cnt1 = (int)o1.get(4);
								int cnt2 = (int)o2.get(4);
								if(cnt1<cnt2) return 1;
								else if(cnt1>cnt2) return -1;
								else return 1;
								
								
							}
							
						});
						List<Object> infolistSpecial = new ArrayList<Object>();
						infolistSpecial.add(ProtocolMsg.MINESWEEP_OVER_ALLINFO);
//						infolist.add(GameServer.minesweepOverInfoList);
						System.out.println(">>"+GameServer.minesweepOverInfoList.get(hisRoomNum));
						List<Object> infoList = new ArrayList<Object>();
						for(ArrayList<Object> tmp1 : GameServer.minesweepOverInfoList.get(hisRoomNum)) {
							System.out.println((String)tmp1.get(1) + tmp1.get(3) + tmp1.get(4));
							ArrayList<Object> tmp2 = new ArrayList<Object>();
							tmp2.add(tmp1.get(1));
							tmp2.add(tmp1.get(3));
							tmp2.add(tmp1.get(4));
							infoList.add(tmp2);
						}
						infolistSpecial.add(infoList);
						
						broadcasting(mineSweeperThreadList.get(hisRoomNum), infolistSpecial);
						
						ProtocolMsg.battleLogInsert(infolistSpecial, "MineSweeper");
						
					}
					
					
					
					
				}else if(protocol.equals(ProtocolMsg.I_WANT_RANKING)) {
					List<Object> vNum = new ArrayList<Object>();
					vNum.add(ProtocolMsg.VICTORY_NUM);
					vNum.add(ProtocolMsg.getRank());
					oos.writeObject(vNum);
					
				}else if(protocol.equals(ProtocolMsg.I_WANT_BATTLELOG)) {
					List<Object> logList = new ArrayList<Object>();
					logList.add(ProtocolMsg.I_WANT_BATTLELOG);
					logList.add(ProtocolMsg.getBattleLog());
					oos.writeObject(logList);
					
				}
				
				System.out.println(command.toString());
			}
			
			
			
			
			}catch(Exception e) {
				System.out.println("연결 하나 끊김.");
				e.printStackTrace();
				threadList.remove(this);
			}
		
	}
	
	public void broadcasting(List<GameServerThread> sendList,List<Object> msgList ) {
		
		for(GameServerThread thread : sendList) {
			thread.sendMsg(msgList);
		}
		
	}
	
	public void sendMsg(List<Object> msgList) {
		
		try {
			oos.writeObject(msgList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

}
