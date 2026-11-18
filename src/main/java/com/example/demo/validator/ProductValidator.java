package com.example.demo.validator;

import com.example.demo.dto.FieldError;
import com.example.demo.dto.Product;
import com.example.demo.exceptions.RestParameterInvalidException;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class ProductValidator {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProductService productService;



    @Autowired
    CategoryService categoryService;

    public void validateProduct(Product product) {

        List<FieldError> errors = new ArrayList<>();

        String productName = product.getProductName();
        if (StringUtils.isBlank(productName) || !StringUtils.isAlphanumeric(productName)) {
            FieldError fieldError = new FieldError();
            fieldError.setKey("ProductName");
            fieldError.setErrorMessage(messageSource.getMessage("product.invalid.productName", null, Locale.getDefault()));
            errors.add(fieldError);
        }

        int categoryId = product.getCategoryId();
        if (categoryId <= 0) {
            FieldError fieldError = new FieldError();
            fieldError.setKey("CategoryId");
            fieldError.setErrorMessage(messageSource.getMessage("product.invalid.categoryId", null, Locale.getDefault()));
            errors.add(fieldError);
        }

        double price = product.getPrice();
        if (price <= 0) {
            FieldError fieldError = new FieldError();
            fieldError.setKey("Price");
            fieldError.setErrorMessage(messageSource.getMessage("product.invalid.price", null, Locale.getDefault()));
            errors.add(fieldError);
        }

        int stock = product.getStockQuantity();
        if (stock <= 0) {
            FieldError fieldError = new FieldError();
            fieldError.setKey("StockQuantity");
            fieldError.setErrorMessage(messageSource.getMessage("product.invalid.stockQuantity", null, Locale.getDefault()));
            errors.add(fieldError);
        }


        if (!errors.isEmpty()) {
            throw new RestParameterInvalidException(errors);
        }
    }

    public void databaseValidator(Product product) {

        List<FieldError> errors = new ArrayList<>();

        String productName = product.getProductName();

        if (StringUtils.isNotBlank(productName)) {

            if (productService.isProductNameExists(productName)) {

                FieldError fieldError = new FieldError();
                fieldError.setKey("productName");
                fieldError.setErrorMessage(messageSource.getMessage("product.duplicate.productName", null, Locale.getDefault()));

                errors.add(fieldError);
            }
        }


        int categoryId = product.getCategoryId();

        if (!categoryService.isCategoryExists(categoryId)) {

            FieldError fieldError = new FieldError();
            fieldError.setKey("categoryId");
            fieldError.setErrorMessage(messageSource.getMessage("product.invalid.categoryId", null, Locale.getDefault()));

            errors.add(fieldError);
        }

        if (!errors.isEmpty()) {
            throw new RestParameterInvalidException(errors);
        }
    }


}
