package com.foodies.mealplanner.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
import com.foodies.mealplanner.model.MealDetailSpinner;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class SpinnerAdapter extends ArrayAdapter<Meal> implements Filterable {

    private Context ctx;
    private List<String> contentList;
    private List<String> imageList;

    private List<MealDetailSpinner> mdsList = new ArrayList<>();
    private List<MealDetailSpinner> itemsCopy = new ArrayList<>();
    private List<MealDetailSpinner> items = new ArrayList<>();
    private ItemFilter mFilter = new ItemFilter();
    private List<Meal> mealList = new ArrayList<>();
    private List<Meal> mealListCopy = new ArrayList<>();

    private Integer filter = 0;

    private ViewHolder mViewHolder = new ViewHolder();

    public SpinnerAdapter(@NonNull Context context, List<String> contentList,
                          List<String> imageList, List<MealDetailSpinner> mds, List<Meal> mealList, Integer filter) {
        super(context, R.layout.meal_listview);
        this.ctx = context;
        this.contentList = contentList;
        this.imageList = imageList;
        this.mdsList = mds;
        this.itemsCopy = mds;

        this.mealListCopy = mealList;
        this.mealList = mealList;

        this.filter = filter;
    }

    @Override
    public int getCount() {
        return mealListCopy.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) ctx.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.meal_listview, parent, false);


            mViewHolder.mImage = (ImageView) convertView.findViewById(R.id.mealLogo);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.mealView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if (filter == 1){
            Collections.sort(mealListCopy, Comparator.comparing(o -> o.getMealName().toLowerCase()));
        }else if(filter == 2){
            Collections.sort(mealListCopy, (o1, o2) -> o2.getMealName().toLowerCase()
                                .compareTo(o1.getMealName().toLowerCase()));
        }

        Meal mdsss = mealListCopy.get(position);
        mViewHolder.mName.setText(mdsss.getMealName());
        Picasso.get().load(mdsss.getImageUrl())
                .into(mViewHolder.mImage);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    private static class ViewHolder {
        ImageView mImage;
        TextView mName;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Meal> list = mealList;

            int count = list.size();
            final ArrayList<Meal> nlist = new ArrayList<>(count);

            Meal filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.getMealName().toLowerCase().contains(filterString.toLowerCase())) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mealListCopy = (ArrayList<Meal>) results.values;
            notifyDataSetChanged();
        }

    }

    /**
     * Overriding the getItem function of adapter to be able to return the correct filtered or sorted list.
     * @param position - position of the object being clicked.
     * @return object that was clicked.
     */
    @Override
    public Meal getItem(int position) {
        return mealListCopy.get(position);
    }
}
