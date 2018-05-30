package com.sunxy.pluginone;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.SunModel;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qihoo360.replugin.RePlugin;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import xiaofei.library.hermeseventbus.HermesEventBus;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HermesEventBus.getDefault().connectApp(this, RePlugin.getHostContext().getPackageName());
        HermesEventBus.getDefault().register(this);
        TextView textView = findViewById(R.id.textView);

        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        sb.append("pid: ").append(android.os.Process.myPid());
        sb.append("\n");
        sb.append("PackageName: ").append(getPackageName());
        sb.append("\n");
        String path = getExternalCacheDir().getAbsolutePath();
        sb.append("ExternalCacheDir: ").append(path);
        sb.append("\n");

        try {
            PackageInfo packageInfo = getPackageManager().
                    getPackageInfo(getPackageName(), 0);

            sb.append("versionCode: ");
            sb.append(packageInfo.versionCode);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        textView.setText(sb.toString());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("sunxy","插件发送成功！");
                HermesEventBus.getDefault().post("12233 来自插件");
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showText(SunModel model) {
        Log.v("sunxy", "来自宿主：" + model.getName());
        Toast.makeText(this, "来自宿主：" + model.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HermesEventBus.getDefault().unregister(this);
    }
}
