package group10_cmsc436.pantrychef;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SavedRecipesActivity extends AppCompatActivity {

    static final String FILENAME = "SavedRecipes.txt";
    RadioGroup radioGroup;
    ArrayList<RadioButton> radioButtons;
    Button selectRecipe;
    Button deleteRecipe;
    int numRecipes;

    ArrayList<String> recipes;
    ArrayList<String> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes);

        recipes =new ArrayList<String>();
        ids = new ArrayList<String>();

        selectRecipe = (Button) findViewById(R.id.select_recipe_button);
        deleteRecipe = (Button) findViewById(R.id.delete_recipe_button);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButtons = new ArrayList<RadioButton>();

        selectRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected = (String) radioButtons.get(radioGroup.getCheckedRadioButtonId()).getText();
                Intent selectIntent = new Intent(SavedRecipesActivity.this, ViewRecipeActivity.class);
                selectIntent.putExtra("recipe_name", selected);
                selectIntent.putExtra("recipe_id", ids.get(recipes.indexOf(selected)));
                selectIntent.putExtra("save_button", false);
                startActivity(selectIntent);
            }
        });

        deleteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String selected = (String) radioButtons.get(radioGroup.getCheckedRadioButtonId()).getText();
                final int pos = recipes.indexOf(selected);

                AlertDialog.Builder builder = new AlertDialog.Builder(SavedRecipesActivity.this);
                builder.setMessage("Delete " + selected + "?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            radioGroup.removeViewAt(pos);
                            radioButtons.remove(pos);
                            recipes.remove(pos);
                            ids.remove(pos);
                            numRecipes--;
                            saveItems();
                            selectRecipe.setEnabled(false);
                            selectRecipe.setBackgroundColor(getResources().getColor(R.color.inactiveButton));
                            deleteRecipe.setEnabled(false);
                            deleteRecipe.setBackgroundColor(getResources().getColor(R.color.inactiveButton));
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage());
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_saved_recipes, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SavedRecipesActivity.this);
            builder.setMessage("Delete all recipes?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteFile(FILENAME);
                    reset();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        reset();
        numRecipes = loadItems(numRecipes);


        if(numRecipes == 0) {
            Toast.makeText(getApplicationContext(), "No Saved Recipes.",
                    Toast.LENGTH_SHORT).show();
        } else {

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    selectRecipe.setEnabled(true);
                    selectRecipe.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    deleteRecipe.setEnabled(true);
                    deleteRecipe.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            });

            for (int i = 0; i < numRecipes; i++) {
                radioButtons.add(new RadioButton(this));
                radioButtons.get(i).setText(recipes.get(i));
                radioButtons.get(i).setId(i);
                radioButtons.get(i).setTextSize(20);
                radioGroup.addView(radioButtons.get(i));
            }
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
            FileInputStream fis = openFileInput(FILENAME);
            reader = new BufferedReader(new InputStreamReader(fis));

            String line;
            String[] params;
            String name;
            String id;

            while (null != (line = reader.readLine())) {
                Log.d("line", line);
                params = line.split("\\|\\|");
                name = params[0];
                Log.d("name", name);
                id = params[1];
                Log.d("id",id);
                numRecipes++;
                recipes.add(name);
                ids.add(id);
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
            FileOutputStream fos = openFileOutput(FILENAME, MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    fos)));

            for (int i = 0; i < numRecipes; i++) {
                writer.write(recipes.get(i) + "||" + ids.get(i) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }

    public void reset() {
        recipes.clear();
        ids.clear();
        numRecipes = 0;
        radioButtons.clear();
        radioGroup.removeAllViews();
        selectRecipe.setEnabled(false);
        selectRecipe.setBackgroundColor(getResources().getColor(R.color.inactiveButton));
        deleteRecipe.setEnabled(false);
        deleteRecipe.setBackgroundColor(getResources().getColor(R.color.inactiveButton));
    }

}
