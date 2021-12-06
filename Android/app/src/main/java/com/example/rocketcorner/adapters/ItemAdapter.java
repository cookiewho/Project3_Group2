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
import com.example.rocketcorner.Product;
import com.example.rocketcorner.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    Context context;
    // TODO Create list of Items class & add to constructor

    private String[] localDataSet; // this will be replaced by list of Items

    public ArrayList<Map.Entry<String, Product>> products;
    private OnItemListener OnItemListener;
    public ItemAdapter(Context context, ArrayList<Map.Entry<String, Product>> products, OnItemListener itemListener){
        this.context = context;
        this.products = products;
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
        Map.Entry<String, Product> entry = products.get(position);
        Product p = entry.getValue();
        String item = p.getName();
        String image = p.getImgLink();
        double price = p.getPrice();

        if(item != null && image != null) {
            viewHolder.bind(item, image, price);
        }
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView itemName;
        TextView itemPrice;
        ImageView itemImage;
        RelativeLayout container;
        OnItemListener itemListener;

        public ViewHolder(@NonNull View view, OnItemListener itemListener) {
            super(view);
            // Define click listener for the ViewHolder's View
            container = itemView.findViewById(R.id.container);
            itemName = (TextView) view.findViewById(R.id.itemName);
            itemImage = (ImageView) view.findViewById(R.id.itemImage);
            itemPrice = (TextView) view.findViewById(R.id.itemPrice);
            this.itemListener = itemListener;

            view.setOnClickListener(this);
        }

        public void bind(String name, String image, double price){
            itemName.setText(name);
            itemPrice.setText(Double.toString(price));
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
        return products.size();
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }

}


