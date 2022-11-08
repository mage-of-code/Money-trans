package com.example.Payments.controllers;

import com.example.Payments.dto.AccountDTO;
import com.example.Payments.dto.UserDTO;
import com.example.Payments.dto.UserInfo;
import com.example.Payments.models.Account;
import com.example.Payments.models.User;
import com.example.Payments.models.enums.TypeOfAccount;
import com.example.Payments.models.AccountBody.CreateAccountBody;
import com.example.Payments.security.UserDetailsImpl;
import com.example.Payments.services.AccountService;
import com.example.Payments.services.RegistrationService;
import com.example.Payments.services.UserService;
import com.example.Payments.util.UserErrorResponse;
import com.example.Payments.util.UserNotCreatedException;
import com.example.Payments.util.UserNotFoundException;
import com.example.Payments.util.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/money-trans/users")

public class UsersController {

    private UserService userService;
    private ModelMapper modelMapper;
    private UserValidator userValidator;
    private RegistrationService registrationService;
    private AccountService accountService;

    @Autowired
    public UsersController(UserService userService, ModelMapper modelMapper, UserValidator userValidator, RegistrationService registrationService, AccountService accountService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.accountService = accountService;
    }

    @GetMapping
    public List<UserInfo> getUsers() {
        return userService.findAll().stream().map(user -> userService.convertToUserInfo(user)).collect(Collectors.toList());
    }

    @PostMapping("/registration")
    public ResponseEntity<String> addUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult){
        User user = userService.convertToUser(userDTO);
        userValidator.validate(user,bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField()).append(" - ").
                        append(error.getDefaultMessage()).
                        append(";");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }
        registrationService.register(user, TypeOfAccount.INDIVIDUAL);
        return ResponseEntity.ok("Пользователь зарегистрирован");
    }

    @GetMapping("/my-information")
    public UserInfo getMyInformation() {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        System.out.println(userDetails.getUser().getRoles().toString());
        return userService.convertToUserInfo(userDetails.getUser()) ;
    }

    @PostMapping(path = "/new-account", consumes="application/json")
    public ResponseEntity<String> createAccount(@RequestBody CreateAccountBody createAccountBody){
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        accountService.saveNewAccount(userDetails.getUser(),createAccountBody.getTypeOfAccount());
        return ResponseEntity.ok("Счет успешно создан");
    }

    @GetMapping("/{id}")
    public UserInfo getUserInformation(@PathVariable("id") int userId) throws UserNotFoundException {
    return userService.convertToUserInfo(userService.findOne(userId));
    }


    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e) {
        UserErrorResponse response = new UserErrorResponse("Пользователь не найден",
                System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException e) {
        UserErrorResponse response = new UserErrorResponse(e.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
