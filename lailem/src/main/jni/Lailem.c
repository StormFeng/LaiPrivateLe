#include <jni.h>

static const int environment = 2;//1:正式 2:公网测试 3:局域网测试



jstring Java_com_lailem_app_jni_JniSharedLibrary_login(JNIEnv *env,
                                                       jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/login,account,password");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_register(JNIEnv *env,
                                                          jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/register,authCode,birthday,cityId,head,nickname,password,phone,provinceId,sex,transId");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_loginThird(JNIEnv
                                                    *env,
                                                    jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/thirdLogin,headBUrl,nickname,openId,type,unionId,birthday,cityId,provinceId,sex");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_authCode(JNIEnv
                                                  *env,
                                                  jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/authCode,authType,phone");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_changePhone(
        JNIEnv
        *env,
        jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/authCode/changePhone,phone,phoneType");
}

jstring
Java_com_lailem_app_jni_JniSharedLibrary_verifyAuthCode(
        JNIEnv
        *env,
        jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************verify/authCode,authCode,phone,transId");
}

jstring
Java_com_lailem_app_jni_JniSharedLibrary_bindPhone(JNIEnv
                                                   *env,
                                                   jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/bindingPhone,authCode,password,phone,transId");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_changePersonInfo(
        JNIEnv
        *env,
        jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/changePersonInfo,birthday,cityId,email,name,nickname,personalizedSignature,provinceId,sex");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_forgetPassword(
        JNIEnv
        *env,
        jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/forgetPassword,authCode,password,phone,transId");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_changPhone(JNIEnv
                                                    *env,
                                                    jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/changPhone,newAuthCode,newPhone,oldAuthCode,oldPhone");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_setUsername(
        JNIEnv
        *env,
        jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/setUsername,username");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_schedule(JNIEnv
                                                  *env,
                                                  jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/schedule");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_changeHead(JNIEnv
                                                    *env,
                                                    jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/changeHead,head");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_addBlack(JNIEnv
                                                  *env,
                                                  jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/blacklist/add,blackUserId");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_setPermission(
        JNIEnv
        *env,
        jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/setPermission");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_submitPosition(
        JNIEnv
        *env,
        jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/submitPosition,address,lat,lon");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_blacklist(JNIEnv
                                                   *env,
                                                   jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/blacklist");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_blacklistIds(
        JNIEnv
        *env,
        jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/blacklistIds");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_removeBlack(
        JNIEnv
        *env,
        jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/blacklist/remove,blackUserId");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_setRemark(JNIEnv
                                                   *env,
                                                   jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/setRemark,rem,remUserId");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_collectList(
        JNIEnv
        *env,
        jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/collect,pageCount,pageNo");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_collectDel(JNIEnv
                                                    *env,
                                                    jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/delete/collect/:id");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_collect(JNIEnv
                                                 *env,
                                                 jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/collect/:id,cType");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_deleteMyDynamic(
        JNIEnv
        *env,
        jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/dynamic/del/:id");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_getMyDynamicList(
        JNIEnv
        *env,
        jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/dynamic,pageCount,pageNo");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_homePage(JNIEnv
                                                  *env,
                                                  jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************homePage");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_fold(JNIEnv
                                              *env,
                                              jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************group/near/fold,lat,lon,pageCount,pageNo,tagId,timeFilter");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_near(JNIEnv
                                              *env,
                                              jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************activity/near,lat,lon,pageCount,pageNo,sort,timeFilter,typeId");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_getGroupActiveList(
        JNIEnv
        *env,
        jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************group/:id/activity,pageCount,pageNo,userId");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_activity(JNIEnv
                                                  *env,
                                                  jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************activity/:id/,userId");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_getVoteActiveDetail(JNIEnv
                                                             *env,
                                                             jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************vote/activity/:id/,userId");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_activity_recommond(
        JNIEnv
        *env,
        jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************activity/recommond,askType,lat,lon,pageCount,pageNo");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_apply(JNIEnv
                                               *env,
                                               jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************user/:id/group/:id/apply,applyText,applyVoice,duration,name,phone");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_membe(JNIEnv
                                               *env,
                                               jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************group/:id/member,pageCount,pageNo,userId");
}

jstring
Java_com_lailem_app_jni_JniSharedLibrary_memberInfo(JNIEnv
                                                    *env,
                                                    jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************other/user/:id,userId");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_group_recommond(
        JNIEnv
        *env,
        jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************group/recommond,askType,lat,lon,pageCount,pageNo");
}


jstring
Java_com_lailem_app_jni_JniSharedLibrary_nofold(JNIEnv
                                                *env,
                                                jobject thiz
) {
    return (*env)->
            NewStringUTF(env,
                         "****************************************group/near/nofold,lat,lon,pageCount,pageNo,sort,tagId");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_group(JNIEnv *env,
                                                       jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************group/:id/,userId");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_verifyWay(JNIEnv *env,
                                                           jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/verifyWay");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_adsList(JNIEnv *env,
                                                         jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************home/adsList");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_groupInviteCode(JNIEnv *env,
                                                                 jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/inviteCode");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_acceptInvite(JNIEnv *env,
                                                              jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/acceptInvite,inviteCode,inviterId");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_isJoinedGroup(JNIEnv *env,
                                                               jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/isJoined");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_syncGroup(JNIEnv *env,
                                                           jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/synchro/group,groupIds");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_groupBrief(JNIEnv *env,
                                                            jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/brief");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_type(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************group/cwtemplate/type");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_cwtemplate(JNIEnv *env,
                                                            jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************group/cwtemplate/:id/");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_group_cwtemplate_type(
        JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************group/cwtemplate/type/:id,pageCount,pageNo");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_group_cw(JNIEnv *env,
                                                          jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/cw/");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_quit(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/quit");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_disbandGroup(JNIEnv *env,
                                                              jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/disband,authCode,transId,password");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_getInviteInfo(JNIEnv *env,
                                                               jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************group/invitation/:inviteCode");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_activityList(JNIEnv *env,
                                                              jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************user/:id/activity");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_applyVerify(JNIEnv *env,
                                                             jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/applyVerify,applyId,verifyStatus");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_getout(JNIEnv *env,
                                                        jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/getout,memberId");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_cancelManager(JNIEnv *env,
                                                               jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/cancelManager,cancelManagerId");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_setManager(JNIEnv *env,
                                                            jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/setManager,toManagerId");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_setPic(JNIEnv *env,
                                                        jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/setPic,pic,picMaterialId");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_createGroup(JNIEnv *env,
                                                             jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/new,address,contact,endTime,groupType,intro,lat,lon,name,parentId,permission,pic,picMaterialId,startTime,typeId,typeName,joinNeedContact,joinVerify");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_createVoteActive(JNIEnv *env,
                                                                  jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/voteGroup/new,endTime,name,pic,picMaterialId,voteInfo");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_setNickname(JNIEnv *env,
                                                             jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/setNickname,nickname");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_onGroup(JNIEnv *env,
                                                         jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************user/:id/group");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_groupSchedule(JNIEnv *env,
                                                               jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/schedule,pageCount,pageNo");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_setTag(JNIEnv *env,
                                                        jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/setTag,tagIds");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_changGroupInfo(JNIEnv *env,
                                                                jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/changGroupInfo,intro,name,address,lat,lon");
}

jstring Java_com_lailem_app_jni_JniSharedLibrary_changProperty(JNIEnv *env,
                                                                jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/changProperty,createActivityFlay");
}

jstring Java_com_lailem_app_jni_JniSharedLibrary_changActiveProperty(JNIEnv *env,
                                                               jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/activity/:id/changProperty,applyFlay,joinNeedContact,name,phone");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_setVerifyWay(JNIEnv *env,
                                                              jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/group/:id/setVerifyWay,verifyWay");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_getGroupDatabase(JNIEnv *env,
                                                                  jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************group/:id/database,pageCount,pageNo,type");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_dynamic(JNIEnv *env,
                                                         jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/public/dynamic,contentList,fileKey,groupId,dynaForm");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_notice(JNIEnv *env,
                                                        jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/public/notice,detail,groupId,topic");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_groupDynamic(JNIEnv *env,
                                                              jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************group/:id/dynamic,contentType,pageCount,pageNo,userId");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_reprint(JNIEnv *env,
                                                         jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************dynamic/:id/reprint,groupId,userId,whither");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_onDynamic(JNIEnv *env,
                                                           jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************dynamic/:id,userId");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_getGroupNoticeList(JNIEnv *env,
                                                                    jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************group/:id/notice,pageCount,pageNo,userId");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_comment(JNIEnv *env,
                                                         jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/comment/:id,comId,comType,comment");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_likeList(JNIEnv *env,
                                                          jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************like/:id,likeType,pageCount,pageNo");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_commentList(JNIEnv *env,
                                                             jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************comment/:id,comType,pageCount,pageNo");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_like(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/like/:id,likeType");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_deleteComment(JNIEnv *env,
                                                               jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/delete/comment/:id");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_vote(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/vote/:id,voteItemIds");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_voterList(JNIEnv *env,
                                                           jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************vote/:id/voter");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_randomOne(JNIEnv *env,
                                                           jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************picMaterial/randomOne");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_picMaterialType(JNIEnv *env,
                                                                 jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************picMaterial/type");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_picMaterialList(JNIEnv *env,
                                                                 jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************picMaterial/type/:id");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_commonConfig(JNIEnv *env,
                                                              jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************common/config,version");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_configVersion(JNIEnv *env,
                                                               jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************common/config/version");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_versionCheck(JNIEnv *env,
                                                              jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************version/android/check,vCode");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_getUserBrief(JNIEnv *env,
                                                              jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************getUserBrief/:id,userId");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_getRemarks(JNIEnv *env,
                                                            jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************user/:id/remarks");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_conversationAdd(JNIEnv *env,
                                                                 jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/conversation/add,objId,objType");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_conversationList(JNIEnv *env,
                                                                  jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/conversation");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_deleteConversation(JNIEnv *env,
                                                                    jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************conversation/:id/delete");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_uploadChatFile(JNIEnv *env,
                                                                jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************upload/chat/file,file,fileType,duration,userId");
}

jstring Java_com_lailem_app_jni_JniSharedLibrary_uploadPicFile(JNIEnv *env,
                                                               jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/upload/pic,file");
}

jstring Java_com_lailem_app_jni_JniSharedLibrary_updatePassword(JNIEnv *env,
                                                                jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/:id/updatePassword,oldPassword,newPassword");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_getFileUrl(JNIEnv *env,
                                                            jobject thiz) {
    switch (environment) {
        case 1:
            return (*env)->NewStringUTF(env,
                                        "****************************************http://file.lailem.com/");
        case 2:
            return (*env)->NewStringUTF(env,
                                        "****************************************http://laile-test.oss-cn-hangzhou.aliyuncs.com/");
        case 3:
            return (*env)->NewStringUTF(env,
                                        "****************************************http://laile-test-bucket.oss-cn-hangzhou.aliyuncs.com/");
        default:
            return (*env)->NewStringUTF(env,
                                        "****************************************http://laile-test-bucket.oss-cn-hangzhou.aliyuncs.com/");
    }
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_dbName(JNIEnv *env,
                                                        jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************hfdskaoknalf");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_hostForChat(JNIEnv *env,
                                                             jobject thiz) {
    switch (environment) {
        case 1:
            return (*env)->NewStringUTF(env,
                                        "****************************************120.55.205.185");
        case 2:
            return (*env)->NewStringUTF(env,
                                        "****************************************192.168.0.188");
        case 3:
            return (*env)->NewStringUTF(env,
                                        "****************************************192.168.1.195");
        default:
            return (*env)->NewStringUTF(env,
                                        "****************************************192.168.1.195");
    }

}


jstring Java_com_lailem_app_jni_JniSharedLibrary_portForChat(JNIEnv *env,
                                                             jobject thiz) {
    switch (environment) {
        case 1:
            return (*env)->NewStringUTF(env, "****************************************80");;
        case 2:
            return (*env)->NewStringUTF(env, "****************************************5222");
        case 3:
            return (*env)->NewStringUTF(env, "****************************************5222");
        default:
            return (*env)->NewStringUTF(env, "****************************************5222");
    }
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_domainForChat(JNIEnv *env,
                                                               jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************@laile");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_subDomainForChat(JNIEnv *env,
                                                                  jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************/Smack");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_domain(JNIEnv *env,
                                                        jobject thiz) {
    switch (environment) {
        case 1:
            return (*env)->NewStringUTF(env,
                                        "****************************************http://app.lailem.com/LaiLeApp/");;
        case 2:
            return (*env)->NewStringUTF(env,
                                        "****************************************http://192.168.0.188:8080/LaiLeApp/");
        case 3:
            return (*env)->NewStringUTF(env,
                                        "****************************************http://192.168.1.115:8080/LaiLeApp/");
        default:
            return (*env)->NewStringUTF(env,
                                        "****************************************http://192.168.1.115:8080/LaiLeApp/");
    }
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_domainWeb(JNIEnv *env,
                                                           jobject thiz) {
    switch (environment) {
        case 1:
            return (*env)->NewStringUTF(env,
                                        "****************************************http://m.lailem.com/");;
        case 2:
            return (*env)->NewStringUTF(env,
                                        "****************************************http://192.168.0.188:8080/LaiLeWeb/");
        case 3:
            return (*env)->NewStringUTF(env,
                                        "****************************************http://192.168.1.115:8080/LaiLeWeb/");
        default:
            return (*env)->NewStringUTF(env,
                                        "****************************************http://192.168.1.115:8080/LaiLeWeb/");
    }
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_ckValue(JNIEnv *env,
                                                         jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************L9ilw64TG6");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_notificationPublisherId(
        JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************00000000000000000000000000010000");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_callbackReceiverId(JNIEnv *env,
                                                                    jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************00000000000000000000000000010002");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_ckKey(JNIEnv *env,
                                                       jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************clientKey");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_tKey(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************token");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_feedback(JNIEnv *env,
                                                          jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/feedback,content,fbItemId,pic,userId");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_feedbackType(JNIEnv *env,
                                                              jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************getFeedback/btype");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_feedbackTypeSpecial(
        JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************getFeedback/stype/:id");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_addComplain(JNIEnv *env,
                                                             jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************user/complain,content,isAnonymous,objId,objType,typeId,userId");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_getComplainType(JNIEnv *env,
                                                                 jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************getComplainType");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_ciaAppId(JNIEnv *env,
                                                          jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************96aa0034d28646febf94ba94080f07f7");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_ciaAuthCode(JNIEnv *env,
                                                             jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************48d117ac012c46f7af022f6abfd8e84b");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_weiboApiKey(JNIEnv *env,
                                                             jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************3798886952");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_weiboRedirectUrl(JNIEnv *env,
                                                                  jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************http://sns.whalecloud.com/sina2/callback");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_weiboScope(JNIEnv *env,
                                                            jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_qqAppId(JNIEnv *env,
                                                         jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************101254730");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_weixinAppId(JNIEnv *env,
                                                             jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************wx94fff56395ea9707");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_weixinAppSecret(JNIEnv *env,
                                                                 jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************67fb0454e6d36fc88229d54785ad5775");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_umengAppkey(JNIEnv *env,
                                                             jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "****************************************55ee44e367e58e1485006516");
}


jstring Java_com_lailem_app_jni_JniSharedLibrary_umengChannel(JNIEnv *env,
                                                              jobject thiz) {
    return (*env)->NewStringUTF(env, "****************************************laile");
}

