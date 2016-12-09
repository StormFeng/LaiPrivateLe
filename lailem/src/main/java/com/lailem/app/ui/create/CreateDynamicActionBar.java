package com.lailem.app.ui.create;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.bean.AddressInfo;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.listenser.OnPhotoListener;
import com.lailem.app.ui.create_old.dynamic.model.AddressModel;
import com.lailem.app.ui.create_old.dynamic.model.ImageModel;
import com.lailem.app.ui.create_old.dynamic.model.VideoModel;
import com.lailem.app.ui.group.SelectLocActivity;
import com.lailem.app.ui.qupai.VideoRecordActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XuYang on 15/12/16.
 */
public class CreateDynamicActionBar extends LinearLayout implements OnChangeListener {
    @Bind(R.id.gallery)
    TextView gallery_cb;
    @Bind(R.id.photo)
    TextView photo_cb;
    @Bind(R.id.video)
    TextView video_cb;
    @Bind(R.id.voice)
    TextView voice_cb;
    @Bind(R.id.location)
    TextView location_cb;

    @Bind(R.id.galleryBadge)
    TextView galleryBadge;
    @Bind(R.id.videoBadge)
    TextView videoBadge;
    @Bind(R.id.voiceBadge)
    TextView voiceBadge;
    @Bind(R.id.locationBadge)
    TextView locationBadge;

    @Bind(R.id.viewFlipper)
    ViewFlipper viewFlipper;
    @Bind(R.id.imageArea)
    ImageArea imageArea;
    @Bind(R.id.videoArea)
    VideoArea videoArea;
    @Bind(R.id.voiceArea)
    VoiceArea voiceArea;
    @Bind(R.id.locationArea)
    LocationArea locationArea;

    private int color_n = Color.parseColor("#606060");
    private int color_c = Color.parseColor("#E56032");


    public CreateDynamicActionBar(Context context) {
        super(context);
        init(context);
    }

    public CreateDynamicActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CreateDynamicActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CreateDynamicActionBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_create_dynamic_action_bar, this);
        ButterKnife.bind(this);
        imageArea.setOnChangeListener(this);
        videoArea.setOnChangeListener(this);
        voiceArea.setOnChangeListener(this);
        locationArea.setOnChangeListener(this);

    }

    @OnClick({R.id.gallery_ll, R.id.photo_ll, R.id.video_ll, R.id.voice_ll, R.id.location_ll})
    public void clickAction(View v) {
        switch (v.getId()) {
            case R.id.gallery_ll:
                gallery_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_gallery_c), null, null);
                gallery_cb.setTextColor(color_c);
                video_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_video_n), null, null);
                video_cb.setTextColor(color_n);
                voice_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_voice_n), null, null);
                voice_cb.setTextColor(color_n);
                location_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_location_n), null, null);
                location_cb.setTextColor(color_n);
                viewFlipper.setDisplayedChild(0);
                break;
            case R.id.photo_ll:
                ImageModel model = imageArea.getModel();
                if (model != null && model.getContent().getPics() != null && model.getContent().getPics().size() == 9) {
                    AppContext.showToast("最多可发表9张照片");
                    return;
                }
                PhotoManager.getInstance().photo(Const.PHOTO_TAG);
                gallery_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_gallery_c), null, null);
                gallery_cb.setTextColor(color_c);
                video_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_video_n), null, null);
                video_cb.setTextColor(color_n);
                voice_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_voice_n), null, null);
                voice_cb.setTextColor(color_n);
                location_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_location_n), null, null);
                location_cb.setTextColor(color_n);
                viewFlipper.setDisplayedChild(0);
                break;
            case R.id.video_ll:
                gallery_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_gallery_n), null, null);
                gallery_cb.setTextColor(color_n);
                video_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_video_c), null, null);
                video_cb.setTextColor(color_c);
                voice_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_voice_n), null, null);
                voice_cb.setTextColor(color_n);
                location_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_location_n), null, null);
                location_cb.setTextColor(color_n);
                viewFlipper.setDisplayedChild(1);
                if (videoArea.getModel() == null) {
                    UIHelper.showVideoRecord((Activity) getContext());
                }
                break;
            case R.id.voice_ll:
                gallery_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_gallery_n), null, null);
                gallery_cb.setTextColor(color_n);
                video_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_video_n), null, null);
                video_cb.setTextColor(color_n);
                voice_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_voice_c), null, null);
                voice_cb.setTextColor(color_c);
                location_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_location_n), null, null);
                location_cb.setTextColor(color_n);
                viewFlipper.setDisplayedChild(2);
                break;
            case R.id.location_ll:
                gallery_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_gallery_n), null, null);
                gallery_cb.setTextColor(color_n);
                video_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_video_n), null, null);
                video_cb.setTextColor(color_n);
                voice_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_voice_n), null, null);
                voice_cb.setTextColor(color_n);
                location_cb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_c_d_location_c), null, null);
                location_cb.setTextColor(color_c);
                viewFlipper.setDisplayedChild(3);
                if (locationArea.getModel() == null) {
                    UIHelper.showSelectLoc(getContext());
                }
                break;
        }
        Activity activity = (Activity) getContext();
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void toggleHasPic(int picCount) {
        if (picCount == 0) {
            galleryBadge.setVisibility(GONE);
        } else {
            galleryBadge.setText(picCount + "");
            galleryBadge.setVisibility(VISIBLE);
        }
    }

    private void toggleHasVideo(boolean has) {
        if (has) {
            videoBadge.setVisibility(VISIBLE);
        } else {
            videoBadge.setVisibility(GONE);
        }
    }

    private void toggleHasVoice(boolean has) {
        if (has) {
            voiceBadge.setVisibility(VISIBLE);
        } else {
            voiceBadge.setVisibility(GONE);
        }
    }

    private void toggleHasLocation(boolean has) {
        if (has) {
            locationBadge.setVisibility(VISIBLE);
        } else {
            locationBadge.setVisibility(GONE);
        }
    }

    public void toggleContentArea(boolean show) {
        if (show) {
            viewFlipper.setVisibility(VISIBLE);
        } else {
            viewFlipper.setVisibility(GONE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case VideoRecordActivity.REQUEST_CODE_FOR_VIDEO_PATH:
                    String videoPath = data.getStringExtra(VideoRecordActivity.DATA_FOR_VIDEO_PATH);
                    String previewImagePath = data.getStringExtra(VideoRecordActivity.DATA_FOR_PREVIEW_IMAGE_PATH);
                    String duration = data.getStringExtra(VideoRecordActivity.DATA_FOR_DURATION);
                    VideoModel videoModel = new VideoModel();
                    VideoModel.Content content = videoModel.getContent();
                    content.setFilename(videoPath);
                    content.setDuration(duration + "");
                    content.setPreviewPic(previewImagePath);
                    videoArea.render(videoModel);
                    break;
                case SelectLocActivity.REQUEST_SELECT_LOC:
                    AddressInfo addressInfo = (AddressInfo) data.getExtras().getSerializable(SelectLocActivity.BUNDLE_KEY_ADDRESSINFO);
                    if (addressInfo.isValid()) {
                        AddressModel addressModel = new AddressModel();
                        addressModel.getContent().setAddress(addressInfo.getAddress());
                        addressModel.getContent().setLat(addressInfo.getLat());
                        addressModel.getContent().setLon(addressInfo.getLng());
                        locationArea.render(addressModel);
                    } else {
                        AppContext.showToast("您选择位置信息无效");
                    }
                    break;
            }
        }
    }

    @Override
    public void onChangeListener(View v, boolean has, int count) {
        switch (v.getId()) {
            case R.id.imageArea:
                toggleHasPic(count);
                break;
            case R.id.videoArea:
                toggleHasVideo(has);
                break;
            case R.id.voiceArea:
                toggleHasVoice(has);
                break;
            case R.id.locationArea:
                toggleHasLocation(has);
                break;
        }
    }

    public ArrayList<Object> buildModels() {

        //sort： 文字 语音 视频 图片 位置
        ArrayList<Object> objects = new ArrayList<Object>();
        //语音
        Object voiceObj = voiceArea.getModel();
        if (voiceObj != null) {
            objects.add(voiceObj);
        }
        //视频
        Object videoObj = videoArea.getModel();
        if (videoObj != null) {
            objects.add(videoObj);
        }
        //图片
        Object imageObj = imageArea.getModel();
        if (imageObj != null) {
            objects.add(imageObj);
        }
        //位置
        Object locationObj = locationArea.getModel();
        if (locationObj != null) {
            objects.add(locationObj);
        }
        return objects;
    }

    public OnPhotoListener getOnPhotoListener() {
        return imageArea;
    }
}