package com.example.android.cookbook.typeconverters;

import android.arch.persistence.room.TypeConverter;

import com.example.android.cookbook.models.IngredientsModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by raghvendra on 14/7/18.
 */

public class IngredientsTypeConverters {


    @TypeConverter
    public static String list_to_string(List<IngredientsModel> list) {
        Gson gson = new Gson();
        return gson.toJson(list);


    }

    @TypeConverter
    public static List<IngredientsModel> string_to_list(String json) {


        Gson gson = new Gson();

        Type listType = new TypeToken<List<IngredientsModel>>() {
        }.getType();

        return gson.fromJson(json, listType);
    }
}