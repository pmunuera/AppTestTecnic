package com.example.apptesttecnic;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;
//Creamos esta clase que se iniciará nada más comenzar para poder iniciar la base de datos Realm
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).deleteRealmIfMigrationNeeded().modules(new ArtistModule()).build();
        Realm.setDefaultConfiguration(configuration);
        Log.d("Realm", "Realm has been initialized.");
    }
}


