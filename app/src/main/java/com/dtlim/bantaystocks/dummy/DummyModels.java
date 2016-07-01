package com.dtlim.bantaystocks.dummy;

import com.dtlim.bantaystocks.data.model.Price;
import com.dtlim.bantaystocks.data.model.Stock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dale on 6/17/16.
 */
public class DummyModels {
    public static List<Stock> getDummyStockList() {
        List<Stock> list = new ArrayList<>();
        Stock stock = new Stock();
        stock.setName("PLDT");
        stock.setSymbol("TEL");
        stock.setPrice(new Price("PHP", "123.00"));
        stock.setVolume("1000");
        stock.setPercentChange("5.67");
        list.add(stock);

        stock = new Stock();
        stock.setName("Meralco Inc.");
        stock.setSymbol("MER");
        stock.setPrice(new Price("PHP", "53.00"));
        stock.setVolume("10300");
        stock.setPercentChange("-10.67");
        list.add(stock);

        stock = new Stock();
        stock.setName("2GO Travel");
        stock.setSymbol("2GO");
        stock.setPrice(new Price("PHP", "123.00"));
        stock.setVolume("1000");
        stock.setPercentChange("5.67");
        list.add(stock);

        return list;
    }
}
