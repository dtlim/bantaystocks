package com.dtlim.bantaystocks.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dale on 6/17/16.
 */
public class Stock {
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private Price price;
    @SerializedName("percent_change")
    private String percentChange;
    @SerializedName("volume")
    private String volume;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("as_of")
    private String lastUpdate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean isValid() {
        return name != null && price != null && price.getAmount() != null;
    }
}
