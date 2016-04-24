package group10_cmsc436.pantrychef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends BaseAdapter {

    private final List<RecipeItem> mItems = new ArrayList<>();
    private final Context mContext;

    public RecipeListAdapter(Context context) {
        mContext = context;
    }

    public void add(RecipeItem item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int pos) {
        return mItems.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final RecipeItem recipeItem = mItems.get(position);

        RelativeLayout itemLayout = (RelativeLayout) convertView;

        if(convertView == null) {
            itemLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(
                    R.layout.recipe_item, parent, false);
        }

        final TextView nameView = (TextView) itemLayout.findViewById(R.id.nameView);
        nameView.setText(recipeItem.getName());

        return itemLayout;

    }
}