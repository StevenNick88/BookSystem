package com.gxu.booksystem;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxu.booksystem.db.domain.BorrowedBook;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.ImgUtils;
import com.gxu.booksystem.utils.JsonService;

import java.util.HashMap;
import java.util.Map;


public class PayFreeListDetailActivity extends BaseActivity implements View.OnClickListener{

    private TextView b_num_value,borrow_time_value,b_name_value,user_num_value,should_return_value,
            return_time_value,overtime_value,remain_time_value,state_value,should_pay_free_value;
    private Button pay_free_return;
    private BorrowedBook pay_borrowed_book;
    private ImageView b_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_free_list_detail);
        init();
    }

    private void init() {
        //获得列表信息界面传过来的book
        pay_borrowed_book = (BorrowedBook)getIntent().getSerializableExtra("pay_borrowed_book");
        //计算欠费
        String overtime=pay_borrowed_book.getOvertime();
        double pay_value=Integer.parseInt(overtime) * 0.02;

        b_num_value=(TextView)this.findViewById(R.id.b_num_value);
        borrow_time_value=(TextView)this.findViewById(R.id.borrow_time_value);
        b_name_value=(TextView)this.findViewById(R.id.b_name_value);
        user_num_value=(TextView)this.findViewById(R.id.user_num_value);
        return_time_value=(TextView)this.findViewById(R.id.return_time_value);
        overtime_value=(TextView)this.findViewById(R.id.overtime_value);
        remain_time_value=(TextView)this.findViewById(R.id.remain_time_value);
        state_value=(TextView)this.findViewById(R.id.state_value);
        should_pay_free_value=(TextView)this.findViewById(R.id.should_pay_free_value);
        should_return_value = (TextView) this.findViewById(R.id.should_return_value);
        b_img = (ImageView) this.findViewById(R.id.b_img);
        getImage(b_img);


        b_num_value.setText(pay_borrowed_book.getB_num());
        borrow_time_value.setText(pay_borrowed_book.getBorrow_time());
        b_name_value.setText(pay_borrowed_book.getB_name());
        user_num_value.setText(pay_borrowed_book.getUser_num());
        return_time_value.setText(pay_borrowed_book.getReturn_time());
        overtime_value.setText(pay_borrowed_book.getOvertime());
        remain_time_value.setText(pay_borrowed_book.getRemain_time());
        state_value.setText(pay_borrowed_book.getState());
        //显示欠费
        should_pay_free_value.setText(String.valueOf(pay_value));
        should_return_value.setText(pay_borrowed_book.getShouldreturn_time());

        pay_free_return=(Button)this.findViewById(R.id.pay_free_return);
        pay_free_return.setOnClickListener(this);
    }


    private void getImage(final ImageView imageView) {
        ImgUtils.loadImage(new ImgUtils.ImageCallBack() {
            @Override
            public void getBitmap(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);

            }
        }, CommonUrl.LOAD_IMG + pay_borrowed_book.getB_img());

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            //还书并交纳欠费
            case R.id.pay_free_return:
                payFree();
                break;

        }
    }

    private void payFree() {
        Map<String, String> map = new HashMap<>();
        map.put("b_num", pay_borrowed_book.getB_num());
        map.put("user_num", pay_borrowed_book.getUser_num());
        Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.PAY_FREE_URL,
                JsonService.createJsonString(map));
        if (flag == true) {
            msg(this, "成功信息", "还书并交纳欠费成功！");
        } else {
            msg(this, "失败信息", "还书并交纳欠费失败！");
        }
    }


}
