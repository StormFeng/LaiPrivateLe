package com.lailem.app.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;

import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.IViewHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class BaseListAdapter<Model> extends BaseAdapter implements IDataAdapter<ArrayList<Model>> {
    public static final int MODE_NORMAL = 0;
    public static final int MODE_EDIT = 1;
    protected Context context;
    protected ArrayList<Model> data;
    protected IDataSource<Model> dataSource;
    protected Class itemViewClazz;
    protected AbsListView absListView;
    protected Runnable runnable;
    protected IViewHelper listViewHelper;
    protected ArrayList<Integer> checkedPositions = new ArrayList<Integer>();
    protected int checkedPosition = -1;
    protected int mode = MODE_NORMAL;

    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;

    protected HashMap<String, Object> tags;//可用于保存临时数据的容器
    protected ArrayList<Object> extras;

    public <T> BaseListAdapter(AbsListView absListView, Context context, IDataSource<Model> dataSource, Class<T> itemViewClazz, IViewHelper listViewHelper) {
        this.absListView = absListView;
        this.context = context;
        this.dataSource = dataSource;
        this.data = dataSource.getResultList();
        this.itemViewClazz = itemViewClazz;
        this.listViewHelper = listViewHelper;
        initListener();
    }

    public <T> BaseListAdapter(AbsListView absListView, Context context, ArrayList<Model> data, Class<T> itemViewClazz, IViewHelper listViewHelper) {
        this.absListView = absListView;
        this.context = context;
        this.data = data;
        this.itemViewClazz = itemViewClazz;
        this.listViewHelper = listViewHelper;
        initListener();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Model getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            try {
                BaseTpl baseTpl = (BaseTpl) itemViewClazz.getConstructor(Context.class).newInstance(context);
                baseTpl.init(context, getItemViewType(position));
                convertView = baseTpl;
                baseTpl.config(this, data, dataSource, absListView, listViewHelper);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!(convertView instanceof BaseTpl)) {
            if ("android.view.View".equals(convertView.getClass().getName())) {
            }
            return convertView;
        }
        BaseTpl view = (BaseTpl) convertView;
        view.setBean(getItem(position), position);
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (listViewHelper != null) {
            if (getCount() == 0) {
                listViewHelper.getLoadView().showEmpty();
            } else {
                listViewHelper.getLoadView().restore();
            }
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void setData(ArrayList<Model> res, boolean isRefresh, boolean isPullToDownLoadMore) {
        if (isPullToDownLoadMore) {
            if (isRefresh) {
                this.data.addAll(dataSource.getRefreshInsertIndex(), res);
            }
        } else {
            if (isRefresh) {
                this.data.clear();
            }
            this.data.addAll(res);
        }
    }

    @Override
    public ArrayList<Model> getData() {
        return data;
    }

    public int getCheckedPosition() {
        return checkedPosition;
    }

    public ArrayList<Integer> getCheckedPositions() {
        return checkedPositions;
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    private void initListener() {
        absListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(parent, view, position, id);
                }
                try {
                    Method method = Class.forName(BaseTpl.class.getName()).getDeclaredMethod("onItemClick");
                    method.setAccessible(true);
                    method.invoke(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        absListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(parent, view, position, id);
                }
                try {
                    Method method = Class.forName(BaseTpl.class.getName()).getDeclaredMethod("onItemLongClick");
                    method.setAccessible(true);
                    method.invoke(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void putTag(String key, Object value) {
        if (tags == null) {
            tags = new HashMap<String, Object>();
        }
        tags.put(key, value);
    }

    public Object getTag(String key) {
        if (tags == null) {
            return null;
        }
        return tags.get(key);
    }


    public Object removeTag(String key) {
        if (tags == null) {
            return null;
        }
        return tags.remove(key);
    }

    public void addExtra(int index, Object value) {
        if (extras == null) {
            extras = new ArrayList<Object>();
        }
        extras.add(index, value);
    }

    public Object getExtra(int index) {
        if (extras == null) {
            return null;
        }
        return extras.get(index);
    }

    public Object removeExtra(Object value) {
        if (extras == null) {
            return null;
        }
        return extras.remove(value);
    }

    public Object removeExtra(int index) {
        if (extras == null) {
            return null;
        }
        return extras.remove(index);
    }

}
