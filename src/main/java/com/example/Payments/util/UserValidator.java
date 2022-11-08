package com.example.Payments.util;

import com.example.Payments.models.User;
import com.example.Payments.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class UserValidator implements Validator {
    private UsersRepository usersRepository;
    @Autowired
    public UserValidator(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    User user=(User) target;
    if(usersRepository.findByName(user.getName()).isPresent()){
        errors.rejectValue("name","","Такой пользователь уже существует");
    }

    }
}
