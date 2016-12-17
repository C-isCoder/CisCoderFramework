package com.baichang.android.utils;

import android.os.CountDownTimer;

/**
 * Created by iscod.
 * Time:2016/8/30-16:59.
 */
public class BCTimeCount extends CountDownTimer {
    private static BCTimeCount INSTANCE;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private BCTimeCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    /**
     * @param millisInFuture    总时长 毫秒
     * @param countDownInterval 每次执行减少的时长  毫秒
     * @return BCTimeCount
     */
    public static BCTimeCount create(long millisInFuture, long countDownInterval) {
        if (INSTANCE == null) {
            INSTANCE = new BCTimeCount(millisInFuture, countDownInterval);
        }
        return INSTANCE;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (listener != null) {
            listener.onTick(millisUntilFinished / 1000);//剩余时间
        }
    }

    @Override
    public void onFinish() {
        if (listener != null) {
            listener.onFinish();                        //计时完毕
        }
    }

    private TimeCountListener listener;

    public BCTimeCount setListener(TimeCountListener listener) {
        this.listener = listener;
        return this;
    }

    public interface TimeCountListener {
        //void onTick(long millisUntilFinished); //毫秒
        void onTick(long second);                //秒

        void onFinish();
    }

    /**
     * 取消，防止倒计时没有结束 Activity销毁造成的内存泄露
     */
    public void destroy() {
        if (INSTANCE != null) {
            INSTANCE.cancel();
        }
        INSTANCE = null;
    }
}
