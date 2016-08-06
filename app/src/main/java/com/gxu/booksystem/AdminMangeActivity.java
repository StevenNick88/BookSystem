package com.gxu.booksystem;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gxu.booksystem.db.domain.Admin;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.JsonService;
import com.gxu.booksystem.utils.JsonTools;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdminMangeActivity extends BaseActivity implements View.OnClickListener {

    private EditText m_num_value, m_permitted_value, m_pwd_value,m_permitborrowtime_value;

    private String adminMangeTitle[] = {"管理员查询", "添加管理员"};
    private List<View> adminMangeViews;
    private List<View> mqPagerViews;

    private TabPageIndicator a_indicator;
    private ViewPager a_pager, admin_query_pager;
    private EditText mq_num_value;
    private Button admin_query, add_admin;
    private uk.co.senab.photoview.PhotoViewAttacher mAttacher;//图片放大缩小的包装器
    private final int BQ_PAGER = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mange);
        init();
    }

    private void init() {
        adminMangeViews = getList();
        a_indicator = (TabPageIndicator) this.findViewById(R.id.a_indicator);
        a_pager = (ViewPager) this.findViewById(R.id.a_pager);
        //bk_pager适配器
        a_pager.setAdapter(new BKMangePagerAdapter());
        a_indicator.setViewPager(a_pager);
    }


    public List<View> getList() {
        List<View> list = new ArrayList<View>();
        //图书查询View
        View v1 = getLayoutInflater().inflate(R.layout.query_admin, null);
        mqPagerViews = getViews();
        mq_num_value = (EditText) v1.findViewById(R.id.mq_num_value);
        admin_query = (Button) v1.findViewById(R.id.admin_query);
        admin_query.setOnClickListener(this);
        admin_query_pager = (ViewPager) v1.findViewById(R.id.admin_query_pager);
        admin_query_pager.setAdapter(new BQPagerAdapter());

        //图书入库View
        View v2 = getLayoutInflater().inflate(R.layout.add_admin, null);
        add_admin = (Button) v2.findViewById(R.id.add_admin);
        add_admin.setOnClickListener(this);

        m_num_value = (EditText) v2.findViewById(R.id.m_num_value);
        m_permitted_value = (EditText) v2.findViewById(R.id.m_permitted_value);
        m_pwd_value = (EditText) v2.findViewById(R.id.m_pwd_value);
        m_permitborrowtime_value = (EditText) v2.findViewById(R.id.m_permitborrowtime_value);

        list.add(v1);
        list.add(v2);
        return list;
    }

    public List<View> getViews() {
        List<View> views = new ArrayList();
        Resources res = this.getResources();
        for (int i = 0; i < BQ_PAGER; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(res.getIdentifier("bg_img" + i, "drawable", this.getPackageName()));
            //实现图片放大缩小
            mAttacher = new uk.co.senab.photoview.PhotoViewAttacher(iv);
            LinearLayout ll = new LinearLayout(this);
            ll.setGravity(Gravity.CENTER);
            ll.addView(iv);
            views.add(ll);
            //点击图片事件
            iv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(AdminMangeActivity.this, "点击了图片", Toast.LENGTH_SHORT).show();
                }
            });

        }
        return views;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击查找图书
            case R.id.admin_query:
                queryAdmin();
                break;
            case R.id.add_admin:
                addAdmin();
                break;
        }
    }

    private void queryAdmin() {
        // 判断输入框是否为空
        if (mq_num_value.getText().toString().equals("")) {
            msg(this, "提示", "管理员ID不能为空！");
        }
        else  {
            Map<String, String> map = new HashMap<>();
            map.put("m_num", mq_num_value.getText().toString());

            //从服务端获取json的数据并封装为javabean
            String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.ADMIN_URL,
                    JsonService.createJsonString(map));
            Admin admin = JsonTools.getAdmin("admin", jsonString);
            if (admin != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("admin_query", admin);
                launch(AdminInfoActivity.class, bundle);
            }else{
                msg(this, "失败信息", "对不起，没有这个管理员！");
            }
        }
    }

    private void addAdmin() {
        // 判断输入框是否为空
        if (m_num_value.getText().toString().equals("")) {
            msg(this, "提示", "管理员ID不能为空！");
        }
        else if ( m_permitted_value.getText().toString().equals("")) {
            msg(this, "提示", "借阅许可不能为空！");
        }
        else if ( m_pwd_value.getText().toString().equals("")) {
            msg(this, "提示", "管理员密码不能为空！");
        }
        else if ( m_permitborrowtime_value.getText().toString().equals("")) {
            msg(this, "提示", "允许借阅时间不能为空！");
        }else{
            Map<String, String> map = new HashMap<>();
            map.put("m_num", m_num_value.getText().toString());
            map.put("m_permitted", m_permitted_value.getText().toString());
            map.put("m_pwd", m_pwd_value.getText().toString());
            map.put("m_permitborrowtime", m_permitborrowtime_value.getText().toString());
            Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.ADD_ADMIN_URL,
                    JsonService.createJsonString(map));
            if (flag == true) {
                msg(this, "成功信息", "添加管理员成功！");
            } else {
                msg(this, "失败信息", "添加管理员失败！");
            }
        }
    }

    private class BQPagerAdapter extends PagerAdapter {

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public int getCount() {
            return mqPagerViews.size();
        }

        //显示界面方法
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            (container).removeView(mqPagerViews.get(position));
        }

        //销毁界面方法
        public Object instantiateItem(ViewGroup container, int position) {
            (container).addView(mqPagerViews.get(position));
            return mqPagerViews.get(position);
        }
    }


    private class BKMangePagerAdapter extends PagerAdapter {

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public int getCount() {
            return adminMangeTitle.length;
        }

        public CharSequence getPageTitle(int position) {
            return adminMangeTitle[position];
        }

        //显示界面方法
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            (container).removeView(adminMangeViews.get(position));
        }

        //销毁界面方法
        public Object instantiateItem(ViewGroup container, int position) {
            (container).addView(adminMangeViews.get(position));
            return adminMangeViews.get(position);
        }

    }

}
