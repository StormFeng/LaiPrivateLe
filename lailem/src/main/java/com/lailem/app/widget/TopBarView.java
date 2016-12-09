package com.lailem.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lailem.app.R;

/**
 * 通用的标题栏 Created by XuYang on 15/4/12.
 */
public class TopBarView extends RelativeLayout {

	private TextView title_tv;// 标题文字
	private TextView left_tv;// 左侧文字
	private TextView right_tv;// 右侧文字
	private ImageButton left_ib;// 左侧按钮
	private ImageButton right_ib;// 右侧按钮
	private ProgressBar progressBar;// 中间加载进度条
	private ImageView line_iv;// 下划线
	private TextView tag_tv;

	private TextView back_tv;// 返回键文字
	View rootView;

	public TopBarView(Context context) {
		super(context);
		init(context);
	}

	public TopBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rootView = inflater.inflate(R.layout.view_topbar, this);
		title_tv = (TextView) findViewById(R.id.title_tv);
		left_tv = (TextView) findViewById(R.id.left_tv);
		right_tv = (TextView) findViewById(R.id.right_tv);
		left_ib = (ImageButton) findViewById(R.id.left_ib);
		right_ib = (ImageButton) findViewById(R.id.right_ib);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		line_iv = (ImageView) findViewById(R.id.line);
		tag_tv = (TextView) findViewById(R.id.tag);

		back_tv = (TextView) findViewById(R.id.back_tv);
	}

	public TopBarView setTitle(String title) {
		title_tv.setText(title);
		return this;
	}

	public TopBarView setTitle(int resId) {
		title_tv.setText(resId);
		return this;
	}

	public TopBarView setLeftText(String text, OnClickListener onclickListener) {
		left_tv.setText(text);
		left_tv.setOnClickListener(onclickListener);
		left_tv.setVisibility(VISIBLE);
		return this;
	}

	public TopBarView setBackText(String text, OnClickListener onClickListener) {
		back_tv.setText(text);
		back_tv.setOnClickListener(onClickListener);
		back_tv.setVisibility(VISIBLE);
		return this;
	}

	public TopBarView setBack(String text, int resId, OnClickListener onClickListener) {
		back_tv.setText(text);
		back_tv.setOnClickListener(onClickListener);
		back_tv.setVisibility(VISIBLE);

		left_ib.setImageResource(resId);
		left_ib.setOnClickListener(onClickListener);
		left_ib.setVisibility(VISIBLE);
		return this;
	}

	public TopBarView setLeftText(int resId, OnClickListener onclickListener) {
		left_tv.setText(resId);
		left_tv.setOnClickListener(onclickListener);
		left_tv.setVisibility(VISIBLE);
		return this;
	}

	public TopBarView setRightText(String text, OnClickListener onclickListener) {
		right_tv.setText(text);
		right_tv.setOnClickListener(onclickListener);
		right_tv.setVisibility(VISIBLE);
		return this;
	}

	public TopBarView setRightText(String text) {
		right_tv.setText(text);
		right_tv.setVisibility(VISIBLE);
		return this;
	}

	public TopBarView setRightText(int resId, OnClickListener onclickListener) {
		right_tv.setText(resId);
		right_tv.setOnClickListener(onclickListener);
		right_tv.setVisibility(VISIBLE);
		return this;
	}

	public TopBarView setRightText(int resId) {
		right_tv.setText(resId);
		right_tv.setVisibility(VISIBLE);
		return this;
	}

	public TopBarView setBackText(int resId, OnClickListener onClickListener) {
		back_tv.setText(resId);
		back_tv.setOnClickListener(onClickListener);
		back_tv.setVisibility(VISIBLE);
		return this;
	}

	public TopBarView setLeftImageButton(int resId, OnClickListener onclickListener) {
		left_ib.setImageResource(resId);
		left_ib.setOnClickListener(onclickListener);
		left_ib.setVisibility(VISIBLE);
		return this;
	}

	public TopBarView setRightImageButton(int resId, OnClickListener onclickListener) {
		right_ib.setImageResource(resId);
		right_ib.setOnClickListener(onclickListener);
		right_ib.setVisibility(VISIBLE);
		return this;
	}

	public TopBarView toggleProgressBar(boolean isShow) {
		if (isShow) {
			progressBar.setVisibility(VISIBLE);
		} else {
			progressBar.setVisibility(GONE);
		}
		return this;
	}

	public TopBarView recovery() {
		title_tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		title_tv.setOnClickListener(null);
		left_tv.setVisibility(GONE);
		right_tv.setVisibility(GONE);
		left_ib.setVisibility(GONE);
		right_ib.setVisibility(GONE);
		return this;
	}

	public TopBarView setBgColor(int color){
		rootView.findViewById(R.id.rl_titlebar).setBackgroundColor(color);
		return this;
	}
	
	/**
	 * 显示标题中的进度条
	 */
	public void showProgressBar() {
		progressBar.setVisibility(VISIBLE);
	}

	/**
	 * 隐藏标题中的进度条
	 */
	public void hideProgressBar() {
		progressBar.setVisibility(GONE);
	}

	public TextView getTitle_tv() {
		return title_tv;
	}

	public TextView getLeft_tv() {
		return left_tv;
	}

	public TextView getRight_tv() {
		return right_tv;
	}

	public ImageButton getLeft_ib() {
		return left_ib;
	}

	public ImageButton getRight_ib() {
		return right_ib;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public ImageView getLine_iv() {
		return line_iv;
	}

	public TopBarView hideRight_ib() {
		right_ib.setVisibility(View.GONE);
		return this;
	}

	public TopBarView hideRight_tv() {
		right_tv.setVisibility(View.GONE);
		return this;
	}

	public TopBarView showRight_ib() {
		right_ib.setVisibility(View.VISIBLE);
		return this;
	}

	public TopBarView showRight_tv() {
		right_tv.setVisibility(View.VISIBLE);
		return this;
	}

	public TextView getTag_tv() {
		return tag_tv;
	}

	public void showTag_iv() {
		tag_tv.setVisibility(VISIBLE);
	}

}
