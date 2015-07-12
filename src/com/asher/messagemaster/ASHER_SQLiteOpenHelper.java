package com.asher.messagemaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ASHER_SQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "asher_messagemaster.db";
    private static final int DATABASE_VERSION = 2;

    public ASHER_SQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//建表,不大信任啊....
		
		  db.execSQL("CREATE TABLE "+Message.TABLE_NAME+" (" +
				  "_id INTEGER PRIMARY KEY," +
                  Message.SEND_CARD+" INTEGER, " +
                  Message.TO_PHONENUMBER+" TEXT, " +
                  Message.ACTIVED+" INTEGER, " +
                  Message.IS_SENT+" INTEGER, " +
                  Message.MSG_CONTENT+" TEXT, " +
                  Message.SEND_DATETIME+" TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		 db.execSQL("DROP TABLE IF EXISTS "+Message.TABLE_NAME);
         onCreate(db);
	}

}
