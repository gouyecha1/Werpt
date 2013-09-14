package com.werpt.bean;

public class Werpt {
	private int id;//微记编号
	private int uid;//用户编号
	private String title;//标题
	private String content;//内容
	private String thumb;//媒体文件地址
	private String addtime;//发布时间
	private int comments;//评论
	private int favs;//收藏
	private int shares;//分享
	private int status;//审核状态
	private String username;
	private String nickname;

	public Werpt() {
		
	}
	public Werpt(int id, int uid, String title, String content,
			String thumb, String addtime, int comments, int favs, int shares,
			int status, String username) {
		super();
		this.id = id;
		this.uid = uid;
		this.title = title;
		this.content = content;
		this.thumb = thumb;
		this.addtime = addtime;
		this.comments = comments;
		this.favs = favs;
		this.shares = shares;
		this.status = status;
		this.username = username;
	}

	public Werpt(int id, int uid, String title, String content,
			String thumb, String addtime, int comments,
			int status, String username,String nickname) {
		super();
		this.id = id;
		this.uid = uid;
		this.title = title;
		this.content = content;
		this.thumb = thumb;
		this.addtime = addtime;
		this.comments = comments;
		this.status = status;
		this.username = username;
		this.nickname=nickname;
	}


	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Werpt(int id, int uid, String title, String content,
			String thumb, String addtime, int comments, int favs, int shares,
			int status, String username,String nickname) {
		super();
		this.id = id;
		this.uid = uid;
		this.title = title;
		this.content = content;
		this.thumb = thumb;
		this.addtime = addtime;
		this.comments = comments;
		this.favs = favs;
		this.shares = shares;
		this.status = status;
		this.username = username;
		this.nickname=nickname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public int getFavs() {
		return favs;
	}

	public void setFavs(int favs) {
		this.favs = favs;
	}

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	
	
	
}
