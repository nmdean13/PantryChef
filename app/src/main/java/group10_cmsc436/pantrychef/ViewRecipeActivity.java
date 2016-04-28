package group10_cmsc436.pantrychef;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class ViewRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_view);

        Intent intent = getIntent();
        String name = intent.getStringExtra("recipe_name");

    }

}
