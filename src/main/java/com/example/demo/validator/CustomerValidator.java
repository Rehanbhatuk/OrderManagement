package com.example.demo.validator;

import com.example.demo.dto.Customer;
import com.example.demo.dto.FieldError;
import com.example.demo.exceptions.RestParameterInvalidException;
import com.example.demo.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class CustomerValidator {

    @Autowired
    private MessageSource messageSource;


    @Autowired
    CustomerService customerService;


    public void validateCustomer(Customer customer) {

        List<FieldError> errors = new ArrayList<>();

        String email = customer.getEmail();

        if (StringUtils.isBlank(email) || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

            FieldError fieldError = new FieldError();
            fieldError.setKey("Email");
            fieldError.setErrorMessage(messageSource.getMessage("customer.invalid.email", null, Locale.getDefault()));

            errors.add(fieldError);
        }

        String firstName = customer.getFirstName();

        if (StringUtils.isBlank(firstName) || !StringUtils.isAlpha(firstName)) {

            FieldError fieldError = new FieldError();
            fieldError.setKey("FirstName");
            fieldError.setErrorMessage(messageSource.getMessage("customer.invalid.firstName", null, Locale.getDefault()));

            errors.add(fieldError);
        }

        String lastName = customer.getLastName();

        if (StringUtils.isBlank(lastName) || !StringUtils.isAlpha(lastName)) {

            FieldError fieldError = new FieldError();
            fieldError.setKey("lastName");
            fieldError.setErrorMessage(messageSource.getMessage("customer.invalid.lastName", null, Locale.getDefault()));

            errors.add(fieldError);
        }


        String phoneNumber = customer.getPhoneNumber();

        if (StringUtils.isBlank(phoneNumber) || !StringUtils.isNumeric(phoneNumber)) {
            FieldError fieldError = new FieldError();
            fieldError.setKey("lastName");
            fieldError.setErrorMessage(messageSource.getMessage("customer.invalid.phoneNumber", null, Locale.getDefault()));
            errors.add(fieldError);
        }

        if (!errors.isEmpty()) {
            throw new RestParameterInvalidException(errors);
        }

    }

    public void databaseValidator(Customer customer) {

        List<FieldError> errors = new ArrayList<>();

        String email = customer.getEmail();

        if (StringUtils.isNotBlank(email)) {
            if (customerService.isEmailExists(email)) {

                FieldError fieldError = new FieldError();
                fieldError.setKey("email");
                fieldError.setErrorMessage(messageSource.getMessage("customer.email.alreadyexists", null, Locale.getDefault()));

                errors.add(fieldError);
            }
        }

        String phoneNumber = customer.getPhoneNumber();

        if (StringUtils.isNotBlank(phoneNumber)) {

            if (customerService.isPhoneNumberExists(phoneNumber)) {

                FieldError fieldError = new FieldError();
                fieldError.setKey("phoneNumber");
                fieldError.setErrorMessage(messageSource.getMessage("customer.phoneNumber.alreadyexists", null, Locale.getDefault()));

                errors.add(fieldError);
            }
        }

        if (!errors.isEmpty()) {
            throw new RestParameterInvalidException(errors);
        }
    }

}
