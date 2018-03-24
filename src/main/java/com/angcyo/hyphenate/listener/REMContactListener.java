package com.angcyo.hyphenate.listener;

import com.angcyo.uiview.utils.ThreadExecutor;
import com.hyphenate.EMContactListener;

/**
 * Created by angcyo on 2018/03/24 22:22
 */
public class REMContactListener implements EMContactListener {

    @Override
    public void onContactInvited(final String username, final String reason) {
        //收到好友邀请
    }

    @Override
    public void onFriendRequestAccepted(String username) {
        //好友请求被同意
    }

    @Override
    public void onFriendRequestDeclined(String username) {
        //好友请求被拒绝
    }

    @Override
    public void onContactDeleted(final String username) {
        //被删除时回调此方法
        ThreadExecutor.instance().onMain(new Runnable() {
            @Override
            public void run() {
                onContactChange("delete", username);
            }
        });
    }

    @Override
    public void onContactAdded(final String username) {
        //增加了联系人时回调此方法
        ThreadExecutor.instance().onMain(new Runnable() {
            @Override
            public void run() {
                onContactChange("add", username);
            }
        });
    }

    public void onContactChange(String action, String username) {

    }
}
