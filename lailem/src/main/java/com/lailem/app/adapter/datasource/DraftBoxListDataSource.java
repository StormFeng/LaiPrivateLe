package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.ui.create_old.dynamic.model.AddressModel;
import com.lailem.app.ui.create_old.dynamic.model.ImageModel;
import com.lailem.app.ui.create_old.dynamic.model.ScheduleModel;
import com.lailem.app.ui.create_old.dynamic.model.TextModel;
import com.lailem.app.ui.create_old.dynamic.model.VideoModel;
import com.lailem.app.ui.create_old.dynamic.model.VoiceModel;
import com.lailem.app.ui.create_old.dynamic.model.VoteModel;
import com.lailem.app.ui.me.DraftBoxListActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.DynamicTaskUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by XuYang on 15/11/10.
 */
public class DraftBoxListDataSource extends BaseListDataSource<Object> {
    public DraftBoxListDataSource(Context context) {
        super(context);
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        ArrayList<DynamicTaskUtil.DynamicTask> tasks = new ArrayList<DynamicTaskUtil.DynamicTask>();
        ArrayList<String> fileNames = new ArrayList<String>(Arrays.asList(context.getFilesDir().list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.startsWith(Const.DYNAMIC_SUFFIX)
                        &&
                        (filename.endsWith(ac.getLoginUid() + "_" + Const.DYNAMIC_STATE_FAIL + "_" + Const.DYNA_FROM_DISABLE_SORT)
                                || filename.endsWith(ac.getLoginUid() + "_" + Const.DYNAMIC_STATE_FAIL + "_" + Const.DYNA_FROM_ENABLE_SORT))) {
                    return true;
                }
                return false;
            }
        })));
        for (String fileName : fileNames) {
            DynamicTaskUtil.DynamicTask task = DynamicTaskUtil.buildTask(fileName);
            if (task != null && Const.DYNAMIC_STATE_FAIL.equals(task.state)) {
                tasks.add(task);
            }
        }

        Collections.sort(tasks, new Comparator<DynamicTaskUtil.DynamicTask>() {
            @Override
            public int compare(DynamicTaskUtil.DynamicTask lhs, DynamicTaskUtil.DynamicTask rhs) {
                return (int) (rhs.time - lhs.time);
            }
        });

        for (int x = 0; x < tasks.size(); x++) {
            DynamicTaskUtil.DynamicTask task = tasks.get(x);
            //添加发送时间
            models.add(new ObjectWrapper(task.time, DraftBoxListActivity.TPL_DATE));
            if (Const.DYNA_FROM_DISABLE_SORT.equals(task.dynaFrom)) {
                //固定排序
                Object videoModel = null;
                boolean hasVideo = false;
                for (int j = 0; j < task.models.size(); j++) {
                    Object model = task.models.get(j);
                    if (model instanceof TextModel) {
                        models.add(new ObjectWrapper(model, DraftBoxListActivity.TPL_TEXT));
                        if (j < task.models.size() - 1) {
                            models.add(new ObjectWrapper("分割", DraftBoxListActivity.TPL_GAP));
                        }
                    } else if (model instanceof VoiceModel) {
                        models.add(new ObjectWrapper(model, DraftBoxListActivity.TPL_NEW_VOICE));
                        if (j < task.models.size() - 1) {
                            models.add(new ObjectWrapper("分割", DraftBoxListActivity.TPL_GAP));
                        }
                    } else if (model instanceof VideoModel) {
                        hasVideo = true;
                        videoModel = model;
                    } else if (model instanceof ImageModel) {
                        ImageModel imageModel = (ImageModel) model;
                        if (hasVideo || (imageModel.getContent() != null && imageModel.getContent().getPics() != null && imageModel.getContent().getPics().size() > 1)) {
                            if (hasVideo) {
                                hasVideo = false;
                            }
                            //九宫格
                            Object[] modelArr = new Object[]{videoModel, model};
                            models.add(new ObjectWrapper(modelArr, DraftBoxListActivity.TPL_VIDEOIMAGE));
                        } else {
                            //单个图片
                            models.add(new ObjectWrapper(model, DraftBoxListActivity.TPL_SINGLE_IMAGE));
                        }
                        if (j < task.models.size() - 1) {
                            models.add(new ObjectWrapper("分割", DraftBoxListActivity.TPL_GAP));
                        }
                    } else if (model instanceof AddressModel) {
                        if (hasVideo == true) {
                            hasVideo = false;
                            models.add(new ObjectWrapper(videoModel, DraftBoxListActivity.TPL_SINGLE_VIDEO));
                            models.add(new ObjectWrapper("分割", DraftBoxListActivity.TPL_GAP));
                        }
                        models.add(new ObjectWrapper(model, DraftBoxListActivity.TPL_NEW_ADDRESS));
                        if (j < task.models.size() - 1) {
                            models.add(new ObjectWrapper("分割", DraftBoxListActivity.TPL_GAP));
                        }
                    } else if (model instanceof ScheduleModel) {
                        models.add(new ObjectWrapper(model, DraftBoxListActivity.TPL_SCHEDULE));
                        if (j < task.models.size() - 1) {
                            models.add(new ObjectWrapper("分割", DraftBoxListActivity.TPL_GAP));
                        }
                    } else if (model instanceof VoteModel) {
                        models.add(new ObjectWrapper(model, DraftBoxListActivity.TPL_VOTE));
                        if (j < task.models.size() - 1) {
                            models.add(new ObjectWrapper("分割", DraftBoxListActivity.TPL_GAP));
                        }
                    }
                    if (j == task.models.size() - 1 && hasVideo == true) {
                        hasVideo = false;
                        models.add(new ObjectWrapper(videoModel, DraftBoxListActivity.TPL_SINGLE_VIDEO));
                    }
                }
            } else {
                for (int j = 0; j < task.models.size(); j++) {
                    Object model = task.models.get(j);
                    if (model instanceof TextModel) {
                        models.add(new ObjectWrapper(model, DraftBoxListActivity.TPL_TEXT));
                    } else if (model instanceof ImageModel) {
                        models.add(new ObjectWrapper(model, DraftBoxListActivity.TPL_IMAGE));
                    } else if (model instanceof VoiceModel) {
                        models.add(new ObjectWrapper(model, DraftBoxListActivity.TPL_VOICE));
                    } else if (model instanceof VideoModel) {
                        models.add(new ObjectWrapper(model, DraftBoxListActivity.TPL_VIDEO));
                    } else if (model instanceof AddressModel) {
                        models.add(new ObjectWrapper(model, DraftBoxListActivity.TPL_ADDRESS));
                    } else if (model instanceof ScheduleModel) {
                        models.add(new ObjectWrapper(model, DraftBoxListActivity.TPL_SCHEDULE));
                    } else if (model instanceof VoteModel) {
                        models.add(new ObjectWrapper(model, DraftBoxListActivity.TPL_VOTE));
                    }
                    if (j < task.models.size() - 1) {
                        models.add(new ObjectWrapper("分割线", DraftBoxListActivity.TPL_LINE));
                    }
                }
            }
            //添加actionbar
            models.add(new ObjectWrapper(task, DraftBoxListActivity.TPL_ACTIONBAR));
        }
        return models;
    }
}
