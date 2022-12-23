package com.example.Payments.dto;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class LoginInfo {


    @Size(min=2, max=30, message = "Размер имени должен быть от 2 до 30 символов")
    private String name;


    @Size(min=8,max =8, message = "Пароль должен содержать 8 символов")
    private String password;



}
