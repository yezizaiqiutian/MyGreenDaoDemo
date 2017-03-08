package com.gh.mygreendaodemo;

import android.app.Application;
import android.content.Context;

/**
 * @author: gh
 * @description:
 * @date: 2017/1/6 10:06
 * @note:
 */

public class MyApplication extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
