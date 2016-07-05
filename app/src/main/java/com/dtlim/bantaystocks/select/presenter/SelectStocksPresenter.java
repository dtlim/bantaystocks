package com.dtlim.bantaystocks.select.presenter;

import com.dtlim.bantaystocks.select.view.SelectStocksView;

/**
 * Created by dale on 7/4/16.
 */
public interface SelectStocksPresenter {
    void bindView(SelectStocksView view);
    void initializeDataFromDatabase();
    void saveSubscribedStocks(String[] stockSymbols);
}
