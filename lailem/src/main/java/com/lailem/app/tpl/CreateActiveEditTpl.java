package com.lailem.app.tpl;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.lailem.app.R;
import com.lailem.app.adapter.SimpleTextWatcher;
import com.lailem.app.bean.CreateActiveIntroEditBean;
import com.lailem.app.ui.create_old.CreateActiveActivity;
import com.lailem.app.utils.TDevice;
import com.lailem.app.widget.EditTextEx;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by XuYang on 15/12/10.
 */
public class CreateActiveEditTpl extends BaseLinearTpl<Object> {
    @Bind(R.id.content)
    EditTextEx content_et;

    public CreateActiveEditTpl(final Context context, ArrayList<Object> data) {
        super(context, data);
        content_et.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    getIntroLL().setTag(getIntroLL().indexOfChild(CreateActiveEditTpl.this));
                }
                View editActionView = ((CreateActiveActivity) _activity).editActionBar;
                editActionView.setVisibility(VISIBLE);
                return false;
            }
        });
        content_et.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getBean().setText(s.toString());
            }
        });

        content_et.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (content_et.getSelectionStart() == 0) {
                        delete();
                        return true;
                    }
                }
                return false;
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_create_active_intro_edit;
    }

    @Override
    public void setBean() {
        content_et.setText(getBean().getText());
        checkMinHeight();
    }

    private void checkMinHeight() {
        //只有文本存在
        if (data.size() == 1) {
            content_et.setHint("请详细介绍一下您的活动内容及安排");
            content_et.setMinHeight((int) TDevice.dpToPixel(140f));
        } else {
            content_et.setHint("");
            content_et.setMinHeight((int) TDevice.dpToPixel(40f));
        }
    }

    private void delete() {
        if (getIndex() > 1) {
            //位置至少为2
            //删除图片条目
            //合并当前条目和上一条edit条目
            int lastEditIndex = getIndex() - 2;
            CreateActiveIntroEditBean lastEditBean = (CreateActiveIntroEditBean) data.get(lastEditIndex);
            CreateActiveIntroEditBean curEditBean = getBean();
            curEditBean.setText(lastEditBean.getText() + curEditBean.getText());
            content_et.setText(curEditBean.getText());
            content_et.setSelection(lastEditBean.getText().length());

            int deleteImageIndex = getIndex() - 1;
            data.remove(deleteImageIndex);
            getIntroLL().removeViewAt(deleteImageIndex);

            data.remove(lastEditIndex);
            getIntroLL().removeViewAt(lastEditIndex);
            checkMinHeight();
            getIntroLL().setTag(getIndex());
        }
    }

    private CreateActiveIntroEditBean getBean() {
        CreateActiveIntroEditBean bean = (CreateActiveIntroEditBean) data.get(getIndex());
        return bean;
    }

    private int getIndex() {
        LinearLayout intro_ll = (LinearLayout) getParent();
        int index = intro_ll.indexOfChild(CreateActiveEditTpl.this);
        return index;
    }

    private LinearLayout getIntroLL() {
        LinearLayout intro_ll = (LinearLayout) getParent();
        return intro_ll;
    }
}
