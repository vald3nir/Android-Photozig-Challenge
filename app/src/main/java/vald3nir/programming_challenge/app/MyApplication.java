package vald3nir.programming_challenge.app;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by vald3nir on 12/12/17
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .name("frente_de_loja_database")
                .deleteRealmIfMigrationNeeded()
                .build());
    }

}
