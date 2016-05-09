package group10_cmsc436.pantrychef;


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

import java.util.ArrayList;

public class RecipeResultsActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton[] radioButtons;
    Button selectRecipe;
    ArrayList<String> recipes;
    ArrayList<String> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_results);

        selectRecipe = (Button) findViewById(R.id.select_recipe_button);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        Intent intent = getIntent();
        recipes = intent.getStringArrayListExtra("name_list");
        ids = intent.getStringArrayListExtra("id_list");

        int numRecipes = recipes.size();

        radioButtons = new RadioButton[numRecipes];

        for (int i = 0; i < recipes.size(); i++) {
                radioButtons[i] = new RadioButton(this);
                radioButtons[i].setText(recipes.get(i));
                radioButtons[i].setId(i);
                radioGroup.addView(radioButtons[i]);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectRecipe.setEnabled(true);
                selectRecipe.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        selectRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected = (String) radioButtons[radioGroup.getCheckedRadioButtonId()].getText();
                Intent selectIntent = new Intent(RecipeResultsActivity.this, ViewRecipeActivity.class);
                selectIntent.putExtra("recipe_name", selected);
                selectIntent.putExtra("recipe_id", ids.get(recipes.indexOf(selected)));
                startActivity(selectIntent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();


    }



}
