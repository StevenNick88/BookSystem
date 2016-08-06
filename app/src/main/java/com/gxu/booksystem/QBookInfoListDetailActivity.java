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
import com.gxu.booksystem.utils.ShareUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class QBookInfoListDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView b_id_value, b_buytime_value, b_name_value, b_author_value, count_value,
            b_press_value, introduction_value;
    private Button borrow_book, order_book, share_book;
    private Book detail_book;
    private ImageView b_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qbook_info_list_detail);
        init();
    }

    private void init() {
        //获得列表信息界面传过来的book
        detail_book = (Book) getIntent().getSerializableExtra("detail_book");

        b_id_value = (TextView) this.findViewById(R.id.b_id_value);
        b_buytime_value = (TextView) this.findViewById(R.id.b_buytime_value);
        b_name_value = (TextView) this.findViewById(R.id.b_name_value);
        b_author_value = (TextView) this.findViewById(R.id.b_author_value);
        b_press_value = (TextView) this.findViewById(R.id.b_press_value);
        introduction_value = (TextView) this.findViewById(R.id.introduction_value);
        count_value = (TextView) this.findViewById(R.id.count_value);
        b_img = (ImageView) this.findViewById(R.id.b_img);
        getImage(b_img);


        b_id_value.setText(detail_book.getB_num());
        b_buytime_value.setText(detail_book.getB_buytime());
        b_name_value.setText(detail_book.getB_name());
        b_author_value.setText(detail_book.getB_author());
        b_press_value.setText(detail_book.getB_press());
        introduction_value.setText(detail_book.getIntroduction());
        count_value.setText(detail_book.getCount());

        borrow_book = (Button) this.findViewById(R.id.borrow_book);
        order_book = (Button) this.findViewById(R.id.order_book);
        share_book = (Button) this.findViewById(R.id.share_book);
        borrow_book.setOnClickListener(this);
        order_book.setOnClickListener(this);
        share_book.setOnClickListener(this);
    }

    private void getImage(final ImageView imageView) {
        ImgUtils.loadImage(new ImgUtils.ImageCallBack() {
            @Override
            public void getBitmap(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);

            }
        }, CommonUrl.LOAD_IMG + detail_book.getB_img());

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.borrow_book:
                borrowBook();
                break;
            case R.id.order_book:
                orderBook();
                break;
            case R.id.share_book:
                ShareUtils.showShare(QBookInfoListDetailActivity.this, CommonUrl.LOAD_IMG+detail_book.getB_img(), detail_book.getB_name(), null);
                break;
        }
    }

    //借书：首先查询book表中所借图书的数量
    private void borrowBook() {
        Map<String, String> map = new HashMap<>();
        map.put("b_num", b_id_value.getText().toString());
        //从服务端获取json的数据并封装为javabean
        String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.BOOK_URL,
                JsonService.createJsonString(map));
        Book book = JsonTools.getBook("book", jsonString);
        //当图书馆中所借图书的数量大于0时才能借阅：将一条借书数据插入图书借阅信息表
        if (Integer.parseInt(book.getCount()) > 0) {
            //首先判断当前用户是学生还是管理员，以便发送数据的服务端
            Map<String, Object> userMap = LoginActivity.getNowUserMap();
            for (String key : userMap.keySet()) {
                if (key.equals("学生")) {
                    Student now_user = (Student) userMap.get(key);
                    borrowBookSendDataToServer(book, now_user);
                } else {
                    Admin now_user = (Admin) userMap.get(key);
                    borrowBookSendDataToServer(book, now_user);
                }
            }
        } else {
            msg(this, "失败信息", "对不起，没有这本书或者该书已被借空！");
        }
    }


    private void borrowBookSendDataToServer(Book book, Student student) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date borrow_time = new Date();
        long day15=(1000 * 60 * 60 * 24)*15;
        long diff =  borrow_time.getTime()+day15; // 这样得到的差值是微妙级别的
        Date shouldReturn_time = new Date(diff);

        Map<String, String> map2 = new HashMap<>();
        map2.put("count", book.getCount());
        map2.put("b_num", b_id_value.getText().toString());
        map2.put("b_name", b_name_value.getText().toString());
        map2.put("user_num", student.getS_num());
        map2.put("borrow_time", dateFormat.format(borrow_time));
        map2.put("shouldReturn_time", dateFormat.format(shouldReturn_time));
        map2.put("return_time", "");
        map2.put("overtime", "0");
        //学生的借书时间为15天
        map2.put("remain_time", "15");
        map2.put("state", "借阅中");
        map2.put("b_img", detail_book.getB_img());
        Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.ADD_BORROW_BOOK_URL,
                JsonService.createJsonString(map2));
        if (flag == true) {
            msg(this, "成功信息", "图书借阅成功！");

        } else {
            msg(this, "失败信息", "图书借阅失败！");
        }
    }


    private void borrowBookSendDataToServer(Book book, Admin admin) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date borrow_time = new Date();
        long day15=(1000 * 60 * 60 * 24)*15;
        long diff =  borrow_time.getTime()+day15; // 这样得到的差值是微妙级别的
        Date shouldReturn_time = new Date(diff);

        Map<String, String> map2 = new HashMap<>();
        map2.put("count", book.getCount());
        map2.put("b_num", b_id_value.getText().toString());
        map2.put("b_name", b_name_value.getText().toString());
        map2.put("user_num", admin.getM_num());
        map2.put("borrow_time", dateFormat.format(borrow_time));
        map2.put("shouldReturn_time", dateFormat.format(shouldReturn_time));
        map2.put("return_time", "");
        map2.put("overtime", "0");
        //学生的借书时间为15天
        map2.put("remain_time", "15");
        map2.put("state", "借阅中");
        map2.put("b_img", detail_book.getB_img());
        Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.ADD_BORROW_BOOK_URL,
                JsonService.createJsonString(map2));
        if (flag == true) {
            msg(this, "成功信息", "图书借阅成功！");

        } else {
            msg(this, "失败信息", "图书借阅失败！");
        }
    }

    /**
     *  预约书：首先查询orderedbook表中是否已经预约过该书，没有预约过才执行预约
         ----->查询book表中该书是否已全部被借阅，全部被借阅，才可预约
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
        map.put("b_num", detail_book.getB_num());
        map.put("user_num", student.getS_num());
        //从服务端获取json的数据并封装为javabean
        String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.ORDER_BOOK_WITH_USER_NUM_B_BUM_URL,
                JsonService.createJsonString(map));
        OrderedBook orderedBook = JsonTools.getOrderBook("orderbookwith_user_num_b_num", jsonString);
        if ( orderedBook.getState()!=null && orderedBook.getState().equals("预约中")) {
            msg(QBookInfoListDetailActivity.this, "提示", "您已经预约该书,请不要重复预约！");
        }
        // 没有预约过才执行预约----->查询book表中该书是否已全部被借阅，全部被借阅，才可预约
        else {
            int count = Integer.parseInt(detail_book.getCount());
            //如果该书被借阅，将一条预约书数据插入图书预约信息表
            if (count == 0) {
                msgWithEvent(QBookInfoListDetailActivity.this,"提示","确定预约","取消预约",
                        "您要预约的书籍已全部被借阅，确定要预约吗？");
                //如果该书未全部被借阅，提示可借阅该书
            }else {
                msg(QBookInfoListDetailActivity.this, "提示", "您要预约的书籍没有被借阅！" +
                        "您随时可以到图书馆查看借阅！无需预约！");
            }
        }
    }

    private void isOrderedAndQueryCount(Admin admin) {
        Map<String, String> map = new HashMap<>();
        map.put("b_num", detail_book.getB_num());
        map.put("user_num", admin.getM_num());
        //从服务端获取json的数据并封装为javabean
        String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.ORDER_BOOK_WITH_USER_NUM_B_BUM_URL,
                JsonService.createJsonString(map));
        OrderedBook orderedBook = JsonTools.getOrderBook("orderbookwith_user_num_b_num", jsonString);
        if ( orderedBook.getState()!=null && orderedBook.getState().equals("预约中")) {
            msg(QBookInfoListDetailActivity.this, "提示", "您已经预约该书,请不要重复预约！");
        }
        else {
            int count = Integer.parseInt(detail_book.getCount());
            //如果该书被借阅，将一条预约书数据插入图书预约信息表
            if (count == 0) {
                msgWithEvent(QBookInfoListDetailActivity.this,"提示","确定预约","取消预约",
                        "您要预约的书籍已全部被借阅，确定要预约吗？");
                //如果该书未全部被借阅，提示可借阅该书
            }else {
                msg(QBookInfoListDetailActivity.this, "提示", "您要预约的书籍没有被借阅！" +
                        "您随时可以到图书馆查看借阅！无需预约！");
            }
        }
    }

    //点击确定预约触发的事件:开始预约图书
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
        map2.put("b_img", detail_book.getB_img());
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
        map2.put("b_img", detail_book.getB_img());
        Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.ADD_ORDER_BOOK_URL,
                JsonService.createJsonString(map2));
        if (flag == true) {
            msg(this, "成功信息", "预约成功！");
        } else {
            msg(this, "失败信息", "预约失败！");
        }

    }
}
