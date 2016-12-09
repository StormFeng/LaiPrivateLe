package com.lailem.app.ui.chat.expression;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

import com.lailem.app.R;
import com.lailem.app.utils.TDevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 默认表情
 * 
 * @author leeib
 * 
 */
public class ExpressionUtil {

	/** 每一页表情的个数 */
	private int pageSize = 27;

	private static ExpressionUtil mFaceConversionUtil;

	/** 保存于内存中的表情HashMap */
	private HashMap<String, String> expressionMap = new HashMap<String, String>();

	/** 保存于内存中的表情集合 */
	private List<Expression> expressions = new ArrayList<Expression>();

	/** 表情分页的结果集合 */
	public List<List<Expression>> expressionLists = new ArrayList<List<Expression>>();

	private Context context;

	private ExpressionUtil() {

	}

	public static ExpressionUtil getInstace() {
		if (mFaceConversionUtil == null) {
			mFaceConversionUtil = new ExpressionUtil();
		}
		return mFaceConversionUtil;
	}

	/**
	 * 得到一个SpanableString对象,通过传入的字符串,并进行正则判断
	 * 
	 * @param context
	 * @param str
	 * @return
	 */
	public SpannableString getExpressionString(String str) {
		SpannableString spannableString = new SpannableString(str);
		// 正则表达式比配字符串里是否含有表情,如： 我好[开心]啊
		String zhengze = "\\[[^\\]]+\\]";
		// 通过传入的正则表达式来生成一个pattern
		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(spannableString, sinaPatten, 0);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		}
		return spannableString;
	}

	public void addExpression(String str) {
		SpannableString spannableString = new SpannableString(str);
		// 正则表达式比配字符串里是否含有表情,如： 我好[开心]啊
		String zhengze = "\\[[^\\]]+\\]";
		// 通过传入的正则表达式来生成一个pattern
		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		try {
			dealExpressionInSetDatabase(spannableString, sinaPatten, 0);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		}
	}

	/**
	 * 添加表情
	 * 
	 * @param context
	 * @param imgId
	 * @param spannableString
	 * @return
	 */
	public SpannableString addFace(int imgId, String spannableString) {
		if (TextUtils.isEmpty(spannableString)) {
			return null;
		}
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgId);
		bitmap = Bitmap.createScaledBitmap(bitmap, (int) TDevice.dpToPixel(30), (int) TDevice.dpToPixel(30), true);
		ImageSpan imageSpan = new ImageSpan(context, bitmap);
		SpannableString spannable = new SpannableString(spannableString);
		spannable.setSpan(imageSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**
	 * 对spanableString进行正则判断,如果符合要求,则以表情图片代替
	 * 
	 * @param context
	 * @param spannableString
	 * @param patten
	 * @param start
	 * @throws Exception
	 */
	private void dealExpression(SpannableString spannableString, Pattern patten, int start) throws Exception {
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			// 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
			if (matcher.start() < start) {
				continue;
			}
			String value = expressionMap.get(key);
			if (TextUtils.isEmpty(value)) {
				continue;
			}
			int resId = context.getResources().getIdentifier(value, "drawable", context.getPackageName());
			// 通过上面匹配得到的字符串来生成图片资源id
			// Field field=R.drawable.class.getDeclaredField(value);
			// int resId=Integer.parseInt(field.get(null).toString())s;
			if (resId != 0) {
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
				bitmap = Bitmap.createScaledBitmap(bitmap, (int) TDevice.dpToPixel(20), (int) TDevice.dpToPixel(20), true);

				// 通过图片资源id来得到bitmap,用一个ImageSpan来包装
				ImageSpan imageSpan = new ImageSpan(context, bitmap);
				// 计算该图片名字的长度,也就是要替换的字符串的长度
				int end = matcher.start() + key.length();
				// 将该图片替换字符串中规定的位置中
				spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				if (end < spannableString.length()) {
					// 如果整个字符串还未验证完,则继续。。
					dealExpression(spannableString, patten, end);
				}
				break;
			}
		}
	}

	public void dealExpressionInSetDatabase(SpannableString spannableString, Pattern patten, int start) throws Exception {

		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			// 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
			if (matcher.start() < start) {
				continue;
			}
			String value = expressionMap.get(key);
			if (TextUtils.isEmpty(value)) {
				continue;
			}
			int resId = context.getResources().getIdentifier(value, "drawable", context.getPackageName());
			if (resId != 0) {
				// 计算该图片名字的长度,也就是要替换的字符串的长度

				Expression emojEentry = new Expression();
				emojEentry.setId(resId);
				emojEentry.setCharacter(key);
				emojEentry.setFaceName(value);
				emojEentry.setTime(System.currentTimeMillis());

				int end = matcher.start() + key.length();
				// 将该图片替换字符串中规定的位置中
				if (end < spannableString.length()) {
					// 如果整个字符串还未验证完,则继续。。
					dealExpressionInSetDatabase(spannableString, patten, end);
				}
				break;
			}
		}

	}

	public void getFileText(Context context) {
		this.context = context;
		ParseData(getExpressionFile("expression"));
	}

	/**
	 * 解析字符
	 * 
	 * @param data
	 */
	private void ParseData(List<String> data) {
		if (data == null) {
			return;
		}
		Expression expressionEentry;
		try {
			for (String str : data) {
				// FDDebug.d("ParseData","str::::::"+str);

				String[] text = str.split(",");
				String fileName = text[0].substring(0, text[0].lastIndexOf("."));
				// FDDebug.d("ParseData","fileName::::::"+fileName);
				expressionMap.put(text[1], fileName);
				int resID = context.getResources().getIdentifier(fileName.trim(), "drawable", context.getPackageName());
				// FDDebug.d("ParseData","resID[1]::::::"+resID);
				if (resID != 0) {
					expressionEentry = new Expression();
					expressionEentry.setId(resID);
					expressionEentry.setCharacter(text[1]);
					expressionEentry.setFaceName(fileName);
					expressions.add(expressionEentry);
				}
			}
			int pageCount = (int) Math.ceil(expressions.size() / (float) pageSize);

			for (int i = 0; i < pageCount; i++) {
				expressionLists.add(getData(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取分页数据
	 * 
	 * @param page
	 * @return
	 */
	private List<Expression> getData(int page) {
		int startIndex = page * pageSize;
		int endIndex = startIndex + pageSize;

		if (endIndex > expressions.size()) {
			endIndex = expressions.size();
		}
		// 不这么写,会在viewpager加载中报集合操作异常,我也不知道为什么
		List<Expression> list = new ArrayList<Expression>();
		list.addAll(expressions.subList(startIndex, endIndex));
		if (list.size() < pageSize) {
			for (int i = list.size(); i < pageSize; i++) {
				Expression object = new Expression();
				list.add(object);
			}
		}
		if (list.size() == pageSize) {
			Expression object = new Expression();
			object.setId(R.drawable.chat_delete);
			list.add(object);
		}
		return list;
	}

	/**
	 * 读取表情配置文件
	 * 
	 * @param context
	 * @return
	 */
	public List<String> getExpressionFile(String assertName) {
		try {
			List<String> list = new ArrayList<String>();
			InputStream in = context.getResources().getAssets().open(assertName);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String str = null;
			while ((str = br.readLine()) != null) {
				list.add(str);
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}