package com.asher.messagemaster;



import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimingMessageSetSendTime extends Activity implements OnClickListener {
	DatePicker dp;
	TimePicker tp;
	TextView tv;
	Button ok_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timing_message_set_send_time);
		InitView();
		InitValues();
	}
	private void InitValues() {
		// TODO Ao-generated method stub
	//根据TimingMessageSet.edit_msg.send_datetime的值来初始化两个控件

		TimeUpdate(TimingMessageSet.edit_msg.send_datetime);
	}
	
	private void TimeUpdate(long timestamp)
	{
		String timetr=DateUtils.getDateToString(timestamp);
		dp.updateDate(intparse(timetr.substring(0,4)), intparse(timetr.substring(5,7))-1, intparse(timetr.substring(8,10)));
		tp.setCurrentHour(intparse(timetr.substring(11,13)));
		tp.setCurrentMinute(intparse(timetr.substring(14,16)));
	}
	

	int intparse(String str)
	{
		return Integer.parseInt(str);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ActionBar actb=this.getActionBar();
		actb.setTitle("设定发送时间");
	}
	
	private void InitView() {
		// TODO Auto-generated method stub
		dp=(DatePicker) findViewById(R.id.datePicker);
		tp=(TimePicker) findViewById(R.id.timePicker);
		tv=(TextView) findViewById(R.id.time_show);
		ok_btn=(Button) findViewById(R.id.time_ok_btn);
		ok_btn.setOnClickListener(this);
		tp.setIs24HourView(true);
		
	}
	
	

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.time_ok_btn)
			{
				String timeStr=dp.getYear()+"-"+(dp.getMonth()+1)+"-"+dp.getDayOfMonth()+" "+tp.getCurrentHour()+":"+tp.getCurrentMinute();
				tv.setText(timeStr);
				Log.i("MSG",timeStr);
				TimingMessageSet.edit_msg.send_datetime=DateUtils.getStringToDate(timeStr);
				TimingMessageSet.edit_msg_send_time_setted=true;
				this.finish();
			}
		}

	
}
