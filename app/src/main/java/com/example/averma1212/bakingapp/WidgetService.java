package com.example.averma1212.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.averma1212.bakingapp.Utils.JsonUtils;
import com.example.averma1212.bakingapp.Utils.NetworkUtils;
import com.example.averma1212.bakingapp.data.Recipe;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by HP on 25-12-2017.
 */

public class WidgetService extends IntentService {

    public static final String ACTION_UPDATE_WIDGET = "update_widget";
    public WidgetService() {
        super("WidgetService");
        Timber.plant(new Timber.DebugTree());
    }

    public static void setActionUpdateWidget(Context context){
        Intent intent = new Intent(context,WidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!=null){
            String action = intent.getAction();
            if(action.equals(ACTION_UPDATE_WIDGET)){
                handleActionUpdateWidget();
            }
        } else {
            handleActionUpdateWidget();
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
            return true;
        return false;
    }

    private void handleActionUpdateWidget() {
        if(Recipe.isNull() && isConnected()){
            new getData().execute();
            SystemClock.sleep(3000);
        }
        else if(!isConnected()){
            return;
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidget.class));
        BakingWidget.updateWidget(this, appWidgetManager, appWidgetIds,0);
    }

    public class getData extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            String Json;
            try {
                Json = NetworkUtils.getResponseFromHttpUrl();
                Recipe.setRecipes(JsonUtils.getRecipeFromJson(Json));
                return Json;
            }catch(JSONException e){
                Timber.e("JsonException in Loader");
                return null;
            }
            catch (IOException e) {
                Timber.e("IOException in Loader");
                return null;
            }

        }

    }
}
