package com.revature.dao;

import java.util.LinkedList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.User;
import com.revature.exceptions.InvalidCredentialsException;

/**
 * Implementation of AccountDAO which reads/writes to files
 */
public class AccountDaoFile implements AccountDao {
	// use this file location to persist the data to
	public static String fileLocation = "";
	
	List<Account> accList = new LinkedList<>();

	public Account addAccount(Account a) {
		// TODO Auto-generated method stub
		accList.add(a);
		return a;
	}

	public Account getAccount(Integer actId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Account> getAccounts() {
		// TODO Auto-generated method stub
		return accList;
	}

	public List<Account> getAccountsByUser(User u) {
		// TODO Auto-generated method stub
		return u.getAccounts();
	}

	public Account updateAccount(Account a) {
		// TODO Auto-generated method stub
		if(!accList.contains(a)) {
			throw new InvalidCredentialsException();
		}
		int index = accList.indexOf(a);
		accList.set(index, a);
		return null;
	}

	public boolean removeAccount(Account a) {
		// TODO Auto-generated method stub
		boolean boo = false;
		if(!accList.contains(a)) {
			throw new InvalidCredentialsException();
		}
		accList.remove(a);
		boo = true;
		return boo;
	}

}
