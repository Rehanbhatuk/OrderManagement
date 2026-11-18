package com.example.demo.dto;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class Customer {

    @NotBlank(message = "FirstName cannot be blanked!")
    String firstName;

    @NotBlank(message = "LastName cannot be blanked!")
    String lastName;

    @NotBlank(message = "Email cannot be blanked!")
    @Email(message = "Invalid email address")
    String email;
    String phoneNumber;
    Date registrationDate;
    String status;
    int id;
}
