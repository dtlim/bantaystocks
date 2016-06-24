package com.dtlim.bantaystocks.data.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.home.HomeActivity;
import com.dtlim.bantaystocks.home.customview.HomescreenItemTouchListener;
import com.dtlim.bantaystocks.home.customview.HomescreenStockItem;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by dale on 6/23/16.
 */
public class StocksService extends Service {

    private MqttClient mClient;

    private WindowManager mWindowManager;
    private HomescreenStockItem mStockItem;

    private Gson gson = new Gson();
    Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mStockItem != null) {
            mWindowManager.removeView(mStockItem);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initialize() {

        initializeForeground();

        addHomescreenStockView();

        try {
            mClient = new MqttClient("tcp://broker.mqttdashboard.com:1883",
                    MqttClient.generateClientId(),
                    new MemoryPersistence());

            mClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    String message = new String(mqttMessage.getPayload());
                    Log.d("MQTT", topic + ": " + message);
                    updateHomeStockView(message);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
            mClient.connect();

            String[] topics = new String[]{"dale/stocks/2GO", "dale/stocks/TEL"};
            subscribe(topics);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeForeground() {
        Intent notificationIntent = new Intent(this, HomeActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("BantayStocks")
                .setContentText("BantayStocks is currently running")
                .setContentIntent(pendingIntent).build();

        startForeground(9999, notification);
    }

    private void subscribe(String... topics) {
        try {
            if (mClient != null) {
                int[] qos = new int[topics.length];
                for (int i = 0; i < topics.length; i++) {
                    qos[i] = 1;
                }
                mClient.subscribe(topics, qos);
            }
        }

        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void addHomescreenStockView() {
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mStockItem = new HomescreenStockItem(this);
        final WindowManager.LayoutParams params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;


        mStockItem.setOnTouchListener(new HomescreenItemTouchListener(mWindowManager, params));

        mWindowManager.addView(mStockItem, params);
    }

    private void updateHomeStockView(String message) {
        final Stock stock = gson.fromJson(message, Stock.class);
        handler.post(new Runnable() {
            @Override
            public void run() {
                mStockItem.setStock(stock);
            }
        });
    }
}
