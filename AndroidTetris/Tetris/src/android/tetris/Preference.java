/*
 * 
 * 	2012. 11. 23
 * 
 * 	Tetris - 플레이어 정보 설정을 위한 SharedPreferences
 * 
 */
package android.tetris;

import android.app.*;
import android.content.*;
import android.os.*;


public class Preference extends Activity {
	//전체 Application에서 사용하는 SharedPreferences 객체 선언
	SharedPreferences settings;		
	SharedPreferences.Editor prefEditor;
	
	public static final String PREFERENCE_FILENAME = "PlayerInfo";
	public static final String PREFERENCE_NAME = "name";
	public static final String PREFERENCE_COLOR = "color"; 
	public static final String PREFERENCE_GRID = "grid";
	public static final String PREFERENCE_DEFAULT_NAME = "Anonymous";
	public static final boolean PREFERENCE_DEFAULT_GRID = false;
	public static final boolean PREFERENCE_DEFAULT_COLOR = true;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		this.settings =  getSharedPreferences(PREFERENCE_FILENAME, 0);
		this.prefEditor = settings.edit();     
	}
		
    
	//Setting으로 저장된 환경 설정 값 불러오기
	public PlayerInfo getPref(){
		PlayerInfo player = new PlayerInfo(settings.getString(PREFERENCE_NAME, PREFERENCE_DEFAULT_NAME),
				settings.getBoolean(PREFERENCE_COLOR, PREFERENCE_DEFAULT_COLOR),
				settings.getBoolean(PREFERENCE_GRID, PREFERENCE_DEFAULT_GRID));
		
		return player;
	}
	
	//환경 설정 수정
	public void modifiedPref(PlayerInfo player){
		prefEditor.clear();
		
		prefEditor.putString(PREFERENCE_NAME, player.getName());
		prefEditor.putBoolean(PREFERENCE_COLOR, player.isColorful());
		prefEditor.putBoolean(PREFERENCE_GRID, player.isGrid());
		prefEditor.commit();
	}
}
