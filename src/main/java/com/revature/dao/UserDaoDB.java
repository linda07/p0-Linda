package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.User;
import com.revature.beans.User.UserType;
import com.revature.utils.ConnectionUtil;

/**
 * Implementation of UserDAO that reads/writes to a relational database
 */
public class UserDaoDB implements UserDao {
	
	private static Connection conn;
	private static Statement stmt;
	private static PreparedStatement pstmt;
	private static ResultSet rs;
	
	public UserDaoDB () {
		conn = ConnectionUtil.getConnection();
	}

	public User addUser(User user) {
		// TODO Auto-generated method stub
		String query = "insert into p0_user (first_name,last_name,username,password,user_type) values (?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, user.getFirstName());
			pstmt.setString(2, user.getLastName());
			pstmt.setString(3, user.getUsername());
			pstmt.setString(4, user.getPassword());
			pstmt.setString(5, user.getUserType().toString());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	public User getUser(Integer userId) {
		// TODO Auto-generated method stub
		String query = "select * from p0_user where id="+userId.intValue();
		User user = new User();
		try {
			stmt= conn.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				user.setId(rs.getInt("id"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setUserType(UserType.valueOf(rs.getString("user_type")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	public User getUser(String username, String pass) {
		// TODO Auto-generated method stub
		String query = "select * from p0_user where username=? and password=?";
		User user = new User();
		try {
			pstmt= conn.prepareStatement(query);
			pstmt.setString(1, username);
			pstmt.setString(2, pass);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user.setId(rs.getInt("id"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setUserType(UserType.valueOf(rs.getString("user_type")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}

	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		String query = "select * from p0_user";
		List<User> userList = new ArrayList<User>();
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				User u = new User();
				u.setId(rs.getInt("id"));
				u.setFirstName(rs.getString("first_name"));
				u.setLastName(rs.getString("last_name"));
				u.setUsername(rs.getString("username"));
				u.setPassword(rs.getString("password"));
				u.setUserType(UserType.valueOf(rs.getString("user_type")));
				userList.add(u);
			}
			userList.forEach(System.out::println);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return userList;
	}

	public User updateUser(User u) {
		// TODO Auto-generated method stub
		String query = "update p0_user set first_name=?, last_name=?, username=?, password=?, user_type=? where id=?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, u.getFirstName());
			pstmt.setString(2, u.getLastName());
			pstmt.setString(3, u.getUsername());
			pstmt.setString(4, u.getPassword());
			pstmt.setString(5, u.getUserType().toString());
			pstmt.setInt(6, u.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u;
	}

	public boolean removeUser(User u) {
		// TODO Auto-generated method stub
		String query = "delete from p0_user where id="+u.getId();
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
