/*
 * 	2012. 11. 19
 * 
 * 	Testris - TetrisView
 * 			- 테트리스 게임 내용을 화면에 출력하기 위한 뷰 정의 			
 * 			- 사용자의 입력을 받아 움직임 정의
 */
package android.tetris;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.LinearLayout.*;

public class TetrisView extends View implements Runnable {
	int brickSize;		//벽돌 크기	
	boolean isRunning;	//게임 진행을 조작
	boolean reDrawFlag;	//뷰 setLayoutParams 호출을 한번만 하기 위한 플래그
	
	PlayerInfo player;	//플레이어 정보
	UserTable user;		//게임을 진행하기 위한 유저테이블
	Canvas canvas;		//캔버스
	Handler mHandler;	//Activity 의 위젯 뷰 갱신을 위한 핸들러
	
	 
	
	private TetrisView(Context context, Handler mHandler) {
		super(context);
		
		reDrawFlag = true;
		isRunning = true;
		this.mHandler = mHandler;

		Thread t = new Thread(this);		
		t.start();
	}
	
	public TetrisView(Context context, UserTable user, PlayerInfo player, Handler mHandler){
		this(context, mHandler);
		this.user = user;
		this.player = player;
	}

	public void onDraw(Canvas canvas){
		this.canvas = canvas;
				
		if(reDrawFlag) {
			user.setBrickSize(brickSize = getWidth()/20);	//벽돌 크기 설정
			setLayoutParams(new LayoutParams(brickSize*user.getWidth(), brickSize*user.getHeight()));
						
			reDrawFlag = false;
		}
		
		drawOutTable();		//바깥 테이블
		drawInTable();		//안쪽 블럭 그리기		
		drawMovingBlock();	//움직이고 있는 블럭 그리기
		drawDescript();		//화면에 설명 그리기
		
    }
    
	//게임 진행
	public synchronized void run() {
		int threadTime = 50;
		Message msg;
		
		while (isRunning) {
			
			switch(user.getGameStat()){
				case UserTable.NEWBLOCK:	// newBlock : 새로운 블럭 생성
					if(user.newBlockPosition() == UserTable.GAMEOVER){
						Log.d("newBlockPosition()","being result is GAMEOVER");
					}
					
					msg = new Message();
					msg.what = 1;				
					msg.arg1 = user.getNextShape();
					msg.arg2 = user.getNextRotation();
					mHandler.sendMessage(msg);	//다음 블럭판 갱신
					break;
					
				case UserTable.MOVINGBLOCK:	// movingBlock : 블럭이 움직이고 있을때
					user.moveDownFrequantly();				
					break;
					
				case UserTable.SAVEBLOCK:	// savingBlock : 블럭 저장
					user.gameOverJudge();
					
					msg = new Message();
					msg.what = 2;						
					msg.arg1 = user.getLevel();
					msg.arg2 = user.getScore();
					mHandler.sendMessage(msg);	//점수판 갱신
					
					break;
				case UserTable.GAMEOVER:	// gameover : 게임 오버
					isRunning = false;
					Log.d("gameover","gameover");				
					break;
			}
		
			msg = new Message();
			msg.what = 0;
			mHandler.sendMessage(msg);
			
			try{Thread.sleep(threadTime);}catch(Exception ex){}
		}

	}
    
    //사용 설명 그리기
    public void drawDescript(){
        Paint pnt = new Paint();
        
        pnt.setColor(Color.WHITE);
        canvas.drawText("gameSpeed : " + user.gameSpeed , brickSize + 10, brickSize-5, pnt);
    }
    
    //바깥 테이블 그리기		
    public void drawOutTable(){
		Paint pnt = new Paint();
		Rect rt = new Rect(brickSize, brickSize, 
				brickSize * (user.getWidth()-1), brickSize * (user.getHeight()-1));
		
		pnt.setColor(Color.WHITE);
		drawLineRect(rt, pnt);
	 }
    
    //내부 테이블 그리기
    public void drawInTable(){
		Paint pnt = new Paint();
		Rect rt = new Rect(brickSize, brickSize, 
				brickSize * (user.getWidth()-1), brickSize * (user.getHeight()-1));
		
    	for (int i = 1; i < user.table.length-1; i++) {
			for (int j = 1; j < user.table[i].length-1; j++) {
				
				//격자 표시 여부
				if(player.isGrid()){
					pnt.setColor(Color.GRAY);
					drawGrid(i, j, pnt);
				}
				
				if (user.table[i][j] != UserTable.BLANK) {
					rt = new Rect(i * brickSize, j * brickSize, i * brickSize + brickSize, j * brickSize + brickSize);

					//색상 블럭 여부
					if(player.isColorful()){	
						pnt.setColor(Shape.color[user.table[i][j]]);					
						canvas.drawRect(rt, pnt);
						
						pnt.setColor(Color.WHITE);
						drawLineRect(rt, pnt);
					}
					else{
						pnt.setColor(Color.GRAY);
						canvas.drawRect(rt, pnt);
						
						//pnt.setColor(Shape.color[user.table[i][j]]);
						pnt.setColor(Color.RED);
						drawLineRect(rt, pnt);
					}
					
					
				}
			}
		}
    }
    
	// 움직이는 블럭 그리기
	public void drawMovingBlock() {
		Rect rt;
		Paint pnt = new Paint();
		
		for (int i = 0; i < user.movingBlock.length; i++) {
			int left = (user.movingBlock[i][0] + user.passedBrick[0])* brickSize;
			int top = (user.movingBlock[i][1] + user.passedBrick[1])* brickSize;

			rt = new Rect(left, top, left + brickSize, top + brickSize);

			if(player.isColorful()){//색 입히는 경우
				pnt.setColor(Shape.color[user.shape]);	
				canvas.drawRect(rt, pnt);
				
				pnt.setColor(Color.WHITE);
				drawLineRect(rt, pnt);
			}
			else{
				//pnt.setColor(Shape.color[user.shape]);
				pnt.setColor(Color.RED);
				drawLineRect(rt, pnt);				
			}
			
		}
	}
	
	//격자 표시를 위한 점 찍기
	public void drawGrid(int i, int j, Paint pnt){
		int x = i*brickSize + brickSize/2;
		int y = j*brickSize + brickSize/2;
		
		canvas.drawPoint(x, y, pnt);
	}

	//채우지 않은 사각형 그리기
	public void drawLineRect(Rect rt, Paint pnt){
		float[] f1 = {rt.left, rt.top, rt.right, rt.top};
		float[] f2 = {rt.left, rt.top, rt.left, rt.bottom};
		float[] f3 = {rt.left, rt.bottom, rt.right, rt.bottom};
		float[] f4 = {rt.right, rt.top, rt.right, rt.bottom};
		
		canvas.drawLines(f1, pnt);
		canvas.drawLines(f2, pnt);
		canvas.drawLines(f3, pnt);
		canvas.drawLines(f4, pnt);
	}
	
	//유저 필드 갱신
	public void setUser(UserTable user){
		this.user = user;
	}
	
	//벽돌 크기 조절하기
	public void setbrickSize(int brickSize){
		this.brickSize = brickSize;
	}

	//벽돌 크기 늘리기
    public boolean brickSizeUp(){
    	brickSize++;
    	return true;
    }
      
    //벽돌 크기 줄이기 - 0보다는 커야함    
    public boolean brickSizeDown(){
    	if(brickSize > 0){
    		brickSize--;    		
    		return true;    		
    	}    	
    	return false;
    }
  
}
