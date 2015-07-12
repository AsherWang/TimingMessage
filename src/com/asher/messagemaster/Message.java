package com.asher.messagemaster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 短信类，包含字段有：短信id，短信内容，发送时间，发送人，发送的卡（针对双卡） 等等
 */
public class Message implements Serializable {
	transient private static final long serialVersionUID = -8978278804052483257L;
	transient final public static String TIMING_MSG_SEND_ACTION="ASHER_TIMING_MSG_SEND_ACTION";
	transient final public static String MSG_SERIALIZE_NAME="msg_send";
	//表示发送用的sim卡
	transient final public static int DEFAULT_SIMCARD=0; //transient暂状变量，不会写入流
	transient final public static int SIMCARD_1=1;
	transient final public static int SIMCARD_2=2;
	
	transient final public static String TABLE_NAME="messagetask";
	/*
	  字段名们
	 */
	transient final public static String TO_PHONENUMBER="TO_PHONENUMBER";
	transient final public static String MSG_CONTENT="MSG_CONTENT";
	transient final public static String SEND_DATETIME="SEND_DATETIME";
	transient final public static String SEND_CARD="SEND_CARD";
	transient final public static String ACTIVED="ACTIVED";
	transient final public static String IS_SENT="IS_SENT";
	
	public int id; //任务id
	public String to_phonnumber;  //发送者
	public String msg_content;   //短信内容
	public long send_datetime;  //发送时间
	public int send_card;  //发送用的卡
	public int Actived;
	public int is_sent;
	
	
/**
 * 默认生成一个当前发送的定时短信
 * */
	public Message() 
	{
		id=-1;
		to_phonnumber=null;
		msg_content="";
		send_card=Message.DEFAULT_SIMCARD;
		Actived=1;
		is_sent=0;
		send_datetime=System.currentTimeMillis();
	}
	
	
	/**
	 * 根据电话号码生成一个当前发送的定时短信
	 * */
		public Message(String phone_number) 
		{
			this();
			to_phonnumber=phone_number;
		}
		
		/**
		 * 
		 * @param id
		 * @param phone_number
		 * @param msgcontent
		 * @param datatime
		 * @param card
		 * @param active
		 */
		public Message(int id,String phone_number,String msgcontent,String datatime,int card,int active) 
		{
			this(phone_number);
			this.id=id;
			this.msg_content=msgcontent;
			this.send_card=card;
			this.Actived=active;
		}
		
		
		public Message(Cursor cursor)
		{
		     this.Actived=cursor.getInt(cursor.getColumnIndex(Message.ACTIVED));
		     this.to_phonnumber=cursor.getString(cursor.getColumnIndex(Message.TO_PHONENUMBER));
		     this.msg_content=cursor.getString(cursor.getColumnIndex(Message.MSG_CONTENT));
		     this.send_card=cursor.getInt(cursor.getColumnIndex(Message.SEND_CARD));
		     this.send_datetime=cursor.getLong(cursor.getColumnIndex(Message.SEND_DATETIME));
		     this.id=cursor.getInt(cursor.getColumnIndex("_id"));
		     this.is_sent=cursor.getInt(cursor.getColumnIndex(Message.IS_SENT));
		}
		
		/**
		 * 增&改，插入或更新一个新的定时短信任务
		 * @param context 上下文
		 * @param msg Message消息
		 * @return 插入或者修改的新短信任务的id
		 */
		public static int updateMessagetTask(Context context,Message msg)
		{
			ContentValues v=new ContentValues();
			v.put(Message.TO_PHONENUMBER,msg.to_phonnumber);
			v.put(Message.MSG_CONTENT,msg.msg_content);
			v.put(Message.SEND_DATETIME,msg.send_datetime);
			v.put(Message.ACTIVED,msg.Actived);
			v.put(Message.IS_SENT,msg.is_sent);

			ASHER_SQLiteOpenHelper asp=new ASHER_SQLiteOpenHelper(context);
			SQLiteDatabase db=asp.getWritableDatabase();
			if(msg.id!=-1)//增
			{
				db.update(Message.TABLE_NAME, v,"_id=?",new String[]{""+msg.id});
				return msg.id;
			}
			db.insert(Message.TABLE_NAME,null,v);
			Cursor cursor = db.rawQuery("select last_insert_rowid() from "+Message.TABLE_NAME,null);       
	        int strid=-1;
	        if(cursor.moveToFirst())
	           strid = cursor.getInt(0);
			db.close();
			return strid;
		}
	/**
	 * 删，根据id删除短信任务
	 * @param context
	 * @param msg_id
	 * @return
	 */
		public static int deleteMessagetTask(Context context,int msg_id)
		{
			ASHER_SQLiteOpenHelper asp=new ASHER_SQLiteOpenHelper(context);
			SQLiteDatabase db=asp.getWritableDatabase();
			int re=db.delete(Message.TABLE_NAME,"_id=?",new String[]{""+msg_id});
			db.close();
			return re;
		}	
	
		public static ArrayList<Message> queryAllMessageTask(Context context)
		{
			ASHER_SQLiteOpenHelper asp=new ASHER_SQLiteOpenHelper(context);
			SQLiteDatabase db=asp.getReadableDatabase();
			Cursor cursor=db.query(Message.TABLE_NAME,new String[]{"*"},"",new String[]{},"","","_id","");
			//Toast.makeText(context, cursor.getCount()+"“", Toast.LENGTH_SHORT).show();
			ArrayList<Message> list=new ArrayList<Message>();
			while(cursor.moveToNext())  //不断往下移动位置
			{
			     list.add(new Message(cursor));
			}
			cursor.close();
			db.close();
			return list;
		}
		
		public static ArrayList<Message> queryAllMessageTaskToSend(Context context)
		{
			ASHER_SQLiteOpenHelper asp=new ASHER_SQLiteOpenHelper(context);
			SQLiteDatabase db=asp.getReadableDatabase();
			Cursor cursor=db.query(Message.TABLE_NAME,new String[]{"*"},Message.SEND_DATETIME+">= ?",new String[]{""+DateUtils.getCurrentTimeStamp()},"","","_id","");
			//Toast.makeText(context, cursor.getCount()+"“", Toast.LENGTH_SHORT).show();
			ArrayList<Message> list=new ArrayList<Message>();
			while(cursor.moveToNext())  //不断往下移动位置
			{
			     list.add(new Message(cursor));
			}
			cursor.close();
			db.close();
			return list;
		}
		
		public static ArrayList<Message> queryAllMessageTaskSent(Context context)
		{
			ASHER_SQLiteOpenHelper asp=new ASHER_SQLiteOpenHelper(context);
			SQLiteDatabase db=asp.getReadableDatabase();
			Cursor cursor=db.query(Message.TABLE_NAME,new String[]{"*"},Message.SEND_DATETIME+"< ?",new String[]{""+DateUtils.getCurrentTimeStamp()},"","","_id","");
			//Toast.makeText(context, cursor.getCount()+"“", Toast.LENGTH_SHORT).show();
			ArrayList<Message> list=new ArrayList<Message>();
			while(cursor.moveToNext())  //不断往下移动位置
			{
			     list.add(new Message(cursor));
			}
			cursor.close();
			db.close();
			return list;
		}
		
		
		public static Message getMessageById(Context context,int id)
		{
			ASHER_SQLiteOpenHelper asp=new ASHER_SQLiteOpenHelper(context);
			SQLiteDatabase db=asp.getReadableDatabase();
			Cursor cursor=db.query(Message.TABLE_NAME,new String[]{"*"},"_id=?",new String[]{id+""}, "", "", "");
			Log.i("MSG","pull id "+id);
			Message msg=null;
			 if(cursor.moveToFirst())
			msg=new Message(cursor);
			 Log.i("MSG","pull id "+id+"success "+msg.msg_content);
			db.close();
			return msg;
		}
	
		/**
		 * 激活任务
		 * @param context 上下文
		 * @param msg_id 任务id
		 * @param timestamp 任务执行时间点
		 * @return
		 */
		public static boolean activeMsgTask(Context context,int msg_id,long timestamp)
		{
			long cur_timestamp=DateUtils.getCurrentTimeStamp();
			if((timestamp-cur_timestamp)<0)  //因为关机等原因导致短信未能及时发出的，将不再发送
			{
				Log.i("MSG","无效任务无法激活");
				return false;
			}
			
			
			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);			
			Intent intent =new Intent(Message.TIMING_MSG_SEND_ACTION);
			intent.putExtra("msg_id", msg_id);
			PendingIntent pi = PendingIntent.getBroadcast(context,msg_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			Calendar cl = Calendar.getInstance();
			cl.add(Calendar.SECOND,(int) ((timestamp-cur_timestamp)/1000));
			am.set(AlarmManager.RTC_WAKEUP, cl.getTimeInMillis(), pi);
			Log.i("MSG","任务激活");
			return true;
		}
		
		public static void cancelMsgTask(Context context,int msg_id)
		{
	        Intent _intent = new Intent(Message.TIMING_MSG_SEND_ACTION); // 必须重新new一个Intent，而不能直接用下面这行
	        PendingIntent sender = PendingIntent.getBroadcast(
	                context, msg_id, _intent, PendingIntent.FLAG_CANCEL_CURRENT);
	                AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	                am.cancel(sender);
	                Log.i("MSG","任务取消");
		}
		
		public static void initiAllMsgtasks(Context context)
		{
			ASHER_SQLiteOpenHelper asp=new ASHER_SQLiteOpenHelper(context);
			SQLiteDatabase db=asp.getReadableDatabase();
			Cursor cursor=db.query(Message.TABLE_NAME,new String[]{"_id,"+Message.SEND_DATETIME},Message.ACTIVED+"= ? and "+Message.SEND_DATETIME+"> ?",new String[]{"1",DateUtils.getCurrentTimeStamp()+""},"","","_id","");
			while(cursor.moveToNext())  //不断往下移动位置
			{
				Log.i("MSG","任务激活 "+cursor.getInt(0));
				activeMsgTask(context,cursor.getInt(0),cursor.getLong(1));
			}
			cursor.close();
			db.close();
		}
		
}

