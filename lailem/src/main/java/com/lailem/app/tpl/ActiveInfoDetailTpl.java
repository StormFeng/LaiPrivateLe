package com.lailem.app.tpl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.adapter.AnimatorAdapter;
import com.lailem.app.api.ApiCallbackAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.ActiveInfoBean.ActiveInfo;
import com.lailem.app.jsonbean.personal.Contact;
import com.lailem.app.ui.active.ActiveLocActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActiveQrCodeDialog;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ActiveInfoDetailTpl extends BaseTpl<Object> {

    public static final String HAS_ZAN = "1";
    public static final String NOT_ZAN = "2";
    public static final String HAS_FAVOR = "1";
    public static final String NOT_FAVOR = "2";

    @Bind(R.id.activeName)
    TextView activeName_tv;
    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.activeType)
    TextView activeType_tv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.date)
    TextView date_tv;

    @Bind(R.id.activeDate)
    TextView activeDate_tv;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.contact_ll)
    View contact_ll;
    @Bind(R.id.contact)
    TextView contact_tv;
    @Bind(R.id.detail)
    TextView detail_tv;
    @Bind(R.id.intro)
    WebView intro_wv;

    @Bind(R.id.addfavor)
    TextView addFavor_tv;
    @Bind(R.id.addZan)
    TextView addZan_tv;


    private ActiveInfo bean;
    private int position;

    private View addZanBig;
    private View addFavorBig;

    private ActiveQrCodeDialog qrCodeDialog;

    public ActiveInfoDetailTpl(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        super.initView();
        addZanBig = View.inflate(_activity, R.layout.view_add_zan_big, null);
        addFavorBig = View.inflate(_activity, R.layout.view_add_favor_big, null);
        FrameLayout decorView = (FrameLayout) _activity.getWindow().getDecorView();
        FrameLayout.LayoutParams zanParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        zanParams.gravity = Gravity.CENTER;
        decorView.addView(addZanBig, zanParams);
        FrameLayout.LayoutParams favorParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        favorParams.gravity = Gravity.CENTER;
        decorView.addView(addFavorBig, favorParams);

        addZanBig.setVisibility(INVISIBLE);
        addFavorBig.setVisibility(INVISIBLE);

        intro_wv.getSettings().setSupportZoom(false);
        intro_wv.getSettings().setBuiltInZoomControls(false);
        intro_wv.getSettings().setDisplayZoomControls(false);
        intro_wv.getSettings().setJavaScriptEnabled(true);
        intro_wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        intro_wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        intro_wv.setBackgroundColor(0);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_active_info_detail;
    }

    @Override
    public void setBean(Object object, int position) {
        this.bean = (ActiveInfo) object;
        this.position = position;

        activeName_tv.setText(bean.getName());
        activeType_tv.setText(bean.getTypeName());
        if (Func.checkImageTag(bean.getCreatorInfo().getHeadSPicName(), avatar_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(bean.getCreatorInfo().getHeadSPicName())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
        }
        name_tv.setText(bean.getRemark());
        date_tv.setText("创建于" + Func.formatTime3(bean.getCreateTime()));

        activeDate_tv.setText(Func.formatTime2(bean.getStartTime()));
        address_tv.setText(bean.getAddress());
        detail_tv.setText(bean.getIntro());

        String WEB_STYLE = "<style>"
                + "* {font-size:16px;line-height:20px;}"
                + " p {color:#202020;} "
                + "img {display: block; height: auto; max-width: 100%; margin: 0 auto 0 auto} "
                + "</style>";
        String body = WEB_STYLE + bean.getIntro();
        body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
        body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
        intro_wv.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);

        ArrayList<Contact> contactList = bean.getContact();

        if (contactList == null || contactList.size() == 0) {
            contact_ll.setVisibility(GONE);
        } else {
            contact_ll.setVisibility(VISIBLE);
            contact_tv.setText("联系人：" + contactList.get(0).getName());
            if (TextUtils.isEmpty(contactList.get(0).getName())) {
                contact_ll.setVisibility(GONE);
            }
        }

        addFavor_tv.setText(bean.getCollectCount());
        addZan_tv.setText(bean.getLikeCount());

        if (HAS_ZAN.equals(bean.getIsLiked())) {
            addZan_tv.setTextColor(getResources().getColor(R.color.orange));
            addZan_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_addzan_orange), null, null, null);
        } else if (NOT_ZAN.equals(bean.getIsLiked())) {
            addZan_tv.setTextColor(Color.parseColor("#999999"));
            addZan_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_addzan), null, null, null);
        }
        if (HAS_FAVOR.equals(bean.getIsCollected())) {
            addFavor_tv.setTextColor(getResources().getColor(R.color.orange));
            addFavor_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_addfavor_orange), null, null, null);
        } else if (NOT_FAVOR.equals(bean.getIsCollected())) {
            addFavor_tv.setTextColor(Color.parseColor("#999999"));
            addFavor_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_addfavor), null, null, null);
        }
    }

    @OnClick({R.id.addfavor, R.id.addZan})
    public void clickAction(View v) {
        switch (v.getId()) {
            case R.id.addfavor:
                toggleFavor();
                break;
            case R.id.addZan:
                toggleZan();
                break;
        }
    }

    public void refreshListView() {
        if (listViewHelper != null) {
            listViewHelper.refresh();
        }
    }


    private void toggleFavor() {
        if (ac.isLogin(this, "refreshListView")) {
            final int favorCount = Integer.parseInt(bean.getCollectCount());
            if (HAS_FAVOR.equals(bean.getIsCollected())) {
                if (favorCount > 0) {
                    bean.setCollectCount(favorCount - 1 + "");
                    bean.setIsCollected(NOT_FAVOR);
                    ApiClient.getApi().collect(new ApiCallbackAdapter() {
                        @Override
                        protected void onApiError(String tag) {
                            bean.setCollectCount(favorCount + 1 + "");
                            bean.setIsCollected(HAS_FAVOR);
                            setBean(bean, position);
                        }

                        public void onApiSuccess(Result res, String tag) {
                            if (res.isOK()) {
                                ac.setProperty(Const.USER_COLLECT_COUNT, (Integer.parseInt(ac.getProperty(Const.USER_COLLECT_COUNT)) - 1) + "");
                            }
                        }

                    }, ac.getLoginUid(), bean.getId(), "2");
                }
            } else if (NOT_FAVOR.equals(bean.getIsCollected())) {
                animIn(addFavorBig);
                bean.setCollectCount(favorCount + 1 + "");
                bean.setIsCollected(HAS_FAVOR);
                ApiClient.getApi().collect(new ApiCallbackAdapter() {
                    @Override
                    protected void onApiError(String tag) {
                        bean.setCollectCount(favorCount - 1 + "");
                        bean.setIsCollected(NOT_FAVOR);
                        setBean(bean, position);
                    }

                    public void onApiSuccess(Result res, String tag) {
                        if (res.isOK()) {
                            ac.setProperty(Const.USER_COLLECT_COUNT, (Integer.parseInt(ac.getProperty(Const.USER_COLLECT_COUNT)) + 1) + "");
                        }
                    }

                }, ac.getLoginUid(), bean.getId(), "2");
            }

            setBean(bean, position);
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }


    private void toggleZan() {
        if (ac.isLogin(this, "refreshListView")) {
            final int zanCount = Integer.parseInt(bean.getLikeCount());
            if (HAS_ZAN.equals(bean.getIsLiked())) {
                if (zanCount > 0) {
                    bean.setLikeCount(zanCount - 1 + "");
                    bean.setIsLiked(NOT_ZAN);
                    ApiClient.getApi().like(new ApiCallbackAdapter() {
                        @Override
                        protected void onApiError(String tag) {
                            bean.setLikeCount(zanCount + 1 + "");
                            bean.setIsLiked(HAS_ZAN);
                            setBean(bean, position);
                        }

                    }, ac.getLoginUid(), bean.getId(), "1");
                }
            } else if (NOT_ZAN.equals(bean.getIsLiked())) {
                animIn(addZanBig);
                bean.setLikeCount(zanCount + 1 + "");
                bean.setIsLiked(HAS_ZAN);
                ApiClient.getApi().like(new ApiCallbackAdapter() {
                    @Override
                    protected void onApiError(String tag) {
                        bean.setLikeCount(zanCount - 1 + "");
                        bean.setIsLiked(NOT_ZAN);
                        setBean(bean, position);
                    }

                }, ac.getLoginUid(), bean.getId(), "1");
            }

            setBean(bean, position);
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }

    private void animIn(final View v) {
        v.setVisibility(VISIBLE);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(v, "scaleX", 0.8f, 1.0f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(v, "scaleY", 0.8f, 1.0f);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(v, "alpha", 0.7f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animatorX, animatorY, animatorAlpha);
        set.setDuration(300).start();
        set.playSequentially(set);
        set.addListener(new AnimatorAdapter() {
            @Override
            public void onAnimationEnd(Animator arg0) {
                super.onAnimationEnd(arg0);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        v.setVisibility(INVISIBLE);
                    }
                }, 300);
            }
        });
    }


    @OnClick({R.id.avatar, R.id.name})
    public void clickAvatarOrName() {
        UIHelper.showMemberInfoAlone(_activity, bean.getCreatorInfo().getId());
    }


    @OnClick(R.id.qrCode_ll)
    public void showQrCode() {
        if (qrCodeDialog == null) {
            qrCodeDialog = new ActiveQrCodeDialog(_activity);
            qrCodeDialog.setGroupInfo(bean.getName(), bean.getStartTime(), bean.getId(), bean.getAddress());
        }
        qrCodeDialog.show();
    }

    @OnClick(R.id.call)
    public void clickCall() {
        if (bean.getContact() != null && bean.getContact().size() > 0) {
            Uri uri = Uri.parse("tel:" + bean.getContact().get(0).getPhone());
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            _activity.startActivity(intent);
        }
    }


    @OnClick(R.id.address_ll)
    public void clickAddress() {
        UIHelper.showActiveLoc(_activity, bean.getLat(), bean.getLon(), bean.getAddress(), ActiveLocActivity.TYPE_ACTIVE);
    }
}
