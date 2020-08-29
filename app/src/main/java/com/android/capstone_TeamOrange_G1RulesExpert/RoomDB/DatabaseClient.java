package com.android.capstone_TeamOrange_G1RulesExpert.RoomDB;

import android.content.Context;

import com.android.capstone_TeamOrange_G1RulesExpert.RoomDB.AppDatabase.AppDatabase;

import androidx.room.Room;

//Here is a singleton class that will init our database access object to access
//form any class or activity by calling DatabaseClient.getInstance();

public class DatabaseClient {

    private Context mCtx;
    private static DatabaseClient mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "AppDB").build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
