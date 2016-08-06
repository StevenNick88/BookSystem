package com.gxu.booksystem;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gxu.booksystem.pulltorefresh.task.CustomLastItemVisibleListener;
import com.gxu.booksystem.pulltorefresh.task.CustomRefreshListener;
import com.gxu.booksystem.pulltorefresh.task.CustomSoundPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OnlineBooksActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private PullToRefreshListView online_books_list;
    private OnlineBooksBaseAdapter adapter;
    private String[] onlineBookItemNames;
    private String[] onlineBookItemUrls;
    List<Map<String, Object>> onlineBookItemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_books);
        onlineBookItemNames=this.getResources().getStringArray(R.array.online_books_item_name);
        onlineBookItemUrls=this.getResources().getStringArray(R.array.online_books_item_url);
        adapter=new OnlineBooksBaseAdapter(this);
        onlineBookItemData=getOnlineBooksData();
        online_books_list=(PullToRefreshListView)this.findViewById(R.id.online_books_list);

        // Set a listener to be invoked when the list should be refreshed.
        online_books_list.setOnRefreshListener(new CustomRefreshListener(
                OnlineBooksActivity.this,onlineBookItemData,adapter,online_books_list));
        // Add an end-of-list listener
        online_books_list.setOnLastItemVisibleListener(new CustomLastItemVisibleListener(
                OnlineBooksActivity.this));
        // Add Sound Event Listener
        online_books_list.setOnPullEventListener(new CustomSoundPullEventListener(
                OnlineBooksActivity.this));

        adapter.setData(onlineBookItemData);
        online_books_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        online_books_list.setOnItemClickListener(this);

    }

    private  List<Map<String, Object>> getOnlineBooksData() {
        Resources res = this.getResources();
        //每个数据项中的数据由Map来存储
        List<Map<String, Object>> data = new ArrayList();
        for (int i=0; i<3; i++) {
            Map<String, Object> map = new HashMap();
            map.put("img", res.getIdentifier("img"+i, "drawable", this.getPackageName()));
            map.put("name", onlineBookItemNames[i]);

            data.add(map);
        }
        return data;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        launch(WebViewActivity.class,"onlineBookItemUrl",onlineBookItemUrls[position-1]);
    }


    private class OnlineBooksBaseAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<Map<String, Object>> list=null;

        public OnlineBooksBaseAdapter(Context context){
            this.context=context;
            layoutInflater=LayoutInflater.from(context);
        }
        public void setData(List<Map<String, Object>> list){
            this.list=list;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.online_books_list_item, null);
            } else {
                view = convertView;
            }
            ImageView online_books_item_img = (ImageView) view.findViewById(R.id.online_books_item_img);
//            TextView online_books_item_name = (TextView) view.findViewById(R.id.online_books_item_name);
            online_books_item_img.setImageResource((Integer)list.get(position).get("img"));
//            online_books_item_name.setText((String)list.get(position).get("name"));
            return view;
        }
    }



}
