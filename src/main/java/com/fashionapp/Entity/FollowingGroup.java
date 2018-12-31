package com.fashionapp.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "following_group")
public class FollowingGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long userId;
	//private long followinguserId;
	private String groupName;
	private String userEmail;
//	private String followiguseremail;
	private boolean isActive;

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
//
//	public long getFollowinguserId() {
//		return followinguserId;
//	}
//
//	public void setFollowinguserId(long followinguserId) {
//		this.followinguserId = followinguserId;
//	}

	public String getGroupname() {
		return groupName;
	}

	public void setGroupname(String groupname) {
		this.groupName = groupname;
	}

	public String getUseremail() {
		return userEmail;
	}

	public void setUseremail(String useremail) {
		this.userEmail = useremail;
	}

//	public String getFollowiguseremail() {
//		return followiguseremail;
//	}
//
//	public void setFollowiguseremail(String followiguseremail) {
//		this.followiguseremail = followiguseremail;
//	}

	public boolean isDefault() {
		return isActive;
	}

	public void setDefault(boolean isDefault) {
		this.isActive = isDefault;
	}

}
