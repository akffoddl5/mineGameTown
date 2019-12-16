package com.java.miniClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MakeMenu extends JFrame implements ListSelectionListener , MouseListener {
	
	private Container con = this.getContentPane();
	private JPanel preTop = new JPanel();
	private JPanel preRight = new JPanel();
	private JPanel preBottom = new JPanel();
	private JPanel preLeft = new JPanel();
	private JPanel main = new JPanel(new BorderLayout(5,5));
	
	private JSplitPane splitPane = null;
	
	private JList gameList =null;
	private JTextArea gameInfoArea = new JTextArea(12,27);
	private JScrollPane gameInfoAreaScrollPane = new JScrollPane(gameInfoArea);
	
	JButton logoutBt = new JButton("Logout");
	JButton startBt = new JButton("START");
	
	//전적,랭킹 dialog
	public static JDialog ranking = null;
	public static JDialog log = null;
	
	public MakeMenu() {

		makePre();
		
		makeSplit();
		
		con.setBackground(new Color(0xfeb81c));
		makeButtons();
		setEvent();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(930,620);
		setLocationRelativeTo(null);
		gameInfoArea.setFont(new Font("나눔고딕",0,21));
		gameList.setFont(new Font("나눔고딕",0,17));
		gameList.setBorder(new LineBorder(Color.red,2));
		gameInfoArea.setEditable(false);
		
	}
	
	
	private void setEvent() {
		gameList.addListSelectionListener(this);
		logoutBt.addMouseListener(this);
		startBt.addMouseListener(this);
	}
	
	
	private void makePre() {
		con.setLayout(new BorderLayout(30,30));
		con.add("North",preTop);
		con.add("East",preRight);
		con.add("South",preBottom);
		con.add("West",preLeft);
		con.add("Center",main);
		preTop.setBackground(new Color(0xfeb81c));
		preRight.setBackground(new Color(0xfeb81c));
		preBottom.setBackground(new Color(0xfeb81c));
		preLeft.setBackground(new Color(0xfeb81c));
		main.setBackground(new Color(0xfeb81c));
		
		gameInfoArea.setBackground(new Color(0xfeb81c));
		gameInfoArea.setBorder(new LineBorder(Color.red,2));
		
	}
	
	private void makeSplit() {
		Vector<Object> v = new Vector<>();
		v.add("다 같이 지뢰찾기");
		v.add("지뢰피해 이동하기");
		v.add("전적 보기");
		v.add("랭킹 보기");
		gameList = new JList(v);
		gameList.setBackground(new Color(0xfeb81c));
		JPanel left = new JPanel();
		JPanel right = new JPanel();
		
//		gameInfoAreaScrollPane.add(gameInfoArea);
		left.add(gameList);
		right.add(gameInfoAreaScrollPane);
		left.setBackground(new Color(0xfeb81c));
		right.setBackground(new Color(0xfeb81c));
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left,right);
		splitPane.setPreferredSize(new Dimension(150,150));
		splitPane.setDividerLocation(150);
		splitPane.setBackground(new Color(0xfeb81c));
		main.add("Center" , splitPane);
		
	}
	
	private void makeButtons() {
		JPanel left = new JPanel(new BorderLayout(5,5));
		JPanel right = new JPanel(new BorderLayout(5,5));
		JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel topLb = new JLabel("게임을 선택하세요.");
		left.setBackground(new Color(0xfeb81c));
		right.setBackground(new Color(0xfeb81c));
		top.setBackground(new Color(0xfeb81c));
		topLb.setBackground(new Color(0xfeb81c));
		
		top.add(topLb);
		left.add("South",logoutBt);
		right.add("South",startBt);
		main.add("West",left);
		main.add("East", right);
		main.add("North",top);
		
	}
	
	
	public static void makeRanking(Object command1) {
		List<ArrayList<Object>> commandGet1 = (List<ArrayList<Object>>)command1;
		ranking= new JDialog();
		ranking.setLayout(new BorderLayout(5,5));
		ranking.setSize(700,520);
		ranking.setLocationRelativeTo(null);
		ranking.setTitle("Ranking ... " );
		JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);
		
		////////////// tab 1 ///////////////
		Vector<String> v1= new Vector<String>();
		int rank1Num = commandGet1.get(0).size()/3;
		int rank2Num = commandGet1.get(1).size()/3;
		int rank3Num = commandGet1.get(2).size()/3;
		int rank4Num = commandGet1.get(3).size()/3;
		Map<String,Integer> rank1Map = new HashMap<>();
		Map<String,Integer> mineMap = new HashMap<>();
		for(int i=0;i<rank1Num;i++) {
			String id = (String)commandGet1.get(0).get(i*3);
			int cnt = (int)commandGet1.get(0).get(i*3+2);
			v1.add(Integer.toString(i+1) + "등 : " + id + " :              " + Integer.toString(cnt) + " 승");
			
			mineMap.put(id,cnt);
			rank1Map.put(id,cnt);
		}
		JList<String> list1 = new JList<String>(v1);
		list1.setFont(new Font("Serif",Font.PLAIN,24));
		
		JScrollPane scroll1 = new JScrollPane(list1);
		//////////
		
		for(int i=0;i<rank2Num;i++) {
			String id = (String)commandGet1.get(1).get(i*3);
			int cnt = (int)commandGet1.get(1).get(i*3+2);
			
			if(!mineMap.containsKey(id)) {
				mineMap.put(id,cnt);				
			}else {
				mineMap.replace(id, mineMap.get(id)+cnt);
			}
		}
		
		
		////////////
		
		for(int i=0;i<rank3Num;i++) {
			String id = (String)commandGet1.get(2).get(i*3);
			int cnt = (int)commandGet1.get(2).get(i*3+2);
			
			if(!mineMap.containsKey(id)) {
				mineMap.put(id,cnt);				
			}else {
				mineMap.replace(id, mineMap.get(id)+cnt);
			}
		}
		
		
		////
		
		
		for(int i=0;i<rank4Num;i++) {
			String id = (String)commandGet1.get(3).get(i*3);
			int cnt = (int)commandGet1.get(3).get(i*3+2);
			
			if(!mineMap.containsKey(id)) {
				mineMap.put(id,cnt);				
			}else {
				mineMap.replace(id, mineMap.get(id)+cnt);
			}
		}
		List<List<Object>> list = new ArrayList<List<Object>>();
		for(String id : mineMap.keySet()) {
			List<Object> inList = new ArrayList<Object>();
			int total = mineMap.get(id);
			
			int vicNum=0;
			if(rank1Map.containsKey(id)) {
				vicNum = rank1Map.get(id);
			}else {
				
			}
			double rate = 100.0*vicNum/total;
			if(total>=5) {
				inList.add(id);
				inList.add(rate);
				list.add(inList);
			}
			
		}
		
		Collections.sort(list, new Comparator<List<Object>>() {
			public int compare(List<Object> o1,List<Object> o2) {
				double rate1 = (double)o1.get(1);
				double rate2 = (double)o2.get(1);
				if(rate1<rate2) return 1;
				else if(rate1>rate2) return -1;
				else return 0;
				
			};
		});
		
		Vector<String> v2 = new Vector<String>();
		int j=0;
		for(List<Object> pp : list) {
			String pid = "";
			pid=(String)pp.get(0);
			if(!(pp.get(0)==null)) {
				j++;
				double tmp = (int)(((Double)pp.get(1))*1000)/1000.0;
				String s = Integer.toString(j)+"등 : " + pp.get(0) +"            " + Double.toString(tmp) + "%";
				v2.add(s);
			}
			
			
		}
		
		JList<String> ratingList = new JList<String>(v2);
		ratingList.setFont(new Font("Serif",Font.PLAIN,24));
		JScrollPane scroll2 = new JScrollPane(ratingList);
		
		
		tabPane.add(scroll1,"지뢰찾기 다승 RANK");
		tabPane.add(scroll2,"지뢰찾기 승률 RANK");
		tabPane.add(null,"길찾기 다승 RANK");
		tabPane.add(null,"길찾기 승률 RANK");
		ranking.add("Center", tabPane);
		
		

		tabPane.setVisible(true);
		ranking.setVisible(true);
		
	}
	
	public static void makeBattleLog(Object loglist1) {
		List<ArrayList<Object>> battleLogList = (List<ArrayList<Object>>)loglist1;
		log= new JDialog();
		log.setLayout(new BorderLayout(5,5));
		log.setSize(800,600);
		log.setLocationRelativeTo(null);
		log.setTitle("battle log" );
		
		List<String> v= new ArrayList<String>();
		v.add("배틀번호");
		v.add("게임종류");
		v.add("게임인원");
		v.add("승자");
		v.add("패자1");
		v.add("패자2");
		v.add("패자3");
		List<ArrayList<String>> data1 = new ArrayList<ArrayList<String>>();
		for(ArrayList<Object> singleLog : battleLogList) {
			int btid = (int)singleLog.get(0);
			String btGame = (String)singleLog.get(1);
			int btUserNum = (int)singleLog.get(2);
			String btPersonId1 = (String)singleLog.get(3);
			String btPersonId2 = (String)singleLog.get(4);
			String btPersonId3 = (String)singleLog.get(5);
			String btPersonId4 = (String)singleLog.get(6);
			int btUserScore1 = (int)singleLog.get(7);
			int btUserScore2 = (int)singleLog.get(8);
			int btUserScore3 = (int)singleLog.get(9);
			int btUserScore4 = (int)singleLog.get(10);
			ArrayList<String> singleData1 = new ArrayList<String>();
			singleData1.add(Integer.toString(btid));
			singleData1.add(btGame);
			singleData1.add(Integer.toString(btUserNum));
			singleData1.add(btPersonId1+"("+Integer.toString(btUserScore1)+")");
			if(!(btPersonId2==null))
			singleData1.add(btPersonId2+"("+Integer.toString(btUserScore2)+")");
			else singleData1.add("");
			if(!(btPersonId3==null))
			singleData1.add(btPersonId3+"("+Integer.toString(btUserScore3)+")");
			else singleData1.add("");
			if(!(btPersonId4==null))
			singleData1.add(btPersonId4+"("+Integer.toString(btUserScore4)+")");
			else singleData1.add("");
			data1.add(singleData1);
//			String ans = Integer.toString(btid)+"\t"+btGame+"\t"+Integer.toString(btUserNum)+"\t"
//					+btPersonId1+"("+btUserScore1+")\t"+btPersonId2+"("+btUserScore2+")\t"+btPersonId3+"("+btUserScore3+")\t"+
//					btPersonId4+"("+btUserScore4+")\t";
//			v.add(ans);
		}
		JTable table;
		BattleLogTableModel model = new BattleLogTableModel(data1, v);
		table = new JTable(model);
		table.setFont(new Font("나눔고딕",0,16));
		
//		table.getColumn(0).getcell
		JScrollPane scroll1 = new JScrollPane(table);
		log.add(scroll1);
		
		log.setVisible(true);
	}
	


	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource()==gameList) {
			System.out.println("listener exe");
			String selectedGame = (String)gameList.getSelectedValue();
			
			
			System.out.println(selectedGame);
			if(selectedGame.equals("다 같이 지뢰찾기")) {
				gameInfoArea.setText("다 같이 지뢰를 피해 빈칸을 최대한 많이 클릭하세요. \n  가장 많이 빈칸을 누른 사람이 승리합니다. \n 인원 제한 없음");
				startBt.setEnabled(true);
			}else if(selectedGame.equals("지뢰피해 이동하기")) {
				gameInfoArea.setText("지뢰가 아닌 경로로 목적지에 도달하세요. \n  단 한번 상하좌우의 지뢰를 파괴할수 있는 스킬을 \n 사용할 수 있습니다. \n 인원 제한 없음");
				startBt.setEnabled(true);
			}else if(selectedGame.equals("전적 보기")) {
				gameInfoArea.setText("최근에 이뤄진 게임에 대한 정보를 보여줍니다. \n 인원 제한 없음");
				startBt.setEnabled(true);
			}else if(selectedGame.equals("랭킹 보기")) {
				gameInfoArea.setText("유저들의 랭킹정보를 보여줍니다. \n 인원 제한 없음");
				startBt.setEnabled(true);
			}
		}
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
		if(e.getSource()==logoutBt) {
			GameClientThread.canLogin=-1;
			this.setVisible(false);
			GameClientThread.makeLogin.setVisible(true);
		}else if(e.getSource()==startBt) {
			
			String selectedGame = (String)gameList.getSelectedValue();
			if(selectedGame.equals("다 같이 지뢰찾기")) {
				
				
				ArrayList<Object> iwantMinesweeper = new ArrayList<>();
				iwantMinesweeper.add(ProtocolMsg.I_WANT_MINESWEEPER);
				try {
					System.out.println("지뢰찾기 시작 가능? : "+GameClientThread.canMineSweeper);
					MakeLogin.oos.writeObject(iwantMinesweeper);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println("지뢰찾기 시작 가능? : "+GameClientThread.canMineSweeper);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if(GameClientThread.canMineSweeper) {
					
					this.setVisible(false);
					if(GameClientThread.master)
					GameClientThread.makeMineSweeper.startBt.setEnabled(true);
					GameClientThread.makeMineSweeper.setVisible(true);
					
				}else {
					JOptionPane.showMessageDialog(this,"게임이 이미 시작했습니다. 다음기회에 ..");
				}
				
				
				
			}else if(selectedGame.equals("지뢰피해 이동하기")) {
				this.setVisible(false);
				
				GameClientThread.makeMineAvoider.startBt.setEnabled(true);
				GameClientThread.makeMineAvoider.setVisible(true);
				
			}else if(selectedGame.equals("랭킹 보기")) {
				List<Object> ranklist = new ArrayList<>();
				ranklist.add(ProtocolMsg.I_WANT_RANKING);
				try {
					MakeLogin.oos.writeObject(ranklist);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}else if(selectedGame.equals("전적 보기")) {
				List<Object> loglist = new ArrayList<Object>();
				loglist.add(ProtocolMsg.I_WANT_BATTLELOG);
				try {
					MakeLogin.oos.writeObject(loglist);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
			}
			
			
		}
	}
	

}
