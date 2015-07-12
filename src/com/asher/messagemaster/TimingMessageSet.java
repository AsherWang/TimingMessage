package com.asher.messagemaster;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.FontAwesomeText;

public class TimingMessageSet extends Activity implements OnTouchListener {
	
	TextView edit_msg_to_who; //短信接受者
	TextView edit_msg_send_time; //短信发送时间
	BootstrapEditText edit_msg_content; //短信内容
	
	BootstrapButton ok_btn;
	FontAwesomeText edit_msg_to_who_btn;
	FontAwesomeText edit_msg_send_time_btn;
	//Button edit_msg_content_btn;
	
	
	private int edit_msg_id=-1;
	public static Message edit_msg;
	public static boolean  edit_msg_to_who_setted=false;
	public static boolean  edit_msg_send_time_setted=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timing_message_set);
		
		initViews();
		
		//是否是编辑模式
		edit_msg_id=this.getIntent().getIntExtra("msg_id", -1);
		if(edit_msg_id!=-1) 
		{//数据库中读取数据
			TimingMessageSet.edit_msg=Message.getMessageById(this, edit_msg_id);
			edit_msg_send_time_setted=true;
			edit_msg_to_who_setted=true;
		}else{
			TimingMessageSet.edit_msg=new Message();
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int act=event.getAction();
		if(act==MotionEvent.ACTION_UP)
		{
			v.setBackgroundColor(Color.rgb(255, 255, 255));
			v.performClick();
		}
		else
		{
			v.setBackgroundColor(Color.rgb(225,225,225));
		}
		return true;
	}
	
	
	  @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initValues();
		ActionBar actb=this.getActionBar();
		actb.setTitle("编辑定时短信");
	}




	private void initValues() {
		// TODO Auto-generated method stub
		if(edit_msg_send_time_setted)
		{
			edit_msg_send_time.setText("发送时间:"+DateUtils.getDateToString(TimingMessageSet.edit_msg.send_datetime));
		}
		edit_msg_content.setText(TimingMessageSet.edit_msg.msg_content);
		if(edit_msg_to_who_setted)
		{
			//如果查到了联系人，则把联系人名字显示出来.
			edit_msg_to_who.setText("发送给:"+TimingMessageSet.edit_msg.to_phonnumber);
		}
		
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TimingMessageSet.edit_msg=null;
		edit_msg_to_who_setted=false;
		edit_msg_send_time_setted=false;
	}




	private void initViews() {
		// TODO Auto-generated method stub
		  
		  edit_msg_to_who=(TextView)findViewById(R.id.edit_msg_to_who);
		  edit_msg_send_time=(TextView)findViewById(R.id.edit_msg_send_time);
		  edit_msg_content=(BootstrapEditText)findViewById(R.id.edit_msg_content);
		  edit_msg_content.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				TimingMessageSet.edit_msg.msg_content=edit_msg_content.getText().toString();
			}
			  
		  });
		  
		  
		  
		  
		  ok_btn=(BootstrapButton)findViewById(R.id.ok_btn);
		  edit_msg_to_who_btn=(FontAwesomeText)findViewById(R.id.edit_msg_to_who_btn);
		  edit_msg_send_time_btn=(FontAwesomeText)findViewById(R.id.edit_msg_send_time_btn);
		 // edit_msg_content_btn=(Button)findViewById(R.id.edit_msg_content_btn);


		  
		  
		  
		  OCL ocl=new OCL();
		  edit_msg_to_who_btn.setOnClickListener(ocl);
		  edit_msg_send_time_btn.setOnClickListener(ocl);
		 // edit_msg_content_btn.setOnClickListener(ocl);
		  ok_btn.setOnClickListener(ocl);  
		  LinearLayout l=(LinearLayout) findViewById(R.id.edit_msg_send_time_layout);
		  l.setOnTouchListener(this);
		  l.setOnClickListener(ocl);
		  
		  l=(LinearLayout) findViewById(R.id.edit_msg_to_whoe_layout);
		  l.setOnTouchListener(this);
		  l.setOnClickListener(ocl);
		  
	}
	
	
    class OCL implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.ok_btn:
				if(!(edit_msg_to_who_setted&&edit_msg_send_time_setted))return;
				//如果是进行任务的编辑，那么edit_msg_id的值不是-1
				
				//任务更新
				TimingMessageSet.edit_msg.msg_content=edit_msg_content.getText().toString();
				TimingMessageSet.edit_msg.id=edit_msg_id;
				TimingMessageSet.edit_msg.id=Message.updateMessagetTask(TimingMessageSet.this,TimingMessageSet.edit_msg);
				Log.i("MSG","新任务添加");
				
				//任务编辑不处理是否激活
				if(edit_msg_id==-1)
					Message.activeMsgTask(TimingMessageSet.this, TimingMessageSet.edit_msg.id,TimingMessageSet.edit_msg.send_datetime);
				TimingMessageSet.this.finish();
				break;
			case R.id.edit_msg_send_time_btn:
			case R.id.edit_msg_send_time_layout:
				Intent i1=new Intent();
				i1.setClass(TimingMessageSet.this, TimingMessageSetSendTime.class);
				startActivity(i1);
				break;
			case R.id.edit_msg_to_who_btn:
			case R.id.edit_msg_to_whoe_layout:
				Intent i2=new Intent();
				i2.setClass(TimingMessageSet.this, TimingMessageSetToWho.class);
				startActivity(i2);
				break;
			}
		}}
    
	
}
