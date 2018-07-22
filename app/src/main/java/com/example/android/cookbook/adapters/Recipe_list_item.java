package com.example.android.cookbook.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.cookbook.R;
import com.example.android.cookbook.models.RecipeEntityModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by raghvendra on 15/7/18.
 */

public class Recipe_list_item extends RecyclerView.Adapter<Recipe_list_item.RecipeViewHolder> {
    private  final List<RecipeEntityModel> data_list;
    private final Context context;
    private final OnItemClickListener listener;
    public Recipe_list_item(Context context, List<RecipeEntityModel> data_list){

       this.data_list=data_list;


        this.context = context;
        listener=(OnItemClickListener)context;
    }

    public interface OnItemClickListener{

        void OnItemClick(int pos, ImageView imageView);

    }


    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.recipe_item,null);



        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        RecipeEntityModel recipeEntityModel=data_list.get(position);

        holder.name.setText(recipeEntityModel.getName());
        holder.videosCount.setText(new StringBuilder().append(recipeEntityModel.getSteps().size())
                .append(context.getResources().getString(R.string.space))
                .append(context.getString(R.string.videos)).toString());
       switch (position){
           case 0:
               holder.img_food.setImageResource(R.drawable.nutellapie);
               break;
           case 1:
               holder.img_food.setImageResource(R.drawable.brownies);
               break;
           case 2:
               holder.img_food.setImageResource(R.drawable.yellow_cake);
               break;
           case 3:
               holder.img_food.setImageResource(R.drawable.cheesecake);
               break;

       }

    }



    @Override
    public int getItemCount() {
        return data_list.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.videosCount)
        TextView videosCount;
        @BindView(R.id.container)
        LinearLayout container;
        @BindView(R.id.img_food)
        ImageView img_food;
        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }

        @OnClick(R.id.container)
        public void  click_on(){

            listener.OnItemClick(getAdapterPosition(),img_food);
        }


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
