package com.fashionapp.Entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



 /*
  * This object is to block the user/data by the ADMIN 
  * 
  * */

@Entity
@Table(name = "blocked_data")
public class BlockedData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private long id;
	private long userId;
	private String userName;
	private long fileId;
	private long adminId;
 	private String reason;
	
	@Column( columnDefinition = "tinyint(1) default 3")
	@Enumerated(value = EnumType.ORDINAL)
	private Type type;
	private boolean isActive;
	
	@Column(name = "blocked_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date blocked_time;
	
	@PrePersist
	protected void onCreate() {
		blocked_time = new Date();
	}
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getBlocked_time() {
		return blocked_time;
	}

	public void setBlocked_time(Date blocked_time) {
		this.blocked_time = blocked_time;
	}

	 

	 
}
