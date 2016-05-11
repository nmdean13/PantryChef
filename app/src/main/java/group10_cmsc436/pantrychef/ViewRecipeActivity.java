package group10_cmsc436.pantrychef;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class ViewRecipeActivity extends TabActivity implements View.OnClickListener {

    static String FILENAME = "SavedRecipes.txt";
    TabHost tabHost;
    String name;
    String id;
    String source_link;
    RecipeAsyncTask task;
    TextView link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        tabHost = getTabHost();
        tabHost.setup();

        name = getIntent().getStringExtra("recipe_name");
        id = getIntent().getStringExtra("recipe_id");

        task = new RecipeAsyncTask();
        String url = task.generateRecipeQuery(id);
        task.execute(url);
        try {
            task.get(5000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            Log.e("ERROR:", e.getMessage());
        }

        TextView socialRank = (TextView) findViewById(R.id.social_rank);
        socialRank.append(" " + task.getRank());

        TextView recipeName = (TextView) findViewById(R.id.recipe_name);
        recipeName.setText(name);

        TabHost.TabSpec descriptionTab = tabHost.newTabSpec("Overview");
        TabHost.TabSpec ingredientsTab = tabHost.newTabSpec("Ingredients");


        descriptionTab.setIndicator("Overview");
        descriptionTab.setContent(R.id.description);
        setUpDescriptionTab();


        ingredientsTab.setIndicator("Ingredients");
        ingredientsTab.setContent(R.id.ingredients);
        setUpIngredientsTab();


        tabHost.addTab(descriptionTab);
        tabHost.addTab(ingredientsTab);
        link = (TextView)findViewById(R.id.desc_string);
        source_link = task.getRecipeURL();
        link.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_view, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        return true;
    }

    public void setUpDescriptionTab() {
        ImageView imageView = (ImageView) findViewById(R.id.image);
        String imgURL = task.getImageURL();
        new ImageDownloader((ImageView) findViewById(R.id.image))
                .execute(imgURL);

        TextView description = (TextView) findViewById(R.id.desc_string);

        Button saveButton = (Button) findViewById(R.id.save);

        if(!getIntent().getBooleanExtra("save_button", true)) {
            saveButton.setVisibility(View.GONE);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String line = name + "||" + id;
                    if (!alreadySaved(line)) {
                        FileOutputStream fos = openFileOutput(FILENAME, MODE_PRIVATE | MODE_APPEND);
                        fos.write((line + "\n").getBytes());
                        fos.close();
                        Toast.makeText(ViewRecipeActivity.this, "Recipe saved!", Toast.LENGTH_SHORT).show();
                        ((Button) findViewById(R.id.save)).setVisibility(View.GONE);
                    } else {
                        Toast.makeText(ViewRecipeActivity.this, "Recipe is already saved.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(source_link));
        startActivity(browserIntent);
    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView mImage;

        public ImageDownloader(ImageView bmImage) {
            this.mImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap img = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                img = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return img;
        }

        protected void onPostExecute(Bitmap result) {
            mImage.setImageBitmap(result);
        }
    }

    protected boolean alreadySaved(String toBeSaved) {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String line;
            while (null != (line = reader.readLine())) {
                if(line.equals(toBeSaved)) {
                    return true;
                }
            }

            return false;
        } catch(Exception e) {
            Log.e("Error", e.getMessage());
            return false;
        }

    }

    public void setUpIngredientsTab() {
        ArrayList<String> ingredients = task.getIngredientsFromJSON();

        TextView ingredientsView = (TextView) findViewById(R.id.ingredients_text);

        for(int i = 0; i < ingredients.size(); i++) {
            ingredientsView.append("\u2022 " + ingredients.get(i) + "\n");
        }
    }
}
