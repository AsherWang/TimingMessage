package com.asher.messagemaster;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MessageTaskSent extends Activity {
	ListView task_sent_list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.timing_message_task_sent);
		initViews();
		initValues();
			
	}

	private void initValues() {
		// TODO Auto-generated method stub
		task_sent_list.setAdapter(new MessageTaskAdapter(this,Message.queryAllMessageTaskSent(this),true));
		
	}

	private void initViews() {
		// TODO Auto-generated method stub
		task_sent_list=(ListView) findViewById(R.id.message_task_sent_list);
		
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		ActionBar actb=this.getActionBar();
		actb.setTitle("¼ÇÂ¼");
	}
	
	
}
