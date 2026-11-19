package com.example.demo.controllers;

import com.example.demo.service.CustomerService;
import com.example.demo.dto.Customer;

import com.example.demo.validator.CustomerValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@RequestMapping("/customer")
@RestController

//Customer controller
public class CustomerController {
    @Autowired
    private CustomerValidator customerValidator;

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;


    @PostMapping("/add")
    public String addCustomer(@Valid @RequestBody Customer customer) {


        log.trace("Entering addCustomer()");


        customerValidator.validateCustomer(customer);
        customerValidator.databaseValidator(customer);

        log.debug("Adding new customer");

        customerService.addCustomer(customer);


        log.info("Customer added successfully");

        return "Customer added successfully!";
    }


    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<?> displayCustomerByEmail(@PathVariable String email) {

        log.trace("Entering displayCustomerByEmail()");

        log.debug("Fetching customer by email");

        Customer customer = customerService.getCustomerByEmail(email);

        if (customer == null) {

            log.warn("Customer not found for the requested email");

            return ResponseEntity.status(404).body("Invalid email or Customer does not exist");
        }

        log.info("Customer fetched successfully");

        return ResponseEntity.ok(customer);
    }


    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> displayCustomerById(@PathVariable int id) {

        log.trace("Entering displayCustomerById()");

        log.debug("Fetching customer with ID: {}", id);

        Customer customer = customerService.getCustomerById(id);

        if (customer == null) {

            log.warn("Customer not found with ID: {}", id);

            return ResponseEntity.status(404).body("Invalid Id or Customer does not exist");
        }

        log.info("Customer fetched successfully with ID: {}", id);

        return ResponseEntity.ok(customer);
    }


    @PostMapping("/update")
    public String updateCustomer(@RequestBody Customer customer) {

        log.trace("Entering updateCustomer()");

        log.debug("Updating customer");

        customerService.updateCustomer(customer);

        log.info("Customer updated successfully");

        return "Customer Updated";
    }


    @GetMapping("/get/active")
    public ResponseEntity<List<Customer>> getAllActiveCustomers() {

        log.trace("Entering getAllActiveCustomers()");

        log.debug("Fetching all active customers");

        List<Customer> customerList = customerService.getAllActiveCustomers();

        log.info("Active customers fetched successfully. Total: {}", customerList.size());

        return ResponseEntity.ok(customerList);
    }


}