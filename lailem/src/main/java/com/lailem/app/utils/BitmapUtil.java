package com.lailem.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * bitmap帮助类
 *
 * @author leeib
 */
public class BitmapUtil {

    /**
     * @param imageFilePath      图片文件的路径
     * @param imageUpperLimitPix 返回的bitmap宽或高的最高像素
     * @return
     */
    public static Bitmap decodeFile(String imageFilePath, int imageUpperLimitPix) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imageFilePath, o);
            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < imageUpperLimitPix && height_tmp / 2 < imageUpperLimitPix)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, o2);
            return rotaingImageView(readPictureDegree(imageFilePath), bitmap);
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * Drawable转bitmap
     *
     * @param d
     * @return
     */
    public static Bitmap drawable2bitmap(Drawable d) {

        return d != null ? ((BitmapDrawable) d).getBitmap() : null;
    }

    public static Bitmap layerdrawable2bitmap(Drawable drawable) {
        Bitmap bitmap_util;
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        bitmap_util = Bitmap.createBitmap(w, h, config);
        // 注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap_util);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap_util;
    }

    /**
     * bitmap转drawable
     *
     * @param b
     * @return
     */
    public static Drawable bitmap2drawable(Bitmap b) {
        return b != null ? (Drawable) new BitmapDrawable(b) : null;
    }

    /**
     * 创建带倒影的Bitmap
     *
     * @param originalImage 原bitmap
     * @param instanceColor 原图与倒影之间的颜色值，传null时默认为白色
     * @return bitmap
     * @author leeib
     */
    public static Bitmap createReflectedBitmap(Bitmap originalImage, Integer instanceColor) {
        final int reflectionGap = 4;

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 5), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);

        canvas.drawBitmap(originalImage, 0, 0, null);

        Paint deafaultPaint = new Paint();
        if (instanceColor != null) {
            deafaultPaint.setColor(instanceColor);
        } else {
            // 绘制的正方形为白色
            deafaultPaint.setColor(Color.WHITE);
        }

        canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);

        paint.setShader(shader);

        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
        return bitmapWithReflection;
    }

    /**
     * 创建带倒影的Bitmap
     *
     * @param originalImage    原bitmap
     * @param mReflectionScale 倒影和原图的比例
     * @param reflectionGap    倒影和原图间隔
     */
    public static Bitmap createReflectedBitmap(Bitmap originalImage, float mReflectionScale, int reflectionGap) {

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (int) (height + height * mReflectionScale + reflectionGap), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);

        canvas.drawBitmap(originalImage, 0, 0, null);

        Paint deafaultPaint = new Paint();
        deafaultPaint.setColor(Color.WHITE);
        canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);

        paint.setShader(shader);

        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
        return bitmapWithReflection;
    }

    /**
     * 创建带圆角的Bitmap
     *
     * @param bitmap 要转换的Bitmap
     * @param pixels 圆角的大小
     * @return
     */
    public static Bitmap createRoundCornerBitmap(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);

        final float roundPx = pixels;

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

    /**
     * 创建圆形的的Bitmap,带颜色边框，图片必须是正方形
     *
     * @param bitmap 要转换的Bitmap
     * @param pixels 圆角的大小
     * @return
     */
    public static Bitmap createRoundCornerBitmap(Bitmap bitmap, int color, int width) {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth() + width * 2, bitmap.getHeight() + width * 2, Config.ARGB_8888);
        final int roundPx = bitmap.getWidth();

        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);

        // 画背景颜色
        canvas.drawARGB(0, 0, 0, 0);

        final Rect rect = new Rect(width, width, bitmap.getWidth() + width, bitmap.getHeight() + width);
        final RectF rectF = new RectF(rect);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        canvas.drawBitmap(bitmap, new Rect(0, 0, roundPx, roundPx), rect, paint);
        paint = new Paint();
        paint.setAntiAlias(true); // 设置画笔为无锯齿
        paint.setColor(color); // 设置画笔颜色
        paint.setStrokeWidth(width); // 线宽
        paint.setStyle(Style.STROKE);

        canvas.drawArc(new RectF(width, width, newBitmap.getWidth() - width, newBitmap.getHeight() - width), 0, 360, false, paint); // 画圆环

        return newBitmap;
    }

    /**
     * 图片与边框组合
     *
     * @param bm  原图片
     * @param res 边框资源，size为8，分别对应左上、左、左下、下、右下、右、右上、上，其中四角传0表示忽略对应的边框绘制
     * @return
     */
    public static Bitmap createFrameBitmap(Bitmap bm, int[] res, Context context) {
        Bitmap bmp = decodeBitmap(res[1], context);
        Bitmap bmp1 = decodeBitmap(res[3], context);
        // 边框的宽高
        final int smallW = bmp.getWidth();
        final int smallH = bmp1.getHeight();

        // 原图片的宽高
        final int bigW = bm.getWidth();
        final int bigH = bm.getHeight();

        int wCount = (int) Math.ceil(bigW * 1.0 / smallW);
        int hCount = (int) Math.ceil(bigH * 1.0 / smallH);

        // 组合后图片的宽高
        // int newW = (wCount + 2) * smallW;
        // int newH = (hCount + 2) * smallH;
        int newW = bigW + 2;
        int newH = bigH + 2;

        // 重新定义大小
        Bitmap newBitmap = Bitmap.createBitmap(newW, newH, Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint p = new Paint();
        p.setColor(Color.TRANSPARENT);
        canvas.drawRect(new Rect(0, 0, newW, newH), p);

        Rect rect = new Rect(smallW, smallH, newW - smallW, newH - smallH);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(rect, paint);

        // 绘原图
        canvas.drawBitmap(bm, (newW - bigW - 2 * smallW) / 2 + smallW, (newH - bigH - 2 * smallH) / 2 + smallH, null);
        // 绘边框
        // 绘四个角
        int startW = newW - smallW;
        int startH = newH - smallH;
        if (res[0] != 0) {
            Bitmap leftTopBm = decodeBitmap(res[0], context); // 左上角
            canvas.drawBitmap(leftTopBm, 0, 0, null);

            leftTopBm.recycle();
            leftTopBm = null;

        }

        if (res[2] != 0) {
            Bitmap leftBottomBm = decodeBitmap(res[2], context); // 左下角
            canvas.drawBitmap(leftBottomBm, 0, startH, null);
            leftBottomBm.recycle();
            leftBottomBm = null;
        }

        if (res[4] != 0) {
            Bitmap rightBottomBm = decodeBitmap(res[4], context); // 右下角
            canvas.drawBitmap(rightBottomBm, startW, startH, null);
            rightBottomBm.recycle();
            rightBottomBm = null;
        }

        if (res[6] != 0) {
            Bitmap rightTopBm = decodeBitmap(res[6], context); // 右上角
            canvas.drawBitmap(rightTopBm, startW, 0, null);
            rightTopBm.recycle();
            rightTopBm = null;
        }

        // 绘左右边框
        Bitmap leftBm = decodeBitmap(res[1], context);
        Bitmap rightBm = decodeBitmap(res[5], context);
        for (int i = 0, length = hCount; i < length; i++) {
            int h = smallH * (i + 1);
            canvas.drawBitmap(leftBm, 0, h, null);
            canvas.drawBitmap(rightBm, startW, h, null);
        }

        leftBm.recycle();
        leftBm = null;
        rightBm.recycle();
        rightBm = null;

        // 绘上下边框
        Bitmap bottomBm = decodeBitmap(res[3], context);
        Bitmap topBm = decodeBitmap(res[7], context);
        for (int i = 0, length = wCount; i < length; i++) {
            int w = smallW * (i + 1);
            canvas.drawBitmap(bottomBm, w, startH, null);
            canvas.drawBitmap(topBm, w, 0, null);
        }

        bottomBm.recycle();
        bottomBm = null;
        topBm.recycle();
        topBm = null;

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return newBitmap;
    }

    public static Bitmap decodeBitmap(int res, Context context) {
        return BitmapFactory.decodeResource(context.getResources(), res);
    }

    /**
     * 获取宽或者高最大像素imageUpperLimitPix的Bitmap
     *
     * @param res
     * @param context
     * @param imageUpperLimitPix
     * @return
     */
    public static Bitmap decodeBitmap(int res, Context context, int imageUpperLimitPix) {

        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), res, o);
            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < imageUpperLimitPix && height_tmp / 2 < imageUpperLimitPix)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeResource(context.getResources(), res, o2);
        } catch (Exception e) {
        }
        return null;

    }

    /**
     * 截取图片的中间的200X200的区域
     *
     * @param bm
     * @return
     */
    public static Bitmap cropCenter(Bitmap bm, int width, int height) {
        if (bm == null) {
            return bm;
        }
        int startWidth = (bm.getWidth() - width) / 2;
        int startHeight = ((bm.getHeight() - height) / 2);
        Rect src = new Rect(startWidth, startHeight, startWidth + width, startHeight + height);
        return cropBitmap(bm, src);
    }

    /**
     * 剪切图片
     *
     * @param bmp 被剪切的图片
     * @param src 剪切的位置
     * @return 剪切后的图片
     */
    public static Bitmap cropBitmap(Bitmap bmp, Rect src) {
        int width = src.width();
        int height = src.height();
        Rect des = new Rect(0, 0, width, height);
        Bitmap croppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(croppedImage);
        canvas.drawBitmap(bmp, src, des, null);
        return croppedImage;
    }

    /**
     * 调整Bitmap的大小,对变形没有保证
     *
     * @param bm
     * @param newHeight
     * @param newWidth
     * @param context
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth, Context context) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

    public static File saveBitmap(Bitmap bm, Context context) {
        File file = FileUtils.createFileWithSuffix(".jpg", context);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;

    }

    /**
     * 生成一个二维码图像
     *
     * @param url       传入的字符串，通常是一个URL
     * @param QR_WIDTH  宽度（像素值px）
     * @param QR_HEIGHT 高度（像素值px）
     * @return
     * @throws WriterException
     */
    public static final Bitmap create2DCoderBitmap(String url, int QR_WIDTH, int QR_HEIGHT) throws WriterException {
        // 判断URL合法性
        if (url == null || "".equals(url) || url.length() < 1) {
            return null;
        }
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 图像数据转换，使用了矩阵转换
        BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
        int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
        // 下面这里按照二维码的算法，逐个生成二维码的图片，
        // 两个for循环是图片横列扫描的结果
        for (int y = 0; y < QR_HEIGHT; y++) {
            for (int x = 0; x < QR_WIDTH; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * QR_WIDTH + x] = 0xff000000;
                } else {
                    pixels[y * QR_WIDTH + x] = 0xffffffff;
                }
            }
        }
        // 生成二维码图片的格式，使用ARGB_8888
        Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
        return bitmap;
    }
}
