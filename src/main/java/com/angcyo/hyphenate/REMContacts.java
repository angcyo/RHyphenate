package com.angcyo.hyphenate;

import com.angcyo.uiview.net.RFunc;
import com.angcyo.uiview.net.RSubscriber;
import com.angcyo.uiview.net.Rx;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：联系人管理
 * 创建人员：Robi
 * 创建时间：2018/03/21 17:00
 * 修改人员：Robi
 * 修改时间：2018/03/21 17:00
 * 修改备注：
 * Version: 1.0.0
 */
public class REMContacts {
    private REMContacts() {
    }

    /**
     * 获取所有联系人
     */
    public static void getAllContactsFromServer(final Func1<List<String>, String> onResult) {
        Rx.base(new RFunc<List<String>>() {
            @Override
            public List<String> onFuncCall() {
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    return usernames;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }
        }, new RSubscriber<List<String>>() {
            @Override
            public void onSucceed(List<String> bean) {
                super.onSucceed(bean);
                onResult.call(bean);
            }
        });
    }

    /**
     * 是否是联系人
     */
    public static void isContacts(final String username, final Func1<Boolean, String> onResult) {
        Rx.base(new RFunc<Boolean>() {
            @Override
            public Boolean onFuncCall() {
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    return usernames.contains(username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return Boolean.FALSE;
                }
            }
        }, new RSubscriber<Boolean>() {
            @Override
            public void onSucceed(Boolean bean) {
                super.onSucceed(bean);
                onResult.call(bean);
            }
        });
    }

    /**
     * 添加好友
     */
    public static void addContact(final String toAddUsername /*需要添加的用户名*/, final String reason /*验证消息*/, final Func1<String, String> onResult) {
        //参数为要添加的好友的username和添加理由
        Rx.base(new RFunc<String>() {
            @Override
            public String onFuncCall() {
                try {
                    EMClient.getInstance().contactManager().addContact(toAddUsername, reason);
                    return toAddUsername;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }, new RSubscriber<String>() {
            @Override
            public void onSucceed(String bean) {
                super.onSucceed(bean);
                onResult.call(bean);
            }
        });
    }

    /**
     * 删除好友
     */
    public static void deleteContact(final String username, final Func1<String, String> onResult) {
        Rx.base(new RFunc<String>() {
            @Override
            public String onFuncCall() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(username);
                    return username;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }, new RSubscriber<String>() {
            @Override
            public void onSucceed(String bean) {
                super.onSucceed(bean);
                onResult.call(bean);
            }
        });
    }

    /**
     * 同意好友请求
     */
    public static void acceptInvitation(final String username, final Func1<String, String> onResult) {
        Rx.base(new RFunc<String>() {
            @Override
            public String onFuncCall() {
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(username);
                    return username;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }, new RSubscriber<String>() {
            @Override
            public void onSucceed(String bean) {
                super.onSucceed(bean);
                onResult.call(bean);
            }
        });
    }

    /**
     * 拒绝好友请求
     */
    public static void declineInvitation(final String username, final Func1<String, String> onResult) {
        Rx.base(new RFunc<String>() {
            @Override
            public String onFuncCall() {
                try {
                    EMClient.getInstance().contactManager().declineInvitation(username);
                    return username;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }, new RSubscriber<String>() {
            @Override
            public void onSucceed(String bean) {
                super.onSucceed(bean);
                onResult.call(bean);
            }
        });

    }

    public static REMContacts instance() {
        return Holder.instance;
    }

    private static class Holder {
        static REMContacts instance = new REMContacts();
    }

}
