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

import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Custom adapter for the listview of user
 * Adapter design pattern.
 *
 * @author herje
 * @version 1
 */
public class UserListViewAdapter extends ArrayAdapter<User> implements Filterable {

    private final Context context;
    private final ItemFilter itemFilter = new ItemFilter();
    private List<User> userList = new ArrayList<>();
    private List<User> userListCopy = new ArrayList<>();
    private Integer sortOrder = 0;
    private final ViewHolder viewHolder = new ViewHolder();

    public UserListViewAdapter(@NonNull Context context, List<User> userList, Integer sortOrder) {
        super(context, R.layout.users_listview);
        this.context = context;
        this.userList = userList;
        this.userListCopy = userList;
        this.sortOrder = sortOrder;
    }

    /**
     * Get the count of the current list.
     *
     * @return size of the list.
     */
    @Override
    public int getCount() {
        return userListCopy.size();
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
            convertView = mInflater.inflate(R.layout.users_listview, parent, false);


            mViewHolder.imageView = (ImageView) convertView.findViewById(R.id.userLogo);
            mViewHolder.name = (TextView) convertView.findViewById(R.id.userView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if (sortOrder == 1) {
            Collections.sort(userListCopy, Comparator.comparing(o -> o.getUserDetails().getFirstName().toLowerCase()));
        } else if (sortOrder == 2) {
            Collections.sort(userListCopy, (o1, o2) -> o2.getUserDetails().getFirstName().toLowerCase()
                    .compareTo(o1.getUserDetails().getFirstName().toLowerCase()));
        }

        User user = userListCopy.get(position);
        mViewHolder.name.setText(user.getUserDetails().getFirstName() + " " + user.getUserDetails().getLastName());
        if(user.getUserType().equals("C")){
        Picasso.get().load(user.getImageUrl())
                .into(mViewHolder.imageView);

        }else{
            //Royalty free image - default admin picture
           Picasso.get().load("https://thumbs.dreamstime.com/z/admin-computer-keys-9565077.jpg")
              .into(mViewHolder.imageView);
        }

        return convertView;
    }

    /**
     * get the view in a dropdown position.
     */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    /**
     * Returns the filtered list
     * @return itemFilter - filters the list by the searchbar.
     */
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
    public User getItem(int position) {
        return userListCopy.get(position);
    }

    /**
     * Holder of the items in the row.
     */
    private static class ViewHolder {
        private ImageView imageView;
        private TextView name;
    }

    /**
     * This is called to filter the list based on the search bar.
     */
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<User> list = userList;

            int count = list.size();
            final ArrayList<User> newList = new ArrayList<>(count);

            User filterableUser;

            for (int i = 0; i < count; i++) {
                filterableUser = list.get(i);
                if (filterableUser.getUserDetails().getFirstName().toLowerCase().contains(filterString.toLowerCase())) {
                    newList.add(filterableUser);
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
            userListCopy = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }

    }
}
