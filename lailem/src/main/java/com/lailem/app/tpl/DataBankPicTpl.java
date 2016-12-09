package com.lailem.app.tpl;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.R;
import com.lailem.app.adapter.ImageViewerAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.GroupDatabaseBean;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;

import java.util.ArrayList;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DataBankPicTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.imageLayout)
    LinearLayout imageLayout;

    public DataBankPicTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_data_bank_pic;
    }

    @Override
    protected void initView() {
        super.initView();
        int width = (int) ((TDevice.getScreenWidth() - TDevice.dpToPixel(40)) / 3);
        int height = width;
        for (int i = 0; i < imageLayout.getChildCount(); i++) {
            FrameLayout fl = (FrameLayout) imageLayout.getChildAt(i);
            ImageView iv = (ImageView) fl.getChildAt(0);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) iv.getLayoutParams();
            params.width = width;
            params.height = height;
            iv.setLayoutParams(params);
        }
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        ArrayList<GroupDatabaseBean.Data> bean = (ArrayList<GroupDatabaseBean.Data>) wrapper.getObject();
        for (int i = 0; i < imageLayout.getChildCount(); i++) {
            FrameLayout fl = (FrameLayout) imageLayout.getChildAt(i);
            if (i < bean.size()) {
                final GroupDatabaseBean.Data itemData = bean.get(i);
                fl.setVisibility(VISIBLE);
                final ImageView pic_iv = (ImageView) fl.findViewById(R.id.pic);
                TextView name_tv = (TextView) fl.findViewById(R.id.name);
                Glide.with(_activity).load(ApiClient.getFileUrl(itemData.gettFilename())).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(_activity), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(pic_iv);
                name_tv.setText(itemData.getNickname());
                fl.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<ImageViewerAdapter.ImageBean> imageBeans = new ArrayList<ImageViewerAdapter.ImageBean>();
                        int temp = 0;//记录分割条个数
                        int startIndex = 0;
                        for (int x = 0; x < data.size(); x++) {
                            if (data.get(x) instanceof ObjectWrapper && data.get(x).getObject() instanceof ArrayList) {
                                ArrayList<GroupDatabaseBean.Data> list = (ArrayList<GroupDatabaseBean.Data>) data.get(x).getObject();
                                for (int y = 0; y < list.size(); y++) {
                                    GroupDatabaseBean.Data d = list.get(y);
                                    String url = ApiClient.getFileUrl(d.getFilename());
                                    String thumb = ApiClient.getFileUrl(d.gettFilename());
                                    imageBeans.add(new ImageViewerAdapter.ImageBean(url, thumb));

                                    System.out.println(url + " " + thumb);
                                    if (itemData.equals(d)) {
                                        startIndex = imageBeans.size() - 1;
                                    }
                                }
                            } else {
                                temp++;
                            }
                        }
                        UIHelper.showImages(_activity, imageBeans, startIndex);
                    }
                });
            } else {
                fl.setVisibility(INVISIBLE);
            }
        }
    }

}
