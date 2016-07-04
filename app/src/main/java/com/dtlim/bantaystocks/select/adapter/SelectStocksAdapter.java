package com.dtlim.bantaystocks.select.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dtlim.bantaystocks.R;
import com.dtlim.bantaystocks.data.model.Stock;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dale on 7/4/16.
 */
public class SelectStocksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Stock> mListStocks;
    private Context mContext;

    protected static class SelectStockViewHolder extends RecyclerView.ViewHolder{
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

    private void bindSelectStockViewHolder(SelectStockViewHolder holder, int position) {
        Stock currentStock = mListStocks.get(position);
        holder.textViewName.setText(currentStock.getName());
        holder.textViewSymbol.setText(currentStock.getSymbol());
    }
}
