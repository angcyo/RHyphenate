package com.angcyo.hyphenate;

import com.angcyo.uiview.utils.ThreadExecutor;
import com.hyphenate.EMCallBack;

/**
 * 环信的回调在子线程, 这里封装一下在主线程回调
 * Created by angcyo on 2018-03-06.
 */

public abstract class REMCallBack implements EMCallBack {
    @Override
    public void onSuccess() {
        ThreadExecutor.instance().onMain(new Runnable() {
            @Override
            public void run() {
                onResult(false, 0, "");
            }
        });
    }

    @Override
    public void onError(final int i, final String s) {
        ThreadExecutor.instance().onMain(new Runnable() {
            @Override
            public void run() {
                onResult(true, i, s);
            }
        });
    }

    @Override
    public void onProgress(int progress, String status) {

    }

    public abstract void onResult(boolean isError, int code, String message);


}
