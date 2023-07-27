package com.task.personal.product.model;

public class Product {
    private String productName;
    private String productCategory;
    private String productImage; // New field for product image

    public Product(String productName, String productCategory, String productImage) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductImage() {
        return productImage;
    }
}
