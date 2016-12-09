package com.lailem.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.socks.library.KLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

public class FileUtils {

    /**
     * 文件根目录
     */
    public static final String DIR_ROOT = "/.laile/";
    /**
     * 图片文件目录，优先存储在sd卡中
     */
    public static final String DIR_IMAGE = "/.laile/image/";
    /**
     * 语音文件目录，优先存储在sd卡中
     */
    public static final String DIR_VOICE = "/.laile/voice/";
    /**
     * 视频文件目录，优先存储在sd卡中
     */
    public static final String DIR_VIDEO = "/.laile/video/";
    /**
     * 其它文件目录:如apk文件，优先存储在sd卡中
     */
    public static final String DIR_OTHER = "/.laile/other/";
    /**
     * 文本文件的缓存，如附近的活动列表缓存，优先缓存在appCache中
     */
    public static final String DIR_TEXT = "/.laile/text/";

    /**
     * 获取sd卡本app的根目录路径
     *
     * @param context
     * @return
     */
    public static String getRootDirOfSDForApp(Context context) {
        File file = new File(getRootPathOfSD(context) + DIR_ROOT);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取sd卡本app的根目录
     *
     * @param context
     * @return
     */
    public static File getRootDirFileOfSDForApp(Context context) {
        File file = new File(getRootPathOfSD(context) + DIR_ROOT);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取图片文件的目录
     *
     * @param context
     * @return
     */
    public static String getImageDir(Context context) {
        File file = new File(getRootPathOfSD(context) + DIR_IMAGE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取语音文件目录
     *
     * @param context
     * @return
     */
    public static String getVoiceDir(Context context) {
        File file = new File(getRootPathOfSD(context) + DIR_VOICE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取视频文件目录
     *
     * @param context
     * @return
     */
    public static String getVideoDir(Context context) {
        File file = new File(getRootPathOfSD(context) + DIR_VIDEO);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取其它文件目录
     *
     * @param context
     * @return
     */
    public static String getOtherDir(Context context) {
        File file = new File(getRootPathOfSD(context) + DIR_OTHER);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取文本文件目录
     *
     * @param context
     * @return
     */
    public static String getTextDir(Context context) {
        File file = new File(getRootPathOfCache(context) + DIR_TEXT);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 优先返回sd卡的根路径，不存在sd卡时返回appCache的根路径
     *
     * @param context
     * @return
     */
    public static String getRootPathOfSD(Context context) {
        if (checkSDExists()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return context.getCacheDir().getAbsolutePath();
        }

    }

    /**
     * 返回appCache的路径
     *
     * @param context
     * @return
     */
    public static String getRootPathOfCache(Context context) {
        return context.getCacheDir().getAbsolutePath();
    }

    /**
     * 优先返回sd卡的根目录，不存在sd卡时返回appCache的根目录
     *
     * @param context
     * @return
     */
    public static File getRootFileOfSD(Context context) {
        if (checkSDExists()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return context.getCacheDir();
        }

    }

    /**
     * 返回appCache的根目录
     *
     * @param context
     * @return
     */
    public static File getRootFileOfCache(Context context) {
        return context.getCacheDir();
    }

    /**
     * 检查是否安装SD卡
     *
     * @return
     */
    public static boolean checkSDExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 检查是否安装外置的SD卡
     *
     * @return
     */
    public static boolean checkExternalSDExists() {
        Map<String, String> evn = System.getenv();
        return evn.containsKey("SECONDARY_STORAGE");
    }

    /**
     * 检查文件是否存在
     *
     * @param fileAbsolutePath 文件的绝对路径
     * @return
     */
    public static boolean checkFileExists(String fileAbsolutePath) {
        boolean status = false;
        if (!StringUtils.isEmpty(fileAbsolutePath)) {
            File file = new File(fileAbsolutePath);
            return file.exists();
        }
        return status;
    }

    /**
     * 产生新的文件名
     *
     * @return
     */
    public static String createFileName() {
        return System.currentTimeMillis() + "";
    }

    /**
     * 产生新的带后缀的文件名
     *
     * @param suffix 如".jpg"、".arm"等
     * @return
     */
    public static String createFileName(String suffix) {
        return createFileName() + suffix;
    }

    /**
     * 创建带后缀的文件
     *
     * @param suffix
     * @param context
     * @return
     */
    public static File createFileWithSuffix(String suffix, Context context) {
        File file = null;
        if (StringUtils.isEmpty(suffix)) {
            KLog.e("suffix为空");
            return file;
        }
        // 图片
        if (".jpg".equals(suffix) || ".png".equals(suffix)) {
            file = new File(getImageDir(context) + File.separator + createFileName(suffix));
            // 音频
        } else if (".amr".equals(suffix) || ".mp3".equals(suffix) || ".pcm".equals(suffix) || ".wav".equals(suffix)) {
            file = new File(getVoiceDir(context) + File.separator + createFileName(suffix));
            // 视频
        } else if (".mp4".equals(suffix)) {
            file = new File(getVideoDir(context) + File.separator + createFileName(suffix));
            // 文本
        } else if (".text".equals(suffix)) {
            file = new File(getTextDir(context) + File.separator + createFileName(suffix));
        } else if (".apk".equals(suffix)) {
            //安装包
            file = new File(getOtherDir(context) + File.separator + createFileName(suffix));
        } else {
            KLog.e("不支持该文件类型：" + suffix);
        }
        return file;
    }

    /**
     * 创建不带后缀的文件
     *
     * @param suffix
     * @param context
     * @return
     */
    public static File createFile(String suffix, Context context) {
        File file = null;
        if (StringUtils.isEmpty(suffix)) {
            KLog.e("suffix为空");
            return file;
        }
        // 图片
        if (".jpg".equals(suffix) || ".png".equals(suffix)) {
            file = new File(getImageDir(context) + File.separator + createFileName());
            // 音频
        } else if (".amr".equals(suffix) || ".mp3".equals(suffix) || ".pcm".equals(suffix) || ".wav".equals(suffix)) {
            file = new File(getVoiceDir(context) + File.separator + createFileName());
            // 视频
        } else if (".mp4".equals(suffix)) {
            file = new File(getVideoDir(context) + File.separator + createFileName());
            // 文本
        } else if (".text".equals(suffix)) {
            file = new File(getTextDir(context) + File.separator + createFileName());
        } else if (".apk".equals(suffix)) {
            //安装包
            file = new File(getOtherDir(context) + File.separator + createFileName());
        } else {
            KLog.e("不支持该文件类型：" + suffix);
        }
        return file;
    }

    /**
     * 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     */
    public static void writeToData(Context context, String fileName, String content) {
        if (content == null)
            content = "";
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param content
     * @return 文件名
     */
    public static String writeToData(Context context, String content) {
        if (content == null)
            content = "";
        String fileName = createFileName();
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 从data中读取文本文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readFromData(Context context, String fileName) {
        try {
            FileInputStream in = context.openFileInput(fileName);
            return readFromInputStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 从输入流中读取
     *
     * @param inStream
     * @return
     */
    public static String readFromInputStream(InputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }
            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            Log.i("FileTest", e.getMessage());
        }
        return null;
    }

    /**
     * 根据文件的绝对路径获取文件名但不包含扩展名
     *
     * @param fileAbsolutePath
     * @return
     */
    public static String getFileNameWithoutSuffix(String fileAbsolutePath) {
        if (StringUtils.isEmpty(fileAbsolutePath)) {
            return "";
        }
        int point = fileAbsolutePath.lastIndexOf('.');
        return fileAbsolutePath.substring(fileAbsolutePath.lastIndexOf(File.separator) + 1, point);
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        if (StringUtils.isEmpty(fileName))
            return "";

        int point = fileName.lastIndexOf('.');
        return fileName.substring(point + 1);
    }

    /**
     * 获取文件大小
     *
     * @param filePath
     * @return
     */
    public static long getFileSize(String filePath) {
        long size = 0;

        File file = new File(filePath);
        if (file != null && file.exists()) {
            size = file.length();
        }
        return size;
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 获取目录文件大小
     *
     * @return
     */
    public static long getDirSize(String path) {
        if (StringUtils.isEmpty(path)) {
            return 0;
        }
        File dir = new File(path);
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += getDirSize(file);
            }
        }
        return dirSize;
    }

    public static byte[] inputStreamToBytes(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch = -1;
        byte buffer[] = null;
        try {
            while ((ch = in.read()) != -1) {
                out.write(ch);
            }
            buffer = out.toByteArray();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer;
    }

    /**
     * 删除文件或目录
     *
     * @param fileAbsolutePath
     * @return
     */
    public static void delete(String fileAbsolutePath) {

        if (!StringUtils.isEmpty(fileAbsolutePath)) {
            File file = new File(fileAbsolutePath);
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                try {
                    for (File deleteFile : files) {
                        if (deleteFile.isFile()) {
                            deleteFile.delete();
                        } else if (deleteFile.isDirectory()) {
                            delete(deleteFile.getAbsolutePath());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除一定时间以前的文件
     *
     * @param fileAbsolutePath
     * @param timeSpace        该方法会删除该时间以前的文件
     * @return
     */
    public static void delete(String fileAbsolutePath, long timeSpace) {

        if (!StringUtils.isEmpty(fileAbsolutePath)) {
            File file = new File(fileAbsolutePath);
            if (file.isFile() && file.lastModified() < timeSpace) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                try {
                    for (File deleteFile : files) {
                        if (deleteFile.isFile() && deleteFile.lastModified() < timeSpace) {
                            deleteFile.delete();
                        } else if (deleteFile.isDirectory()) {
                            delete(deleteFile.getAbsolutePath(), timeSpace);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件或目录
     *
     * @return
     */
    public static void delete(File file) {

        if (file != null) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                try {
                    for (File deleteFile : files) {
                        if (deleteFile.isFile()) {
                            deleteFile.delete();
                        } else if (deleteFile.isDirectory()) {
                            delete(deleteFile);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除一定时间以前新建或修改的文件
     *
     * @param file
     * @param timeSpace 该方法会删除该时间以前的文件
     * @return
     */
    public static void delete(File file, long timeSpace) {

        if (file != null) {
            if (file.isFile() && file.lastModified() < timeSpace) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                try {
                    for (File deleteFile : files) {
                        if (deleteFile.isFile() && deleteFile.lastModified() < timeSpace) {
                            deleteFile.delete();
                        } else if (deleteFile.isDirectory()) {
                            delete(deleteFile, timeSpace);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 重命名
     *
     * @param oldPath
     * @return
     */
    public static boolean reName(String oldPath, String newPath) {
        File file = new File(oldPath);
        return file.renameTo(new File(newPath));
    }

    /**
     * 重命名
     *
     * @param oldFile
     * @param newPath
     * @return
     */
    public static boolean reName(File oldFile, String newPath) {
        return oldFile.renameTo(new File(newPath));
    }

    /**
     * 清理文件
     *
     * @param context
     */
    public static void checkFile(final Context context) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -30);//30天前
        delete(getRootDirFileOfSDForApp(context), cal.getTimeInMillis());
        delete(getRootFileOfCache(context), cal.getTimeInMillis());
    }


    /**
     * 保存图片到本地
     */
    private String saveDrawable(Context context, int drawabelResid, String fileName) {
        String fileToPath = FileUtils.getImageDir(context) + File.separator + fileName;
        File file = new File(fileToPath);
        try {
            if (!file.exists()) {
                file.createNewFile();
                Bitmap pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_square);
                FileOutputStream fos = new FileOutputStream(file);
                pic.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
            return fileToPath;
        } catch (IOException e) {
            e.printStackTrace();
            AppContext.showToast("创建文件失败,请检查sd卡是否可用!");
        }
        return "";
    }


}
