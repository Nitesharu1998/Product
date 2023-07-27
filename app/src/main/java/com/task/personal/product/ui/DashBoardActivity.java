package com.task.personal.product.ui;

import static android.view.View.GONE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.task.personal.R;
import com.task.personal.databinding.ActivityDashBoardBinding;
import com.task.personal.product.db.ProductDatabaseHelper;

public class DashBoardActivity extends AppCompatActivity {
    ActivityDashBoardBinding binding;
    private static final int REQUEST_IMAGE_PICKER = 100;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_dash_board);

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });

        binding.productlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, DataListActivity.class);
                startActivity(intent);
            }
        });


        binding.productcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductListDialog();
            }
        });

        binding.productimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            System.out.println("image path"+data.getData().getPath());
            imagePath = getRealPathFromUri(imageUri);
//            binding.productimg.setImage(imagePath);
            if (imagePath != null && !imagePath.isEmpty()) {
                binding.producttimg.setVisibility(View.VISIBLE);
                binding.cardview.setVisibility(View.VISIBLE);
                binding.productimg.setText("Replace Image");
                /*binding.productimg.setVisibility(GONE);*/
                Glide.with(this)
                        .load(imagePath).into(binding.producttimg);
            } else {
                binding.producttimg.setVisibility(GONE);
                binding.cardview.setVisibility(GONE);
                binding.productimg.setVisibility(GONE);

            }

        }
    }
    private void showProductListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Category");
        int checkedItem = -1;

        // Create the list of products
        final String[] productsArray = getResources().getStringArray(R.array.products_array);

        builder.setSingleChoiceItems(productsArray, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedProduct = productsArray[which];
                binding.productcat.setText(selectedProduct);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private String getRealPathFromUri(Uri uri) {
        String imagePath ="";
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {

            int id = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            imagePath=cursor.getString(id);

            cursor.close();
            return imagePath;
        }
    }


    private void saveProduct() {
        String productName = binding.editTextProduct.getText().toString();
        String productCategory = binding.productcat.getText().toString();

        // Check if any of the fields are empty
        if (TextUtils.isEmpty(productName) || TextUtils.isEmpty(productCategory) || TextUtils.isEmpty(imagePath)) {
            Toast.makeText(this, "Please fill all the data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert the product into the database
        insertProduct(productName, productCategory, imagePath);

        // Clear the fields after saving
        binding.editTextProduct.setText("");
        binding.productcat.setText("");
        imagePath = ""; // Clear the imagePath after saving
        binding.cardview.setVisibility(View.GONE);
        binding.productimg.setText("");
    }
    private void insertProduct(String productName, String productCategory, String imagePath) {
        // Get a writable instance of the database
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Create a ContentValues object to store the product data
        ContentValues values = new ContentValues();
        values.put(ProductDatabaseHelper.COLUMN_PRODUCT_NAME, productName);
        values.put(ProductDatabaseHelper.COLUMN_PRODUCT_CATEGORY, productCategory);
        values.put(ProductDatabaseHelper.COLUMN_PRODUCT_IMAGE, imagePath);

        // Insert the data into the database
        long newRowId = database.insert(ProductDatabaseHelper.TABLE_PRODUCTS, null, values);

        if (newRowId != -1) {
            // Data insertion successful
            Toast.makeText(this, "Product saved!", Toast.LENGTH_SHORT).show();
        } else {
            // Data insertion failed
            Toast.makeText(this, "Failed to save product!", Toast.LENGTH_SHORT).show();
        }

        // Close the database
        database.close();
    }


}