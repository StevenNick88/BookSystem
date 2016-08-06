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

import com.gxu.booksystem.db.domain.Student;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.JsonService;
import com.gxu.booksystem.utils.JsonTools;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StuMangeActivity extends BaseActivity implements View.OnClickListener {

    private EditText s_num_value, s_name_value, s_age_value, s_sex_value, s_department_value,
            s_pwd_value,s_permitborrowtime_value;

    private String stuMangeTitle[] = {"学生查询", "添加学生"};
    private List<View> stuMangeViews;
    private List<View> stuQueryPagerViews;

    private TabPageIndicator stu_indicator;
    private ViewPager stu_pager, stu_query_pager;
    private EditText sq_num_value;
    private Button stu_query, add_stu;
    private uk.co.senab.photoview.PhotoViewAttacher mAttacher;//图片放大缩小的包装器
    private final int BQ_PAGER = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_mange);
        init();
    }

    private void init() {
        stuMangeViews = getList();
        stu_indicator = (TabPageIndicator) this.findViewById(R.id.stu_indicator);
        stu_pager = (ViewPager) this.findViewById(R.id.stu_pager);
        //bk_pager适配器
        stu_pager.setAdapter(new BKMangePagerAdapter());
        stu_indicator.setViewPager(stu_pager);
    }


    public List<View> getList() {
        List<View> list = new ArrayList<View>();
        //图书查询View
        View v1 = getLayoutInflater().inflate(R.layout.query_stu, null);
        stuQueryPagerViews = getViews();
        sq_num_value = (EditText) v1.findViewById(R.id.sq_num_value);
        stu_query = (Button) v1.findViewById(R.id.stu_query);
        stu_query.setOnClickListener(this);
        stu_query_pager = (ViewPager) v1.findViewById(R.id.stu_query_pager);
        stu_query_pager.setAdapter(new BQPagerAdapter());

        //图书入库View
        View v2 = getLayoutInflater().inflate(R.layout.add_stu, null);
        add_stu = (Button) v2.findViewById(R.id.add_stu);
        add_stu.setOnClickListener(this);
        s_num_value = (EditText) v2.findViewById(R.id.s_num_value);
        s_name_value = (EditText) v2.findViewById(R.id.s_name_value);
        s_age_value = (EditText) v2.findViewById(R.id.s_age_value);
        s_sex_value = (EditText) v2.findViewById(R.id.s_sex_value);
        s_department_value = (EditText) v2.findViewById(R.id.s_department_value);
        s_pwd_value = (EditText) v2.findViewById(R.id.s_pwd_value);
        s_permitborrowtime_value = (EditText) v2.findViewById(R.id.s_permitborrowtime_value);
        //学生的允许借书时间默认为15天
        s_permitborrowtime_value.setText("15");

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
                    Toast.makeText(StuMangeActivity.this, "点击了图片", Toast.LENGTH_SHORT).show();
                }
            });

        }
        return views;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击查找学生
            case R.id.stu_query:
                queryStu();
                break;
            case R.id.add_stu:
                addStu();
                break;
        }
    }

    private void queryStu() {
        // 判断输入框是否为空
        if (sq_num_value.getText().toString().equals("")) {
            msg(this, "提示", "学号不能为空！");
        }
        else  {
            Map<String, String> map = new HashMap<>();
            map.put("s_num", sq_num_value.getText().toString());
            //从服务端获取json的数据并封装为javabean
            String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.STUDENT_URL,
                    JsonService.createJsonString(map));
            Student student = JsonTools.getStudent("student", jsonString);
            if (student != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("stu_query", student);
                launch(StuInfoActivity.class, bundle);
            }else{
                msg(this, "失败信息", "对不起，没有这个学生！");
            }
        }
    }

    private void addStu() {
        // 判断输入框是否为空
        if (s_num_value.getText().toString().equals("")) {
            msg(this, "提示", "学号不能为空！");
        }
        else if ( s_name_value.getText().toString().equals("")) {
            msg(this, "提示", "姓名不能为空！");
        }
        else if ( s_age_value.getText().toString().equals("")) {
            msg(this, "提示", "年龄不能为空！");
        }
        else if ( s_sex_value.getText().toString().equals("")) {
            msg(this, "提示", "性别不能为空！");
        }
        else if ( s_department_value.getText().toString().equals("")) {
            msg(this, "提示", "院系不能为空！");
        }
        else if ( s_pwd_value.getText().toString().equals("")) {
            msg(this, "提示", "密码不能为空！");
        }
        else if ( s_permitborrowtime_value.getText().toString().equals("")) {
            msg(this, "提示", "允许借书时间不能为空！");
        }else{
            Map<String, String> map = new HashMap<>();
            map.put("s_num", s_num_value.getText().toString());
            map.put("s_name", s_name_value.getText().toString());
            map.put("s_age", s_age_value.getText().toString());
            map.put("s_sex", s_sex_value.getText().toString());
            map.put("s_department", s_department_value.getText().toString());
            map.put("s_pwd", s_pwd_value.getText().toString());
            map.put("s_permitborrowtime", s_permitborrowtime_value.getText().toString());
            Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.ADD_STUDENT_URL,
                    JsonService.createJsonString(map));
            if (flag == true) {
                msg(this, "成功信息", "添加学生成功！");
            } else {
                msg(this, "失败信息", "添加学生失败！");
            }
        }
    }


    private class BQPagerAdapter extends PagerAdapter {

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public int getCount() {
            return stuQueryPagerViews.size();
        }

        //显示界面方法
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            (container).removeView(stuQueryPagerViews.get(position));
        }

        //销毁界面方法
        public Object instantiateItem(ViewGroup container, int position) {
            (container).addView(stuQueryPagerViews.get(position));
            return stuQueryPagerViews.get(position);
        }
    }


    private class BKMangePagerAdapter extends PagerAdapter {

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public int getCount() {
            return stuMangeTitle.length;
        }

        public CharSequence getPageTitle(int position) {
            return stuMangeTitle[position];
        }

        //显示界面方法
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            (container).removeView(stuMangeViews.get(position));
        }

        //销毁界面方法
        public Object instantiateItem(ViewGroup container, int position) {
            (container).addView(stuMangeViews.get(position));
            return stuMangeViews.get(position);
        }

    }

}
