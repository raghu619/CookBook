package com.example.android.cookbook.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.android.cookbook.MainActivity;
import com.example.android.cookbook.R;
import com.example.android.cookbook.globaldata.Global_Constants;
import com.example.android.cookbook.globaldata.Global_Data;
import com.example.android.cookbook.models.IngredientsModel;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class CookBookWidget extends AppWidgetProvider {
    private RemoteViews mViews;

     void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {



        // Construct the RemoteViews object
        mViews = new RemoteViews(context.getPackageName(), R.layout.cook_book_widget);


         Intent intent=new Intent(context, MainActivity.class);
         intent.putExtra(Global_Constants.WIDGET_INIT,Global_Constants.WIDGET_INIT);
         PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);
         mViews.setOnClickPendingIntent(R.id.img,pendingIntent);
         appWidgetManager.updateAppWidget(appWidgetId, mViews);
        // Instruct the widget manager to update the widget

        }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        final String action = intent.getAction();
        mViews = new RemoteViews(context.getPackageName(), R.layout.cook_book_widget);
        Intent launchActivity = new Intent(context,MainActivity.class);
        launchActivity.putExtra(Global_Constants.WIDGET_INIT,Global_Constants.WIDGET_INIT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchActivity, 0);
        mViews.setOnClickPendingIntent(R.id.img, pendingIntent);
        try {
            if (action.equals(AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED) ||
                    action.equals(Global_Constants.WIDGET_DATA_CHANGE)) {
                mViews.setTextViewText(R.id.txt_ingredients, Global_Data.getInstance().get_recipe_name(context));
                List<IngredientsModel> list = Global_Data.getInstance().get_Ingredients(context);
                StringBuilder builder = new StringBuilder();
                for (IngredientsModel model : list) {
                    builder.append(model.getIngredient() + " " + model.getQuantity() + " " + model.getMeasure() + "\n");


                }
                mViews.setTextViewText(R.id.text, builder.toString());
                //Now update all widgets
                AppWidgetManager.getInstance(context).updateAppWidget(
                        new ComponentName(context, CookBookWidget.class), mViews);

            }
        }
        catch (Exception e){

            Toast.makeText(context,"ready",Toast.LENGTH_LONG).show();
        }

        }
}

