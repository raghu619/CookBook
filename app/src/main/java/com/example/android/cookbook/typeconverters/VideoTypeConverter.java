package com.example.android.cookbook.typeconverters;

import android.arch.persistence.room.TypeConverter;
import android.provider.MediaStore;

import com.example.android.cookbook.models.IngredientsModel;
import com.example.android.cookbook.models.VideosModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by raghvendra on 14/7/18.
 */

public class VideoTypeConverter  {



    @TypeConverter
    public static String list_to_string(List<VideosModel> list) {
        Gson gson = new Gson();
        return gson.toJson(list);


    }

    @TypeConverter
    public static List<VideosModel> string_to_list(String json) {


        Gson gson = new Gson();

        Type listType = new TypeToken<List<VideosModel>>() {
        }.getType();

        return gson.fromJson(json, listType);
    }
}
