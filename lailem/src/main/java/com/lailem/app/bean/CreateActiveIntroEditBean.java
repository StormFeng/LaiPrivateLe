package com.lailem.app.bean;

import com.lailem.app.ui.create_old.CreateActiveActivity;

/**
 * Created by XuYang on 15/12/9.
 */
public class CreateActiveIntroEditBean extends Base {
    private String text="";
    private int selectionStart;

    public CreateActiveIntroEditBean() {
        this.itemViewType = CreateActiveActivity.TPL_EDIT;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSelectionStart() {
        return selectionStart;
    }

    public void setSelectionStart(int selectionStart) {
        this.selectionStart = selectionStart;
    }

}
