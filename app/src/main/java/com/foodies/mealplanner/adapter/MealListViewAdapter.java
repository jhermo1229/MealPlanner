package com.foodies.mealplanner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.Meal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Custom adapter for the listview of meal.
 * Adapter design pattern
 *
 * @author herje
 * @version 1
 */
public class MealListViewAdapter extends ArrayAdapter<Meal> implements Filterable {

    private final Context context;
    private final ItemFilter itemFilter = new ItemFilter();
    private List<Meal> mealList = new ArrayList<>();
    private List<Meal> mealListCopy = new ArrayList<>();
    private Integer sortOrder = 0;
    private final ViewHolder viewHolder = new ViewHolder();

    public MealListViewAdapter(@NonNull Context context, List<Meal> mealList, Integer sortOrder) {
        super(context, R.layout.meal_listview);
        this.context = context;
        this.mealListCopy = mealList;
        this.mealList = mealList;
        this.sortOrder = sortOrder;
    }

    /**
     * Get the count of the current list.
     *
     * @return size of the list.
     */
    @Override
    public int getCount() {
        return mealListCopy.size();
    }

    /**
     * This is the view of the list.
     *
     * @param position    - current position of the row,
     * @param convertView - change view depending on the list.
     * @param parent      - super of View
     * @return - converted view.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.meal_listview, parent, false);


            mViewHolder.imageView = (ImageView) convertView.findViewById(R.id.mealLogo);
            mViewHolder.mealName = (TextView) convertView.findViewById(R.id.mealView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if (sortOrder == 1) {
            Collections.sort(mealListCopy, Comparator.comparing(o -> o.getMealName().toLowerCase()));
        } else if (sortOrder == 2) {
            Collections.sort(mealListCopy, (o1, o2) -> o2.getMealName().toLowerCase()
                    .compareTo(o1.getMealName().toLowerCase()));
        }

        Meal meal = mealListCopy.get(position);
        mViewHolder.mealName.setText(meal.getMealName());
        Picasso.get().load(meal.getImageUrl())
                .into(mViewHolder.imageView);

        return convertView;
    }

    /**
     * get the view in a dropdown position.
     */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    //returns the filtered list.
    public Filter getFilter() {
        return itemFilter;
    }

    /**
     * Overriding the getItem function of adapter to be able to return the correct filtered or sorted list.
     *
     * @param position - position of the object being clicked.
     * @return object that was clicked.
     */
    @Override
    public Meal getItem(int position) {
        return mealListCopy.get(position);
    }

    /**
     * Holder of the items in the row.
     */
    private static class ViewHolder {
        private ImageView imageView;
        private TextView mealName;
    }

    /**
     * This is called to filter the list based on the search bar.
     */
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Meal> list = mealList;

            int count = list.size();
            final ArrayList<Meal> newList = new ArrayList<>(count);

            Meal filterableMeal;

            for (int i = 0; i < count; i++) {
                filterableMeal = list.get(i);
                if (filterableMeal.getMealName().toLowerCase().contains(filterString.toLowerCase())) {
                    newList.add(filterableMeal);
                }
            }

            results.values = newList;
            results.count = newList.size();

            return results;
        }

        /**
         * Returns the new list based on the filters
         *
         * @param constraint - character being inputted in the search bar.
         * @param results    - new list being filtered.
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mealListCopy = (ArrayList<Meal>) results.values;
            notifyDataSetChanged();
        }

    }
}
