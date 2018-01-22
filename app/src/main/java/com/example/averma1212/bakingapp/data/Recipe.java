package com.example.averma1212.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.averma1212.bakingapp.stepsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HP on 23-12-2017.
 */

public class Recipe{

    private static ArrayList<Recipe> recipes;
    private Integer id;
    private String name;
    private ArrayList <Steps> steps;
    private ArrayList <Ingredients> ings;
    private Integer servings;
    private String imageURL;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Recipe(JSONObject Json){
        JSONArray ing_array = null;
        try {
            id = Integer.valueOf(Json.getString("id"));
            name = Json.getString("name");
            ing_array = Json.getJSONArray("ingredients");
            imageURL = Json.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ings = new ArrayList<Ingredients>();
        for(int i=0;i<ing_array.length();i++){
            Ingredients Ing = null;
            try {
                Ing = new Ingredients(ing_array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ings.add(Ing);
        }
        JSONArray steps_array = null;
        try {
            steps_array = Json.getJSONArray("steps");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        steps = new ArrayList<Steps>();
        for (int i=0;i<steps_array.length();i++){
            Steps step = null;
            try {
                step = new Steps(steps_array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            steps.add(step);
        }
    }

    public String ingredientsList(){
        String list="";
        int i=0;
        for (Ingredients ingredients : ings){
            list+=(String.valueOf(++i) + ". " +
                    ingredients.getIngredient().substring(0,1).toUpperCase() +
                    ingredients.getIngredient().substring(1) + " - " +
                    ingredients.getQuantity() + " " +
                    ingredients.getMeasure() + "\n\n");
        }
        list=list.substring(0,list.length()-2);

        return list;
    }

    public String ingredientsListForWidget(){
        String list="";
        int i=0;
        list+="Ingredients: \n";
        for (Ingredients ingredients : ings){
            list+=(String.valueOf(++i) + ". " +
                    ingredients.getIngredient().substring(0,1).toUpperCase() +
                    ingredients.getIngredient().substring(1) + "\n");
        }
        list = list.substring(0,list.length()-1);
        return list;
    }

    public ArrayList<Steps> getSteps() {
        return steps;
    }

    public ArrayList<Ingredients> getIngs() {
        return ings;
    }

    public static void setRecipes(ArrayList<Recipe> recipe) {
        recipes = recipe;
    }

    public static Recipe getRecipe(int id){
        return recipes.get(id);
    }

    public static boolean isNull(){
        if(recipes==null) return true;
        return false;
    }

    public Steps getStep(int id) { return steps.get(id); }


}