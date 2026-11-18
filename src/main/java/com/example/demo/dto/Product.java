package com.example.demo.dto;

import lombok.Data;


@Data
public class Product {
    private String productName;
    private String description;
    private int productId;
    private double price;
    private int stockQuantity;
    private int categoryId;
    private int quantity;

    public Product(int productId, int quantity){
        this.productId = productId;
        this.quantity = quantity;
    }

    public Product(int categoryid, String productName, String description, double price, int stockQuantity){
        this.categoryId = categoryid;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Product(String productName,int productid, String description, double price, int stockQuantity){
        this.productId = productid;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Product(){

    }


}
