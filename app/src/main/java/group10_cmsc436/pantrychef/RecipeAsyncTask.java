package group10_cmsc436.pantrychef;

import android.os.AsyncTask;
import android.util.Log;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by Nick Dean on 4/26/16.
 */
class RecipeAsyncTask extends AsyncTask<Void, Void, String> {

    private Exception exception;

    private String email = "twhtie_6@msn.com";
    private String key = "5d2d82f5596e20218a43ae496842ca72";
    private String URL = "http://food2fork.com/api/search?key=" + key + "&q=shredded%20chicken";


    protected String doInBackground(Void... params) {
        String data = "";
        // Do some validation here
        HttpURLConnection urlConnection;

        try {
            java.net.URL url = new URL(URL);
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

        return data;
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        Log.i("INFO", response);

    }
}