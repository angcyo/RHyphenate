package com.angcyo.hyphenate;

import android.text.TextUtils;

import com.angcyo.uiview.net.RFunc;
import com.angcyo.uiview.net.RSubscriber;
import com.angcyo.uiview.net.Rx;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.functions.Func1;

/**
 * 群聊管理
 * Created by angcyo on 2018/03/25 08:01
 */
public class REMGroupManager {

    /**
     * 创建群组
     *
     * @param groupName  群组名称
     * @param desc       群组简介
     * @param allMembers 群组初始成员，如果只有自己传空数组即可
     * @param reason     邀请成员加入的reason
     * @param option     群组类型选项，可以设置群组最大用户数(默认200)及群组类型@see {@link EMGroupManager.EMGroupStyle}
     *                   option.inviteNeedConfirm表示邀请对方进群是否需要对方同意，默认是需要用户同意才能加群的。
     *                   option.extField创建群时可以为群组设定扩展字段，方便个性化订制。
     * @return 创建好的group
     * @throws HyphenateException
     */
    public static Subscription createGroup(final String groupName, final String desc, final String reason,
                                           final EMGroupOptions option, final String[] allMembers,
                                           final Func1<Boolean, String> onResult) {
//        EMGroupOptions option = new EMGroupOptions();
//        option.maxUsers = 200;
//        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
//        EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;//公开,需要确认
//        EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;//公开, 不需要确认
//        EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;//私有, 群成员可以邀请
//        EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;//私有, 管理员可以邀请

//        EMGroupStylePrivateOnlyOwnerInvite——私有群，只有群主可以邀请人；
//        EMGroupStylePrivateMemberCanInvite——私有群，群成员也能邀请人进群；
//        EMGroupStylePublicJoinNeedApproval——公开群，加入此群除了群主邀请，只能通过申请加入此群；
//        EMGroupStylePublicOpenJoin ——公开群，任何人都能加入此群。

        return Rx.base(new RFunc<Boolean>() {
            @Override
            public Boolean onFuncCall() {
                try {
                    EMClient.getInstance().groupManager().createGroup(groupName, desc, allMembers, reason, option);
                    return true;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return false;
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
     * 群主邀请人加群
     */
    public static Subscription addUsersToGroup(final String groupId, final String[] newmembers, final Func1<Boolean, String> onResult) {
        return Rx.base(new RFunc<Boolean>() {
            @Override
            public Boolean onFuncCall() {
                try {
                    //群主加人调用此方法
                    EMClient.getInstance().groupManager().addUsersToGroup(groupId, newmembers);//需异步处理
                    return true;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return false;
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
     * 邀请用户进群
     */
    public static Subscription inviteUser(final String groupId, final String[] newmembers, final Func1<Boolean, String> onResult) {
        return Rx.base(new RFunc<Boolean>() {
            @Override
            public Boolean onFuncCall() {
                try {
                    //私有群里，如果开放了群成员邀请，群成员邀请调用下面方法
                    EMClient.getInstance().groupManager().inviteUser(groupId, newmembers, null);//需异步处理
                    return true;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return false;
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
     * 申请加入群组
     */
    public static Subscription joinGroup(final EMGroup emGroup, final Func1<Boolean, String> onResult) {
        return Rx.base(new RFunc<Boolean>() {
            @Override
            public Boolean onFuncCall() {
                try {
                    String groupId = emGroup.getGroupId();
                    if (emGroup.isMemberOnly()) {
                        //需要申请和验证才能加入的，即group.isMembersOnly()为true，调用下面方法
                        EMClient.getInstance().groupManager().applyJoinToGroup(groupId, REM.getCurrentUserName() + ":请求加入群组");//需异步处理
                    } else {
                        //如果群开群是自由加入的，即group.isMembersOnly()为false，直接join
                        EMClient.getInstance().groupManager().joinGroup(groupId);//需异步处理
                    }
                    return true;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return false;
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
     * 离开群组
     */
    public static Subscription leaveGroup(final String groupId, final Func1<Boolean, String> onResult) {
        return Rx.base(new RFunc<Boolean>() {
            @Override
            public Boolean onFuncCall() {
                try {
                    EMClient.getInstance().groupManager().leaveGroup(groupId);//需异步处理
                    return true;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return false;
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
     * 解散群组
     */
    public static Subscription destroyGroup(final String groupId, final Func1<Boolean, String> onResult) {
        return Rx.base(new RFunc<Boolean>() {
            @Override
            public Boolean onFuncCall() {
                try {
                    EMClient.getInstance().groupManager().destroyGroup(groupId);//需异步处理
                    return true;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return false;
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
     * 改变群组信息
     */
    public static Subscription changeGroupInfo(final String groupId,
                                               final String changedGroupName, final String description,
                                               final Func1<Boolean, String> onResult) {
        return Rx.base(new RFunc<Boolean>() {
            @Override
            public Boolean onFuncCall() {
                try {
                    //修改群名称
                    EMClient.getInstance().groupManager().changeGroupName(groupId, changedGroupName);//需异步处理
                    //修改群描述
                    EMClient.getInstance().groupManager().changeGroupDescription(groupId, description);//需异步处理
                    return true;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return false;
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
     * 获取群组成员列表
     */
    public static Subscription fetchGroupMembers(final String groupId, final Func1<List<String>, String> onResult) {
        return Rx.base(new RFunc<List<String>>() {
            @Override
            public List<String> onFuncCall() {
                List<String> memberList = new ArrayList<>();
                try {
                    //如果群成员较多，需要多次从服务器获取完成
                    EMCursorResult<String> result = null;
                    final int pageSize = 20;
                    do {
                        result = EMClient.getInstance().groupManager().fetchGroupMembers(groupId,
                                result != null ? result.getCursor() : "", pageSize);
                        memberList.addAll(result.getData());
                    }
                    while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);
                    return memberList;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return memberList;
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
     * 获取公开群列表
     */
    public static Subscription getPublicGroupsFromServer(final Func1<List<EMGroupInfo>, String> onResult) {
        return Rx.base(new RFunc<List<EMGroupInfo>>() {
            @Override
            public List<EMGroupInfo> onFuncCall() {
                try {
                    final int pageSize = 20;
                    String cursor = "";
                    //pageSize为要取到的群组的数量，cursor用于告诉服务器从哪里开始取
                    EMCursorResult<EMGroupInfo> result = EMClient.getInstance().groupManager().getPublicGroupsFromServer(pageSize, cursor);//需异步处理
                    List<EMGroupInfo> groupsList = result.getData();
                    cursor = result.getCursor();
                    return groupsList;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }
        }, new RSubscriber<List<EMGroupInfo>>() {
            @Override
            public void onSucceed(List<EMGroupInfo> bean) {
                super.onSucceed(bean);
                onResult.call(bean);
            }
        });
    }

    /**
     * 从服务器拉取自己加入的群聊
     */
    public static Subscription getJoinedGroupsFromServer(final Func1<List<EMGroup>, String> onResult) {
        return Rx.base(new RFunc<List<EMGroup>>() {
            @Override
            public List<EMGroup> onFuncCall() {
                try {
                    //从服务器获取自己加入的和创建的群组列表，此api获取的群组sdk会自动保存到内存和db。
                    List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();//需异步处理
                    return grouplist;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }
        }, new RSubscriber<List<EMGroup>>() {
            @Override
            public void onSucceed(List<EMGroup> bean) {
                super.onSucceed(bean);
                onResult.call(bean);
            }
        });
    }

    /**
     * 从本地加载群组列表
     */
    public static Subscription getAllGroups(final Func1<List<EMGroup>, String> onResult) {
        return Rx.base(new RFunc<List<EMGroup>>() {
            @Override
            public List<EMGroup> onFuncCall() {
                //从本地加载群组列表
                List<EMGroup> grouplist = EMClient.getInstance().groupManager().getAllGroups();
                return grouplist;
            }
        }, new RSubscriber<List<EMGroup>>() {
            @Override
            public void onSucceed(List<EMGroup> bean) {
                super.onSucceed(bean);
                onResult.call(bean);
            }
        });
    }

    /**
     * 从服务器获取群组基本信息
     */
    public static Subscription getGroupFromServer(final String groupId, final Func1<EMGroup, String> onResult) {
        return Rx.base(new RFunc<EMGroup>() {
            @Override
            public EMGroup onFuncCall() {

                //根据群组ID从服务器获取群组基本信息
                EMGroup group = null;
                try {
                    group = EMClient.getInstance().groupManager().getGroupFromServer(groupId);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                return group;
//                group.getOwner();//获取群主
//                List<String> members = group.getMembers();//获取内存中的群成员
//                List<String> adminList = group.getAdminList();//获取管理员列表
            }
        }, new RSubscriber<EMGroup>() {
            @Override
            public void onSucceed(EMGroup bean) {
                super.onSucceed(bean);
                onResult.call(bean);
            }
        });
    }

    /**
     * 从本地获取群组基本信息
     */
    public static Subscription getGroup(final String groupId, final Func1<EMGroup, String> onResult) {
        return Rx.base(new RFunc<EMGroup>() {
            @Override
            public EMGroup onFuncCall() {
                EMGroup group = null;
                //根据群组ID从本地获取群组基本信息
                group = EMClient.getInstance().groupManager().getGroup(groupId);
                return group;
            }
        }, new RSubscriber<EMGroup>() {
            @Override
            public void onSucceed(EMGroup bean) {
                super.onSucceed(bean);
                onResult.call(bean);
            }
        });
    }


    public void init() {
        initGroupListener();
        getJoinedGroupsFromServer(new Func1<List<EMGroup>, String>() {
            @Override
            public String call(List<EMGroup> emGroups) {
                return null;
            }
        });
    }

    private REMGroupManager() {
    }

    public static REMGroupManager instance() {
        return Holder.instance;
    }

    private static class Holder {
        static REMGroupManager instance = new REMGroupManager();
    }

    private void initGroupListener() {
        EMClient.getInstance().groupManager().addGroupChangeListener(new EMGroupChangeListener() {
            @Override
            public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
                //接收到群组加入邀请
            }

            @Override
            public void onRequestToJoinReceived(String groupId, String groupName, String applyer, String reason) {
                //用户申请加入群
            }

            @Override
            public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
                //加群申请被同意
            }

            @Override
            public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
                //加群申请被拒绝
            }

            @Override
            public void onInvitationAccepted(String groupId, String inviter, String reason) {
                //群组邀请被同意
            }

            @Override
            public void onInvitationDeclined(String groupId, String invitee, String reason) {
                //群组邀请被拒绝
            }

            @Override
            public void onUserRemoved(String groupId, String groupName) {
                //群组用户被移除
            }

            @Override
            public void onGroupDestroyed(String groupId, String groupName) {
                //群组解散
            }

            @Override
            public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
                //接收邀请时自动加入到群组的通知
            }

            @Override
            public void onMuteListAdded(String groupId, final List<String> mutes, final long muteExpire) {
                //成员禁言的通知
            }

            @Override
            public void onMuteListRemoved(String groupId, final List<String> mutes) {
                //成员从禁言列表里移除通知
            }

            @Override
            public void onAdminAdded(String groupId, String administrator) {
                //增加管理员的通知
            }

            @Override
            public void onAdminRemoved(String groupId, String administrator) {
                //管理员移除的通知
            }

            @Override
            public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {
                //群所有者变动通知
            }

            @Override
            public void onMemberJoined(final String groupId, final String member) {
                //群组加入新成员通知
            }

            @Override
            public void onMemberExited(final String groupId, final String member) {
                //群成员退出通知
            }

            @Override
            public void onAnnouncementChanged(String groupId, String announcement) {
                //群公告变动通知
            }

            @Override
            public void onSharedFileAdded(String groupId, EMMucSharedFile sharedFile) {
                //增加共享文件的通知
            }

            @Override
            public void onSharedFileDeleted(String groupId, String fileId) {
                //群共享文件删除通知
            }
        });
    }
}
