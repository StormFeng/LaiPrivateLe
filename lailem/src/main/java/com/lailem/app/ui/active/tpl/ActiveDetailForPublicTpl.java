package com.lailem.app.ui.active.tpl;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.R;
import com.lailem.app.adapter.AnimatorAdapter;
import com.lailem.app.api.ApiCallbackAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.ActiveInfoBean;
import com.lailem.app.jsonbean.activegroup.Member;
import com.lailem.app.jsonbean.personal.Contact;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.active.ActiveLocActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class ActiveDetailForPublicTpl extends BaseTpl<ObjectWrapper> {

    public static final String HAS_ZAN = "1";
    public static final String NOT_ZAN = "2";
    public static final String HAS_FAVOR = "1";
    public static final String NOT_FAVOR = "2";

    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.activeName)
    TextView activeName_tv;
    @Bind(R.id.activeType)
    TextView activeType_tv;
    @Bind(R.id.nameDate)
    TextView nameDate_tv;

    @Bind(R.id.activeDate)
    TextView activeDate_tv;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.contact_ll)
    View contact_ll;
    @Bind(R.id.contact)
    TextView contact_tv;

    @Bind(R.id.memberCount)
    TextView memberCount_tv;
    @Bind(R.id.femaleCount)
    TextView femaleCount_tv;
    @Bind(R.id.maleCount)
    TextView maleCount_tv;
    @Bind(R.id.memberLayout)
    LinearLayout memberLayout;

    @Bind(R.id.detail)
    TextView detail_tv;
    @Bind(R.id.intro)
    WebView intro_wv;
    @Bind(R.id.addfavor)
    TextView addFavor_tv;
    @Bind(R.id.addZan)
    TextView addZan_tv;

    @Bind(R.id.dynamicCount)
    TextView dynamicCount_tv;

    private View addZanBig;
    private View addFavorBig;

    private ActiveInfoBean.ActiveInfo bean;
    private int position;
    private ObjectWrapper wrapper;

    public ActiveDetailForPublicTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_active_detail_for_public;
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
    public void setBean(ObjectWrapper wrapper, int position) {
        this.wrapper = wrapper;
        this.bean = (ActiveInfoBean.ActiveInfo) wrapper.getObject();
        this.position = position;

        String avatarUrl = ApiClient.getFileUrl(bean.getCreatorInfo().getHeadSPicName());
        if (Func.checkImageTag(avatarUrl, avatar_iv)) {
            Glide.with(_activity).load(avatarUrl).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(_activity), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(avatar_iv);
        }
        activeName_tv.setText(bean.getName());
        activeType_tv.setText(bean.getTypeName());
        nameDate_tv.setText(bean.getRemark() + " 创建于" + Func.formatTime3(bean.getCreateTime()));


        activeDate_tv.setText(Func.formatTime2(bean.getStartTime()));
        address_tv.setText(bean.getAddress());
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

        int maleCount = Integer.parseInt(bean.getManCount());
        int femaleCount = Integer.parseInt(bean.getWomenCount());
        int memberCount = Integer.parseInt(bean.getCurrCount());
        memberCount_tv.setText("(" + memberCount + ")");
        maleCount_tv.setText(maleCount + "");
        femaleCount_tv.setText(femaleCount + "");
        ArrayList<Member> list = bean.getMemberList();
        memberLayout.removeAllViews();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Member member = list.get(i);
                ImageView imageView = (ImageView) View.inflate(_activity, R.layout.view_member_item, null);
                if (Func.checkImageTag(member.getHeadSPicName(), imageView)) {
                    Glide.with(_activity).load(ApiClient.getFileUrl(member.getHeadSPicName())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(imageView);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) TDevice.dpToPixel(30f), (int) TDevice.dpToPixel(30f));
                params.rightMargin = (int) TDevice.dpToPixel(10f);
                memberLayout.addView(imageView, params);
                if ((TDevice.dpToPixel(30 * (i + 2)) + TDevice.dpToPixel(10 * (i + 1)) + TDevice.dpToPixel(110)) > (TDevice.getScreenWidth())) {
                    break;
                }
            }
        }


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


        dynamicCount_tv.setText("(" + bean.getDynamicCount() + ")");

    }

    @OnClick({R.id.avatar})
    public void clickAvatar() {
        UIHelper.showMemberInfoAlone(_activity, bean.getCreatorInfo().getId());
    }

    @OnClick(R.id.address_ll)
    public void clickAddress() {
        UIHelper.showActiveLoc(_activity, bean.getLat(), bean.getLon(), bean.getAddress(), ActiveLocActivity.TYPE_ACTIVE);
    }

    @OnClick(R.id.call)
    public void clickCall() {
        if (bean.getContact() != null && bean.getContact().size() > 0) {
            Uri uri = Uri.parse("tel:" + bean.getContact().get(0).getPhone());
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            _activity.startActivity(intent);
        }
    }

    @OnClick(R.id.members_ll)
    public void clickMembers() {
        UIHelper.showActiveMemberList(_activity, bean.getName(), bean.getId(), bean.getRoleType(), bean.getJoinNeedContact());
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

    @OnClick(R.id.showDynamics)
    public void showDyanmics() {
        UIHelper.showActiveDynamicList(_activity, bean.getId());
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
                            setBean(wrapper, position);
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
                        setBean(wrapper, position);
                    }

                    public void onApiSuccess(Result res, String tag) {
                        if (res.isOK()) {
                            ac.setProperty(Const.USER_COLLECT_COUNT, (Integer.parseInt(ac.getProperty(Const.USER_COLLECT_COUNT)) + 1) + "");
                        }
                    }

                }, ac.getLoginUid(), bean.getId(), "2");
            }

            setBean(wrapper, position);
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
                            setBean(wrapper, position);
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
                        setBean(wrapper, position);
                    }

                }, ac.getLoginUid(), bean.getId(), "1");
            }

            setBean(wrapper, position);
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
}
