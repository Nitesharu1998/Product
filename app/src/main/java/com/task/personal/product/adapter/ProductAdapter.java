package com.task.personal.product.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.task.personal.R;
import com.task.personal.product.model.Product;
import com.task.personal.product.ui.DataListActivity;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    public ProductAdapter(List<Product> productList, DataListActivity dataListActivity) {
        this.productList=productList;
        this.context=dataListActivity;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewProductName.setText(product.getProductName());
        holder.textViewProductCategory.setText(product.getProductCategory());
        Glide.with(context)
                .load(productList.get(position).getProductImage()).into(holder.img);
        Glide.with(context).load(product.getProductImage()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProductName;
        TextView textViewProductCategory;
        ImageView img;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductCategory = itemView.findViewById(R.id.textViewProductCategory);
            img = itemView.findViewById(R.id.imageViewProduct);
        }
    }
}