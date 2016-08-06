package com.gxu.booksystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxu.booksystem.db.domain.LossBook;
import com.gxu.booksystem.pulltorefresh.task.CustomLastItemVisibleListener;
import com.gxu.booksystem.pulltorefresh.task.CustomRefreshListener;
import com.gxu.booksystem.pulltorefresh.task.CustomSoundPullEventListener;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.ImgUtils;
import com.gxu.booksystem.utils.JsonService;
import com.gxu.booksystem.utils.JsonTools;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LossBookActivity extends BaseActivity implements
        View.OnClickListener,AdapterView.OnItemClickListener {

    private EditText loss_edit;
    private Button q_stu_loss;
    private PullToRefreshListView loss_list;
    private LossBookBaseAdapter adapter;
    private LossBook listViewItem;
    private ProgressDialog dialog;
    private List<LossBook> listData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss_book);
        adapter=new LossBookBaseAdapter(LossBookActivity.this);
        loss_edit=(EditText)this.findViewById(R.id.loss_edit);
        q_stu_loss=(Button)this.findViewById(R.id.q_stu_loss);
        loss_list=(PullToRefreshListView)this.findViewById(R.id.loss_list);


        // Set a listener to be invoked when the list should be refreshed.
        loss_list.setOnRefreshListener(new CustomRefreshListener(
                LossBookActivity.this,listData,adapter,loss_list));
        // Add an end-of-list listener
        loss_list.setOnLastItemVisibleListener(new CustomLastItemVisibleListener(
                LossBookActivity.this));
        // Add Sound Event Listener
        loss_list.setOnPullEventListener(new CustomSoundPullEventListener(
                LossBookActivity.this));

        q_stu_loss.setOnClickListener(this);
        loss_list.setOnItemClickListener(this);

        new QLossBookTask().execute(CommonUrl.LOSS_BOOKS_URL);

    }

    @Override
    public void onClick(View v) {
        queryLossBook();
    }

    private void queryLossBook() {
        // 判断输入框是否为空
        if (loss_edit.getText().toString().equals("")) {
            msg(this, "提示", "学号不能为空！");
        }
        else  {
            Map<String, String> map = new HashMap<>();
            map.put("user_num", loss_edit.getText().toString());
            //从服务端获取json的数据并封装为javabean
            String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.LOSS_BOOK_WITH_USER_NAME_URL,
                    JsonService.createJsonString(map));
            List<LossBook> list = JsonTools.getLossBooks("lossbookwith_user_num", jsonString);
            if (list != null && !list.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("lossBook_user_num", (Serializable)list);
                launch(LossBookListActivity.class, bundle);
            }else{
                msg(this, "提示", "该同学没有挂失书籍！");
            }

//
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listViewItem=(LossBook)loss_list.getRefreshableView().getItemAtPosition(position);
//        listViewItem=(LossBook)adapter.getItem(position);
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

    private class QLossBookTask extends AsyncTask<String, Void ,List<LossBook>> {

        //执行耗时操作之前的准备
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(LossBookActivity.this, "正在加载...", "系统正在处理您的请求");
        }

        //执行耗时操作
        @Override
        protected List<LossBook> doInBackground(String[] params) {
            //从服务端获取json的数据并封装为javabean
            String jsonString = HttpUtils.getJsonContent(params[0]);
            List<LossBook> list = JsonTools.getLossBooks("lossbooks", jsonString);
            return list;
        }

        //更新UI
        @Override
        protected void onPostExecute(List<LossBook> lossBooks) {
            super.onPostExecute(lossBooks);
            listData=lossBooks;
            adapter.setData(lossBooks);
            loss_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }

    private class LossBookBaseAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<LossBook> list=null;

        public LossBookBaseAdapter(Context context){
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
                view = layoutInflater.inflate(R.layout.loss_listview_item, null);
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
