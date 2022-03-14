package com.revature.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.Transaction;
import com.revature.beans.Transaction.TransactionType;
import com.revature.beans.User;
import com.revature.dao.AccountDao;
import com.revature.dao.TransactionDao;
import com.revature.dao.TransactionDaoDB;
import com.revature.exceptions.OverdraftException;
import com.revature.exceptions.UnauthorizedException;

/**
 * This class should contain the business logic for performing operations on Accounts
 */
public class AccountService {
	
	public AccountDao actDao;
	public static final double STARTING_BALANCE = 25d;
	List<Transaction> tranList = new ArrayList<Transaction>();
	TransactionDao tranDao = new TransactionDaoDB();
	
	public AccountService(AccountDao dao) {
		this.actDao = dao;
	}
	
	/**
	 * Withdraws funds from the specified account
	 * @throws OverdraftException if amount is greater than the account balance
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void withdraw(Account a, Double amount) {
		if (amount > a.getBalance()) {
			throw new OverdraftException();
		}
		else if (amount < 0) {
			throw new UnsupportedOperationException();
		}
		else {
			a.setBalance(a.getBalance() - amount);

			Transaction transaction = new Transaction(a,null,amount,TransactionType.WITHDRAWAL,LocalDateTime.now());
			if (a.getTransactions() != null) {
				for (Transaction tran:a.getTransactions()) {
				tranList.add(tran);
				}
			}
			tranList.add(transaction);
			tranDao.addTransaction(transaction);
			a.setTransactions(tranList);
		}
	}
	
	/**
	 * Deposit funds to an account
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void deposit(Account a, Double amount) {
		if (amount < 0) {
			throw new UnsupportedOperationException();
		}
		else {
			a.setBalance(a.getBalance() + amount);
			
			Transaction transaction = new Transaction(a,null,amount,TransactionType.DEPOSIT,LocalDateTime.now());
			if (a.getTransactions() != null) {
				for (Transaction tran:a.getTransactions()) {
				tranList.add(tran);
				}
			}
			tranList.add(transaction);
			tranDao.addTransaction(transaction);
			a.setTransactions(tranList);
		}
	}
	
	/**
	 * Transfers funds between accounts
	 * @throws UnsupportedOperationException if amount is negative or 
	 * the transaction would result in a negative balance for either account
	 * or if either account is not approved
	 * @param fromAct the account to withdraw from
	 * @param toAct the account to deposit to
	 * @param amount the monetary value to transfer
	 */
	public void transfer(Account fromAct, Account toAct, double amount) {
		if (amount < 0 || fromAct.getBalance() - amount < 0 
				|| !fromAct.isApproved() || !toAct.isApproved()) {
			throw new UnsupportedOperationException();
		}
		else {
			fromAct.setBalance(fromAct.getBalance() - amount);
			toAct.setBalance(toAct.getBalance() + amount);

			Transaction transaction = new Transaction(fromAct,toAct,amount,TransactionType.TRANSFER,LocalDateTime.now());
			
			if (fromAct.getTransactions() != null) {
				for (Transaction tran:fromAct.getTransactions()) {
				tranList.add(tran);
				}
			}
			tranList.add(transaction);
			fromAct.setTransactions(tranList);
			
			List<Transaction> tranList2 = new ArrayList<Transaction>();
			if (toAct.getTransactions() != null) {
				for (Transaction tran:toAct.getTransactions()) {
				tranList2.add(tran);
				}
			}
			tranList2.add(transaction);
			tranDao.addTransaction(transaction);
			toAct.setTransactions(tranList2);
		}
	}
	
	/**
	 * Creates a new account for a given User
	 * @return the Account object that was created
	 */
	public Account createNewAccount(User u) {
		Account newAcc = new Account(null, u.getId(), null, null, false, null);
		return newAcc;
	}
	
	/**
	 * Approve or reject an account.
	 * @param a
	 * @param approval
	 * @throws UnauthorizedException if logged in user is not an Employee
	 * @return true if account is approved, or false if unapproved
	 */
	public boolean approveOrRejectAccount(Account a, boolean approval) {
		boolean apprRes = false;
		if (a.getOwnerId() == 0) { 				
			throw new UnauthorizedException();
		}
		else {
			if (approval == true) {
				a.setApproved(approval);
				apprRes = true;
			}
			else if (approval == false) {
				a.setApproved(approval);
			}
		}
		return apprRes;
	}
	
	public List<Account> getAccounts(User u) {
		List<Account> accountList = new ArrayList<Account>();
		accountList = u.getAccounts();
		return accountList;
	}
}
