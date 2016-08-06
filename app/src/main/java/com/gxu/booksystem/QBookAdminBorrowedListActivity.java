package com.gxu.booksystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxu.booksystem.db.domain.BorrowedBook;
import com.gxu.booksystem.pulltorefresh.task.CustomLastItemVisibleListener;
import com.gxu.booksystem.pulltorefresh.task.CustomRefreshListener;
import com.gxu.booksystem.pulltorefresh.task.CustomSoundPullEventListener;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.ImgUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;


public class QBookAdminBorrowedListActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private List<BorrowedBook> stuBorrowedBooks;
    private PullToRefreshListView admin_borrowed_list_books;
    private ListBorrowedBookBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qborrowed_book_admin_list);
        adapter=new ListBorrowedBookBaseAdapter(QBookAdminBorrowedListActivity.this);
        stuBorrowedBooks=(List<BorrowedBook>)getIntent().getSerializableExtra("borrowbook_with_usernum");

        admin_borrowed_list_books=(PullToRefreshListView)this.findViewById(R.id.admin_borrowed_list_books);
        // Set a listener to be invoked when the list should be refreshed.
        admin_borrowed_list_books.setOnRefreshListener(new CustomRefreshListener(
                QBookAdminBorrowedListActivity.this,stuBorrowedBooks,adapter,admin_borrowed_list_books));
        // Add an end-of-list listener
        admin_borrowed_list_books.setOnLastItemVisibleListener(new CustomLastItemVisibleListener(
                QBookAdminBorrowedListActivity.this));
        // Add Sound Event Listener
        admin_borrowed_list_books.setOnPullEventListener(new CustomSoundPullEventListener(
                QBookAdminBorrowedListActivity.this));

        adapter.setData(stuBorrowedBooks);
        admin_borrowed_list_books.setAdapter(adapter);
        admin_borrowed_list_books.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BorrowedBook borrowedBook=(BorrowedBook)admin_borrowed_list_books.getRefreshableView().
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
            if (!(list.get(position).getState().equals("借阅中")||
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
                view = layoutInflater.inflate(R.layout.borrowed_book_admin_list_item, null);
            } else {
                view = convertView;
            }
            TextView b_num_value = (TextView) view.findViewById(R.id.b_num_value);
            TextView b_name_value = (TextView) view.findViewById(R.id.b_name_value);
            TextView state_value = (TextView) view.findViewById(R.id.state_value);
            final ImageView list_image=(ImageView)view.findViewById(R.id.list_image);
            b_num_value.setText(list.get(position).getB_num());
            b_name_value.setText(list.get(position).getB_name());
            state_value.setText(list.get(position).getState());
            if (!(state_value.getText().toString().equals("借阅中")||
                    list.get(position).getState().equals("续借中"))) {
                view.setBackgroundColor(Color.GRAY);
            }

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
