package com.gxu.booksystem.db.test;

import java.util.List;
import java.util.Map;

import android.test.AndroidTestCase;
import android.util.Log;

import com.gxu.booksystem.db.DbOpenHelper;
import com.gxu.booksystem.db.dao.UsersDao;
import com.gxu.booksystem.db.service.UsersService;

public class Test extends AndroidTestCase {

	public Test() {
		// TODO Auto-generated constructor stub
	}
	
	public void createDb(){
		DbOpenHelper helper = new DbOpenHelper(getContext());
		helper.getReadableDatabase();
	}

	public void insertDB(){
		UsersService service = new UsersDao(getContext());
        Object[] params = {"王五","qaz456","学生"};
		boolean flag = service.addPerson(params);
		System.out.println("--->>"+flag);
	}
	
	public void deleteDB(){
		UsersService service = new UsersDao(getContext());
		Object[] params = {1};
		boolean flag = service.deletePerson(params);
		System.out.println("--->>"+flag);
	}
	
	public void updateDB(){
		UsersService service = new UsersDao(getContext());
        Object[] params = {"shangsan","45561","学生","1"};
		service.updatePerson(params);
	}
	
	public void viewDB(){
		UsersService service = new UsersDao(getContext());
		String[] selectionArgs = {"3"};
		Map<String, String> map = service.viewPerson(selectionArgs);
		Log.i("Test", "-->>"+map.toString());
	}
	
	public void listDB(){
		UsersService service = new UsersDao(getContext());
		
		List<Map<String,String>> list = service.listPersonMaps(null);
		Log.i("Test", "-->>"+list.toString());
	}
}
