package com.example.demo.validator;

import com.example.demo.dto.Category;
import com.example.demo.dto.FieldError;
import com.example.demo.exceptions.RestParameterInvalidException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class CategoryValidator {
    @Autowired
    private MessageSource messageSource;

    List<FieldError> errors = new ArrayList<>();

    public void categoryValidator(Category category){
        String categoryName = category.getCategoryName();
        if (StringUtils.isBlank(categoryName) || StringUtils.isAlphanumeric(categoryName)){
            FieldError fieldError = new FieldError();
            fieldError.setKey("CategoryName");
            fieldError.setErrorMessage(messageSource.getMessage("category.invalid.categoryName",null, Locale.getDefault()));

            errors.add(fieldError);
        }

        if (!errors.isEmpty()){
            throw new RestParameterInvalidException(errors);
        }
    }
}
