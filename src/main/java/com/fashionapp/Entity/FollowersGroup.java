package com.fashionapp.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "followers_group")
public class FollowersGroup {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long userId;
//	private long followeruserId;
	private String groupName;
	private String userEmail;
	private boolean isDefault;
	
	
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
	/*public long getFolloweruserId() {
		return followeruserId;
	}
	public void setFolloweruserId(long followeruserId) {
		this.followeruserId = followeruserId;
	}*/
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
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}


}
