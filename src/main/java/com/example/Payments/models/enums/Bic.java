package com.example.Payments.models.enums;

public enum Bic {
     BIC("123456789");
    private String bic;

    Bic(String bic) {
        this.bic = bic;
    }

    public String getBic() {
        return bic;
    }
}
