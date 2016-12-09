package com.lailem.app.ui.me;

import android.content.pm.PackageManager;
import android.os.Bundle;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.adapter.datasource.ContacsDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.base.BaseSectionListActivity;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.ContactAlphaSection;
import com.lailem.app.tpl.ContactTpl;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.IndexBar;
import com.lailem.app.widget.IndexBar.OnIndexChangeListener;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactsActivity extends BaseSectionListActivity<Object> implements OnIndexChangeListener {

    public static final int TPL_CONTACT = 1;

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.indexBar)
    IndexBar indexBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        listViewHelper.refresh();
        if (!isHasPermission()) {
            AppContext.showToast("无法获取手机通讯录,请检查安全设置并开启");
            finish();
        }
    }

    /**
     * 判断是否有获取联系人权限
     *
     * @return
     */
    private boolean isHasPermission() {
        PackageManager pkm = _activity.getPackageManager();
        String packageName = _activity.getPackageName();
        boolean has_permission = (PackageManager.PERMISSION_GRANTED == pkm.checkPermission("android.permission.READ_CONTACTS", "com.lailem.app"));
        if (has_permission) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_me_contacts;
    }

    private void initView() {
        topbar.setTitle("联系人").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        indexBar.setListener(this);
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new ContacsDataSource(this);
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(BaseMultiTypeListAdapter.TPL_SECTION, ContactAlphaSection.class);
        tpls.add(TPL_CONTACT, ContactTpl.class);
        return tpls;
    }

    @Override
    public void onIndexChange(char c) {
        for (int i = 1; i < resultList.size(); i++) {
            Object object = resultList.get(i);
            if (object instanceof ObjectWrapper && ((ObjectWrapper) object).getObject().toString().equalsIgnoreCase(c + "")) {
                listView.setSelection(i);
                break;
            }
        }
    }

}
