package com.angcyo.hyphenate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Pair;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：环信会话管理
 * 创建人员：Robi
 * 创建时间：2018/03/14 15:01
 * 修改人员：Robi
 * 修改时间：2018/03/14 15:01
 * 修改备注：
 * Version: 1.0.0
 */
public class REMConversation {

    public static final String ACTION_GROUP_CHANAGED = "action_group_changed";
    public static final String ACTION_CONTACT_CHANAGED = "action_contact_changed";
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;

    private REMConversation() {
    }

    /*根据消息的时间, 降序排序*/
    private static void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    /**
     * 获取所有会话列表
     */
    public static List<EMConversation> getAllConversations() {
        return loadConversationList();
    }

    public static List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 删除和某个user会话，如果需要保留聊天记录，传false
     */
    public static void deleteConversation(String username, boolean deleteMessage /*清空聊天记录*/) {
        //删除和某个user会话，如果需要保留聊天记录，传false
        EMClient.getInstance().chatManager().deleteConversation(username, deleteMessage);
    }

    /**
     * 删除当前会话的某条聊天记录
     */
    public static void removeMessage(String msgId, String username) {
        //删除当前会话的某条聊天记录
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        if (conversation == null) {
            return;
        }

        conversation.removeMessage(msgId);
    }

    public static REMConversation instance() {
        return Holder.instance;
    }

    /**
     * 获取会话未读消息数量
     */
    public static int getUnreadMsgCount(String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        if (conversation == null) {
            return 0;
        }
        return conversation.getUnreadMsgCount();
    }

    /**
     * 获取所有会话的未读消息数
     */
    public static int getAllUnreadMsgCount() {
        return EMClient.getInstance().chatManager().getUnreadMessageCount();
    }

    /**
     * 清空未读消息数
     */
    public static void markAllMessagesAsRead(String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        if (conversation == null) {
            return;
        }
        //指定会话消息未读数清零
        conversation.markAllMessagesAsRead();
        //把一条消息置为已读
        //conversation.markMessageAsRead(messageId);
        ////所有未读消息数清零
        //EMClient.getInstance().chatManager().markAllConversationsAsRead();
    }

    /**
     * 指定消息已读
     */
    public static void markMessageAsRead(String username, String messageId) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        if (conversation == null) {
            return;
        }
        //指定会话消息未读数清零
        //conversation.markAllMessagesAsRead();
        //把一条消息置为已读
        conversation.markMessageAsRead(messageId);
        ////所有未读消息数清零
        //EMClient.getInstance().chatManager().markAllConversationsAsRead();
    }

    /**
     * 清空所有会话的未读消息数
     */
    public static void markAllConversationsAsRead() {
        EMClient.getInstance().chatManager().markAllConversationsAsRead();
    }

    /**
     * 获取会话所有消息数量
     */
    public static int getAllMsgCount(String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        if (conversation == null) {
            return 0;
        }
        //获取此会话在本地的所有的消息数量
        return conversation.getAllMsgCount();
        //如果只是获取当前在内存的消息数量，调用
        // conversation.getAllMessages().size();
    }

    /**
     * 注册广播监听, 当会话改变
     */
    public void registerBroadcastReceiver(Context context) {
        broadcastManager = LocalBroadcastManager.getInstance(context.getApplicationContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
//                updateUnreadLabel();
//                updateUnreadAddressLable();
//                if (currentTabIndex == 0) {
//                    // refresh conversation list
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
//                } else if (currentTabIndex == 1) {
//                    if (contactListFragment != null) {
//                        contactListFragment.refresh();
//                    }
//                }
//                String action = intent.getAction();
//                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
//                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
//                    }
//                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private static class Holder {
        static REMConversation instance = new REMConversation();
    }
}
