package materialtest.theartistandtheengineer.co.materialtest.materialtest;

import android.app.Application;
import android.content.Context;

/**
 * Created by mpcen-desktop on 3/25/15.
 */
public class MyApplication extends Application {

    public static final String API_KEY_GOOGLE_BOOKS = "AIzaSyBlZGnFuy7ro_RieQn8zRNr2QSM5CjHatI";
    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
