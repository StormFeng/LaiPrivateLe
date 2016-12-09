package com.lailem.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.cache.RemarkCache;
import com.lailem.app.cache.UserCache;
import com.lailem.app.dao.Remark;
import com.lailem.app.dao.User;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.jsonbean.personal.Contact;
import com.lailem.app.widget.ListActionDialog;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by XuYang on 15/4/20.
 */
public class Func {

    public final static Pattern mobiler = Pattern.compile("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");

    public final static String[] weeks = {"周一", "周二", "周三", "周四", "周五", "周六", "周日",};
    public final static String[] weeksAbnormal = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    /**
     * 判断是不是一个合法的手机号
     *
     * @return
     */
    public static boolean isMobileNO(String mobile) {
        if (mobile == null || mobile.trim().length() == 0) {
            return false;
        }
        return mobiler.matcher(mobile).matches();
    }

    /**
     * 是不是电话号码（手机号或者固定电话）
     *
     * @param input
     */
    public static boolean isPhoneNumber(String input) {
        String regex = "^(010|02\\d|0[3-9]\\d{2})?\\d{6,8}$";
        Pattern p = Pattern.compile(regex);
        return isMobileNO(input) || p.matcher(input).matches();
    }

    /**
     * 判断是否为空
     *
     * @param phone
     * @param pwd
     * @return
     */
    public static boolean isEmpty(String phone, String pwd) {
        if (phone == null || phone.trim().length() == 0) {
            return false;
        }

        if (pwd == null || pwd.trim().length() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 把dp转为px
     */
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 把px转为dp
     */
    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * json字符串的格式化
     */
    public static String formatJson(String json, String fillStringUnit) {
        if (json == null || json.trim().length() == 0) {
            return null;
        }

        int fixedLenth = 0;
        ArrayList<String> tokenList = new ArrayList<String>();
        {
            String jsonTemp = json;
            // 预读取
            while (jsonTemp.length() > 0) {
                String token = getToken(jsonTemp);
                jsonTemp = jsonTemp.substring(token.length());
                token = token.trim();
                tokenList.add(token);
            }
        }

        for (int i = 0; i < tokenList.size(); i++) {
            String token = tokenList.get(i);
            int length = token.getBytes().length;
            if (length > fixedLenth && i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
                fixedLenth = length;
            }
        }

        StringBuilder buf = new StringBuilder();
        int count = 0;
        for (int i = 0; i < tokenList.size(); i++) {

            String token = tokenList.get(i);

            if (token.equals(",")) {
                buf.append(token);
                doFill(buf, count, fillStringUnit);
                continue;
            }
            if (token.equals(":")) {
                buf.append(" ").append(token).append(" ");
                continue;
            }
            if (token.equals("{")) {
                String nextToken = tokenList.get(i + 1);
                if (nextToken.equals("}")) {
                    i++;
                    buf.append("{ }");
                } else {
                    count++;
                    buf.append(token);
                    doFill(buf, count, fillStringUnit);
                }
                continue;
            }
            if (token.equals("}")) {
                count--;
                doFill(buf, count, fillStringUnit);
                buf.append(token);
                continue;
            }
            if (token.equals("[")) {
                String nextToken = tokenList.get(i + 1);
                if (nextToken.equals("]")) {
                    i++;
                    buf.append("[ ]");
                } else {
                    count++;
                    buf.append(token);
                    doFill(buf, count, fillStringUnit);
                }
                continue;
            }
            if (token.equals("]")) {
                count--;
                doFill(buf, count, fillStringUnit);
                buf.append(token);
                continue;
            }

            buf.append(token);
            // 左对齐
            if (i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
                int fillLength = fixedLenth - token.getBytes().length;
                if (fillLength > 0) {
                    for (int j = 0; j < fillLength; j++) {
                        buf.append(" ");
                    }
                }
            }
        }
        return buf.toString();
    }

    private static String getToken(String json) {
        StringBuilder buf = new StringBuilder();
        boolean isInYinHao = false;
        while (json.length() > 0) {
            String token = json.substring(0, 1);
            json = json.substring(1);

            if (!isInYinHao && (token.equals(":") || token.equals("{") || token.equals("}") || token.equals("[") || token.equals("]") || token.equals(","))) {
                if (buf.toString().trim().length() == 0) {
                    buf.append(token);
                }

                break;
            }

            if (token.equals("\\")) {
                buf.append(token);
                buf.append(json.substring(0, 1));
                json = json.substring(1);
                continue;
            }
            if (token.equals("\"")) {
                buf.append(token);
                if (isInYinHao) {
                    break;
                } else {
                    isInYinHao = true;
                    continue;
                }
            }
            buf.append(token);
        }
        return buf.toString();
    }

    private static void doFill(StringBuilder buf, int count, String fillStringUnit) {
        buf.append("\n");
        for (int i = 0; i < count; i++) {
            buf.append(fillStringUnit);
        }
    }

    public static String formatWeek(int index) {
        return weeks[index - 1];
    }

    public static String formatWeekAbNormal(int index) {
        return weeksAbnormal[index - 1];
    }

    public static String formatToTwo(int i) {
        String x = "";
        if (i < 10) {
            x = "0" + i;
        } else {
            x = "" + i;
        }
        return x;
    }

    /**
     * 获得当前时间
     *
     * @return
     */
    public static String getNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * yyyy-MM-dd hh:mm:ss格式转为yyyy年MM月dd日 hh:mm
     */
    public static String formatTime(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

        try {
            return sdf2.format(sdf.parse(s));
        } catch (ParseException e) {
            return s;
        }
    }

    /**
     * yyyy-MM-dd hh:mm:ss格式转为yyyy年MM月dd日(周一) 上午hh:mm
     */
    public static String formatTime2(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日(E) ahh:mm");

        try {
            return sdf2.format(sdf.parse(s));
        } catch (ParseException e) {
            return s;
        }
    }

    /**
     * yyyy-MM-dd hh:mm:ss格式转为yyyy年MM月dd日
     */
    public static String formatTime3(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");

        try {
            return sdf2.format(sdf.parse(s));
        } catch (ParseException e) {
            return s;
        }
    }

    public static String formatTime4(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日(E) ahh:mm");

        try {
            return sdf2.format(sdf.parse(s));
        } catch (ParseException e) {
            return s;
        }
    }

    /**
     * 是否过期（早于当前时间视为过期）
     *
     * @param s
     * @return
     */
    public static boolean isExpired(String s) {
        if (TextUtils.isEmpty(s)) {
            return true;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(s).getTime() < System.currentTimeMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            AppContext.showToast("日期格式转化错误");
            return true;
        }
    }

    public static String getMethodName(StackTraceElement ste[]) {
        String methodName = "";
        boolean flag = false;

        for (StackTraceElement s : ste) {

            if (flag) {

                methodName = s.getMethodName();
                break;
            }
            flag = s.getMethodName().equals("getStackTrace");
        }
        return methodName;
    }

    /**
     * 将联系人字符串转为列表
     *
     * @param contactStr
     * @return
     */
    public static ArrayList<Contact> formatContact(String contactStr) {
        return new Gson().fromJson(contactStr, new TypeToken<List<Contact>>() {
        }.getType());
    }


    /**
     * 出生年月转年龄
     *
     * @throws Exception
     */
    public static String getAge(String birthday) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(birthday);

            Calendar cal = Calendar.getInstance();

            if (cal.before(date)) {
                return "0";
            }
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH);
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
            cal.setTime(date);

            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH);
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

            int age = yearNow - yearBirth;

            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayOfMonthNow < dayOfMonthBirth) age--;
                } else {
                    age--;
                }
            }
            if (age < 0) {
                age = 0;
            }
            return age + "";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 用于处理imageloader重复显示图片
     *
     * @param imagePath
     * @param imageView
     * @return true:需要显示 ，false：不需要显示
     */
    public static boolean checkImageTag(String imagePath, ImageView imageView) {
        boolean flag = true;
        if (imageView.getTag(R.id.image_tag) != null && imageView.getTag(R.id.image_tag).toString().equals(imagePath)) {
            flag = false;
        }
        imageView.setTag(R.id.image_tag, imagePath);
        return flag;
    }

    /**
     * 使用备注名格式化昵称
     *
     * @param context
     * @param userId
     * @param defaultNickName
     * @return
     */
    public static String formatNickName(Context context, String userId, String defaultNickName) {
        AppContext ac = (AppContext) context.getApplicationContext();
        if (!ac.isLogin()) {
            return defaultNickName;
        }

        Remark remark = RemarkCache.getInstance(context).get(userId);
        if (remark != null) {
            return remark.getRemark();
        } else {
            return defaultNickName;
        }
    }

    public static String getRemark(Context context, String userId) {
        AppContext ac = (AppContext) context.getApplicationContext();
        if (ac.isLogin()) {
            Remark remark = RemarkCache.getInstance(context).get(userId);
            if (remark != null) {
                return remark.getRemark();
            } else {
                User user = UserCache.getInstance(context).get(userId);
                if (user != null) {
                    return user.getNickname();
                } else {
                    return "";
                }
            }
        } else {
            User user = UserCache.getInstance(context).get(userId);
            if (user != null) {
                return user.getNickname();
            } else {
                return "";
            }
        }
    }

    public static String getHead(Context context, String userId) {
        User user = UserCache.getInstance(context).get(userId);
        if (user != null) {
            return StringUtils.getUri(user.getHead());
        } else {
            return "";
        }
    }

    /**
     * 根据城市名称和省份名称获取城市id和省id
     *
     * @param cityName
     * @return
     */
    public static String[] getIdByName(String provinceName, String cityName) {
        ConfigManager configManager = ConfigManager.getConfigManager();
        String provinceId = "";
        String cityId = "";
        if (!TextUtils.isEmpty(provinceName) && !TextUtils.isEmpty(cityName)) {
            String pId = configManager.queryRId(provinceName);
            String cId = configManager.queryRId(cityName);

            if (!TextUtils.isEmpty(pId) && !TextUtils.isEmpty(cId)) {
                //非直辖市情况
                provinceId = pId;
                cityId = cId;
            } else if (!TextUtils.isEmpty(pId) && TextUtils.isEmpty(cId)) {
                //直辖市
                provinceId = pId;
            }
        } else if (!TextUtils.isEmpty(provinceName) && TextUtils.isEmpty(cityName)) {
            //直辖市
            String pId = configManager.queryRId(provinceName);
            if (!TextUtils.isEmpty(pId)) {
                provinceId = pId;
            }
        }
        return new String[]{provinceId, cityId};
    }

    /**
     * 坐标是否合法
     *
     * @param lat
     * @param lon
     * @return
     */
    public static boolean istLocValid(String lat, String lon) {
        return "4.9E-324".equals(lat) || "4.9E-324".equals(lon) || TextUtils.isEmpty(lat) || TextUtils.isEmpty(lon);
    }

    /**
     * 跳转地图app
     *
     * @param context
     * @param lat
     * @param lon
     */
    public static void goToMap(final Activity context, final String lat, final String lon, final String title, final String address) {
        Uri uri = Uri.parse("geo:" + lat + "," + lon + "(" + address + ")");
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = resolveInfos.size() > 0;
        if (isIntentSafe) {
            if (resolveInfos.size() == 1) {
                context.startActivity(intent);
            } else {
                ArrayList<String> items = new ArrayList<String>();
                for (int i = 0; i < resolveInfos.size(); i++) {
                    items.add(resolveInfos.get(i).loadLabel(packageManager).toString());
                }
                new ListActionDialog(context).config(items, new ListActionDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        //使用geo协议调用会有偏差，因此改用各平台api实现
                        String packageName = resolveInfos.get(position).activityInfo.packageName;
                        try {
                            if ("com.autonavi.minimap".equals(packageName)) {
                                //高德地图
                                Intent intent = new Intent();
                                intent.setData(Uri.parse("androidamap://viewMap?sourceApplication=" + context.getResources().getString(R.string.app_name) + "&poiname=" + address + "&lat=" + lat + "&lon=" + lon + "&dev=1"));
                                context.startActivity(intent);
                            } else if ("com.baidu.BaiduMap".equals(packageName)) {
                                //百度地图
                                Intent intent = Intent.getIntent("intent://map/marker?location=" + lat + "," + lon + "&title=" + address + "&content=" + address + "&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                                context.startActivity(intent);
                            } else if ("com.tencent.map".equals(packageName)) {
                                //腾讯地图
                                intent.setPackage(packageName);
                                context.startActivity(intent);
                            } else {
                                intent.setPackage(packageName);
                                context.startActivity(intent);
                            }
                        } catch (URISyntaxException e) {
                            intent.setPackage(packageName);
                            context.startActivity(intent);
                        }
                    }
                }).show();
            }
        } else {
            Uri webUri = Uri.parse("http://api.map.baidu.com/marker?location=" + lat + "," + lon + "&title=" + title + "&content=" + address + "&output=html");
            Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
            context.startActivity(webIntent);
        }
    }

    /**
     * 分享动态
     *
     * @param context
     * @param bean
     */
    public static void shareDynamic(Context context, DynamicBean bean) {
        String nickname = bean.getPublisherInfo().getNickname();
        String title = "";
        String content = "";
        String dataUrl = Const.URL_DYNAMIC_DETAIL + bean.getId();
        String imageUrl = "";
        String smsContent = "分享给你一个动态，点按查看详情：" + Const.URL_DYNAMIC_DETAIL + bean.getId() + "，来自\"来了\"App--最专业的活动聚会平台。";
        String emailContent = "分享给你一个动态 ，点按查看详情：" + Const.URL_DYNAMIC_DETAIL + bean.getId() + "，来自\"来了\"App--最专业的活动聚会平台。";
        String emailTopic = "分享给你一个动态";
        String weixinMomentTitle = "";
        String weiboContent = "";

        //通知动态处理(特殊处理)
        if (Const.DYNA_TYPE_NOTICE.equals(bean.getDynaType())) {
            title = bean.getNoticeInfo().getTopic();
            content = bean.getNoticeInfo().getDetail();
            if (!TextUtils.isEmpty(bean.getPublisherInfo().getHeadSPicName())) {
                imageUrl = ApiClient.getFileUrl(bean.getPublisherInfo().getHeadSPicName());
            } else {
                imageUrl = FileUtils.getImageDir(context) + "/logo.png";
            }
            weixinMomentTitle = title;
            weiboContent = bean.getNoticeInfo().getTopic() + "," + bean.getNoticeInfo().getDetail() + Const.URL_DYNAMIC_DETAIL + bean.getId();
            UIHelper.showShare(context, title, content, dataUrl, imageUrl, smsContent, emailContent, emailTopic, weixinMomentTitle, weiboContent);
            return;
        }


//        pic 优先级：图片组的第一张图、视频预览图、动态发表人头像、来了logo
//        title 优先级：文本、投票主题、日程主题、语音文本（为空是往下走）、地址、[动态发表人的昵称]的动态
//        content 优先级（排除掉title中的内容）：文本、投票主题、日程主题、语音文本（为空是往下走）、地址、活动来了，群组来了，最专业的活动聚会平台-来了。

        Comparator<DynamicBean.PublishInfo> comparator = new Comparator<DynamicBean.PublishInfo>() {
            @Override
            public int compare(DynamicBean.PublishInfo lhs, DynamicBean.PublishInfo rhs) {
                return Integer.parseInt(rhs.getPublishType()) - Integer.parseInt(lhs.getPublishType());
            }
        };
        ArrayList<DynamicBean.PublishInfo> sortedPublishList = (ArrayList<DynamicBean.PublishInfo>) bean.getPublishList().clone();
        Collections.sort(sortedPublishList, comparator);

        int textIndex = Collections.binarySearch(sortedPublishList, new DynamicBean.PublishInfo(Const.PUBLISH_TYPE_TEXT), comparator);
        int imageIndex = Collections.binarySearch(sortedPublishList, new DynamicBean.PublishInfo(Const.PUBLISH_TYPE_IMAGE), comparator);
        int videoIndex = Collections.binarySearch(sortedPublishList, new DynamicBean.PublishInfo(Const.PUBLISH_TYPE_VIDEO), comparator);
        int voiceIndex = Collections.binarySearch(sortedPublishList, new DynamicBean.PublishInfo(Const.PUBLISH_TYPE_VOICE), comparator);
        int addressIndex = Collections.binarySearch(sortedPublishList, new DynamicBean.PublishInfo(Const.PUBLISH_TYPE_ADDRESS), comparator);
        int scheduleIndex = Collections.binarySearch(sortedPublishList, new DynamicBean.PublishInfo(Const.PUBLISH_TYPE_SCHEDULE), comparator);
        int voteIndex = Collections.binarySearch(sortedPublishList, new DynamicBean.PublishInfo(Const.PUBLISH_TYPE_VOTE), comparator);


//        pic 优先级：图片组的第一张图、视频预览图、动态发表人头像、来了logo
        if (imageIndex >= 0) {
            imageUrl = ApiClient.getFileUrl(sortedPublishList.get(imageIndex).getPics().get(0).getFilename());
        } else if (videoIndex >= 0) {
            imageUrl = ApiClient.getFileUrl(sortedPublishList.get(videoIndex).getPreviewPic());
        } else if (!TextUtils.isEmpty(bean.getPublisherInfo().getHeadSPicName())) {
            imageUrl = ApiClient.getFileUrl(bean.getPublisherInfo().getHeadSPicName());
        } else {
            imageUrl = FileUtils.getImageDir(context) + "/logo.png";
        }
//       title 优先级：文本、投票主题、日程主题、语音文本（为空是往下走）、地址、[动态发表人的昵称]的动态
        if (textIndex >= 0) {
            title = sortedPublishList.get(textIndex).getContent();
            sortedPublishList.remove(textIndex);
        } else if (voteIndex >= 0) {
            title = sortedPublishList.get(voteIndex).getTopic();
            sortedPublishList.remove(voteIndex);
        } else if (scheduleIndex >= 0) {
            title = sortedPublishList.get(scheduleIndex).getTopic();
            sortedPublishList.remove(scheduleIndex);
        } else if (voiceIndex >= 0 && !TextUtils.isEmpty(sortedPublishList.get(voiceIndex).getText())) {
            title = sortedPublishList.get(voiceIndex).getText();
            sortedPublishList.remove(voiceIndex);
        } else if (addressIndex >= 0) {
            title = sortedPublishList.get(addressIndex).getAddress();
            sortedPublishList.remove(addressIndex);
        } else {
            title = nickname + "的动态";
        }
        weixinMomentTitle = title;
        weiboContent = title + Const.URL_DYNAMIC_DETAIL + bean.getId();


        textIndex = Collections.binarySearch(sortedPublishList, new DynamicBean.PublishInfo(Const.PUBLISH_TYPE_TEXT), comparator);
        imageIndex = Collections.binarySearch(sortedPublishList, new DynamicBean.PublishInfo(Const.PUBLISH_TYPE_IMAGE), comparator);
        videoIndex = Collections.binarySearch(sortedPublishList, new DynamicBean.PublishInfo(Const.PUBLISH_TYPE_VIDEO), comparator);
        voiceIndex = Collections.binarySearch(sortedPublishList, new DynamicBean.PublishInfo(Const.PUBLISH_TYPE_VOICE), comparator);
        addressIndex = Collections.binarySearch(sortedPublishList, new DynamicBean.PublishInfo(Const.PUBLISH_TYPE_ADDRESS), comparator);
        scheduleIndex = Collections.binarySearch(sortedPublishList, new DynamicBean.PublishInfo(Const.PUBLISH_TYPE_SCHEDULE), comparator);
        voteIndex = Collections.binarySearch(sortedPublishList, new DynamicBean.PublishInfo(Const.PUBLISH_TYPE_VOTE), comparator);

//       content 优先级（排除掉title中的内容）：文本、投票主题、日程主题、语音文本（为空是往下走）、地址、活动来了，群组来了，最专业的活动聚会平台-来了。
        if (textIndex >= 0) {
            content = sortedPublishList.get(textIndex).getContent();
        } else if (voteIndex >= 0) {
            content = sortedPublishList.get(voteIndex).getTopic();
        } else if (scheduleIndex >= 0) {
            content = sortedPublishList.get(scheduleIndex).getTopic();
        } else if (voiceIndex >= 0 && !TextUtils.isEmpty(sortedPublishList.get(voiceIndex).getText())) {
            content = sortedPublishList.get(voiceIndex).getText();
        } else if (addressIndex >= 0) {
            content = sortedPublishList.get(addressIndex).getAddress();
        } else {
            content = "活动来了，群组来了，最专业的活动聚会平台-来了";
        }


        UIHelper.showShare(context, title, content, dataUrl, imageUrl, smsContent, emailContent, emailTopic, weixinMomentTitle, weiboContent);
    }

    /**
     * 够着创建活动缓存名称
     *
     * @param userId
     * @param typeId
     * @return
     */
    public static String buildCreateTypeCacheName(String userId, String parentId, String typeId) {
        if (TextUtils.isEmpty(parentId)) {
            //非群内活动
            return userId + "_" + typeId;
        } else {
            //群内活动
            return userId + "_" + parentId + "_" + typeId;
        }
    }

    /**
     * 获得本地图片的宽高
     */
    public static int[] getImageSize(String imagePath) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, options);
            return new int[]{options.outWidth, options.outHeight};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[]{0, 0};
    }

    public static String formatDuration(String duration) {
        String d = "00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        try {
            Date date = new Date(Integer.parseInt(duration) * 1000);
            d = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

}
