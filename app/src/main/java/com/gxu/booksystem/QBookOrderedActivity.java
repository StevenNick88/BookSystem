package com.gxu.booksystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

import com.gxu.booksystem.db.domain.Admin;
import com.gxu.booksystem.db.domain.Book;
import com.gxu.booksystem.db.domain.BorrowedBook;
import com.gxu.booksystem.db.domain.LossBook;
import com.gxu.booksystem.db.domain.OrderedBook;
import com.gxu.booksystem.db.domain.Student;
import com.gxu.booksystem.pulltorefresh.task.CustomLastItemVisibleListener;
import com.gxu.booksystem.pulltorefresh.task.CustomRefreshListener;
import com.gxu.booksystem.pulltorefresh.task.CustomSoundPullEventListener;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.ImgUtils;
import com.gxu.booksystem.utils.JsonService;
import com.gxu.booksystem.utils.JsonTools;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.viewpagerindicator.TabPageIndicator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QBookOrderedActivity extends BaseActivity implements
        View.OnClickListener,AdapterView.OnItemClickListener{

    private String orderMangeTitle[] = {"预约图书", "预约管理"};
    private List<View> orderMangeViews;
    private TabPageIndicator order_indicator;
    private ViewPager order_pager;
    private EditText order_b_num;
    private Button query_book;
    private PullToRefreshListView order_manage_list;
    private OrderManageBaseAdapter adapter;
    private ProgressDialog dialog;
    private OrderedBook listViewItem;
    private List<OrderedBook> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qbook_ordered);
        order_indicator = (TabPageIndicator) this.findViewById(R.id.order_indicator);
        order_pager = (ViewPager) this.findViewById(R.id.order_pager);
        adapter=new OrderManageBaseAdapter(QBookOrderedActivity.this) ;
        orderMangeViews = getViews();
        order_pager.setAdapter(new OrderManagePageAdapter());
        order_indicator.setViewPager(order_pager);

        //根据不同的用户执行异步任务获取数据
        startQOrderBookTask();
    }

    private void startQOrderBookTask() {
        //首先判断当前用户是学生还是管理员，以便发送数据的服务端
        Map<String, Object> userMap = LoginActivity.getNowUserMap();
        for (String key : userMap.keySet()) {
            if (key.equals("学生")) {
                //执行异步任务获取网络数据
                new QOrderBookTask().execute(CommonUrl.ORDER_BOOK_WITH_USER_NUM_URL);
            //管理员
            } else {
                //执行异步任务获取网络数据
                new QOrderBookTask().execute(CommonUrl.ORDER_BOOKS_URL);
            }
        }

    }


    public List<View> getViews() {
        List<View> list = new ArrayList<>();
        View v1 = LayoutInflater.from(QBookOrderedActivity.this).inflate(R.layout.order_item, null);
        order_b_num = (EditText) v1.findViewById(R.id.order_b_num);
        query_book = (Button) v1.findViewById(R.id.query_book);
        query_book.setOnClickListener(this);

        View v2 = LayoutInflater.from(QBookOrderedActivity.this).inflate(R.layout.order_manage_list, null);
        order_manage_list = (PullToRefreshListView) v2.findViewById(R.id.order_manage_list);
        order_manage_list.setOnItemClickListener(this);
        // Set a listener to be invoked when the list should be refreshed.
        order_manage_list.setOnRefreshListener(new CustomRefreshListener(
                QBookOrderedActivity.this,listData,adapter,order_manage_list));
        // Add an end-of-list listener
        order_manage_list.setOnLastItemVisibleListener(new CustomLastItemVisibleListener(
                QBookOrderedActivity.this));
        // Add Sound Event Listener
        order_manage_list.setOnPullEventListener(new CustomSoundPullEventListener(
                QBookOrderedActivity.this));

        list.add(v1);
        list.add(v2);
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.query_book:
                queryBookWithB_num();
                break;

        }
    }

    private void queryBookWithB_num() {
        if (order_b_num.getText().toString().equals("")) {
            msg(this, "提示", "书号不能为空！");
        }else{
            Map<String, String> map = new HashMap<>();
            map.put("b_num", order_b_num.getText().toString());
            //从服务端获取json的数据并封装为javabean
            String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.BOOK_URL,
                    JsonService.createJsonString(map));
            Book book = JsonTools.getBook("book", jsonString);
            if (book != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("ordered_book", book);
                launch(OrderBookDetailActivity.class, bundle);
            }else{
                msg(this, "失败信息", "对不起，没有这本书！");
            }
        }

    }

    //取消预约
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listViewItem=(OrderedBook)order_manage_list.getRefreshableView().getItemAtPosition(position);
        msgWithEvent(this,"提示信息","确定","取消","确定要取消预约图书吗？");
    }

    //点击确定触发的事件
    @Override
    public void onPositiveEvent() {
        Map<String, String> map = new HashMap<>();
        map.put("b_num", listViewItem.getB_num());
        map.put("user_num", listViewItem.getUser_num());
        Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.CANCEL_ORDER_BOOK_URL,
                JsonService.createJsonString(map));
        if (flag == true) {
            msg(this, "成功信息", "取消预约图书成功！");
        } else {
            msg(this, "失败信息", "取消预约图书失败！");
        }
    }


    private class QOrderBookTask extends AsyncTask<String, Void ,List<OrderedBook>> {

        //执行耗时操作之前的准备
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(QBookOrderedActivity.this, "正在加载...", "系统正在处理您的请求");
        }

        //执行耗时操作
        @Override
        protected List<OrderedBook> doInBackground(String[] params) {
            //首先判断当前用户是学生还是管理员，以便发送数据的服务端
            List<OrderedBook> list = null;
            Map<String, Object> userMap = LoginActivity.getNowUserMap();
            for (String key : userMap.keySet()) {
                //学生端只可获取自己的预约记录
                if (key.equals("学生")) {
                    Student now_user = (Student) userMap.get(key);
                    //从服务端获取json的数据并封装为javabean
                    Map<String, String> map = new HashMap<>();
                    map.put("user_num", now_user.getS_num());
                    //从服务端获取json的数据并封装为javabean
                    String jsonString = HttpUtils.sendInfoToServerGetJsonData(params[0],
                            JsonService.createJsonString(map));
                    list = JsonTools.getOrderBooks("orderbookwith_user_num", jsonString);
                    System.out.println(list);
                //管理员端可获取所有预约记录
                } else {
                    //从服务端获取json的数据并封装为javabean
                    String jsonString = HttpUtils.getJsonContent(params[0]);
                    list = JsonTools.getOrderBooks("orderbooks", jsonString);
                }
            }
            return list;

        }

        //更新UI
        @Override
        protected void onPostExecute(List<OrderedBook> orderedBooks) {
            super.onPostExecute(orderedBooks);
            listData=orderedBooks;
            adapter.setData(orderedBooks);
            order_manage_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }


    private class OrderManagePageAdapter extends PagerAdapter {


        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public int getCount() {
            return orderMangeTitle.length;
        }

        public CharSequence getPageTitle(int position) {
            return orderMangeTitle[position];
        }

        //显示界面方法
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            (container).removeView(orderMangeViews.get(position));
        }

        //销毁界面方法
        public Object instantiateItem(ViewGroup container, int position) {
            (container).addView(orderMangeViews.get(position));
            return orderMangeViews.get(position);
        }
    }

    private class OrderManageBaseAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<OrderedBook> list = null;

        public OrderManageBaseAdapter(Context context) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        public void setData(List<OrderedBook> list) {
            this.list = list;
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
                view = layoutInflater.inflate(R.layout.order_listview_item, null);
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
