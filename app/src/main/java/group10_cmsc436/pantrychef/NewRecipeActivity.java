package group10_cmsc436.pantrychef;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.CheckBox;

/**
 * Created by Pramath on 4/11/2016.
 */
public class NewRecipeActivity extends AppCompatActivity {

    Button addIngredient;
    Button findRecipe;
    EditText ingredient;
    LinearLayout linLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        addIngredient = (Button) findViewById(R.id.add_ingredient_button);
        findRecipe = (Button) findViewById(R.id.find_recipe_button);
        ingredient = (EditText) findViewById(R.id.ingredient_text);
        linLayout = (LinearLayout) findViewById(R.id.ingredient_lin_layout);
//fsasdf
        addIngredient.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String ingredientString = ingredient.getText().toString();
                if (!ingredientString.isEmpty()) {
                    CheckBox cb = new CheckBox(getApplicationContext());
                    cb.setText(ingredientString);
                    linLayout.addView(cb);
                }
            }
        });
    }
}
