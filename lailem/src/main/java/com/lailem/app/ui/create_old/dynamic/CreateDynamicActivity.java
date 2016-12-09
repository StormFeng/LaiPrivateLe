package com.lailem.app.ui.create_old.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.base.BaseListAdapter;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.bean.AddressInfo;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.bean.Result;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.DynamicTaskReceiver;
import com.lailem.app.jsonbean.activegroup.Pic;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.photo.listenser.OnPhotoListener;
import com.lailem.app.ui.create_old.dynamic.model.AddressModel;
import com.lailem.app.ui.create_old.dynamic.model.ImageModel;
import com.lailem.app.ui.create_old.dynamic.model.ImageModel.Content;
import com.lailem.app.ui.create_old.dynamic.model.ScheduleModel;
import com.lailem.app.ui.create_old.dynamic.model.TextModel;
import com.lailem.app.ui.create_old.dynamic.model.VideoModel;
import com.lailem.app.ui.create_old.dynamic.model.VoiceModel;
import com.lailem.app.ui.create_old.dynamic.model.VoteModel;
import com.lailem.app.ui.create_old.dynamic.tpl.CreateAddressTpl;
import com.lailem.app.ui.create_old.dynamic.tpl.CreateImageTpl;
import com.lailem.app.ui.create_old.dynamic.tpl.CreateSchduleTpl;
import com.lailem.app.ui.create_old.dynamic.tpl.CreateTextTpl;
import com.lailem.app.ui.create_old.dynamic.tpl.CreateVideoTpl;
import com.lailem.app.ui.create_old.dynamic.tpl.CreateVoiceTpl;
import com.lailem.app.ui.create_old.dynamic.tpl.CreateVoteTpl;
import com.lailem.app.ui.group.SelectLocActivity;
import com.lailem.app.ui.qupai.VideoRecordActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.DynamicTaskUtil;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;
import com.lailem.app.widget.ConfirmDialog;
import com.lailem.app.widget.dragsortlistview.DragSortListView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.lailem.app.ui.video.record.MediaRecorderActivity;

public class CreateDynamicActivity extends BaseActivity implements OnPhotoListener, AdapterView.OnItemLongClickListener {
    public static final String BUNDLE_KEY_TYPE = "type";

    public static final int TPL_TEXT = 0;
    public static final int TPL_IMAGE = 1;
    public static final int TPL_VIDEO = 2;
    public static final int TPL_VOICE = 3;
    public static final int TPL_VOTE = 4;
    public static final int TPL_SCHEDULE = 5;
    public static final int TPL_ADDRESS = 6;

    public static final int REQUEST_TEXT = 2000;
    public static final int REQUEST_VOTE = 2003;
    public static final int REQUEST_SCHEDULE = 1004;

    public static final String BUNDLE_KEY_TEXT = "text";
    public static final String BUNDLE_KEY_PICS = "pics";
    public static final String BUNDLE_KEY_VIDEO = "video";
    public static final String BUNDLE_KEY_VOICE = "voice";
    public static final String BUNDLE_KEY_ADDRESS = "address";
    public static final String BUNDLE_KEY_VOTE = "vote";
    public static final String BUNDLE_KEY_SCHEDULE = "schedule";

    public static final String BUNDLE_KEY_DYNAMICS = "dynamics";

    // 发表类型：1（文字）、2（图片）、3（语音）、4（视频）、5（地址）、6（日程）、7（投票）、8（连接）
    public static final String PUBLISH_TYPE_TEXT = "1";
    public static final String PUBLISH_TYPE_PICS = "2";
    public static final String PUBLISH_TYPE_VOICE = "3";
    public static final String PUBLISH_TYPE_VIDEO = "4";
    public static final String PUBLISH_TYPE_ADDRESS = "5";
    public static final String PUBLISH_TYPE_SCHEDULE = "6";
    public static final String PUBLISH_TYPE_VOTE = "7";
    public static final String PUBLISH_TYPE_LINK = "8";

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_GALLERY = 2;
    public static final int TYPE_VIDEO = 3;
    public static final int TYPE_VOICE = 4;
    public static final int TYPE_VOTE = 5;
    public static final int TYPE_ADDRESS = 6;
    public static final int TYPE_SCHEDULE = 7;

    @Bind(R.id.preview)
    TextView preview_tv;
    @Bind(R.id.publish)
    TextView publish_tv;

    @Bind(R.id.viewFlipper)
    ViewFlipper viewFlipper;
    @Bind(R.id.list)
    DragSortListView mListView;

    private String groupId;
    private int type = -1;

    private ArrayList<Object> models;
    private BaseListAdapter<Object> adapter;
    private HashMap<String, ImageModel> picsTagMode = new HashMap<String, ImageModel>();

    ArrayList<File> fileKey;
    String contentList;

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                Object item = models.remove(from);
                models.add(to, item);
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoManager.getInstance().onCreate(_activity, this);
        setContentView(R.layout.activity_create_dynamic_old);
        ButterKnife.bind(this);

        groupId = _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID);
        type = _Bundle.getInt(BUNDLE_KEY_TYPE);

        initView();

        switch (type) {
            case TYPE_TEXT:
                UIHelper.showCreateDynamicText(this);
                break;
            case TYPE_PHOTO:
                PhotoManager.getInstance().photo(System.currentTimeMillis() + "");
                break;
            case TYPE_GALLERY:
                PhotoManager.getInstance().album(System.currentTimeMillis() + "");
                break;
            case TYPE_VIDEO:
                UIHelper.showMediaRecord(_activity);
                break;
            case TYPE_VOICE:
                UIHelper.showCreateDynamicVoice(this);
                break;
            case TYPE_VOTE:
                UIHelper.showCreateDynamicVote(this);
                break;
            case TYPE_ADDRESS:
                UIHelper.showCreateDynamicAddress(this);
                break;
            case TYPE_SCHEDULE:
                UIHelper.showCreateDynamicSchedule(this);
                break;
        }
    }

    private void initView() {
        models = new ArrayList<Object>();
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(TPL_TEXT, CreateTextTpl.class);
        tpls.add(TPL_IMAGE, CreateImageTpl.class);
        tpls.add(TPL_VIDEO, CreateVideoTpl.class);
        tpls.add(TPL_VOICE, CreateVoiceTpl.class);
        tpls.add(TPL_VOTE, CreateVoteTpl.class);
        tpls.add(TPL_SCHEDULE, CreateSchduleTpl.class);
        tpls.add(TPL_ADDRESS, CreateAddressTpl.class);
        adapter = new BaseMultiTypeListAdapter<>(mListView, this, models, tpls, null);
        adapter.setRunnable(new Runnable() {

            @Override
            public void run() {
                if (models.size() > 0) {
                    publish_tv.setEnabled(true);
                    preview_tv.setEnabled(true);
                    viewFlipper.setDisplayedChild(1);
                } else {
                    publish_tv.setEnabled(false);
                    preview_tv.setEnabled(false);
                    viewFlipper.setDisplayedChild(0);
                }
            }
        });

        mListView.setOnItemLongClickListener(this);
        mListView.setAdapter(adapter);
        mListView.setDropListener(onDrop);
    }

    @OnClick(R.id.close)
    public void close() {
        finish();
    }

    @OnClick(R.id.preview)
    public void clickPreview() {
        UIHelper.showCreateDynamicPreview(this, models);
    }

    @OnClick(R.id.publish)
    public void clickPublish() {
        if (models.size() == 0) {
            AppContext.showToast("请添加要发布的内容");
            return;
        }
        if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
            String key = DynamicTaskUtil.buildTaskKey(_Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), Const.DYNAMIC_STATE_SENDING, Const.DYNA_FROM_ENABLE_SORT);
            ArrayList<Object> ms = new ArrayList<Object>();
            for (int i = 0; i < models.size(); i++) {
                ObjectWrapper wrapper = (ObjectWrapper) models.get(i);
                ms.add(wrapper.getObject());
            }
            ac.saveObject(ms, key);
            BroadcastManager.sendDynamicTaskBroadcast(_activity, DynamicTaskReceiver.ACTION_ADD_TASK, key);
            finish();
        } else {
            UIHelper.showLogin(this, false);
        }
    }

    @OnClick({R.id.addText, R.id.addImage, R.id.addVideo, R.id.addVoice, R.id.addAddress, R.id.addSchedule, R.id.addVote})
    public void clickAddType(View v) {
        switch (v.getId()) {
            case R.id.addText:
                UIHelper.showCreateDynamicText(this);
                break;
            case R.id.addImage:
                new ActionDialog(this).init(DialogActionData.build(null, null, new ActionData("立即拍照", R.drawable.ic_by_camera_selector), new ActionData("图库上传", R.drawable.ic_by_gallery_selector)))
                        .setOnActionClickListener(new OnActionClickListener() {

                            @Override
                            public void onActionClick(ActionDialog dialog, View View, int position) {
                                if (position == 0) {
                                    PhotoManager.getInstance().photo(System.currentTimeMillis() + "");
                                } else if (position == 1) {
                                    PhotoManager.getInstance().album(System.currentTimeMillis() + "");
                                }
                            }
                        }).show();

                break;
            case R.id.addVideo:
                UIHelper.showMediaRecord(_activity);
                break;
            case R.id.addVoice:
                UIHelper.showCreateDynamicVoice(this);
                break;
            case R.id.addAddress:
                UIHelper.showCreateDynamicAddress(this);
                break;
            case R.id.addSchedule:
                UIHelper.showCreateDynamicSchedule(this);
                break;
            case R.id.addVote:
                UIHelper.showCreateDynamicVote(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TEXT:
                    TextModel textModel = (TextModel) data.getExtras().getSerializable(BUNDLE_KEY_TEXT);
                    models.add(new ObjectWrapper(textModel, TPL_TEXT));
                    break;
//			case MediaRecorderActivity.REQUEST_CODE_FOR_VIDEO_PATH:
//				String videoPath = data.getStringExtra(MediaRecorderActivity.DATA_FOR_VIDEO_PATH);
//				String previewImagePath = data.getStringExtra(MediaRecorderActivity.DATA_FOR_PREVIEW_IMAGE_PATH);
//				VideoModel videoModel = new VideoModel();
//				videoModel.setItemViewType(TPL_VIDEO);
//				videoModel.setPublishType(PUBLISH_TYPE_VIDEO);
//				VideoModel.Content content = videoModel.getContent();
//				content.setFilename(videoPath);
//				content.setPreviewImage(previewImagePath);
//				models.d(videoModel);
//				break;
                case VideoRecordActivity.REQUEST_CODE_FOR_VIDEO_PATH:
                    String videoPath = data.getStringExtra(VideoRecordActivity.DATA_FOR_VIDEO_PATH);
                    String previewImagePath = data.getStringExtra(VideoRecordActivity.DATA_FOR_PREVIEW_IMAGE_PATH);
                    VideoModel videoModel = new VideoModel();
                    VideoModel.Content content = videoModel.getContent();
                    content.setFilename(videoPath);
                    content.setPreviewPic(previewImagePath);
                    models.add(new ObjectWrapper(videoModel, TPL_VIDEO));
                    break;
                case CreateDynamicVoiceActivity.REQUEST_VOICE:
                    VoiceModel voiceModel = (VoiceModel) data.getExtras().getSerializable(CreateDynamicVoiceActivity.BUNDLE_KEY_VOICE);
                    models.add(new ObjectWrapper(voiceModel, TPL_VOICE));
                    break;
                case SelectLocActivity.REQUEST_SELECT_LOC:
                    AddressInfo addressInfo = (AddressInfo) data.getExtras().getSerializable(SelectLocActivity.BUNDLE_KEY_ADDRESSINFO);
                    if (addressInfo.isValid()) {
                        AddressModel addressModel = new AddressModel();
                        addressModel.getContent().setAddress(addressInfo.getAddress());
                        addressModel.getContent().setLat(addressInfo.getLat());
                        addressModel.getContent().setLon(addressInfo.getLng());
                        models.add(new ObjectWrapper(addressModel, TPL_ADDRESS));
                    } else {
                        AppContext.showToast("您选择位置信息无效");
                    }
                    break;
                case REQUEST_VOTE:
                    VoteModel voteModel = (VoteModel) data.getExtras().getSerializable(BUNDLE_KEY_VOTE);
                    models.add(new ObjectWrapper(voteModel, TPL_VOTE));
                    break;
                case REQUEST_SCHEDULE:
                    ScheduleModel scheduleModel = (ScheduleModel) data.getExtras().getSerializable(BUNDLE_KEY_SCHEDULE);
                    models.add(new ObjectWrapper(scheduleModel, TPL_SCHEDULE));
                    break;
            }

            adapter.notifyDataSetChanged();
        }
        PhotoManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhotoManager.getInstance().onDestory();
    }

    @Override
    public void onPhoto(String tag, ArrayList<PhotoBean> photos) {
        ImageModel picsModel = null;
        for (Object obj : models) {
            if (obj instanceof ImageModel) {
                ImageModel picsModelTemp = (ImageModel) obj;
                if (tag.equals(picsModelTemp.getTag())) {
                    picsModel = picsModelTemp;
                    break;
                }
            }
        }

        // 不存在则新增
        if (picsModel == null) {
            picsModel = new ImageModel();
            picsModel.setTag(tag);
            Content content = picsModel.getContent();
            ArrayList<Pic> pics = new ArrayList<>();
            for (PhotoBean photo : photos) {
                Pic pic = new Pic();
                pic.setFilename(photo.getImagePath());
                int[] imageSize = Func.getImageSize(photo.getImagePath());
                pic.setW(imageSize[0] + "");
                pic.setH(imageSize[1] + "");
                pics.add(pic);
            }
            content.setPics(pics);
            models.add(new ObjectWrapper(picsModel, TPL_IMAGE));
            // 存在则修改
        } else {
            ArrayList<Pic> pics = picsModel.getContent().getPics();
            pics.clear();
            for (PhotoBean photo : photos) {
                Pic pic = new Pic();
                pic.setFilename(photo.getImagePath());
                int[] imageSize = Func.getImageSize(photo.getImagePath());
                pic.setW(imageSize[0] + "");
                pic.setH(imageSize[1] + "");
                pics.add(pic);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        showWaitDialog();
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        hideWaitDialog();
        if (res.isOK()) {
            AppContext.showToast("发布成功");
            ac.setProperty(Const.USER_DYNAMIC_COUNT, (Integer.parseInt(ac.getProperty(Const.USER_DYNAMIC_COUNT)) + 1) + "");
            finish();
        } else {
            ac.handleErrorCode(this, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        hideWaitDialog();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new ConfirmDialog(_activity).config("提示", "是否删除该条内容", "确定", new OnClickListener() {

            @Override
            public void onClick(View v) {
                models.remove(position);
                adapter.notifyDataSetChanged();
            }
        }).show();
        return true;
    }
}
