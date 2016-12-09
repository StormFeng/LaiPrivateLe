package com.lailem.app;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.lailem.app.adapter.OnPageChangeAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.jsonbean.personal.UserBean;
import com.lailem.app.share_ex.data.ShareConstants;
import com.lailem.app.share_ex.model.ILoginManager;
import com.lailem.app.share_ex.model.PlatformActionListener;
import com.lailem.app.share_ex.wechat.WechatLoginManager;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.CloudTagView;
import com.lailem.app.widget.DotIndicator;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 应用启动界面
 *
 * @author XuYang
 */
public class AppStart extends BaseActivity implements PlatformActionListener {
    private static final long DURATION = 800;

    private static String[] keyWordsArrOne;
    private static String[] keyWordsArrTwo;
    private static String[] keyWordsArrThree;

    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.indicator)
    DotIndicator indicator;

    private View pageOne;
    private View pageTwo;
    private View pageThree;
    private ArrayList<View> pages = new ArrayList<View>();

    private CloudTagView tagsOne;
    private CloudTagView tagsTwo;
    private CloudTagView tagsThree;

    private ILoginManager wechatLoginManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appstart);
        ButterKnife.bind(this);
        initViewPager();
        if (ac.isLogin()) {
            UIHelper.showMain(_activity, false);
            finish();
        }
    }


    @Override
    protected boolean isNeedTranslucentStatus() {
        return false;
    }

    @SuppressWarnings("deprecation")
    private void initViewPager() {
        pageOne = View.inflate(this, R.layout.view_appstart_page_1, null);
        pageTwo = View.inflate(this, R.layout.view_appstart_page_2, null);
        pageThree = View.inflate(this, R.layout.view_appstart_page_3, null);
        pages.add(pageOne);
        pages.add(pageTwo);
        pages.add(pageThree);

        pager.setAdapter(new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getCount() {
                if (pages != null && pages.size() > 0) {
                    return pages.size();
                } else {
                    return 0;
                }
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(pages.get(position));
                return pages.get(position);
            }
        });

        indicator.initIndicator(this, pager);

        tagsOne = (CloudTagView) pageOne.findViewById(R.id.tags);
        tagsTwo = (CloudTagView) pageTwo.findViewById(R.id.tags);
        tagsThree = (CloudTagView) pageThree.findViewById(R.id.tags);


        keyWordsArrOne = getResources().getStringArray(R.array.appstart_keywords_one);
        keyWordsArrTwo = getResources().getStringArray(R.array.appstart_keywords_two);
        keyWordsArrThree = getResources().getStringArray(R.array.appstart_keywords_three);

        tagsOne.configTags(keyWordsArrOne);
        tagsTwo.configTags(keyWordsArrTwo);
        tagsThree.configTags(keyWordsArrThree);


        pager.setOnPageChangeListener(new OnPageChangeAdapter() {
            @Override
            public void onPageSelected(int position) {
                indicator.changeIndiccator(position);
                switch (position) {
                    case 0:
                        tagsOne.start();
                        break;
                    case 1:
                        tagsTwo.start();
                        break;
                    case 2:
                        tagsThree.start();
                        break;
                }

            }
        });

        pager.setCurrentItem(0);
    }

    @OnClick(R.id.login)
    public void login() {
        UIHelper.showLogin(this, false);
        finish();
    }

    @OnClick(R.id.loginWeixin)
    public void loginWeixin() {
        if (!ac.isInitShareReady) {
            showWaitDialog();
            ac.initShare();
            hideWaitDialog();
        }
        wechatLoginManager = new WechatLoginManager(this);
        this.wechatLoginManager.login(this);
    }

    @OnClick(R.id.visitor)
    public void visitor() {
        UIHelper.showMain(this, false, true);
        finish();
    }

    @Override
    public void onBackPressed() {
        UIHelper.showMain(this, false);
        finish();
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
            UserBean userBean = (UserBean) res;

            AppContext.showToast("登录成功");
            if (Const.DATA_INTACT_NO.equals(userBean.getUserInfo().getDataIntact())) {
                UIHelper.showEditProfileActivity(_activity, userBean);
            } else {
                ac.saveUserInfo(userBean);
                if (ac.isNeedLoginCallback()) {
                    ac.excuteLoginCallback();
                } else {
                    UIHelper.showMain(this, true);
                }
                BroadcastManager.sendLoginBroadcast(_activity);
            }
            finish();

        } else {
            ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        hideWaitDialog();
    }

    @Override
    public void onComplete(HashMap<String, Object> userInfo) {
        String headBUrl = userInfo.get(ShareConstants.PARAMS_IMAGEURL).toString();
        String nickName = userInfo.get(ShareConstants.PARAMS_NICK_NAME).toString();
        String openId = userInfo.get(ShareConstants.PARAMS_USERID).toString();
        String birthday = null;
        String cityId = null;
        String provinceId = null;
        String sex = null;
        String unionId = userInfo.get(ShareConstants.PARAMS_UNIONID).toString();
        //性别
        String weixinSex = userInfo.get(ShareConstants.PARAMS_SEX).toString();
        if (!TextUtils.isEmpty(weixinSex)) {
            if ("1".equals(weixinSex)) {
                //男
                sex = Const.MALE;
            } else if ("2".equals(weixinSex)) {
                sex = Const.FEMALE;
            }
        }
        //省份和城市
        String provinceName = userInfo.get(ShareConstants.PARAMS_PRIVOINCE).toString();
        String cityName = userInfo.get(ShareConstants.PARAMS_CITY).toString();
        String[] ids = Func.getIdByName(provinceName, cityName);
        provinceId = ids[0];
        cityId = ids[1];
        ApiClient.getApi().loginThird(this, headBUrl, nickName, openId, "2", unionId, birthday, cityId, provinceId, sex);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onCancel() {

    }
}
