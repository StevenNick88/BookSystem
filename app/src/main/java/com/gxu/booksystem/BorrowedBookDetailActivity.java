package com.gxu.booksystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxu.booksystem.db.domain.BorrowedBook;
import com.gxu.booksystem.receiver.OrderReceiver;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.ImgUtils;
import com.gxu.booksystem.utils.JsonService;
import com.gxu.booksystem.utils.JsonTools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class BorrowedBookDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView b_num_value, borrow_time_value, b_name_value, user_num_value,
            return_time_value, should_return_value,overtime_value, remain_time_value, state_value;
    private Button return_book, loss_book, borrow_book_again;
    private ImageView b_img;
    private BorrowedBook borrowed_book;
    private SimpleDateFormat dateFormat;
    private Date date;
    private int overtime;
    private int remain_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_book_detail);
        init();
    }

    private void init() {
        //获得列表信息界面传过来的book
        borrowed_book = (BorrowedBook) getIntent().getSerializableExtra("borrowed_book");

        b_num_value = (TextView) this.findViewById(R.id.b_num_value);
        borrow_time_value = (TextView) this.findViewById(R.id.borrow_time_value);
        b_name_value = (TextView) this.findViewById(R.id.b_name_value);
        user_num_value = (TextView) this.findViewById(R.id.user_num_value);
        return_time_value = (TextView) this.findViewById(R.id.return_time_value);
        overtime_value = (TextView) this.findViewById(R.id.overtime_value);
        remain_time_value = (TextView) this.findViewById(R.id.remain_time_value);
        state_value = (TextView) this.findViewById(R.id.state_value);
        should_return_value = (TextView) this.findViewById(R.id.should_return_value);
        b_img = (ImageView) this.findViewById(R.id.b_img);
        getImage(b_img);


        b_num_value.setText(borrowed_book.getB_num());
        borrow_time_value.setText(borrowed_book.getBorrow_time());
        b_name_value.setText(borrowed_book.getB_name());
        user_num_value.setText(borrowed_book.getUser_num());
        return_time_value.setText(borrowed_book.getReturn_time());

        state_value.setText(borrowed_book.getState());
        return_book = (Button) this.findViewById(R.id.return_book);
        loss_book = (Button) this.findViewById(R.id.loss_book);
        borrow_book_again = (Button) this.findViewById(R.id.borrow_book_again);
        return_book.setOnClickListener(this);
        loss_book.setOnClickListener(this);
        borrow_book_again.setOnClickListener(this);

        getBorrowedInfo();

    }

    private void getImage(final ImageView imageView) {
        ImgUtils.loadImage(new ImgUtils.ImageCallBack() {
            @Override
            public void getBitmap(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);

            }
        }, CommonUrl.LOAD_IMG + borrowed_book.getB_img());

    }

    //从服务端获取超期时间和剩余时间
    private void getBorrowedInfo() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date();
        Map<String, String> map = new HashMap<>();
        map.put("b_num", b_num_value.getText().toString());
        map.put("return_time", dateFormat.format(date));
        //从服务端获取json的数据并封装为javabean
        String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.RETURN_BORROW_BOOK_PRE_URL,
                JsonService.createJsonString(map));
        Map<String, Object> borrowedInfo = JsonTools.getMaps("borrowedInfo", jsonString);
        overtime_value.setText((String) borrowedInfo.get("overtime"));
        remain_time_value.setText((String) borrowedInfo.get("remain_time"));
        should_return_value.setText((String) borrowedInfo.get("shouldReturnTime"));

        overtime = Integer.parseInt((String) borrowedInfo.get("overtime"));
        remain_time = Integer.parseInt((String) borrowedInfo.get("remain_time"));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //还书
            case R.id.return_book:
                returnBook();
                break;
            //挂失
            case R.id.loss_book:
                lossBook();
                break;
            //续借
            case R.id.borrow_book_again:
                borrowBookAgain();
                break;
        }
    }

    //还书：先判断是否超期，再在图书借阅信息表中修改该书的相关信息，最后将book表中该书的数量加1.
    private void returnBook() {
        //超期
        if (overtime > 0) {
            msg(this, "提示", "对不起，您所借的图书已经超期，超期天数为：" + overtime + "天，请先与管理员联系交纳欠款再还书！");
            //未超期
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(BorrowedBookDetailActivity.this);
            builder.setTitle("提示");
            builder.setMessage("您所借的图书还剩余：" + remain_time + "天,确定要还书吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Map<String, String> map2 = new HashMap<>();
                    map2.put("b_num", b_num_value.getText().toString());
                    map2.put("b_name", b_name_value.getText().toString());
                    map2.put("user_num", user_num_value.getText().toString());
                    map2.put("return_time", dateFormat.format(date));
                    //学生的借书时间为15天
                    map2.put("state", "已还书");
                    Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.RETURN_BORROW_BOOK_URL,
                            JsonService.createJsonString(map2));
                    if (flag == true) {
                        msg(BorrowedBookDetailActivity.this, "成功信息", "还书成功！");
                        Intent intent=new Intent(BorrowedBookDetailActivity.this, OrderReceiver.class);
                        intent.putExtra("b_num",b_num_value.getText().toString());
                        sendBroadcast(intent);
                    } else {
                        msg(BorrowedBookDetailActivity.this, "失败信息", "还书失败！");
                    }
                }
            });
            builder.create();
            builder.show();

        }
    }


    //挂失：将挂失的图书插入挂失图书信息表中
    private void lossBook() {
        msgWithEvent(this, "挂失声明", "确定挂失", "取消挂失",
                "凡丢失图书的读者，在挂失成功后请速与图书管理员联系，解决图书遗失事宜！");
    }

    //点击确定挂失触发的事件
    @Override
    public void onPositiveEvent() {
        Map<String, String> map = new HashMap<>();
        map.put("b_num", b_num_value.getText().toString());
        map.put("b_name", b_name_value.getText().toString());
        map.put("user_num", user_num_value.getText().toString());
        map.put("state", "挂失中");
        map.put("b_img", borrowed_book.getB_img());
        Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.ADD_LOSS_BOOK_URL,
                JsonService.createJsonString(map));
        if (flag == true) {
            msg(this, "成功信息", "挂失成功！");
        } else {
            msg(this, "失败信息", "挂失失败！");
        }
    }


    //续借：首先判断是否超期，未超期才能续借
    private void borrowBookAgain() {
        if (overtime > 0) {
            msg(this, "提示信息", "对不起，请所借的图书已经超期，请先还书并交纳欠款再借阅！");
            //未超期，可续借：修改图书借阅信息表中的borrow_time以及其他字段的值
        } else {
            Date d1 = null;
            Date d2 = null;
            try {
                d1 = new Date();                     //今天的日期
                long fifteenTimes = 15 * 24 * 60 * 60 * 1000;     //15天的毫秒数
                long diff = d1.getTime() + fifteenTimes;    //这样得到的差值是微妙级别的
                d2 = new Date(diff);                      //今天+15天之后的日期
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map<String, String> map2 = new HashMap<>();
            map2.put("b_num", b_num_value.getText().toString());
            map2.put("b_name", b_name_value.getText().toString());
            map2.put("user_num", user_num_value.getText().toString());
            map2.put("borrow_time", dateFormat.format(d1));
            map2.put("shouldReturn_time", dateFormat.format(d2));
            map2.put("return_time", "");
            map2.put("state", "续借中");
            Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.AGAIN_BORROW_BOOK_URL,
                    JsonService.createJsonString(map2));
            if (flag == true) {
                msg(this, "成功信息", "续借成功！");
            } else {
                msg(this, "失败信息", "续借失败！");
            }
        }
    }


}
