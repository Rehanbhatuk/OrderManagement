package com.example.demo.dto;

import lombok.Data;


import java.util.Date;
import java.util.List;

@Data
public class OrderDto {

    private int orderId;
    private int customerId;
    private Date orderDate;
    private double totalAmount;
    private String status;
    List<Product> productsList;


    public OrderDto() {
    }


    public OrderDto(int customerId, List<Product> productsList) {
        this.customerId = customerId;
        this.productsList = productsList;

    }
}
