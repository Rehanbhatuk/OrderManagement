package com.example.demo.service;


import com.example.demo.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addProduct(Product product) {
        String sql = "INSERT into products(product_name,category_id,description,unit_price,stock_quantity) values(?,?,?,?,?)";
        jdbcTemplate.update(sql, product.getProductName(), product.getCategoryId(), product.getDescription(), product.getPrice(), product.getStockQuantity());

    }

    public void updateStock(int productId, int newStock) {
        String sql = "UPDATE products SET stock_quantity = ? where product_id = ?";
        jdbcTemplate.update(sql, newStock, productId);
    }

    public Product getProductByName(String productName) {

        String sql = "SELECT * FROM products WHERE product_name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {

                Product product = new Product();

                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("unit_price"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                return product;

            }, productName);
        } catch (IncorrectResultSizeDataAccessException e) {
            e.getMessage();
            return null;
        }

    }

    public Product getProductById(int productId) {

        String sql = "SELECT * FROM products WHERE product_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {

                Product product = new Product();

                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("unit_price"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                return product;

            }, productId);
        } catch (IncorrectResultSizeDataAccessException e) {
            e.getMessage();
            return null;
        }

    }

    public List<Product> getSameCategoryProducts(int categoryId) {
        String sql = "SELECT * from products where  category_id = ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowmapper) -> {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("unit_price"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                return product;
            }, categoryId);
        } catch (EmptyResultDataAccessException e) {
            e.getMessage();
            return null;
        }

    }

    public List<Product> getProductsBetweenPrice(double fromPrice, double toPrice) {
        String sql = "SELECT * FROM products WHERE unit_price >= ? AND unit_price <= ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("unit_price"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                return product;
            }, fromPrice, toPrice);
        }
        catch (EmptyResultDataAccessException e) {
            e.getMessage();
            return null;
        }

    }

    public Double getProductPriceById(int productId) {

        String sql = "SELECT unit_price FROM products WHERE product_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, Double.class, productId);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean isProductNameExists(String productName) {

        String sql = "SELECT product_name FROM products WHERE product_name = ?";

        List<String> result = jdbcTemplate.queryForList(sql, String.class, productName);

        return !result.isEmpty();
    }


}
