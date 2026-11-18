package com.example.demo.controllers;

import com.example.demo.dto.Category;
import com.example.demo.service.CategoryService;
import com.example.demo.validator.CategoryValidator;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryValidator categoryValidator;

    @PostMapping("/add/category")
    public void addCategory(@Valid @RequestBody Category category) {

        log.info("Received request to add a new category");

        log.debug("Validating category details");
        categoryValidator.categoryValidator(category);

        log.info("Category validation successful");
        log.debug("Calling CategoryService to save category");
        categoryService.addCategory(category);
        log.info("Category added successfully");

    }
}