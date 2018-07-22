package com.example.android.cookbook;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.android.cookbook.adapters.Recipe_list_item;
import com.example.android.cookbook.adapters.ingredients_list;
import com.example.android.cookbook.fragments.VideoFragment;
import com.example.android.cookbook.globaldata.Global_Constants;
import com.example.android.cookbook.globaldata.Global_Data;

import com.example.android.cookbook.idlingresources.simpleidlingresource;
import com.example.android.cookbook.models.RecipeEntityModel;
import com.example.android.cookbook.models.RecipeViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Recipe_detail_activity extends AppCompatActivity implements Recipe_list_item.OnItemClickListener ,VideoFragment.FragmentListener{
    private boolean mTwoPane;
    private Context context;
    private RecipeEntityModel recipesModel;

    @BindView(R.id.item_list)
    RecyclerView recyclerView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_activity);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }
        recipesModel = Global_Data.getInstance().getRecipeModel();
        setTitle(recipesModel.getName());

        final ingredients_list adapter = new ingredients_list(context, recipesModel.getIngredients(), recipesModel.getSteps(), mTwoPane);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(adapter);

            }
        }, 200);
    }

    @Override
    public void OnItemClick(int pos, ImageView imageView) {


        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putInt(Global_Constants.ARG_VIDEO_STEP, pos);
            bundle.putString(Global_Constants.ARG_RECIPE_NAME, recipesModel.getName());
            VideoFragment fragment = new VideoFragment();
            fragment.setArguments(bundle);
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {

            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra(Global_Constants.ARG_VIDEO_STEP, pos);
            intent.putExtra(Global_Constants.ARG_RECIPE_NAME, recipesModel.getName());
            startActivity(intent);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();

        }
        return true;
    }

    @Override
    public void set_fragment_data(long seekTo, boolean isPlaying) {
        Bundle bundle = new Bundle();
        bundle.putLong(Global_Constants.VID_SEEKTO, seekTo);
        bundle.putBoolean(Global_Constants.VID_IS_PLAYING, isPlaying);
        Global_Data.getInstance().setBundle(bundle);
    }
}
