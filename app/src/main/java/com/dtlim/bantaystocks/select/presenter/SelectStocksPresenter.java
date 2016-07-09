package com.dtlim.bantaystocks.select.presenter;

import com.dtlim.bantaystocks.base.BasePresenter;
import com.dtlim.bantaystocks.select.view.SelectStocksView;

/**
 * Created by dale on 7/4/16.
 */
public interface SelectStocksPresenter extends BasePresenter<SelectStocksView> {
    void initializeDataFromDatabase();
    void initializeSubscribedStocks();
    void saveSubscribedStocks(String[] stockSymbols);
}
