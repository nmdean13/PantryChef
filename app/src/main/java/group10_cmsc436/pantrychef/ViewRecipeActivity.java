package group10_cmsc436.pantrychef;

import android.app.TabActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class ViewRecipeActivity extends TabActivity {

    static String FILENAME = "SavedRecipes.txt";
    TabHost tabHost;
    String name;
    String id;
    RecipeAsyncTask task;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        tabHost = getTabHost();
        tabHost.setup();
        saveButton = (Button) findViewById(R.id.save);

        name = getIntent().getStringExtra("recipe_name");
        id = getIntent().getStringExtra("recipe_id");

        boolean showButton = getIntent().getBooleanExtra("save_button", true);

        if (!showButton) {
            saveButton.setVisibility(View.GONE);
        }

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



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_view, menu);
        return true;
    }

    public void setUpDescriptionTab() {
        ImageView imageView = (ImageView) findViewById(R.id.image);
        String imgURL = task.getImageURL();
        new ImageDownloader((ImageView) findViewById(R.id.image))
                .execute(imgURL);

        TextView socialRank = (TextView) findViewById(R.id.social_rank);
        socialRank.append(" " + task.getSocialRank());
        String url = getResources().getString(R.string.get_directions);

        TextView description = (TextView) findViewById(R.id.desc_string);
        String recipeUrl = "<a href='" + task.getRecipeURL() + "'> " + url + " </a>";

        description.setMovementMethod(LinkMovementMethod.getInstance());
        description.setText(Html.fromHtml(recipeUrl));


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
                        saveButton.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(ViewRecipeActivity.this, "Recipe is already saved.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
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
