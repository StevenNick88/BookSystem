package com.gxu.booksystem.db.domain;

import java.io.Serializable;

public class LossBook implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String b_num;
	private String b_name;
	private String user_num;
	private String state;
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
	public String getUser_num() {
		return user_num;
	}
	public void setUser_num(String user_num) {
		this.user_num = user_num;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getB_img() {
		return b_img;
	}
	public void setB_img(String b_img) {
		this.b_img = b_img;
	}


	@Override
	public String toString() {
		return "LossBook [id=" + id + ", b_num=" + b_num + ", b_name=" + b_name
				+ ", user_num=" + user_num + ", state=" + state + ", b_img="
				+ b_img + "]";
	}
	public LossBook(int id, String b_num, String b_name, String user_num,
			String state, String b_img) {
		super();
		this.id = id;
		this.b_num = b_num;
		this.b_name = b_name;
		this.user_num = user_num;
		this.state = state;
		this.b_img = b_img;
	}
	public LossBook() {
		super();
		// TODO 自动生成的构造函数存根
	}
	
	
	
	
}
