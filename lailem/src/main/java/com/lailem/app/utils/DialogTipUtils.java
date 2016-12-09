package com.lailem.app.utils;

import android.content.Context;

import com.lailem.app.R;

import java.util.Random;

public class DialogTipUtils {
    
	private static DialogTipUtils instance;
    String[] contents;
    Random random;
	private DialogTipUtils(Context context) {
		contents = context.getResources().getStringArray(R.array.dialog_tip_content); 
		random = new Random();
	}

	public static DialogTipUtils getInstance(Context context) {
		if (instance == null)
			instance = new DialogTipUtils(context);
		return instance;
	}

	public String getContent() {
		return contents[random.nextInt(contents.length)];
	}

}
