package com.example.averma1212.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.averma1212.bakingapp.adapters.*;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.averma1212.bakingapp.Utils.JsonUtils;
import com.example.averma1212.bakingapp.Utils.NetworkUtils;
import com.example.averma1212.bakingapp.data.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<String>,
    mAdapter.ListItemClickListener{

    private static final int RECIPE_NAMES_LOADER = 22;
    private static final int DELAY_MILI_SECONDS = 2000;

    @BindView(R.id.rv_mainActivity)
    RecyclerView rv_recipe;
    mAdapter recipeListAdapter;
    @BindView(R.id.tv_error_message)
    TextView tv_noInternet;
    @BindView(R.id.rv_loading)
    RelativeLayout rv_loading;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;


     // Only called from test, creates and returns a new {@link SimpleIdlingResource}.
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rv_recipe.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            rv_recipe.setLayoutManager(layoutManager);
        }
        rv_recipe.setHasFixedSize(true);
        if(!isConnected()){
            showNotConnected();
            return;
        }
        else
            getSupportLoaderManager().initLoader(RECIPE_NAMES_LOADER, null, this);
        getIdlingResource();
    }

   @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            private String mJson;

            @Override
            protected void onStartLoading() {
                showLoading();
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(false);
                }
                if (mJson != null) {
                    deliverResult(mJson);
                    showMain();
                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                String Json;
                try {
                    Json = NetworkUtils.getResponseFromHttpUrl();
                    return Json;
                } catch (IOException e) {
                    Timber.e("IOException in Loader");
                    return null;
                }
            }

            @Override
            public void deliverResult(String Json) {
                Timber.v("delivering results");
                mJson = Json;
                super.deliverResult(Json);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        String[][] names=null;
        try {
            names = JsonUtils.getNamesFromJson(data);
            Recipe.setRecipes(JsonUtils.getRecipeFromJson(data));
        } catch (JSONException e) {
            Timber.e("JSON Exception");
        }
        recipeListAdapter = new mAdapter(names,this);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    if(recipeListAdapter!=null)
                        rv_recipe.setAdapter(recipeListAdapter);
                    if (mIdlingResource != null) {
                        mIdlingResource.setIdleState(true);
                    }
                }
        }, DELAY_MILI_SECONDS);
        showMain();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }


    @Override
    public void onListItemClicked(int clickedItemIndex) {
        //Update Widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidget.class));
        BakingWidget.updateWidget(this, appWidgetManager, appWidgetIds, clickedItemIndex);

        Intent startStep = new Intent(MainActivity.this, stepsActivity.class);
        startStep.putExtra("recipe_class",clickedItemIndex);
        startActivity(startStep);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
            return true;
        return false;
    }

    public void showLoading(){
       // Timber.e("showloading");
        rv_loading.setVisibility(View.VISIBLE);
        rv_recipe.setVisibility(View.INVISIBLE);
    }

    public void showMain(){
      //  Timber.e("showmain");
        rv_loading.setVisibility(View.INVISIBLE);
        rv_recipe.setVisibility(View.VISIBLE);
    }

    public void showNotConnected(){
        rv_recipe.setVisibility(View.INVISIBLE);
        tv_noInternet.setVisibility(View.VISIBLE);
    }
}
