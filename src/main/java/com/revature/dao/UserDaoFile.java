package com.revature.dao;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.revature.beans.User;
import com.revature.exceptions.InvalidCredentialsException;

/**
 * Implementation of UserDAO that reads and writes to a file
 */
public class UserDaoFile implements UserDao{
	
	public static String fileLocation = "C:\\Users\\hbi12\\Documents\\Revature\\other\\users.txt";
	
	ArrayList<User> userList = new ArrayList<>();
	
	FileOutputStream fos; 
	ObjectOutputStream oos;
	FileInputStream fis;
	ObjectInputStream ois;

	public User addUser(User user){
		// TODO Auto-generated method stub
		userList.add(user);
		try{
			fos = new FileOutputStream(fileLocation);
			oos = new ObjectOutputStream(fos);
			
			oos.writeObject(userList);
			oos.close();
		}catch(FileNotFoundException e) {
			System.out.println("Users file is missing/in wrong location");
		}catch(IOException e) {
			System.out.println("An exception was thrown: "+e.getMessage());
		}
		
		return null;
	}

	public User getUser(Integer userId) {
		// TODO Auto-generated method stub
		for (User listElem : userList) {
			if (listElem.getId().equals(userId)) {
				return listElem;
			}
		}
		return null;
	}

	public User getUser(String username, String pass) {
		// TODO Auto-generated method stub
		for (User listElem : userList) {
			if ((listElem.getUsername().equals(username)) 
					&& (listElem.getPassword().equals(pass))) {
				return listElem;
			}
		}
		return null;
	}

	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		try {
			fis = new FileInputStream(fileLocation);
			ois = new ObjectInputStream(fis);
			
			do {
				userList = (ArrayList<User>) ois.readObject();
			    System.out.println("userList --" + userList + "\n");
			} while (userList != null);
			
			ois.close();
		} catch (EOFException e) {
			System.out.println("End of file reached.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}

	public User updateUser(User u) {
		// TODO Auto-generated method stub
		if(!userList.contains(u)) {
			throw new InvalidCredentialsException();
		}
		int index = userList.indexOf(u);
		userList.set(index, u);
		
		try{
			fos = new FileOutputStream(fileLocation);
			oos = new ObjectOutputStream(fos);
			
			oos.writeObject(userList);
			oos.close();
		}catch(FileNotFoundException e) {
			System.out.println("Users file is missing/in wrong location");
		}catch(IOException e) {
			System.out.println("An exception was thrown: "+e.getMessage());
		}
		return u;
	}

	public boolean removeUser(User u) {
		// TODO Auto-generated method stub
		boolean boo = false;
		for (User user : userList) {
			if (user.getId() == u.getId()) {
				boo = userList.remove(u);
				
				try{
					fos = new FileOutputStream(fileLocation);
					oos = new ObjectOutputStream(fos);
					
					oos.writeObject(userList);
					oos.close();
				}catch(FileNotFoundException e) {
					System.out.println("Users file is missing/in wrong location");
				}catch(IOException e) {
					System.out.println("An exception was thrown: "+e.getMessage());
				}
				break;
			}
		}
		boo = true;
		return boo;
	}

}
