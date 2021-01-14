/*
 * 
 * 	2012. 11. 23
 * 
 * 	테트리스 랭킹보기
 * 
 */
package android.tetris;

import java.util.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class RankingActivity extends Preference {
	DBHelper mDatabase;
	ArrayList<PlayerInfo> list;
	ListAdapter myAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rankingactivity);
        
        list = new ArrayList<PlayerInfo>();
        mDatabase = new DBHelper(getApplicationContext());
        
        myAdapter = new ListAdapter(this, list);
   		ListView myList;
   		myList = (ListView)findViewById(R.id.lv_ranking_listview);
   		myList.setAdapter(myAdapter);
   		
   		//바로 랭킹 보여주기
		list.clear();
		mDatabase.search(list);
		myAdapter.notifyDataSetChanged();
    }
    
    
	@Override
	//finish()가 호출되면 자동으로 호출되는 메서드
	protected void onDestroy() {
		super.onDestroy();
		if(mDatabase != null){
			mDatabase.close();
		}
	} // onDestroy() END 
    
	
	//버튼 리스너
    public void mOnClick(View v){
    	switch(v.getId()){
    	case R.id.btn_ranking_all:
    		list.clear();
    		mDatabase.search(list);
    		myAdapter.notifyDataSetChanged();
    		break;
    	case R.id.btn_ranking_search:
    		EditText et = (EditText)findViewById(R.id.et_setting_name);
    		String name = et.getText().toString();
    		
    		if(name.equals("")){
    			name = getPref().getName();
    			et.setText(name);
    		}
    		list.clear();
    		mDatabase.search(name, list);
    		myAdapter.notifyDataSetChanged();
    		break;
    	case R.id.btn_ranking_exit:
    		finish();
    		break;
    	
    	}
    }
	
	//리스트 뷰에 추가하기 위한 Adapter
	class ListAdapter extends BaseAdapter{
		ArrayList<PlayerInfo> list;
		LayoutInflater inflater;
		Context mainContext;
		
		ListAdapter(Context mainContext, ArrayList<PlayerInfo> list){
			this.mainContext = mainContext;
			this.list = list;			
			this.inflater = (LayoutInflater) mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final int pos = position;
			
			if(convertView == null){
				convertView = inflater.inflate(R.layout.listviewlayout, parent, false);				
			}
			TextView id = (TextView)convertView.findViewById(R.id.tv_listview_id);
			id.setText("" +  (position+1));
			
			
			TextView name = (TextView)convertView.findViewById(R.id.tv_listview_name);
			name.setText(list.get(position).getName());
			
			
			TextView level = (TextView)convertView.findViewById(R.id.tv_listview_level);
			level.setText("" + list.get(position).getLevel());
			
			
			TextView score = (TextView)convertView.findViewById(R.id.tv_listview_score);
			score.setText("" + list.get(position).getScore());
			
			
			Button delBtn = (Button)convertView.findViewById(R.id.btn_listview_del);
			delBtn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					try{
						mDatabase.delete(list.get(pos).getId());		//DB에서 삭제
						list.remove(pos);				//리스트에서 삭제
						notifyDataSetChanged();
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			});
			
			return convertView;
		}
		
	}
	
}
