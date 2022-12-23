package com.example.Payments.controllers;

import com.example.Payments.dto.AccountTypeDTO;
import com.example.Payments.dto.PaymentDTO;
import com.example.Payments.dto.UserDTO;

import com.example.Payments.mapper.UserMapper;
import com.example.Payments.models.AccountType;
import com.example.Payments.security.UserDetailsImpl;
import com.example.Payments.services.PaymentService;
import com.example.Payments.services.UserService;
import com.example.Payments.util.Exceptions.PaymentRequestException;
import com.example.Payments.util.Responses.PaymentRequestErrorResponse;
import com.example.Payments.util.validations.PaymentRequestValidator;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/money-trans/users")
@AllArgsConstructor

public class UserController {

    private UserService userService;
    private UserMapper userMapper;
    private PaymentService paymentService;
    private PaymentRequestValidator paymentRequestValidator;
    private ModelMapper modelMapper;


    @PostMapping(path = "/new-account", consumes = "application/json")
    public ResponseEntity<String> createAccount(@RequestBody AccountTypeDTO accountTypeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
       userService.addAccountToUser(userDetails.getUser(),userService.createAccount(modelMapper.map(accountTypeDTO, AccountType.class)));
        return ResponseEntity.ok("Счет успешно создан");
    }

    @GetMapping("/my-information")
    public UserDTO getMyInformation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userMapper.toDto(userDetails.getUser());
    }

    @GetMapping("/{accountId}/all-transfers")
    public Page<PaymentDTO> getAllPaymentsByAccountId(@PathVariable("accountId") Integer accountId,
                                                      @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        paymentRequestValidator.validate(userDetails.getUser(),accountId);
        return paymentService.findAllByAccountId(pageable, accountId);
    }

    @GetMapping("/{accountId}/sent")
    public Page<PaymentDTO> getSentPayments(@PathVariable("accountId") Integer accountId,
                                            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        paymentRequestValidator.validate(userDetails.getUser(),accountId);
        return paymentService.findSent(pageable, accountId);
    }

    @GetMapping("/{accountId}/received")
    public Page<PaymentDTO> getReceivedPayments(@PathVariable("accountId") Integer accountId,
                                                @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        paymentRequestValidator.validate(userDetails.getUser(),accountId);
        return paymentService.findReceived(pageable, accountId);
    }

    @ExceptionHandler
    public ResponseEntity<PaymentRequestErrorResponse> handleException(PaymentRequestException e){
        PaymentRequestErrorResponse response = new PaymentRequestErrorResponse(e.getMessage(),System.currentTimeMillis());
        return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
    }

}
