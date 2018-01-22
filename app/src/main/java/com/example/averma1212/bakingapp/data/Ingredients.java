package com.example.averma1212.bakingapp.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HP on 22-12-2017.
 */

public class Ingredients {
    private String quantity,measure,ingredient;

    public Ingredients(JSONObject json){
        try {
            quantity = json.getString("quantity");
            measure = json.getString("measure");
            ingredient = json.getString("ingredient");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}
