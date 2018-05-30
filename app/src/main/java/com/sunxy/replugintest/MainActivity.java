package com.sunxy.replugintest;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.SunModel;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import xiaofei.library.hermeseventbus.HermesEventBus;

public class MainActivity extends AppCompatActivity {

    private PluginAdapter adapter;
    private List<PluginInfoModel> list;
    private RecyclerView recycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showText("---");

        HermesEventBus.getDefault().register(this);

        recycleView = findViewById(R.id.recycleView);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PluginAdapter(new PluginAdapter.OnItemClick() {
            @Override
            public void onClick(int pos) {
                dealClick(adapter.getModel(pos), true);
            }

            @Override
            public void reInsall(int pos) {
                dealClick(adapter.getModel(pos), false);
            }
        });
        recycleView.setAdapter(adapter);

        Observable.just(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/sunxy_file/plugin")
                .map(new Function<String, File>() {
                    @Override
                    public File apply(String s) throws Exception {
                        return new File(s);
                    }
                })
                .filter(new Predicate<File>() {
                    @Override
                    public boolean test(File file) throws Exception {
                        return file.exists();
                    }
                })
                .flatMap(new Function<File, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(File file) throws Exception {
                        return Observable.fromArray(file.listFiles());
                    }
                })
                .map(new Function<File, PluginInfoModel>() {
                    @Override
                    public PluginInfoModel apply(File file) throws Exception {
                        return file2PackageInfo(file);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PluginInfoModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (list == null){
                            list = new ArrayList<>();
                        }
                        list.clear();
                    }

                    @Override
                    public void onNext(PluginInfoModel pluginInfoModel) {
                        list.add(pluginInfoModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onComplete() {
                        adapter.setList(list);
                    }
                });


    }

    private PluginInfoModel file2PackageInfo(File file){
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(file.getAbsolutePath(), PackageManager.GET_ACTIVITIES);

        PluginInfoModel info = new PluginInfoModel();
        info.setFile(file);
        info.setPackageName(packageInfo.packageName);
        info.setApkCode(packageInfo.versionCode);
        info.setMainActivityName(packageInfo.activities[0].name);

        PluginInfo pluginInfo = RePlugin.getPluginInfo(packageInfo.packageName);
        if (pluginInfo != null){
            info.setInstall(true);
            info.setInstallCode(pluginInfo.getVersion());
        }
        return info;
    }

    private void dealClick(PluginInfoModel info, boolean isCheck){
        if ( !isCheck || (!info.isInstall() || info.getApkCode() > info.getInstallCode()) ){
            Toast.makeText(this, "安装升级中", Toast.LENGTH_SHORT).show();
            Disposable disposable = Observable
                    .just(info)
                    .map(new Function<PluginInfoModel, PluginInfoModel>() {
                        @Override
                        public PluginInfoModel apply(PluginInfoModel info) throws Exception {
                            install(info);
                            return info;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<PluginInfoModel>() {
                        @Override
                        public void accept(PluginInfoModel pluginInfoModel) throws Exception {
                            adapter.notifyDataSetChanged();
                        }
                    });
        }else{
            RePlugin.startActivity(MainActivity.this, RePlugin.createIntent(info.getPackageName(), info.getMainActivityName()));
        }
    }

    private void install(PluginInfoModel model) {
        PluginInfo pluginInfo = RePlugin.install(model.getFile().getAbsolutePath());
        if (pluginInfo != null){
            model.setInstall(true);
            model.setInstallCode(pluginInfo.getVersion());
            RePlugin.preload(pluginInfo);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showText(String text) {
        Log.v("sunxy", "来自插件：" + text);
        Toast.makeText(this, "来自插件：" + text, Toast.LENGTH_SHORT).show();
        SunModel sunModel = new SunModel();
        sunModel.setName("我是宿主.....=---");
        HermesEventBus.getDefault().post(sunModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HermesEventBus.getDefault().unregister(this);
    }
}
