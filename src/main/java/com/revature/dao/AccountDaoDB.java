package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.Account.AccountType;
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
				//a.setType((AccountType) rs.getObject("account_type"));
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
				//a.setType((AccountType) rs.getObject("account_type"));
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
				//a.setId(rs.getInt("id"));
				a.setId(rs.getInt("account_id"));
				a.setOwnerId(rs.getInt("owner_id"));
				a.setBalance(rs.getDouble("balance"));
				//a.setType((AccountType) rs.getObject("account_type"));
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
		String query = "update account set owner_id=?, balance=?, account_type=?, approved=? where account_id=?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, a.getOwnerId());
			pstmt.setDouble(2, a.getBalance());
			pstmt.setObject(3, a.getType().toString());
			pstmt.setBoolean(4, a.isApproved());
			//pstmt.setObject(5, UserType.CUSTOMER);
			//pstmt.setString(5, UserType.CUSTOMER.toString());
			pstmt.setInt(5, a.getId());
			pstmt.executeUpdate();
			//stmt.executeUpdate(query);
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
			
			//stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}

}
