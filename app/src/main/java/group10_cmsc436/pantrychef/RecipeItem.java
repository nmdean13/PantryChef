package group10_cmsc436.pantrychef;

import android.content.Intent;

public class RecipeItem {

    public static final String ITEM_SEP = System.getProperty("line.separator");

    public final static String NAME = "Name";

    private String mName;

    RecipeItem(String name) {
        this.mName = name;
    }

    RecipeItem(Intent intent) {
        mName = intent.getStringExtra(RecipeItem.NAME);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public static void packageIntent(Intent intent, String name) {
        intent.putExtra(RecipeItem.NAME, name);
    }

    public String toString() {
        return mName;
    }


}
