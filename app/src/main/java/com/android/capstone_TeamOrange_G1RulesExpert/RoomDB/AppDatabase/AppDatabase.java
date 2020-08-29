package com.android.capstone_TeamOrange_G1RulesExpert.RoomDB.AppDatabase;

import com.android.capstone_TeamOrange_G1RulesExpert.RoomDB.Dao.LoginDao;
import com.android.capstone_TeamOrange_G1RulesExpert.RoomDB.Entity.LoginEntity;

import androidx.room.Database;
import androidx.room.RoomDatabase;
//App database that will create instance of our data manipulation objects
@Database(entities = {LoginEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LoginDao loginDao();

}