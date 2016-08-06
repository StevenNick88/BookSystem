package com.gxu.booksystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gxu.booksystem.db.domain.Book;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.ImgUtils;
import com.gxu.booksystem.utils.JsonService;
import com.gxu.booksystem.utils.JsonTools;
import com.gxu.booksystem.utils.UploadFileTask;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class  BookManageActivity extends BaseActivity implements View.OnClickListener {

    private EditText b_num_value, b_buytime_value, b_name_value, b_author_value, b_press_value,
            introduction_value, count_value;

    private String bookMangeTitle[] = {"图书查询", "图书入库"};
    private List<View> bookMangeViews;
    private List<View> bqPagerViews;

    private TabPageIndicator bk_indicator;
    private ViewPager bk_pager, bq_pager;
    private EditText b_id_value;
    private Button b_query, b_img, add_book;
    private uk.co.senab.photoview.PhotoViewAttacher mAttacher;//图片放大缩小的包装器
    private final int BQ_PAGER = 4;
    private static final String TAG = "uploadImage";
    private String picPath = null;
    private ImageView b_img_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manage);
        init();
    }

    private void init() {
        bookMangeViews = getList();
        bk_indicator = (TabPageIndicator) this.findViewById(R.id.bk_indicator);
        bk_pager = (ViewPager) this.findViewById(R.id.bk_pager);
        //bk_pager适配器
        bk_pager.setAdapter(new BKMangePagerAdapter());
        bk_indicator.setViewPager(bk_pager);
    }


    public List<View> getList() {
        List<View> list = new ArrayList<View>();
        //图书查询View
        View v1 = getLayoutInflater().inflate(R.layout.query_book, null);
        bqPagerViews = getViews();
        b_id_value = (EditText) v1.findViewById(R.id.b_id_value);
        b_query = (Button) v1.findViewById(R.id.b_query);
        b_query.setOnClickListener(this);
        bq_pager = (ViewPager) v1.findViewById(R.id.bq_pager);
        bq_pager.setAdapter(new BQPagerAdapter());

        //图书入库View
        View v2 = getLayoutInflater().inflate(R.layout.add_book, null);
        add_book = (Button) v2.findViewById(R.id.add_book);
        b_img = (Button) v2.findViewById(R.id.b_img);
        add_book.setOnClickListener(this);
        b_img.setOnClickListener(this);

        b_num_value = (EditText) v2.findViewById(R.id.b_num_value);
        b_buytime_value = (EditText) v2.findViewById(R.id.b_buytime_value);
        b_name_value = (EditText) v2.findViewById(R.id.b_name_value);
        b_author_value = (EditText) v2.findViewById(R.id.b_author_value);
        b_press_value = (EditText) v2.findViewById(R.id.b_press_value);
        introduction_value = (EditText) v2.findViewById(R.id.introduction_value);
        count_value = (EditText) v2.findViewById(R.id.count_value);
        b_img_value = (ImageView) v2.findViewById(R.id.b_img_value);

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
                    Toast.makeText(BookManageActivity.this, "点击了图片", Toast.LENGTH_SHORT).show();
                }
            });

        }
        return views;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击查找图书
            case R.id.b_query:
                queryBook();
                break;
            //点击选择图片
            case R.id.b_img:
                chooseImg();
                break;
            //点击添加图书
            case R.id.add_book:
                addBook();
                break;
        }
    }

    private void chooseImg() {
        /***
         * 这个是调用android内置的intent，来过滤图片文件   ，同时也可以过滤其他的
         */
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //回调图片类使用的
        startActivityForResult(intent, RESULT_CANCELED);
    }

    /**
     * 回调执行的方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            Uri uri = data.getData();
            Log.e(TAG, "uri = " + uri);
            try {
                String[] pojo = {MediaStore.Images.Media.DATA};

                Cursor cursor = managedQuery(uri, pojo, null, null, null);
                if (cursor != null) {
                    ContentResolver cr = this.getContentResolver();
                    int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(colunm_index);
                    /***
                     * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，这样的话，我们判断文件的后缀名
                     * 如果是图片格式的话，那么才可以
                     */
                    if (path.endsWith("jpg") || path.endsWith("png")) {
                        picPath = path;
//                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        Bitmap bitmap = ImgUtils.getThumbnail(uri, cr,
                                MediaStore.Images.Thumbnails.MICRO_KIND, new BitmapFactory.Options());
                        b_img_value.setImageBitmap(bitmap);
                    } else {
                        alert();
                    }
                } else {
                    alert();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 回调使用
         */
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void alert() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("您选择的不是有效的图片")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                picPath = null;
                            }
                        })
                .create();
        dialog.show();
    }

    private void queryBook() {
        if (b_id_value.getText().toString().equals("")) {
            msg(this, "提示", "书号不能为空！");
        }else{
            Map<String, String> map = new HashMap<>();
            map.put("b_num", b_id_value.getText().toString());
            //从服务端获取json的数据并封装为javabean
            String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.BOOK_URL,
                    JsonService.createJsonString(map));
            Book book = JsonTools.getBook("book", jsonString);
            if (book != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("b_query", book);
                launch(BookInfoActivity.class, bundle);
            } else {
                msg(this, "失败信息", "对不起，没有这本书！");
            }
        }


    }

    private void addBook() {
        // 判断输入框是否为空
        if (b_num_value.getText().toString().equals("")) {
            msg(this, "提示", "书号不能为空！");
        } else if (b_buytime_value.getText().toString().equals("")) {
            msg(this, "提示", "购买日期不能为空！");
        } else if (b_name_value.getText().toString().equals("")) {
            msg(this, "提示", "书名不能为空！");
        } else if (count_value.getText().toString().equals("")) {
            msg(this, "提示", "数量不能为空！");
        } else if (b_author_value.getText().toString().equals("")) {
            msg(this, "提示", "作者不能为空！");
        } else if (b_press_value.getText().toString().equals("")) {
            msg(this, "提示", "出版社不能为空！");
        } else if (b_img_value.getDrawable() == null) {
            msg(this, "提示", "图书图片不能为空！");
        } else if (introduction_value.getText().toString().equals("")) {
            msg(this, "提示", "简介不能为空！");
        } else {
            if (picPath != null && picPath.length() > 0) {
                UploadFileTask uploadFileTask = new UploadFileTask(this);
                uploadFileTask.execute(picPath);
            }
            Map<String, String> map = new HashMap<>();
            map.put("b_num", b_num_value.getText().toString());
            map.put("b_buytime", b_buytime_value.getText().toString());
            map.put("b_author", b_author_value.getText().toString());
            map.put("b_press", b_press_value.getText().toString());
            map.put("b_name", b_name_value.getText().toString());
            map.put("introduction", introduction_value.getText().toString());
            map.put("count_value", count_value.getText().toString());
            Boolean flag1 = HttpUtils.sendJavaBeanToServer(CommonUrl.ADD_BOOK_URL,
                    JsonService.createJsonString(map));
            if (flag1 == true) {
                msg(this, "成功信息", "添加图书成功！");
            } else {
                msg(this, "失败信息", "添加图书失败！");
            }


        }
    }


    private class BQPagerAdapter extends PagerAdapter {

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public int getCount() {
            return bqPagerViews.size();
        }

        //显示界面方法
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            (container).removeView(bqPagerViews.get(position));
        }

        //销毁界面方法
        public Object instantiateItem(ViewGroup container, int position) {
            (container).addView(bqPagerViews.get(position));
            return bqPagerViews.get(position);
        }
    }


    private class BKMangePagerAdapter extends PagerAdapter {

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public int getCount() {
            return bookMangeTitle.length;
        }

        public CharSequence getPageTitle(int position) {
            return bookMangeTitle[position];
        }

        //显示界面方法
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            (container).removeView(bookMangeViews.get(position));
        }

        //销毁界面方法
        public Object instantiateItem(ViewGroup container, int position) {
            (container).addView(bookMangeViews.get(position));
            return bookMangeViews.get(position);
        }

    }

}
