package com.sunxy.replugintest;

import java.io.File;
import java.io.Serializable;

/**
 * --
 * <p>
 * Created by sunxy on 2018/5/28 0028.
 */
public class PluginInfoModel implements Serializable{

    private File file;
    private boolean isInstall;
    private int installCode;
    private int apkCode;
    private String packageName;
    private String mainActivityName;

    public String getMainActivityName() {
        return mainActivityName;
    }

    public void setMainActivityName(String mainActivityName) {
        this.mainActivityName = mainActivityName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isInstall() {
        return isInstall;
    }

    public void setInstall(boolean install) {
        isInstall = install;
    }

    public int getInstallCode() {
        return installCode;
    }

    public void setInstallCode(int installCode) {
        this.installCode = installCode;
    }

    public int getApkCode() {
        return apkCode;
    }

    public void setApkCode(int apkCode) {
        this.apkCode = apkCode;
    }
}
