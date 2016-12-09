package com.lailem.app.jsonbean.personal;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * Created by XuYang on 15/10/19.
 */
public class ComplainTypeBean extends Result {

    private ArrayList<ComplainType> compplainTypeList;

    public static ComplainTypeBean parse(String json) throws AppException {
        ComplainTypeBean res = new ComplainTypeBean();
        try {
            res = gson.fromJson(json, ComplainTypeBean.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw AppException.json(e);
        }
        return res;
    }

    public ArrayList<ComplainType> getCompplainTypeList() {
        return compplainTypeList;
    }

    public void setCompplainTypeList(ArrayList<ComplainType> compplainTypeList) {
        this.compplainTypeList = compplainTypeList;
    }

    public class ComplainType extends  Result{
        private String id;//		string
        private String name;//	举报类型名称	string
        private int resourceId;

        public void setResourceId(int resourceId) {
            this.resourceId = resourceId;
        }

        public int getResourceId() {
            return resourceId;
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
    }
}
