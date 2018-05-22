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
 * Created by B on 08-Apr-17.
 */

public class DashboardAdapter extends ArrayAdapter<Expense> {
    public DashboardAdapter(Context context, Expense[] expenses) {
        super(context, R.layout.item_expense, expenses);
    }

    private static class ViewHolder {
        TextView title;
        TextView category;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get the com.example.b.expensewatcher.data item for this position
        Expense exp = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_expense,parent,false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.expenseAmount);
            viewHolder.category = (TextView) convertView.findViewById(R.id.expenseCategory);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(exp != null) {
            viewHolder.title.setText(String.format("%.2f",exp.amount));
            viewHolder.title.setTextSize(22);
            viewHolder.category.setText(exp.category);
            viewHolder.category.setTextSize(22);
        }
        return convertView;
    }
}
