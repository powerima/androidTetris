package android.tetris;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.widget.LinearLayout.*;

public class NextBlockView extends BaseView{
	int brickSize;		//다음 블럭을 표시하기 위한 벽돌 크기
	int nextShape;		//다음 블럭 모양
	int nextRotation;	//다음 블럭 회전 번호
	
	Canvas canvas;		
	UserTable user;
	PlayerInfo player;
	
	public NextBlockView(Context context, UserTable user, PlayerInfo player) {
		super(context);			
		
		this.user = user;
		this.player = player;
		
	}

	public void onDraw(Canvas canvas){
		this.canvas = canvas;
		this.brickSize = (int)(user.getBrickSize()/1.7);
		
		Log.d("ondraw","nextblock ondraw");
		drawTableAndSetLayout(canvas, brickSize);
		drawNextBlock(canvas, user);
	}	
	
	// 뷰 테두리 그리기고 레이아웃 크기 설정
	public void drawTableAndSetLayout(Canvas canvas, int brickSize) {
		int width = brickSize * 7;
		int height = width;

		setLayoutParams(new LayoutParams(width, height));

		Rect rt = new Rect(0, 0, width-1, height-1);
		Paint pnt = new Paint();
		pnt.setColor(Color.WHITE);
		
		drawLineRect(canvas, rt, pnt);
	}
	
	// 다음 블럭 그리기
	public void drawNextBlock(Canvas canvas, UserTable user) {
		Rect rt;

		int nextShape = player.getNextShape();
		int nextRotation = player.getNextRotation();
		int margin = brickSize * 2;
		
		Paint pnt = new Paint();
		pnt.setColor(Color.WHITE);
		
		for (int i = 0; i < Shape.block[nextShape][nextRotation].length; i++) {
			int left = Shape.block[nextShape][nextRotation][i][0] * brickSize + margin;
			int top = Shape.block[nextShape][nextRotation][i][1] * brickSize + margin;

			rt = new Rect(left, top, left + brickSize, top + brickSize);

			if(player.isColorful()){	//색 입히는 경우
				pnt.setColor(Shape.color[nextShape]);	
				canvas.drawRect(rt, pnt);
				
				pnt.setColor(Color.WHITE);
				drawLineRect(canvas, rt, pnt );
			}
			else{
				pnt.setColor(Shape.color[nextShape]);	
				drawLineRect(canvas, rt, pnt );
			}
			
		}
	}
}
