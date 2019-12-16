package com.java.mini;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProtocolMsg {
	public static final String LOGIN = "101";
	public static final String LOGIN_CHECK = "102";
	public static final String JUST_LOGIN = "103";  // 없어도 로그인
	
	public static final String I_WANT_MINESWEEPER = "104";  // 없어도 로그인
	public static final String I_WANTTO_AVOID_MINESWEEPER = "105";  // 없어도 로그인
	public static final String YOU_CAN_MINESWEEPER = "201";
	public static final String YOU_CANNOT_MINESWEEPER = "202";
	public static final String YOU_ARE_MINESWEEPER_MASTER = "203";
	public static final String MINESWEEPER_MASTER_EXIT = "204";
	public static final String START_MINESWEEPER = "205";
	public static final String MINESWEEPER_PRESTART = "206";
	public static final String CLICK_SAFEZONE = "207";
	public static final String MINESWEEPER_ALREADY_START = "208";
	public static final String MINESWEEPER_GAME_END = "209";
	
	
	public static final String USER_FINISH_MINESWEEPER = "210";
	public static final String I_FAIL_MINESWEEP= "211";
	public static final String MINESWEEP_ALL_FAIL = " 212";
	public static final String MINESWEEP_OVER_INFO = "213";
	public static final String MINESWEEP_OVER_ALLINFO = "214";
	public static final String SOMEONE_EXIT  = "215";
	public static final String SOMEONE_IN = "216";
	
	public static final String I_WANT_MINEAVOIDER = "309";  
	public static final String I_WANTTO_AVOID_MINEAVOIDER = "308";  
	public static final String YOU_CAN_MINEAVOIDER = "301";
	public static final String YOU_CANNOT_MINEAVOIDER = "302";
	public static final String YOU_ARE_MINEAVOIDER_MASTER = "303";
	
	public static final String I_WANT_RANKING = "401";
	public static final String VICTORY_NUM = "402";
	public static final String I_WANT_BATTLELOG = "403";
	//
	
	
	public static int canLogin(ArrayList<Object> command) {
		
		Connection conn = null;
		Statement stmt = null;
//		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("db 연결 성공");
			String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
			String dbId = "red";
			String dbPw = "red";
			conn = DriverManager.getConnection(url,dbId,dbPw);
			
			String inputId = (String)command.get(1);
			String inputPw = (String)command.get(2);
			
			String sql = "select * from login where id = '"+inputId+"'";
			stmt= conn.createStatement();
			rs=stmt.executeQuery(sql);
			rs.next();
			String getId = rs.getString("id");
			if(getId == null) getId = "";
			String getPw = rs.getString("pw");
			if(getPw == null) getPw = "";
//			rs.next();
			if(inputId.equals(getId) && inputPw.equals(getPw)) {  
				return 1;  // 로그인 성공
			}else if(inputPw.equals("")) {
				return 0;  // 로그인 정보 없음
			}else if(!inputPw.equals(getPw)) {
				return 2;  // 비밀번호 불일치
			}
			
			
			
		} catch (ClassNotFoundException e) {
			System.out.println("db 못찾겟는데요");
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}finally{
			try{if(rs!=null) rs.close();}catch(SQLException sqle) {}
			try{if(stmt!=null) stmt.close();}catch(SQLException sqle) {}
			try{if(conn!=null) conn.close();}catch(SQLException sqle) {}
			
			
		}
		
		return 0;
	}
	
	public static void powerLogin(String id, String pw ) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("db 연결 성공");
			String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
			String dbId = "red";
			String dbPw = "red";
			conn = DriverManager.getConnection(url,dbId,dbPw);
			
			StringBuffer sb = new StringBuffer();
			sb.append("insert into login values ( ? , ? )");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			
			pstmt.executeUpdate();
			System.out.println("유저 한명 추가요~");
			
		}catch(SQLException sqle) {
			System.out.println("insert 안댐");
		}catch(ClassNotFoundException cnfe) {
			System.out.println("db 연결안댐");
		}finally{
			
			try{if(pstmt!=null) pstmt.close();}catch(SQLException sqle) {}
			try{if(conn!=null) conn.close();}catch(SQLException sqle) {}
			
			
		}
	}
	
	public static void battleLogInsert(List<Object> command1, String whatGame) {
		Object command = command1.get(1);
		List<ArrayList<Object>> infolist = (List<ArrayList<Object>>)command;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("db 연결 성공");
			String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
			String dbId = "red";
			String dbPw = "red";
			conn = DriverManager.getConnection(url,dbId,dbPw);
			
			StringBuffer sb = new StringBuffer();
			
			sb.append("insert into battleLog values (battlelog_seq.nextVal ,?,?,?,?,?,?,?,?,?,? )");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, whatGame);
			pstmt.setInt(2, infolist.size());
			pstmt.setString(3, null);
			pstmt.setString(4, null);
			pstmt.setString(5, null);
			pstmt.setString(6, null);
			pstmt.setInt(7, 0);
			pstmt.setInt(8, 0);
			pstmt.setInt(9, 0);
			pstmt.setInt(10, 0);
			if(infolist.size()>=1) {
				pstmt.setString(3, (String)infolist.get(0).get(0));
				pstmt.setInt(7, (int)infolist.get(0).get(2));
			}
			
			if(infolist.size()>=2) {
				pstmt.setString(4, (String)infolist.get(1).get(0));
				pstmt.setInt(8, (int)infolist.get(1).get(2));
			}
			
			if(infolist.size()>=3) {
				pstmt.setString(5, (String)infolist.get(2).get(0));
				pstmt.setInt(9, (int)infolist.get(2).get(2));
			}
			
			if(infolist.size()>=4) {
				pstmt.setString(6, (String)infolist.get(3).get(0));
				pstmt.setInt(10, (int)infolist.get(3).get(2));
			}
			
			
			
			pstmt.executeUpdate();
			System.out.println("전적 하나 추가요 ~~ * ") ;
			
		}catch(SQLException sqle) {
			System.out.println("insert 안댐");
		}catch(ClassNotFoundException cnfe) {
			System.out.println("db 연결안댐");
		}finally{
			
			try{if(pstmt!=null) pstmt.close();}catch(SQLException sqle) {}
			try{if(conn!=null) conn.close();}catch(SQLException sqle) {}
			
			
		}
		
		
	}
	
	
	public static List<Object> getRank() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		ArrayList<ArrayList<Object> > allRank = new ArrayList<>();
		List<Object> list = new ArrayList<Object>();
		
		
		String[] array = {"btusername1","btusername2","btusername3","btusername4"};
		for(int i=0;i<4;i++) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("db 연결 성공");
			String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
			String dbId = "red";
			String dbPw = "red";
			conn = DriverManager.getConnection(url,dbId,dbPw);
			
			StringBuffer sb = new StringBuffer();
			sb.append("select "+array[i]+" , count(*)  " + 
					" from battlelog " + 
					" where btgame='MineSweeper' and ? is not null  " + 
					" group by  "+array[i] + 
					" order by count(*) desc ");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1,array[i] );
			
		
		
			List<Object> rankrankList = new ArrayList<Object>();
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String name = rs.getString(array[i]);
				int cnt = rs.getInt("COUNT(*)");
				int rank = i+1;
				rankrankList.add(name);
				rankrankList.add(rank);
				rankrankList.add(cnt);
			}
			list.add(rankrankList);
			
			System.out.println(i+1+"등 랭킹 하나 불러옴 @@ ") ;
			
		}catch(SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("rank get 안댐");
		}catch(ClassNotFoundException cnfe) {
			System.out.println("db 연결안댐");
		}finally{
			
			try{if(pstmt!=null) pstmt.close();}catch(SQLException sqle) {}
			try{if(conn!=null) conn.close();}catch(SQLException sqle) {}
			
			
		}
		}
		
		return list;
		
	}
	
	public static List<List<Object>> getBattleLog() {
		Connection conn = null;
//		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("db 연결 성공");
			String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
			String dbId = "red";
			String dbPw = "red";
			conn = DriverManager.getConnection(url,dbId,dbPw);
			
			
			String sql = "select * from battleLog order by btid desc ";
			pstmt= conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			
			rs = pstmt.executeQuery();
			List<List<Object> > allLog = new ArrayList<List<Object>>();
			while(rs.next()) {
				List<Object> singleLog = new ArrayList<Object>();
				int btid = rs.getInt(1);
				System.out.println(btid);
				String btGame = rs.getString(2);
				int btUserNum = rs.getInt(3);
				String btPersonId1 = rs.getString(4);
				String btPersonId2 = rs.getString(5);
				String btPersonId3 = rs.getString(6);
				String btPersonId4 = rs.getString(7);
				int btUserScore1 = rs.getInt(8);
				int btUserScore2 = rs.getInt(9);
				int btUserScore3 = rs.getInt(10);
				int btUserScore4 = rs.getInt(11);
				singleLog.add(btid);
				singleLog.add(btGame);
				singleLog.add(btUserNum);
				singleLog.add(btPersonId1);
				singleLog.add(btPersonId2);
				singleLog.add(btPersonId3);
				singleLog.add(btPersonId4);
				singleLog.add(btUserScore1);
				singleLog.add(btUserScore2);
				singleLog.add(btUserScore3);
				singleLog.add(btUserScore4);
				
				allLog.add(singleLog);
				
				
			}
			
			return allLog;
			
			
		} catch (ClassNotFoundException e) {
			System.out.println("db 못찾겟는데요");
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}finally{
			try{if(rs!=null) rs.close();}catch(SQLException sqle) {}
			try{if(pstmt!=null) pstmt.close();}catch(SQLException sqle) {}
			try{if(conn!=null) conn.close();}catch(SQLException sqle) {}
			
			
		}
		
		return null;
	}

}
