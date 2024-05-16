package com.github.tvbox.osc.util;

import android.util.Log;

import androidx.annotation.Nullable;

import com.github.tvbox.osc.event.LogEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * @author pj567
 * @date :2020/12/18
 * @description:
 */
public class LOG {
    private static final String TAG = "TVBox";

    public static void e(Throwable t) {
        Log.e(TAG, t.getMessage(), t);
        EventBus.getDefault().post(new LogEvent(String.format("【E/%s】=>>>", TAG) + Log.getStackTraceString(t)));
    }

    public static void e(String tag, Throwable t) {
        Log.e(tag, t.getMessage(), t);
        EventBus.getDefault().post(new LogEvent(String.format("【E/%s】=>>>", tag) + Log.getStackTraceString(t)));
    }

    public static void e(@Nullable String msg) {
        Log.e(TAG, String.valueOf(msg));
        EventBus.getDefault().post(new LogEvent(String.format("【E/%s】=>>>", TAG) + msg));
    }

    public static void e(String tag, @Nullable String msg) {
        Log.e(tag, String.valueOf(msg));
        EventBus.getDefault().post(new LogEvent(String.format("【E/%s】=>>>", tag) + msg));
    }

    public static void i(@Nullable String msg) {
        Log.i(TAG, String.valueOf(msg));
        EventBus.getDefault().post(new LogEvent(String.format("【I/%s】=>>>", TAG) + msg));
    }

    public static void i(String tag,@Nullable String msg) {
        Log.i(tag, String.valueOf(msg));
        EventBus.getDefault().post(new LogEvent(String.format("【I/%s】=>>>", tag) + msg));
    }
}