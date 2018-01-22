package com.example.averma1212.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.averma1212.bakingapp.data.Recipe;

import timber.log.Timber;

import static android.R.attr.data;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {
    private static final String APP_NAME = "Baking App";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId,int index) {
        Recipe recipe = Recipe.getRecipe(index);
        String widgetTitle = recipe.getName();
        String widgetText = recipe.ingredientsListForWidget();
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        views.setTextViewText(R.id.appwidget_title, widgetTitle);
        views.setTextViewText(R.id.appwidget_text,widgetText);

        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        views.setOnClickPendingIntent(R.id.layout_widget,pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetService.setActionUpdateWidget(context);
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,int index) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,index);
        }
    }





    @Override
    public void onEnabled(Context context) {
        WidgetService.setActionUpdateWidget(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

