package com.funfit.usjr.thesis.funfitv2.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Territory;

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
        List<Territory> listTerritory = (List<Territory>) intent.getSerializableExtra("RESPONSETERRITORY");
        Intent i = new Intent(getString(R.string.broadcast_encodedpolyline));
        i.putExtra("encodedPolyLine", (Serializable) listTerritory);
        sendBroadcast(i);
        return START_NOT_STICKY;
    }
}
