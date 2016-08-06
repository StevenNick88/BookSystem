package com.gxu.booksystem.db.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/2/7.
 */

public class Book implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String b_num;
    private String b_name;
    private String b_author;
    private String b_press;
    private String b_buytime;
    private String introduction;
    private String count;
    private String b_img;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getB_num() {
        return b_num;
    }
    public void setB_num(String b_num) {
        this.b_num = b_num;
    }
    public String getB_name() {
        return b_name;
    }
    public void setB_name(String b_name) {
        this.b_name = b_name;
    }
    public String getB_author() {
        return b_author;
    }
    public void setB_author(String b_author) {
        this.b_author = b_author;
    }
    public String getB_press() {
        return b_press;
    }
    public void setB_press(String b_press) {
        this.b_press = b_press;
    }
    public String getB_buytime() {
        return b_buytime;
    }
    public void setB_buytime(String b_buytime) {
        this.b_buytime = b_buytime;
    }
    public String getIntroduction() {
        return introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public String getCount() {
        return count;
    }
    public void setCount(String count) {
        this.count = count;
    }
    public String getB_img() {
        return b_img;
    }
    public void setB_img(String b_img) {
        this.b_img = b_img;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Book() {
        super();
        // TODO 自动生成的构造函数存根
    }

    public Book(int id, String b_num, String b_name, String b_author,
                String b_press, String b_buytime, String introduction,
                String count, String b_img) {
        super();
        this.id = id;
        this.b_num = b_num;
        this.b_name = b_name;
        this.b_author = b_author;
        this.b_press = b_press;
        this.b_buytime = b_buytime;
        this.introduction = introduction;
        this.count = count;
        this.b_img = b_img;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", b_num=" + b_num + ", b_name=" + b_name
                + ", b_author=" + b_author + ", b_press=" + b_press
                + ", b_buytime=" + b_buytime + ", introduction=" + introduction
                + ", count=" + count + ", b_img=" + b_img + "]";
    }





}