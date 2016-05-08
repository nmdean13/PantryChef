package group10_cmsc436.pantrychef;

import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;


public class ViewRecipeActivity extends TabActivity {

    TabHost tabHost;
    String name;
    String id;
    RecipeAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        tabHost = getTabHost();
        tabHost.setup();

        name = getIntent().getStringExtra("recipe_name");
        id = getIntent().getStringExtra("recipe_id");

        task = new RecipeAsyncTask(getApplicationContext());
        String url = task.generateRecipeQuery(id);
        task.execute(url);
        try {
            task.get(5000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            Log.e("ERROR:", e.getMessage());
        }

        TextView recipeName = (TextView) findViewById(R.id.recipe_name);
        recipeName.setText(name);

        TabHost.TabSpec descriptionTab = tabHost.newTabSpec("Description");
        TabHost.TabSpec ingredientsTab = tabHost.newTabSpec("Ingredients");
        TabHost.TabSpec directionsTab = tabHost.newTabSpec("Directions");


        descriptionTab.setIndicator("Description");
        descriptionTab.setContent(R.id.description);
        setUpDescriptionTab();


        ingredientsTab.setIndicator("Ingredients");
        ingredientsTab.setContent(R.id.ingredients);

        directionsTab.setIndicator("Directions");
        directionsTab.setContent(R.id.directions);

        tabHost.addTab(descriptionTab);
        tabHost.addTab(ingredientsTab);
        tabHost.addTab(directionsTab);


    }

    public void setUpDescriptionTab() {
        ImageView imageView = (ImageView) findViewById(R.id.image);
        String imgURL = task.getImageURL();
        new ImageDownloader((ImageView) findViewById(R.id.image))
                .execute(imgURL);

        //TODO: fetch description
        TextView description = (TextView) findViewById(R.id.desc_string);

        Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: save recipe
            }
        });
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
}
