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
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get the data item for this position
        Expense exp = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_expense,parent,false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.expenseName);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //StringBuilder exp_info = new StringBuilder();
        //exp_info.append(exp.amount).append(" ").append(exp.category);
        if(exp != null) {
            viewHolder.title.setText(new StringBuilder().append(String.format("%.2f",exp.amount)).append(" ").append(exp.category));
            viewHolder.title.setTextSize(22);
        }
        return convertView;
    }
}
