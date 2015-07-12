package com.asher.messagemaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class TimingMessageTaskListActivity extends Activity {
	ListView task_list;
	BootstrapButton msg_task_add;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timing_message_task);
		initi();
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		task_list.setAdapter(new MessageTaskAdapter(this,Message.queryAllMessageTaskToSend(this)));
		Log.i("MSG","列表已更新");
		getActionBar().setTitle("定时短信列表");
		super.onResume();
	}


	class OCL implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.msg_task_add)
			{
				Intent i0=new Intent();
				i0.setClass(TimingMessageTaskListActivity.this, TimingMessageSet.class);
				startActivity(i0);
			}
		}
		
	}
	
	
	/**
	 * 绑定按钮实践，初始化列表
	 */
	private void initi() {
		// TODO Auto-generated method stub
		msg_task_add=(BootstrapButton) findViewById(R.id.msg_task_add);
		OCL ocl=new OCL();
	//	msg_task_list_edit.setOnClickListener(ocl);
		msg_task_add.setOnClickListener(ocl);
		task_list=(ListView) findViewById(R.id.message_task_list);
	}
}
