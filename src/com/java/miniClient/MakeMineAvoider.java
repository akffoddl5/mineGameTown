package com.java.miniClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class MakeMineAvoider extends JFrame implements ActionListener {
   
   private JPanel main = new JPanel(new BorderLayout(200,5));
   private JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
   private JPanel right = new JPanel(new BorderLayout(20,150));
   private JPanel middle = new JPanel(new BorderLayout(200,50));
   private JPanel top = new JPanel(new FlowLayout());
   private JPanel left = new JPanel(new BorderLayout(5,5));
   private JPanel mainTop = new JPanel(new FlowLayout());
   private JPanel mainBottom = new JPanel(new FlowLayout());
   private JPanel mainLeft = new JPanel(new FlowLayout());
   private JPanel mainRight = new JPanel(new FlowLayout());
   
   private Container con = this.getContentPane();
   private JPanel minePanel = new JPanel(new GridLayout(12, 12));
   private static JButton[][] buttons ;
   
   private JButton backMenuBt = new JButton("back to menu");
   
   public static JButton startBt = new JButton("START");
   private JTextArea timeCnt = new JTextArea(5,15);
   private JTextArea moveCnt = new JTextArea(5,15);
   
   private static JDialog dialog2;
   private static JProgressBar jpb2 = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
   
   
   public MakeMineAvoider() {
      
      con.setLayout(new BorderLayout(5,5));
      dialog2= new JDialog(this);
      con.add("North",mainTop);
      con.add("East",mainRight);
      con.add("South",mainBottom);
      con.add("West",mainLeft);
      con.add("Center",main);
      main.add("North",top);
      main.add("East",right);
      main.add("West",left);
      main.add("South",bottom);
      main.add("Center",middle);
      middle.add("Center",minePanel);
      startBt.setEnabled(false);
      
      buttons = new JButton[12][12];
      for(int i=0;i<12;i++) {
         for(int j=0;j<12;j++) {
            buttons[i][j]=new JButton();
            minePanel.add(buttons[i][j]);
         }
      }
      
      makeMain();
      makeListener();
      makeDialog();
      
      setSize(1280,720);
      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
      setLocationRelativeTo(null);
      setVisible(false);
      
   }
   
   private void makeMain() {
      bottom.add(backMenuBt);
      right.add("North",moveCnt);
      right.add("Center",startBt);
      right.add("South",timeCnt);
      
   }
   
   private void makeListener() {
      backMenuBt.addActionListener(this);
      startBt.addActionListener(this);
   }
   
   
   public void makeDialog() {
      
      dialog2.setLayout(new FlowLayout());
      JPanel jp1 = new JPanel();
      jp1.add(jpb2);
//      dialog2.add("Center",jpb2);
      dialog2.add(jp1);
//      dialog2.add(new JLabel("red"));
      dialog2.setSize(500, 200);
      
      
      jpb2.setStringPainted(true);
      jpb2.setString("0 %");
      dialog2.setLocationRelativeTo(null);
      dialog2.setVisible(false);
   }
   
   public void dialogStart() {
      //preStart
      
      startBt.setEnabled(false);
      //
      
      dialog2.setVisible(true);
//      jpb.setVisible(true);
      runProgressbar();
      
      
   }
   
   public static void runProgressbar() {
      
      for(int i=0;i<=100;i++) {
         try {
            Thread.sleep(30);
         }catch(InterruptedException ie) {
            ie.printStackTrace();
         }
         jpb2.setValue(i);
         jpb2.setString("방 설정중 .. \n" + i+" %");
      }
      dialog2.setVisible(false);
      
   }
   

   @Override
   public void actionPerformed(ActionEvent e) {
      if(e.getSource() == backMenuBt) {

         GameClientThread.makeMineAvoider.setVisible(false);
         GameClientThread.makeMenu.setVisible(true);
      }else if(e.getSource() == startBt) {
    	  JOptionPane.showMessageDialog(this, "길찾기 START");
    	  startBt.setEnabled(false);
//         dialogStart();
      }
   }
   

}