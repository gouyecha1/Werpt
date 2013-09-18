package com.werpt.util;

public class Task {
	
	private int id;
	private String title;
	private String description ;
	private String pic;
	private int werptNum;
	private String endTime;
	private String teams;
	
	public Task() {
		super();
	}
	public Task(int id, String title, String pic, String endTime, String teams) {
		super();
		this.id = id;
		this.title = title;
		this.pic = pic;
		this.endTime = endTime;
		this.teams = teams;
	}
	
	public Task(int id, String title, String description, String pic,
			int werptNum, String endTime) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.pic = pic;
		this.werptNum = werptNum;
		this.endTime = endTime;
	}
	
	public Task(int id, String title, String description, String pic) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.pic = pic;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public int getWerptNum() {
		return werptNum;
	}
	public void setWerptNum(int werptNum) {
		this.werptNum = werptNum;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTeams() {
		return teams;
	}
	public void setTeams(String teams) {
		this.teams = teams;
	}
	
	
	
	
	
}
