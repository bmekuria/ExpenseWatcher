package com.example.b.expensewatcher.Utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.b.expensewatcher.R;
import com.example.b.expensewatcher.models.Category;

/**
 * Created by B on 24-Mar-17.
 */

public class CategoryAdapter extends ArrayAdapter<Category>{
    public CategoryAdapter(Context context, Category[] categories) {
        super(context, R.layout.item_category, categories);
    }

    private static class ViewHolder {
        TextView title;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the com.example.b.expensewatcher.data item for this position
        Category cat = getItem(position);
        //Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; //view lookup cache stored in tag
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category,parent,false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.categoryName);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(cat.title);
        viewHolder.title.setTextSize(18);
        return convertView;
    }
}
