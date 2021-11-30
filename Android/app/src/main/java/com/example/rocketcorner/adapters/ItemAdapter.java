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
    public ItemAdapter(Context context, List<String> items, List<String> images){
        this.context = context;
        this.items = items;
        this.images = images;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, viewGroup, false);
        return new ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        String item = items.get(position);
        String image = images.get(position);
        viewHolder.bind(item, image);
//        viewHolder.getTextView().setText(items.get(position));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        ImageView itemImage;
        RelativeLayout container;

        public ViewHolder(@NonNull View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            container = itemView.findViewById(R.id.container);
            itemName = (TextView) view.findViewById(R.id.itemName);
            itemImage = (ImageView) view.findViewById(R.id.itemImage);
        }

        public void bind(String name, String image){
            itemName.setText(name);
            Log.d("=== GLIDE", image);
            Glide.with(context).load(image).into(itemImage);
        }

        public TextView getTextView() {
            return itemName;
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

}


