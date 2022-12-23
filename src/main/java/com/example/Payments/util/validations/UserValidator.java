package com.example.Payments.util.validations;

import com.example.Payments.dto.LoginInfo;
import com.example.Payments.models.User;
import com.example.Payments.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class UserValidator implements Validator {
    private UsersRepository usersRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginInfo user = (LoginInfo) target;
        if (usersRepository.findByName(user.getName()).isPresent()) {
            errors.rejectValue("name", "", "Такой пользователь уже существует");
        }
    }
}
