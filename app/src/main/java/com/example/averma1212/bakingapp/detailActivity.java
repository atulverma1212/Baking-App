package com.example.averma1212.bakingapp;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.averma1212.bakingapp.adapters.StepsAdapter;
import com.example.averma1212.bakingapp.data.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class detailActivity extends AppCompatActivity{
    private int recipeIndex;
    private int stepIndex;
    @BindView(R.id.nextButton)
    CardView nextButton;
    FragmentManager fragmentManager;
    private final String STEP = "step";
    private static int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        final DetailFragment detailFragment = new DetailFragment();
        fragmentManager = getSupportFragmentManager();
        final Intent startingIntent = getIntent();
        boolean choice = startingIntent.getBooleanExtra("ingredients",false);
        recipeIndex = startingIntent.getIntExtra("recipe",0);
        setTitle(Recipe.getRecipe(recipeIndex).getName());
        if(choice){
            detailFragment.setIngredients(Recipe.getRecipe(recipeIndex).ingredientsList());
            nextButton.setVisibility(View.GONE);
            if (savedInstanceState == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.detail_container, detailFragment)
                        .commit();
            }
        }
        else {
            stepIndex = startingIntent.getIntExtra("step", 0);

            if (stepIndex + 1 == Recipe.getRecipe(recipeIndex).getSteps().size()) {
                nextButton.setVisibility(View.GONE);
            }
            detailFragment.setSteps(Recipe.getRecipe(recipeIndex).getStep(stepIndex));


            if (savedInstanceState == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.detail_container, detailFragment)
                        .commit();
            } else {
                stepIndex = index;
                if (stepIndex + 1 == Recipe.getRecipe(recipeIndex).getSteps().size()) {
                    nextButton.setVisibility(View.GONE);
                }

                DetailFragment detailFragment1 = new DetailFragment();
                detailFragment1.setSteps(Recipe.getRecipe(recipeIndex).getStep(stepIndex));
                fragmentManager.beginTransaction()
                        .replace(R.id.detail_container, detailFragment1)
                        .commit();

            }
        }



        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepIndex++;

                if(stepIndex+1==Recipe.getRecipe(recipeIndex).getSteps().size()){
                    nextButton.setVisibility(View.GONE);
                }

                DetailFragment detailFragment1 = new DetailFragment();
                detailFragment1.setSteps(Recipe.getRecipe(recipeIndex).getStep(stepIndex));
                fragmentManager.beginTransaction()
                        .replace(R.id.detail_container,detailFragment1)
                        .commit();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        index = stepIndex;
    }
}
