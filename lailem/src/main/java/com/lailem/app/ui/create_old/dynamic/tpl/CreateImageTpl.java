package com.lailem.app.ui.create_old.dynamic.tpl;

import android.content.Context;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.adapter.ImageViewerAdapter.ImageBean;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.Pic;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.ImageModel;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;

import java.util.ArrayList;

import butterknife.OnClick;

public class CreateImageTpl extends BaseTpl<ObjectWrapper> {
    LinearLayout layout;
    ImageModel bean;
    private int position;

    public CreateImageTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_create_image;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        setItemFillParent(getChildAt(0));
        this.bean = (ImageModel) wrapper.getObject();
        this.position = position;
        ArrayList<Pic> pics = bean.getContent().getPics();
        layout = (LinearLayout) ((HorizontalScrollView) ((LinearLayout) getChildAt(0))
                .getChildAt(0)).getChildAt(0);
        checkViewSize(pics);
        int size = pics.size();
        for (int i = 0; i < size; i++) {
            Pic pic = pics.get(i);
            ImageView imageView =
                    (ImageView) layout.getChildAt(i).findViewById(R.id.image);
            if (Func.checkImageTag(pic.getFilename(), imageView)) {
                Glide.with(_activity).load(pic.getFilename()).placeholder(R.drawable.empty).error(R.drawable.empty).into(imageView);
            }
        }

    }

    private void setItemFillParent(View item) {
        LayoutParams params = (LayoutParams) item
                .getLayoutParams();
        params.width = (int) TDevice.getScreenWidth();
        setLayoutParams(params);
    }

    @OnClick(R.id.addImage)
    public void addImage() {
        new ActionDialog(_activity, R.style.confirm_dialog)
                .init(DialogActionData.build(null, null, new ActionData("立即拍照",
                        R.drawable.ic_by_camera_selector), new ActionData("图库上传",
                        R.drawable.ic_by_gallery_selector)))
                .setOnActionClickListener(new OnActionClickListener() {

                    @Override
                    public void onActionClick(ActionDialog dialog, View View,
                                              int position) {
                        if (position == 0) {
                            PhotoManager.getInstance().photo(bean.getTag());
                        } else if (position == 1) {
                            PhotoManager.getInstance().album(bean.getTag());
                        }
                    }
                }).show();
    }

    private void addImageView() {
        final View item = View.inflate(_activity, R.layout.item_add_image, null);
        final ImageView imageView = (ImageView) item.findViewById(R.id.image);
        ImageView delete = (ImageView) item.findViewById(R.id.delete);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        int width = (int) TDevice.dpToPixel(84.5f);
        int height = (int) TDevice.dpToPixel(84.5f);
        LayoutParams params = new LayoutParams(width,
                height);
        params.rightMargin = 20;
        item.setLayoutParams(params);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int startIndex = (int) item.getTag();
                ArrayList<ImageBean> beans = new ArrayList<ImageBean>();
                ArrayList<Pic> pics = bean.getContent().getPics();
                int size = pics.size();
                for (int i = 0; i < size; i++) {
                    Pic pic = pics.get(i);
                    String url = StringUtils.getUri(pic.getFilename());
                    String thumb = StringUtils.getUri(pic.gettFilename());
                    ImageBean imageBean = new ImageBean(url, thumb);
                    beans.add(imageBean);
                }
                UIHelper.showImages(_activity, beans, startIndex);
            }
        });
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PhotoBean> photoBeans = PhotoManager.getInstance().getPhotos(bean.getTag());
                int startIndex = (int) item.getTag();
                photoBeans.remove(startIndex);
                bean.getContent().getPics().remove(startIndex);
                layout.removeView(item);
                //更新tag
                int size = layout.getChildCount() - 1;// 去除加号
                for (int i = 0; i < size; i++) {
                    layout.getChildAt(i).setTag(i);
                }
                //图片删除完
                if (bean.getContent().getPics().size() == 0) {
                    data.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        layout.addView(item, getChildCount() - 1);
    }

    private void checkViewSize(ArrayList<Pic> pics) {
        // -1是除去加号
        if (pics.size() > layout.getChildCount() - 1) {
            int size = pics.size() - layout.getChildCount() + 1;
            for (int i = 0; i < size; i++)
                addImageView();
        } else if (pics.size() < layout.getChildCount() - 1) {// -1是因为有个加号
            int size = layout.getChildCount() - 1 - pics.size();
            for (int i = 0; i < size; i++)
                layout.removeViewAt(0);
        }

        int size = layout.getChildCount() - 1;// 去除加号
        for (int i = 0; i < size; i++) {
            layout.getChildAt(i).setTag(i);
        }
    }
}
