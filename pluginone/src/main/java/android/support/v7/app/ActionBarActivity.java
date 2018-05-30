package android.support.v7.app;

/**
 * -- HermesEventBus 依赖了过时的ActionBarActivity，在新版support下编辑有时会报找不到
 *      ActionBarActivity类，所以这里新建这个类防止编译时报错。
 * <p>
 * Created by sunxy on 2018/5/29 0029.
 */
public class ActionBarActivity extends AppCompatActivity {
}
