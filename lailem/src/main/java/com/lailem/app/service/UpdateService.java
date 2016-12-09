package com.lailem.app.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.utils.FileUtils;
import com.lailem.app.utils.TDevice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 更新下载服务
 */
public class UpdateService extends Service {
    public static final String BUNDLE_KEY_DOWN_URL = "down_url";

    private static final int TIMEOUT = 10 * 1000;
    private static final int DOWN_OK = 1;
    private static final int DOWN_ERROR = 0;

    private String app_name;
    private static String down_url;
    private static File apkfile;

    private NotificationManager notificationManager;
    private Notification notification;
    private PendingIntent pendingIntent;
    private RemoteViews contentView;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        app_name = getString(R.string.app_name);
        down_url = intent.getStringExtra(BUNDLE_KEY_DOWN_URL);
        apkfile = FileUtils.createFileWithSuffix(".apk", this);
        try {
            apkfile.createNewFile();
            createNotification();
            createThread();
        } catch (IOException e) {
            AppContext.showToast("创建文件失败,请检查SD卡是否存在");
            stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_OK:
                    Uri uri = Uri.fromFile(apkfile);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, 0);
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    notification.setLatestEventInfo(UpdateService.this, app_name, "下载成功", pendingIntent);
                    notificationManager.notify(R.layout.view_update_notification_item, notification);
                    installApk();
                    stopSelf();
                    break;
                case DOWN_ERROR:
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    notification.setLatestEventInfo(UpdateService.this, app_name, "下载失败", null);
                    stopSelf();
                    break;
                default:
                    stopSelf();
                    break;
            }
        }
    };

    private void installApk() {
        Uri uri = Uri.fromFile(apkfile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        UpdateService.this.startActivity(intent);
    }

    public void createThread() {
        new DownLoadThread().start();
    }

    private class DownLoadThread extends Thread {
        @Override
        public void run() {
            Message message = new Message();
            try {
                long downloadSize = downloadUpdateFile(down_url, apkfile.toString());
                if (downloadSize > 0) {
                    message.what = DOWN_OK;
                    handler.sendMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                message.what = DOWN_ERROR;
                handler.sendMessage(message);
            }
        }
    }

    public void createNotification() {
        notification = new Notification(R.drawable.logo, app_name + "正在下载", System.currentTimeMillis());
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        contentView = new RemoteViews(getPackageName(), R.layout.view_update_notification_item);
        contentView.setTextViewText(R.id.notificationTitle, app_name + "正在下载");
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
        notification.contentView = contentView;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.layout.view_update_notification_item, notification);
    }

    public long downloadUpdateFile(String down_url, String file) throws Exception {
        int down_step = 3;
        int totalSize;// 文件总大小
        int downloadCount = 0;// 已经下载好的大小
        int updateCount = 0;// 已经上传的文件大小
        InputStream inputStream;
        OutputStream outputStream;
        URL url = new URL(down_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        totalSize = httpURLConnection.getContentLength();

        if (httpURLConnection.getResponseCode() == 404) {
            throw new Exception("fail!");
        }

        inputStream = httpURLConnection.getInputStream();
        outputStream = new FileOutputStream(file, false);

        byte buffer[] = new byte[1024];
        int readsize = 0;

        while ((readsize = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, readsize);
            downloadCount += readsize;// 时时获取下载到的大小
            /*** 每次增张3% **/
            if (updateCount == 0 || (downloadCount * 100 / totalSize - down_step) >= updateCount) {
                updateCount += down_step;
                contentView.setTextViewText(R.id.notificationPercent, updateCount + "%");
                contentView.setProgressBar(R.id.notificationProgress, 100, updateCount, false);
                notification.contentView = contentView;
                notificationManager.notify(R.layout.view_update_notification_item, notification);
            }
        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        inputStream.close();
        outputStream.close();

        return downloadCount;
    }


}