package com.gxu.booksystem.db.domain;

import java.io.Serializable;

public class BorrowedBook implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String b_num;
	private String b_name;
	private String user_num;
	private String borrow_time;
	private String shouldreturn_time;
	private String return_time;
	private String overtime;
	private String remain_time;
	private String state;
	private String b_img;
	
	
	public String getB_img() {
		return b_img;
	}
	public void setB_img(String b_img) {
		this.b_img = b_img;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
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
	public String getBorrow_time() {
		return borrow_time;
	}
	public void setBorrow_time(String borrow_time) {
		this.borrow_time = borrow_time;
	}
	public String getReturn_time() {
		return return_time;
	}
	public void setReturn_time(String return_time) {
		this.return_time = return_time;
	}
	public String getOvertime() {
		return overtime;
	}
	public void setOvertime(String overtime) {
		this.overtime = overtime;
	}
	public String getRemain_time() {
		return remain_time;
	}
	public void setRemain_time(String remain_time) {
		this.remain_time = remain_time;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	
	public String getShouldreturn_time() {
		return shouldreturn_time;
	}
	public void setShouldreturn_time(String shouldreturn_time) {
		this.shouldreturn_time = shouldreturn_time;
	}
	
	
	
	
	public BorrowedBook(int id, String b_num, String b_name, String user_num,
			String borrow_time, String shouldreturn_time, String return_time,
			String overtime, String remain_time, String state, String b_img) {
		super();
		this.id = id;
		this.b_num = b_num;
		this.b_name = b_name;
		this.user_num = user_num;
		this.borrow_time = borrow_time;
		this.shouldreturn_time = shouldreturn_time;
		this.return_time = return_time;
		this.overtime = overtime;
		this.remain_time = remain_time;
		this.state = state;
		this.b_img = b_img;
	}
	
	public BorrowedBook() {
		super();
	}
	
	@Override
	public String toString() {
		return "BorrowedBook [id=" + id + ", b_num=" + b_num + ", b_name="
				+ b_name + ", user_num=" + user_num + ", borrow_time="
				+ borrow_time + ", shouldreturn_time=" + shouldreturn_time
				+ ", return_time=" + return_time + ", overtime=" + overtime
				+ ", remain_time=" + remain_time + ", state=" + state
				+ ", b_img=" + b_img + "]";
	}
	
	
	
	
}
