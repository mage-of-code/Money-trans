package com.example.Payments.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

public class UserInfo {

    @NotEmpty
    @Size(min=2, max=30, message = "name should be between 2 and 30 characters")
    private String name;

    private Set<AccountDTO> accountDTOSet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<AccountDTO> getAccountDTOSet() {
        return accountDTOSet;
    }

    public void setAccountDTOSet(Set<AccountDTO> accountDTOSet) {
        this.accountDTOSet = accountDTOSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(name, userInfo.name) && Objects.equals(accountDTOSet, userInfo.accountDTOSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, accountDTOSet);
    }
}
