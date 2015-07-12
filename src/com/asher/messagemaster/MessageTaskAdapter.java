package com.asher.messagemaster;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.FontAwesomeText;


public class MessageTaskAdapter extends BaseAdapter {
	public boolean isLog=false;
	ArrayList<Message> list;
	private LayoutInflater m_Inflater;
	public MessageTaskAdapter(Context context,ArrayList<Message> m_list)
	{
		m_Inflater=(LayoutInflater)context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		list=m_list;
	}
	public MessageTaskAdapter(Context context,ArrayList<Message> m_list,boolean islog)
	{
		this(context,m_list);
		isLog=islog;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public static int  getWindowWidth(Context context)
	{
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		convertView = m_Inflater.inflate(R.layout.message_task_list_item, null);
		LinearLayout base_content=(LinearLayout) convertView.findViewById(R.id.task_message_item_base_content);
		
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) base_content.getLayoutParams(); // 取控件mGrid当前的布局参数
		linearParams.width = getWindowWidth(m_Inflater.getContext());// 当控件的高强制设成50象素 
		
		//linearParams.width = convertView.getWidth();//
		
		
		base_content.setLayoutParams(linearParams);
		TextView task_message_item_datetime=(TextView) convertView.findViewById(R.id.task_message_item_datetime);
		TextView task_message_item_to_who=(TextView) convertView.findViewById(R.id.task_message_item_to_who);
		TextView task_message_item_cont_brief=(TextView) convertView.findViewById(R.id.task_message_item_cont_brief);
		FontAwesomeText task_message_item_active_toggle=(FontAwesomeText) convertView.findViewById(R.id.task_message_item_active_toggle);
		
		BootstrapButton edit_btn= (BootstrapButton) convertView.findViewById(R.id.task_message_item_edit_btn);
		BootstrapButton del_btn= (BootstrapButton) convertView.findViewById(R.id.task_message_item_del_btn);
		
		
		Message msg=list.get(pos);
		
		task_message_item_datetime.setText(DateUtils.getDateToString(msg.send_datetime));
		task_message_item_to_who.setText(msg.to_phonnumber); //将来换成联系人姓名(如果有的话)
		
		String msg_content_brief=msg.msg_content;
		if(msg_content_brief.length()>12)msg_content_brief=msg_content_brief.substring(0,12);
		task_message_item_cont_brief.setText("部分内容: "+msg_content_brief.replace("\n"," "));
		if(msg.Actived==1)task_message_item_active_toggle.setIcon("fa-check-circle-o");
		else task_message_item_active_toggle.setIcon("fa-circle-o");
		
		
		
		OCL ocl=new OCL();
		ocl.msg_id=msg.id;
		ocl.item_pos=pos;
		edit_btn.setOnClickListener(ocl);
		del_btn.setOnClickListener(ocl);
		base_content.setOnClickListener(ocl);
		task_message_item_active_toggle.setOnClickListener(ocl);
		
		if(isLog){
			task_message_item_active_toggle.setVisibility(View.INVISIBLE);
			LinearLayout.LayoutParams liParams = (LinearLayout.LayoutParams) task_message_item_active_toggle.getLayoutParams(); // 取控件mGrid当前的布局参数
			liParams.width = 20;
			task_message_item_active_toggle.setLayoutParams(liParams);
//			task_message_item_active_toggle.setIcon("");
		}
		return convertView;
	}
	
	class OCL implements OnClickListener{
		public int msg_id;
		public int item_pos;
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int viewId=v.getId();
			
			if(viewId==R.id.task_message_item_active_toggle)
			{
				FontAwesomeText toggle_view=(FontAwesomeText)v;
				Message ttmp_msg=list.get(item_pos);
				if(ttmp_msg.send_datetime<DateUtils.getCurrentTimeStamp())
				{
					ToastUtils.simpleToast(m_Inflater.getContext(), "设定的时间是过去，激活与否无意义！");
					return;
				}

				
				if(ttmp_msg.Actived==1)
				{
					ttmp_msg.Actived=0;
					Message.cancelMsgTask(m_Inflater.getContext(),ttmp_msg.id);
					toggle_view.setIcon("fa-circle-o");
					ToastUtils.simpleToast(m_Inflater.getContext(), "任务取消");
				}
				else
				{
					ttmp_msg.Actived=1;
					Message.activeMsgTask(m_Inflater.getContext(),ttmp_msg.id,ttmp_msg.send_datetime);
					toggle_view.setIcon("fa-check-circle-o");
					ToastUtils.simpleToast(m_Inflater.getContext(), "任务激活！");
				}
				Message.updateMessagetTask(m_Inflater.getContext(), ttmp_msg);
			}
			else if(viewId==R.id.task_message_item_edit_btn||viewId==R.id.task_message_item_base_content) //编辑
			{
				Intent intent=new Intent();
				intent.setClass(m_Inflater.getContext(),TimingMessageSet.class);
				intent.putExtra("msg_id", msg_id);
				m_Inflater.getContext().startActivity(intent);
			}
			else if(viewId==R.id.task_message_item_del_btn)
			{ //删除
				//Log.i("MSG","del +"+msg_id);
				Message.deleteMessagetTask(m_Inflater.getContext(), msg_id);
				list.remove(item_pos);
				MessageTaskAdapter.this.notifyDataSetChanged();  
				ToastUtils.simpleToast(m_Inflater.getContext(), "任务已删除");
			}
		}
		
	}
}
