package com.gxu.booksystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gxu.booksystem.db.domain.BorrowedBook;
import com.gxu.booksystem.pulltorefresh.task.CustomLastItemVisibleListener;
import com.gxu.booksystem.pulltorefresh.task.CustomRefreshListener;
import com.gxu.booksystem.pulltorefresh.task.CustomSoundPullEventListener;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.ImgUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;


public class PayFreeListActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private List<BorrowedBook> stuPayBooks;
    private PullToRefreshListView stu_pay_list;
    private StuPayFreeBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_free_list);
        adapter=new StuPayFreeBaseAdapter(PayFreeListActivity.this);
        stuPayBooks=(List<BorrowedBook>)getIntent().getSerializableExtra("overtime_books");

        stu_pay_list=(PullToRefreshListView)this.findViewById(R.id.stu_pay_list);

        // Set a listener to be invoked when the list should be refreshed.
        stu_pay_list.setOnRefreshListener(new CustomRefreshListener(
                PayFreeListActivity.this,stuPayBooks,adapter,stu_pay_list));
        // Add an end-of-list listener
        stu_pay_list.setOnLastItemVisibleListener(new CustomLastItemVisibleListener(
                PayFreeListActivity.this));
        // Add Sound Event Listener
        stu_pay_list.setOnPullEventListener(new CustomSoundPullEventListener(
                PayFreeListActivity.this));

        adapter.setData(stuPayBooks);
        stu_pay_list.setAdapter(adapter);
        stu_pay_list.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("pay_borrowed_book", (BorrowedBook)stu_pay_list.
                getRefreshableView().getItemAtPosition(position));
        launch(PayFreeListDetailActivity.class,bundle);
    }

    private class StuPayFreeBaseAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<BorrowedBook> list=null;

        public StuPayFreeBaseAdapter(Context context){
            this.context=context;
            layoutInflater=LayoutInflater.from(context);
        }
        public void setData(List<BorrowedBook> list){
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
                view = layoutInflater.inflate(R.layout.pay_stu_listview_item, null);
            } else {
                view = convertView;
            }
            TextView b_num_value = (TextView) view.findViewById(R.id.b_num_value);
            TextView b_name_value = (TextView) view.findViewById(R.id.b_name_value);
            TextView user_num_value = (TextView) view.findViewById(R.id.user_num_value);
            TextView overtime_value = (TextView) view.findViewById(R.id.overtime_value);
            final ImageView list_image=(ImageView)view.findViewById(R.id.list_image);
            b_num_value.setText(list.get(position).getB_num());
            b_name_value.setText(list.get(position).getB_name());
            user_num_value.setText(list.get(position).getUser_num());
            overtime_value.setText(list.get(position).getOvertime());

            //开启另外一个线程去加载图书图片
            String img_url= CommonUrl.LOAD_IMG+list.get(position).getB_img();
            ImgUtils.loadImage(new ImgUtils.ImageCallBack() {
                @Override
                public void getBitmap(Bitmap bitmap) {
                    list_image.setImageBitmap(bitmap);
                }
            }, img_url);

            return view;
        }
    }



}
