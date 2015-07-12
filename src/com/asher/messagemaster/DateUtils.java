package com.asher.messagemaster;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
                                      
/* 
 * @author Msquirrel
 */
public class DateUtils {
                                          
    private static SimpleDateFormat sf = null;
    
    /**
     * ��ȡϵͳʱ�� ��ʽΪ��"yyyy-MM-dd HH:mm
     * @return
     */
    public static String getCurrentDate() {
        Date d = new Date();
         sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sf.format(d);
    }
        
    /**
     * ��ȡϵͳʱ�� ʱ���
     * @return
     */
    public static long getCurrentTimeStamp() {
    	return  System.currentTimeMillis();
    }
    /**
     * ʱ���ת�����ַ���
     * */
    public static String getDateToString(long time) {
        Date d = new Date(time);
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sf.format(d);
    }

    
    
                                      
    /**���ַ���yyyy-MM-dd HH:mmתΪʱ���*/
    public static long getStringToDate(String time) {
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try{
            date = sf.parse(time);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }
    
    
    
}
    
    