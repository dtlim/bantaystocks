package com.dtlim.bantaystocks.home.presenter;

import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.home.view.HomeView;

import java.util.List;

/**
 * Created by dale on 7/1/16.
 */
public interface HomePresenter {
    void bindView(HomeView view);
    void initializeData();
}