package com.gxu.booksystem;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.gxu.booksystem.db.domain.Admin;
import com.gxu.booksystem.db.domain.BorrowedBook;
import com.gxu.booksystem.db.domain.Student;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.JsonService;
import com.gxu.booksystem.utils.JsonTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QBookActivity extends BaseActivity implements View.OnClickListener{

    private Button main_book_query,borrowed_book_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qbook);
        main_book_query=(Button)this.findViewById(R.id.main_book_query);
        borrowed_book_query=(Button)this.findViewById(R.id.borrowed_book_query);
        main_book_query.setOnClickListener(this);
        borrowed_book_query.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.main_book_query:
                launch(QBookInfoActivity.class);
                break;
            case R.id.borrowed_book_query:
                //首先判断当前用户是学生还是管理员，分别跳转到不同的界面
                Map<String, Object> userMap = LoginActivity.getNowUserMap();
                for (String key : userMap.keySet()) {
                    if (key.equals("学生")) {
                        launch(QBookBorrowedActivity.class);
                    } else {
                        launch(QBookAdminBorrowedActivity.class);
                    }
                }
                break;
        }

    }
}
