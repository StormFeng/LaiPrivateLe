package com.lailem.app.ui.chat.expression;

/**
 * 
 ******************************************
 * @author fxx
 * @文件名称	:  ChatEmoji.java
 * @创建时间	: 2013-1-27 下午02:33:43
 * @文件描述	: 表情对象实体
 ******************************************
 */
public class Expression {

    /** 表情资源图片对应的ID */
    private int id;

    /** 表情资源对应的文字描述 */
    private String character;

    /** 表情资源的文件名 */
    private String faceName;
    
    
    private long time;
    

    /** 表情资源图片对应的ID */
    public int getId() {
        return id;
    }

    /** 表情资源图片对应的ID */
    public void setId(int id) {
        this.id=id;
    }

    /** 表情资源对应的文字描述 */
    public String getCharacter() {
        return character;
    }

    /** 表情资源对应的文字描述 */
    public void setCharacter(String character) {
        this.character=character;
    }

    /** 表情资源的文件名 */
    public String getFaceName() {
        return faceName;
    }

    /** 表情资源的文件名 */
    public void setFaceName(String faceName) {
        this.faceName=faceName;
    }

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		
		
		return "{\"id\":" + id + ", \"character\":" + "\""+character+"\""
				+ ", \"faceName\":" + "\""+faceName +"\""+ ", \"time\":" + time + "}";
	}

    
    
}
