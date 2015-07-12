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
     * 获取系统时间 格式为："yyyy-MM-dd HH:mm
     * @return
     */
    public static String getCurrentDate() {
        Date d = new Date();
         sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sf.format(d);
    }
        
    /**
     * 获取系统时间 时间戳
     * @return
     */
    public static long getCurrentTimeStamp() {
    	return  System.currentTimeMillis();
    }
    /**
     * 时间戳转换成字符窜
     * */
    public static String getDateToString(long time) {
        Date d = new Date(time);
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sf.format(d);
    }

    
    
                                      
    /**将字符串yyyy-MM-dd HH:mm转为时间戳*/
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
    
    