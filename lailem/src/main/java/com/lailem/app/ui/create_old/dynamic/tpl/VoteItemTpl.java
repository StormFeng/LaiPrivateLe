package com.lailem.app.ui.create_old.dynamic.tpl;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.VoteModel.VoteItem;

import butterknife.Bind;

public class VoteItemTpl extends BaseTpl<VoteItem> implements TextWatcher {

    @Bind(R.id.name)
    EditText name_et;
    @Bind(R.id.label)
    TextView label_tv;

    private VoteItem bean;

    private int position;

    public VoteItemTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_vote_item;
    }

    @Override
    protected void initView() {
        super.initView();
        name_et.addTextChangedListener(this);
    }

    @Override
    public void setBean(VoteItem bean, int position) {
        this.bean = bean;
        this.position = position;
        name_et.setText(bean.getName());
        label_tv.setText((position + 1) + ".");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        bean.setName(name_et.getText().toString().trim());

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
