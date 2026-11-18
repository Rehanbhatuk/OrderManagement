package com.example.demo.validator;

import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.FieldError;
import com.example.demo.exceptions.RestParameterInvalidException;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Component
public class PaymentValidator {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PaymentService paymentService;



    public void validatePayment(PaymentDto payment) {

        List<FieldError> errors = new ArrayList<>();

        int orderId = payment.getOrderid();

        if (orderId <= 0) {

            FieldError fieldError = new FieldError();
            fieldError.setKey("orderId");
            fieldError.setErrorMessage(messageSource.getMessage("payment.invalid.orderId", null, Locale.getDefault()));

            errors.add(fieldError);
        }

        double amount = payment.getAmount();

        if (amount <= 0) {

            FieldError fieldError = new FieldError();
            fieldError.setKey("amount");
            fieldError.setErrorMessage(messageSource.getMessage("payment.invalid.amount", null, Locale.getDefault()));

            errors.add(fieldError);
        }

        String paymentMethod = payment.getPaymentMethod();

        if (StringUtils.isBlank(paymentMethod)) {

            FieldError fieldError = new FieldError();
            fieldError.setKey("paymentMethod");
            fieldError.setErrorMessage(messageSource.getMessage("payment.invalid.paymentMethod", null, Locale.getDefault()));

            errors.add(fieldError);
        }

        int productId = payment.getProductId();

        if (productId <= 0) {

            FieldError fieldError = new FieldError();
            fieldError.setKey("productId");
            fieldError.setErrorMessage(messageSource.getMessage("payment.invalid.productId", null, Locale.getDefault()));

            errors.add(fieldError);
        }

        if (!errors.isEmpty()) {

            throw new RestParameterInvalidException(errors);

        }
    }

    public void databaseValidator(PaymentDto payment) {

        List<FieldError> errors = new ArrayList<>();

        int orderId = payment.getOrderid();

        if (!paymentService.isOrderExists(orderId)) {

            FieldError fieldError = new FieldError();
            fieldError.setKey("orderId");
            fieldError.setErrorMessage(messageSource.getMessage("payment.order.doesnotexist", null, Locale.getDefault()));

            errors.add(fieldError);
        }


        int productId = payment.getProductId();

        if (!paymentService.isProductExists(productId)) {

            FieldError fieldError = new FieldError();
            fieldError.setKey("productId");
            fieldError.setErrorMessage(messageSource.getMessage("payment.product.doesnotexist", null, Locale.getDefault()));

            errors.add(fieldError);
        }


        if (!errors.isEmpty()) {
            throw new RestParameterInvalidException(errors);
        }
    }
}