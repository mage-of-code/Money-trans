package com.example.Payments.dto;

import com.example.Payments.models.Account;



import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;


public class UserDTO {

    @NotEmpty
    @Size(min=2, max=30, message = "name should be between 2 and 30 characters")
    private String name;

    @NotEmpty
    @Size(min=8,max =8, message = "password should contains 8 symbols")
    private String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
