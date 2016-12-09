package com.lailem.app.jsonbean.activegroup;

import com.lailem.app.bean.Result;

import java.util.ArrayList;

public class VoteInfo extends Result {
	private String canVote;
	private String desc;
	private String endTime;
	private String id;
	private String isVoted;
	private ArrayList<Item> items;
	private String selectCount;
	private String startTime;

	private String topic;

	private String voteCount;

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(String voteCount) {
		this.voteCount = voteCount;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getCanVote() {
		return canVote;
	}

	public void setCanVote(String canVote) {
		this.canVote = canVote;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsVoted() {
		return isVoted;
	}

	public void setIsVoted(String isVoted) {
		this.isVoted = isVoted;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public String getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(String selectCount) {
		this.selectCount = selectCount;
	}

	public static class Item {
		private String desc;
		private String id;
		private String name;
		private String voteCount;

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getVoteCount() {
			return voteCount;
		}

		public void setVoteCount(String voteCount) {
			this.voteCount = voteCount;
		}

	}
}
