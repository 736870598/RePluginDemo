package com.sunxy.pluginone;

import android.app.Application;
import android.util.Log;

import com.qihoo360.replugin.RePlugin;

import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * --
 * <p>
 * Created by sunxy on 2018/5/29 0029.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("sunxy", "plubin Application onCreate");

    }
}
