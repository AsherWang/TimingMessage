package com.asher.messagemaster;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
public class MessageMatserReciver extends BroadcastReceiver {	
	
	public void onReceive(Context context, Intent intent) {
		if(Message.TIMING_MSG_SEND_ACTION.equals(intent.getAction()))
		{
			//Message msg=(Message)intent.getExtras().getSerializable(Message.MSG_SERIALIZE_NAME);
			int msg_id=intent.getIntExtra("msg_id", -1);
			Log.i("MSG","recive id"+msg_id);
			if(msg_id!=-1)
			{
				SendMsg(Message.getMessageById(context, msg_id));
			}
			
		}else {
			
			
		}
	}
	
	/**
	 * 当前是使用默认的sim卡发送短信
	 * @param msg
	 */
	private void SendMsg(Message msg)
	{
		if(msg==null)return;
        SmsManager smsManager = SmsManager.getDefault();
        if(msg.msg_content.length() > 70) {
            List<String> contents = smsManager.divideMessage(msg.msg_content);
            for(String sub_content : contents) {
                smsManager.sendTextMessage(msg.to_phonnumber, null, sub_content, null, null);
            }
        } else {
         smsManager.sendTextMessage(msg.to_phonnumber, null, msg.msg_content, null, null);
        }
	}
	
	
	public void SendDemoMsg()
	{
		Message msg=new Message();
		msg.to_phonnumber="13146021274";
		SendMsg(msg);
	}
}
