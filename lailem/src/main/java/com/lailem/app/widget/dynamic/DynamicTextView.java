package com.lailem.app.widget.dynamic;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.widget.XTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DynamicTextView extends LinearLayout {

    public int maxLine = 5;
    @Bind(R.id.text)
    XTextView text_tv;
    @Bind(R.id.toggleAll)
    TextView toggleAll_tv;

    private SparseIntArray openedList = new SparseIntArray();

    public DynamicTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DynamicTextView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_dynamic_text, this);
        ButterKnife.bind(this, this);
    }

    @OnClick(R.id.toggleAll)
    public void toggleAll() {
        if (text_tv.getMaxLines() == maxLine) {
            text_tv.setMaxLines(Integer.MAX_VALUE);
            toggleAll_tv.setText("收起");
        } else {
            text_tv.setMaxLines(maxLine);
            toggleAll_tv.setText("显示全部");
        }
        text_tv.isOverFlowed();
    }

    public void render(String text) {
        text_tv.setMaxLines(Integer.MAX_VALUE);
        text_tv.setText(text);
        toggleAll_tv.setText("显示全部");
        if (text_tv.getLineCount() == 0) {
            text_tv.post(new Runnable() {
                @Override
                public void run() {
                    handleLineCount();
                }
            });
        } else {
            handleLineCount();
        }
    }

    private void handleLineCount() {
        int lineCount = text_tv.getLineCount();
        if (lineCount > maxLine) {
            text_tv.setMaxLines(maxLine);
            toggleAll_tv.setVisibility(VISIBLE);
        } else {
            text_tv.setMaxLines(Integer.MAX_VALUE);
            toggleAll_tv.setVisibility(GONE);
        }
    }

    public void render(String text, boolean hideToggle) {
        text_tv.setText(text);
        if (hideToggle) {
            toggleAll_tv.setVisibility(GONE);
        }
        text_tv.setMaxLines(Integer.MAX_VALUE);
    }

}
