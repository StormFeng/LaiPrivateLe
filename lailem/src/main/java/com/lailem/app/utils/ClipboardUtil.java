package com.lailem.app.utils;

import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardUtil {
    public static void copyText(Context context,String text){
    	ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(text);
    }
}
