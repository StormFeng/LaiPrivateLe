package com.lailem.app.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiCallbackAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.Result;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.DynamicTaskReceiver;
import com.lailem.app.jsonbean.activegroup.Pic;
import com.lailem.app.ui.create_old.dynamic.model.ImageModel;
import com.lailem.app.ui.create_old.dynamic.model.VideoModel;
import com.lailem.app.ui.create_old.dynamic.model.VoiceModel;
import com.socks.library.KLog;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 更新下载服务
 */
public class DynamicTaskUtil {
    private AppContext ac;
    public static final int STATE_ADD = 1;
    public static final int STATE_REMOVE = 2;

    private NotificationManager mNotifyManager;
    private static int notifyId = 1;

    private static DynamicTaskUtil uploadDynamicUtil;

    private DynamicTaskUtil() {
        ac = AppContext.getInstance();
        if (mNotifyManager == null) {
            mNotifyManager =
                    (NotificationManager) ac.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    public static DynamicTaskUtil getInstance() {
        if (uploadDynamicUtil == null) {
            uploadDynamicUtil = new DynamicTaskUtil();
        }
        return uploadDynamicUtil;
    }

    public synchronized void handleTask(String key, int state) {
        switch (state) {
            case STATE_ADD:
                DynamicTask task = buildTask(key);
                if (task != null) {
                    ++notifyId;
                    task.notifyIdFailure = notifyId;
                    ++notifyId;
                    task.notifyIdSend = notifyId;
                    ++notifyId;
                    task.notifyIdSuccess = notifyId;
                    sendDynamic(task);
                }
                break;
            case STATE_REMOVE:

                break;
        }

    }

    private void sendDynamic(final DynamicTask task) {
        if (task.models == null || task.models.size() == 0) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object[] objects = null;
                if (Const.DYNA_FROM_DISABLE_SORT.equals(task.dynaFrom)) {
                    objects = newGenerateContentListAndFileKey(task.models);
                } else {
                    objects = generateContentListAndFileKey(task.models);
                }

                String contentList = objects[0].toString();
                KLog.e("contentList=" + contentList);
                ArrayList<File> fileKey = (ArrayList<File>) objects[1];
                ApiClient.getApi().dynamic(new ApiCallbackAdapter() {
                    @Override
                    public void onApiStart(String tag) {
                        super.onApiStart(tag);
                        showUpLoadNotificaiton(task);
                    }

                    @Override
                    public void onApiSuccess(Result res, String tag) {
                        super.onApiSuccess(res, tag);
                        if (res.isOK()) {
                            ac.setProperty(Const.USER_DYNAMIC_COUNT, (Integer.parseInt(ac.getProperty(Const.USER_DYNAMIC_COUNT)) + 1) + "");
                            ac.deleteFile(task.key);
                            AppContext.showToast("发表成功");
                            showUploadSuccessNotification(task);
                            if (DynamicTaskUtil.getDynamicTaskCount(ac) == 0) {
                                BroadcastManager.sendDynamicTaskBroadcast(ac, DynamicTaskReceiver.ACTION_TASK_EMPTY, task.key);
                            }
                        } else {
                            showUpLoadErrorNotification(task);
                            changeTaskState(ac, task, Const.DYNAMIC_STATE_FAIL);
                            BroadcastManager.sendDynamicTaskBroadcast(ac, DynamicTaskReceiver.ACTION_TASK_FAIL, task.key);
                            ac.handleErrorCode(ac, res.errorCode, res.errorInfo);
                        }
                        mNotifyManager.cancel(task.notifyIdSend);
                    }

                    @Override
                    protected void onApiError(String tag) {
                        super.onApiError(tag);
                        showUpLoadErrorNotification(task);
                        changeTaskState(ac, task, Const.DYNAMIC_STATE_FAIL);
                        mNotifyManager.cancel(task.notifyIdSend);
                        BroadcastManager.sendDynamicTaskBroadcast(ac, DynamicTaskReceiver.ACTION_TASK_FAIL, task.key);
                    }
                }, ac.getLoginUid(), contentList, fileKey, task.groupId, task.dynaFrom);
            }
        }).start();
    }

    /**
     * 显示上传中通知
     *
     * @param task
     */
    private void showUpLoadNotificaiton(DynamicTask task) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ac);
        mBuilder.setContentTitle("发表动态")
                .setContentText("正在发表")
                .setProgress(0, 0, true)
                .setSmallIcon(R.drawable.logo_square)
                .setWhen(System.currentTimeMillis())
                .setTicker("动态发表中")
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MAX);
        mNotifyManager.notify(task.notifyIdSend, mBuilder.build());
    }

    /**
     * 显示上传成功通知
     *
     * @param task
     */
    private void showUploadSuccessNotification(DynamicTask task) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ac);
        mBuilder.setContentTitle("发表动态")
                .setContentText("发表成功")
                .setProgress(0, 0, false)
                .setSmallIcon(R.drawable.logo_square)
                .setWhen(System.currentTimeMillis())
                .setTicker("发表成功")
                .setAutoCancel(true);
        mNotifyManager.notify(task.notifyIdSuccess, mBuilder.build());
        cancelNotification(task.notifyIdSuccess);
    }

    /**
     * 显示上传失败通知
     *
     * @param task
     */
    private void showUpLoadErrorNotification(DynamicTask task) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ac);
        mBuilder.setContentTitle("发表动态")
                .setContentText("发表失败")
                .setProgress(0, 0, false)
                .setSmallIcon(R.drawable.logo_square)
                .setWhen(System.currentTimeMillis())
                .setTicker("发表失败")
                .setAutoCancel(true);
        mNotifyManager.notify(task.notifyIdFailure, mBuilder.build());
        cancelNotification(task.notifyIdFailure);
    }

    /**
     * 关闭通知
     */
    private void cancelNotification(final int notifyId) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mNotifyManager.cancel(notifyId);
            }
        }, 5000);
    }

    /**
     * 生成动态文件名称
     *
     * @param groupId
     * @param state
     * @return
     */
    public static String buildTaskKey(String groupId, String state, String dynaFrom) {
        //dynamic_afadgdfgdg_123243242_1_dfafwefw  dynamic开头，中间为groupId  然后为时间戳 然后数字1：发送失败 2:发送中 3:发送成功 最后为useId
        return "dynamic_" + groupId + "_" + System.currentTimeMillis() + "_" + AppContext.getInstance().getLoginUid() + "_" + state + "_" + dynaFrom;
    }

    /**
     * 组装上传任务
     *
     * @param key
     * @return
     */
    public static DynamicTask buildTask(String key) {
        String[] strArr = key.split("_");
        DynamicTask task = new DynamicTask();
        task.key = key;
        task.groupId = strArr[1];
        task.models = (ArrayList<Object>) AppContext.getInstance().readObject(key);
        long time = Long.parseLong(strArr[2]);
        task.time = time;
        task.userId = strArr[3];
        task.state = strArr[4];
        task.dynaFrom = strArr[5];
        return task;
    }

    /**
     * 修改任务状态
     *
     * @param context
     * @param task
     * @param state
     * @return
     */
    public static void changeTaskState(final Context context, final DynamicTask task, final String state) {
        if (TextUtils.isEmpty(task.key) || !new File(context.getFilesDir() + File.separator + task.key).exists()) {
            return;
        }
        String filePath = context.getFilesDir() + File.separator + task.key;
        String[] arr = task.key.split("_");
        String newFilePath = context.getFilesDir() + File.separator + arr[0] + "_" + arr[1] + "_" + arr[2] + "_" + arr[3] + "_" + state + "_" + arr[5];
        if (state == Const.DYNAMIC_STATE_FAIL) {
            newFilePath = context.getFilesDir() + File.separator + arr[0] + "_" + arr[1] + "_" + System.currentTimeMillis() + "_" + arr[3] + "_" + state + "_" + arr[5];
        }
        FileUtils.reName(filePath, newFilePath);
    }


    public static int getDynamicTaskCount(Context context) {
        ArrayList<String> fileNames = new ArrayList<String>(Arrays.asList(context.getFilesDir().list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.startsWith(Const.DYNAMIC_SUFFIX)
                        &&
                        (filename.endsWith(AppContext.getInstance().getLoginUid() + "_" + Const.DYNAMIC_STATE_FAIL + "_" + Const.DYNA_FROM_DISABLE_SORT)
                                || filename.endsWith(AppContext.getInstance().getLoginUid() + "_" + Const.DYNAMIC_STATE_FAIL + "_" + Const.DYNA_FROM_ENABLE_SORT))) {
                    return true;
                }
                return false;
            }
        })));
        if (fileNames == null) {
            return 0;
        } else {
            return fileNames.size();
        }
    }

    public static void resetDynamicTask(Context context) {
        ArrayList<String> fileNames = new ArrayList<String>(Arrays.asList(context.getFilesDir().list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.startsWith(Const.DYNAMIC_SUFFIX) && filename.endsWith(AppContext.getInstance().getLoginUid() + "_" + Const.DYNAMIC_STATE_SENDING)) {
                    return true;
                }
                return false;
            }
        })));
        if (fileNames != null) {
            for (String fileName : fileNames) {
                String filePath = context.getFilesDir() + File.separator + fileName;
                String[] arr = fileName.split("_");
                String newFilePath = context.getFilesDir() + File.separator + arr[0] + "_" + arr[1] + "_" + arr[2] + "_" + arr[3] + "_" + Const.DYNAMIC_STATE_FAIL + "_" + arr[5];
                FileUtils.reName(filePath, newFilePath);
            }
        }
    }

    /**
     * 动态任务
     */
    public static class DynamicTask implements Serializable {
        public String key;
        public ArrayList<Object> models;
        public String groupId;
        public long time;
        public String state;
        public String userId;
        public int notifyIdSend;
        public int notifyIdSuccess;
        public int notifyIdFailure;
        public String dynaFrom;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DynamicTask task = (DynamicTask) o;

            return key.equals(task.key);

        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }


    public static Object[] generateContentListAndFileKey(ArrayList<Object> models) {
        Gson gson = new Gson();
        ArrayList<File> fileKey = new ArrayList<File>();

        String tempFileName = String.valueOf(System.currentTimeMillis());
        AppContext.getInstance().saveObject(models, tempFileName);
        ArrayList<Object> modelsCloned = (ArrayList<Object>) AppContext.getInstance().readObject(tempFileName);
        ArrayList<Object> contentModels = new ArrayList<Object>();
        for (Object obj : modelsCloned) {
            if (obj instanceof ImageModel) {
                ImageModel model = (ImageModel) obj;
                ImageModel.Content content = model.getContent();
                ArrayList<Pic> pics = content.getPics();
                for (Pic pic : pics) {
                    File file = new File(pic.getFilename());
                    fileKey.add(file);
                    String fileName = file.getName();
                    pic.setFilename(fileName);
                    String imageType = ImageUtils.getImageType(file);
                    if ("image/gif".equals(imageType)) {
                        if (!fileName.endsWith(".gif")) {
                            String newFileName = fileName.substring(0, fileName.indexOf(".")) + ".gif";
                            file.renameTo(new File(newFileName));
                            fileName = newFileName;
                        }
                        pic.setgFilename(fileName.replace(".", "G."));
                        pic.settFilename(fileName.replace(".", "T."));
                        pic.setW(fileName.replace(".gif", "W"));
                        pic.setH(fileName.replace(".gif", "H"));
                        pic.setTw(fileName.replace(".gif", "TW"));
                        pic.setTh(fileName.replace(".gif", "TH"));
                    } else {
                        pic.settFilename(fileName.replace(".", "T."));
                        pic.setW(fileName.replace(".jpg", "W"));
                        pic.setH(fileName.replace(".jpg", "H"));
                        pic.setTw(fileName.replace(".jpg", "TW"));
                        pic.setTh(fileName.replace(".jpg", "TH"));
                    }

                }
            } else if (obj instanceof VideoModel) {
                VideoModel model = (VideoModel) obj;
                VideoModel.Content content = model.getContent();
                File file = new File(content.getFilename());
                fileKey.add(file);
                content.setFilename(file.getName());
                File file1 = new File(content.getPreviewPic());
                fileKey.add(file1);
                content.setPreviewPic(file1.getName());
            } else if (obj instanceof VoiceModel) {
                VoiceModel model = (VoiceModel) obj;
                VoiceModel.Content content = model.getContent();
                File file = new File(content.getFilename());
                fileKey.add(file);
                content.setFilename(file.getName());
            }
            contentModels.add(obj);
        }
        String contentList = gson.toJson(contentModels);
        KLog.json(contentList);
        return new Object[]{contentList, fileKey};
    }

    public static Object[] newGenerateContentListAndFileKey(ArrayList<Object> models) {
        Gson gson = new Gson();
        ArrayList<File> fileKey = new ArrayList<File>();

        String tempFileName = String.valueOf(System.currentTimeMillis());
        AppContext.getInstance().saveObject(models, tempFileName);
        ArrayList<Object> modelsCloned = (ArrayList<Object>) AppContext.getInstance().readObject(tempFileName);
        ArrayList<Object> contentModels = new ArrayList<Object>();
        for (Object obj : modelsCloned) {
            if (obj instanceof ImageModel) {
                ImageModel model = (ImageModel) obj;
                ImageModel.Content content = model.getContent();
                ArrayList<Pic> pics = content.getPics();
                for (Pic pic : pics) {
                    File file = new File(pic.getFilename());
                    fileKey.add(file);
                    String fileName = file.getName();
                    pic.setFilename(fileName);
                    String imageType = ImageUtils.getImageType(file);
                    if ("image/gif".equals(imageType)) {
                        if (!fileName.endsWith(".gif")) {
                            String newFileName = fileName.substring(0, fileName.indexOf(".")) + ".gif";
                            file.renameTo(new File(newFileName));
                            fileName = newFileName;
                        }
                        pic.setgFilename(fileName.replace(".", "G."));
                        pic.settFilename(fileName.replace(".", "T."));
                        pic.setW(fileName.replace(".gif", "W"));
                        pic.setH(fileName.replace(".gif", "H"));
                        pic.setTw(fileName.replace(".gif", "TW"));
                        pic.setTh(fileName.replace(".gif", "TH"));
                    } else {
                        pic.settFilename(fileName.replace(".", "T."));
                        pic.setW(fileName.replace(".jpg", "W"));
                        pic.setH(fileName.replace(".jpg", "H"));
                        pic.setTw(fileName.replace(".jpg", "TW"));
                        pic.setTh(fileName.replace(".jpg", "TH"));
                    }

                }
            } else if (obj instanceof VideoModel) {
                VideoModel model = (VideoModel) obj;
                VideoModel.Content content = model.getContent();
                File file = new File(content.getFilename());
                fileKey.add(file);
                content.setFilename(file.getName());
                File file1 = new File(content.getPreviewPic());
                fileKey.add(file1);
                content.setPreviewPic(file1.getName());
            } else if (obj instanceof VoiceModel) {
                VoiceModel model = (VoiceModel) obj;
                VoiceModel.Content content = model.getContent();
                File file = new File(content.getFilename());
                fileKey.add(file);
                content.setFilename(file.getName());
            }
            contentModels.add(obj);
        }
        String contentList = gson.toJson(contentModels);
        KLog.json(contentList);
        return new Object[]{contentList, fileKey};
    }


}