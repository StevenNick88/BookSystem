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

import com.gxu.booksystem.db.domain.LossBook;
import com.gxu.booksystem.pulltorefresh.task.CustomLastItemVisibleListener;
import com.gxu.booksystem.pulltorefresh.task.CustomRefreshListener;
import com.gxu.booksystem.pulltorefresh.task.CustomSoundPullEventListener;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.ImgUtils;
import com.gxu.booksystem.utils.JsonService;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LossBookListActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private List<LossBook> stuLossBooks;
    private PullToRefreshListView stu_loss_list;
    private StuLossBookBaseAdapter adapter;
    private LossBook listViewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss_book_list);
        adapter=new StuLossBookBaseAdapter(LossBookListActivity.this);
        stuLossBooks=(List<LossBook>)getIntent().getSerializableExtra("lossBook_user_num");

        stu_loss_list=(PullToRefreshListView)this.findViewById(R.id.stu_loss_list);
        // Set a listener to be invoked when the list should be refreshed.
        stu_loss_list.setOnRefreshListener(new CustomRefreshListener(
                LossBookListActivity.this,stuLossBooks,adapter,stu_loss_list));
        // Add an end-of-list listener
        stu_loss_list.setOnLastItemVisibleListener(new CustomLastItemVisibleListener(
                LossBookListActivity.this));
        // Add Sound Event Listener
        stu_loss_list.setOnPullEventListener(new CustomSoundPullEventListener(
                LossBookListActivity.this));

        adapter.setData(stuLossBooks);
        stu_loss_list.setAdapter(adapter);
        stu_loss_list.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listViewItem=(LossBook)stu_loss_list.getRefreshableView().getItemAtPosition(position);
        msgWithEvent(this,"提示信息","确定","取消","确定要交纳欠费并取消挂失吗？");

    }

    //点击确定触发的事件
    @Override
    public void onPositiveEvent() {
        Map<String, String> map = new HashMap<>();
        map.put("b_num", listViewItem.getB_num());
        map.put("user_num", listViewItem.getUser_num());
        Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.UPDATE_LOSS_BOOK_URL,
                JsonService.createJsonString(map));
        if (flag == true) {
            msg(this, "成功信息", "交纳欠费取消挂失成功！");
        } else {
            msg(this, "失败信息", "交纳欠费取消挂失失败！");
        }
    }

    private class StuLossBookBaseAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<LossBook> list=null;

        public StuLossBookBaseAdapter(Context context){
            this.context=context;
            layoutInflater=LayoutInflater.from(context);
        }
        public void setData(List<LossBook> list){
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
                view = layoutInflater.inflate(R.layout.loss_stu_listview_item, null);
            } else {
                view = convertView;
            }
            TextView b_num_value = (TextView) view.findViewById(R.id.b_num_value);
            TextView b_name_value = (TextView) view.findViewById(R.id.b_name_value);
            TextView user_num_value = (TextView) view.findViewById(R.id.user_num_value);
            TextView state_value = (TextView) view.findViewById(R.id.state_value);
            final ImageView list_image=(ImageView)view.findViewById(R.id.list_image);
            b_num_value.setText(list.get(position).getB_num());
            b_name_value.setText(list.get(position).getB_name());
            user_num_value.setText(list.get(position).getUser_num());
            state_value.setText(list.get(position).getState());

            //开启另外一个线程去加载图书图片
            String img_url=CommonUrl.LOAD_IMG+list.get(position).getB_img();
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
