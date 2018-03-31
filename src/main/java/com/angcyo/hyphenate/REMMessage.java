package com.angcyo.hyphenate;

import android.text.TextUtils;

import com.angcyo.library.utils.L;
import com.angcyo.uiview.manager.RNotifier;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

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

    public static final String M_TYPE_TXT = "TXT";
    public static final String M_TYPE_IMAGE = "IMAGE";
    public static final String M_TYPE_VIDEO = "VIDEO";
    public static final String M_TYPE_LOCATION = "LOCATION";
    public static final String M_TYPE_VOICE = "VOICE";
    public static final String M_TYPE_FILE = "FILE";
    public static final String M_TYPE_CMD = "CMD";
    public static final String EX_TYPE = "ex_type";
    public static final String EX_DATA = "ex_data";
    /**
     * 当前正在聊天的用户名
     */
    public String currentChatUserName = "";
    EMMessageListener msgListener;

    private REMMessage() {
    }

    /**
     * 发送扩展消息
     */
    public static EMMessage sendTxtMessage(String content, String toChatUsername, boolean isGroup) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);

        // 增加自己特定的属性
        message.setAttribute("attribute1", "value");
        message.setAttribute("attribute2", true);
        EMClient.getInstance().chatManager().saveMessage(message);

        if (isGroup) {
            message.setChatType(ChatType.GroupChat);
        } else {
            message.setChatType(ChatType.Chat);
        }

        //接收消息的时候获取到扩展属性
        //获取自定义的属性，第2个参数为没有此定义的属性时返回的默认值
        //message.getStringAttribute("attribute1", null);
        //message.getBooleanAttribute("attribute2", false);
        return message;
    }

    /**
     * 发送透传消息
     */
    public static EMMessage sendCmdMessage(String action, String toChatUsername, boolean isGroup) {
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

        //支持单聊和群聊，默认单聊，如果是群聊添加下面这行
        // cmdMsg.setChatType(ChatType.GroupChat);
        //String action = action;//action可以自定义
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        //String toUsername = "test1";//发送给某个人
        if (isGroup) {
            cmdMsg.setChatType(ChatType.GroupChat);
        } else {
            cmdMsg.setChatType(ChatType.Chat);
        }
        cmdMsg.setTo(toChatUsername);
        cmdMsg.addBody(cmdBody);
        EMClient.getInstance().chatManager().saveMessage(cmdMsg);
        return cmdMsg;
    }

    /**
     * 发送文件消息
     */
    public static EMMessage sendFileMessage(String filePath, String toChatUsername, boolean isGroup) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        // 如果是群聊，设置chattype，默认是单聊
        if (isGroup) {
            message.setChatType(ChatType.GroupChat);
<<<<<<< HEAD
        EMClient.getInstance().chatManager().saveMessage(message);
=======
        } else {
            message.setChatType(ChatType.Chat);
        }
        EMClient.getInstance().chatManager().sendMessage(message);
>>>>>>> fix base
        return message;
    }

    /**
     * 发送位置消息
     */
    public static EMMessage sendLocationMessage(double latitude, double longitude, String locationAddress, String toChatUsername, boolean isGroup) {
        //latitude为纬度，longitude为经度，locationAddress为具体位置内容
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        if (isGroup) {
            message.setChatType(ChatType.GroupChat);
<<<<<<< HEAD
        EMClient.getInstance().chatManager().saveMessage(message);
=======
        } else {
            message.setChatType(ChatType.Chat);
        }
        EMClient.getInstance().chatManager().sendMessage(message);
>>>>>>> fix base
        return message;
    }

    /**
     * 发送图片消息
     */
    public static EMMessage sendImageMessage(String imagePath, boolean isOrigin /*是否发送原图*/, String toChatUsername, boolean isGroup) {
        //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        EMMessage message = EMMessage.createImageSendMessage(imagePath, isOrigin, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
<<<<<<< HEAD
        if (isGroup)
            message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().saveMessage(message);
=======
        if (isGroup) {
            message.setChatType(ChatType.GroupChat);
        } else {
            message.setChatType(ChatType.Chat);
        }
        EMClient.getInstance().chatManager().sendMessage(message);
>>>>>>> fix base
        return message;
    }

    /**
     * 发送视频消息
     */
    public static EMMessage sendVideoMessage(String videoPath, String thumbPath, int videoLength /*秒*/, String toChatUsername, boolean isGroup) {
        //videoPath为视频本地路径，thumbPath为视频预览图路径，videoLength为视频时间长度
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
<<<<<<< HEAD
        if (isGroup)
            message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().saveMessage(message);
=======
        if (isGroup) {
            message.setChatType(ChatType.GroupChat);
        } else {
            message.setChatType(ChatType.Chat);
        }
        EMClient.getInstance().chatManager().sendMessage(message);
>>>>>>> fix base
        return message;
    }

    /**
     * 发送语音消息
     */
    public static EMMessage sendVoiceMessage(String filePath, int length, String toChatUsername, boolean isGroup) {
        //filePath为语音文件路径，length为录音时间(秒)
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
<<<<<<< HEAD
        if (isGroup)
            message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().saveMessage(message);
=======
        if (isGroup) {
            message.setChatType(ChatType.GroupChat);
        } else {
            message.setChatType(ChatType.Chat);
        }
        EMClient.getInstance().chatManager().sendMessage(message);
>>>>>>> fix base
        return message;
    }

    /**
     * 发送文本消息
     */
    public static EMMessage sendMessage(String content, String toChatUsername, boolean isGroup) {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        if (isGroup) {
            message.setChatType(ChatType.GroupChat);
        } else {
            message.setChatType(ChatType.Chat);
        }
        //发送消息
        EMClient.getInstance().chatManager().saveMessage(message);
        return message;
    }

    public static List<EMMessage> loadMoreMsgFromDB(String username, String startMsgId, int pagesize) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
        List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
        return messages;
    }

    public static List<EMMessage> getAllMessages(String username, EMConversation.EMConversationType type) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username, type, true);
        //获取此会话的所有消息
        List<EMMessage> messages = conversation.getAllMessages();
        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        return messages;
    }

    /**
     * 获取消息类型
     */
    public static String getEMessageType(EMMessage message) {
        String type = "";
        if (message.getType() == EMMessage.Type.TXT) {
            String ex_type = message.getStringAttribute(EX_TYPE, "");
            if (TextUtils.isEmpty(ex_type)) {
                type = M_TYPE_TXT;
            } else {
                type = ex_type;
            }
        } else if (message.getType() == EMMessage.Type.IMAGE) {
            type = M_TYPE_IMAGE;
        } else if (message.getType() == EMMessage.Type.LOCATION) {
            type = M_TYPE_LOCATION;
        } else if (message.getType() == EMMessage.Type.VOICE) {
            type = M_TYPE_VOICE;
        } else if (message.getType() == EMMessage.Type.VIDEO) {
            type = M_TYPE_VIDEO;
        } else if (message.getType() == EMMessage.Type.FILE) {
            type = M_TYPE_FILE;
        } else if (message.getType() == EMMessage.Type.CMD) {
            type = M_TYPE_CMD;
        }
        return type;
    }

    /**
     * 获取消息摘要
     */
    public static String getMessageDigest(EMMessage message) {
        String digest = "";
        switch (message.getType()) {
            case LOCATION:
                if (message.direct() == EMMessage.Direct.RECEIVE) {
                    digest = "[%1$s的位置]";
                    digest = String.format(digest, message.getFrom());
                    return digest;
                } else {
                    digest = "[我的位置]";
                }
                break;
            case IMAGE:
                digest = "[图片]";
                break;
            case VOICE:
                digest = "[语音]";
                break;
            case VIDEO:
                digest = "[视频]";
                break;
            case TXT:
                EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                String ex_type = message.getStringAttribute(EX_TYPE, "");
                if (TextUtils.isEmpty(ex_type)) {
                    digest = txtBody.getMessage();
                } else {
                    String ex_data = message.getStringAttribute(EX_DATA, "");
                    digest = "[扩展消息]" + ex_data;
                }
                break;
            case FILE:
                digest = "[文件]";
                break;
            default:
                digest = "[不支持的格式]";
                break;
        }
        return digest;
    }

    public static REMMessage instance() {
        return Holder.instance;
    }

    public static void addMessageListener(EMMessageListener listener) {
        EMClient.getInstance().chatManager().addMessageListener(listener);
    }

    public static void removeMessageListener(EMMessageListener listener) {
        EMClient.getInstance().chatManager().removeMessageListener(listener);
    }

    public void init() {
        initMessageListener();
    }

    private void initMessageListener() {
        removeMessageListener();
        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                L.i("REMMessage 收到消息->" + messages.size()
                        + " from:" + messages.get(0).getFrom() + " to" + messages.get(0).getTo());

                try {
                    EMMessage lastMessage = messages.get(messages.size() - 1);
                    //正在聊天的人, 发来了消息, 不触发通知提示
//                    if (lastMessage.getChatType() == ChatType.Chat) {
//
//                    } else {
//                        TextUtils.equals(it.conversationId(), username)
//                    }
//
                    if (TextUtils.equals(currentChatUserName, lastMessage.conversationId())) {
                    } else {
                        RNotifier.instance().vibrateAndPlayTone();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
                L.i("REMMessage 收到透传消息->" + messages.size());
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
                L.i("REMMessage 收到已读回执->" + messages.size());
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
                L.i("REMMessage 收到已送达回执->" + message.size());
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
                L.i("REMMessage 消息被撤回->" + messages.size());
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
                L.i("REMMessage 消息状态变动->" + message + ":" + change);
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    public void removeMessageListener() {
        if (msgListener != null) {
            EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        }
    }

    private static class Holder {
        static REMMessage instance = new REMMessage();
    }
}
