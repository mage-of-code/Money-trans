package com.example.Payments.controllers;

import com.example.Payments.dto.PaymentDTO;
import com.example.Payments.dto.UserDTO;
import com.example.Payments.mapper.UserMapper;
import com.example.Payments.models.User;
import com.example.Payments.services.PaymentService;
import com.example.Payments.services.UserService;
import com.example.Payments.util.Responses.AccountErrorResponse;
import com.example.Payments.util.Exceptions.AccountNotFoundException;
import com.example.Payments.util.Responses.UserErrorResponse;
import com.example.Payments.util.Exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/money-trans/admin")
@AllArgsConstructor
public class AdminController {
    private PaymentService paymentService;
    private UserService userService;
    private UserMapper userMapper;


    @GetMapping("/users")
    public Page<UserDTO> getUsers(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserDTO> all = userService.findAll(pageable);
        return all;
    }

    @GetMapping("/users/{id}")
    public UserDTO getUserInformation(@PathVariable("id") int userId) {
        User user = userService.findOne(userId);
        return userMapper.toDto(user);
    }


    @GetMapping("/all-transfers")
    public Page<PaymentDTO> getAllPayments(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        return paymentService.findAll(pageable);


    }

    @GetMapping("/{accountId}/all-transfers")
    public Page<PaymentDTO> getAllPaymentsByAccountId(@PathVariable("accountId") Integer accountId,
                                                      @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return paymentService.findAllByAccountId(pageable, accountId);
    }

    @GetMapping("/{accountId}/sent")
    public Page<PaymentDTO> getSentPayments(@PathVariable("accountId") Integer accountId,
                                            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return paymentService.findSent(pageable, accountId);
    }

    @GetMapping("/{accountId}/received")
    public Page<PaymentDTO> getReceivedPayments(@PathVariable("accountId") Integer accountId,
                                                @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return paymentService.findReceived(pageable, accountId);

    }

    @ExceptionHandler
    public ResponseEntity<AccountErrorResponse> handleException(AccountNotFoundException e) {
        AccountErrorResponse response = new AccountErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e) {
        UserErrorResponse response = new UserErrorResponse(e.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}

