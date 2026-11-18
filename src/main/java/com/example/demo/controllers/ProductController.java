package com.example.demo.controllers;

import com.example.demo.dto.Product;
import com.example.demo.service.ProductService;

import com.example.demo.validator.ProductValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductValidator productValidator;


    @PostMapping("/add")
    public void addProduct(@Valid @RequestBody Product product) {

        logger.trace("Entering addProduct()");

        productValidator.validateProduct(product);
        productValidator.databaseValidator(product);

        logger.debug("Adding product: {}", product.getProductName());

        productService.addProduct(product);

        logger.info("Product added successfully: {}", product.getProductName());
    }


    @PostMapping("/update/stock")
    public void updateStock(@RequestParam int productId, @RequestParam int newStock) {

        logger.trace("Entering updateStock()");

        logger.debug("Updating stock for product ID: {}", productId);

        if (newStock < 0) {
            logger.warn("Invalid stock quantity: {}", newStock);
        }

        productService.updateStock(productId, newStock);

        logger.info("Stock updated successfully for product ID: {}", productId);
    }


    @GetMapping("/by-name/{productName}")
    public Product getProductByName(@PathVariable String productName) {

        logger.trace("Entering getProductByName()");

        logger.debug("Searching product by name: {}", productName);

        Product product = productService.getProductByName(productName);

        if (product == null) {
            logger.warn("Product not found with name: {}", productName);
            return null;
        }

        logger.info("Product fetched successfully: {}", productName);

        return product;
    }


    @GetMapping("/by-id/{productId}")
    public Product getProductById(@PathVariable int productId) {

        logger.trace("Entering getProductById()");

        logger.debug("Searching product with ID: {}", productId);

        Product product = productService.getProductById(productId);

        if (product == null) {
            logger.warn("Product not found with ID: {}", productId);
            return null;
        }

        logger.info("Product fetched successfully with ID: {}", productId);

        return product;
    }


    @GetMapping("/by-category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable int categoryId) {

        logger.trace("Entering getProductsByCategory()");

        logger.debug("Fetching products for category ID: {}", categoryId);

        List<Product> productList = productService.getSameCategoryProducts(categoryId);

        if (productList.isEmpty()) {
            logger.warn("No products found for category ID: {}", categoryId);
        } else {
            logger.info("Products fetched successfully. Total: {}", productList.size());
        }

        return productList;
    }


    @GetMapping("/between-price")
    public List<Product> getProductsBetweenPrice(@RequestParam double fromPrice, @RequestParam double toPrice) {

        logger.trace("Entering getProductsBetweenPrice()");

        logger.debug("Searching products between price {} and {}", fromPrice, toPrice);

        if (fromPrice > toPrice) {
            logger.warn("Invalid price range: fromPrice is greater than toPrice");
        }

        List<Product> productList = productService.getProductsBetweenPrice(fromPrice, toPrice);

        if (productList.isEmpty()) {
            logger.warn("No products found in the given price range");
        } else {
            logger.info("Products fetched successfully. Total: {}", productList.size());
        }

        return productList;
    }


    @GetMapping("/price-by-id/{productId}")
    public double getProductPriceByProductId(@PathVariable int productId) {

        logger.trace("Entering getProductPriceByProductId()");

        logger.debug("Fetching price for product ID: {}", productId);

        double price = productService.getProductPriceById(productId);

        logger.info("Product price fetched successfully for ID: {}", productId);

        return price;
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {

        logger.error("An unexpected error occurred in ProductController", e);

        return ResponseEntity.status(500).body("Internal Server Error");
    }

}