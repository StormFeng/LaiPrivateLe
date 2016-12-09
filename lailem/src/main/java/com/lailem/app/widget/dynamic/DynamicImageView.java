package com.lailem.app.widget.dynamic;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.lailem.app.R;
import com.lailem.app.jsonbean.activegroup.Pic;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.widget.NineGridLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DynamicImageView extends LinearLayout {
    @Bind(R.id.images)
    NineGridLayout images;

    public DynamicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DynamicImageView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_dynamic_image, this);
        ButterKnife.bind(this, this);

    }

    public void render(ArrayList<Pic> pics) {
        render(pics, NineGridLayout.DEFAULT_WIDTH_RATIO);
    }

    public void render(ArrayList<Pic> pics, float widthRatio) {
        ArrayList<NineGridLayout.Image> nineGridLayoutImages = new ArrayList<NineGridLayout.Image>();
        for (int i = 0; pics != null && i < pics.size(); i++) {
            int width = 0;
            int height = 0;
            try {
                width = Integer.parseInt(pics.get(i).getW());
                height = Integer.parseInt(pics.get(i).getH());
            } catch (Exception e) {
                width = 0;
                height = 0;
            }
            if (!TextUtils.isEmpty(pics.get(i).gettFilename())) {
                if (pics.size() == 1 && !pics.get(i).getFilename().endsWith(".gif")) {
                    nineGridLayoutImages.add(new NineGridLayout.Image(
                            StringUtils.getUri(pics.get(i).getFilename()),
                            StringUtils.getUri(pics.get(i).getFilename()),
                            width, height
                    ));
                } else {
                    nineGridLayoutImages.add(new NineGridLayout.Image(
                            StringUtils.getUri(pics.get(i).gettFilename()),
                            StringUtils.getUri(pics.get(i).getFilename()),
                            width, height
                    ));
                }

            } else {
                nineGridLayoutImages.add(new NineGridLayout.Image(
                        StringUtils.getUri(pics.get(i).getFilename()),
                        StringUtils.getUri(pics.get(i).getFilename()),
                        width, height
                ));
            }
        }
        images.setWithRatio(widthRatio);
        images.setImagesData(nineGridLayoutImages);
    }

}
