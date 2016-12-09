package com.lailem.app.ui.create_old.preview.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.VoteModel;
import com.lailem.app.widget.dynamic.CreateVoteView;

import butterknife.Bind;

public class LocalDynamicVoteTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.dynamicVote)
    CreateVoteView dynamicVoteView;
    private VoteModel bean;

    public LocalDynamicVoteTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_dynamic_preview_vote;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        this.bean = (VoteModel) wrapper.getObject();
        dynamicVoteView.render(bean);
    }

}
