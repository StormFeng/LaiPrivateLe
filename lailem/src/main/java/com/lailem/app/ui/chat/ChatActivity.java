package com.lailem.app.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.adapter.datasource.MessageListDataSource;
import com.lailem.app.base.BaseListActivity;
import com.lailem.app.bean.AddressInfo;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.ConversationSetBroadcastReceiver;
import com.lailem.app.cache.GroupCache;
import com.lailem.app.cache.UserCache;
import com.lailem.app.chat.listener.OnChatListener;
import com.lailem.app.chat.listener.OnSendListener;
import com.lailem.app.chat.util.ChatListenerManager;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.chat.util.MessageFactory;
import com.lailem.app.dao.Group;
import com.lailem.app.dao.Message;
import com.lailem.app.dao.User;
import com.lailem.app.listener.OnAudioRecordListener;
import com.lailem.app.loadfactory.ChatLoadViewFactory;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.photo.listenser.OnPhotoListener;
import com.lailem.app.tpl.chat.MessagePicTpl;
import com.lailem.app.tpl.chat.MessagePositionTpl;
import com.lailem.app.tpl.chat.MessageTextTpl;
import com.lailem.app.tpl.chat.MessageTipTpl;
import com.lailem.app.tpl.chat.MessageVideoTpl;
import com.lailem.app.tpl.chat.MessageVoiceTpl;
import com.lailem.app.ui.chat.expression.Expression;
import com.lailem.app.ui.chat.expression.ExpressionFragment;
import com.lailem.app.ui.chat.expression.ExpressionUtil;
import com.lailem.app.ui.chat.expression.OnExpressionClickListener;
import com.lailem.app.ui.group.SelectLocActivity;
import com.lailem.app.ui.qupai.VideoRecordActivity;
import com.lailem.app.utils.AudioRecordUtils;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.socks.library.KLog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.lailem.app.ui.video.record.MediaRecorderActivity;

public class ChatActivity extends BaseListActivity<Message> implements OnPhotoListener, OnExpressionClickListener, OnChatListener, OnAudioRecordListener, OnSendListener {
    public static final int TPL_LEFT_TEXT = 0;
    public static final int TPL_LEFT_PIC = 1;
    public static final int TPL_LEFT_VOICE = 2;
    public static final int TPL_LEFT_VIDEO = 3;
    public static final int TPL_LEFT_POSITION = 4;
    public static final int TPL_RIGHT_TEXT = 5;
    public static final int TPL_RIGHT_PIC = 6;
    public static final int TPL_RIGHT_VOICE = 7;
    public static final int TPL_RIGHT_VIDEO = 8;
    public static final int TPL_RIGHT_POSITION = 9;
    public static final int TPL_TIP = 10;

    public static final String TAG_VOICE = "voice";
    public static final String TAG_KEYBOARD = "keyboard";

    @Bind(R.id.topbar)
    TopBarView topbar;

    @Bind(R.id.switch_iv)
    ImageView switchIv;
    /**
     * 文本输入
     */
    @Bind(R.id.et)
    EditText et;
    @Bind(R.id.fragment_ll)
    LinearLayout fragmentLL;
    @Bind(R.id.add)
    Button addOrSendBtn;
    @Bind(R.id.face)
    ImageView faceIv;
    ChatAddFragment chatAddFragment;
    ExpressionFragment expressionFragment;
    @Bind(R.id.fl)
    FrameLayout fl;
    @Bind(R.id.recordVoiceView)
    RecordAudioViewForChat recordVoiceView;
    MessageListDataSource dataSource;
    /**
     * 表情与加号对应的fragment是否显示
     */
    boolean isFragmentShow;
    /**
     * 软键盘是否显示
     */
    boolean isKeyboardShow;
    AudioRecordUtils soundRecordUtils;
    /**
     * 录音持续时间
     */
    int durationForVoice;
    /**
     * 正在播放语音的item位置，没有正在播放时该值等于-1
     */
    public static final int DEFAULT_POSITION = -1;
    public int positionForPlayingVoice = DEFAULT_POSITION;
    String conversationId, tId, cType;

    ConversationSetBroadcastReceiver conversationSetBroadcastReceiver = new ConversationSetBroadcastReceiver() {
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (ConversationSetBroadcastReceiver.ACTION_CLEAR_MSG_RECORD.equals(intent.getAction())) {
                if (conversationId.equals(intent.getStringExtra("conversationId")))
                    listViewHelper.refresh();
            }
        }
    };
    private AudioRecordUtils recordUtilsForPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        PhotoManager.getInstance().onCreate(_activity, this);
        initView();
        ChatListenerManager.getInstance().registerOnChatListener(this);
        ChatListenerManager.getInstance().registerOnSendListener(this);
        BroadcastManager.registerConversationSetBroadcastReceiver(_activity, conversationSetBroadcastReceiver);
        listViewHelper.refresh();

        if (recordUtilsForPermission == null) {
            recordUtilsForPermission = new AudioRecordUtils();
            recordUtilsForPermission.setMaxDuration(10*1000);
            recordUtilsForPermission.setOnSoundRecordListener(new OnAudioRecordListener() {
                @Override
                public void onAudioRecordStart() {

                }

                @Override
                public void onAudioRecordDb(double db) {

                }

                @Override
                public void onAudioRecordEnd(String path, int time) {

                }

                @Override
                public void onAudioRecordTime(int time) {

                }

                @Override
                public void onAudioRecordCancel() {

                }
            });
        }
        recordUtilsForPermission.start(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recordUtilsForPermission.stop();
            }
        }, 1000);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        listViewHelper.setDataSource(getDataSource(intent));
        initView();
        resultList.clear();
        adapter.notifyDataSetChanged();
        listViewHelper.refresh();

    }

    private void initView() {
        String title = "聊天";
        if (Constant.cType_sc.equals(cType)) {
            User user = UserCache.getInstance(_activity).get(tId);
            if (user != null) {
                title = user.getNickname() == null ? "" : Func.formatNickName(_activity, tId, user.getNickname());
            }
        } else if (Constant.cType_gc.equals(cType)) {
            Group group = GroupCache.getInstance(_activity).get(tId);
            if (group != null) {
                title = group.getName();
            }
        }
        topbar.setTitle(title).setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(_activity)).setRightImageButton(R.drawable.ic_topbar_more, new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Constant.cType_sc.equals(cType)) {
                    UIHelper.showMemberInfoForChat(_activity, tId, conversationId);
                } else if (Constant.cType_gc.equals(cType)) {
                    UIHelper.showGroupInfoForChat(_activity, tId, conversationId);
                } else if (Constant.cType_p.equals(cType)) {

                }
            }
        });
        refreshListView.setScrollLoadEnabled(false);
        listView.setDividerHeight(0);
        et.addTextChangedListener(textWatcher);
        et.setOnTouchListener(onTouchListenerForEt);
    }

    @Override
    public void onEndRefresh(IDataAdapter<ArrayList<Message>> adapter, ArrayList<Message> result) {
        super.onEndRefresh(adapter, result);
        if (resultList.size() == 0) {
            return;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    /**
     * 父类onCreate方法中调用
     */
    @Override
    protected IDataSource getDataSource() {
        tId = getIntent().getStringExtra("tId");
        cType = getIntent().getStringExtra("cType");
        conversationId = MessageFactory.getFactory().onCreate(tId, getIntent().getStringExtra("conversationId"), cType);
        dataSource = new MessageListDataSource(_activity, conversationId);
        return dataSource;
    }

    private IDataSource getDataSource(Intent newIntent) {
        tId = newIntent.getStringExtra("tId");
        cType = newIntent.getStringExtra("cType");
        conversationId = MessageFactory.getFactory().onCreate(tId, newIntent.getStringExtra("conversationId"), cType);
        dataSource = new MessageListDataSource(_activity, conversationId);
        return dataSource;
    }

    @Override
    protected ArrayList getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(TPL_LEFT_TEXT, MessageTextTpl.class);
        tpls.add(TPL_LEFT_PIC, MessagePicTpl.class);
        tpls.add(TPL_LEFT_VOICE, MessageVoiceTpl.class);
        tpls.add(TPL_LEFT_VIDEO, MessageVideoTpl.class);
        tpls.add(TPL_LEFT_POSITION, MessagePositionTpl.class);
        tpls.add(TPL_RIGHT_TEXT, MessageTextTpl.class);
        tpls.add(TPL_RIGHT_PIC, MessagePicTpl.class);
        tpls.add(TPL_RIGHT_VOICE, MessageVoiceTpl.class);
        tpls.add(TPL_RIGHT_VIDEO, MessageVideoTpl.class);
        tpls.add(TPL_RIGHT_POSITION, MessagePositionTpl.class);
        tpls.add(TPL_TIP, MessageTipTpl.class);
        return tpls;
    }

    OnLongClickListener onLongClickListener = new OnLongClickListener() {

        @Override
        public boolean onLongClick(View view) {
            KLog.i("开始录音");
            // 录音开始
            recordVoiceView.setVisibility(View.VISIBLE);
            recordVoiceView.start();
            if (soundRecordUtils == null) {
                soundRecordUtils = new AudioRecordUtils();
                soundRecordUtils.setUpdatePeriod(100);
                soundRecordUtils.setMaxDuration(60);
                soundRecordUtils.setOnSoundRecordListener(ChatActivity.this);
            }
            soundRecordUtils.start(_activity);

            return false;
        }

    };
    OnTouchListener onTouchListenerForEt = new OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent event) {

            if (TAG_KEYBOARD.equals(switchIv.getTag().toString())) {// 输入框时触摸
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hideFragmemnt();
                    // 表示键盘显示
                    isKeyboardShow = true;
                }
            } else if (TAG_VOICE.equals(switchIv.getTag().toString())) {// 语音输入时触摸
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // 录音结束
                    if (recordVoiceView.getVisibility() == view.VISIBLE) {
                        soundRecordUtils.stop();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (event.getY() < -10) {
                        recordVoiceView.showTip2();
                    } else {
                        recordVoiceView.showTip1();
                    }
                }
            }
            return false;
        }
    };

    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            LayoutParams params = (LayoutParams) addOrSendBtn.getLayoutParams();
            if (StringUtils.isEmpty(editable.toString())) {
                params.width = (int) TDevice.dpToPixel(28.8f);
                params.height = params.width;
                addOrSendBtn.setLayoutParams(params);
                addOrSendBtn.setText("");
                addOrSendBtn.setBackgroundResource(R.drawable.ic_add);
            } else {
                params.width = (int) TDevice.dpToPixel(40.3f);
                params.height = (int) TDevice.dpToPixel(30.7f);
                addOrSendBtn.setLayoutParams(params);
                addOrSendBtn.setText("发送");
                addOrSendBtn.setBackgroundResource(R.drawable.bg_send);
            }
        }
    };

    /**
     * 点击了语音与键盘切换
     */
    @OnClick(R.id.switch_iv)
    public void clickSwitch() {
        String tag = (String) switchIv.getTag();
        if (TAG_KEYBOARD.equals(tag)) {
            showVoiceInput();
        } else if (TAG_VOICE.equals(tag)) {
            showTextInput();
        }
    }

    /**
     * 显示语音输入
     */
    private void showVoiceInput() {
        if (TAG_KEYBOARD.equals(switchIv.getTag())) {
            hideKeyboard();
            hideFragmemnt();
            switchIv.setTag("voice");
            switchIv.setBackgroundResource(R.drawable.ic_keyboard);
            et.setBackgroundResource(R.drawable.bg_chat_et_p);
            et.setFocusable(false);
            et.setCursorVisible(false);
            et.setLongClickable(true);
            et.setHint("按住输入语音");
            et.setGravity(Gravity.CENTER);
            // 长按
            et.setOnLongClickListener(onLongClickListener);
        }
    }

    /**
     * 显示文本输入
     */
    private void showTextInput() {
        if (TAG_VOICE.equals(switchIv.getTag())) {
            switchIv.setTag("keyboard");
            switchIv.setBackgroundResource(R.drawable.ic_voice1);
            et.setBackgroundResource(R.drawable.bg_chat_et_n);
            et.setFocusable(true);
            et.setFocusableInTouchMode(true);
            et.requestFocus();
            et.setCursorVisible(true);
            et.setLongClickable(false);
            et.setHint("");
            et.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
    }

    /**
     * 点击加号
     */
    @OnClick(R.id.add)
    public void clickAddOrSend() {
        // 点击加号
        if (StringUtils.isEmpty(addOrSendBtn.getText().toString())) {

            faceIv.setTag(false);
            Object tag = addOrSendBtn.getTag();
            if (tag == null || !(Boolean) tag) {
                hideKeyboard();
                showTextInput();
                addOrSendBtn.setTag(true);
                chatAddFragment = new ChatAddFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_ll, chatAddFragment, null).commit();
                isFragmentShow = true;
            } else {
                addOrSendBtn.setTag(false);
                hideFragmemnt();
            }
        } else {// 点击发送
            sendText();
        }
    }

    @OnClick(R.id.face)
    public void clickFace() {

        addOrSendBtn.setTag(false);
        Object tag = faceIv.getTag();
        if (tag == null || !(Boolean) tag) {
            hideKeyboard();
            showTextInput();
            faceIv.setTag(true);
            expressionFragment = new ExpressionFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_ll, expressionFragment, null).commit();
            isFragmentShow = true;
        } else {
            faceIv.setTag(false);
            hideFragmemnt();
        }

    }

    /**
     * 隐藏表情或功能区
     */
    private void hideFragmemnt() {
        if (isFragmentShow) {
            fragmentLL.removeAllViews();
            isFragmentShow = false;
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (isKeyboardShow) {
            InputMethodManager inputManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(et.getWindowToken(), 0);
            isKeyboardShow = false;
        }
    }

    @Override
    protected void onPause() {
        recordVoiceView.stop();
        super.onPause();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PhotoManager.getInstance().onActivityResult(requestCode, resultCode, data);
//		if (resultCode == MediaRecorderActivity.RESULT_CODE_FOR_VIDEO_PATH && requestCode == MediaRecorderActivity.REQUEST_CODE_FOR_VIDEO_PATH) {
//			String videoPath = data.getStringExtra(MediaRecorderActivity.DATA_FOR_VIDEO_PATH);
//			String previewImagePath = data.getStringExtra(MediaRecorderActivity.DATA_FOR_PREVIEW_IMAGE_PATH);
//			String duration = data.getStringExtra(MediaRecorderActivity.DATA_FOR_DURATION);
//			sendVideo(videoPath, previewImagePath, duration);
//		}
        if (resultCode == VideoRecordActivity.RESULT_CODE_FOR_VIDEO_PATH && requestCode == VideoRecordActivity.REQUEST_CODE_FOR_VIDEO_PATH) {
            String videoPath = data.getStringExtra(VideoRecordActivity.DATA_FOR_VIDEO_PATH);
            String previewImagePath = data.getStringExtra(VideoRecordActivity.DATA_FOR_PREVIEW_IMAGE_PATH);
            String duration = data.getStringExtra(VideoRecordActivity.DATA_FOR_DURATION);
            sendVideo(videoPath, previewImagePath, duration);
        }
        // 选择位置
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SelectLocActivity.REQUEST_SELECT_LOC:
                    AddressInfo addressInfo = (AddressInfo) data.getExtras().getSerializable(SelectLocActivity.BUNDLE_KEY_ADDRESSINFO);
                    if (addressInfo.isValid()) {
                        sendAddr(addressInfo.getAddress(), addressInfo.getLng(), addressInfo.getLat());
                    } else {
                        AppContext.showToast("您选择位置信息无效");
                    }
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PhotoManager.getInstance().onDestory();
        MessageFactory.getFactory().onDestory();
        ChatListenerManager.getInstance().unRegisterOnChatListener();
        ChatListenerManager.getInstance().unRegisterOnSendListener(this);
        BroadcastManager.unRegisterConversationSetBroadcastReceiver(_activity, conversationSetBroadcastReceiver);
    }

    @Override
    public void onPhoto(String tag, ArrayList<PhotoBean> photos) {
        for (PhotoBean photoBean : photos) {
            sendPic(photoBean);
        }
    }

    @Override
    public void onExpressionClick(Expression emoji, String type) {
        if (emoji.getId() == R.drawable.chat_delete) {
            int selection = et.getSelectionStart();
            String text = et.getText().toString();
            if (selection > 0) {
                String text2 = text.substring(selection - 1);
                if ("]".equals(text2)) {
                    int start = text.lastIndexOf("[");
                    int end = selection;
                    et.getText().delete(start, end);
                    return;
                }
                et.getText().delete(selection - 1, selection);
            }
        }
        if (!StringUtils.isEmpty(emoji.getCharacter())) {
            if (ExpressionFragment.class.getSimpleName().equals(type)) {
                SpannableString spannableString = ExpressionUtil.getInstace().addFace(emoji.getId(), emoji.getCharacter());
                et.append(spannableString);
            }

        }
    }

    /**
     * 发送消息
     *
     * @param message
     */
    private void send(Message message) {
        resultList.add(message);
        adapter.notifyDataSetChanged();
        listView.setSelection(resultList.size() - 1);
        message.send();

    }

    private void dealTime() {
        Message timeMessage = MessageFactory.getFactory().checkMessageTimeForSend();
        if (timeMessage != null) {
            resultList.add(timeMessage);
        }
    }

    /**
     * 发送文本
     */
    private void sendText() {
        dealTime();
        Message message = MessageFactory.getFactory().createMessageText(et.getText().toString());
        send(message);
        et.setText("");
    }

    /**
     * 发送图片
     *
     * @param photoBean
     */
    private void sendPic(PhotoBean photoBean) {
        dealTime();
        Message message = MessageFactory.getFactory().createMessagePic(photoBean.getImagePath());
        send(message);
    }

    private void sendVoice(String path, String duration) {
        dealTime();
        Message message = MessageFactory.getFactory().createMessageVoice(path, duration);
        send(message);
    }

    private void sendVideo(String videoPath, String previewImagePath, String durtion) {
        dealTime();
        Message message = MessageFactory.getFactory().createMessageVideo(videoPath, previewImagePath, durtion);
        send(message);
    }

    private void sendAddr(String address, String lon, String lat) {
        dealTime();
        Message message = MessageFactory.getFactory().createMessageAddress(address, lon, lat);
        send(message);
    }

    /**
     * OnChatListener
     */
    @Override
    public void onChat(Message message) {
        resultList.add(message);
        listView.setSelection(resultList.size() - 1);
    }

    /**
     * OnAudioRecordListener
     */
    @Override
    public void onAudioRecordStart() {

    }

    /**
     * OnAudioRecordListener
     */
    @Override
    public void onAudioRecordDb(double db) {
        KLog.i("db:::" + db);
        recordVoiceView.setDb(db);

    }

    /**
     * OnAudioRecordListener
     */
    @Override
    public void onAudioRecordEnd(String path, int time) {
        KLog.i("soundRecordPath:::" + path);
        if (time > 1) {
            sendVoice(path, durationForVoice + "");
        }
        recordVoiceView.stop();
        recordVoiceView.setVisibility(View.GONE);
    }

    /**
     * OnAudioRecordListener
     */
    @Override
    public void onAudioRecordTime(int time) {
        KLog.i("soundRecordTime:::" + time);
        durationForVoice = time;
    }

    /**
     * OnAudioRecordListener
     */
    @Override
    public void onAudioRecordCancel() {
        recordVoiceView.setVisibility(View.GONE);
    }


    /**
     * OnSendListener
     */
    @Override
    public void onSended(Message message) {

    }

    /**
     * OnSendListener
     */
    @Override
    public void onSendFail(Message message) {
        for (Message message2 : resultList) {
            if (message2.getId() == message.getId()) {
                message2.setStatus(message.getStatus());
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }

    /**
     * OnSendListener
     */
    @Override
    public void onSendUploading(Message message, String progress) {

    }

    @Override
    protected com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory getLoadViewFactory() {
        return new ChatLoadViewFactory();
    }

}
