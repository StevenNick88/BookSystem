package com.gxu.booksystem;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.gxu.booksystem.db.domain.Admin;
import com.gxu.booksystem.db.domain.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.AdapterView.OnItemClickListener;


public class StuMainActivity extends BaseActivity implements OnItemClickListener {

    private GridView gridView;
    private int resIds[] = new int[]{R.drawable.query_b,
            R.drawable.loss_b,
            R.drawable.yuyue,
            R.drawable.online_books,
            R.drawable.relax,
            R.drawable.about_b,
            R.drawable.help_b,
            R.drawable.unregiste,
            R.drawable.exit,
            R.drawable.more,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_main);
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
                launch(QBookActivity.class);
                break;
            case 1:
                launch(QBookBorrowedActivity.class);
                break;
            case 2:
                launch(QBookOrderedActivity.class);
                break;
            case 3:
                launch(OnlineBooksActivity.class);
                break;
            case 4:
                launch(RelaxationActivity.class);
                break;
            case 5:
                msg(StuMainActivity.this, "软件说明", getResources().getString(R.string.about_software_value));
                break;
            case 6:
                msg(StuMainActivity.this, "帮助说明", getResources().getString(R.string.help_value));
                break;
            case 7:
//                LoginActivity.setNowUser(null);
//                LoginActivity.setNowUserMap("学生");
                msgWithEvent(StuMainActivity.this, "提示", "确定", "取消", "确定要注销吗？");
                break;
            case 8:
                onBackPressed();
                break;
            case 9:
                msg(StuMainActivity.this, "提示", "更多功能尽请期待！");
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
