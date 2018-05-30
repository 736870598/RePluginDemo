package android.support.v7.app;

import java.io.Serializable;

/**
 * --  进行间通信model类，在进程间通信时要求包名类名必须一致。
 * <p>
 * Created by sunxy on 2018/5/29 0029.
 */
public class SunModel implements Serializable{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
