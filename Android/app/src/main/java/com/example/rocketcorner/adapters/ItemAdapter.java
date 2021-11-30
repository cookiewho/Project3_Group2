package com.example.rocketcorner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rocketcorner.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    Context context;
    // TODO Create list of Items class & add to constructor

    private String[] localDataSet; // this will be replaced by list of Items
    public List<String> items;
    public List<String> images;
    private OnItemListener OnItemListener;
    public ItemAdapter(Context context, List<String> items, List<String> images, OnItemListener itemListener){
        this.context = context;
        this.items = items;
        this.images = images;
        this.OnItemListener = itemListener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, viewGroup, false);
        return new ViewHolder(view, OnItemListener);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        String item = items.get(position);
        String image = images.get(position);
        viewHolder.bind(item, image);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView itemName;
        ImageView itemImage;
        RelativeLayout container;
        OnItemListener itemListener;

        public ViewHolder(@NonNull View view, OnItemListener itemListener) {
            super(view);
            // Define click listener for the ViewHolder's View
            container = itemView.findViewById(R.id.container);
            itemName = (TextView) view.findViewById(R.id.itemName);
            itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.itemListener = itemListener;

            view.setOnClickListener(this);
        }

        public void bind(String name, String image){
            itemName.setText(name);
            Log.d("=== GLIDE", image);
            Glide.with(context).load(image).into(itemImage);
        }

        public TextView getTextView() {
            return itemName;
        }

        @Override
        public void onClick(View view) {
            itemListener.onItemClick(getBindingAdapterPosition());
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }

}


