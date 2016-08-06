package com.gxu.booksystem.utils;

import com.gxu.booksystem.db.domain.Admin;
import com.gxu.booksystem.db.domain.Book;
import com.gxu.booksystem.db.domain.BorrowedBook;
import com.gxu.booksystem.db.domain.LossBook;
import com.gxu.booksystem.db.domain.OrderedBook;
import com.gxu.booksystem.db.domain.Student;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 根据业务的需求数据类型
 * 完成对json数据的解析
 *
 * @author jack
 *
 */
public class JsonTools {

    public JsonTools() {
        // TODO Auto-generated constructor stub
    }

    public static Student getStudent(String key, String jsonString) {
        Student student = new Student();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject userObject = jsonObject.getJSONObject(key);
            student.setS_id(userObject.getInt("s_id"));
            student.setS_num(userObject.getString("s_num"));
            student.setS_name(userObject.getString("s_name"));
            student.setS_age(userObject.getString("s_age"));
            student.setS_sex(userObject.getString("s_sex"));
            student.setS_department(userObject.getString("s_department"));
            student.setS_pwd(userObject.getString("s_pwd"));
            student.setS_permitborrowtime(userObject.getString("s_permitborrowtime"));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return student;
    }

    public static List<Student> getStudents(String key, String jsonString) {
        List<Student> list = new ArrayList<Student>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            // 返回json的数组
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Student student = new Student();
                student.setS_id(jsonObject2.getInt("s_id"));
                student.setS_num(jsonObject2.getString("s_num"));
                student.setS_name(jsonObject2.getString("s_name"));
                student.setS_age(jsonObject2.getString("s_age"));
                student.setS_sex(jsonObject2.getString("s_sex"));
                student.setS_department(jsonObject2.getString("s_department"));
                student.setS_pwd(jsonObject2.getString("s_pwd"));
                student.setS_permitborrowtime(jsonObject2.getString("s_permitborrowtime"));
                list.add(student);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }

/*
*   private String m_num;
    private String m_permitted;
    private String m_pwd;*/
    public static Admin getAdmin(String key, String jsonString) {
        Admin admin = new Admin();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject adminObject = jsonObject.getJSONObject(key);
            admin.setM_num(adminObject.getString("m_num"));
            admin.setM_pwd(adminObject.getString("m_pwd"));
            admin.setM_permitborrowtime(adminObject.getString("m_permitborrowtime"));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return admin;
    }

    public static List<Admin> getAdmins(String key, String jsonString) {
        List<Admin> list = new ArrayList<Admin>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            // 返回json的数组
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Admin admin = new Admin();
                admin.setM_num(jsonObject2.getString("m_num"));
                admin.setM_pwd(jsonObject2.getString("m_pwd"));
                admin.setM_permitborrowtime(jsonObject2.getString("m_permitborrowtime"));
                list.add(admin);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }

    public static Book getBook(String key, String jsonString) {
        Book book = new Book();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject bookObject = jsonObject.getJSONObject(key);
            book.setId(bookObject.getInt("id"));
            book.setB_num(bookObject.getString("b_num"));
            book.setB_name(bookObject.getString("b_name"));
            book.setB_author(bookObject.getString("b_author"));
            book.setB_press(bookObject.getString("b_press"));
            book.setB_buytime(bookObject.getString("b_buytime"));
            book.setIntroduction(bookObject.getString("introduction"));
            book.setCount(bookObject.getString("count"));
            book.setB_img(bookObject.getString("b_img"));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return book;
    }


    public static BorrowedBook getBorrowedBook(String key, String jsonString) {
        BorrowedBook borrowedBook = new BorrowedBook();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject bookObject = jsonObject.getJSONObject(key);
            borrowedBook.setId(bookObject.getInt("id"));
            borrowedBook.setB_num(bookObject.getString("b_num"));
            borrowedBook.setB_name(bookObject.getString("b_name"));
            borrowedBook.setUser_num(bookObject.getString("user_num"));
            borrowedBook.setBorrow_time(bookObject.getString("borrow_time"));
            borrowedBook.setReturn_time(bookObject.getString("return_time"));
            borrowedBook.setOvertime(bookObject.getString("overtime"));
            borrowedBook.setRemain_time(bookObject.getString("remain_time"));
            borrowedBook.setState(bookObject.getString("state"));
            borrowedBook.setShouldreturn_time(bookObject.getString("shouldreturn_time"));
            borrowedBook.setB_img(bookObject.getString("b_img"));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return borrowedBook;
    }


    public static List<BorrowedBook> getBorrowedBooks(String key, String jsonString) {
        List<BorrowedBook> list = new ArrayList<BorrowedBook>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            // 返回json的数组
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                BorrowedBook borrowedBook = new BorrowedBook();
                borrowedBook.setId(jsonObject2.getInt("id"));
                borrowedBook.setB_num(jsonObject2.getString("b_num"));
                borrowedBook.setB_name(jsonObject2.getString("b_name"));
                borrowedBook.setUser_num(jsonObject2.getString("user_num"));
                borrowedBook.setBorrow_time(jsonObject2.getString("borrow_time"));
                borrowedBook.setReturn_time(jsonObject2.getString("return_time"));
                borrowedBook.setOvertime(jsonObject2.getString("overtime"));
                borrowedBook.setRemain_time(jsonObject2.getString("remain_time"));
                borrowedBook.setState(jsonObject2.getString("state"));
                borrowedBook.setShouldreturn_time(jsonObject2.getString("shouldreturn_time"));
                borrowedBook.setB_img(jsonObject2.getString("b_img"));
                list.add(borrowedBook);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }


    public static List<OrderedBook> getOrderBooks(String key, String jsonString) {
        List<OrderedBook> list = new ArrayList<OrderedBook>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            // 返回json的数组
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                OrderedBook orderedBook = new OrderedBook();
                orderedBook.setId(jsonObject2.getInt("id"));
                orderedBook.setB_num(jsonObject2.getString("b_num"));
                orderedBook.setB_name(jsonObject2.getString("b_name"));
                orderedBook.setUser_num(jsonObject2.getString("user_num"));
                orderedBook.setState(jsonObject2.getString("state"));
                orderedBook.setB_img(jsonObject2.getString("b_img"));
                list.add(orderedBook);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return list;
    }


    public static OrderedBook getOrderBook(String key, String jsonString) {
        OrderedBook orderedBook = new OrderedBook();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject bookObject = jsonObject.getJSONObject(key);
            orderedBook.setId(bookObject.getInt("id"));
            orderedBook.setB_num(bookObject.getString("b_num"));
            orderedBook.setB_name(bookObject.getString("b_name"));
            orderedBook.setUser_num(bookObject.getString("user_num"));
            orderedBook.setState(bookObject.getString("state"));
            orderedBook.setB_img(bookObject.getString("b_img"));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return orderedBook;
    }



    public static List<LossBook> getLossBooks(String key, String jsonString) {
        List<LossBook> list = new ArrayList<LossBook>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            // 返回json的数组
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                LossBook lossBook = new LossBook();
                lossBook.setId(jsonObject2.getInt("id"));
                lossBook.setB_num(jsonObject2.getString("b_num"));
                lossBook.setB_name(jsonObject2.getString("b_name"));
                lossBook.setUser_num(jsonObject2.getString("user_num"));
                lossBook.setState(jsonObject2.getString("state"));
                lossBook.setB_img(jsonObject2.getString("b_img"));
                list.add(lossBook);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }

    public static List<Book> getBooks(String key, String jsonString) {
        List<Book> list = new ArrayList<Book>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            // 返回json的数组
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Book book = new Book();
                book.setId(jsonObject2.getInt("id"));
                book.setB_num(jsonObject2.getString("b_num"));
                book.setB_name(jsonObject2.getString("b_name"));
                book.setB_author(jsonObject2.getString("b_author"));
                book.setB_press(jsonObject2.getString("b_press"));
                book.setB_buytime(jsonObject2.getString("b_buytime"));
                book.setIntroduction(jsonObject2.getString("introduction"));
                book.setCount(jsonObject2.getString("count"));
                book.setB_img(jsonObject2.getString("b_img"));
                list.add(book);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }

    public static List<String> getList(String key, String jsonString) {
        List<String> list = new ArrayList<String>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                String msg = jsonArray.getString(i);
                list.add(msg);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }


    //将json数据转换成map
    public static Map<String, Object> getMaps(String key,String jsonString) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject jsonObject2 = jsonObject.getJSONObject(key);
            Iterator<String> iterator = jsonObject2.keys();
            while (iterator.hasNext()) {
                String json_key = iterator.next();
                Object json_value = jsonObject2.get(json_key);
                if (json_value == null) {
                    json_value = "";
                }
                map.put(json_key, json_value);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return map;
    }

    public static List<Map<String, Object>> listKeyMaps(String key,
                                                        String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                Iterator<String> iterator = jsonObject2.keys();
                while (iterator.hasNext()) {
                    String json_key = iterator.next();
                    Object json_value = jsonObject2.get(json_key);
                    if (json_value == null) {
                        json_value = "";
                    }
                    map.put(json_key, json_value);
                }
                list.add(map);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }
}
