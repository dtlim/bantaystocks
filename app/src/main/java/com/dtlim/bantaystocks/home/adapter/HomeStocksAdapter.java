package com.dtlim.bantaystocks.home.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dtlim.bantaystocks.R;
import com.dtlim.bantaystocks.data.model.Stock;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dale on 6/17/16.
 */
public class HomeStocksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Stock> mListStocks;
    private Context mContext;

    protected static class StockViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.bantaystocks_stock_item_symbol)
        public TextView textViewSymbol;
        @BindView(R.id.bantaystocks_stock_item_name)
        public TextView textViewName;
        @BindView(R.id.bantaystocks_stock_item_price)
        public TextView textViewPrice;
        @BindView(R.id.bantaystocks_stock_item_percent_change)
        public TextView textViewPercentChange;

        public StockViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public HomeStocksAdapter(Context context) {
        mContext = context;
        mListStocks = new ArrayList<>();
    }

    public void setStockList(List<Stock> list) {
        mListStocks = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.bantaystocks_stock_item, parent, false);
        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindStockViewHolder((StockViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mListStocks.size();
    }

    private void bindStockViewHolder(StockViewHolder holder, int position) {
        Stock currentStock = mListStocks.get(position);
        holder.textViewName.setText(currentStock.getName());
        holder.textViewSymbol.setText(currentStock.getSymbol());
        holder.textViewPrice.setText(currentStock.getPrice().getAmount());
        holder.textViewPercentChange.setText(currentStock.getPercentChange());

        float percentage = Float.valueOf(currentStock.getPercentChange());
        if(percentage > 0) {
            holder.textViewPrice.setTextColor(
                    ContextCompat.getColor(mContext, R.color.bantaystocks_color_stock_price_up_green));
            holder.textViewPercentChange.setTextColor(
                    ContextCompat.getColor(mContext, R.color.bantaystocks_color_stock_price_up_green));
            holder.textViewPercentChange.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(mContext, R.drawable.bantaystocks_ticker_up), null, null, null);
        }
        else if(percentage < 0) {
            holder.textViewPrice.setTextColor(
                    ContextCompat.getColor(mContext, R.color.bantaystocks_color_stock_price_down_red));
            holder.textViewPercentChange.setTextColor(
                    ContextCompat.getColor(mContext, R.color.bantaystocks_color_stock_price_down_red));
            holder.textViewPercentChange.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(mContext, R.drawable.bantaystocks_ticker_down), null, null, null);
        }
        else {
            holder.textViewPrice.setTextColor(
                    ContextCompat.getColor(mContext, android.R.color.primary_text_dark));
            holder.textViewPercentChange.setTextColor(
                    ContextCompat.getColor(mContext, android.R.color.primary_text_dark));
            holder.textViewPercentChange.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, null, null);
        }
    }
}
