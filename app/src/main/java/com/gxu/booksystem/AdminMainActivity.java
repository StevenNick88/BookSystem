package com.gxu.booksystem;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdminMainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private GridView gridView;
    private int resIds[] = new int[]{R.drawable.book_mangement,
            R.drawable.student_mangement,
            R.drawable.admin_mangement,
            R.drawable.query_book,
            R.drawable.loss_mangement,
            R.drawable.reserve_mangement,
            R.drawable.pay_mangement,
            R.drawable.online_books,
            R.drawable.relax,
            R.drawable.unregiste,
            R.drawable.exit,
            R.drawable.more,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        getOverflowMenu();

        gridView = (GridView) this.findViewById(R.id.gridViews);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < resIds.length; i++) {
            Map<String, Object> cell = new HashMap<>();
            cell.put("imageView", resIds[i]);
            list.add(cell);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.admin_mangement_item,
                new String[]{"imageView"}, new int[]{R.id.imageView});
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onPositiveEvent() {
        launch(LoginActivity.class);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                launch(BookManageActivity.class);
                break;
            case 1:
                launch(StuMangeActivity.class);
                break;
            case 2:
                launch(AdminMangeActivity.class);
                break;
            case 3:
                launch(QBookActivity.class);
                break;
            case 4:
                launch(LossBookActivity.class);
                break;
            case 5:
                launch(QBookOrderedActivity.class);
                break;
            case 6:
                launch(PayFreeActivity.class);
                break;
            case 7:
                launch(OnlineBooksActivity.class);
                break;
            case 8:
                launch(RelaxationActivity.class);
                break;
            case 9:
//                LoginActivity.setNowUser(null);
//                LoginActivity.setNowUserMap("管理员");
                msgWithEvent(AdminMainActivity.this, "提示", "确定", "取消", "确定要注销吗？");
                break;
            case 10:
                onBackPressed();
                break;
            case 11:
                msg(AdminMainActivity.this, "提示", "更多功能尽请期待！");
                break;
            default:
                break;

        }
    }


    long waitTime = 2000;
    long touchTime = 0;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= waitTime) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            touchTime = currentTime;
        } else {
            finish();
        }
    }

}
