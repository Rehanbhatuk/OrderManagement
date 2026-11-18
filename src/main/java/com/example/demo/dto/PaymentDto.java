package com.example.demo.dto;

import lombok.Data;


import java.util.Date;

@Data
public class PaymentDto {
    private int orderid;
    private  double amount;
    private String paymentMethod;
    private int paymentId;
    private Date paymentdate;
    private int productId;

}

