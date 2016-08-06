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
import android.widget.ListView;
import android.widget.TextView;

import com.gxu.booksystem.db.domain.BorrowedBook;
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


public class PayFreeActivity extends BaseActivity implements
        View.OnClickListener,AdapterView.OnItemClickListener {

    private EditText pay_edit;
    private Button q_stu_pay;
    private PullToRefreshListView pay_list;
    private PayFreeBaseAdapter adapter;
    private ProgressDialog dialog;
    private List<BorrowedBook> listData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_free);
        adapter=new PayFreeBaseAdapter(PayFreeActivity.this);
        pay_edit=(EditText)this.findViewById(R.id.pay_edit);
        q_stu_pay=(Button)this.findViewById(R.id.q_stu_pay);
        q_stu_pay.setOnClickListener(this);
        pay_list=(PullToRefreshListView)this.findViewById(R.id.pay_list);

        // Set a listener to be invoked when the list should be refreshed.
        pay_list.setOnRefreshListener(new CustomRefreshListener(
                PayFreeActivity.this,listData,adapter,pay_list));
        // Add an end-of-list listener
        pay_list.setOnLastItemVisibleListener(new CustomLastItemVisibleListener(
                PayFreeActivity.this));
        // Add Sound Event Listener
        pay_list.setOnPullEventListener(new CustomSoundPullEventListener(
                PayFreeActivity.this));

        pay_list.setOnItemClickListener(this);

        new PayFreeTask().execute(CommonUrl.BORROW_BOOK_WITH_OVERTIME_URL);
    }

    @Override
    public void onClick(View v) {
        queryLossBook();
    }

    private void queryLossBook() {
        // 判断输入框是否为空
        if (pay_edit.getText().toString().equals("")) {
            msg(this, "提示", "学号不能为空！");
        }
        else{
            Map<String, String> map = new HashMap<>();
            map.put("user_num", pay_edit.getText().toString());
            //从服务端获取json的数据并封装为javabean
            String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.BORROW_BOOK_WITH_OVERTIME_USER_NUM_URL,
                    JsonService.createJsonString(map));
            List<BorrowedBook> list = JsonTools.getBorrowedBooks("borrowbook_with_overtime_usernum", jsonString);
            if (list != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("overtime_books", (Serializable)list);
                launch(PayFreeListActivity.class, bundle);
            }else{
                msg(this, "提示信息", "对不起，该同学没有欠费书籍！");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("pay_borrowed_book", (BorrowedBook)pay_list.getRefreshableView().getItemAtPosition(position));
        launch(PayFreeListDetailActivity.class,bundle);

    }

    private class PayFreeTask extends AsyncTask<String, Void ,List<BorrowedBook>> {

        //执行耗时操作之前的准备
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(PayFreeActivity.this, "正在加载...", "系统正在处理您的请求");
        }

        //执行耗时操作
        @Override
        protected List<BorrowedBook> doInBackground(String[] params) {
            //从服务端获取json的数据并封装为javabean
            String jsonString = HttpUtils.getJsonContent(params[0]);
            List<BorrowedBook> list = JsonTools.getBorrowedBooks("borrowbook_with_overtime", jsonString);
            return list;
        }

        //更新UI
        @Override
        protected void onPostExecute(List<BorrowedBook> borrowedBooks) {
            super.onPostExecute(borrowedBooks);
            listData=borrowedBooks;
            adapter.setData(borrowedBooks);
            pay_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }

    private class PayFreeBaseAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<BorrowedBook> list=null;

        public PayFreeBaseAdapter(Context context){
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
                view = layoutInflater.inflate(R.layout.pay_listview_item, null);
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
