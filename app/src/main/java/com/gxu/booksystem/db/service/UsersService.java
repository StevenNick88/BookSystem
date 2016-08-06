package com.gxu.booksystem.db.service;

import java.util.List;
import java.util.Map;

public interface UsersService {

	public boolean addPerson(Object[] params);
	
	public boolean deletePerson(Object[] params);
	
	public boolean updatePerson(Object[] params);
	
	public Map<String,String> getInfoFromUser(String[] selectionArgs);
	
	public Map<String,String> viewPerson(String[] selectionArgs);
	
	public List<Map<String,String>> listPersonMaps(String[] selectionArgs);
}
