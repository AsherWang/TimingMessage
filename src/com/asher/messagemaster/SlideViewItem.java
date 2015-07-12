package com.asher.messagemaster;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * ���һ����Ĵ��˵���view
 * @author Asher
 *
 */
public class SlideViewItem extends HorizontalScrollView {
	
	private int childWidth=0; //���ӿؼ���һ��LinearLayout
	private int Width=0;
	private int overWidth=0;
	private boolean original=true;
	public SlideViewItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}

	public SlideViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

/**
 * ���ڳ������ֵ��ӿؼ����������1/3���������ȫ����������ȥʱ������1/3���Ͻ���ȫ����
 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(childWidth==0||Width==0)
		{
			childWidth=this.getChildAt(0).getWidth();
			Width=this.getWidth();
			overWidth=childWidth-Width;
			//Log.i("SCR","ĸ  ��"+Width);
			//Log.i("SCR","�ӣ�"+childWidth);
			//Log.i("SCR","�"+overWidth);
		}
		
		if(childWidth<=Width)return super.onTouchEvent(ev);
		int action=ev.getAction();
		int cur_postion=this.getScrollX();
		
		if(action==MotionEvent.ACTION_UP)//̧��
		{
		//	Log.i("SCR","��ǰ:"+this.getScrollX());
			if(cur_postion<overWidth/4)
			{
				//Log.i("SCR","<1/3 <-");
				close();
			}
			else if(cur_postion>3*overWidth/4)
			{
				//Log.i("SCR",">1/3 ->");
				open();
			}
			else
			{
				if(original){//Log.i("SCR","in 2/3 ->");
					open();}
				else{//Log.i("SCR","in 2/3 <-");
					close();}
			}
		}
		return super.onTouchEvent(ev);
	}
	
	/**
	 * �򿪳����Ĳ���
	 */
	private void open()
	{
		this.scrollTo(overWidth, 0);
		original=false;
	}
	
	/**
	 * �رճ����Ĳ���
	 */
	private void close()
	{
		this.scrollTo(0, 0);
		original=true;
	}
}
