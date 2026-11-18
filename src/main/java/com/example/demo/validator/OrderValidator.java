package com.example.demo.validator;

import com.example.demo.dto.OrderDto;
import com.example.demo.dto.FieldError;
import com.example.demo.dto.Product;
import com.example.demo.exceptions.RestParameterInvalidException;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class OrderValidator {

    @Autowired
    private OrderService orderService;


    @Autowired
    private MessageSource messageSource;

    public void validateOrder(OrderDto order) {

        List<FieldError> errors = new ArrayList<>();

        int customerId = order.getCustomerId();

        if (customerId <= 0) {

            FieldError fieldError = new FieldError();
            fieldError.setKey("customerId");
            fieldError.setErrorMessage(messageSource.getMessage("order.invalid.customerId", null, Locale.getDefault()));

            errors.add(fieldError);
        }

        double totalAmount = order.getTotalAmount();

        if (totalAmount < 0) {

            FieldError fieldError = new FieldError();
            fieldError.setKey("totalAmount");
            fieldError.setErrorMessage(messageSource.getMessage("order.invalid.totalAmount", null, Locale.getDefault()));

            errors.add(fieldError);
        }

        List<?> productsList = order.getProductsList();

        if (productsList == null || productsList.isEmpty()) {

            FieldError fieldError = new FieldError();
            fieldError.setKey("productsList");
            fieldError.setErrorMessage(messageSource.getMessage("order.invalid.productsList", null, Locale.getDefault()));

            errors.add(fieldError);
        }

        if (!errors.isEmpty()) {
            throw new RestParameterInvalidException(errors);
        }
    }

    public void databaseValidator(OrderDto order) {

        List<FieldError> errors = new ArrayList<>();

        int customerId = order.getCustomerId();

        if (!orderService.isCustomerExists(customerId)) {

            FieldError fieldError = new FieldError();
            fieldError.setKey("customerId");
            fieldError.setErrorMessage(messageSource.getMessage("order.customer.doesnotexist",null,Locale.getDefault()));

            errors.add(fieldError);
        }


        for (Product product : order.getProductsList()) {

            int productId = product.getProductId();

            if (!orderService.isProductExists(productId)) {

                FieldError fieldError = new FieldError();
                fieldError.setKey("productId");
                fieldError.setErrorMessage(messageSource.getMessage("order.product.doesnotexist",null,Locale.getDefault()));

                errors.add(fieldError);
            }
        }


        if (!errors.isEmpty()) {
            throw new RestParameterInvalidException(errors);
        }
    }
}