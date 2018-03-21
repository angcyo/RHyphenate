package com.angcyo.hyphenate;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

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
    public static void getAllContactsFromServer() {
        try {
            List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加好友
     */
    public static void addContact(String toAddUsername /*需要添加的用户名*/, String reason /*验证消息*/) {
        //参数为要添加的好友的username和添加理由
        try {
            EMClient.getInstance().contactManager().addContact(toAddUsername, reason);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除好友
     */
    public static void deleteContact(String username) {
        try {
            EMClient.getInstance().contactManager().deleteContact(username);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同意好友请求
     */
    public static void acceptInvitation(String username) {
        try {
            EMClient.getInstance().contactManager().acceptInvitation(username);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拒绝好友请求
     */
    public static void declineInvitation(String username) {
        try {
            EMClient.getInstance().contactManager().declineInvitation(username);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    public static REMContacts instance() {
        return Holder.instance;
    }

    private static class Holder {
        static REMContacts instance = new REMContacts();
    }

}
