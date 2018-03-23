package com.angcyo.realm.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 用户申请添加好友
 * Created by angcyo on 2018/03/23 22:03
 */
public class ContactInviteRealm extends RealmObject {
    @PrimaryKey
    private String username;
    private String reason;
    private int statue = 0;//操作状态,0未操作, 1同意, 2拒绝

    public ContactInviteRealm() {
    }

    public ContactInviteRealm(String username, String reason) {
        this.username = username;
        this.reason = reason;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
