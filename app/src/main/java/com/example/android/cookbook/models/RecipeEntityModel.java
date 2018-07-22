package com.example.android.cookbook.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.android.cookbook.typeconverters.IngredientsTypeConverters;
import com.example.android.cookbook.typeconverters.VideoTypeConverter;

import java.util.List;

/**
 * Created by raghvendra on 13/7/18.
 */
@Entity(tableName = "recipe_entity")
public class RecipeEntityModel {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int uid;
    private String id;
    private String name;
    @ColumnInfo(name = "ingredients")
    @TypeConverters(IngredientsTypeConverters.class)
    private List<IngredientsModel> ingredients;
    @ColumnInfo(name = "steps")
    @TypeConverters(VideoTypeConverter.class)
    private List<VideosModel>steps;

    @NonNull
    public int getUid() {
        return uid;
    }

    public void setUid(@NonNull int uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientsModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientsModel> ingredients) {
        this.ingredients = ingredients;
    }

    public List<VideosModel> getSteps() {
        return steps;
    }

    public void setSteps(List<VideosModel> steps) {
        this.steps = steps;
    }







}
