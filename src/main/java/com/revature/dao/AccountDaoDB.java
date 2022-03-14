package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.Account.AccountType;
import com.revature.beans.Transaction;
import com.revature.beans.Transaction.TransactionType;
import com.revature.beans.User.UserType;
import com.revature.beans.User;
import com.revature.utils.ConnectionUtil;

/**
 * Implementation of AccountDAO which reads/writes to a database
 */
public class AccountDaoDB implements AccountDao {
	
	private static Connection conn;
	private static Statement stmt;
	private static PreparedStatement pstmt;
	private static ResultSet rs;
	
	public AccountDaoDB() {
		conn = ConnectionUtil.getConnection();
	}

	public Account addAccount(Account a) {
		// TODO Auto-generated method stub
		String query = "insert into account (owner_id,balance,account_type,approved) values (?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, a.getOwnerId());
			pstmt.setDouble(2, a.getBalance());
			pstmt.setObject(3, a.getType().toString());
			pstmt.setBoolean(4, a.isApproved());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	public Account getAccount(Integer actId) {
		// TODO Auto-generated method stub
		String query = "select * from account where account_id="+actId.intValue();
		Account a = new Account();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				a.setId(rs.getInt("account_id"));
				a.setOwnerId(rs.getInt("owner_id"));
				a.setBalance(rs.getDouble("balance"));
				a.setType(AccountType.valueOf(rs.getString("account_type")));
				a.setApproved(rs.getBoolean("approved"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	public List<Account> getAccounts() {
		// TODO Auto-generated method stub
		String query = "select * from account";
		List<Account> accountList = new ArrayList<Account>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				Account a = new Account();
				a.setId(rs.getInt("account_id"));
				a.setOwnerId(rs.getInt("owner_id"));
				a.setBalance(rs.getDouble("balance"));
				a.setType(AccountType.valueOf(rs.getString("account_type")));
				a.setApproved(rs.getBoolean("approved"));
				accountList.add(a);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accountList;
	}

	public List<Account> getAccountsByUser(User u) {
		// TODO Auto-generated method stub
		String query = "select * from account where owner_id="+u.getId();
		List<Account> accountList = new ArrayList<Account>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				Account a = new Account();
				a.setId(rs.getInt("account_id"));
				a.setOwnerId(rs.getInt("owner_id"));
				a.setBalance(rs.getDouble("balance"));
				a.setType(AccountType.valueOf(rs.getString("account_type")));
				a.setApproved(rs.getBoolean("approved"));
				accountList.add(a);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accountList;
	}

	public Account updateAccount(Account a) {
		// TODO Auto-generated method stub
		String query = "update account set owner_id=?, balance=?, account_type=?, approved=?, transaction = ? where account_id=?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, a.getOwnerId());
			pstmt.setDouble(2, a.getBalance());
			pstmt.setObject(3, a.getType().toString());
			pstmt.setBoolean(4, a.isApproved());
			
			List<Transaction>  transaction = a.getTransactions();
			if (transaction == null)
			{
				pstmt.setObject(5, null);				
			}
			else
			{
				Transaction transactionLast = new Transaction();;
				if (transaction != null && !transaction.isEmpty()) 
				{
					  transactionLast = transaction.get(transaction.size()-1);
				}
				Integer fromAccountId = transactionLast.getSender().getId();
				Integer toAccountId;
				
				if (transactionLast.getRecipient() == null)
			    	toAccountId = null;
				else
					toAccountId = transactionLast.getRecipient().getId();
				
				Double amount = transactionLast.getAmount();
			    TransactionType type = transactionLast.getType();
			    LocalDateTime timestamp =  transactionLast.getTimestamp();
				 
			    String jsonText = String.format ("{\"from_accountId\": %d,\"to_accountId\": %d,\"amount\": %f,\"transaction_type\": \"%s\",\"timestamp\": \"%s\"}", fromAccountId, toAccountId,amount, type, timestamp);
				
				System.out.println("jsonText -- " + jsonText); 
				
				pstmt.setObject(5, jsonText);
			}
			
			pstmt.setInt(6, a.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	public boolean removeAccount(Account a) {
		// TODO Auto-generated method stub
		String query = "delete from account where account_id="+a.getId();
		boolean status = false;
		try {
			stmt = conn.createStatement();
			status = stmt.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}
}
