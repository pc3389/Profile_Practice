package android.example.profile_practice;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(MyApplication.this);
        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .name("RealmData.realm")
                        .build();
        Realm.setDefaultConfiguration(config);
    }

}
