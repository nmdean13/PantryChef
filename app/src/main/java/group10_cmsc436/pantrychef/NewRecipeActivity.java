package group10_cmsc436.pantrychef;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.CheckBox;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

/**
 * Created by Pramath on 4/11/2016.
 */
public class NewRecipeActivity extends AppCompatActivity {

    // Buttons, text boxes, etc, for UI
    private Button addIngredient;
    private Button findRecipe;
    private EditText ingredient;
    private LinearLayout linLayout;

    // List of ingredients
    private ArrayList<CheckBox> buttonArray;
    private ArrayList<String> nameArray;
    private ArrayList<String> checkedIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        addIngredient = (Button) findViewById(R.id.add_ingredient_button);
        findRecipe = (Button) findViewById(R.id.find_recipe_button);
        ingredient = (EditText) findViewById(R.id.ingredient_text);
        linLayout = (LinearLayout) findViewById(R.id.ingredient_lin_layout);
        buttonArray = new ArrayList<CheckBox>();
        nameArray = new ArrayList<String>();
        checkedIngredients = new ArrayList<String>();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        int size = sharedPreferences.getInt("size", 0);

        for (int i = 0; i < size; i++) {
            String name = sharedPreferences.getString("ingredient_" + i, "");
            boolean checked = sharedPreferences.getBoolean("checked_" + i, false);

            restoreCheckBox(name, checked);
        }

        // Sets listener for "Add" button
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCheckBox(ingredient.getText().toString());
                ingredient.setText("");
            }
        });

        findRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeAsyncTask task = new RecipeAsyncTask();
                String url = task.generateIngredientQuery(checkedIngredients);
                task.execute(url);
                try {
                    task.get(5000, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    Log.d("ERROR:", e.getMessage());
                }

                ArrayList<String> recipeNames = task.getRecipeNamesFromJSON();
                ArrayList<String> recipeIDs = task.getRecipeIDsFromJSON();

                if(recipeNames.size() == 0) {
                    Toast.makeText(NewRecipeActivity.this, "No recipes to show", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(NewRecipeActivity.this, RecipeResultsActivity.class);
                    intent.putStringArrayListExtra("name_list", recipeNames);
                    intent.putStringArrayListExtra("id_list", recipeIDs);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ingredient, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean check = false;

        if (item.getItemId() == R.id.action_check) {
            check = true;
            findRecipe.setEnabled(true);
            findRecipe.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            findRecipe.setEnabled(false);
            findRecipe.setBackgroundColor(getResources().getColor(R.color.inactiveButton));
        }

        for (CheckBox c : buttonArray) {
            c.setChecked(check);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("size", buttonArray.size());

        for (int i = 0; i < buttonArray.size(); i++) {
            String name = buttonArray.get(i).getText().toString();

            editor.putString("ingredient_" + i, name);

            if (buttonArray.get(i).isChecked()) {
                editor.putBoolean("checked_" + i, true);
            } else {
                editor.putBoolean("checked_" + i, false);
            }
        }

        editor.commit();
        super.onBackPressed();
    }

    // Restores the check boxes via SharedPreferences when the activity is brought back up
    private void restoreCheckBox(String ingredientString, boolean checked) {
        CheckBox cb = new CheckBox(getApplicationContext());
        cb.setText(ingredientString);
        cb.setTextColor(Color.BLACK);
        cb.setTextSize(25.0f);
        cb.setChecked(checked);
        linLayout.addView(cb);
        buttonArray.add(cb);
        nameArray.add(ingredientString);

        if (checked) {
            checkedIngredients.add(ingredientString);
            findRecipe.setEnabled(true);
            findRecipe.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        OnClickIngredientListener listener = new OnClickIngredientListener(
                NewRecipeActivity.this, findRecipe, buttonArray, nameArray, checkedIngredients);

        cb.setOnClickListener(listener);
        cb.setOnLongClickListener(listener);
    }

    // Creates a check box for the ingredient and adds it to buttonArray.
    private void makeCheckBox(String ingredientString) {
        if (!ingredientString.isEmpty()) {
            if (nameArray.contains(ingredientString)) {
                Toast.makeText(getApplicationContext(),
                        "Ingredient Already Present", Toast.LENGTH_LONG).show();
            } else {
                nameArray.add(ingredientString);
                Collections.sort(nameArray);
                linLayout.removeAllViews();
                HashMap<String, Boolean> buttonMap = new HashMap<String, Boolean>();
                int size = buttonArray.size();

                for (int i = 0; i < size; i++) {
                    buttonMap.put(buttonArray.get(i).getText().toString(),
                            buttonArray.get(i).isChecked());
                }

                buttonMap.put(ingredientString, false);
                buttonArray.clear();

                for (int i = 0; i < nameArray.size(); i++) {
                    CheckBox cb = new CheckBox(getApplicationContext());
                    cb.setText(nameArray.get(i));
                    cb.setTextColor(Color.BLACK);
                    cb.setTextSize(25.0f);
                    cb.setChecked(buttonMap.get(nameArray.get(i)));
                    linLayout.addView(cb);
                    buttonArray.add(cb);

                    OnClickIngredientListener listener = new OnClickIngredientListener(
                            NewRecipeActivity.this, findRecipe, buttonArray, nameArray, checkedIngredients);

                    cb.setOnClickListener(listener);
                    cb.setOnLongClickListener(listener);
                }
            }
        }
    }
}
