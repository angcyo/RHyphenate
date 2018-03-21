package com.angcyo.hyphenate;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;

import static com.hyphenate.chat.EMMessage.ChatType;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2018/03/21 15:45
 * 修改人员：Robi
 * 修改时间：2018/03/21 15:45
 * 修改备注：
 * Version: 1.0.0
 */
public class REMMessage {

    /**
     * 发送扩展消息
     */
    public static void sendTxtMessage(String content, String toChatUsername, boolean isGroup) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);

        // 增加自己特定的属性
        message.setAttribute("attribute1", "value");
        message.setAttribute("attribute2", true);
        EMClient.getInstance().chatManager().sendMessage(message);

        if (isGroup)
            message.setChatType(ChatType.GroupChat);

        //接收消息的时候获取到扩展属性
        //获取自定义的属性，第2个参数为没有此定义的属性时返回的默认值
        //message.getStringAttribute("attribute1", null);
        //message.getBooleanAttribute("attribute2", false);
    }

    /**
     * 发送透传消息
     */
    public static void sendCmdMessage(String action, String toChatUsername, boolean isGroup) {
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

        //支持单聊和群聊，默认单聊，如果是群聊添加下面这行
        cmdMsg.setChatType(ChatType.GroupChat);
        //String action = action;//action可以自定义
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        //String toUsername = "test1";//发送给某个人
        if (isGroup)
            cmdMsg.setChatType(ChatType.GroupChat);
        cmdMsg.setTo(toChatUsername);
        cmdMsg.addBody(cmdBody);
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    }

    /**
     * 发送文件消息
     */
    public static void sendFileMessage(String filePath, String toChatUsername, boolean isGroup) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        // 如果是群聊，设置chattype，默认是单聊
        if (isGroup)
            message.setChatType(ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送位置消息
     */
    public static void sendLocationMessage(double latitude, double longitude, String locationAddress, String toChatUsername, boolean isGroup) {
        //latitude为纬度，longitude为经度，locationAddress为具体位置内容
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        if (isGroup)
            message.setChatType(ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送图片消息
     */
    public static void sendImageMessage(String imagePath, boolean isOrigin /*是否发送原图*/, String toChatUsername, boolean isGroup) {
        //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        EMMessage message = EMMessage.createImageSendMessage(imagePath, isOrigin, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        if (isGroup)
            message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送视频消息
     */
    public static void sendVideoMessage(String videoPath, String thumbPath, int videoLength /*秒*/, String toChatUsername, boolean isGroup) {
        //videoPath为视频本地路径，thumbPath为视频预览图路径，videoLength为视频时间长度
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        if (isGroup)
            message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送语音消息
     */
    public static void sendVoiceMessage(String filePath, int length, String toChatUsername, boolean isGroup) {
        //filePath为语音文件路径，length为录音时间(秒)
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        if (isGroup)
            message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送文本消息
     */
    public static void sendMessage(String content, String toChatUsername, boolean isGroup) {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        if (isGroup)
            message.setChatType(EMMessage.ChatType.GroupChat);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
    }


}
