package com.example.b.expensewatcher.Utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.b.expensewatcher.R;
import com.example.b.expensewatcher.models.Expense;

/**
 * Created by B on 01-Jun-17.
 */


public class ExpensesPerCategoryAdapter extends ArrayAdapter<Expense> {
    public ExpensesPerCategoryAdapter(Context context, Expense[] expenses) {
        super(context, R.layout.item_expensespercategory, expenses);
    }

    private static class ViewHolder {
        TextView title;
        TextView date;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get the com.example.b.expensewatcher.data item for this position
        Expense exp = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_expensespercategory,parent,false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.expenseAmount);
            viewHolder.date = (TextView) convertView.findViewById(R.id.expensedate);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(exp != null) {
            viewHolder.title.setText(String.format("%.2f",exp.amount));

            DateFormatting dt = new DateFormatting();
            String[] datearray = dt.formatLongtoString((exp.timestamp)).split("/");
            viewHolder.date.setText(new StringBuilder().append(datearray[0]).
                    append("/").append(datearray[1]).append("/").append(datearray[2]));
        }
        return convertView;
    }
}
