package com.werpt.costant;

import java.io.File;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class Adress {
	public static String sdCard = Environment.getExternalStorageDirectory().toString();
	public static final String VIDEOPATH = sdCard + File.separator + "customVideo";//使用微记摄像 之后的  存储地址
	public static final String PICPATH = sdCard + File.separator + "customPicture";//使用微记摄像 之后的照片的  存储地址
	
	public static Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;//手机中视频的Uri
	public static Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;//手机中图片的Uri
	public static String videoOrder = MediaStore.Video.Media.DATE_ADDED;
	public static String imageOrder = MediaStore.Images.Media.DATE_ADDED;//按照时间  进行降序(最新的在最后)
	public static String[] videoPath = new String[]{ MediaStore.Video.Media.DATA};//视频的地址
	public static String[] imagePath = new String[]{ MediaStore.Images.Media.DATA};//图片的地址
	
	public static String IP = "222.92.117.23";
	
	public static String GETTASKTITLE = "http://" + IP + ":8080/WerptFiles/upload/task_getTitle";//获得悬赏的标题
	public static String GETTASKSIMPLEINFO = "http://" + IP + ":8080/WerptFiles/upload/task_getTaskSimpleInfo";//获得悬赏的简单信息
	public static String GETTASKALLINFO = "http://" + IP + ":8080/WerptFiles/upload/task_getTaskAllInfo";//获得悬赏的详细信息
	public static String GETTASKCOUNTS = "http://" + IP + ":8080/WerptFiles/upload/task_getAllCounts";//获得悬赏的总个数
	public static String UPLOADURL = "http://" + IP + ":8080/WerptFiles/upload/uploadfile_upload";//上传文件
	public static String GETLASTTASK = "http://" + IP + ":8080/WerptFiles/upload/task_getLastTask";//获得悬赏的详细信息
	
	public static String WEIJISIMPLEINFO = "http://"+ IP +":8080/WerptFiles/upload/werpt_getSimpleInfo";//微记简单信息
	public static String WEIJIALLINFO = "http://"+ IP +":8080/WerptFiles/upload/werpt_getAllInfo";//微记详细信息
	public static String WEIJICOUNT = "http://"+ IP +":8080/WerptFiles/upload/werpt_getCount";//微记条数
	public static String WEIJIUSERALL = "http://"+ IP +":8080/WerptFiles/upload/werpt_getUserAllWerpt";//获得对用用户的所有微记
	public static String WEIJIUSERCOUNT = "http://"+ IP +":8080/WerptFiles/upload/werpt_getUserCount";//获得对用用户的所有微记的条数
	
	public static String GETWEIJICOMMENT = "http://"+ IP +":8080/WerptFiles/upload/werpt_getAllComments";//获得微记的评论
	public static String INSERTCOMMENT = "http://"+ IP +":8080/WerptFiles/upload/werpt_insertComment";//添加评论
	
	public static String REGIST = "http://"+ IP +":8080/WerptFiles/upload/user_regist";//用户注册
	public static String LOGIN = "http://"+ IP +":8080/WerptFiles/upload/user_login";//用户登录
	
	public static String WEIJIIMAGE = "http://werpt.szxuanhao.com"; //微记图片的保存地址
}
