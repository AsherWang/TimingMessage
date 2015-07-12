package com.asher.messagemaster;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TimingMessageSetToWho extends Activity implements OnClickListener {
	TextView to_who_name;
	EditText to_who_number;
	Button to_who_ok_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timing_message_set_to_who);
		InitViews();
		InitValues();
		
	}

	private void InitValues() {
		// TODO Auto-generated method stub
		if(TimingMessageSet.edit_msg_to_who_setted)
		{
			
			to_who_number.setText(TimingMessageSet.edit_msg.to_phonnumber);
			//����鵽����ϵ�ˣ������ϵ��������ʾ����..
			Log.i("MSG","��ָ�룿");
			
		}
	}

	private void InitViews() {
		// TODO Auto-generated method stub
		to_who_name=(TextView) findViewById(R.id.to_who_name);
		to_who_number=(EditText) findViewById(R.id.to_who_number);
		to_who_ok_btn=(Button) findViewById(R.id.to_who_ok_btn);
		
		to_who_ok_btn.setOnClickListener(this);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ActionBar actb=this.getActionBar();
		actb.setTitle("���͸�");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.to_who_ok_btn)
		{
			TimingMessageSet.edit_msg.to_phonnumber=to_who_number.getText().toString();
			//����鵽����ϵ�ˣ������ϵ��������ʾ����..
			
			TimingMessageSet.edit_msg_to_who_setted=true;
			this.finish();
		}
	}
	
}
