package group10_cmsc436.pantrychef;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Nick Dean on 4/26/16.
 */
class RecipeAsyncTask extends AsyncTask<Void, Context, RecipeItem> {

    private Exception exception;

    private String email = "twhtie_6@msn.com";
    private String k = "5d2d82f5596e20218a43ae496842ca72";
    private String searchUrl = "http://food2fork.com/api/search?key=" + k;
    private String requestUrl = "http://food2fork.com/api/get?key=" + k;


    public RecipeAsyncTask(Context c){

    }

    protected RecipeItem doInBackground(Void... params) {
        String data = "";
        // Do some validation here
        HttpURLConnection urlConnection;

        try {
            java.net.URL url = new URL(requestUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            data = stringBuilder.toString();

            Log.e("Tag", stringBuilder.toString());

            bufferedReader.close();
            urlConnection.disconnect();

        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
        RecipeItem newR = null;
        return newR;
    }

    protected void onPostExecute(RecipeItem result) {
        if(result == null) {
            //TODO: Display toast of no found recipes


            Log.i("INFO", "No Recipe Found");
        } else {
            //TODO: Populate recipe view


        }
    }

    private String getRecipeInfoFromURL(String query){

        //TODO: Get the specific recipe information from the query

        return null;
    }

    private String getRecipesFromURL(String query){

        //TODO: Get the recipes that work with the ingredients

        return null;
    }

    //Creates a URL to query the database based off the ingredients
    private String generateIngredientQuery(ArrayList<String> ingredients){
        String query = searchUrl;

        //Adds query opperator
        query += "&q=";

        //Adds each ingredient to the query separated by a comma
        for (String temp : ingredients){
            query += temp + ",";
        }

        //Removes the comma at the very end
        query = query.replace(query.substring(query.length()-1), "");

        //Replaces all spaces with the ascii space "%20"
        query = query.replaceAll(" ", "%20");

        Log.i("QueryToFindRecipes", query);

        return query;
    }

    //Creates a URL to query the database for a specified recipe
    private String generateRecipeQuery(int id){
        String query = requestUrl;

        query += "&rId=" + id;
        
        return query;
    }
}