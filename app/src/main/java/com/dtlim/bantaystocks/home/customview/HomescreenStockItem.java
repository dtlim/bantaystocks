package com.dtlim.bantaystocks.home.customview;

import android.animation.LayoutTransition;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dtlim.bantaystocks.R;
import com.dtlim.bantaystocks.common.utility.ViewAnimationUtility;
import com.dtlim.bantaystocks.data.model.Stock;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dale on 6/23/16.
 */
public class HomescreenStockItem extends CardView implements View.OnClickListener{

    public interface HomescreenStockItemListener {
        void onCloseButtonClick(Stock stock);
    }

    private static final int ANIMATION_DURATION = 150;

    @BindView(R.id.bantaystocks_homescreen_stock_item_container)
    ViewGroup container;
    @BindView(R.id.bantaystocks_homescreen_stock_item_symbol)
    TextView textViewSymbol;
    @BindView(R.id.bantaystocks_homescreen_stock_item_price)
    TextView textViewPrice;
    @BindView(R.id.bantaystocks_homescreen_stock_item_percent_change)
    TextView textViewPercentChange;
    @BindView(R.id.bantaystocks_homescreen_stock_item_close)
    ViewGroup closeButton;

    private Stock mStock;
    private HomescreenStockItemListener mListener;
    private boolean isShown = false;

    public HomescreenStockItem(Context context) {
        super(context);
        initialize();
    }

    public HomescreenStockItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public HomescreenStockItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        inflate(getContext(), R.layout.bantaystocks_homescreen_item, this);
        ButterKnife.bind(this);

        setOnClickListener(this);
        closeButton.setOnClickListener(this);
        LayoutTransition transition = new LayoutTransition();
        setLayoutTransition(transition);
    }

    public void setHomescreenStockItemListener(HomescreenStockItemListener listener) {
        mListener = listener;
    }

    public void setStock(Stock stock) {
        mStock = stock;
        textViewSymbol.setText(stock.getSymbol());
        textViewPrice.setText(stock.getPrice().getAmount());
        textViewPercentChange.setText(stock.getPercentChange());

        try {
            float percentage = Float.valueOf(stock.getPercentChange());
            if (percentage > 0) {
                textViewPrice.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.bantaystocks_color_stock_price_up_green));
                textViewPercentChange.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.bantaystocks_color_stock_price_up_green));
                textViewPercentChange.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(getContext(), R.drawable.bantaystocks_ticker_up), null, null, null);
            } else if (percentage < 0) {
                textViewPrice.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.bantaystocks_color_stock_price_down_red));
                textViewPercentChange.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.bantaystocks_color_stock_price_down_red));
                textViewPercentChange.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(getContext(), R.drawable.bantaystocks_ticker_down), null, null, null);
            } else {
                textViewPrice.setTextColor(
                        ContextCompat.getColor(getContext(), android.R.color.primary_text_dark));
                textViewPercentChange.setTextColor(
                        ContextCompat.getColor(getContext(), android.R.color.primary_text_dark));
                textViewPercentChange.setCompoundDrawablesWithIntrinsicBounds(
                        null, null, null, null);
            }
        }
        catch(NumberFormatException e) {
            textViewPrice.setTextColor(
                    ContextCompat.getColor(getContext(), android.R.color.primary_text_dark));
            textViewPercentChange.setTextColor(
                    ContextCompat.getColor(getContext(), android.R.color.primary_text_dark));
            textViewPercentChange.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, null, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bantaystocks_homescreen_stock_item_close: {
                if(mListener != null && mStock != null) {
                    mListener.onCloseButtonClick(mStock);
                }
                break;
            }

            default: {
                expandOrContractCloseButton();
            }
        }
    }

    private void expandOrContractCloseButton() {
        closeButton.setVisibility(closeButton.getVisibility()==VISIBLE ? GONE : VISIBLE);
    }

    public void show() {
        if(!isShown) {
            ViewAnimationUtility.playScaleUpAnimation(this, ANIMATION_DURATION, 0);
            isShown = true;
        }
    }

    public void hide() {
        if(isShown) {
            ViewAnimationUtility.playScaleDownAnimation(this, ANIMATION_DURATION, 0);
            isShown = false;
        }
    }
}
