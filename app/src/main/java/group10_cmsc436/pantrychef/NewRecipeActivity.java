package group10_cmsc436.pantrychef;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.CheckBox;

import java.util.ArrayList;

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
    private ArrayList<CheckBox> buttonArray = new ArrayList<CheckBox>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        addIngredient = (Button) findViewById(R.id.add_ingredient_button);
        findRecipe = (Button) findViewById(R.id.find_recipe_button);
        ingredient = (EditText) findViewById(R.id.ingredient_text);
        linLayout = (LinearLayout) findViewById(R.id.ingredient_lin_layout);

        // Creates a check box for the ingredient and adds it to buttonArray.
        // TODO: Give check boxes a border so they can be seen against white background
        // TODO: Make the check boxes bigger so that they're proportionate to the text size
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredientString = ingredient.getText().toString();
                if (!ingredientString.isEmpty()) {
                    final CheckBox cb = new CheckBox(getApplicationContext());
                    cb.setText(ingredientString);
                    cb.setTextColor(Color.BLACK);
                    cb.setTextSize(25.0f);
                    ingredient.setText("");
                    linLayout.addView(cb);
                    buttonArray.add(cb);

                    // Sets a long click listener for each check box created. If the check box is
                    // long pressed, it will be deleted.
                    cb.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(final View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NewRecipeActivity.this);
                            builder.setMessage("Delete " + cb.getText().toString() + "?");

                            // Delete ingredient if "Yes" button is pressed.
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    buttonArray.remove(cb);
                                    ViewGroup parentView = (ViewGroup) v.getParent();
                                    parentView.removeView(v);
                                }
                            });

                            // Close dialog if "No" button is pressed.
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                            return true;
                        }
                    });
                }
            }
        });
    }
}
