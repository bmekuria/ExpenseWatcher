package com.example.b.expensewatcher.Utilities;

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import com.example.b.expensewatcher.R;
        import com.example.b.expensewatcher.models.RecyclerViewCategory;

public class RecyclerViewCategoryAdapter extends RecyclerView.Adapter<RecyclerViewCategoryAdapter.ViewHolder> {

    RecyclerViewCategory[] mValues;
    Context mContext;
    protected ItemListener mListener;
    int selected_position = RecyclerView.NO_POSITION;

    public RecyclerViewCategoryAdapter(Context context, RecyclerViewCategory[] values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener=itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        public RecyclerViewCategory item;

        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(this);

            textView = (TextView) v.findViewById(R.id.recycleView_category_textView);
            imageView = (ImageView) v.findViewById(R.id.recycleView_category_imageView);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.recycleView_category_relativeLayout);

        }

        public void setData(RecyclerViewCategory item) {
            this.item = item;

            textView.setText(item.title);
            textView.setTextColor(mContext.getResources().getColor(R.color.pin_normal));
            int id = mContext.getResources().getIdentifier("drawable/"+item.image,null,mContext.getPackageName());
            imageView.setImageResource(id);
        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }

            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);
        }
    }

    @Override
    public RecyclerViewCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_category, parent, false);
        RecyclerViewCategoryAdapter.ViewHolder cvh = new RecyclerViewCategoryAdapter.ViewHolder(view);
        return cvh;
    }

    @Override
    public void onBindViewHolder(RecyclerViewCategoryAdapter.ViewHolder holder, int position) {
        holder.setData(mValues[position]);

        int color = mContext.getResources().getColor(R.color.backgroundGreen);
        //Highlight the chosen category
        if(selected_position == position) {
            holder.relativeLayout.setBackgroundColor(color);
        }
        else{
            holder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {

        return mValues.length;
    }

    public interface ItemListener {
        void onItemClick(RecyclerViewCategory item);
    }
}
