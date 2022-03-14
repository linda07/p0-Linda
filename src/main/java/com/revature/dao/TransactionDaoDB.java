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
import com.revature.beans.Transaction;
import com.revature.beans.Transaction.TransactionType;
import com.revature.utils.ConnectionUtil;

public class TransactionDaoDB implements TransactionDao {
	
	private static Connection conn;
	private static Statement stmt;
	private static PreparedStatement pstmt;
	private static ResultSet rs;
	
	public TransactionDaoDB() {
		conn = ConnectionUtil.getConnection();
	}

	public List<Transaction> getAllTransactions() {
		// TODO Auto-generated method stub
		List<Transaction> transactionList = new ArrayList<Transaction>();
		String query = "select * from transactions";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				Transaction transaction = new Transaction();
				transaction.setType(TransactionType.valueOf(rs.getString("transaction_type")));
				transaction.setSender((Account) rs.getObject("from_accountId"));
				if((TransactionType)rs.getObject("transaction_type") == TransactionType.TRANSFER) {
					transaction.setRecipient((Account) rs.getObject("to_accountId"));
				}
				transaction.setTimestamp((LocalDateTime) rs.getObject("timestamp"));
				transaction.setAmount(rs.getDouble("amount"));
				transactionList.add(transaction);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return transactionList;
	}
	public Transaction addTransaction(Transaction t) {
		String query = "insert into transactions (from_accountID,to_accountID,amount,transaction_type,timestamp) values (?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setObject(1, t.getSender().getId());
			if (t.getRecipient() == null) {
				pstmt.setObject(2, null);
			}
			else {
				pstmt.setObject(2, t.getRecipient().getId());
			}
			pstmt.setDouble(3, t.getAmount());
			pstmt.setObject(4, t.getType().toString());
			pstmt.setObject(5, t.getTimestamp().toString());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
}
