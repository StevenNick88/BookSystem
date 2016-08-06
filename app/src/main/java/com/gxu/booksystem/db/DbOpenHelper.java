package com.gxu.booksystem.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

    private static String name = "library.db";// 表示数据库的名称
    private static int version = 1;// 表示数据库的版本号码

    public DbOpenHelper(Context context) {
        super(context, name, null, version);
    }

    // 当数据库创建的时候，是第一次被执行,完成对数据库的表的创建
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        //支持的数据类型：整型数据，字符串类型，日期类型，二进制的数据类型，
        String sql = "create table users(id integer primary key autoincrement,name varchar(64),password varchar(64),comment varchar(64))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
//		String sql = "alter table person add sex varchar(8)";
//		db.execSQL(sql);
    }

}
