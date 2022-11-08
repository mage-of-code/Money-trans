package com.example.Payments.models;

import com.example.Payments.models.enums.TypeOfAccount;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "bic")
    private String bic;

    @Column(name = "account_type")
    private TypeOfAccount accountType;

    @Column(name = "balance")
    private BigDecimal balance;

@OneToMany(mappedBy = "payerAccount")
    private List<Payment> sentPayments;

@OneToMany(mappedBy = "receiverAccount")
    private List<Payment> receivedPayments;

    @ManyToOne
    @JoinTable(name = "user_account",
    joinColumns = {@JoinColumn(name = "account_id")},
    inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private User user;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public TypeOfAccount getAccountType() {
        return accountType;
    }

    public void setAccountType(TypeOfAccount accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Payment> getSentPayments() {
        return sentPayments;
    }

    public void setSentPayments(List<Payment> sentPayments) {
        this.sentPayments = sentPayments;
    }

    public List<Payment> getReceivedPayments() {
        return receivedPayments;
    }

    public void setReceivedPayments(List<Payment> receivedPayments) {
        this.receivedPayments = receivedPayments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && Objects.equals(accountNumber, account.accountNumber) && Objects.equals(bic, account.bic) && accountType == account.accountType && Objects.equals(balance, account.balance) && Objects.equals(sentPayments, account.sentPayments) && Objects.equals(receivedPayments, account.receivedPayments) && Objects.equals(user, account.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, bic, accountType, balance, sentPayments, receivedPayments, user);
    }
}
