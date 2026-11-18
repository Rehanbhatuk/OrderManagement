package com.example.demo.exceptions;


import com.example.demo.dto.FieldError;
import lombok.Data;

import java.util.List;
@Data
public class RestParameterInvalidException extends RuntimeException {

    private List<FieldError> errors;

    public RestParameterInvalidException(List<FieldError> errors) {
        super("Invalid request parameters");
        this.errors = errors;
    }


}
