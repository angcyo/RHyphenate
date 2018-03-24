package com.angcyo.hyphenate.listener;

import android.support.annotation.NonNull;

import com.angcyo.uiview.utils.RUtils;
import com.angcyo.uiview.utils.ThreadExecutor;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by angcyo on 2018/03/24 21:27
 */
public class REMMessageListener implements EMMessageListener {
    @Override
    public void onMessageReceived(final List<EMMessage> messages) {
        if (RUtils.isListEmpty(messages)) {
            return;
        }
        ThreadExecutor.instance().onMain(new Runnable() {
            @Override
            public void run() {
                onNewMessage(messages);
            }
        });
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        if (RUtils.isListEmpty(messages)) {
            return;
        }
    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        if (RUtils.isListEmpty(messages)) {
            return;
        }
    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {
        if (RUtils.isListEmpty(messages)) {
            return;
        }
    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {
        if (RUtils.isListEmpty(messages)) {
            return;
        }
    }

    @Override
    public void onMessageChanged(EMMessage message, Object change) {
        if (message == null) {
            return;
        }
    }

    public void onNewMessage(@NonNull List<EMMessage> messages) {

    }
}
