package com.lailem.app.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lailem.app.R;
import com.lailem.app.base.BaseFragment;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.utils.UIHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatAddFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_chat_add, null);
        ButterKnife.bind(this, root);
        return root;
    }

    /**
     * 点击拍照
     */
    @OnClick(R.id.take_photo)
    public void clickTakePhoto() {
        PhotoManager.getInstance().photo(System.currentTimeMillis() + "");
    }

    /**
     * 点击图库
     */
    @OnClick(R.id.album)
    public void clickAlbum() {
        String tag = System.currentTimeMillis() + "";
        PhotoManager.getInstance().setLimit(tag, 9);
        PhotoManager.getInstance().album(tag);
    }

    /**
     * 点击视频
     */
    @OnClick(R.id.video)
    public void clickVideo() {
        UIHelper.showVideoRecord(_activity);
    }

    /**
     * 点击位置
     */
    @OnClick(R.id.position)
    public void clickPosition() {
        UIHelper.showSelectLoc(_activity);
    }

    /**
     * 点击联系人
     */
    @OnClick(R.id.contact)
    public void clickContact() {

    }

}
