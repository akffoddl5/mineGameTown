package com.java.miniClient;

public class ProtocolMsg {
	public static final String LOGIN = "101";  // 로그인 요청
	public static final String LOGIN_CHECK = "102";  // 로그인 응답
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
	
	

}
