package com.example.android.cookbook;

import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.arch.lifecycle.Observer;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.cookbook.adapters.Recipe_list_item;
import com.example.android.cookbook.database.Maindatabase;
import com.example.android.cookbook.globaldata.Global_Constants;
import com.example.android.cookbook.globaldata.Global_Data;
import com.example.android.cookbook.idlingresources.simpleidlingresource;
import com.example.android.cookbook.models.RecipeEntityModel;
import com.example.android.cookbook.models.RecipeViewModel;
import com.example.android.cookbook.widget.CookBookWidget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Recipe_list_item.OnItemClickListener {
    @BindView(R.id.recipeRecycle)
    RecyclerView recipeRecycle;
    private ProgressDialog progressDialog;
    private Context context;
    private Global_Data global_data;
    private Maindatabase maindatabase;
    private List<RecipeEntityModel> list_data;
private  boolean FROMSPLASH;
private int mWidgetId;
private String checking="";
    @Nullable
    private simpleidlingresource mIdlingResource;
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new simpleidlingresource();
        }
        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;

        global_data = Global_Data.getInstance();
        maindatabase = Maindatabase.getInstance(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        getIdlingResource();
         Intent splashintent=getIntent();

         if(splashintent!=null){
             FROMSPLASH=splashintent.getBooleanExtra(Global_Constants.SPLASH_INTENT,false);
             if(getIntent().hasExtra(Global_Constants.WIDGET_INIT))
                  checking=getIntent().getStringExtra(Global_Constants.WIDGET_INIT);
             Bundle widgetbundle=getIntent().getExtras();
             if(widgetbundle !=null){
                 mWidgetId=widgetbundle.getInt( AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

             }


         }
        int spanCount = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 3;
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, spanCount);
        recipeRecycle.setLayoutManager(gridLayoutManager);
        ViewInit();
        recipe_network_request();


    }

    private void ViewInit() {


        RecipeViewModel model = ViewModelProviders.of((FragmentActivity) context).get(RecipeViewModel.class);
        model.getLivedata().observe(MainActivity.this, observer);
    }

    final Observer<List<RecipeEntityModel>> observer = new Observer<List<RecipeEntityModel>>() {
        @Override
        public void onChanged(@Nullable List<RecipeEntityModel> list) {
            if (list.size() > 0) {
                list_data = new ArrayList<>();
                list_data.addAll(list);
                populateUi();
            }
        }
    };

    private void populateUi() {
        progressDialog.dismiss();

        if (list_data.size() != 0) {

            Recipe_list_item adapter = new Recipe_list_item(context, list_data);
            recipeRecycle.setAdapter(adapter);
        }

    }

    private void recipe_network_request() {

        progressDialog.show();
        mIdlingResource.setIdleState(false);

        StringRequest json_request = new StringRequest(Request.Method.GET, BuildConfig.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray respnse_array = new JSONArray(response);


                    if (respnse_array.length() > 0) {
                        maindatabase.dao_recipe().deleteAll();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<RecipeEntityModel>>() {
                        }.getType();
                        list_data = gson.fromJson(respnse_array.toString(), listType);
                        maindatabase.dao_recipe().insertAll(list_data);
                        mIdlingResource.setIdleState(true);

                        populateUi();



                    } else {

                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                showError(error);
            }
        });
        global_data.add_request_to_queue(json_request);
    }

    @Override
    public void OnItemClick(int pos, ImageView imageView) {
        if (checking.equals(Global_Constants.WIDGET_INIT) || getIntent().hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID) ) {

            global_data.save_recipe_name(context,list_data.get(pos).getName());
            global_data.save_Ingredients(context,list_data.get(pos).getIngredients());
            Intent intent = new Intent(context, CookBookWidget.class);
            intent.setAction(Global_Constants.WIDGET_DATA_CHANGE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,mWidgetId);
            setResult(RESULT_OK, intent);
            sendBroadcast(intent);
            ((AppCompatActivity) context).finish();



        } else {

            global_data.setRecipeViewModel(list_data.get(pos));
            global_data.setBundle(null);
            Intent intent = new Intent(context, Recipe_detail_activity.class);
            startActivity(intent);
        }
    }

    public  void showError(Exception error) {
        String message = null;
        if (error instanceof NetworkError) {
            message = context.getString(R.string.error_network);
        } else if (error instanceof ServerError) {
            message = context.getString(R.string.error_server);
        } else if (error instanceof AuthFailureError) {
            message = context.getString(R.string.error_auth);
        } else if (error instanceof ParseError) {
            message = context.getString(R.string.error_parse);
        } else if (error instanceof TimeoutError) {
            message = context.getString(R.string.error_timeout);
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}
