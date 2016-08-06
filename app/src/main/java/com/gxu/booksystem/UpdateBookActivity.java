package com.gxu.booksystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gxu.booksystem.db.domain.Book;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.ImgUtils;
import com.gxu.booksystem.utils.JsonService;
import com.gxu.booksystem.utils.UploadFileTask;

import java.util.HashMap;
import java.util.Map;


public class UpdateBookActivity extends BaseActivity implements View.OnClickListener{

    private EditText b_num_value,b_buytime_value,b_name_value,b_author_value,b_press_value,
            introduction_value,count_value;
    private Button update_book,select_img;
    private Book b_update;
    private ImageView b_img;
    private Bitmap bitmap;
    private static final String TAG = "uploadImage";
    private static final String FLAG = "updatedImage";
    private String picPath = null;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);
        init();
    }

    private void init() {
        //获得图书详细信息界面传过来的book
        b_update = (Book)getIntent().getSerializableExtra("update_book");
        dialog= createProgressDialog(UpdateBookActivity.this,"提示","正在处理数据。。。请稍后！");

        b_num_value=(EditText)this.findViewById(R.id.b_num_value);
        b_buytime_value=(EditText)this.findViewById(R.id.b_buytime_value);
        b_name_value=(EditText)this.findViewById(R.id.b_name_value);
        b_author_value=(EditText)this.findViewById(R.id.b_author_value);
        b_press_value=(EditText)this.findViewById(R.id.b_press_value);
        introduction_value=(EditText)this.findViewById(R.id.introduction_value);
        count_value=(EditText)this.findViewById(R.id.count_value);

        b_img=(ImageView)this.findViewById(R.id.b_img);
        getImage(b_img);

        b_num_value.setText(b_update.getB_num());
        //图书ID不可修改，修改之后无法插入数据库
        b_num_value.setEnabled(false);
        b_buytime_value.setText(b_update.getB_buytime());
        b_name_value.setText(b_update.getB_name());
        b_author_value.setText(b_update.getB_author());
        b_press_value.setText(b_update.getB_press());
        introduction_value.setText(b_update.getIntroduction());
        count_value.setText(b_update.getCount());

        update_book =(Button)this.findViewById(R.id.update_book);
        select_img =(Button)this.findViewById(R.id.select_img);
        update_book.setOnClickListener(this);
        select_img.setOnClickListener(this);
    }

    private void getImage(final ImageView imageView) {
        ImgUtils.loadImage(new ImgUtils.ImageCallBack() {
            @Override
            public void getBitmap(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);

            }
        },CommonUrl.LOAD_IMG+b_update.getB_img());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_book:
                updateBook();
                break;
            //更改图片
            case R.id.select_img:
                chooseImg();
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
                        b_img.setImageBitmap(bitmap);
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




    private void updateBook() {
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
        } else if (b_img.getDrawable() == null) {
            msg(this, "提示", "图书图片不能为空！");
        } else if (introduction_value.getText().toString().equals("")) {
            msg(this, "提示", "简介不能为空！");
        } else {
            dialog.show();
            Map<String, String> map = new HashMap<>();
            Drawable drawable = new BitmapDrawable(bitmap);
            //如果修改了图书图片:将一个修改了图书图片的标识传到服务端
            if (!(b_img.getDrawable().equals(drawable))){
                map.put(FLAG,"true");
            //未修改图片
            }else{
                map.put(FLAG,"false");
            }
            //如果修改了图书图片才进行图片上传操作：异步任务开启另一个线程
            if (!(b_img.getDrawable().equals(drawable))&&picPath != null && picPath.length() > 0) {
                UploadFileTask uploadFileTask = new UploadFileTask(this);
                uploadFileTask.execute(picPath);
            }
            map.put("b_num", b_num_value.getText().toString());
            map.put("b_buytime", b_buytime_value.getText().toString());
            map.put("b_author", b_author_value.getText().toString());
            map.put("b_press", b_press_value.getText().toString());
            map.put("b_name", b_name_value.getText().toString());
            map.put("introduction", introduction_value.getText().toString());
            map.put("count", count_value.getText().toString());
            map.put("b_img", b_update.getB_img());
            Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.UPDATE_BOOK_URL,
                    JsonService.createJsonString(map));
            if (flag == true) {
                dialog.dismiss();
                msg(this, "成功信息", "修改图书成功！");
            } else {
                dialog.dismiss();
                msg(this, "失败信息", "修改图书失败！");
            }
        }
    }




}
