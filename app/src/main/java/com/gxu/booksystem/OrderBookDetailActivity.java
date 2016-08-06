package com.gxu.booksystem;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxu.booksystem.db.domain.Admin;
import com.gxu.booksystem.db.domain.Book;
import com.gxu.booksystem.db.domain.BorrowedBook;
import com.gxu.booksystem.db.domain.OrderedBook;
import com.gxu.booksystem.db.domain.Student;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.ImgUtils;
import com.gxu.booksystem.utils.JsonService;
import com.gxu.booksystem.utils.JsonTools;

import java.util.HashMap;
import java.util.Map;


public class OrderBookDetailActivity extends BaseActivity implements View.OnClickListener{

    private TextView b_id_value,b_buytime_value,b_name_value,b_author_value,
            b_press_value,introduction_value,count_value;
    private Button orderBookBtn;
    private Book ordered_book;
    private ImageView b_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_book_detail);
        init();

    }

    private void init() {
        //获得预约图书界面传过来的book
        ordered_book = (Book)getIntent().getSerializableExtra("ordered_book");

        b_id_value=(TextView)this.findViewById(R.id.b_id_value);
        b_buytime_value=(TextView)this.findViewById(R.id.b_buytime_value);
        b_name_value=(TextView)this.findViewById(R.id.b_name_value);
        b_author_value=(TextView)this.findViewById(R.id.b_author_value);
        b_press_value=(TextView)this.findViewById(R.id.b_press_value);
        introduction_value=(TextView)this.findViewById(R.id.introduction_value);
        count_value=(TextView)this.findViewById(R.id.count_value);

        b_img=(ImageView)this.findViewById(R.id.b_img);
        getImage(b_img);

        b_id_value.setText(ordered_book.getB_num());
        b_buytime_value.setText(ordered_book.getB_buytime());
        b_name_value.setText(ordered_book.getB_name());
        b_author_value.setText(ordered_book.getB_author());
        b_press_value.setText(ordered_book.getB_press());
        introduction_value.setText(ordered_book.getIntroduction());
        count_value.setText(ordered_book.getCount());

        orderBookBtn=(Button)this.findViewById(R.id.order_book);
        orderBookBtn.setOnClickListener(this);

    }

    private void getImage(final ImageView imageView) {
        ImgUtils.loadImage(new ImgUtils.ImageCallBack() {
            @Override
            public void getBitmap(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);

            }
        }, CommonUrl.LOAD_IMG + ordered_book.getB_img());

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.order_book:
                orderBook();
                break;

        }
    }


    /**
     *  预约书：首先查询orderedbook表中是否已经预约过该书，没有预约过才执行预约;
     查询book表中该书是否已全部被借阅，全部被借阅，才可预约;
     如果该书被借阅，将一条预约书数据插入图书预约信息表.
     */
    private void orderBook() {
        //首先判断当前用户是学生还是管理员，以便发送数据到服务端
        Map<String, Object> userMap = LoginActivity.getNowUserMap();
        for (String key : userMap.keySet()) {
            if (key.equals("学生")) {
                Student now_user = (Student) userMap.get(key);
                isOrderedAndQueryCount(now_user);
            } else {
                Admin now_user = (Admin) userMap.get(key);
                isOrderedAndQueryCount(now_user);
            }
        }
    }

    //首先查询orderedbook表中是否已经预约过该书，没有预约过才执行预约----->查询book表中该书是
    // 否已全部被借阅，全部被借阅，才可预约
    private void isOrderedAndQueryCount(Student student) {
        Map<String, String> map = new HashMap<>();
        map.put("b_num", ordered_book.getB_num());
        map.put("user_num", student.getS_num());
        //从服务端获取json的数据并封装为javabean
        String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.ORDER_BOOK_WITH_USER_NUM_B_BUM_URL,
                JsonService.createJsonString(map));
        OrderedBook orderedBook = JsonTools.getOrderBook("orderbookwith_user_num_b_num", jsonString);
        if ( orderedBook.getState()!=null && orderedBook.getState().equals("预约中")) {
            msg(OrderBookDetailActivity.this, "提示", "您已经预约该书,请不要重复预约！");
        }
        // 没有预约过才执行预约----->查询book表中该书是否已全部被借阅，全部被借阅，才可预约
        else {
            int count = Integer.parseInt(ordered_book.getCount());
            //如果该书被借阅，将一条预约书数据插入图书预约信息表
            if (count == 0) {
                msgWithEvent(OrderBookDetailActivity.this,"提示","确定预约","取消预约",
                        "您要预约的书籍已全部被借阅，确定要预约吗？");
                //如果该书未全部被借阅，提示可借阅该书
            }else {
                msg(OrderBookDetailActivity.this, "提示", "您要预约的书籍没有被借阅！" +
                        "您随时可以到图书馆查看借阅！无需预约！");
            }
        }
    }

    private void isOrderedAndQueryCount(Admin admin) {
        Map<String, String> map = new HashMap<>();
        map.put("b_num", ordered_book.getB_num());
        map.put("user_num", admin.getM_num());
        //从服务端获取json的数据并封装为javabean
        String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.ORDER_BOOK_WITH_USER_NUM_B_BUM_URL,
                JsonService.createJsonString(map));
        OrderedBook orderedBook = JsonTools.getOrderBook("orderbookwith_user_num_b_num", jsonString);
        if ( orderedBook.getState()!=null && orderedBook.getState().equals("预约中")) {
            msg(OrderBookDetailActivity.this, "提示", "您已经预约该书,请不要重复预约！");
        }
        else {
            int count = Integer.parseInt(ordered_book.getCount());
            //如果该书被借阅，将一条预约书数据插入图书预约信息表
            if (count == 0) {
                msgWithEvent(OrderBookDetailActivity.this,"提示","确定预约","取消预约",
                        "您要预约的书籍已全部被借阅，确定要预约吗？");
                //如果该书未全部被借阅，提示可借阅该书
            }else {
                msg(OrderBookDetailActivity.this, "提示", "您要预约的书籍没有被借阅！" +
                        "您随时可以到图书馆查看借阅！无需预约！");
            }
        }
    }

    //点击确定预约触发的事件
    @Override
    public void onPositiveEvent() {
        //首先判断当前用户是学生还是管理员，以便发送数据的服务端
        Map<String, Object> userMap = LoginActivity.getNowUserMap();
        for (String key : userMap.keySet()) {
            if (key.equals("学生")) {
                Student now_user = (Student) userMap.get(key);
                orderBookSendDataToServer(now_user);
            } else {
                Admin now_user = (Admin) userMap.get(key);
                orderBookSendDataToServer(now_user);
            }
        }
    }



    //点击取消预约触发的事件
    @Override
    public void onNegativeEvent() {
        super.onNegativeEvent();
    }

    private void orderBookSendDataToServer(Student student){
        Map<String, String> map2 = new HashMap<>();
        map2.put("b_num", b_id_value.getText().toString());
        map2.put("b_name", b_name_value.getText().toString());
        map2.put("user_num", student.getS_num());
        map2.put("state", "预约中");
        map2.put("b_img", ordered_book.getB_img());
        Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.ADD_ORDER_BOOK_URL,
                JsonService.createJsonString(map2));
        if (flag == true) {
            msg(this, "成功信息", "预约成功！");
        } else {
            msg(this, "失败信息", "预约失败！");
        }
    }

    private void orderBookSendDataToServer(Admin admin){
        Map<String, String> map2 = new HashMap<>();
        map2.put("b_num", b_id_value.getText().toString());
        map2.put("b_name", b_name_value.getText().toString());
        map2.put("user_num", admin.getM_num());
        map2.put("state", "预约中");
        map2.put("b_img", ordered_book.getB_img());
        Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.ADD_ORDER_BOOK_URL,
                JsonService.createJsonString(map2));
        if (flag == true) {
            msg(this, "成功信息", "预约成功！");
        } else {
            msg(this, "失败信息", "预约失败！");
        }

    }



}
