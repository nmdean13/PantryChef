package group10_cmsc436.pantrychef;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nick Dean on 4/26/16.
 */
class RecipeAsyncTask extends AsyncTask<String, Context, ArrayList<String>> {

    private Exception exception;

    private String email = "twhtie_6@msn.com";
    private String k = "5d2d82f5596e20218a43ae496842ca72";
    private String searchUrl = "http://food2fork.com/api/search?key=" + k;
    private String requestUrl = "http://food2fork.com/api/get?key=" + k;

    private JSONObject jsonObject;


    public RecipeAsyncTask(){ }

    protected ArrayList<String> doInBackground(String... params) {
        HttpURLConnection urlConnection;
        BufferedReader bufferedReader;
        try {
            java.net.URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String jsonString = bufferedReader.readLine();
            bufferedReader.close();
            urlConnection.disconnect();
            jsonObject = new JSONObject(jsonString);
            Log.i("doInBackground", jsonString);
        } catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }

        return new ArrayList<String>();
    }

    protected HashMap<String, String> getRecipesFromURL(String line){

        HashMap<String, String> id_and_name = new HashMap<String, String>();
        char[] char_array = line.toCharArray();

        String key = "";
        String val = "";

        for (int i = 0; i < char_array.length-5; i++) {
            if (line.substring(i, i + 5).equals("http:/")) {
                key = line.substring(i + 26, i + 30);
            }
            if (line.substring(i, i + 4).equals("title")) {
                int j;
                for (j = i + 9; j < char_array.length; j++) {
                    if (char_array[j] == '"') {
                        break;
                    }
                    val = line.substring(i + 9, j);
                }
            }
            if (!key.equals("") && !val.equals("")) {
                id_and_name.put(key, val);
                key = "";
                val = "";
            }
        }
        return id_and_name;
    }

    //Creates a URL to query the database based off the ingredients
    protected String generateIngredientQuery(ArrayList<String> ingredients){
        String query = searchUrl;

        //Adds query opperator
        query += "&q=";

        //Adds each ingredient to the query separated by a comma
        for (String temp : ingredients){
            query += temp + ",";
        }

        //Removes the comma at the very end
        query = query.substring(0, query.length()-1);

        //Replaces all spaces with the ascii space "%20"
        query = query.replaceAll(" ", "%20");

        Log.i("QueryToFindRecipes", query);

        return query;
    }

    //Creates a URL to query the database for a specified recipe
    protected String generateRecipeQuery(String id){
        String query = requestUrl;

        query += "&rId=" + id;

        Log.i("FindSpecificRecipe", query);
        return query;
    }

    //Parses JSON response for recipe names
    protected ArrayList<String> getRecipeNamesFromJSON() {
        ArrayList<String> recipeNames = new ArrayList<String>();
        try {
            JSONArray recipesArray = jsonObject.getJSONArray("recipes");

            for(int i=0;i < recipesArray.length();i++) {
                JSONObject recipeObj = (JSONObject) recipesArray.get(i);
                String s = recipeObj.getString("title");
                if (s.contains("&amp;")) {
                    char[] fixed_string = new char[s.length()-4];
                    int buffer = 0;
                    for (int j = 0; j < s.length(); j++) {
                        if (s.charAt(j) == '&' && s.charAt(j+1) == 'a') {
                            j += 5;
                            buffer = -4;
                            fixed_string[j - 6] = ' ';
                            fixed_string[j-5] = '&';
                        }
                        fixed_string[j+buffer] = s.charAt(j);
                    }
                    s = new String(fixed_string);
                }
                recipeNames.add(s);
            }

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return recipeNames;
    }

    protected ArrayList<String> getRecipeIDsFromJSON() {
        ArrayList<String> recipeIDs = new ArrayList<String>();
        try {
            JSONArray recipesArray = jsonObject.getJSONArray("recipes");

            for(int i=0;i < recipesArray.length();i++){
                JSONObject recipeObj = (JSONObject) recipesArray.get(i);
                recipeIDs.add(recipeObj.getString("recipe_id"));
            }

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return recipeIDs;
    }

    protected String getImageURL() {
        String url = new String();
        try {
            JSONObject recipeObj = jsonObject.getJSONObject("recipe");
            url = recipeObj.getString("image_url");

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        return url;
    }

    protected String getRank() {
        String rank = new String();
        try {
            JSONObject recipeObj = jsonObject.getJSONObject("recipe");
            rank = recipeObj.getString("social_rank");
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        return rank;
    }

    protected String getRecipeURL() {
        String url = new String();
        try {
            JSONObject recipeObj = jsonObject.getJSONObject("recipe");
            url = recipeObj.getString("source_url");

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        return url;
    }

    protected ArrayList<String> getIngredientsFromJSON() {
        ArrayList<String> ingredients = new ArrayList<String>();
        try {
            JSONObject recipeObj = jsonObject.getJSONObject("recipe");
            JSONArray ingredientsArray = recipeObj.getJSONArray("ingredients");
            for(int i=0;i < ingredientsArray.length();i++){
                ingredients.add((String) ingredientsArray.get(i));
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return ingredients;
    }
}