/*
 * 	2012. 11. 19
 * 
 * 	Testris - TestrisActivity
 * 			- 테트리스 게임 화면을 위한 액티비티
 * 
 */

package android.tetris;

import android.os.*;
import android.view.*;
import android.widget.*;

public class TetrisActivity extends Preference{
	
	int xSize;		//가로 블럭 테이블 크기
	int ySize;		//세로 블럭 테이블 크기
	
	UserTable user;					//UserTable	
	TetrisView tetrisView;			//TetrisView
	NextBlockView nextBlockView;	//NextBlockView
    ScoreView scoreView;			//ScoreView
	PlayerInfo player;				//PlayerInfo
	
	LinearLayout tetrisLayout;		//TetrisLayout
	LinearLayout nextBlockLayout;	//NextBlockLayout
	LinearLayout scoreLayout;		//ScoreLayout
	
    public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);		
		
		
		this.xSize = 12; // 가로 크기
		this.ySize = 20; // 세로 크기
		this.user = new UserTable(xSize, ySize);
		this.player = getPref();		//SettingActivity에서 설정한 내용 읽어오기
		
		
		setContentView(R.layout.tetrisactivity);		
		reset();
	}
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	DBHelper mDatabase = new DBHelper(this);
    	
    	try{
    		if(player.getScore() != 0)	//점수가 0이면 저장 ㄴㄴ
    			mDatabase.insert(player);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}finally{
    		if(mDatabase != null){
    			mDatabase.close();
    		}
    	}
    }

    @Override
    protected void onResume() {    
    	super.onResume();
    	
    	this.player.setName(getPref().getName());
    	this.player.setColorful(getPref().isColorful());
    }
    
	// 키 이벤트
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_SPACE:
				user.moveFastDown();
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				user.moveRotation();
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				user.moveLeft();
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				user.moveRight();
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				user.moveDown();
				break;
			case KeyEvent.KEYCODE_BACK:
				finish();
				break;
			}
		}
		return false;
	}
	
    //화면 터치 결과
    public void mOnClick(View v){    	
    	switch(v.getId()){
    	case R.id.layout_left:
    		//Toast.makeText(this, "left", Toast.LENGTH_SHORT).show();
    		user.moveFastDown();
    		break;
    	case R.id.layout_right:
    		//Toast.makeText(this, "right", Toast.LENGTH_SHORT).show();
    		user.moveFastDown();
    		break;
    	case R.id.layout_tetristable:
    		//Toast.makeText(this, "table", Toast.LENGTH_SHORT).show();
    		user.moveRotation();
    		break;
    		
    	case R.id.btn_arrowdown:
    		//Toast.makeText(this, "bottom", Toast.LENGTH_SHORT).show();
    		user.moveDown();
    		break;
    	case R.id.btn_arrowup:
    		user.moveRotation();
    		break;
    	case R.id.btn_arrowleft:
    		user.moveLeft();
    		break;
    	case R.id.btn_arrowright:
    		user.moveRight();
    		break;
    		
    	}    	
    }
    
    public void reset(){
		/* Tetris Layout */
    	tetrisLayout = (LinearLayout)findViewById(R.id.layout_tetristable);
    	tetrisLayout.removeAllViews();
		tetrisLayout.addView(tetrisView = new TetrisView(this, user, player, mHandler ));
		
		
		/* NextBlock Layout */
		nextBlockLayout = (LinearLayout)findViewById(R.id.layout_nextBlock);
		nextBlockLayout.removeAllViews();
		nextBlockLayout.addView(nextBlockView = new NextBlockView(this, user, player));
		
		
		/*Score Layout */
		scoreLayout = (LinearLayout)findViewById(R.id.layout_score);
		scoreLayout.removeAllViews();
		scoreLayout.addView(scoreView = new ScoreView(this, user, player));
		
    }
    //핸들러
  	Handler mHandler = new Handler(){
  		public void handleMessage(Message msg){  			  			
  			tetrisView.invalidate();
  			
  			switch(msg.what){
	  			case 1:
	  				player.setNextShape(msg.arg1);
	  				player.setNextRotation(msg.arg2);
	  				nextBlockView.invalidate();  				
	  				break;
	  				
	  			case 2:
	  				player.setLevel(msg.arg1);
	  				player.setScore(msg.arg2);
	  				break;
  			}
  		}
  	};
}
