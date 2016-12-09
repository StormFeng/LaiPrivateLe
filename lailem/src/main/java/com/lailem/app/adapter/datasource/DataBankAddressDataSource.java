package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.GroupDatabaseBean;
import com.lailem.app.ui.databank.DataBankMapFragment;
import com.lailem.app.utils.Const;

import java.util.ArrayList;

public class DataBankAddressDataSource extends BaseListDataSource<Object> {

    private final String groupId;

    public DataBankAddressDataSource(Context context, String groupId) {
        super(context);
        this.groupId = groupId;
    }


    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        GroupDatabaseBean list = (GroupDatabaseBean) ApiClient.getApi().getGroupDatabase(groupId, AppContext.PAGE_SIZE + "", page + "", Const.DATABASE_TYPE_ADDRESS);

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        if (list.getDatabase() != null && list.getDatabase().size() > 0) {
            for (GroupDatabaseBean.DataBase dataBase : list.getDatabase()) {
                //添加时间分割
                if (page > FIRST_PAGE_NO) {

                }
                models.add(new ObjectWrapper(dataBase.getCreateTime(), BaseMultiTypeListAdapter.TPL_SECTION));
                for (int i = 0; i < dataBase.getDataList().size(); i++) {
                    models.add(new ObjectWrapper(dataBase.getDataList().get(i), DataBankMapFragment.TPL_ADDRESS));
                }
            }
        }

        hasMore = list.getDatabase() != null && list.getDatabase().size() == AppContext.PAGE_SIZE;
        this.page = page;
        return models;
    }

}
