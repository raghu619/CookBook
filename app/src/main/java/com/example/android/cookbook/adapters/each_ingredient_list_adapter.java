package com.example.android.cookbook.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.cookbook.R;
import com.example.android.cookbook.models.IngredientsModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by raghvendra on 16/7/18.
 */

public class each_ingredient_list_adapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context context;
    private final List<IngredientsModel> ingredientsList;

    public each_ingredient_list_adapter(Context context, List<IngredientsModel> ingredientsList) {
        this.context = context;
        this.ingredientsList = ingredientsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_each_item, parent, false);
        return new  ingredient_View_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ingredientsList.get(position).getIngredient());
        stringBuilder.append(context.getString(R.string.colon));
        stringBuilder.append(ingredientsList.get(position).getQuantity());
        stringBuilder.append(context.getString(R.string.space));
        stringBuilder.append(ingredientsList.get(position).getMeasure());
        ((ingredient_View_Holder) holder).name.setText(stringBuilder.toString());

    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    class  ingredient_View_Holder extends RecyclerView.ViewHolder{

        @BindView(R.id.name)
        TextView name;

        public ingredient_View_Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
