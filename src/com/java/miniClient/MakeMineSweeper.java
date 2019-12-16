package com.java.miniClient;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;



public class MakeMineSweeper extends JFrame implements MouseListener, ActionListener {

	/**

	 * 

	 */

	private static final long serialVersionUID = 1L;

	private JPanel main = new JPanel(new BorderLayout(20,5));
	private JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private static JPanel right = new JPanel(new BorderLayout(5,150));
	private JPanel middle = new JPanel(new BorderLayout(200,50));
	private JPanel top = new JPanel(new FlowLayout());
	private JPanel mainTop = new JPanel(new FlowLayout());
	private JPanel mainBottom = new JPanel(new FlowLayout());
	private JPanel mainLeft = new JPanel(new FlowLayout());
	private JPanel mainRight = new JPanel(new FlowLayout());
	private Container con = this.getContentPane();
	
	private int a;
	private int b;
	
	public static JButton[][] buttons;
	public static int checkButtons[][] ;
	
	
	private JButton backMenuBt = new JButton("back to Menu");
	public static JButton startBt = new JButton("START GAME");

	private static JList<String> js = new JList<String>();
	private JTextArea mineCnt = new JTextArea(5,15);
	public static JLabel roomNumLabel = new JLabel("<>");
	
	public static boolean imMaster = false;
	
	//progress 관련
	public static JProgressBar jpb = new JProgressBar(JProgressBar.HORIZONTAL, 0,100);
	private static JDialog dialog =  null;
	private static JDialog gameOverInfoDialog=null;
	
	//mine 관련
	public static char[][] minesMap ;
	public static Color myColor ;
	
	//승패관련
	public static boolean gameFail = false;
	public static int cnt = 0;
	public static int remainMineCnt = 100;
	
	private int[] bp1 = {0,1,0,-1};
	private int[] bp2 = {1,0,-1,0};
	
	public MakeMineSweeper() {
		
		con.setLayout(new BorderLayout(5,5));
		dialog= new JDialog(this);
		
		makePre();
		makeMain();
		roomNumLabel.setFont(new Font("Serif",Font.PLAIN,25));
//		mineCnt.setFont(new Font("나눔고딕",Font.PLAIN,15));
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(imMaster) {
			startBt.setEnabled(true);
		}else {
			startBt.setEnabled(false);
		}
		
		setSize(1280,720);
		setLocationRelativeTo(null);
		setVisible(false);
		
	}



	private void makePre() {

		con.add("Center",main);
		con.add("South",mainBottom);
		con.add("East",mainRight);
		con.add("West",mainLeft);
		con.add("North",mainTop);
		
		con.setBackground(new Color(0xfeb81c));
		main.setBackground(new Color(0xfeb81c));
		mainBottom.setBackground(new Color(0xfeb81c));
		mainRight.setBackground(new Color(0xfeb81c));
		mainLeft.setBackground(new Color(0xfeb81c));
		mainTop.setBackground(new Color(0xfeb81c));
		mineCnt.setBackground(new Color(0xfeb81c));
//		mineCnt.setFont(new Font("나눔고딕",Font.PLAIN,35));
	}



	private void makeMain() {

		main.add("North", top);
		main.add("Center", middle);
		main.add("East", right);
		main.add("South", bottom);
		
		top.setBackground(new Color(0xfeb81c));
		middle.setBackground(new Color(0xfeb81c));
		right.setBackground(new Color(0xfeb81c));
		bottom.setBackground(new Color(0xfeb81c));

		bottom.add(backMenuBt);

		//

		a = 16;
		b= 16;

		System.out.println(a+" "+b);
		buttons= new JButton[a][b];
		JPanel minePanelUp = new JPanel(new BorderLayout(5,5));
		JPanel yellow = new JPanel(new FlowLayout());
		yellow.add(roomNumLabel);
		JPanel minePanel = new JPanel(new GridLayout(a, b));
		
		minePanel.setSize(200, 200);
		minePanel.setSize(new Dimension(50,50));
		
		minePanel.setBackground(new Color(0xfeb81c));
		yellow.setBackground(new Color(0xfeb81c));
		minePanelUp.setBackground(new Color(0xfeb81c));
		
		for(int i=0;i<a;i++) {

			for(int j=0;j<b;j++) {

				buttons[i][j]=new JButton();
				buttons[i][j].setPreferredSize(new Dimension(5,5));
				
				minePanel.add(buttons[i][j]);

			}

		}
		
		buttonsConfig();


		//

		minePanelUp.add("North",yellow);
		minePanelUp.add("Center",minePanel);

		//		minePanelUp.add("East",right);

		right.add("North",js);
		right.add("South",mineCnt);
		right.add("Center",startBt);
		mineCnt.setText("남은 지뢰 : " + remainMineCnt/2);
		middle.add("East",right);
		middle.add("Center",minePanelUp);
		middle.add("West",new JLabel(""));

	}

	
	private void buttonsConfig() {
		
		backMenuBt.addMouseListener(this);
		startBt.addMouseListener(this);
		
		for(int i=0;i<a;i++) {
			for(int j=0;j<b;j++) {
				buttons[i][j].removeActionListener(this);
				buttons[i][j].addMouseListener(this);
				buttons[i][j].addActionListener(this);
			}
		}
	}
	
	public void makeDialog() {
		dialog.setLayout(new BorderLayout(5,5));
		dialog.add("Center",jpb);
		dialog.setSize(500, 200);
		dialog.setLocationRelativeTo(null);
		remainMineCnt = 100;
		mineCnt.setText("남은 지뢰 : " + remainMineCnt/2);
		jpb.setStringPainted(true);
		jpb.setString("0 %");
		
		//preStart
		checkButtons = new int[16][16];
		for(int i=0;i<16;i++) {
			for(int j=0;j<16;j++) {     // 여기 좀 이상함.. 여기때매 여러분 클릭인듯?
				buttons[i][j].setIcon(new ImageIcon());
				buttons[i][j].setBorder(new LineBorder(Color.black,1));
				buttons[i][j].addMouseListener(this);
				buttons[i][j].removeActionListener(this);
				buttons[i][j].addActionListener(this);
			}
		}
		startBt.setEnabled(false);
		//
		
		
		dialog.setVisible(true);
		runProgressbar();
		
		
	}
	
	public static void runProgressbar() {
		
		for(int i=0;i<=100;i++) {
			try {
				Thread.sleep(30);
			}catch(InterruptedException ie) {
				ie.printStackTrace();
			}
			jpb.setValue(i);
			jpb.setString("방 설정중 .. \n" + i+" %");
		}
		dialog.setVisible(false);
		
	}
	
	public void buttonReset() {
		for(int i=0;i<16;i++) {
			for(int j=0;j<16;j++) {
				buttons[i][j].setIcon(new ImageIcon());
				buttons[i][j].setBorder(new LineBorder(Color.black,1));
				buttons[i][j].addActionListener(this);
				buttons[i][j].addMouseListener(this);
				
			}
		}
		
		startBt.setEnabled(false);
	}
	
	public static void makeGameInfoDialog(Object infoList1) {
		gameOverInfoDialog = null;
		gameOverInfoDialog = new JDialog();
		gameOverInfoDialog.setTitle("순위 발표");
		Object getInfoList = infoList1;
		ArrayList<ArrayList<Object>> infoList = (ArrayList<ArrayList<Object> >)getInfoList;
		System.out.println("인포 나눠준 인원 : " + infoList.size());
		
		
		
		gameOverInfoDialog.setLayout(new BorderLayout(5,5));
		gameOverInfoDialog.setSize(400,400);
		gameOverInfoDialog.setLocationRelativeTo(null);
		JPanel p1 = new JPanel(new FlowLayout());
		
//		JTextArea ta = new JTextArea(70,70);
		
		int i=0;
		for(ArrayList<Object> singleInfo : infoList) {
			
			i++;
			JTextField tf  = new JTextField(20);
			String id = (String)singleInfo.get(0);
			Color color = (Color)singleInfo.get(1);
			int cnt = (int)singleInfo.get(2);
			
			JPanel p2 = new JPanel(new FlowLayout());
			JLabel l1 = new JLabel(i+ "등");
			
			String wholeInfo = id+" : " + Integer.toString(cnt);
			System.out.println(id + color + cnt);
			tf.setText(wholeInfo);
			tf.setDisabledTextColor(color);
			p2.add(l1);
			p2.add(tf);
			p2.setBackground(color);
			p1.add(p2);
		}
		gameOverInfoDialog.add("Center",p1);
		gameOverInfoDialog.setVisible(true);
		
		
	}
	
	public void setRight(Vector<String> idlist2) {
		System.out.println(idlist2);
		js.setListData(idlist2);
//		right.removeAll();
//		JList<String> tmp = new JList<String>();
//		tmp.set
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
		
		
	}



	@Override
	public void mouseEntered(MouseEvent e) {
		
	}



	@Override
	public void mouseExited(MouseEvent e) {
		
	}



	@Override
	public void mousePressed(MouseEvent e) {


	}



	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getSource()== backMenuBt) {
			ArrayList<Object> list = new ArrayList<>();
			list.add(ProtocolMsg.I_WANTTO_AVOID_MINESWEEPER);
			list.add(GameClientThread.myRoomNum);
			imMaster=false;
			startBt.setEnabled(false);
			try {
				MakeLogin.oos.writeObject(list);
			} catch (IOException e1) {
				System.out.println("빠져나갈 수 없다.");
			}
			
			GameClientThread.makeMineSweeper.setVisible(false);
			GameClientThread.makeMenu.setVisible(true);
			
		}else if(e.getSource() == startBt) {
			ArrayList<Object> list = new ArrayList();
			list.add(ProtocolMsg.START_MINESWEEPER);
			list.add(GameClientThread.myRoomNum);
			try {
				MakeLogin.oos.writeObject(list);
			} catch (IOException e1) {
				System.out.println("시작할 수 없다.");
			}
		}else {
			Object source = e.getSource();
			boolean isRight = e.isPopupTrigger();
			
			for(int i=0;i<a;i++) {
				for(int j=0;j<b;j++) {
					if(buttons[i][j]==source && isRight) {
						System.out.println("right");
						remainMineCnt--;
						mineCnt.setText("남은 지뢰 : " + remainMineCnt/2);
						buttons[i][j].setIcon(new ImageIcon("src/flag.png"));
						break;
					}
					
					
				}
			}
			
		}
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()== backMenuBt) {
//			ArrayList<Object> list = new ArrayList<>();
//			list.add(ProtocolMsg.I_WANTTO_AVOID_MINESWEEPER);
//			imMaster=false;
//			startBt.setEnabled(false);
//			try {
//				MakeLogin.oos.writeObject(list);
//			} catch (IOException e1) {
//				System.out.println("빠져나갈 수 없다.");
//			}
//			
//			GameClientThread.makeMineSweeper.setVisible(false);
//			GameClientThread.makeMenu.setVisible(true);
			
		}else if(e.getSource() == startBt) {
//			ArrayList<Object> list = new ArrayList();
//			list.add(ProtocolMsg.START_MINESWEEPER);
//			try {
//				MakeLogin.oos.writeObject(list);
//			} catch (IOException e1) {
//				System.out.println("시작할 수 없다.");
//			}
		}else {
			Object source = e.getSource();
			boolean isRight = false;
			
			for(int i=0;i<a;i++) {
				for(int j=0;j<b;j++) {
					if(buttons[i][j]==source && isRight) {
//						System.out.println("right");
//						buttons[i][j].setIcon(new ImageIcon("src/flag.png"));
//						break;
					}else if(buttons[i][j]==source) {
						
						System.out.println("left");
						System.out.println(minesMap[i][j]);
						if(minesMap[i][j]!='X') {
							buttons[i][j].removeMouseListener(this);
							buttons[i][j].removeActionListener(this);
							buttons[i][j].setBorder(new LineBorder(myColor,5));
							checkButtons[i][j]=1;
							
							List<Object> list = new ArrayList<>();
							list.add(ProtocolMsg.CLICK_SAFEZONE);
							list.add(i);
							list.add(j);
							list.add(myColor);
							list.add(minesMap[i][j]);
							list.add(GameClientThread.myRoomNum);
							
							
							
							try {
								MakeLogin.oos.writeObject(list);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							this.cnt++;
							System.out.println("cnt++");
							
							//0 터뜨리기
							if(minesMap[i][j]=='0') {
								int[][] check = new int[16][16];
								Queue<Pos> q = new LinkedList<Pos>();
								q.add(new Pos(i,j));
								check[i][j]=1;
								while(!q.isEmpty()) {
									int topRow = q.peek().row;
									int topCol = q.peek().col;
									int[] bp3 = {0,1,0,-1,1,1,-1,-1};
									int[] bp4 = {1,0,-1,0,1,-1,-1,1};
									for(int k=0;k<8;k++) {
										int qwerRow = topRow+bp3[k];
										int qwerCol = topCol+bp4[k];
										if(qwerRow>=0 && qwerCol>=0 && qwerRow<16 && qwerCol<16) {
											if(check[qwerRow][qwerCol]==0 && 
													minesMap[qwerRow][qwerCol]!='X'
													&&minesMap[qwerRow][qwerCol]!='0') {
												
												List<Object> listTo2 = new ArrayList<>();
												listTo2.add(ProtocolMsg.CLICK_SAFEZONE);
												listTo2.add(qwerRow);
												listTo2.add(qwerCol);
												listTo2.add(myColor);
												listTo2.add(minesMap[qwerRow][qwerCol]);
												listTo2.add(GameClientThread.myRoomNum);
												check[qwerRow][qwerCol] = 1;
												checkButtons[qwerRow][qwerCol]= 1;
												cnt++;
												try {
													MakeLogin.oos.writeObject(listTo2);
												} catch (IOException e1) {
													e1.printStackTrace();
												}
												
											}
										}
									}
									
									for(int p=0;p<8;p++) {
										int nextRow = topRow + bp3[p];
										int nextCol = topCol + bp4[p];
										if(nextRow>=0 && nextCol >=0 && nextRow < 16 && nextCol<16) {
											if(minesMap[nextRow][nextCol]=='0' && check[nextRow][nextCol]==0) {
												q.add(new Pos(nextRow,nextCol));
												
												List<Object> listTo = new ArrayList<>();
												listTo.add(ProtocolMsg.CLICK_SAFEZONE);
												listTo.add(nextRow);
												listTo.add(nextCol);
												listTo.add(myColor);
												listTo.add(minesMap[nextRow][nextCol]);
												listTo.add(GameClientThread.myRoomNum);
												check[nextRow][nextCol] = 1;
												checkButtons[nextRow][nextCol]= 1;
												cnt++;
												try {
													MakeLogin.oos.writeObject(listTo);
												} catch (IOException e1) {
													e1.printStackTrace();
												}
												
												check[nextRow][nextCol]=1;
											}
										}
									}
									
									q.poll();
								}
							}
							
							
							
							boolean gameOver = true;
							for(int p1=0;p1<a;p1++) {
								for(int p2=0;p2<b;p2++) {
									if(checkButtons[p1][p2]==0) {
										gameOver=false;
										break;
									}
								}
							}
							if(gameOver) {
								List<Object> gameOverMsg = new ArrayList<Object>();
								gameOverMsg.add(ProtocolMsg.USER_FINISH_MINESWEEPER);
								gameOverMsg.add(GameClientThread.myRoomNum);
								try {
									MakeLogin.oos.writeObject(gameOverMsg);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
							
						}else {
							System.out.println("탈락 메시지");
							JOptionPane.showMessageDialog(this,"지뢰 클릭! 탈락입니다.");
							gameFail=true;
							for(int p1=0;p1<a;p1++) {
								for(int p2=0;p2<b;p2++) {
									buttons[p1][p2].removeMouseListener(this);
									buttons[p1][p2].removeActionListener(this);
								}
							}
							
							List<Object> list = new ArrayList<Object>();
							list.add(ProtocolMsg.I_FAIL_MINESWEEP);
							list.add(GameClientThread.myRoomNum);
							try {
								MakeLogin.oos.writeObject(list);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							
						}
						
						if(minesMap[i][j]=='1') {
							buttons[i][j].setIcon(new ImageIcon("src/safe1.png"));
						}else if(minesMap[i][j]=='2') {
							buttons[i][j].setIcon(new ImageIcon("src/safe2.png"));
						}else if(minesMap[i][j]=='3') {
							buttons[i][j].setIcon(new ImageIcon("src/safe3.png"));
						}else if(minesMap[i][j]=='4') {
							buttons[i][j].setIcon(new ImageIcon("src/safe4.png"));
						}else if(minesMap[i][j]=='5') {
							buttons[i][j].setIcon(new ImageIcon("src/safe5.png"));
						}else if(minesMap[i][j]=='6') {
							buttons[i][j].setIcon(new ImageIcon("src/safe6.png"));
						}else if(minesMap[i][j]=='7') {
							buttons[i][j].setIcon(new ImageIcon("src/safe7.png"));
						}else if(minesMap[i][j]=='8') {
							buttons[i][j].setIcon(new ImageIcon("src/safe8.png"));
						}else if(minesMap[i][j]=='X') {
							buttons[i][j].setIcon(new ImageIcon("src/mine.png"));
						}
						
						break;
					}
					
					
				}
			}
			
		}
		
		
	}





}