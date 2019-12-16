package com.java.mini;

 

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

 
public class MakeRandom {


	static int[] p1 = {0,1,0,-1};
	static int[] p2 = {1,0,-1,0};
	static int[][] map;
	static int[][] map2;
	static int[][][] check;
	static int[][][] dp;
	static List<Pos> tracking = new ArrayList<>();
	static int a;
	static int b;
	static int skillAtRow;
	static int skillAtCol;

	public static void main(String[] args) {

		// a * b 에서 n개 장애물의 랜덤 맵 만들기
		
		Scanner sc= new Scanner(System.in);
		
		a=sc.nextInt();
		b=sc.nextInt();
		int n=sc.nextInt();
		while(true) {
			System.out.println("*");
		map = new int[a][b];
		map2= new int[a][b];
		check = new int[a][b][2];
		dp = new int[a][b][2];
		int makeCnt = 0 ;
		for(int i=0;i<n;i++) {
			makeCnt ++ ; 
			

			int row = (int)(Math.random()*a);
			int col = (int)(Math.random()*b);

			if(map[row][col]!=1 && !(row==a-1 & col==b-1) && !(row==0 & col==0)) {

				map[row][col]=1;

			}else {

				i--;

			}
//			System.out.println(row+" " + col);

		}


		//map 사용자 설정

//		System.out.println("map 사용자 설정 시작");
//
//		for(int i=0;i<a;i++) {
//			for(int j=0;j<b;j++) {
//				map[i][j]=sc.nextInt();
//			}
//		}

		System.out.println("flag1");
		for(int i=0;i<a;i++) {
			for(int j=0;j<b;j++) {
				map2[i][j]=map[i][j];

				System.out.print(map[i][j]+" ");

				if(map[i][j]==1) {
					check[i][j][0]=1;
				}
			}
			System.out.println();
		}

		System.out.println();
		Pos start = new Pos(a-1,b-1,0,false,-1,-1);


		Queue<Pos> q = new LinkedList<>();

		q.add(start);

		check[a-1][b-1][0]=1;
		check[a-1][b-1][1]=1;

		int ans =-1;
		
		System.out.println("flag2");

		while(!q.isEmpty()) {

			System.out.println("^");

			int topRow = q.peek().getRow();
			int topCol = q.peek().getCol();
			int topCnt = q.peek().getCnt();
//			System.out.println(topRow + " "+topCol);

			int topSkillRow = q.peek().getSkillRow();
			int topSkillCol = q.peek().getSkillCol();
			boolean useSkill = q.peek().isSkill();
//			System.out.println("cnt : " + topCnt);

//			System.out.println(useSkill);

			if(useSkill) {
				if(dp[topRow][topCol][1]==0)
				dp[topRow][topCol][1] = -topCnt;
			}

			else {
				if(dp[topRow][topCol][0]==0)
				dp[topRow][topCol][0] = -topCnt;
			}

			

			if(topRow == 0 && topCol == 0) {

				System.out.println("skill 어디서썼냐면" + topSkillRow+","+topSkillCol);
				skillAtRow=topSkillRow;
				skillAtCol=topSkillCol;
				ans=topCnt;
				break;

			}

			
			boolean skill = q.peek().isSkill();

			q.poll();

			

			for(int i=0;i<4;i++) {

				int nextRow = topRow+p1[i];
				int nextCol = topCol+p2[i];


				if(nextRow>=0 && nextCol>=0 && nextRow<a && nextCol<b) {
						

					if(!useSkill) {
						if(map[nextRow][nextCol]==0) {
							if(check[nextRow][nextCol][0]==0) {
								q.add(new Pos(nextRow,nextCol,topCnt+1,useSkill,topSkillRow,topSkillCol));
								check[nextRow][nextCol][0]= 1;
							}else {
								
							}

						}else {

							if(check[nextRow][nextCol][1]==0) {
								q.add(new Pos(nextRow,nextCol,topCnt+1,true,topRow,topCol));
								check[nextRow][nextCol][1]=1;

							}

						}

						

					}else {

						if(map[nextRow][nextCol]==0) {
							if(false) { //check[nextRow][nextCol][0]==0 에서 false 로 고침

								//q.add(new Pos(nextRow,nextCol,topCnt+1,useSkill,topSkillRow,topSkillCol));

								//check[nextRow][nextCol][1]= 1;

							}else if(check[nextRow][nextCol][1]==0){
								q.add(new Pos(nextRow,nextCol,topCnt+1,useSkill,topSkillRow,topSkillCol));
								check[nextRow][nextCol][1]=1;
							}

						}

					}


				}

				

				

			}

			if(useSkill) check[topRow][topCol][1]= 1;

			else check[topRow][topCol][0]=1;


		}

		if(check[0][0][0]==0 && check[0][0][1]==0) {
			System.out.println("못가");
			continue;
		}

		

		

		for(int i=0;i<a;i++) {
			for(int j=0;j<b;j++) {
				System.out.print(map[i][j]+" ");
			}
			System.out.println();
		}

		System.out.println();

		for(int i=0;i<a;i++) {
			for(int j=0;j<b;j++) {
				System.out.print(dp[i][j][0]+" ");
			}
			System.out.println();
		}

		

		for(int i=0;i<a;i++) {
			for(int j=0;j<b;j++) {
				System.out.print(dp[i][j][1]+" ");
			}
			System.out.println();
		}



		System.out.println(ans);
		if(ans<=(a+b-2)){
			continue;
		}
		
		Pos end = new Pos(0,0,ans,false,0,0);
		tracking.add(end);
		
		for(int i=0;i<4;i++) {

			int desR = skillAtRow+p1[i];
			int desC = skillAtCol+p2[i];
			if(desR>=0 && desC >=0 && desR<a && desC<b) {
				map[desR][desC]=0;
			}

		}

		traceCheck = new int[a][b];

//		System.out.println("======dp1=====");

//		for(int i=0;i<a;i++) {

//			for(int j=0;j<b;j++) {

//				System.out.print(dp[i][j][1]+" ");

//			}

//			System.out.println();

//		}

		traceSuccess=false;
		dfs(end);
		
		System.out.println("역추적 들어갑니다잉");

//		for(Pos pos : tracking) {

//			System.out.println(pos.getRow() + ","+pos.getCol());

//		}
		if(!traceSuccess) {
			continue;
		}
		
		if(ans>a+b-2+8 && skillAtRow<a-2 && skillAtCol <b-2){
			System.out.println(makeCnt+"만에 만들어짐");
			break;
			
		}
		
		}
		

	}

	

	

	static int[] p3 = {-1,0,1,0};
	static int[] p4 = {0,-1,0,1};
	static int[][] traceCheck;
	static boolean traceSkill;
	static boolean traceSuccess=false;
	static void dfs(Pos cur) {

//		System.out.print("*");

		int row = cur.getRow();
		int col = cur.getCol();
		int cnt = cur.getCnt();
		boolean useSkill = cur.isSkill();

		traceCheck[row][col]=1;

//		System.out.println(cnt);

//		System.out.println("&"+row+" "+col);

		if(row==a-1 && col==b-1) {

			System.out.println("추적성공");
			traceSuccess=true;
			System.out.println(">>"+row+" "+col);

			return;

		}

		
		for(int i=0;i<4;i++) {

			int nextRow = row+p3[i];
			int nextCol = col+p4[i];

			if(nextRow>=0 && nextCol >=0 && nextRow<a && nextCol<b) {

				if(((-dp[nextRow][nextCol][1])==cnt-1 ||(-dp[nextRow][nextCol][0])==cnt-1)
						
						&& map[nextRow][nextCol]==0 && traceCheck[nextRow][nextCol]==0) {
					if(useSkill && map2[nextRow][nextCol]==1) continue;
					if(map2[nextRow][nextCol]==1) useSkill=true;   // 이게 잘못된듯.

//					System.out.println(nextRow +","+nextCol);

//					System.out.println("감지");

					Pos tmp = new Pos(nextRow,nextCol,cnt-1,useSkill,0,0);
					tracking.add(tmp);

					
					dfs(tmp);

					if(traceSuccess) {
						break;
					}
					
				}

			}

//			System.out.println("이러고끝남 " + nextRow + "," + nextCol);
		}

		if(traceSuccess) {
			System.out.println(">>"+row+" "+col);

		}

		

	}

 

}

 

 

class Pos{

	int row;
	int col;
	int cnt;
	boolean skill;
	int skillRow;
	int skillCol;

	public int getSkillRow() {
		return skillRow;
	}

	public void setSkillRow(int skillRow) {
		this.skillRow = skillRow;
	}

	public int getSkillCol() {
		return skillCol;
	}

	public void setSkillCol(int skillCol) {
		this.skillCol = skillCol;
	}

	public boolean isSkill() {
		return skill;
	}

	public void setSkill(boolean skill) {
		this.skill = skill;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public Pos(int row, int col, int cnt, boolean skill, int skillRow, int skillCol) {

		super();

		this.row = row;
		this.col = col;
		this.cnt = cnt;
		this.skill = skill;
		this.skillRow = skillRow;
		this.skillCol = skillCol;

	}

	

	

	

	

	

	

	

	

}
