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
import com.foodies.mealplanner.model.Menu;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Custom adapter for the listview of menu.
 * Adapter design pattern.
 *
 * @author herje
 * @version 1
 */
public class MenuListViewAdapter extends ArrayAdapter<Menu> implements Filterable {

    private final Context context;
    private final ItemFilter itemFilter = new ItemFilter();
    private List<Menu> menuList = new ArrayList<>();
    private List<Menu> menuListCopy = new ArrayList<>();
    private Integer sortOrder = 0;
    private final ViewHolder viewHolder = new ViewHolder();

    public MenuListViewAdapter(@NonNull Context context, List<Menu> menuList, Integer sortOrder) {
        super(context, R.layout.menu_listview);
        this.context = context;
        this.menuListCopy = menuList;
        this.menuList = menuList;
        this.sortOrder = sortOrder;
    }

    /**
     * Get the count of the current list.
     *
     * @return size of the list.
     */
    @Override
    public int getCount() {
        return menuListCopy.size();
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
            convertView = mInflater.inflate(R.layout.menu_listview, parent, false);

            mViewHolder.imageView = (ImageView) convertView.findViewById(R.id.menuLogo);
            mViewHolder.menuName = (TextView) convertView.findViewById(R.id.menuView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if (sortOrder == 1) {
            Collections.sort(menuListCopy, Comparator.comparing(o -> o.getMenuName().toLowerCase()));
        } else if (sortOrder == 2) {
            Collections.sort(menuListCopy, (o1, o2) -> o2.getMenuName().toLowerCase()
                    .compareTo(o1.getMenuName().toLowerCase()));
        }

        Menu menu = menuListCopy.get(position);
        mViewHolder.menuName.setText(menu.getMenuName());
        Picasso.get().load(menu.getImageUrl())
                .into(mViewHolder.imageView);

        return convertView;
    }

    /**
     * Holder of the items in the row.
     */
    private static class ViewHolder {
        private ImageView imageView;
        private TextView menuName;
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
    public Menu getItem(int position) {
        return menuListCopy.get(position);
    }

    /**
     * This is called to filter the list based on the search bar.
     */
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Menu> list = menuList;

            int count = list.size();
            final ArrayList<Menu> newList = new ArrayList<>(count);

            Menu filterableMenu;

            for (int i = 0; i < count; i++) {
                filterableMenu = list.get(i);
                if (filterableMenu.getMenuName().toLowerCase().contains(filterString.toLowerCase())) {
                    newList.add(filterableMenu);
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
            menuListCopy = (ArrayList<Menu>) results.values;
            notifyDataSetChanged();
        }

    }
}
