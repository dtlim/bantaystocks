package com.dtlim.bantaystocks.select.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dtlim.bantaystocks.R;
import com.dtlim.bantaystocks.data.model.Stock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dale on 7/4/16.
 */
public class SelectStocksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Stock> mListStocks;
    private List<String> mSubscribedStocks;
    private Context mContext;

    protected static class SelectStockViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.bantaystocks_select_stock_item_container)
        public ViewGroup container;
        @BindView(R.id.bantaystocks_select_stock_item_symbol)
        public TextView textViewSymbol;
        @BindView(R.id.bantaystocks_select_stock_item_name)
        public TextView textViewName;
        @BindView(R.id.bantaystocks_select_stock_checkbox)
        public CheckBox checkBox;

        public SelectStockViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public SelectStocksAdapter(Context context) {
        mContext =  context;
        mListStocks = new ArrayList<>();
        mSubscribedStocks = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.bantaystocks_select_stock_item, parent, false);
        return new SelectStockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindSelectStockViewHolder((SelectStockViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mListStocks.size();
    }

    public void setStockList(List<Stock> stockList) {
        mListStocks = stockList;
        notifyDataSetChanged();
    }

    public void setSubscribedStocks(String[] subscribedStocks) {
        mSubscribedStocks.clear();
        mSubscribedStocks.addAll(Arrays.asList(subscribedStocks));
        notifyDataSetChanged();
    }

    public String[] getSubscribedStocks() {
        String[] arr = new String[mSubscribedStocks.size()];
        return mSubscribedStocks.toArray(arr);
    }

    private void bindSelectStockViewHolder(final SelectStockViewHolder holder, int position) {
        final Stock currentStock = mListStocks.get(position);
        holder.textViewName.setText(currentStock.getName());
        holder.textViewSymbol.setText(currentStock.getSymbol());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSubscribedStocks.contains(currentStock.getSymbol())) {
                    mSubscribedStocks.remove(currentStock.getSymbol());
                    holder.checkBox.setChecked(false);
                } else {
                    mSubscribedStocks.add(currentStock.getSymbol());
                    holder.checkBox.setChecked(true);
                }

                Log.d("SUBSCRIBEZ", "SUBSCRIBEZ " + mSubscribedStocks.size());
            }
        });

        if(mSubscribedStocks.contains(currentStock.getSymbol())) {
            holder.checkBox.setChecked(true);
        }
        else {
            holder.checkBox.setChecked(false);
        }

        if(position % 2 == 0) {
            holder.container.setBackgroundColor(ContextCompat.getColor(
                    mContext, R.color.bantaystocks_color_select_stocks_item_gray));
        }
        else {
            holder.container.setBackgroundColor(Color.WHITE);
        }
    }
}
