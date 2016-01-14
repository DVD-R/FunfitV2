package com.funfit.usjr.thesis.funfitv2.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Food;

import java.io.Serializable;
import java.util.List;

/**
 * Created by victor on 1/14/2016.
 */
public class FoodInfoService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<Food> list = (List<Food>) intent.getExtras().getSerializable("FoodInfoList");
        Intent i = new Intent(getString(R.string.broadcastInfo));
        i.putExtra("broadcastList", (Serializable) list);
        sendBroadcast(i);
        return START_NOT_STICKY;
    }
}