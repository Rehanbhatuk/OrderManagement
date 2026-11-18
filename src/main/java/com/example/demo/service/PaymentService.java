package com.example.demo.service;


import com.example.demo.dto.OrderDto;
import com.example.demo.dto.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean makePayment(PaymentDto paymentDto) {

        try {
            String getOrderSql = "SELECT amount FROM orders WHERE order_id = ?";
            Double orderAmount = jdbcTemplate.queryForObject(getOrderSql, Double.class, paymentDto.getOrderid());

            double paidAmount = paymentDto.getAmount();
            if (paidAmount <= 0 || paidAmount > orderAmount) {
                System.out.println("Invalid payment amount!");
                return false;
            }

            double outstanding = orderAmount - paidAmount;

            String insertIntosql = "INSERT INTO payments(order_id, amount, payment_method, payment_status) VALUES (?, ?, ?, ?)";
            String status;

            if (outstanding == 0) {
                status = "DONE";
            } else {
                status = "OUTSTANDING:" + outstanding;
            }

            int rowsAffected = jdbcTemplate.update(insertIntosql, paymentDto.getOrderid(), paidAmount, paymentDto.getPaymentMethod(), status);

            return rowsAffected > 0;

        } catch (EmptyResultDataAccessException e) {
            System.out.println("Order not found!");
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<PaymentDto> findPaymentsByStatus(String status) {
        String sql = "SELECT * FROM payments WHERE payment_status LIKE ?";

        List<PaymentDto> payments = jdbcTemplate.query(sql, (rs, rowMapper) -> {
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setPaymentId(rs.getInt("payment_id"));
            paymentDto.setOrderid(rs.getInt("order_id"));
            paymentDto.setAmount(rs.getInt("amount"));
            paymentDto.setPaymentMethod(rs.getString("payment_method"));
            paymentDto.setPaymentdate(rs.getDate("payment_date"));
            return paymentDto;
        }, status + "%");

        if (payments.isEmpty()) {
            throw new RuntimeException("No payments with this status found!");
        }

        return payments;

    }

    public List<PaymentDto> getPaymentHistoryForOrder(int orderId) {
        try {
            String sql = "SELECT * from payments where order_id = ?";
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                PaymentDto paymentDto = new PaymentDto();
                paymentDto.setOrderid(rs.getInt("order_id"));
                paymentDto.setPaymentId(rs.getInt("payment_id"));
                paymentDto.setAmount(rs.getDouble("amount"));
                paymentDto.setPaymentMethod(rs.getString("payment_method"));
                paymentDto.setPaymentdate(rs.getDate("payment_date"));
                return paymentDto;
            }, orderId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<OrderDto> getUnpaidOrders() {
        String sql = "SELECT o.order_id, o.customer_id, o.amount - p.amount AS pending_amount FROM orders o INNER JOIN payments p ON o.order_id = p.order_id WHERE p.payment_status LIKE 'OUTSTANDING%'";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            OrderDto orderDto = new OrderDto();

            orderDto.setOrderId(rs.getInt("order_id"));
            orderDto.setCustomerId(rs.getInt("customer_id"));
            orderDto.setTotalAmount(rs.getDouble("pending_amount"));

            return orderDto;
        });
    }

    public boolean isOrderExists(int orderId) {

        String sql = "SELECT order_id FROM orders WHERE order_id = ?";

        List<Integer> result = jdbcTemplate.queryForList(sql, Integer.class, orderId);

        return !result.isEmpty();
    }


    public boolean isProductExists(int productId) {

        String sql = "SELECT product_id FROM products WHERE product_id = ?";

        List<Integer> result = jdbcTemplate.queryForList(sql, Integer.class, productId);

        return !result.isEmpty();
    }


}











