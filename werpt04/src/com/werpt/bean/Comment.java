package com.werpt.bean;

public class Comment {
	private int id;
	private int wid;
	private String uname;
	private String content;
	private String addTime;
	private long time;

	public Comment(int id, int wid, String uname, String content, String addTime) {
		super();
		this.id = id;
		this.wid = wid;
		this.uname = uname;
		this.content = content;
		this.addTime = addTime;
	}

	public Comment(int id, int wid, String uname, String content, long time) {
		super();
		this.id = id;
		this.wid = wid;
		this.uname = uname;
		this.content = content;
		this.time = time;
	}

	public Comment(int wid, String uname, String content, long time) {
		super();
		this.wid = wid;
		this.uname = uname;
		this.content = content;
		this.time = time;
	}

	public Comment(int wid, String uname, String content, String addTime) {
		super();
		this.wid = wid;
		this.uname = uname;
		this.content = content;
		this.addTime = addTime;
	}

	public Comment(int wid, String uname, String content) {
		super();
		this.wid = wid;
		this.uname = uname;
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWid() {
		return wid;
	}

	public void setWid(int wid) {
		this.wid = wid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
