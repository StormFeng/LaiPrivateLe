package com.lailem.app.ui.create;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.R;
import com.lailem.app.adapter.ImageViewerAdapter;
import com.lailem.app.jsonbean.activegroup.Pic;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.photo.listenser.OnPhotoListener;
import com.lailem.app.ui.create_old.dynamic.model.ImageModel;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.socks.library.KLog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by XuYang on 15/12/17.
 */
public class ImageArea extends FrameLayout implements OnPhotoListener {

    @Bind(R.id.hsv)
    HorizontalScrollView hsv;
    @Bind(R.id.images)
    LinearLayout images_ll;
    @Bind(R.id.addImage)
    View addImage;
    @Bind(R.id.count)
    TextView count_vt;

    private int IMAGE_WIDTH = 0;
    private int IMAGE_HEIGHT = 0;

    private OnChangeListener onChangeListener;

    private ImageModel model;

    public ImageArea(Context context) {
        super(context);
        init(context);
    }

    public ImageArea(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageArea(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ImageArea(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_create_dynamic_action_bar_image, this);
        ButterKnife.bind(this);

        IMAGE_WIDTH = (int) (TDevice.getScreenWidth() * 110 / 375f);
        IMAGE_HEIGHT = (int) (IMAGE_WIDTH * 140f / 90);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) addImage.getLayoutParams();
        params.width = IMAGE_WIDTH;
        params.height = IMAGE_HEIGHT;
        addImage.setLayoutParams(params);

    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public OnChangeListener getOnChangeListener() {
        return onChangeListener;
    }

    public ImageModel getModel() {
        return model;
    }

    @OnClick(R.id.addImage)
    public void addImage() {
        PhotoManager.getInstance().album(Const.PHOTO_TAG);
    }

    @Override
    public void onPhoto(String tag, ArrayList<PhotoBean> photos) {
        if (photos.size() > 0) {
            if (model == null) {
                model = new ImageModel();
            }
            ImageModel.Content content = model.getContent();
            ArrayList<Pic> pics = new ArrayList<>();
            for (int i = 0; i < photos.size(); i++) {
                Pic pic = new Pic();
                pic.setFilename(photos.get(i).getImagePath());
                int[] imageSize = Func.getImageSize(photos.get(i).getImagePath());
                pic.setW(imageSize[0] + "");
                pic.setH(imageSize[1] + "");
                pics.add(pic);
            }
            content.setPics(pics);
            checkCount();
            render(pics);
        } else {
            model = null;
        }
    }

    private void render(ArrayList<Pic> pics) {
        //增删条目
        if (pics.size() < images_ll.getChildCount() - 1) {
            //需要从最右侧移除多余条目
            for (int i = images_ll.getChildCount() - 2; i >= 0; i--) {
                images_ll.removeViewAt(i);
            }
        } else if (pics.size() > images_ll.getChildCount() - 1) {
            //需要添加新的条目
            for (int i = images_ll.getChildCount() - 1; i < pics.size(); i++) {
                final View item = View.inflate(getContext(), R.layout.item_c_d_add_image, null);
                final ImageView imageView = (ImageView) item.findViewById(R.id.image);
                ImageView delete = (ImageView) item.findViewById(R.id.delete);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(IMAGE_WIDTH,
                        IMAGE_HEIGHT);
                params.rightMargin = (int) TDevice.dpToPixel(5f);
                item.setLayoutParams(params);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<ImageViewerAdapter.ImageBean> beans = new ArrayList<ImageViewerAdapter.ImageBean>();
                        ArrayList<Pic> pics = model.getContent().getPics();
                        int size = pics.size();
                        for (int i = 0; i < size; i++) {
                            Pic pic = pics.get(i);
                            String url = StringUtils.getUri(pic.getFilename());
                            String thumb = StringUtils.getUri(pic.gettFilename());
                            ImageViewerAdapter.ImageBean imageBean = new ImageViewerAdapter.ImageBean(url, thumb);
                            beans.add(imageBean);
                        }
                        UIHelper.showImages(getContext(), beans, images_ll.indexOfChild(item));
                    }
                });
                delete.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<PhotoBean> photoBeans = PhotoManager.getInstance().getPhotos(Const.PHOTO_TAG);
                        photoBeans.remove(images_ll.indexOfChild(item));
                        model.getContent().getPics().remove(images_ll.indexOfChild(item));
                        images_ll.removeView(item);
                        checkCount();
                    }
                });
                images_ll.addView(item, i);
            }
        } else {
            //do nothing
        }


        //渲染图片
        for (int i = 0; i < pics.size(); i++) {
            Pic pic = pics.get(i);
            FrameLayout item = (FrameLayout) images_ll.getChildAt(i);
            ImageView imageView = (ImageView) item.getChildAt(0);
            KLog.e(imageView + " " + pic.getFilename());
            if (Func.checkImageTag(pic.getFilename(), imageView)) {
                Glide.with(getContext()).load(pic.getFilename()).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(getContext()), new RoundedCornersTransformation(getContext(), (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(imageView);
            }
        }
    }

    private void checkCount() {
        int count = model.getContent().getPics().size();
        if (count == 9) {
            addImage.setVisibility(GONE);
        } else {
            addImage.setVisibility(VISIBLE);
        }
        count_vt.setText("已添加" + count + "/9");

        if (onChangeListener != null) {
            onChangeListener.onChangeListener(this, true, count);
        }
    }

}
