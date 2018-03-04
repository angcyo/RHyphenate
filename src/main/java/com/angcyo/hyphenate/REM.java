package com.angcyo.hyphenate;

import android.app.Application;

import com.angcyo.library.utils.L;
import com.angcyo.uiview.net.Func;
import com.angcyo.uiview.net.RException;
import com.angcyo.uiview.net.RSubscriber;
import com.angcyo.uiview.net.Rx;
import com.angcyo.uiview.utils.ThreadExecutor;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import rx.Observable;
import rx.Observer;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：环信SDK封装
 * 创建人员：Robi
 * 创建时间：2018/02/22 15:51
 * 修改人员：Robi
 * 修改时间：2018/02/22 15:51
 * 修改备注：
 * Version: 1.0.0
 */
public class REM {
    public static void init(Application application, boolean debug) {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);

        //初始化
        EMClient.getInstance().init(application, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(debug);
    }

    public static Observable register(final String username, final String pwd) {
        return Rx.create(new Func<String>() {
            @Override
            public String call(Observer observer) {
                try {
                    EMClient.getInstance().createAccount(username, pwd);
                } catch (HyphenateException e) {
                    observer.onError(e);
                }
                return username;
            }
        });
    }

    public static void login(final String username, final String pwd, final RSubscriber<String> subscriber) {
        subscriber.onStart();
        EMClient.getInstance().login(username, pwd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                ThreadExecutor.instance().onMain(new Runnable() {
                    @Override
                    public void run() {
                        subscriber.onCompleted();
                        subscriber.onNext("");
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
                L.e("onProgress() -> " + progress + " " + status);
                ThreadExecutor.instance().onMain(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onError(final int code, final String message) {
                ThreadExecutor.instance().onMain(new Runnable() {
                    @Override
                    public void run() {
                        subscriber.onError(new RException(code, message));
                    }
                });
            }
        });
    }

}
