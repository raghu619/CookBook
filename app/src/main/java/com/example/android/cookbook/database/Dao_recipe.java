package com.example.android.cookbook.database;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.cookbook.models.RecipeEntityModel;

import java.util.List;

import android.arch.lifecycle.LiveData;
/**
 * Created by raghvendra on 14/7/18.
 */

@Dao
public interface Dao_recipe  {
    @Query("SELECT * FROM  recipe_entity")
    LiveData<List<RecipeEntityModel>> getAll();


    @Insert
    void insertAll(List<RecipeEntityModel>data_list);


    @Query("DELETE FROM recipe_entity")
    void deleteAll();


   }
