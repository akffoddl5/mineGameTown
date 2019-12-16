package com.java.mini;

import java.util.Scanner;

public class MakeMine {
	
	
	
	public static void main(String[] args) {
		
		makeMine();
		
	
	}
	
	public static char[][] makeMine(){
		Scanner sc= new Scanner(System.in);
		
		int[][] map;
		char[][] map2;
		int a;
		int b;
		int[] p1 = {0,1,0,-1,1,1,-1,-1};
		int[] p2 = {1,0,-1,0,1,-1,-1,1};
		
		
		
		a=16;
		b=16;
		int n=50;
		
//			System.out.println("*");
		map = new int[a][b];
		map2 = new char[a][b];


		for(int i=0;i<n;i++) {
			
			int row = (int)(Math.random()*a);
			int col = (int)(Math.random()*b);

			if(map[row][col]!=1 && !(row==a-1 & col==b-1) && !(row==0 & col==0)) {

				map[row][col]=1;

			}else {

				i--;

			}
//			System.out.println(row+" " + col);

		}
		
		
		//지뢰 0,1 
//		for(int i=0;i<a;i++) {
//			for(int j=0;j<b;j++) {
//				System.out.print(map[i][j]);
//			}
//			System.out.println();
//		}
		
		for(int i=0;i<a;i++) {
			for(int j=0;j<b;j++) {
				int row = i;
				int col = j;
				int tmpCnt = 0;
				for(int k=0;k<8;k++) {
					int nextRow= row+p1[k];
					int nextCol= col+p2[k];
					if(nextRow>=0 && nextCol >=0 && nextRow<a && nextCol <b) {
						if(map[nextRow][nextCol]==1) {
							tmpCnt++;
						}
						
					}
					
				}
				if(map[row][col]==0)
				map2[row][col]=(char)(tmpCnt+'0');
				else map2[row][col]='X';
				
			}
		}
//		System.out.println("================");
//		for(int i=0;i<a;i++) {
//			for(int j=0;j<b;j++) {
//				System.out.print(map2[i][j]);
//			}
//			System.out.println();
//		}
//		
		return map2;
	
}
	
	
}
