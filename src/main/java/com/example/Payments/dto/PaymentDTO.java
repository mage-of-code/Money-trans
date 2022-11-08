package com.example.Payments.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

public class PaymentDTO {

    @NotNull(message = "payer account should not be null")
    private AccountDTO payerAccount;

    @NotNull(message = "receiver account should not be null")
    private AccountDTO receiverAccount;

    @Min(value = 0, message = "amount should be >0")
    private BigDecimal amount;

    @Size(min = 0,max = 150, message = "comment should be less than 150 characters")
    private String comment;

    public AccountDTO getPayerAccount() {
        return payerAccount;
    }

    public void setPayerAccount(AccountDTO payerAccount) {
        this.payerAccount = payerAccount;
    }

    public AccountDTO getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(AccountDTO receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentDTO that = (PaymentDTO) o;
        return Objects.equals(payerAccount, that.payerAccount) && Objects.equals(receiverAccount, that.receiverAccount) && Objects.equals(amount, that.amount) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payerAccount, receiverAccount, amount, comment);
    }
}
