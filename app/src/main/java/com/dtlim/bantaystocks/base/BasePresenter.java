package com.dtlim.bantaystocks.base;

/**
 * Created by dale on 7/8/16.
 */
public interface BasePresenter<T> {
    void bindView(T view);
    void onActivityResume();
    void onActivityPause();
}
