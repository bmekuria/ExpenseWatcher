package com.example.b.expensewatcher.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.b.expensewatcher.R;
import com.example.b.expensewatcher.models.Expense;

public class MonthlyExpensesAdapter extends RecyclerView.Adapter<MonthlyExpensesAdapter.ViewHolder> {

    Context mContext;
    Expense[] mValues;
    protected MonthlyExpensesAdapter.Monthly_Exp_ItemListener Monthly_Exp_ItemListenerItemListener;
    int selected_position = RecyclerView.NO_POSITION;

    public MonthlyExpensesAdapter(Context context, Expense[] expenses, Monthly_Exp_ItemListener itemListener) {

        mContext = context;
        mValues = expenses;
        Monthly_Exp_ItemListenerItemListener = itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, date;
        public Expense item;

        public ViewHolder(View v){

            super(v);
            v.setOnClickListener(this);

            title = (TextView) v.findViewById(R.id.monthlyexpenseAmount);
            date = (TextView) v.findViewById(R.id.monthlyexpensedate);
        }

        public void setData(Expense item){

            this.item = item;

            title.setText(String.valueOf(item.amount));
            date.setText(String.valueOf(new DateFormatting().formatDatetoString("notmodel",item.timestamp)));
        }

        @Override
        public void onClick(View view) {
            if (Monthly_Exp_ItemListenerItemListener != null) {
                Monthly_Exp_ItemListenerItemListener.onItemClick(item);
            }

            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);
        }
    }

    @Override
    public MonthlyExpensesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monthlyexpenses, parent, false);
        MonthlyExpensesAdapter.ViewHolder cvh = new MonthlyExpensesAdapter.ViewHolder(view);
        return cvh;
    }

    @Override
    public void onBindViewHolder(MonthlyExpensesAdapter.ViewHolder holder, int position) {
        holder.setData(mValues[position]);
    }

    @Override
    public int getItemCount() {
        return mValues.length;
    }

    public interface Monthly_Exp_ItemListener {
        void onItemClick(Expense item);
    }
}
