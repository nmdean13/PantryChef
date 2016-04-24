package group10_cmsc436.pantrychef;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;

public class SavedRecipesActivity extends ListActivity {

    private static final String FILE_NAME = "SavedRecipes.txt";
    private Button selectRecipe;

    RecipeListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes);

        selectRecipe = (Button) findViewById(R.id.select_recipe_button);

        //TODO: Allow mAdapter to add recipes
        getListView().setAdapter(mAdapter);

        //TODO: Make select button open recipe
        selectRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAdapter.getCount() == 0)
            loadItems();
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveItems();

    }

    //Load saved recipes
    private void loadItems() {
        BufferedReader reader = null;
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(fis));

            String name;

            while (null != (name = reader.readLine())) {
                mAdapter.add(new RecipeItem(name));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Save Recipes
    private void saveItems() {
        PrintWriter writer = null;
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    fos)));

            for (int idx = 0; idx < mAdapter.getCount(); idx++) {
                writer.println(mAdapter.getItem(idx));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }
}
