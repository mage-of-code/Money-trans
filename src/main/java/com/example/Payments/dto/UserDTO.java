package com.example.Payments.dto;

import lombok.Data;
import java.util.Set;
@Data
public class UserDTO {

    private String name;
    private Set<AccountDTO> accountDTOSet;

}
