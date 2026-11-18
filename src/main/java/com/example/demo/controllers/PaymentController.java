package com.example.demo.controllers;

import com.example.demo.dto.OrderDto;
import com.example.demo.dto.PaymentDto;
import com.example.demo.service.PaymentService;

import com.example.demo.validator.PaymentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {


    //Payment Controller in Github Folder

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentValidator paymentValidator;


    @PostMapping("/make")
    public ResponseEntity<String> makePayment(@RequestBody PaymentDto paymentDto) {

        log.trace("Entering makePayment()");

        try {
            paymentValidator.validatePayment(paymentDto);
            paymentValidator.databaseValidator(paymentDto);

            paymentService.makePayment(paymentDto);

            log.info("Payment processed successfully");

            return ResponseEntity.ok("Payment processed successfully");

        } catch (Exception e) {

            log.error("Failed to process payment", e);

            throw e;
        }
    }


        @GetMapping("/get-by-status")
        public List<PaymentDto> getPaymentByStatus(@RequestParam String status) {

            log.trace("Entering getPaymentByStatus()");

            log.debug("Fetching payments with status: {}", status);

            List<PaymentDto> payments = paymentService.findPaymentsByStatus(status);

            if (payments.isEmpty()) {
                log.warn("No payments found with status: {}", status);
            } else {
                log.info("Payments fetched successfully. Total: {}", payments.size());
            }

            return payments;
        }


    @GetMapping("/get-payment-history")
    public List<PaymentDto> getPaymentHistoryForOrder(@RequestParam int orderId) {

        log.trace("Entering getPaymentHistoryForOrder()");

        log.debug("Fetching payment history for order ID: {}", orderId);

        List<PaymentDto> paymentHistory = paymentService.getPaymentHistoryForOrder(orderId);

        if (paymentHistory.isEmpty()) {
            log.warn("No payment history found for order ID: {}", orderId);
        } else {
            log.info("Payment history fetched successfully for order ID: {}", orderId);
        }

        return paymentHistory;
    }


    @GetMapping("/get-unpaid-orders")
    public List<OrderDto> getUnpaidOrders() {

        log.trace("Entering getUnpaidOrders()");

        log.debug("Fetching all unpaid orders");

        List<OrderDto> unpaidOrders = paymentService.getUnpaidOrders();

        if (unpaidOrders.isEmpty()) {
            log.warn("No unpaid orders found");
        } else {
            log.info("Unpaid orders fetched successfully. Total: {}", unpaidOrders.size());
        }

        return unpaidOrders;
    }



}