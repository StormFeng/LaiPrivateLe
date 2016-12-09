package com.lailem.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.lailem.app.R;
import com.lailem.app.ui.chat.expression.Expression;
import com.lailem.app.ui.chat.expression.ExpressionUtil;
import com.lailem.app.ui.chat.expression.ExpressionView;
import com.lailem.app.ui.chat.expression.OnExpressionClickListener;
import com.lailem.app.utils.TDevice;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加评论输入对话框
 *
 * @author XuYang
 */
public class AddCommentBarDialog extends Dialog implements OnTouchListener, OnExpressionClickListener {
    /**
     * 默认主题
     */
    private static final int DEFAULT_THEME = R.style.add_comment_bar_dialog;
    /**
     * 文本输入状态
     */
    public static final int STATE_NORMAL = 0;
    /**
     * 表情输入状态
     */
    public static final int STATE_FACE = 1;

    /**
     * 文本/表情切换
     */
    @Bind(R.id.toggleFace)
    ImageButton toggleFace_ib;
    /**
     * 输入框
     */
    @Bind(R.id.input)
    EditText input_et;
    /**
     * 表情控件
     */
    @Bind(R.id.faceArea)
    ExpressionView faceArea;

    /**
     * 当前输入状态
     */
    private int state = STATE_NORMAL;

    private OnSendClickListener onSendClickListener;

    public interface OnSendClickListener {
        void onSendClick(String content);
    }

    public AddCommentBarDialog(Context context) {
        super(context, DEFAULT_THEME);
        init(context);
    }

    public AddCommentBarDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(true);
        View contentView = View.inflate(context, R.layout.dialog_add_comment_bar, null);
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams((int) TDevice.getScreenWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(contentView, p);
        ButterKnife.bind(this, contentView);

        // 设置表情点击回调
        faceArea.setCallBack(this);
        input_et.setOnTouchListener(this);
    }

    /**
     * 切换文本输入/表情输入
     */
    @OnClick(R.id.toggleFace)
    public void clickToggleFace() {
        switch (state) {
            case STATE_NORMAL:
                dismiss();
                setState(STATE_FACE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        show();
                    }
                }, 100);
                break;
            case STATE_FACE:
                dismiss();
                setState(STATE_NORMAL);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        show();
                    }
                }, 100);
                break;
        }
    }

    /**
     * 点击发送按钮
     */
    @OnClick(R.id.submit)
    public void submit() {
        if (onSendClickListener != null) {
            String content = input_et.getText().toString().trim();
            content.replaceAll("\n", " ");
            onSendClickListener.onSendClick(content);
        }
    }

    /**
     * 显示文本输入
     */
    public void showNormal() {
        // 隐藏表情
        faceArea.setVisibility(View.GONE);
        // 弹出键盘
        input_et.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) input_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

    }

    /**
     * 显示表情输入
     */
    public void showFace() {
        // 隐藏键盘
        InputMethodManager inputManager = (InputMethodManager) input_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(input_et.getWindowToken(), 0);
        // 显示表情
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                faceArea.setVisibility(View.VISIBLE);
            }
        }, 10);
    }

    /**
     * 表情点击回调
     */
    @Override
    public void onExpressionClick(Expression emoji, String type) {
        // 点击删除按钮
        if (emoji.getId() == R.drawable.chat_delete) {
            int selectionStart = input_et.getSelectionStart();
            int selectionEnd = input_et.getSelectionEnd();
            String text = input_et.getText().toString();
            if (selectionStart > 0) {
                String text2 = text.substring(selectionStart - 1);
                if ("]".equals(text2)) {
                    int start = text.lastIndexOf("[");
                    int end = selectionStart;
                    input_et.getText().delete(start, end);
                    return;
                }
                input_et.getText().delete(selectionStart - 1, selectionStart);
            }
        }
        // 点击正常表情
        if (!TextUtils.isEmpty(emoji.getCharacter())) {
            if (ExpressionView.class.getSimpleName().equals(type)) {
                SpannableString spannableString = ExpressionUtil.getInstace().addFace(emoji.getId(), emoji.getCharacter());
                int selectionStart = input_et.getSelectionStart();
                int selectionEnd = input_et.getSelectionEnd();
                Editable editable = input_et.getText();
                if (selectionEnd > selectionStart) {
                    editable.delete(selectionStart, selectionEnd);
                }
                editable.insert(selectionStart, spannableString);
            }

        }
    }

    /**
     * 监听输入框touch时间
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (v == input_et) {
                // 显示文本输入
                if (state != STATE_NORMAL) {
                    dismiss();
                    setState(STATE_NORMAL);
                    show();
                }
            }
        }
        return false;
    }

    /**
     * 设置点击发送按钮的回调
     *
     * @param onSendClickListener
     */
    public void setOnSendClickListener(OnSendClickListener onSendClickListener) {
        this.onSendClickListener = onSendClickListener;
    }

    @Override
    public void show() {
        switch (state) {
            case STATE_NORMAL:
                showNormal();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                break;
            case STATE_FACE:
                showFace();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;
        }
        super.show();
    }

    /**
     * 清空输入框
     */
    public void restore() {
        input_et.setText("");
        input_et.setHint("说点什么吧");
    }

    /**
     * 设置输入提示
     *
     * @param hint
     */
    public void setHint(String hint) {
        input_et.setHint(hint);
    }

    public void setState(int state) {
        this.state = state;
    }
}
