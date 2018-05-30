package com.sunxy.replugintest;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.qihoo360.replugin.RePluginApplication;
import com.qihoo360.replugin.RePluginCallbacks;
import com.qihoo360.replugin.RePluginConfig;
import com.qihoo360.replugin.RePluginEventCallbacks;
import com.qihoo360.replugin.model.PluginInfo;

import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * --
 * <p>
 * Created by sunxy on 2018/5/28 0028.
 */
public class MyApplication extends RePluginApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        HermesEventBus.getDefault().init(this);
    }

    @Override
    protected RePluginConfig createConfig() {
        RePluginConfig config = new RePluginConfig();
        //是否开启签名认证
        config.setVerifySign(!BuildConfig.DEBUG);
        //安装后原apk文件不被移动
        config.setMoveFileWhenInstalling(false);

        config.setCallbacks(new RePluginCallbacks(this){
            @Override
            public boolean onPluginNotExistsForActivity(Context context, String plugin, Intent intent, int process) {
                Toast.makeText(context, plugin + " 插件还没有安装，请先安装", Toast.LENGTH_SHORT).show();
                Log.v("sunxy", plugin + " 插件还没有安装，请先安装");
                return true;
            }

            @Override
            public boolean onLoadLargePluginForActivity(Context context, String plugin, Intent intent, int process) {
                Toast.makeText(context, plugin + " 插件太大了，请先安装在跳转", Toast.LENGTH_SHORT).show();
                Log.v("sunxy", plugin + " 插件太大了，请先安装在跳转");
                return true;
            }
        });

        config.setEventCallbacks(new RePluginEventCallbacks(this){
            @Override
            public void onInstallPluginSucceed(PluginInfo info) {
//                Toast.makeText(MyApplication.this, info.getName() + " 插件安装成功", Toast.LENGTH_SHORT).show();
                Log.v("sunxy", info.getName() + " 插件安装成功");
                super.onInstallPluginSucceed(info);
            }

            @Override
            public void onInstallPluginFailed(String path, InstallResult code) {
//                Toast.makeText(MyApplication.this, path + " 插件安装失败", Toast.LENGTH_SHORT).show();
                Log.v("sunxy", path + " 插件安装失败 " + code.name());
                super.onInstallPluginFailed(path, code);
            }
        });

        return config;
    }
}
