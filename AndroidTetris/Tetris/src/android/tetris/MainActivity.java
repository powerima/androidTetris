/*
 * 	2012. 11. 19
 * 
 * 	Testris - MainActivity
 * 			- 메인 화면
 */
package android.tetris;

import android.os.Bundle;
import android.app.Activity;
import android.content.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    //버튼 리스너
    public void mOnClick(View v){
    	Intent intent;
    	switch(v.getId()){
    	case R.id.startBtn:
    		intent = new Intent(this, TetrisActivity.class);
    		startActivity(intent);
    		break;
    	case R.id.showrankBtn:
    		intent = new Intent(this, RankingActivity.class);
    		startActivity(intent);
    		break;
    	case R.id.settingBtn:
    		intent = new Intent(this, SettingActivity.class);
    		startActivity(intent);
    		break;
    	case R.id.exitBtn:
    		finish();
    		break;
    	}
    }
}
