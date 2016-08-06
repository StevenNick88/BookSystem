package com.gxu.booksystem.utils;

public class CommonUrl {

    public static final String BASE_URL="http://192.168.43.24:8080/";
//    public static final String BASE_URL="http://192.168.173.1:8080/";
    public static final String SUCCESS="success";
    public static final String FAIL="fail";

    public static final String UPLOAD_FILE=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=upload_file";
    public static final String LOAD_IMG=BASE_URL+"BookSystemServer/upload/";


    //学生登录向服务端发送数据url
    public static final String STU_LOGIN_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=stulogin";
    //管理员登录向服务端发送数据url
    public static final String ADMIN_LOGIN_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=adminlogin";


    //查询student表中单条记录的url
    public static final String STUDENT_URL =BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=stu";
    //查询student表中所有记录的url
    public static final String STUDENTS_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=stus";
    //向服务器添加student表中单条记录的url
    public static final String ADD_STUDENT_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=addstu";
    //向服务器修改student表中单条记录的url
    public static final String UPDATE_STUDENT_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=updatestu";
    //向服务器删除student表中单条记录的url
    public static final String DELETE_STUDENT_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=deletestu";


    //查询admin表中单条记录的url
    public static final String ADMIN_URL =BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=admin";
    //查询admin表中所有记录的url
    public static final String ADMINS_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=admins";
    //向服务器添加admin表中单条记录的url
    public static final String ADD_ADMIN_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=addadmin";
    //向服务器修改admin表中单条记录的url
    public static final String UPDATE_ADMIN_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=updateadmin";
    //向服务器删除admin表中单条记录的url
    public static final String DELETE_ADMIN_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=deleteadmin";


    //根据书号查询book表中单条记录的url
    public static final String BOOK_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=q_book_with_b_num";
    //根据书名查询book表中单条记录的url
    public static final String BOOK_URL_WITH_B_NAME=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=q_book_with_b_name";
    //根据作者查询book表中单条记录的url
    public static final String BOOK_URL_WITH_B_AUTHOR=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=q_book_with_b_author";
    //根据出版社查询book表中单条记录的url
    public static final String BOOK_URL_WITH_B_PRESS=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=q_book_with_b_press";
    //根据书名，作者，出版社查询book表中单条记录的url
    public static final String BOOK_URL_WITH_B_NAME_AUTHOR_PRESS=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=q_book_with_b_name_author_press";


    //查询book表中所有记录的url
    public static final String BOOKS_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=books";
    //向服务器添加book表中单条记录的url
    public static final String ADD_BOOK_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=addbook";
    //向服务器修改book表中单条记录的url
    public static final String UPDATE_BOOK_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=updatebook";
    //向服务器删除book表中单条记录的url
    public static final String DELETE_BOOK_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=deletebook";


    //图书借阅信息表：借书
    public static final String ADD_BORROW_BOOK_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=addborrowbook";
    //查询图书借阅信息表单条记录
    public static final String BORROW_BOOK_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=borrowbook";
    //查询图书借阅信息表所有记录
    public static final String BORROW_BOOKS_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=borrowbooks";
    //查询图书借阅信息表中user_num=? 的数据集合
    public static final String BORROW_BOOK_WITH_USER_NUM_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=borrowbook_with_usernum";
    //查询图书借阅信息表中overtime>0的数据集合
    public static final String BORROW_BOOK_WITH_OVERTIME_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=borrowbook_with_overtime";
    //查询图书借阅信息表中overtime>0 && user_num=? 的数据集合
    public static final String BORROW_BOOK_WITH_OVERTIME_USER_NUM_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=borrowbook_with_overtime_usernum";


    //修改图书借阅信息表单条记录:还书
    public static final String RETURN_BORROW_BOOK_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=returnborrowbook";

    //还书之前查询服务端计算好的overtime和remain_time的值，看是否超期，为还书做准备
    public static final String RETURN_BORROW_BOOK_PRE_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=returnborrowbookpre";

    //修改图书借阅信息表单条记录:续借
    public static final String AGAIN_BORROW_BOOK_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=againborrowbook";



    //图书预约信息表
    public static final String ADD_ORDER_BOOK_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=addorderbook";
    //查询图书预约信息表所有记录
    public static final String ORDER_BOOKS_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=orderbooks";
    //根据用户名查询图书预约信息表所有记录
    public static final String ORDER_BOOK_WITH_USER_NUM_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=orderbookwith_user_num";
    //根据用户名和书号查询图书预约信息表一条记录
    public static final String ORDER_BOOK_WITH_USER_NUM_B_BUM_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=orderbookwith_user_num_b_num";
    //删除图书预约信息表中一条记录：取消预约
    public static final String CANCEL_ORDER_BOOK_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=cancel_orderbook";


    //图书挂失信息表
    public static final String ADD_LOSS_BOOK_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=addlossbook";
    //查询图书挂失信息表所有记录
    public static final String LOSS_BOOKS_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=lossbooks";
    //查询图书挂失信息表单条记录
    public static final String LOSS_BOOK_WITH_USER_NAME_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=lossbookwith_user_num";
    //修改图书挂失信息表单条记录：交纳欠费取消挂失
    public static final String UPDATE_LOSS_BOOK_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=updatelossbook";


    //交纳欠费url:修改boorowedbook表中的state字段和其他字段的相应信息
    public static final String PAY_FREE_URL=BASE_URL+"BookSystemServer/servlet/JsonAction?action_flag=pay_free";


}
