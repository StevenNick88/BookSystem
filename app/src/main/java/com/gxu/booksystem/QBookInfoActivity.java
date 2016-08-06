package com.gxu.booksystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.gxu.booksystem.db.domain.Book;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.JsonService;
import com.gxu.booksystem.utils.JsonTools;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QBookInfoActivity extends BaseActivity implements View.OnClickListener {
    private EditText b_name_value, b_author_value, b_press_value;

    private String[] qBookTitle = {"简单查询", "高级查询"};
    private List<View> qBookView;
    private List<String> sq_data;
    private TabPageIndicator qbi_indicator;
    private ViewPager qbi_pager;
    private Spinner sq_spinner;
    private EditText sq_edit;
    private Button sq_button, sq_complex_button;
    private String sq_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qbook_info);
        qbi_indicator = (TabPageIndicator) this.findViewById(R.id.qbi_indicator);
        qBookView = getView();
        qbi_pager = (ViewPager) this.findViewById(R.id.qbi_pager);
        qbi_pager.setAdapter(new QBookPagerAdapter());
        qbi_indicator.setViewPager(qbi_pager);
    }

    public List<View> getView() {
        List<View> list = new ArrayList<>();
        //简单查询View
        View v1 = LayoutInflater.from(QBookInfoActivity.this).inflate(R.layout.simple_q_book, null);
        sq_spinner = (Spinner) v1.findViewById(R.id.sq_spinner);
        sq_edit = (EditText) v1.findViewById(R.id.sq_edit);
        sq_button = (Button) v1.findViewById(R.id.sq_button);
        sq_button.setOnClickListener(this);
        sq_data = getData();
        sq_spinner.setAdapter(new ArrayAdapter(QBookInfoActivity.this,
                android.R.layout.simple_spinner_item, sq_data));

        //高级查询View
        View v2 = LayoutInflater.from(QBookInfoActivity.this).inflate(R.layout.complex_q_book, null);
        b_name_value = (EditText) v2.findViewById(R.id.b_name_value);
        b_author_value = (EditText) v2.findViewById(R.id.b_author_value);
        b_press_value = (EditText) v2.findViewById(R.id.b_press_value);
        sq_complex_button = (Button) v2.findViewById(R.id.sq_complex_button);
        sq_complex_button.setOnClickListener(this);

        list.add(v1);
        list.add(v2);
        return list;
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        list.add("书号");
        list.add("书名");
        list.add("作者");
        list.add("出版社");
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //简单查询
            case R.id.sq_button:
                //根据不同的查询方式查询
                switch (sq_spinner.getSelectedItem().toString()) {

                    case "书号":
                        queryBookWithB_num();
                        break;
                    case "书名":
                        queryBookWithB_name();
                        break;
                    case "作者":
                        queryBookWithB_author();
                        break;
                    case "出版社":
                        queryBookWithB_press();
                        break;

                }
                break;
            //高级查询
            case R.id.sq_complex_button:
                queryBookWithB_name_author_press();
                break;

        }
    }

    private void queryBookWithB_name_author_press() {
        Map<String, String> map = new HashMap<>();
        map.put("b_name", b_name_value.getText().toString());
        map.put("b_author", b_author_value.getText().toString());
        map.put("b_press", b_press_value.getText().toString());

        //从服务端获取json的数据并封装为javabean
        String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.BOOK_URL_WITH_B_NAME_AUTHOR_PRESS,
                JsonService.createJsonString(map));
        List<Book> list = JsonTools.getBooks("q_book_with_b_name_author_press", jsonString);
        if (list != null) {
            launch(QBookInfoListActivity.class, new Intent(), "q_book_with_b_name_author_press", list);
        } else {
            msg(this, "失败信息", "对不起，没有这本书！");
        }

    }


    private void queryBookWithB_num() {
        Map<String, String> map = new HashMap<>();
        map.put("b_num", sq_edit.getText().toString());
        //从服务端获取json的数据并封装为javabean
        String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.BOOK_URL,
                JsonService.createJsonString(map));
        Book book = JsonTools.getBook("book", jsonString);
        List<Book> list = new ArrayList<Book>();
        list.add(book);
        if (list != null) {
            launch(QBookInfoListActivity.class, new Intent(), "list_book_withB_num", list);
        } else {
            msg(this, "失败信息", "对不起，没有这本书！");
        }
    }

    private void queryBookWithB_name() {
        Map<String, String> map = new HashMap<>();
        map.put("b_name", sq_edit.getText().toString());
        //从服务端获取json的数据并封装为javabean
        String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.BOOK_URL_WITH_B_NAME,
                JsonService.createJsonString(map));
        List<Book> list = JsonTools.getBooks("list_book_withB_name", jsonString);
        if (list != null) {
            launch(QBookInfoListActivity.class, new Intent(), "list_book_withB_name", list);
        } else {
            msg(this, "失败信息", "对不起，没有这本书！");
        }
    }


    private void queryBookWithB_author() {
        Map<String, String> map = new HashMap<>();
        map.put("b_author", sq_edit.getText().toString());
        //从服务端获取json的数据并封装为javabean
        String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.BOOK_URL_WITH_B_AUTHOR,
                JsonService.createJsonString(map));
        List<Book> list = JsonTools.getBooks("list_book_withB_author", jsonString);
        if (list != null) {
            launch(QBookInfoListActivity.class, new Intent(), "list_book_withB_author", list);
        } else {
            msg(this, "失败信息", "对不起，没有这本书！");
        }

    }

    private void queryBookWithB_press() {
        Map<String, String> map = new HashMap<>();
        map.put("b_press", sq_edit.getText().toString());
        //从服务端获取json的数据并封装为javabean
        String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.BOOK_URL_WITH_B_PRESS,
                JsonService.createJsonString(map));
        List<Book> list = JsonTools.getBooks("list_book_withB_press", jsonString);
        if (list != null) {
            launch(QBookInfoListActivity.class, new Intent(), "list_book_withB_press", list);
        } else {
            msg(this, "失败信息", "对不起，没有这本书！");
        }
    }


    private class QBookPagerAdapter extends PagerAdapter {
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public int getCount() {
            return qBookTitle.length;
        }

        public CharSequence getPageTitle(int position) {
            return qBookTitle[position];
        }

        //显示界面方法
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            (container).removeView(qBookView.get(position));
        }

        //销毁界面方法
        public Object instantiateItem(ViewGroup container, int position) {
            (container).addView(qBookView.get(position));
            return qBookView.get(position);
        }
    }


}
