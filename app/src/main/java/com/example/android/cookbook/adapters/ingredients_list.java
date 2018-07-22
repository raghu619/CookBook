package com.example.android.cookbook.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.cookbook.R;
import com.example.android.cookbook.globaldata.Global_Data;
import com.example.android.cookbook.models.IngredientsModel;
import com.example.android.cookbook.models.VideosModel;
import com.example.android.cookbook.utils.Video_Image_AsyncTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by raghvendra on 16/7/18.
 */

public class ingredients_list extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final Context context;
    private final List<IngredientsModel> mingredientsList;
    private final List<VideosModel> mvideoStepList;
    private final boolean mTwoPane;
    private  final int INGREDIENT_VIEW=0;
    private final int  VIDEO_VIEW=1;
    public Recipe_list_item.OnItemClickListener listener;
    public int getItemViewType(int position) {
        if(position==0)
            return INGREDIENT_VIEW;

        else
            return VIDEO_VIEW;


    }


    public ingredients_list(Context context, List<IngredientsModel> ingredientsList, List<VideosModel> videoStepList, boolean mTwoPane) {
        this.context = context;
        this.mingredientsList = ingredientsList;
        this.mvideoStepList = videoStepList;
        this.mTwoPane = mTwoPane;
        this.listener=(Recipe_list_item.OnItemClickListener)context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==INGREDIENT_VIEW) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_item, parent, false);

            return new Ingredients_View_Holder(view);
        }else if(viewType==VIDEO_VIEW){


View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item,parent,false);

           return new Video_View_holder(view);
        }

        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0 && mvideoStepList.size() > 0 && mTwoPane)
            listener.OnItemClick(position, null);

        if(holder instanceof Ingredients_View_Holder) {
            final Ingredients_View_Holder ingredientsHolder = (Ingredients_View_Holder) holder;
            each_ingredient_list_adapter adapter = new each_ingredient_list_adapter(context, mingredientsList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            ingredientsHolder.recyclerView.setLayoutManager(linearLayoutManager);
            ingredientsHolder.recyclerView.setAdapter(adapter);
            StringBuilder builder = new StringBuilder();
            builder.append(String.valueOf(mingredientsList.size()));
            builder.append(context.getString(R.string.space));
            builder.append(context.getString(R.string.ingredients));
            ingredientsHolder.txt_ingredients.setText(builder.toString());

        }
        else if(holder instanceof Video_View_holder){

            final int real_position=position-1;
            final Video_View_holder video_view_holder=(Video_View_holder)holder;
            StringBuilder builder = new StringBuilder();
            builder.append(context.getString(R.string.step));
            builder.append(context.getString(R.string.space));
            builder.append(String.valueOf(Integer.parseInt(mvideoStepList.get(real_position).getId()) + 1));
            video_view_holder.txtStepNo.setText(builder.toString());
            video_view_holder.txtTitle.setText(mvideoStepList.get(real_position).getShortDescription());
            video_view_holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global_Data.getInstance().setBundle(null);
                    listener.OnItemClick(real_position, null);
                }
            });
            Picasso.get().load(mvideoStepList.get(real_position).getThumbnailURL()).placeholder(R.drawable.placeholder_video)
                     .error(R.drawable.placeholder_video)
                    .into(video_view_holder.img_step_thumb, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Video_Image_AsyncTask.retrive_image_from_video(mvideoStepList.get(real_position).getVideoURL()
                                    ,video_view_holder.img_step_thumb);


                        }
                    }) ;



        }
    }

    @Override
    public int getItemCount() {
        return mvideoStepList.size()+1;
    }

    class Ingredients_View_Holder extends RecyclerView.ViewHolder{
        @BindView(R.id.txt_ingredients)
        TextView txt_ingredients;
        @BindView(R.id.recycle)
        RecyclerView recyclerView;
        @BindView(R.id.container)
        LinearLayout container;

        @BindView(R.id.cardView)
        CardView cardView;


        public Ingredients_View_Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class  Video_View_holder extends RecyclerView.ViewHolder{
        @BindView(R.id.txt_step_no)
        TextView txtStepNo;
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.container)
        LinearLayout container;
        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.img_step_thumb)
        ImageView img_step_thumb;


        public Video_View_holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
