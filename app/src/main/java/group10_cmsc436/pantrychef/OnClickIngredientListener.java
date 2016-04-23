package group10_cmsc436.pantrychef;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * Created by Pramath on 4/21/2016.
 */
public class OnClickIngredientListener implements View.OnClickListener, View.OnLongClickListener {

    Activity activity;
    Button findRecipe;
    ArrayList<CheckBox> buttonArray;
    ArrayList<String> nameArray;

    public OnClickIngredientListener(Activity activity, Button findRecipe,
                                     ArrayList<CheckBox> buttonArray,
                                     ArrayList<String> nameArray) {
        this.activity = activity;
        this.findRecipe = findRecipe;
        this.buttonArray = buttonArray;
        this.nameArray = nameArray;
    }

    @Override
    public void onClick(View v) {
        // Enable button and change color to red if a check box is checked.
        if (((CheckBox) v).isChecked() && !findRecipe.isEnabled()) {
            findRecipe.setEnabled(true);
            findRecipe.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        } else {
            // Check if any check boxes are checked, and disable the
            // "Find Recipe" button if none are.
            if (!checkedBoxes()) {
                disableButton();
            }
        }
    }

    @Override
    public boolean onLongClick(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Delete " + ((CheckBox) v).getText().toString() + "?");

        // Delete ingredient if "Yes" button is pressed.
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                buttonArray.remove((CheckBox) v);
                nameArray.remove(((CheckBox) v).getText().toString());
                ViewGroup parentView = (ViewGroup) v.getParent();
                parentView.removeView(v);

                // Disable the "Find Recipe" button if there are no checked
                // check boxes and the button is currently enabled.
                if (!checkedBoxes() && findRecipe.isEnabled()) {
                    disableButton();
                }
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

    /**
     * Goes through the array of check boxes and checks to see if any are checked.
     * @return true if a check box is checked, false otherwise
     */
    private boolean checkedBoxes() {
        for (CheckBox c : buttonArray) {
            if (c.isChecked()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Disables the "Find Recipe" button. Called when either no check boxes are checked or when all
     * checked boxes have been deleted.
     */
    private void disableButton() {
        findRecipe.setEnabled(false);
        findRecipe.setBackgroundColor(activity.getResources().getColor(R.color.inactiveButton));
    }
}
