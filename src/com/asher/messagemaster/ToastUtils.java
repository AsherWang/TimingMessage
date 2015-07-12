package com.asher.messagemaster;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static boolean DEBUG=true;
    public static void simpleToast(Context context,String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
    
    public static void simpleDebugToast(Context context,String text)
    {
        if(DEBUG)
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
