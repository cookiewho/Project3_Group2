package com.example.rocketcorner.adapters;

import android.content.Context;
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
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    public ArrayList<Map.Entry<Product, Integer>> products;

    public CartAdapter(Context context, ArrayList<Map.Entry<Product, Integer>> products){
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Map.Entry<Product, Integer> entry = products.get(position);
        Product p = entry.getKey();
        int amount = entry.getValue();
        String item = p.getName();
        String image = p.getImgLink();
        double price = p.getPrice();

        viewHolder.bind(item, image, price, amount);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        TextView itemPrice;
        TextView itemAmount;
        ImageView itemImage;
        RelativeLayout container;

        public ViewHolder(@NonNull View view){
            super(view);
            container = itemView.findViewById(R.id.container);
            itemName = (TextView) view.findViewById(R.id.itemName);
            itemImage = (ImageView) view.findViewById(R.id.itemImage);
            itemPrice = (TextView) view.findViewById(R.id.itemPrice);
            itemAmount = (TextView) view.findViewById(R.id.itemAmount);
        }

        public void bind(String name, String image, double price, int amount){
            itemName.setText(name);
            itemPrice.setText(Double.toString(price));
            itemAmount.setText("Amount: " + Integer.toString(amount));
            Glide.with(context).load(image).into(itemImage);
        }

        public TextView getTextView() {
            return itemName;
        }

    }
}
