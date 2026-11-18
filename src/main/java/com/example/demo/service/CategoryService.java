package com.example.demo.service;

import com.example.demo.dto.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addCategory(Category categories){
        String sql = "Insert into categories(category_name, description,status) values (?,?,?)";
        jdbcTemplate.update(sql, categories.getCategoryName(),categories.getDescription(),categories.getActiveStatus());
    }

    public boolean isCategoryExists(int categoryId) {

        String sql = "SELECT category_id FROM categories WHERE category_id = ?";

        List<Integer> result = jdbcTemplate.queryForList(sql, Integer.class, categoryId);

        return !result.isEmpty();
    }
}
