package com.lailem.app.zxing;

import android.app.Activity;
import android.content.Intent;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.lailem.app.AppManager;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.ui.main.MainActivity;
import com.lailem.app.utils.AcceptInviteManager;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.socks.library.KLog;

public class ResultHandler {

    public ResultHandler(Activity activity, Result result) {
        ParsedResult parsedResult = parseResult(result);
//		switch (parsedResult.getType()) {
//		case ADDRESSBOOK:
//		case EMAIL_ADDRESS:
//		case PRODUCT:
//		case URI:
//		case WIFI:
//		case GEO:
//		case TEL:
//		case SMS:
//		case CALENDAR:
//		case ISBN:
//		default:
//		}
        KLog.i(result.getText().toString());
        switch (parsedResult.getType()) {
            case URI:
                if (result.getText().contains(Const.VOTE_ACTIVE_PATTERN)) {
                    //投票活动二维码
                    UIHelper.showVoteActiveDetail(activity, result.getText().substring(result.getText().lastIndexOf("/") + 1), true);
                } else if (result.getText().contains(Const.GROUP_PATTERN)) {
                    //群组二维码
                    UIHelper.showGroupHome(activity, result.getText().substring(result.getText().lastIndexOf("/") + 1), true);
                } else if (result.getText().contains(Const.ACTIVE_PATTERN)) {
                    //活动二维码
                    UIHelper.showActiveDetail(activity, result.getText().substring(result.getText().lastIndexOf("/") + 1), true);
                } else if (result.getText().contains(Const.URL_INVITE_LETTER)) {
                    //邀请
                    String inviteCode = result.getText().replace(Const.URL_INVITE_LETTER, "");
                    AcceptInviteManager.getInstance().init((BaseActivity) AppManager.getActivity(MainActivity.class), inviteCode);
                } else {
                    Intent intent = new Intent(activity, CaptureRusultActivity.class);
                    intent.putExtra(CaptureRusultActivity.BUNDLE_KEY_CONTENT, result.getText());
                    activity.startActivity(intent);
                }
                break;
            default:
                Intent intent = new Intent(activity, CaptureRusultActivity.class);
                intent.putExtra(CaptureRusultActivity.BUNDLE_KEY_CONTENT, result.getText());
                activity.startActivity(intent);
                break;
        }
    }

    private static ParsedResult parseResult(Result rawResult) {
        return ResultParser.parseResult(rawResult);
    }
}
