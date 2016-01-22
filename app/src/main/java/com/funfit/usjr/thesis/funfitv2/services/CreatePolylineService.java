package com.funfit.usjr.thesis.funfitv2.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.R;

import java.io.Serializable;
import java.util.List;

/**
 * Created by victor on 1/23/2016.
 */
public class CreatePolylineService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<String> encodedPolyLine = (List<String>) intent.getSerializableExtra("ENCODEDPOLYLINE");
        Intent i = new Intent(getString(R.string.broadcast_encodedpolyline));
        i.putExtra("encodedPolyLine", (Serializable) encodedPolyLine);
        sendBroadcast(i);
        return START_NOT_STICKY;
    }
}
