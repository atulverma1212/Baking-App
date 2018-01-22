package com.example.averma1212.bakingapp.Utils;

import android.util.Log;

import com.example.averma1212.bakingapp.data.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HP on 21-12-2017.
 */

public class JsonUtils {
    public static String[][] getNamesFromJson(String JsonString) throws JSONException {
        if(JsonString==null)
            return null;

        String[][] recipes;
        JSONArray result_arr = new JSONArray(JsonString);
        recipes = new String[2][result_arr.length()];
        for(int i=0;i<result_arr.length();i++)
        {
            JSONObject object = result_arr.getJSONObject(i);
            recipes[0][i] = object.getString("name");
            recipes[1][i] = object.getString("image");
        }
        return recipes;
    }

    public static ArrayList<Recipe> getRecipeFromJson(String json) throws JSONException{
        if(json==null) return null;

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        JSONArray array = new JSONArray(json);
        for(int i=0;i<array.length();i++){
            Recipe rec = new Recipe(array.getJSONObject(i));
            recipes.add(rec);
        }
        return recipes;
    }
}
