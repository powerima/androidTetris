/*
 * 
 * 	2012. 11. 23
 * 
 * 	Tetris - SettingActivity : 환결 설정 화면 
 */
package android.tetris;

import android.os.*;
import android.view.*;
import android.widget.*;

public class SettingActivity extends Preference {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingactivity);
        
        initLayout();
    }

    @Override
    protected void onResume() {    
    	super.onResume();
    	
    	initLayout();
    }
    
    //초기 레이아웃 정보를 설정
    public void initLayout(){
    	EditText name = (EditText)findViewById(R.id.et_setting_name);
    	RadioGroup color = (RadioGroup)findViewById(R.id.rg_setting_radiogroup);
    	RadioGroup grid = (RadioGroup)findViewById(R.id.rg_setting_radiogroup2);
    	
    	name.setText(getPref().getName());	//name EditText 설정
    	color.check(getPref().isColorful()? R.id.rb_setting_colorful : R.id.rb_setting_simple);	//라디오 버튼 설정
    	grid.check(getPref().isGrid()? R.id.rb_setting_grid : R.id.rb_setting_nogrid);
    }
    
    //save 버튼 누를 때 저장하기 위한 메서드
    public void mOnSaveClick(View v){
    	switch(v.getId()){
    		case R.id.btn_setting_save:
    	    	modifiedPref(save());
    	    	Toast.makeText(SettingActivity.this, "Save", Toast.LENGTH_SHORT).show();
    	    	break;
    	    	
    		case R.id.btn_setting_exit:
    			finish();
    			break;    			
    	}
    }
    
    //레이아웃(EditText, RadioGroup ..)에 입력한 정보를 player 객체로 바꿔주는 메서드
    public PlayerInfo save(){
    	EditText name = (EditText)findViewById(R.id.et_setting_name);
    	RadioGroup color = (RadioGroup)findViewById(R.id.rg_setting_radiogroup);
    	RadioButton rBtn = (RadioButton)findViewById(color.getCheckedRadioButtonId());
    	
    	RadioGroup grid = (RadioGroup)findViewById(R.id.rg_setting_radiogroup2);
    	RadioButton rBtn2 = (RadioButton)findViewById(grid.getCheckedRadioButtonId());
    	
    	String str = name.getText().toString();
    	if(str.equals("")){
    		str = "anonymous";
    		name.setText(str);
    	}
    	
    	return new PlayerInfo(str, 
    			rBtn.getText().toString().equals("Color"), rBtn2.getText().toString().equals("Grid"));
    }
}
