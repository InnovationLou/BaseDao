package demo.me.entity;

import demo.me.annotation.Bean;
import demo.me.annotation.Column;
import demo.me.annotation.Id;

@Bean("user")
public class User {
	@Id("userId")
	private int userId;
	@Column("userName")
	private String userName;
	@Column("userPwd")
	private String userPwd;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", userPwd=" + userPwd + "]";
	}
	/**
	 * @param userId
	 * @param userName
	 * @param userPwd
	 */
	public User(int userId, String userName, String userPwd) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userPwd = userPwd;
	}
	/**
	 * 
	 */
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
