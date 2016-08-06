package com.gxu.booksystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import com.gxu.booksystem.db.domain.BorrowedBook;
import com.gxu.booksystem.pulltorefresh.task.CustomSoundPullEventListener;
import com.gxu.booksystem.pulltorefresh.task.CustomLastItemVisibleListener;
import com.gxu.booksystem.pulltorefresh.task.CustomRefreshListener;
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


public class QBookAdminBorrowedActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private PullToRefreshListView admin_borrowed_books;
    private ListBorrowedBookBaseAdapter adapter;
    private List<BorrowedBook> listData;
    private ProgressDialog dialog;
    private EditText borrowed_edit;
    private Button q_stu_borrowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qbook_admin_borrowed);
        adapter = new ListBorrowedBookBaseAdapter(QBookAdminBorrowedActivity.this);
        borrowed_edit = (EditText) this.findViewById(R.id.borrowed_edit);
        q_stu_borrowed = (Button) this.findViewById(R.id.q_stu_borrowed);
        q_stu_borrowed.setOnClickListener(this);
        admin_borrowed_books = (PullToRefreshListView) this.findViewById(R.id.admin_borrowed_books);

        // Set a listener to be invoked when the list should be refreshed.
        admin_borrowed_books.setOnRefreshListener(new CustomRefreshListener(
                QBookAdminBorrowedActivity.this, listData, adapter, admin_borrowed_books));
        // Add an end-of-list listener
        admin_borrowed_books.setOnLastItemVisibleListener(new CustomLastItemVisibleListener(
                QBookAdminBorrowedActivity.this));
        // Add Sound Event Listener
        admin_borrowed_books.setOnPullEventListener(new CustomSoundPullEventListener(
                QBookAdminBorrowedActivity.this));

        //执行异步任务获取网络数据
        new QBookTask().execute(CommonUrl.BORROW_BOOKS_URL);
        admin_borrowed_books.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        queryBorrowedBook();
    }

    private void queryBorrowedBook() {
        // 判断输入框是否为空
        if (borrowed_edit.getText().toString().equals("")) {
            msg(this, "提示", "学号不能为空！");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("user_num", borrowed_edit.getText().toString());
            //从服务端获取json的数据并封装为javabean
            String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.BORROW_BOOK_WITH_USER_NUM_URL,
                    JsonService.createJsonString(map));
            List<BorrowedBook> list = JsonTools.getBorrowedBooks("borrowbook_with_usernum", jsonString);
            if (list != null && !list.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("borrowbook_with_usernum", (Serializable) list);
                launch(QBookAdminBorrowedListActivity.class, bundle);
            } else {
                msg(this, "提示", "该同学没有借阅书籍！");
            }
        }
    }


    public class QBookTask extends AsyncTask<String, Void, List<BorrowedBook>> {

        //执行耗时操作之前的准备
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(QBookAdminBorrowedActivity.this, "正在加载...", "系统正在处理您的请求");
        }

        //执行耗时操作
        @Override
        protected List<BorrowedBook> doInBackground(String[] params) {
            String jsonString = HttpUtils.getJsonContent(params[0]);
            List<BorrowedBook> list = JsonTools.getBorrowedBooks("borrowbooks", jsonString);

            return list;
        }

        //更新UI
        @Override
        protected void onPostExecute(List<BorrowedBook> borrowedBooks) {
            super.onPostExecute(borrowedBooks);
            listData = borrowedBooks;
            adapter.setData(borrowedBooks);
            admin_borrowed_books.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BorrowedBook borrowedBook = (BorrowedBook) admin_borrowed_books.getRefreshableView().
                getItemAtPosition(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("borrowed_book", borrowedBook);
        launch(BorrowedBookDetailActivity.class, bundle);
    }


    private class ListBorrowedBookBaseAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<BorrowedBook> list = null;

        public ListBorrowedBookBaseAdapter(Context context) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        public void setData(List<BorrowedBook> list) {
            this.list = list;
        }

        @Override
        public boolean isEnabled(int position) {
            if (!(list.get(position).getState().equals("借阅中") ||
                    list.get(position).getState().equals("续借中"))) {
                return false;
            }
            return true;
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
                view = layoutInflater.inflate(R.layout.borrowed_book_admin_item, null);
            } else {
                view = convertView;
            }
            TextView b_num_value = (TextView) view.findViewById(R.id.b_num_value);
            TextView b_name_value = (TextView) view.findViewById(R.id.b_name_value);
            TextView state_value = (TextView) view.findViewById(R.id.state_value);
            final ImageView list_image = (ImageView) view.findViewById(R.id.list_image);
            b_num_value.setText(list.get(position).getB_num());
            b_name_value.setText(list.get(position).getB_name());
            state_value.setText(list.get(position).getState());
            if (!(state_value.getText().toString().equals("借阅中") ||
                    list.get(position).getState().equals("续借中"))) {
                view.setBackgroundColor(Color.GRAY);
            }

            //开启另外一个线程去加载图书图片
            String img_url = CommonUrl.LOAD_IMG + list.get(position).getB_img();
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
