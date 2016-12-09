package com.lailem.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.adapter.ImageViewerAdapter;
import com.lailem.app.adapter.ImageViewerAdapter.ImageBean;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.nostra13.universalimageloader.utils.L;
import com.socks.library.KLog;

import java.util.ArrayList;

public class NineGridLayout extends ViewGroup {
    public static final float DEFAULT_WIDTH_RATIO = 2.0f / 3.0f;

    /**
     * 图片之间的间隔
     */
    private int gap = (int) TDevice.dpToPixel(5);
    private int columns;//
    private int rows;//
    private ArrayList<NineGridLayout.Image> listData;
    private int totalWidth;
    private float withRatio = DEFAULT_WIDTH_RATIO;

    public NineGridLayout(Context context) {
        super(context);
        init(context);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        KLog.i(System.currentTimeMillis() + "");
        post(new Runnable() {
            @Override
            public void run() {
                totalWidth = getWidth();
                KLog.i(System.currentTimeMillis() + "");
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void layoutChildrenView() {
        int childrenCount = listData.size();
        int singleWidth = (totalWidth - gap * (3 - 1)) / 3;
        int singleHeight = singleWidth;

        if (childrenCount == 1 && listData.get(0).height != 0 && listData.get(0).width != 0) {
            CustomImageView childrenView = (CustomImageView) getChildAt(0);
            childrenView.setScaleType(ImageView.ScaleType.FIT_START);
            if (!TextUtils.isEmpty(listData.get(0).getBigUrl()) && !listData.get(0).getBigUrl().endsWith(".gif")) {
                childrenView.setImageUrl(((Image) listData.get(0)).getUrl());//先显示缩略图
                childrenView.setImageUrl(((Image) listData.get(0)).getBigUrl());//再显示大图
            } else {
                childrenView.setImageUrl(((Image) listData.get(0)).getUrl());//先显示缩略图
            }
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = (int) ((totalWidth * withRatio) * listData.get(0).height / listData.get(0).width);
            setLayoutParams(params);
            childrenView.layout(0, 0, (int) (totalWidth * withRatio), params.height);
        } else {
            // 根据子view数量确定高度
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = singleHeight * rows + gap * (rows - 1);
            setLayoutParams(params);

            for (int i = 0; i < childrenCount; i++) {
                CustomImageView childrenView = (CustomImageView) getChildAt(i);
                childrenView.setImageUrl(((Image) listData.get(i)).getUrl());
                int[] position = findPosition(i);
                int left = (singleWidth + gap) * position[1];
                int top = (singleHeight + gap) * position[0];
                int right = left + singleWidth;
                int bottom = top + singleHeight;

                childrenView.layout(left, top, right, bottom);
            }
        }

    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) == childNum) {
                    position[0] = i;// 行
                    position[1] = j;// 列
                    break;
                }
            }
        }
        return position;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public void setImagesData(final ArrayList<Image> lists) {
        if (totalWidth == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    totalWidth = getWidth();
                    render(lists);
                }
            });

        } else {
            render(lists);
        }
    }

    private void render(final ArrayList<Image> lists) {
        if (lists == null || lists.isEmpty()) {
            removeAllViews();
            return;
        }
        // 初始化布局
        generateChildrenLayout(lists.size());
        removeAllViews();
        for (int j = 0; j < lists.size(); j++) {
            CustomImageView iv = generateImageView(j);
            addView(iv, generateDefaultLayoutParams());
        }
        listData = lists;
        layoutChildrenView();
    }

    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            rows = 1;
            columns = length;
        } else if (length <= 6) {
            rows = 2;
            columns = 3;
        } else {
            rows = 3;
            columns = 3;
        }
    }

    private CustomImageView generateImageView(final int i) {
        CustomImageView iv = new CustomImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ImageBean> imageBeans = new ArrayList<ImageViewerAdapter.ImageBean>();
                for (int i = 0; i < listData.size(); i++) {
                    imageBeans.add(new ImageBean(listData.get(i).getBigUrl(), listData.get(i).getUrl()));
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("imageBeans", imageBeans);
                bundle.putInt("startIndex", i);
                UIHelper.showImages(getContext(), imageBeans, i);
            }
        });
        iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
        return iv;
    }

    public static class Image {
        private String url;
        private String bigUrl;
        private int width;
        private int height;

        public Image(String url, String bigUrl) {
            this.url = url;
            this.bigUrl = bigUrl;
        }

        public Image(String url, String bigUrl, int width, int height) {
            this.url = url;
            this.bigUrl = bigUrl;
            this.width = width;
            this.height = height;
            L.i(toString());
        }

        public String getBigUrl() {
            return bigUrl;
        }

        public void setBigUrl(String bigUrl) {
            this.bigUrl = bigUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public String toString() {
            return "image---->>url=" + url + "width=" + width + "height" + height;
        }
    }

    public static class CustomImageView extends ImageView {
        private AppContext ac;
        private String url;
        private boolean isAttachedToWindow;

        public CustomImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.ac = (AppContext) context.getApplicationContext();
        }

        public CustomImageView(Context context) {
            super(context);
            this.ac = (AppContext) context.getApplicationContext();
        }

        @Override
        public void onAttachedToWindow() {
            isAttachedToWindow = true;
            setImageUrl(url);
            super.onAttachedToWindow();
        }

        @Override
        public void onDetachedFromWindow() {
            isAttachedToWindow = false;
            setImageBitmap(null);
            super.onDetachedFromWindow();
        }

        public void setImageUrl(String url) {
            if (!TextUtils.isEmpty(url)) {
                this.url = url;
                if (isAttachedToWindow) {
                    if (url != null && url.startsWith("file://")) {
                        Glide.with(getContext()).load(url.replace("file://", "")).placeholder(R.drawable.empty).error(R.drawable.empty).into(this);
                    } else {
                        if (Func.checkImageTag(url, this)) {
                            Glide.with(getContext()).load(url).placeholder(R.drawable.empty).error(R.drawable.empty).into(this);
                        }
                    }
                }
            }
        }
    }

    public void setWithRatio(float withRatio) {
        this.withRatio = withRatio;
    }
}
