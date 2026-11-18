package com.example.demo.service;

import com.example.demo.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addCustomer(Customer customer) {

        String sql = "INSERT INTO customers(firstname, lastname, email, phone_number, registration_date, status) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getPhoneNumber(), customer.getRegistrationDate(), customer.getStatus());
    }


    public Customer getCustomerByEmail(String email) {

        try {
            String sql = "SELECT * FROM customers WHERE email = ?";

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {

                Customer customer = new Customer();

                customer.setId(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("firstname"));
                customer.setLastName(rs.getString("lastname"));
                customer.setEmail(rs.getString("email"));
                customer.setPhoneNumber(rs.getString("phone_number"));
                customer.setRegistrationDate(rs.getDate("registration_date"));
                customer.setStatus(rs.getString("status"));

                return customer;

            }, email);

        } catch (IncorrectResultSizeDataAccessException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }


    public Customer getCustomerById(int id) {

        try {
            String sql = "SELECT * FROM customers WHERE customer_id = ?";

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {

                Customer customer = new Customer();

                customer.setId(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("firstname"));
                customer.setLastName(rs.getString("lastname"));
                customer.setEmail(rs.getString("email"));
                customer.setPhoneNumber(rs.getString("phone_number"));
                customer.setRegistrationDate(rs.getDate("registration_date"));
                customer.setStatus(rs.getString("status"));

                return customer;

            }, id);

        } catch (IncorrectResultSizeDataAccessException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }


    public void updateCustomer(Customer customer) {

        String sql = "UPDATE customers SET firstname = ?, lastname = ?, phone_number = ?, status = ?, email = ? WHERE customer_id = ?";

        jdbcTemplate.update(sql, customer.getFirstName(), customer.getLastName(), customer.getPhoneNumber(), customer.getStatus(), customer.getEmail(), customer.getId());
    }


    public List<Customer> getAllActiveCustomers() {

        String sql = "SELECT * FROM customers WHERE status = 'ACTIVE'";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            Customer customer = new Customer();

            customer.setFirstName(rs.getString("firstname"));
            customer.setLastName(rs.getString("lastname"));
            customer.setId(rs.getInt("customer_id"));
            customer.setEmail(rs.getString("email"));
            customer.setRegistrationDate(rs.getDate("registration_date"));
            customer.setPhoneNumber(rs.getString("phone_number"));
            customer.setStatus(rs.getString("status"));

            return customer;
        });
    }

    public boolean isEmailExists(String email) {

        String sql = "SELECT email FROM customers WHERE email = ?";

        List<String> result = jdbcTemplate.queryForList(sql, String.class, email);

        return !result.isEmpty();
    }


    public boolean isPhoneNumberExists(String phoneNumber) {

        String sql = "SELECT phone_number FROM customers WHERE phone_number = ?";

        List<String> result = jdbcTemplate.queryForList(sql, String.class, phoneNumber);

        return !result.isEmpty();
    }
}