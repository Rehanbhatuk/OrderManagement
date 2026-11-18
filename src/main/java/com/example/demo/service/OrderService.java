package com.example.demo.service;

import com.example.demo.dto.OrderDto;
import com.example.demo.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@Service
public class OrderService {

    @Autowired
   private JdbcTemplate jdbcTemplate;

    public void insertOrder(OrderDto orderDto) {

        String sql = "INSERT INTO orders (customer_id, order_date, order_status, amount) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsInserted = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, orderDto.getCustomerId());
            ps.setDate(2, new java.sql.Date(orderDto.getOrderDate().getTime()));
            ps.setString(3, orderDto.getStatus());
            ps.setDouble(4, orderDto.getTotalAmount());
            return ps;
        }, keyHolder);

        if (rowsInserted > 0) {
            orderDto.setOrderId(keyHolder.getKey().intValue());
            insertOrderItems(orderDto);

            for (Product productDto : orderDto.getProductsList()) {
                updateStock(productDto.getProductId(), productDto.getQuantity());
            }
        }
    }

    public void insertOrderItems(OrderDto orderDto){
        String orderitemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price, line_total) VALUES (?, ?, ?, ?, ?)";
        for (Product product: orderDto.getProductsList()){
            int productId = product.getProductId();
            int quantity = product.getQuantity();
            double productPrice = product.getPrice();
            double lineTotal = quantity * productPrice;
            jdbcTemplate.update(orderitemSql,orderDto.getOrderId(), productId,quantity,productPrice,lineTotal);


        }
    }

    public void updateStock(int productId, int quantity){
        String sql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id = ? AND stock_quantity >= ?";
        jdbcTemplate.update(sql, quantity,productId,quantity);
    }

    public List<OrderDto> orderBetweenDates(java.util.Date fromDate, java.util.Date toDate) {

        String sql = "SELECT * FROM orders WHERE order_date BETWEEN ? AND ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            OrderDto orderDto = new OrderDto();
            orderDto.setOrderId(rs.getInt("order_id"));
            orderDto.setCustomerId(rs.getInt("customer_id"));
            orderDto.setOrderDate(rs.getDate("order_date"));
            orderDto.setStatus(rs.getString("order_status"));
            orderDto.setTotalAmount(rs.getDouble("amount"));
            return orderDto;
        }, new java.sql.Date(fromDate.getTime()), new java.sql.Date(toDate.getTime()));
    }

    public boolean isCustomerExists(int customerId) {

        String sql = "SELECT customer_id FROM customers WHERE customer_id = ?";

        List<Integer> result = jdbcTemplate.queryForList(sql, Integer.class, customerId);

        return !result.isEmpty();
    }


    public boolean isProductExists(int productId) {

        String sql = "SELECT product_id FROM products WHERE product_id = ?";

        List<Integer> result = jdbcTemplate.queryForList(sql, Integer.class, productId);

        return !result.isEmpty();
    }




}
