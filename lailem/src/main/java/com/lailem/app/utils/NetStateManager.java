package com.lailem.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
//import android.telephony.cdma.CdmaCellLocation;
//import android.telephony.gsm.GsmCellLocation;

import com.socks.library.KLog;

public class NetStateManager {
    private static NetStateManager instance;
    private boolean isAvailable, isWifiAvailable, isMobileAvailable;
    private int baseStationId;
    Context context;
    ConnectivityManager cm;

    private boolean isCellLocationChanged = false;

//    public PhoneStateListener celllistener = new PhoneStateListener() {
//        @Override
//        public void onCellLocationChanged(CellLocation location) {
//            super.onCellLocationChanged(location);
//            // 判断 location的类型 是GsmCellLocation 还是 CdmaCellLocation
//            KLog.i("baseStationId:::" + baseStationId);
//            if (location instanceof GsmCellLocation) {
//                GsmCellLocation gsmCellLocation = (GsmCellLocation) location;
//                baseStationId = gsmCellLocation.getCid();
//                KLog.i("gsmCellLocation baseStationId:::" + baseStationId);
//            } else if (location instanceof CdmaCellLocation) {
//                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) location;
//                baseStationId = cdmaCellLocation.getBaseStationId();
//                KLog.i("CdmaCellLocation baseStationId:::" + baseStationId);
//            }
//            isCellLocationChanged = true;
//        }
//    };

    private NetStateManager() {

    }

    public static NetStateManager getInstance() {
        if (instance == null)
            instance = new NetStateManager();
        return instance;
    }

    public void initNetworkState(Context context) {
        this.context = context;
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (getWifiNetworkInfo().isConnected()) {
            isAvailable = true;
            isWifiAvailable = true;
            isMobileAvailable = false;
        } else if (getMobileNetworkInfo().isConnected()) {
            isAvailable = true;
            isWifiAvailable = false;
            isMobileAvailable = true;

        } else {
            isAvailable = false;
        }

        // TODO 双卡的基站监听 怎样解决
        //TODO 暂时注释掉基站变化对网络的影响
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        telephonyManager.listen(celllistener, PhoneStateListener.LISTEN_CELL_LOCATION); // 基站位置的变化

        KLog.i("初始化网络状态：isAvailable：：：" + isAvailable + ",isWifiAvailable:::" + isWifiAvailable + ",isMobileAvailable:::" + isMobileAvailable);
    }

    private NetworkInfo getWifiNetworkInfo() {
        return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    }

    private NetworkInfo getMobileNetworkInfo() {
        return cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean isWifiAvailable() {
        return isWifiAvailable;
    }

    public boolean isMobileAvailable() {
        return isMobileAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setWifiAvailable(boolean isWifiAvailable) {
        this.isWifiAvailable = isWifiAvailable;
    }

    public void setMobileAvailable(boolean isMobileAvailable) {
        this.isMobileAvailable = isMobileAvailable;
    }

    private void resetAvailableState() {
        isWifiAvailable = getWifiNetworkInfo().isConnected();
        isMobileAvailable = getMobileNetworkInfo().isConnected();
        isAvailable = isWifiAvailable || isMobileAvailable;

        isCellLocationChanged = false;//重置基站变换
    }


    /**
     * 是否需要重新连接
     */
    public boolean isNeedReconn() {
        KLog.i("原来的网络状态：isAvailable：：：" + isAvailable + ",isWifiAvailable:::" + isWifiAvailable + ",isMobileAvailable:::" + isMobileAvailable);
        boolean isNeedReconn = false;

        if (isAvailable) {// 原来的网络可用
            if (isWifiAvailable) {// 原来wifi可用
                if (isMobileAvailableForNow()) {// 现在手机可用
                    isNeedReconn = true;
                }
            } else if (isMobileAvailable) {// 原来手机可用
                if (isWifiAvailableForNow()) {// 现在wifi可用
                    isNeedReconn = true;
                }
                if (isCellLocationChanged) {//基站变换
                    isNeedReconn = true;
                }
            }
        } else {// 原来的网络不可用
            if (isAvailableForNow()) {// 现在的网络可用
                isNeedReconn = true;
            }
        }
        //重置各可用状态
        resetAvailableState();
        KLog.i("现在的网络状态：isAvailable：：：" + isAvailable + ",isWifiAvailable:::" + isWifiAvailable + ",isMobileAvailable:::" + isMobileAvailable);
        return isNeedReconn;
    }

    public boolean isAvailableForNow() {
        if (getWifiNetworkInfo().isConnected() || getMobileNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isWifiAvailableForNow() {
        if (getWifiNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMobileAvailableForNow() {
        if (getMobileNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}
