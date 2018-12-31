package com.fashionapp.Entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


 /*table is to auto map the hashtags to video*/

@Entity
@Table(name = "hashtag_video_map")
public class HashtagVideoMap implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long videoId;
	private long tagId;
	private boolean isMapped;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getVideoId() {
		return videoId;
	}
	public void setVideoId(long videoId) {
		this.videoId = videoId;
	}
	public long getTagId() {
		return tagId;
	}
	public void setTagId(long tagId) {
		this.tagId = tagId;
	}
	public boolean isMapped() {
		return isMapped;
	}
	public void setMapped(boolean isMapped) {
		this.isMapped = isMapped;
	}
	
	
	
	 

}
