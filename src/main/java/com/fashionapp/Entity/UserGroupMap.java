package com.fashionapp.Entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="usergroup_map")
public class UserGroupMap implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private long userId;
	private long groupId;
	private String userEmail;
	private long followinguserId;
	private boolean isMapped;
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
	public long getGroupId() {
		return groupId;
	}
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public long getFollowinguserId() {
		return followinguserId;
	}
	public void setFollowinguserId(long followinguserId) {
		this.followinguserId = followinguserId;
	}
	public boolean isMapped() {
		return isMapped;
	}
	public void setMapped(boolean isMapped) {
		this.isMapped = isMapped;
	}
	
	 

}
