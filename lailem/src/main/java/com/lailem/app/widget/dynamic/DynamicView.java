package com.lailem.app.widget.dynamic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.lailem.app.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DynamicView extends LinearLayout {
	@Bind(R.id.text)
	View text;
	@Bind(R.id.images)
	View images;
	@Bind(R.id.voice)
	View voice;
	@Bind(R.id.video)
	View video;
	@Bind(R.id.map)
	View map;
	@Bind(R.id.vote)
	View vote;

	private ArrayList<View> views = new ArrayList<View>();

	public DynamicView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DynamicView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.view_common_dynamic, this);
		ButterKnife.bind(this);
		views.add(text);
		views.add(images);
		views.add(voice);
		views.add(video);
		views.add(map);
		views.add(vote);
	}

	public void resort(int textOrder, int imagesOrder, int voiceOrder, int videoOrder, int mapOrder, int voteOrder) {
		if (textOrder == 0) {
			text.setVisibility(GONE);
		}
		if (imagesOrder == 0) {
			images.setVisibility(GONE);
		}
		if (voiceOrder == 0) {
			voice.setVisibility(GONE);
		}
		if (videoOrder == 0) {
			video.setVisibility(GONE);
		}
		if (mapOrder == 0) {
			map.setVisibility(GONE);
		}
		
		
		if (voteOrder == 1) {
			vote.setVisibility(GONE);
		}
		if (textOrder == 1) {
			text.setVisibility(GONE);
		}
		if (imagesOrder == 1) {
			images.setVisibility(GONE);
		}
		if (voiceOrder == 1) {
			voice.setVisibility(GONE);
		}
		if (videoOrder == 1) {
			video.setVisibility(GONE);
		}
		if (mapOrder == 1) {
			map.setVisibility(GONE);
		}
		if (voteOrder == 1) {
			vote.setVisibility(GONE);
		}
		
		if (textOrder == 2) {
			text.setVisibility(GONE);
		}
		if (imagesOrder == 2) {
			images.setVisibility(GONE);
		}
		if (voiceOrder == 2) {
			voice.setVisibility(GONE);
		}
		if (videoOrder == 2) {
			video.setVisibility(GONE);
		}
		if (mapOrder == 2) {
			map.setVisibility(GONE);
		}
		if (voteOrder == 2) {
			vote.setVisibility(GONE);
		}
		
		
		if (textOrder == 3) {
			text.setVisibility(GONE);
		}
		if (imagesOrder == 3) {
			images.setVisibility(GONE);
		}
		if (voiceOrder == 3) {
			voice.setVisibility(GONE);
		}
		if (videoOrder == 3) {
			video.setVisibility(GONE);
		}
		if (mapOrder == 3) {
			map.setVisibility(GONE);
		}
		if (voteOrder == 3) {
			vote.setVisibility(GONE);
		}
		
		
		if (textOrder == 4) {
			text.setVisibility(GONE);
		}
		if (imagesOrder == 4) {
			images.setVisibility(GONE);
		}
		if (voiceOrder == 4) {
			voice.setVisibility(GONE);
		}
		if (videoOrder == 4) {
			video.setVisibility(GONE);
		}
		if (mapOrder == 4) {
			map.setVisibility(GONE);
		}
		if (voteOrder == 4) {
			vote.setVisibility(GONE);
		}
		
		if (textOrder == 5) {
			text.setVisibility(GONE);
		}
		if (imagesOrder == 5) {
			images.setVisibility(GONE);
		}
		if (voiceOrder == 5) {
			voice.setVisibility(GONE);
		}
		if (videoOrder == 5) {
			video.setVisibility(GONE);
		}
		if (mapOrder == 5) {
			map.setVisibility(GONE);
		}
		if (voteOrder == 5) {
			vote.setVisibility(GONE);
		}
	}

}
