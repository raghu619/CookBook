package com.example.android.cookbook.database;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.android.cookbook.models.RecipeEntityModel;

/**
 * Created by raghvendra on 13/7/18.
 */
@Database(entities ={RecipeEntityModel.class},version = 1,exportSchema = false)
public  abstract  class Maindatabase extends RoomDatabase {


    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "recipe_db";
    private static Maindatabase instance;
    public static Maindatabase getInstance(Context context){

        if(instance==null){

            synchronized (LOCK){

                instance= Room.databaseBuilder(context.getApplicationContext(),
                        Maindatabase.class,Maindatabase.DATABASE_NAME).allowMainThreadQueries().build();
            }
        }

            return instance;
    }


    public abstract Dao_recipe dao_recipe();


}
