package com.lailem.app.bean;

public class Contact extends Result {
	private String name;
	private String nickName;
	private String letter;
	private String imageUri;
	private String phone;

	public Contact() {

	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getImageUri() {
		return imageUri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public Contact(String imageUri, String name, String nickName, String letter, int itemViewType) {
		super();
		this.imageUri = imageUri;
		this.name = name;
		this.nickName = nickName;
		this.letter = letter;
		this.itemViewType = itemViewType;
	}

}