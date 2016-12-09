package com.lailem.app.ui.create_old.dynamic.tpl;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.VoteModel;
import com.lailem.app.utils.TDevice;
import com.lailem.app.widget.dynamic.CreateVoteView;

import butterknife.Bind;

public class CreateVoteTpl extends BaseTpl<ObjectWrapper> {

    @Bind(R.id.createVoteView)
    CreateVoteView createVoteView;
    private VoteModel bean;

    public CreateVoteTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_create_vote;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        setItemFillParent(getChildAt(0));
        this.bean = (VoteModel) wrapper.getObject();
        createVoteView.render(bean);
    }

    private void setItemFillParent(View item) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) item.getLayoutParams();
        params.width = (int) TDevice.getScreenWidth();
        setLayoutParams(params);
    }
}
