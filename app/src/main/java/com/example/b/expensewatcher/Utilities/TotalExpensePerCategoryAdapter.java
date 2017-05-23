package com.example.b.expensewatcher.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.b.expensewatcher.R;

/**
 * Created by B on 19-May-17.
 */

public class TotalExpensePerCategoryAdapter extends ArrayAdapter<String[]> {
    public TotalExpensePerCategoryAdapter(Context context, String[][] cat_expenses) {
        super(context, R.layout.item_totalcategoryexpenses, cat_expenses);
    }

    public static class ViewHolder {
        TextView title, total;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //Get the data item for this position
        String[] cat_exp = getItem(position);
        TotalExpensePerCategoryAdapter.ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new TotalExpensePerCategoryAdapter.ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_totalcategoryexpenses,parent,false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.total_expenses_per_category_name);
            viewHolder.total = (TextView) convertView.findViewById(R.id.total_expenses_per_category_total);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (TotalExpensePerCategoryAdapter.ViewHolder) convertView.getTag();
        }

        //StringBuilder exp_info = new StringBuilder();
        //exp_info.append(exp.amount).append(" ").append(exp.category);
        if(cat_exp != null) {
            viewHolder.title.setText(new StringBuilder().append(cat_exp[0]));
            viewHolder.title.setTextSize(22);
            viewHolder.total.setText(new StringBuilder().append(cat_exp[1]));
            viewHolder.total.setTextSize(22);
        }
        return convertView;
    }
}
