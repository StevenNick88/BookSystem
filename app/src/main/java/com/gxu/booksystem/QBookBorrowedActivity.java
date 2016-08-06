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
import android.widget.ImageView;
import android.widget.TextView;

import com.gxu.booksystem.db.domain.Admin;
import com.gxu.booksystem.db.domain.BorrowedBook;
import com.gxu.booksystem.db.domain.Student;
import com.gxu.booksystem.pulltorefresh.task.CustomSoundPullEventListener;
import com.gxu.booksystem.pulltorefresh.task.CustomLastItemVisibleListener;
import com.gxu.booksystem.pulltorefresh.task.CustomRefreshListener;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.ImgUtils;
import com.gxu.booksystem.utils.JsonService;
import com.gxu.booksystem.utils.JsonTools;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QBookBorrowedActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private PullToRefreshListView borrowed_books;
    private ListBorrowedBookBaseAdapter adapter;
    private List<BorrowedBook> listData;
//    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qbook_borrowed);
        adapter = new ListBorrowedBookBaseAdapter(QBookBorrowedActivity.this);
        borrowed_books = (PullToRefreshListView) this.findViewById(R.id.borrowed_books);

        // Set a listener to be invoked when the list should be refreshed.
        borrowed_books.setOnRefreshListener(new CustomRefreshListener(
                QBookBorrowedActivity.this, listData, adapter, borrowed_books));
        // Add an end-of-list listener
        borrowed_books.setOnLastItemVisibleListener(new CustomLastItemVisibleListener(
                QBookBorrowedActivity.this));
        // Add Sound Event Listener
        borrowed_books.setOnPullEventListener(new CustomSoundPullEventListener(
                QBookBorrowedActivity.this));

        //执行异步任务获取网络数据
        new QBookTask().execute(CommonUrl.BORROW_BOOK_WITH_USER_NUM_URL);
        borrowed_books.setOnItemClickListener(this);
    }


    public class QBookTask extends AsyncTask<String, Void, List<BorrowedBook>> {

        //执行耗时操作之前的准备
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = ProgressDialog.show(QBookBorrowedActivity.this, "正在加载...", "系统正在处理您的请求");
        }

        //执行耗时操作
        @Override
        protected List<BorrowedBook> doInBackground(String[] params) {
            List<BorrowedBook>  list=null;
            //首先判断当前用户是学生还是管理员，以便发送数据的服务端
            Map<String, Object> userMap = LoginActivity.getNowUserMap();
            for (String key : userMap.keySet()) {
                if (key.equals("学生")) {
                    Student now_user = (Student) userMap.get(key);
                    //从服务端获取json的数据并封装为javabean
                    Map<String, String> map = new HashMap<>();
                    map.put("user_num", now_user.getS_num());
                    //从服务端获取json的数据并封装为javabean
                    String jsonString = HttpUtils.sendInfoToServerGetJsonData(params[0],
                            JsonService.createJsonString(map));
                    list = JsonTools.getBorrowedBooks("borrowbook_with_usernum", jsonString);
                }
            }
            return list;
        }

        //更新UI
        @Override
        protected void onPostExecute(List<BorrowedBook> borrowedBooks) {
            super.onPostExecute(borrowedBooks);
            listData = borrowedBooks;
            adapter.setData(borrowedBooks);
            borrowed_books.setAdapter(adapter);
            adapter.notifyDataSetChanged();
//            dialog.dismiss();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BorrowedBook borrowedBook = (BorrowedBook) borrowed_books.getRefreshableView().
                getItemAtPosition(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("borrowed_book", borrowedBook);
        launch(BorrowedBookDetailActivity.class, bundle);
    }


    public class ListBorrowedBookBaseAdapter extends BaseAdapter {
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
                view = layoutInflater.inflate(R.layout.borrowed_book_item, null);
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
