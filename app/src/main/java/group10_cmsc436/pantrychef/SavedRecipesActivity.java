package group10_cmsc436.pantrychef;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SavedRecipesActivity extends AppCompatActivity {

    private static final String FILE_NAME = "SavedRecipes.txt";
    private RadioGroup radioGroup;
    private ArrayList<RadioButton> radioButtons;
    private Button selectRecipe;
    private int numRecipes;

    RecipeListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes);

        selectRecipe = (Button) findViewById(R.id.select_recipe_button);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);


        selectRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        numRecipes = loadItems(numRecipes);

        if(numRecipes == 0) {
            Toast.makeText(getApplicationContext(), "No Saved Recipes.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    //Load saved recipes
    private int loadItems(int numRecipes) {
        BufferedReader reader = null;
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(fis));

            String name;

            while (null != (name = reader.readLine())) {
                RadioButton rButton = new RadioButton(this);
                rButton.setText(name);
                rButton.setId(numRecipes + 1);
                radioButtons.add(rButton);
                radioGroup.addView(rButton);
                numRecipes++;
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
        return numRecipes;
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
