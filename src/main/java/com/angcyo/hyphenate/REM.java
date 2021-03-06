package com.angcyo.hyphenate;

import android.app.Application;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.angcyo.realm.RRealm;
import com.angcyo.uiview.RApplication;
import com.angcyo.uiview.net.Func;
import com.angcyo.uiview.net.RException;
import com.angcyo.uiview.net.RSubscriber;
import com.angcyo.uiview.net.Rx;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMCheckType;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;

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
        //初始化数据库
        RRealm.init(application, true);

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);

        //options.messa(1);

        options.setAutoLogin(true);

        //初始化
        EMClient.getInstance().init(application, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(debug);

        registerConnectionListener();

        //联系人相关
        REMContacts.instance().init();

        //消息相关
        REMMessage.instance().init();

        //群组相关
        REMGroupManager.instance().init();
    }

    private static void registerConnectionListener() {
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int error) {
                if (error == EMError.USER_REMOVED) {
                    // 显示帐号已经被移除

                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    // 显示帐号在其他设备登录
                } else {
//                    if () {
//                        //连接不到聊天服务器
//                    } else {
//                        //当前网络不可用，请检查网络设置
//                    }
                }
            }
        });
    }

    /**
     * 注册账号
     */
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

    /**
     * 登录接口
     */
    public static void login(final String username, final String pwd, final RSubscriber<String> subscriber) {
        subscriber.onStart();
        EMClient.getInstance().login(username, pwd, new REMCallBack() {
            @Override
            public void onResult(boolean isError, int code, String message) {
                if (isError) {
                    subscriber.onError(new RException(code, message));
                } else {
                    initOnLoggedIn();
                    subscriber.onNext("");
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 登出
     */
    public static void logout(final RSubscriber<String> subscriber) {
        EMClient.getInstance().logout(true, new REMCallBack() {
            @Override
            public void onResult(boolean isError, int code, String message) {
                if (isError) {
                    subscriber.onError(new RException(code, message));
                } else {
                    callOnLoggedOut();
                    subscriber.onNext("");
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 是否已经登录成功
     */
    public static boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected();
    }

    /**
     * 登录成功之后, 调用此方法初始化一些数据
     */
    public static void initOnLoggedIn() {
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
    }

    /**
     * 登出操作后的处理
     */
    public static void callOnLoggedOut() {
        try {
            EMClient.getInstance().callManager().endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前登录成功的用户名
     */
    public static String getCurrentUserName() {
        return EMClient.getInstance().getCurrentUser();
    }

    /**
     * 环信服务器是否连接
     */
    public static boolean isConnected() {
        return EMClient.getInstance().isConnected();
    }

    public static void check(String currentUsername, String currentPassword, final Action1<String> onResult) {
        final StringBuilder builder = new StringBuilder("开始诊断:\n");
        EMClient.getInstance().check(currentUsername, currentPassword, new EMClient.CheckResultListener() {
            @Override
            public void onResult(int type, int result, String desc) {
                switch (type) {
                    case EMCheckType.ACCOUNT_VALIDATION: // Account validation.
                        if (result != EMError.EM_NO_ERROR) {
                            updateResultOnUiThread(builder, R.string.check_result_account_validate_fail, result, desc);
                        }
                        Rx.base(new Runnable() {
                            @Override
                            public void run() {
                                onResult.call(builder.toString());
                            }
                        });
                        break;
                    case EMCheckType.GET_DNS_LIST_FROM_SERVER: // Get dns list from server.
                        if (result == EMError.EM_NO_ERROR) {
                            updateResultOnUiThread(builder, R.string.check_result_get_dns_list_success, 0, null);
                        } else {
                            updateResultOnUiThread(builder, R.string.check_result_get_dns_list_fail, result, desc);
                        }
                        Rx.base(new Runnable() {
                            @Override
                            public void run() {
                                onResult.call(builder.toString());
                            }
                        });
                        break;
                    case EMCheckType.GET_TOKEN_FROM_SERVER: // Get token from server.
                        if (result == EMError.EM_NO_ERROR) {
                            updateResultOnUiThread(builder, R.string.check_result_get_token_success, 0, null);
                        } else {
                            updateResultOnUiThread(builder, R.string.check_result_get_token_fail, result, desc);
                        }
                        Rx.base(new Runnable() {
                            @Override
                            public void run() {
                                onResult.call(builder.toString());
                            }
                        });
                        break;
                    case EMCheckType.DO_LOGIN: // User login
                        if (result == EMError.EM_NO_ERROR) {
                            updateResultOnUiThread(builder, R.string.check_result_login_success, 0, null);
                        } else {
                            updateResultOnUiThread(builder, R.string.check_result_login_fail, result, desc);
                        }
                        Rx.base(new Runnable() {
                            @Override
                            public void run() {
                                onResult.call(builder.toString());
                            }
                        });
                        break;
                    case EMCheckType.DO_LOGOUT: // User logout
                        if (result == EMError.EM_NO_ERROR) {
                            updateResultOnUiThread(builder, R.string.check_result_logout_success, 0, null);
                        } else {
                            updateResultOnUiThread(builder, R.string.check_result_logout_fail, result, desc);
                        }
                        Rx.base(new Runnable() {
                            @Override
                            public void run() {
                                onResult.call(builder.toString());
                            }
                        });
                        break;
                }
            }
        });
    }

    private static void updateResultOnUiThread(final StringBuilder builder, @StringRes int resId, int result, String desc) {
        builder.append(String.format(RApplication.getApp().getResources().getString(resId), result == EMError.EM_NO_ERROR ? "" : ", error code: " + result,
                TextUtils.isEmpty(desc) ? "" : ", desc: " + desc))
                .append("\n");
    }
}
