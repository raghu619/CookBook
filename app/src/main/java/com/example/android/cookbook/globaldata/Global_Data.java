package com.example.android.cookbook.globaldata;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.example.android.cookbook.models.IngredientsModel;
import com.example.android.cookbook.models.RecipeEntityModel;
import com.example.android.cookbook.models.RecipeViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by raghvendra on 14/7/18.
 */

public class Global_Data extends Application {


    private  static  Global_Data instance;
    private RequestQueue mRequestQueue;
    private RecipeEntityModel recipeViewModel;
    private Bundle bundle = new Bundle();




    @Override
    public void onCreate() {
        super.onCreate();
        instance=(Global_Data)getApplicationContext();
    }

    private synchronized RequestQueue getmRequestQueue(){

        if(mRequestQueue==null){

            mRequestQueue = Volley.newRequestQueue(instance, new HurlStack());

        }
        return  mRequestQueue;

    }

    public synchronized <T>void  add_request_to_queue(Request<T> request){

        request.setTag("data");
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(30000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getmRequestQueue().add(request);
    }

    public RecipeEntityModel getRecipeModel(){


        return recipeViewModel;
    }


    public void setRecipeViewModel(RecipeEntityModel recipeViewModel){

        this.recipeViewModel=recipeViewModel;
    }

    public  void save_Ingredients(Context context, List<IngredientsModel> ingredients){

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<IngredientsModel>>() {
        }.getType();
        String ingredients_string=gson.toJson(ingredients,listType);
        SharedPreferences.Editor prefs=context.getSharedPreferences(Global_Constants.SHARED_NAME,0).edit();
        prefs.putString(Global_Constants.SHARED_INGREDIENTS,ingredients_string);
        prefs.commit();
    }

    public   List<IngredientsModel> get_Ingredients(Context context){

        SharedPreferences prefs=context.getSharedPreferences(Global_Constants.SHARED_NAME,0);

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<IngredientsModel>>() {
        }.getType();
        String ingredients_string=prefs.getString(Global_Constants.SHARED_INGREDIENTS,Global_Constants.NO_DATA);

        return gson.fromJson(ingredients_string,listType);

    }




public  void save_recipe_name(Context context, String name){

    SharedPreferences prefs = context.getSharedPreferences(Global_Constants.SHARED_NAME,0);
    prefs.edit().putString(Global_Constants.SHARED_RECIPE_NAME,name).commit();

}

public  String get_recipe_name(Context context){

    SharedPreferences prefs=context.getSharedPreferences(Global_Constants.SHARED_NAME,0);
    return prefs.getString(Global_Constants.SHARED_RECIPE_NAME,Global_Constants.NO_DATA);


}

    public static Global_Data getInstance()
    {

        return instance;
    }
    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

}
