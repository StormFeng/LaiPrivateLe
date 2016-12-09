package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

public class UploadFileInfo extends Result {

    private String fileName;
    private String localPath;

    public static UploadFileInfo parse(String json) throws AppException {
        UploadFileInfo res = new UploadFileInfo();
        try {
            res = gson.fromJson(json, UploadFileInfo.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw AppException.json(e);
        }
        return res;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
