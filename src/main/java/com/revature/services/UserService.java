package com.revature.services;

import java.util.List;

import com.revature.beans.User;
import com.revature.dao.AccountDao;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoFile;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.UsernameAlreadyExistsException;

/**
 * This class should contain the business logic for performing operations on users
 */
public class UserService {
	UserDao userdao;
	AccountDao accountDao;
	
	public UserService(UserDao udao, AccountDao adao) {
		this.userdao = udao;
		this.accountDao = adao;
	}
	
	/**
	 * Validates the username and password, and return the User object for that user
	 * @throws InvalidCredentialsException if either username is not found or password does not match
	 * @return the User who is now logged in
	 */
	public User login(String username, String password) {
		if (userdao.getAllUsers().contains(userdao.getUser(username, password))) {
			return (User) userdao;
		}
		else {
			throw new InvalidCredentialsException();
		}
	}
	
	/**
	 * Creates the specified User in the persistence layer
	 * @param newUser the User to register
	 * @throws UsernameAlreadyExistsException if the given User's username is taken
	 */
	public void register(User newUser) {
		//if (userDao.getAllUsers().contains(newUser)) {
//		if ((userdao.getAllUsers()).indexOf(newUser) == -1) { // currently prints out contents of file, make sure to not do that
//			userdao.addUser(newUser);
//		}
//		else {
//			throw new UsernameAlreadyExistsException();
//		}
		userdao.addUser(newUser);
	}
}
