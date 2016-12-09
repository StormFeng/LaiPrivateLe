package com.lailem.app.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.lailem.app.bean.Base;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.widget.astickyheader.PinnedSectionListView.PinnedSectionListAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.IViewHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class BaseMultiTypeListAdapter<Model> extends BaseListAdapter implements PinnedSectionListAdapter {

    public static final int TPL_SECTION = 0;

    private ArrayList<Class> itemViewClazzs;

    public BaseMultiTypeListAdapter(AbsListView absListView, Context context, ArrayList<Model> data, Class itemViewClazz, IViewHelper listViewHelper) {
        super(absListView, context, data, itemViewClazz, listViewHelper);
    }

    public BaseMultiTypeListAdapter(AbsListView absListView, Context context, IDataSource<Model> dataSource, Class itemViewClazz, IViewHelper listViewHelper) {
        super(absListView, context, dataSource, itemViewClazz, listViewHelper);
    }

    public BaseMultiTypeListAdapter(AbsListView absListView, Context context, IDataSource<Model> dataSource, ArrayList<Class> itemViewClazzs, IViewHelper listViewHelper) {
        this(absListView, context, dataSource, itemViewClazzs.get(0), listViewHelper);
        this.itemViewClazzs = itemViewClazzs;
    }

    public BaseMultiTypeListAdapter(AbsListView absListView, Context context, ArrayList<Model> data, ArrayList<Class> itemViewClazzs, IViewHelper listViewHelper) {
        this(absListView, context, data, itemViewClazzs.get(0), listViewHelper);
        this.itemViewClazzs = itemViewClazzs;
    }

    @Override
    public int getViewTypeCount() {
        return itemViewClazzs.size();
    }

    @Override
    public int getItemViewType(int position) {
        Base baseModel = (Base) getItem(position);
        return baseModel.getItemViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            try {
                BaseTpl baseTpl = (BaseTpl) itemViewClazzs.get(getItemViewType(position)).getConstructor(Context.class).newInstance(context);
                baseTpl.init(context, getItemViewType(position));
                convertView = baseTpl;
                baseTpl.config(this, data, dataSource, absListView, listViewHelper);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!(convertView instanceof BaseTpl)) {
            if (convertView.getClass().getName().equals("android.view.View")) {
            }
            return convertView;
        }
        BaseTpl view = (BaseTpl) convertView;
        view.setBean(getItem(position), position);
        return convertView;
    }

    @Override
    public boolean isItemViewTypePinned(int position) {
        if (position >= getCount()) {
            return false;
        }
        return getItemViewType(position) == TPL_SECTION;
    }

    public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {
            }
        }
        return null;
    }

}
