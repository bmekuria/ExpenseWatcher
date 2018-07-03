package com.example.b.expensewatcher.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.b.expensewatcher.R;
import com.example.b.expensewatcher.models.Expense;

/**
 * Created by B on 01-Jun-17.
 */


public class ExpensesPerMonthlyCategoryAdapter extends RecyclerView.Adapter<ExpensesPerMonthlyCategoryAdapter.ViewHolder> {

    Context mContext;
    Expense[] mValues;
    protected Exp_Monthly_Category_ItemListener expMonthlyCategoryItemListener;
    int selected_position = RecyclerView.NO_POSITION;

    public ExpensesPerMonthlyCategoryAdapter(Context context, Expense[] expenses, Exp_Monthly_Category_ItemListener itemListener) {

        mContext = context;
        mValues = expenses;
        expMonthlyCategoryItemListener = itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, date;
        public Expense item;

        public ViewHolder(View v){

            super(v);
            v.setOnClickListener(this);

            title = (TextView) v.findViewById(R.id.expenseAmount);
            date = (TextView) v.findViewById(R.id.expensedate);
        }

        public void setData(Expense item){

            this.item = item;

            title.setText(String.valueOf(item.amount));
            date.setText(String.valueOf(item.monthyear));
        }

        @Override
        public void onClick(View view) {
            if (expMonthlyCategoryItemListener != null) {
                expMonthlyCategoryItemListener.onItemClick(item);
            }

            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expensespercategory, parent, false);
        ExpensesPerMonthlyCategoryAdapter.ViewHolder cvh = new ExpensesPerMonthlyCategoryAdapter.ViewHolder(view);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mValues[position]);
    }


    @Override
    public int getItemCount() {
        return mValues.length;
    }

    public interface Exp_Monthly_Category_ItemListener {
        void onItemClick(Expense item);
    }
}
