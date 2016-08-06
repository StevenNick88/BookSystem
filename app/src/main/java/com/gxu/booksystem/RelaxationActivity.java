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


public class RelaxationActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private PullToRefreshListView relaxation_list;
    private RelaxationBaseAdapter adapter;
    private String[] relaxationItemNames;
    private String[] relaxationItemUrls;
    List<Map<String, Object>> relaxationItemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxation);
        relaxationItemNames =this.getResources().getStringArray(R.array.relaxation_item_name);
        relaxationItemUrls=this.getResources().getStringArray(R.array.relaxation_item_url);
        relaxationItemData = getRelaxationData();
        adapter=new RelaxationBaseAdapter(this);
        relaxation_list=(PullToRefreshListView)this.findViewById(R.id.relaxation_list);

        // Set a listener to be invoked when the list should be refreshed.
        relaxation_list.setOnRefreshListener(new CustomRefreshListener(
                RelaxationActivity.this,relaxationItemData,adapter,relaxation_list));
        // Add an end-of-list listener
        relaxation_list.setOnLastItemVisibleListener(new CustomLastItemVisibleListener(
                RelaxationActivity.this));
        // Add Sound Event Listener
        relaxation_list.setOnPullEventListener(new CustomSoundPullEventListener(
                RelaxationActivity.this));

        adapter.setData(relaxationItemData);
        relaxation_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        relaxation_list.setOnItemClickListener(this);

    }

    private  List<Map<String, Object>> getRelaxationData() {
        Resources res = this.getResources();
        //每个数据项中的数据由Map来存储
        List<Map<String, Object>> data = new ArrayList();
        for (int i=0; i<3; i++) {
            Map<String, Object> map = new HashMap();
            map.put("img", res.getIdentifier("rimg"+i, "drawable", this.getPackageName()));
            map.put("name", relaxationItemNames[i]);

            data.add(map);
        }
        return data;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        launch(WebViewActivity.class,"relaxationItemUrl",relaxationItemUrls[position-1]);
    }


    private class RelaxationBaseAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<Map<String, Object>> list=null;

        public RelaxationBaseAdapter(Context context){
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
                view = layoutInflater.inflate(R.layout.relaxation_list_item, null);
            } else {
                view = convertView;
            }
            ImageView relaxation_item_img = (ImageView) view.findViewById(R.id.relaxation_item_img);
//            TextView relaxation_item_name = (TextView) view.findViewById(R.id.relaxation_item_name);
            relaxation_item_img.setImageResource((Integer) list.get(position).get("img"));
//            relaxation_item_name.setText((String)list.get(position).get("name"));
            return view;
        }
    }



}
