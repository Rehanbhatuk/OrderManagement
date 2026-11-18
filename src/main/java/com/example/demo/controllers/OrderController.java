package com.example.demo.controllers;

import com.example.demo.dto.OrderDto;

import com.example.demo.service.OrderService;

import com.example.demo.validator.OrderValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

     @Autowired
    private OrderValidator orderValidator;


    @PostMapping("/add")
    public ResponseEntity<String> insertOrder(@Valid @RequestBody OrderDto orderDto) {

        logger.trace("Entering insertOrder()");
        orderValidator.validateOrder(orderDto);
        orderValidator.databaseValidator(orderDto);
        logger.debug("Preparing order details");

        try {

            logger.debug("Order prepared with {} products", orderDto.getProductsList().size());

            orderService.insertOrder(orderDto);

            logger.info("Order inserted successfully for customer ID: {}", orderDto.getCustomerId());

            return ResponseEntity.ok("Order inserted successfully!");

        } catch (Exception e) {

            logger.error("Failed to insert order", e);

            throw e;
        }
    }


    @GetMapping("/get-order-between-dates/{fromDate}/{toDate}")
    public List<OrderDto> getOrdersBetweenDates(@PathVariable String fromDate, @PathVariable String toDate) {

        logger.trace("Entering getOrdersBetweenDates()");

        logger.debug("Fetching orders between {} and {}", fromDate, toDate);

        try {

            List<OrderDto> orderList = orderService.orderBetweenDates(java.sql.Date.valueOf(fromDate), java.sql.Date.valueOf(toDate));

            if (orderList.isEmpty()) {

                logger.warn("No orders found between {} and {}", fromDate, toDate);

            } else {

                logger.info("Orders fetched successfully. Total: {}", orderList.size());
            }

            return orderList;

        } catch (Exception e) {

            logger.error("Failed to fetch orders between {} and {}", fromDate, toDate, e);

            throw e;
        }
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {

        logger.error("Unexpected error occurred in OrderController", e);

        return ResponseEntity.status(500).body("Internal Server Error");
    }

}