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
 * �����࣬�����ֶ��У�����id���������ݣ�����ʱ�䣬�����ˣ����͵Ŀ������˫���� �ȵ�
 */
public class Message implements Serializable {
	transient private static final long serialVersionUID = -8978278804052483257L;
	transient final public static String TIMING_MSG_SEND_ACTION="ASHER_TIMING_MSG_SEND_ACTION";
	transient final public static String MSG_SERIALIZE_NAME="msg_send";
	//��ʾ�����õ�sim��
	transient final public static int DEFAULT_SIMCARD=0; //transient��״����������д����
	transient final public static int SIMCARD_1=1;
	transient final public static int SIMCARD_2=2;
	
	transient final public static String TABLE_NAME="messagetask";
	/*
	  �ֶ�����
	 */
	transient final public static String TO_PHONENUMBER="TO_PHONENUMBER";
	transient final public static String MSG_CONTENT="MSG_CONTENT";
	transient final public static String SEND_DATETIME="SEND_DATETIME";
	transient final public static String SEND_CARD="SEND_CARD";
	transient final public static String ACTIVED="ACTIVED";
	transient final public static String IS_SENT="IS_SENT";
	
	public int id; //����id
	public String to_phonnumber;  //������
	public String msg_content;   //��������
	public long send_datetime;  //����ʱ��
	public int send_card;  //�����õĿ�
	public int Actived;
	public int is_sent;
	
	
/**
 * Ĭ������һ����ǰ���͵Ķ�ʱ����
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
	 * ���ݵ绰��������һ����ǰ���͵Ķ�ʱ����
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
		 * ��&�ģ���������һ���µĶ�ʱ��������
		 * @param context ������
		 * @param msg Message��Ϣ
		 * @return ��������޸ĵ��¶��������id
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
			if(msg.id!=-1)//��
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
	 * ɾ������idɾ����������
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
			//Toast.makeText(context, cursor.getCount()+"��", Toast.LENGTH_SHORT).show();
			ArrayList<Message> list=new ArrayList<Message>();
			while(cursor.moveToNext())  //���������ƶ�λ��
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
			//Toast.makeText(context, cursor.getCount()+"��", Toast.LENGTH_SHORT).show();
			ArrayList<Message> list=new ArrayList<Message>();
			while(cursor.moveToNext())  //���������ƶ�λ��
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
			//Toast.makeText(context, cursor.getCount()+"��", Toast.LENGTH_SHORT).show();
			ArrayList<Message> list=new ArrayList<Message>();
			while(cursor.moveToNext())  //���������ƶ�λ��
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
		 * ��������
		 * @param context ������
		 * @param msg_id ����id
		 * @param timestamp ����ִ��ʱ���
		 * @return
		 */
		public static boolean activeMsgTask(Context context,int msg_id,long timestamp)
		{
			long cur_timestamp=DateUtils.getCurrentTimeStamp();
			if((timestamp-cur_timestamp)<0)  //��Ϊ�ػ���ԭ���¶���δ�ܼ�ʱ�����ģ������ٷ���
			{
				Log.i("MSG","��Ч�����޷�����");
				return false;
			}
			
			
			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);			
			Intent intent =new Intent(Message.TIMING_MSG_SEND_ACTION);
			intent.putExtra("msg_id", msg_id);
			PendingIntent pi = PendingIntent.getBroadcast(context,msg_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			Calendar cl = Calendar.getInstance();
			cl.add(Calendar.SECOND,(int) ((timestamp-cur_timestamp)/1000));
			am.set(AlarmManager.RTC_WAKEUP, cl.getTimeInMillis(), pi);
			Log.i("MSG","���񼤻�");
			return true;
		}
		
		public static void cancelMsgTask(Context context,int msg_id)
		{
	        Intent _intent = new Intent(Message.TIMING_MSG_SEND_ACTION); // ��������newһ��Intent��������ֱ������������
	        PendingIntent sender = PendingIntent.getBroadcast(
	                context, msg_id, _intent, PendingIntent.FLAG_CANCEL_CURRENT);
	                AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	                am.cancel(sender);
	                Log.i("MSG","����ȡ��");
		}
		
		public static void initiAllMsgtasks(Context context)
		{
			ASHER_SQLiteOpenHelper asp=new ASHER_SQLiteOpenHelper(context);
			SQLiteDatabase db=asp.getReadableDatabase();
			Cursor cursor=db.query(Message.TABLE_NAME,new String[]{"_id,"+Message.SEND_DATETIME},Message.ACTIVED+"= ? and "+Message.SEND_DATETIME+"> ?",new String[]{"1",DateUtils.getCurrentTimeStamp()+""},"","","_id","");
			while(cursor.moveToNext())  //���������ƶ�λ��
			{
				Log.i("MSG","���񼤻� "+cursor.getInt(0));
				activeMsgTask(context,cursor.getInt(0),cursor.getLong(1));
			}
			cursor.close();
			db.close();
		}
		
}

