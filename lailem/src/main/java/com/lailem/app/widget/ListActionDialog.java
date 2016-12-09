package com.lailem.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lailem.app.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListActionDialog extends Dialog {
    private Context context;

    private static final int DEFAULT_THEME = R.style.bottom_dialog;

    @Bind(R.id.items)
    LinearLayout itemsView;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ListActionDialog(Context context) {
        super(context, DEFAULT_THEME);
        init(context);
    }

    public ListActionDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(true);

        View contentView = View.inflate(context, R.layout.dialog_list_action, null);
        this.setContentView(contentView);

        ButterKnife.bind(this, contentView);
    }

    public ListActionDialog config(ArrayList<String> items, OnItemClickListener onItemClickListener) {
        return config(items.toArray(new String[items.size()]), onItemClickListener);
    }

    public ListActionDialog config(String[] items, final OnItemClickListener onItemClickListener) {
        for (int i = 0; i < items.length; i++) {
            final int position = i;
            TextView item = (TextView) View.inflate(getContext(), R.layout.dialog_item, null);
            ImageView divider = (ImageView) View.inflate(getContext(), R.layout.dialog_divider, null);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position);
                    }
                    dismiss();
                }
            });
            item.setText(items[i]);
            itemsView.addView(item);

            if (items.length == 1) {
                item.setBackgroundResource(R.drawable.dialog_item_bg);
            } else {
                LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                if (i == 0) {
                    itemsView.addView(divider, dividerParams);
                    item.setBackgroundResource(R.drawable.dialog_first_item_bg);
                } else if (i == items.length - 1) {
                    item.setBackgroundResource(R.drawable.dialog_last_item_bg);
                } else {
                    itemsView.addView(divider, dividerParams);
                    item.setBackgroundResource(R.drawable.dialog_middle_item_bg);
                }
            }
        }
        return this;
    }

    @OnClick(R.id.cancel)
    public void clickCancel() {
        dismiss();
    }
}
