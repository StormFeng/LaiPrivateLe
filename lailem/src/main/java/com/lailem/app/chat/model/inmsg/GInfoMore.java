package com.lailem.app.chat.model.inmsg;

public class GInfoMore {
	private String id;
	private String gType;
	private String gName;
	private String gSSPic;
	private String gIntro;
    private String perm;//1（私有群）2（公有群）

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getgType() {
		return gType;
	}

	public void setgType(String gType) {
		this.gType = gType;
	}

	public String getgName() {
		return gName;
	}

	public void setgName(String gName) {
		this.gName = gName;
	}

	public String getgSSPic() {
		return gSSPic;
	}

	public void setgSSPic(String gSSPic) {
		this.gSSPic = gSSPic;
	}

	public String getgIntro() {
		return gIntro;
	}

	public void setgIntro(String gIntro) {
		this.gIntro = gIntro;
	}

}
