package com.example.b.expensewatcher.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.b.expensewatcher.R;
import com.example.b.expensewatcher.models.CategoryPerExpense;

import java.util.List;

/**
 * Created by B on 19-May-17.
 */

public class TotalExpensePerCategoryAdapter extends RecyclerView.Adapter<TotalExpensePerCategoryAdapter.ViewHolder> {

    Context mContext;
    CategoryPerExpense[] mValues;
    int[] ColorCodes;
    protected Exp_Category_ItemListener expCategoryItemListener;
    int selected_position = RecyclerView.NO_POSITION;

    public TotalExpensePerCategoryAdapter(Context context, CategoryPerExpense[] cat_expenses, int[] colorcodes, Exp_Category_ItemListener listener) {

        mContext = context;
        mValues = cat_expenses;
        ColorCodes = colorcodes;
        expCategoryItemListener = listener;

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, total;
        public ImageView image;
        public CategoryPerExpense item;

        public ViewHolder(View v){

            super(v);
            v.setOnClickListener(this);

            image = (ImageView) v.findViewById(R.id.total_expenses_per_category_image);
            title = (TextView) v.findViewById(R.id.total_expenses_per_category_name);
            total = (TextView) v.findViewById(R.id.total_expenses_per_category_total);
        }

        public void setData(CategoryPerExpense item, int colorCode){

            this.item = item;


            int id = mContext.getResources().getIdentifier("drawable/"+item.category_title.toLowerCase(),null,mContext.getPackageName());
            image.setImageResource(id);
            image.setBackgroundColor(colorCode);
            title.setText(item.category_title);
            total.setText(String.valueOf(item.total_expenses_per_category));
        }

        @Override
        public void onClick(View view) {
            if (expCategoryItemListener != null) {
                expCategoryItemListener.onItemClick(item);
            }

            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);
        }
    }

    @Override
    public TotalExpensePerCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_totalcategoryexpenses, parent, false);
        TotalExpensePerCategoryAdapter.ViewHolder cvh = new TotalExpensePerCategoryAdapter.ViewHolder(view);
        return cvh;
    }

     @Override
    public void onBindViewHolder(TotalExpensePerCategoryAdapter.ViewHolder holder, int position) {
        int colorpos = position < ColorCodes.length ? position : position % ColorCodes.length;
        Log.d("POS",String.valueOf(position));
         Log.d("COLORPOS",String.valueOf(colorpos));
        holder.setData(mValues[position], ColorCodes[colorpos]);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mValues.length;
    }

    public interface Exp_Category_ItemListener {
        void onItemClick(CategoryPerExpense item);
    }
}
