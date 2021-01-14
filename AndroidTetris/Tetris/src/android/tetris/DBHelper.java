package android.tetris;


import java.util.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBHelper extends SQLiteOpenHelper {
	
	public static final String TABLENAME = "player_ranking";
	public static final String _ID = "_id";
	public static final String PLAYER_NAME = "player_name";
	public static final String PLAYER_SCORE = "player_score";
	public static final String PLAYER_LEVEL = "player_level";
	
	private static final String DATABASE_NAME = "tetris_ranking.db";
	private static final int DATABASE_VERSION = 1;
	

	public DBHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	public DBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLENAME + " (" + _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT ," + PLAYER_NAME + " TEXT ," + PLAYER_LEVEL + " INTEGER ,"
				+  PLAYER_SCORE + " INTEGER);"); 	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	@Override
	public void onOpen(SQLiteDatabase db){
		super.onOpen(db);
	}
	
    //id값으로 db에서 삭제
    public void delete(Integer id){
		SQLiteDatabase db = getWritableDatabase();
		String astrArgs[] = {id.toString()};
		
		db.delete(TABLENAME, DBHelper._ID + "=?", astrArgs);
		db.close();	
    }
    
    //db에 랭킹 추가
    public void insert(PlayerInfo player){
    	
    	SQLiteDatabase db = getWritableDatabase();
    	
    	try{
			ContentValues playerRecord = new ContentValues();
			
			playerRecord.put(PLAYER_NAME, player.getName());
			playerRecord.put(PLAYER_SCORE, player.getScore());
			playerRecord.put(PLAYER_LEVEL, player.getLevel());			
			
			db.insert(TABLENAME, PLAYER_NAME, playerRecord);
			db.setTransactionSuccessful();	
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}finally{
    		db.endTransaction();
    		db.close();
    	}
    	return;
    }
    
    //모든 검색 db 검색후 ArrayList로 반환
    public ArrayList<PlayerInfo> search(ArrayList<PlayerInfo> list){
    	Cursor cursor;
    	
		SQLiteDatabase db = getWritableDatabase();
		
		try{
		cursor = db.rawQuery("SELECT * FROM " + TABLENAME 
				+ " ORDER BY " + PLAYER_LEVEL + " DESC, "+ PLAYER_SCORE + " DESC", null);
		
		if(cursor.moveToFirst()){
			for(int i=0; i<cursor.getCount(); i++){
				
				
				String name = cursor.getString(cursor.getColumnIndex(PLAYER_NAME));
				int id = cursor.getInt(cursor.getColumnIndex(_ID));
				int score = cursor.getInt(cursor.getColumnIndex(PLAYER_SCORE));
				int level = cursor.getInt(cursor.getColumnIndex(PLAYER_LEVEL));
				
				list.add(new PlayerInfo(id, name, level, score));	
				cursor.moveToNext();				
			}//for()		
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			db.close();	
		}
		
		return list;
    }
    
    //name값으로 db 검색 해서 ArrayList로 반환
	public  ArrayList<PlayerInfo> search(String n, ArrayList<PlayerInfo> list ){
		Cursor cursor;
		SQLiteDatabase db = getWritableDatabase();
		
		try{
			cursor = db.rawQuery("SELECT * FROM " + TABLENAME + " WHERE " + PLAYER_NAME + " = " 
					+ "'" + n + "' ORDER BY " + PLAYER_LEVEL +" DESC, "+ PLAYER_SCORE + " DESC", null);
			
			if(cursor.moveToFirst()){
				for(int i=0; i<cursor.getCount(); i++){
					
					
					String name = cursor.getString(cursor.getColumnIndex(PLAYER_NAME));
					int id = cursor.getInt(cursor.getColumnIndex(_ID));
					int score = cursor.getInt(cursor.getColumnIndex(PLAYER_SCORE));
					int level = cursor.getInt(cursor.getColumnIndex(PLAYER_LEVEL));
					
					list.add(new PlayerInfo(id, name, level, score));	
					cursor.moveToNext();				
				}//for()		
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			db.close();	
		}
		
		return list;
	}

}
