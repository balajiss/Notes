package com.balaji.notehomelane;

import android.app.Application;
import android.content.Context;

import com.balaji.notehomelane.LocalStorage.DatabaseHandler;

/**
 * Created by balaji on 16/01/18.
 */

public class NoteApplication extends Application {

    public static NoteApplication INSTANCE;
    public static DatabaseHandler dbHandler;
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        applicationContext = getApplicationContext();
        getDatabaseHandler();
    }

    public synchronized static DatabaseHandler getDatabaseHandler() {
        if(dbHandler == null) {
            dbHandler = new DatabaseHandler(applicationContext);
        }
        return dbHandler;
    }

    public static synchronized NoteApplication getInstance() {
        return INSTANCE;
    }
}
