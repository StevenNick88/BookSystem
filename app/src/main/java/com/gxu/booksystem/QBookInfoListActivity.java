package com.gxu.booksystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gxu.booksystem.db.domain.Book;
import com.gxu.booksystem.pulltorefresh.task.CustomLastItemVisibleListener;
import com.gxu.booksystem.pulltorefresh.task.CustomRefreshListener;
import com.gxu.booksystem.pulltorefresh.task.CustomSoundPullEventListener;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.ImgUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;
import java.util.Set;


public class QBookInfoListActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private PullToRefreshListView list_book;
    private List<Book> intentData;
    private ListBookBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qbook_info_list);
        intentData=getIntentData();
        Log.i("QBookInfoListActivity",intentData.toString());
        adapter=new ListBookBaseAdapter(QBookInfoListActivity.this);
        list_book=(PullToRefreshListView)this.findViewById(R.id.list_book);

        // Set a listener to be invoked when the list should be refreshed.
        list_book.setOnRefreshListener(new CustomRefreshListener(
                QBookInfoListActivity.this,intentData,adapter,list_book));
        // Add an end-of-list listener
        list_book.setOnLastItemVisibleListener(new CustomLastItemVisibleListener(
                QBookInfoListActivity.this));
        // Add Sound Event Listener
        list_book.setOnPullEventListener(new CustomSoundPullEventListener(
                QBookInfoListActivity.this));


        adapter.setData(intentData);
        list_book.setAdapter(adapter);
        list_book.setOnItemClickListener(this);
    }

    private List<Book> getIntentData() {
        Set<String> keySet=getIntent().getExtras().keySet();
        for (String key:keySet){
            if (key.equals("list_book_withB_num")){
                List<Book> list=(List<Book>)getIntent().getSerializableExtra(key);
                return list;
            }
            if (key.equals("list_book_withB_name")){
                List<Book> list=(List<Book>)getIntent().getSerializableExtra(key);
                return list;
            }
            if (key.equals("list_book_withB_author")){
                List<Book> list=(List<Book>)getIntent().getSerializableExtra(key);
                return list;
            }
            if (key.equals("list_book_withB_press")){
                List<Book> list=(List<Book>)getIntent().getSerializableExtra(key);
                return list;
            }
            if (key.equals("q_book_with_b_name_author_press")){
                List<Book> list=(List<Book>)getIntent().getSerializableExtra(key);
                return list;
            }

        }
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("detail_book", (Book)list_book.getRefreshableView().getItemAtPosition(position));
        launch(QBookInfoListDetailActivity.class,bundle);
    }


    private class ListBookBaseAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<Book> list=null;

        public ListBookBaseAdapter(Context context){
            this.context=context;
            layoutInflater=LayoutInflater.from(context);
        }
        public void setData(List<Book> list){
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
            View view=null;
            if (convertView == null) {
                view= layoutInflater.inflate(R.layout.q_book_info_list_item, null);
            }else {
                view=convertView;
            }
            TextView list_b_name_value=(TextView)view.findViewById(R.id.list_b_name_value);
            TextView list_b_author_value=(TextView)view.findViewById(R.id.list_b_author_value);
            TextView list_list_b_press_value=(TextView)view.findViewById(R.id.list_list_b_press_value);
            final ImageView list_image=(ImageView)view.findViewById(R.id.list_image);
            list_b_name_value.setText(list.get(position).getB_name());
            list_b_author_value.setText(list.get(position).getB_author());
            list_list_b_press_value.setText(list.get(position).getB_press());

            //开启另外一个线程去加载图书图片
            String img_url=CommonUrl.LOAD_IMG+list.get(position).getB_img();
            ImgUtils.loadImage(new ImgUtils.ImageCallBack() {
                @Override
                public void getBitmap(Bitmap bitmap) {
                    list_image.setImageBitmap(bitmap);
                }
            },img_url);

            return view;
        }
    }
}
