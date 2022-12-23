package com.example.Payments.controllers;

import com.example.Payments.dto.LoginInfo;
import com.example.Payments.services.RegistrationService;
import com.example.Payments.util.ErrorMessageBuilder;
import com.example.Payments.util.Responses.UserErrorResponse;
import com.example.Payments.util.Exceptions.UserNotCreatedException;
import com.example.Payments.util.validations.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/money-trans")
@AllArgsConstructor
public class RegistrationController {

    private UserValidator userValidator;
    private RegistrationService registrationService;

    @PostMapping("/registration")
    public ResponseEntity<String> addUser(@RequestBody @Valid LoginInfo loginInfo, BindingResult bindingResult) {
        userValidator.validate(loginInfo, bindingResult);
        if (bindingResult.hasErrors())
            throw new UserNotCreatedException(ErrorMessageBuilder.buildCorrectErrorMessage(bindingResult));
        registrationService.register(loginInfo);

        return ResponseEntity.ok("Пользователь зарегистрирован");
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException e) {
        UserErrorResponse response = new UserErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



}
