/*
 * 	2012. 11. 19
 * 
 * 	Testris - UserTable
 *			- 테트리스 화면에 적용하기 전 논리적 좌표값을 저장하는 테이블과 움직이는 블럭 정의			
 * 			- 블럭이 움직이기 위한 명령과 게임 진행을 판정
 */
package android.tetris;

import android.util.*;

public class UserTable {
	int brickSize;			//벽돌 사이즈
	int shape;				//블럭 모양 정보
	int rotation;			//블럭 회전 정보
	int nextShape;			//새로운 블럭 회전 정보
	int nextRotation;		//새로운 블럭 회전 정보
	int gameSpeed;			//게임 속도를 위한 카운터
	int level;				//게임 레벨 - 점수에 따라 결정됨 (Shape.score[])
	int difficulty;			//게임 속도 - 게임 레벨에 따라 결정됨 (difficulty =  BASICGAMESPEED - level)
	int score;				//게임 점수 - 한번 블럭 지울 때 마다 1 점씩 올라감
	
	
	int[][] movingBlock; 	//조작중인 블럭 모양 정보 - 이동한 위치 정보가 아닌 그냥 모양 정보만 기억
	int[] passedBrick;		//이동한 좌표   - 이동한 x,y 좌표만 기억, 실제 좌표 = movingBlock + passedBrick
	int[][] table;			//게임 테이블
	int gameStat; 			//게임 상태 - 3가지  newBlock, movingBlock, SAVEBLOCK, gameOver
	
	
	final static int BASICGAMESPEED = 10;		//기본 게임 속도
	final static int NEWBLOCK = 0;				//게임 상태 - 새로운 블럭
	final static int MOVINGBLOCK = 1;			//게임 상태 - 블럭 이동
	final static int SAVEBLOCK = 2;				//게임 상태 - 블럭 저장
	final static int GAMEOVER = 3;				//게임 상태 - 게임 끝
	final static int WALL = Shape.block.length;	//벽
	final static int BLANK = WALL+1;			//빈 공간
	
	UserTable(){
		
		table = new int[11][18];		
		movingBlock = new int[4][2];		
		passedBrick = new int[2];		
		
		brickSize = -1;
		passedBrick[0] = table.length/2;
		passedBrick[1] = 1;
		
		nextShape = (int)(Math.random() * Shape.block.length);
		nextRotation = (int)(Math.random() * Shape.block[nextShape].length);
		gameStat = NEWBLOCK;
		gameSpeed = 100;
		difficulty = BASICGAMESPEED;
		level = 1;
		score = 0;
		
		initTable();
	}
	
	UserTable(int x, int y){
		this();
		table = new int[x][y];		
		passedBrick[0] = table.length/2;
		initTable();
	}
	
		
	//테이블 벽만드는 코드 - 단순한 직사각형의 벽 만들기 
	public void initTable(){
		for(int i=0; i <table.length; i++){
			for(int j=0; j<table[i].length; j++){
				table[i][j] = BLANK;
				if(i ==0 || i == table.length-1){
					table[i][j] = WALL;	
				}
				else{
					table[i][table[i].length-1] = WALL;
					table[i][0] = WALL;
				}
			}
						
		}
	}
	
	//충돌 여부 검사 충돌 -true, 충돌 아님 - false
	public boolean crashJudge(){
		for(int i=0; i<movingBlock.length; i++){			
			if(table[movingBlock[i][0] + passedBrick[0]][movingBlock[i][1] + passedBrick[1]] != BLANK){
				return true;
			}  
		}
		return false;
	}
	
	//충돌 여부 검사 충돌 -true, 충돌 아님 - false
	public boolean crashJudge(int x, int y){
		for(int i=0; i<movingBlock.length; i++){			
			if(table[movingBlock[i][0] + x][movingBlock[i][1] + y] != BLANK){
				return true;
			}  
		}
		return false;
	}
	
	//블럭을 왼쪽으로 이동 - 이동 성공시 true 실패시 false
	public boolean moveLeft(){
		passedBrick[0]--;		
		if(crashJudge()){
			passedBrick[0]++;
			
			return false;
		}
		
		return true;
	}
	
	//블럭을 오른쪽으로 이동
	public boolean moveRight(){
		passedBrick[0]++;		
		if(crashJudge()){
			passedBrick[0]--;			
			return false;
		}			
		return true;
	}
	
	//블럭을 아래로 이동
	public boolean moveDown(){
		passedBrick[1]++;		
		if(crashJudge()){
			passedBrick[1]--;			
			return false;
		}		
		return true;		
	}
	
	//블럭을 위로 이동
	public boolean moveUp(){
		passedBrick[1]--;		
		if(crashJudge()){
			passedBrick[1]++;			
			return false;
		}				
		return true;
	}
	
	//블럭 회전하기 - 회전 성공시 true 실패시 false 반환
	public boolean moveRotation(){
		++rotation; 
		
		//회전이 다했을경우 첨부터
		if (rotation >= Shape.block[shape].length)  {	
	        rotation =0;
	    }		
		movingBlock = Shape.block[shape][rotation];    
		
		//벽돌 회전시 벽에 충돌할 때 - 상태 복구
		if(crashJudge()){
			if(rotation == 0){
				rotation = Shape.block[shape].length-1;
				movingBlock = Shape.block[shape][rotation];
			}else{
				movingBlock = Shape.block[shape][--rotation];                			
			}  			
			return false;
		} 		
		return true;
	}
	
	//블럭 빠르게 내리기
	public boolean moveFastDown(){
		for(int i = passedBrick[1]; i < table[1].length; i++){			
			if(crashJudge(passedBrick[0], i)){
				passedBrick[1] = i-1;
				gameSpeed = level;
				break;
			}
		}
		return true;
	}
	
	//이동중인 블럭 정보를 테이블에 저장 - 저장 성공시 true 실패시 false
	public boolean saveMovingBlockToTable(){		
		if(setTableAble()){
			for(int i=0; i<movingBlock.length; i++){
				table[movingBlock[i][0]+passedBrick[0]][movingBlock[i][1]+passedBrick[1]] = shape;
			}			
			isFullLine();
			isEmptyLine();
			return true;
		}
		return false;
	}
	
	
	
	//테이블에 정보 저장 가능 여부 - 저장 가능할 경우 true 불가능할경우 false
	public boolean setTableAble(){		
		for(int i=0; i<movingBlock.length; i++){
			if(table[movingBlock[i][0] + passedBrick[0]][movingBlock[i][1] + passedBrick[1]] != BLANK){
				return false;
			}				
		}
		return true;		
	}
	
	//게임 상태 출력
	public int getGameStat(){
		return gameStat;
	}
	
	
		
	//새로운 블럭생성과 위치도 바꾸기
	public int newBlockPosition(){
		newBlock();
    	passedBrick[0] = table.length/2;            		
    	passedBrick[1] = 1;
    	gameStat = MOVINGBLOCK;		
    	gameSpeed = difficulty;
    	if(crashJudge()){
    		gameStat = GAMEOVER;
    	}   	    	
    	return gameStat;
	}
		
	
	//주기적인 블럭 내리기 - 블럭 내리기 성공시 - true, 실패시  false 반납
	public int moveDownFrequantly(){	        	
    	if(--gameSpeed < 0){
        	if(moveDown()){        	
        		gameSpeed = difficulty;
        		return gameStat;
        	}
        	gameStat = SAVEBLOCK;
    	}    	
    	return gameStat;
	}
	
	//그냥 내리기
	public boolean moveDownDirect(){
		if(moveDown()){
			return true;
		}
		gameStat = SAVEBLOCK;
		return false;		
	}
		
	
	
	//새로운 블럭 생성하기 - 새로운 블럭 생성 성공시 true 위치는 변하지 않음
	public boolean newBlock(){
		
		shape = nextShape;
		rotation = nextRotation;
		
		nextShape = (int)(Math.random() * Shape.block.length);
	    nextRotation = (int)(Math.random() * Shape.block[nextShape].length);
	    movingBlock = Shape.block[shape][rotation];  
	    
		if(crashJudge()){
			if(rotation == 0){
				movingBlock = Shape.block[shape][Shape.block[shape].length-1];
			}else{
				movingBlock = Shape.block[shape][--rotation];
				
			}     
			return false;
		}    	    
		return true;
	}
	
	
	
	//게임 오버 판단 후 테이블에 정보 저장
	public int gameOverJudge(){		
		if(saveMovingBlockToTable()){
			this.gameStat = NEWBLOCK;			
			return gameStat;
		}		
		this.gameStat = GAMEOVER;		
		return gameStat;
	}
	
	//라인 지우기
	public void deleteLine(int y){
		for(int i=1; i < table.length-1; i++ ){
			table[i][y] = BLANK;
		}
		return;
	}
	
	//라인 검사 후 지우기
	public boolean isFullLine(){
		int count;
		for(int i=1; i < table[0].length-1; i++){
			count =0;
			for(int j=1; j < table.length-1; j++){
				if(table[j][i] == BLANK){	
					break;
				}
				count++;
			}
			if(count == table.length -2){
				deleteLine(i);	//지우기
				score++;
				judgeLevelUp();		
			}
		}		
		return false;
	}
	
	//전체 라인 내리기 - 한줄씩 내리기
	public void downLine(int y){
		for(int i=y; i> 1;i--){
			for(int j=1; j< table.length-1; j++){
				table[j][i] = table[j][i-1];
			}
		}
	}
	
	
	//라인 검사후 내리기
	public boolean isEmptyLine(){
		int count;
		for(int i=1; i < table[0].length-1; i++){
			count =0;
			for(int j=1; j < table.length-1; j++){
				if(table[j][i] != BLANK){
					break;
				}
				count++;
			}
			if(count == table.length -2){
				downLine(i);
			}
		}		
		return false;
	}
	
	//레벨 올리기
	public void judgeLevelUp(){
		for(int i=level; i<Shape.level.length; i++){
			if(Shape.level[i] <= score){
				level = ++i;					//레벨 상승 
				difficulty = BASICGAMESPEED - level;	//게임 난이도 상승
				
				return;
			}
		}
	}
	
	//다음 블럭 회전번호
	public int getNextRotation(){
		return nextRotation;
	}
	//다음 블럭 모양
	public int getNextShape(){		
		return nextShape;
	}
	
	//점수 반환
	public int getScore(){
		return score;
	}
	
	//게임 레벨 임의 셋팅
	public void setLevel(int level){
		if(level > 0 ) this.level = level;
		return;
	}
	
	//게임 레벨 구하기
	public int getLevel(){
		return level;
	}
	
	//brickSize 셋팅
	public void setBrickSize(int brickSize){
		if(brickSize > 0) this.brickSize = brickSize;
	}
		
	//brickSize 반환
	public int getBrickSize(){
		return brickSize;
	}
	//가로 블럭테이블 크기 반환
	public int getWidth(){
		return table.length;
	}
	
	//세로 블럭테이블 크기 반환
	public int getHeight(){
		return table[0].length;
	}
}
