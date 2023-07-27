package com.task.personal.product.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.task.personal.R;
import com.task.personal.databinding.ActivityDataListBinding;
import com.task.personal.product.adapter.ProductAdapter;
import com.task.personal.product.db.ProductDatabaseHelper;
import com.task.personal.product.model.Product;

import java.util.ArrayList;
import java.util.List;

public class DataListActivity extends AppCompatActivity {

    private ActivityDataListBinding binding;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_list); // Replace with your layout file name

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList,this);
        binding.recyclerView.setAdapter(productAdapter);

        showProductList();
        binding.totalcount.setText("Total Count- "+productList.size());
    }

    @SuppressLint("Range")
    private void showProductList() {
        // Get the data from the SQLite database
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(ProductDatabaseHelper.TABLE_PRODUCTS,
                null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            productList.clear();

            do {
                String productName = cursor.getString(cursor.getColumnIndex(ProductDatabaseHelper.COLUMN_PRODUCT_NAME));
                String productCategory = cursor.getString(cursor.getColumnIndex(ProductDatabaseHelper.COLUMN_PRODUCT_CATEGORY));
                String productImage = cursor.getString(cursor.getColumnIndex(ProductDatabaseHelper.COLUMN_PRODUCT_IMAGE));

                // Add the product to the list
                productList.add(new Product(productName, productCategory, productImage));

            } while (cursor.moveToNext());

            cursor.close();

            // Notify the adapter about the data change
            productAdapter.notifyDataSetChanged();
        }

        database.close();
    }
}