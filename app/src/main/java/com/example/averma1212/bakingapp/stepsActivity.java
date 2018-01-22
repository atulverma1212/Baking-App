package com.example.averma1212.bakingapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.averma1212.bakingapp.adapters.StepsAdapter;
import com.example.averma1212.bakingapp.adapters.mAdapter;
import com.example.averma1212.bakingapp.data.Recipe;
import com.example.averma1212.bakingapp.data.Steps;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class stepsActivity extends AppCompatActivity implements StepsAdapter.StepItemClickListener{
    private ArrayList<Steps> mSteps;
    private int position;
    private boolean mTwoPane;
    private final String TAG = stepsActivity.class.getSimpleName();

    @BindView(R.id.ingredients_card_view)
    CardView cv_ingredients;
    @BindView(R.id.rv_stepsName)
    RecyclerView rv_stepsName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        Intent startingIntent = getIntent();

        if(findViewById(R.id.tablet_layout)!=null){
            mTwoPane=true;

            if(savedInstanceState==null){

                FragmentManager fragmentManager = getSupportFragmentManager();
                DetailFragment detailFragment = new DetailFragment();
                boolean choice = startingIntent.getBooleanExtra("ingredients",false);
                int recipeIndex = startingIntent.getIntExtra("recipe", 0);
                if(choice){
                    detailFragment.setIngredients(Recipe.getRecipe(recipeIndex).ingredientsList());
                    ((CardView) findViewById(R.id.nextButton)).setVisibility(View.GONE);
                }
                else{
                    int stepIndex = startingIntent.getIntExtra("step", 0);
                    detailFragment.setSteps(Recipe.getRecipe(recipeIndex).getStep(stepIndex));
                }

                fragmentManager.beginTransaction()
                        .add(R.id.detail_container,detailFragment)
                        .commit();
            } else {
                //do nothing
            }


        } else {
            mTwoPane = false;
        }



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_stepsName.setLayoutManager(layoutManager);
        rv_stepsName.setHasFixedSize(true);
        StepsAdapter stepsAdapter;
        try{
            position = startingIntent.getIntExtra("recipe_class",0);
            mSteps = Recipe.getRecipe(position).getSteps();
        }catch (Exception e){
            Timber.e("Error recieving position");
        }
        if(mSteps!=null) {
            stepsAdapter = new StepsAdapter(mSteps,this);
            rv_stepsName.setAdapter(stepsAdapter);
        } else {
            Timber.e("mSteps not Initialized");
        }

        setTitle(Recipe.getRecipe(position).getName());
        cv_ingredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTwoPane){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    DetailFragment mdetailFragment = new DetailFragment();
                    mdetailFragment.setIngredients(Recipe.getRecipe(position).ingredientsList());

                    fragmentManager.beginTransaction()
                            .replace(R.id.detail_container, mdetailFragment)
                            .commit();
                }

                else{
                    Intent startStep = new Intent(stepsActivity.this, detailActivity.class);
                    startStep.putExtra("recipe", position);
                    startStep.putExtra("ingredients", true);
                    startActivity(startStep);
                }
            }
        });

    }

    @Override
    public void onStepItemClicked(int clickedItemIndex) {

        if (mTwoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DetailFragment mdetailFragment = new DetailFragment();
            mdetailFragment.setSteps(Recipe.getRecipe(position).getStep(clickedItemIndex));

            fragmentManager.beginTransaction()
                    .replace(R.id.detail_container, mdetailFragment)
                    .commit();
        } else {
            Intent startStep = new Intent(this, detailActivity.class);
            startStep.putExtra("recipe", position);
            startStep.putExtra("step", clickedItemIndex);
            startActivity(startStep);
        }
    }

}
