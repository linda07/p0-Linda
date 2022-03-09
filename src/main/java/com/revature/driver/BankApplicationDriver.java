package com.revature.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.revature.beans.*;
import com.revature.beans.Account.AccountType;
import com.revature.beans.User.UserType;
import com.revature.dao.*;
import com.revature.services.*;
import com.revature.utils.SessionCache;

/**
 * This is the entry point to the application
 */
public class BankApplicationDriver {

	public static void main(String[] args) {
		// your code here...
		int choice = 0;
		int id = 0;
		String fn = null;
		String ln = null;
		String un = null;
		String pw = null;
		
		//User use = new User();
		Scanner scan = new Scanner(System.in);
	    //UserDao userDao = new UserDaoFile();
		UserDao userDao = new UserDaoDB();
	    //AccountDao accDao = new AccountDaoFile();
		AccountDao accDao = new AccountDaoDB();
	    UserService userServ = new UserService(userDao, accDao);
	    AccountService accServ = new AccountService(accDao);
	    TransactionDao tranDao = new TransactionDaoDB();
	    
	    while (choice < 6) {
	    	System.out.println("Welcome to the Bank!"
				+ "\n1. Register"
				+ "\n2. Login"
				+ "\n3. View Customers"
				+ "\n4. Remove Customer"
				+ "\n5. Update Customer"
				+ "\n6. Exit");
	    	System.out.print("Enter your choice [1-6]: ");
	    	choice = scan.nextInt();
	    	
	    	switch (choice) {
	    	case 1:
	    		//id = userDao.getAllUsers().size();
	    		System.out.print("Enter first name: ");
	    		fn = scan.next();
	    		System.out.print("Enter last name: ");
	    		ln = scan.next();
	    		System.out.print("Enter username: ");
	    		un = scan.next();
	    		System.out.print("Enter password: ");
	    		pw = scan.next();
	    		
	    		User user1 = new User(id++, un, pw, fn, ln, UserType.CUSTOMER);
	    		userServ.register(user1);
	    		break;
	    	case 2:
	    		System.out.print("Enter username: ");
	    		un = scan.next();
	    		System.out.print("Enter password: ");
	    		pw = scan.next();
	    		User user2 = userDao.getUser(un, pw);
	    		if (user2 == null) {
	    			System.out.println("User " + un + " is not found or Password " + pw + " does not match.");
	    		}
	    		else {
	    			//userServ.login(un, pw);
	    			System.out.println("Login successful.");
	    			SessionCache.setCurrentUser(user2);
	    			
	    			int id2 = 0;
	    			int option = 0;
	    			int accType = 0;
	    			int accountId = 0;
	    			double startBal = 0.0;
	    			double amount = 0.0;
	    			Account acc = new Account();
	    			List<Account> accList = new ArrayList<Account>();
	    			//User loggedUser = userServ.login(un, pw);
	    			
	    			System.out.println("logged user" + user2);
	    			while(option <= 6) {
	    				System.out.println("1. Apply account"
		    					+ "\n2. Deposit"
		    					+ "\n3. Withdraw"
		    					+ "\n4. Transfer"
		    					+ "\n5. Approve or Reject"
		    					+ "\n6. Logout");
	    				System.out.print("Enter your option [1-6]: ");
		    			option = scan.nextInt();
	    				switch (option) {
						case 1:
							System.out.print("select an account type [1.Checking, 2.Saving]: ");
							accType = scan.nextInt();
							System.out.print("Enter starting balance: " );
							startBal = scan.nextDouble();
							//Account acc = new Account();
							acc.setBalance(startBal);
							acc.setOwnerId(user2.getId());
							acc.setType(accType == 1 ? AccountType.CHECKING : AccountType.SAVINGS);
							acc.setId(id++);
							//List<Account> accList = new ArrayList<Account>();
							accList.add(acc);
							user2.setAccounts(accList);
							accServ.createNewAccount(user2);
							accDao.addAccount(acc);
							break;
						case 2:
							System.out.println("Available accounts for this user");
							accDao.getAccountsByUser(user2).forEach(System.out::println);
							//List<Account> accList = new ArrayList<Account>();
							//accList = accDao.getAccountsByUser(user2);
							System.out.print("Enter account ID to Deposit: ");
							accountId = 0;
							accountId = scan.nextInt();
							System.out.print("Enter the amount to deposit: ");
							amount = 0.0;
							amount = scan.nextDouble();
							acc = accDao.getAccount(accountId);
							accServ.deposit(acc, amount);
							accDao.updateAccount(acc);
							break;
						case 3:
							System.out.println("Available accounts for this user");
							accDao.getAccountsByUser(user2).forEach(System.out::println);
							System.out.print("Enter account ID to Withdraw: ");
							accountId = 0;
							accountId = scan.nextInt();
							System.out.print("Enter the amount to Withdraw: ");
							amount = 0.0;
							amount = scan.nextDouble();
							acc = accDao.getAccount(accountId);
							accServ.withdraw(acc, amount);
							accDao.updateAccount(acc);
							break;
						case 4:
							System.out.println("Available accounts for this user");
							accDao.getAccountsByUser(user2).forEach(System.out::println);
							System.out.print("Enter account ID to transfer FROM: ");
							int tFrom = 0;
							tFrom = scan.nextInt();
							System.out.print("Enter account ID to transfer TO: ");
							int tTo = 0;
							tTo = scan.nextInt();
							System.out.print("Enter the amount to Withdraw: ");
							amount = 0.0;
							amount = scan.nextDouble();
							Account accFrom = new Account();
							Account accTo = new Account();
							accFrom = accDao.getAccount(tFrom);
							accTo = accDao.getAccount(tTo);
							accServ.transfer(accFrom, accTo, amount);
							accDao.updateAccount(accFrom);
							accDao.updateAccount(accTo);
							//tranDao.getAllTransactions();
							break;
						case 5:
							//if (user2.getUserType() == UserType.EMPLOYEE) {
							System.out.println("Available accounts for this user");
							//accDao.getAccountsByUser(user2).forEach(System.out::println);
							accDao.getAccounts();
							System.out.print("Enter account ID to approve or reject: ");
							accountId = 0;
							accountId = scan.nextInt();
							System.out.print("Select an option [true for approve, false for reject]: ");
							boolean optionAR = false;
							optionAR = scan.nextBoolean();
							acc = accDao.getAccount(accountId);
							accServ.approveOrRejectAccount(acc, optionAR);
							accDao.updateAccount(acc);
							//}
							//else {
							//	System.out.println("Only employees can approve or reject transactions.");
							//}
							
							break;
						case 6:
							System.out.print("Do you want to Logout? (1.Yes/2.No) :");
							int logout = 0;
							logout = scan.nextInt();
							if (logout == 1) {
								SessionCache.setCurrentUser(null);
								System.out.println("Thank you for using the Bank!");
								System.exit(0);
							}
							break;
						default:
							System.out.print("Enter a number between 1 to 6");
							break;
						}
	    			}
	    		}
	    		//userServ.login(un, pw);
	    		break;
	    	case 3:
	    		userDao.getAllUsers();
	    		//userDao.getAllUsers().forEach(System.out::println);
	    		break;
	    	case 4:
	    		System.out.print("Enter username: ");
	    		un = scan.next();
	    		System.out.print("Enter password: ");
	    		pw = scan.next();
	    		User user4 = userDao.getUser(un, pw);
	    		
	    		if (user4 != null) {
	    			userDao.removeUser(user4);
	    		}
	    		else {
	    			System.out.println("User could not be found, thus cannot be deleted.\n");
	    		}
	    		break;
	    	case 5:
	    		System.out.print("Enter username: ");
	    		un = scan.next();
	    		System.out.print("Enter password: ");
	    		pw = scan.next();
	    		User user5 = userDao.getUser(un, pw);
	    		if (user5 != null) {
	    			System.out.print("Enter or Update first name: ");
		    		fn = scan.next();
		    		System.out.print("Enter or Update last name: ");
		    		ln = scan.next();
		    		System.out.print("Enter or Update username: ");
		    		un = scan.next();
		    		System.out.print("Enter or Update password: ");
		    		pw = scan.next();
		    		
		    		user5.setFirstName(fn);
		    		user5.setLastName(ln);
		    		user5.setUsername(un);
		    		user5.setPassword(pw);
	    			userDao.updateUser(user5);
	    		}
	    		else {
	    			System.out.println("User could not be found, thus cannot be updated.\n");
	    		}
	    		
	    		break;
	    	case 6:
	    		System.out.println("Thank you for using the Bank!");
	    		System.exit(0);
	    		break;
	    	default:
	    		break;
	    	}
	    }
	    scan.close();
	}
}
