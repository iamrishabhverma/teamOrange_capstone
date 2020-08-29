package com.android.capstone_TeamOrange_G1RulesExpert.RoomDB.Dao;

import com.android.capstone_TeamOrange_G1RulesExpert.RoomDB.Entity.LoginEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

//this is data access object through which we will access our table or entity by
//we can use query to perform our task
@Dao
public interface LoginDao {
    @Query("SELECT * FROM LoginEntity")
    List<LoginEntity> getAll();


    /*
     * Insert the object in database
     * @param loginEntity, object to be inserted
     */
    @Insert
    void insert(LoginEntity loginEntity);

    /*
     * update the object in database
     * @param loginEntity, object to be updated
     */
    @Update
    void update(LoginEntity loginEntity);

    /*
     * delete the object from database
     * @param loginEntity, object to be deleted
     */
    @Delete
    void delete(LoginEntity loginEntity);

    /*
     * delete list of objects from database
     * @param loginEntity, array of objects to be deleted
     */
    @Delete
    void delete(LoginEntity... loginEntities);      // LoginEntity... is varargs, loginEntities note is an array

    @Query("DELETE FROM LoginEntity")
    void deleteAll();
}
