package com.example.demo.dto;

import lombok.Data;

@Data
public class Category {
    int categoryId;
    String categoryName;
    String description;
    String activeStatus;

    public Category(String categoryName, String description, String activeStatus){
        this.categoryName =  categoryName;
        this.description = description;
        this.activeStatus = activeStatus;
    }

    public Category(){

    }

}

