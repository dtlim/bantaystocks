package com.dtlim.bantaystocks.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dale on 6/17/16.
 */
public class Price {
    @SerializedName("currency")
    private String currency;
    @SerializedName("amount")
    private String amount;

    public Price(String currency, String amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
