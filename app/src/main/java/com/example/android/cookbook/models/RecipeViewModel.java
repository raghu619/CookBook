package com.example.android.cookbook.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.cookbook.database.Maindatabase;

import java.util.List;

/**
 * Created by raghvendra on 14/7/18.
 */

public class RecipeViewModel extends AndroidViewModel {

    private final LiveData<List<RecipeEntityModel>>Livedata;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        Livedata = Maindatabase.getInstance(application).dao_recipe().getAll();

    }



    public LiveData<List<RecipeEntityModel>> getLivedata(){

        return Livedata;
    }




}
