package com.asher.messagemaster;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	TextView send_message_btn;
	TextView message_log_btn;
	ListView main_meun_list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initListener();
		
		
		
		//激活计时器任务
		Message.initiAllMsgtasks(this);
		
		
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		ActionBar actb=this.getActionBar();
		actb.setTitle("MessageMaster");
	}

	class CL implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(arg0.getId()==R.id.send_message_btn)
			{
				Intent i0=new Intent();
				i0.setClass(MainActivity.this, TimingMessageTaskListActivity.class);
				startActivity(i0);
			}
			else if(arg0.getId()==R.id.message_log_btn)
			{
				Intent i0=new Intent();
				i0.setClass(MainActivity.this, MessageTaskSent.class);
				startActivity(i0);
			}
		}
		
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		message_log_btn=(TextView) findViewById(R.id.message_log_btn);
		send_message_btn=(TextView) findViewById(R.id.send_message_btn);
	//	main_meun_list=(ListView) findViewById(R.id.main_meun_list);
		//SimpleAdapter as=new SimpleAdapter();
		
		
		//main_meun_list.setAdapter(adapter);
		
		
		CL cl=new CL();
		send_message_btn.setOnClickListener(cl);
		message_log_btn.setOnClickListener(cl);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
