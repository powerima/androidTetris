package android.tetris;

import android.content.*;
import android.graphics.*;
import android.widget.LinearLayout.*;

public class ScoreView extends BaseView{
	UserTable user;
	PlayerInfo player;
	Canvas canvas;
	int brickSize;
	
	public ScoreView(Context context, UserTable user, PlayerInfo player) {
		super(context);
		this.user = user;
		this.player = player;
	}
	
	public void onDraw(Canvas canvas){		
		this.canvas = canvas;
		this.brickSize = (int)(user.getBrickSize()/1.7);
		
		drawTableAndSetLayout(canvas, brickSize);
		drawDescript(player);
	}
	
	// 뷰 테두리 그리기고 레이아웃 크기 설정
	public void drawTableAndSetLayout(Canvas canvas, int brickSize) {
		int width = brickSize * 7;
		int height = (int) (width * 2.5);

		setLayoutParams(new LayoutParams(width, height));

		Rect rt = new Rect(0, 0, width-1, height-1);
		Paint pnt = new Paint();
		pnt.setColor(Color.WHITE);

		drawLineRect(canvas, rt, pnt);
	}
	
    //사용 설명 그리기
    public void drawDescript(PlayerInfo player){
        Paint pnt = new Paint();
        
        pnt.setColor(Color.WHITE);
        canvas.drawText("  - name - ",  0, brickSize * 2+15, pnt);
        canvas.drawText(" " + player.getName() , 0, brickSize * 2+30, pnt);
        
        canvas.drawText(" level : " + player.getLevel(), 0, brickSize * 2+60, pnt);        
        canvas.drawText(" score : " + player.getScore(), 0, brickSize * 2+80, pnt);
        
    }
}
