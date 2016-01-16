package com.funfit.usjr.thesis.funfitv2.history;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Toast;

import com.funfit.usjr.thesis.funfitv2.adapters.HistoryAdapter;
import com.funfit.usjr.thesis.funfitv2.model.HistoryEventCoordinates;

import java.util.Arrays;
import java.util.ArrayList;

/**
 * Created by victor on 1/17/2016.
 */
public class EventHistoryActivityImpl extends EventHistoryActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);

        mHistoryRecycler.setHasFixedSize(true);
    }

    @Override
    protected HistoryAdapter createHistoryListAdapter() {
        ArrayList<HistoryEventCoordinates> historyEventCoordinates = new ArrayList<>(EVENT_HISTORY_DUMMY_LOCATIONS.length);
        historyEventCoordinates.addAll(Arrays.asList(EVENT_HISTORY_DUMMY_LOCATIONS));

        HistoryAdapter historyAdapter = new HistoryAdapter();
        historyAdapter.setHistoryEventLocations(historyEventCoordinates);

        return historyAdapter;
    }

    @Override
    public void showMapDetails(View view) {
        HistoryEventCoordinates historyEventCoordinates = (HistoryEventCoordinates) view.getTag();
        Toast.makeText(this, historyEventCoordinates.center.latitude +" "+ historyEventCoordinates.center.longitude, Toast.LENGTH_LONG).show();
    }


    private static final HistoryEventCoordinates[] EVENT_HISTORY_DUMMY_LOCATIONS = new HistoryEventCoordinates[]{
            new HistoryEventCoordinates("Sto. Nino", 10.2942318,123.898417),
            new HistoryEventCoordinates("Osmena Blvd.", 10.3007793,123.8942264),
            new HistoryEventCoordinates("IT Park", 10.3275433,123.9029863),
            new HistoryEventCoordinates("USJ-R Basak", 10.2898013,123.8594703)
    };
}
