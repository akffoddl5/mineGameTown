package com.java.miniClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

 
public class MakeLogin extends JFrame implements MouseListener {

	

	/**

	 * 

	 */

	private static final long serialVersionUID = 1L;

	private JPanel preLeft = new JPanel();
	private JPanel preRight = new JPanel();
	private JPanel preBottom = new JPanel();
	private JPanel preTop = new JPanel();
	private JPanel main = new JPanel(new BorderLayout(5,3));
	private Container con = this.getContentPane();
	
	
	private JTextField idField = new JTextField(12);
	private JTextField pwField = new JTextField(12);
	
	private JButton enter = new JButton("접속");
	
	private Socket socket;
	
	public static ObjectOutputStream oos;
//	private ObjectInputStream ois = null;
	
	public static String myId = null;
	public static String myPw = null;
	
	
	
	
	public MakeLogin(Socket socket1) {
		this.socket = socket1;
		try {
		oos= new ObjectOutputStream(socket.getOutputStream());
//		ois= new ObjectInputStream(socket.getInputStream());
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		makePre();
		makeMain();
		setEvent();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500,430);
		con.setBackground(new Color(0xfeb81c));
		Icon imgIcon = new ImageIcon("src/login4.png");
		
		JLabel imgLb = new JLabel(imgIcon);
		imgLb.setBackground(new Color(0xfeb81c));
		imgLb.setSize(50,50);
		preTop.add(imgLb);
		setBackground(new Color(0xfeb81c));
		preTop.setBackground(new Color(0xfeb81c));
		preBottom.setBackground(new Color(0xfeb81c));
		preLeft.setBackground(new Color(0xfeb81c));
		main.setBackground(new Color(0xfeb81c));
		preRight.setBackground(new Color(0xfeb81c));
		setLocationRelativeTo(null);
		idField.setFont(new Font("Serif",Font.PLAIN,24));
		pwField.setFont(new Font("Serif",Font.PLAIN,24));
		enter.setFont(new Font("Serif",Font.PLAIN,15));
		setVisible(true);

	}

	

	public static void main(String[] args) {
		Socket socketTmp = null;
		new MakeLogin(socketTmp);

	}

	

	private void makePre() {

		con.setLayout(new BorderLayout(5,5));
//		Toolkit toolkit = this.getToolkit();
//		Image image = toolkit.createImage("C:\\Users\\Administrator\\Desktop\\반응형자바웹\\giphy.gif");
//		JLabel lb1 = new JLabel();
//		lb1.setIcon(icon);
		
		con.add("North",preTop);
		con.add("East",preRight);
		con.add("South",preBottom);
		con.add("West",preLeft);
		con.add("Center",main);

	}

	

	private void makeMain() {

		main.setBackground(new Color(0xfeb81c));

		JPanel upPanel = new JPanel(new FlowLayout());
		JPanel downPanel = new JPanel(new FlowLayout());
		JPanel loginPanel = new JPanel(new GridLayout(2,1));
		JPanel gridPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		

		upPanel.add(new JLabel(" ID  "));
		upPanel.add(idField);
		
		downPanel.add(new JLabel("PW"));
		downPanel.add(pwField);
		upPanel.setFont(new Font("Serif",Font.PLAIN,25));
		downPanel.setFont(new Font("Serif",Font.PLAIN,25));
		upPanel.setBackground(new Color(0xfeb81c));
		downPanel.setBackground(new Color(0xfeb81c));
		loginPanel.add(upPanel);
		loginPanel.add(downPanel);
		loginPanel.setBackground(new Color(0xfeb81c));
		
		gridPanel.add(loginPanel);
		gridPanel.add(enter);
		gridPanel.setBackground(new Color(0xfeb81c));
		gridPanel.setAlignmentX(CENTER_ALIGNMENT);
		idField.setBorder(new LineBorder(Color.red,1));
		pwField.setBorder(new LineBorder(Color.red,1));
		JPanel titlePanel = new JPanel(new FlowLayout());

		titlePanel.add(new JLabel("지 뢰 게 임 천 국"));
		titlePanel.setBackground(new Color(0xfeb81c));
		titlePanel.setFont(new Font("Serif",Font.PLAIN,35));

		main.add("Center", gridPanel);
//		main.add("North",titlePanel);

	}
	
	private void setEvent() {
		enter.addMouseListener(this);
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
		
		
		
		
		if(e.getSource() == enter) {
			System.out.println("enter click");
			String id = idField.getText();
			String pw = pwField.getText();
			
			
			String msg = ProtocolMsg.LOGIN;
			
//			ois.reset();
//			oos.reset();
			
			boolean loginPermit = false;
			if(id.trim().length()<4) {
				JOptionPane.showMessageDialog(this,"아이디를 4글자 이상 입력해주세요.");
			}else if(pw.trim().length()<4) {
				JOptionPane.showMessageDialog(this,"비밀번호를 4글자 이상 입력해주세요.");
			}else {
				try {
//					oos = new ObjectOutputStream(socket.getOutputStream());
//					ois = new ObjectInputStream(socket.getInputStream());
					List<Object> command = new ArrayList<>();
					command.add(msg);
					command.add(id);
					command.add(pw);
					oos.writeObject(command);
					Thread.sleep(1300);
					int canLogin = GameClientThread.canLogin;
					System.out.println(canLogin);
					if(canLogin==1) {
						loginPermit = true;
						JOptionPane.showMessageDialog(this,"환영합니다.");
//						try{if(oos!=null) oos.close();}catch(IOException ioe) {}
						myId = id;
						myPw = pw;
						setVisible(false);
						GameClientThread.makeMenu.setSize(930,620);
						GameClientThread.makeMenu.setVisible(true);
					}else if(canLogin==2) {
						JOptionPane.showMessageDialog(this,"비밀번호가 틀렸습니다~.");
					}else if(canLogin==0) {

						String[] str = {"예", "아니오"};
						int select = JOptionPane.showOptionDialog(this,"등록된 아이디가 아닙니다. 이대로 접속하시겠습니까?","warning",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,str,str[0]);
						if(select == JOptionPane.YES_OPTION) {
							ArrayList<Object> justLogMsg = new ArrayList<Object>();
							justLogMsg.add(ProtocolMsg.JUST_LOGIN);
							justLogMsg.add(id);
							justLogMsg.add(pw);
							
							oos.writeObject(justLogMsg);
							
							
							JOptionPane.showMessageDialog(this,"환영합니다.");
//							try{if(oos!=null) oos.close();}catch(IOException ioe) {}
							myId = id;
							myPw = pw;
							setVisible(false);
							GameClientThread.makeMenu.setSize(1280,720);
							GameClientThread.makeMenu.setVisible(true);
							
							
						}else {
							
						}
						
//	//					try{if(ois!=null) ois.close();}catch(IOException ioe) {}
//	//					try{if(oos!=null) oos.close();}catch(IOException ioe) {}
					}
					
					
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch(Exception e2) {
					e2.printStackTrace();
				}finally {
//					try{if(ois!=null) ois.close();}catch(IOException ioe) {}
//					try{if(oos!=null) oos.close();}catch(IOException ioe) {}
				}
				
			}
			
			
			
		}
		
	}

 

}
