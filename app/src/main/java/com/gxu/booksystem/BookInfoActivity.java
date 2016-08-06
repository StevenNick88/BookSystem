package com.gxu.booksystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gxu.booksystem.db.domain.Book;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.ImgUtils;
import com.gxu.booksystem.utils.ShareUtils;


public class BookInfoActivity extends BaseActivity implements View.OnClickListener{

    private TextView b_id_value,b_buytime_value,b_name_value,b_author_value,
            b_press_value,introduction_value,count_value;
    private Button update_book,delete_book,share_book;
    private Book b_query;
    private ImageView b_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        init();

    }

    private void init() {
        //获得图书查询界面传过来的book
        b_query = (Book)getIntent().getSerializableExtra("b_query");

        b_id_value=(TextView)this.findViewById(R.id.b_id_value);
        b_buytime_value=(TextView)this.findViewById(R.id.b_buytime_value);
        b_name_value=(TextView)this.findViewById(R.id.b_name_value);
        b_author_value=(TextView)this.findViewById(R.id.b_author_value);
        b_press_value=(TextView)this.findViewById(R.id.b_press_value);
        introduction_value=(TextView)this.findViewById(R.id.introduction_value);
        count_value=(TextView)this.findViewById(R.id.count_value);

        b_img=(ImageView)this.findViewById(R.id.b_img);
        getImage(b_img);

        b_id_value.setText(b_query.getB_num());
        b_buytime_value.setText(b_query.getB_buytime());
        b_name_value.setText(b_query.getB_name());
        b_author_value.setText(b_query.getB_author());
        b_press_value.setText(b_query.getB_press());
        introduction_value.setText(b_query.getIntroduction());
        count_value.setText(b_query.getCount());

        share_book=(Button)this.findViewById(R.id.share_book);
        update_book=(Button)this.findViewById(R.id.update_book);
        delete_book=(Button)this.findViewById(R.id.delete_book);
        share_book.setOnClickListener(this);
        update_book.setOnClickListener(this);
        delete_book.setOnClickListener(this);

    }

    private void getImage(final ImageView imageView) {
        ImgUtils.loadImage(new ImgUtils.ImageCallBack() {
            @Override
            public void getBitmap(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);

            }
        },CommonUrl.LOAD_IMG+b_query.getB_img());

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.share_book:
                ShareUtils.showShare(BookInfoActivity.this,null,b_query.getB_name(),null);
                break;
            case R.id.update_book:
                launch(UpdateBookActivity.class,new Intent(),"update_book",b_query);
                break;
            case R.id.delete_book:
                deleteBook();
                break;
        }
    }

    private void deleteBook() {
        //将图书的编码传递到服务端
        Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.DELETE_BOOK_URL,
                b_id_value.getText().toString());
        if (flag == true) {
            msgWithPositiveButton(this, "成功信息", "确定", "删除图书成功！");
        } else {
            msg(this, "失败信息", "删除图书失败！");
        }
    }

    @Override
    public void onPositiveEvent() {
        launch(BookManageActivity.class);
        finish();
    }
}
