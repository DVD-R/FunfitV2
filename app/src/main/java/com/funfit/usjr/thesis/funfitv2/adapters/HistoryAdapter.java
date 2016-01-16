package com.funfit.usjr.thesis.funfitv2.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.HistoryEventCoordinates;
import com.google.android.gms.maps.MapView;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by victor on 1/16/2016.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder>{

    protected HashSet<MapView> mMapViews = new HashSet<>();
    protected ArrayList<HistoryEventCoordinates> mHistoryEventCoordinates;

    public void setHistoryEventLocations(ArrayList<HistoryEventCoordinates> historyEventCoordinates){
        mHistoryEventCoordinates = historyEventCoordinates;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_row, parent, false);
        HistoryViewHolder historyViewHolder = new HistoryViewHolder(parent.getContext(), view);

        mMapViews.add(historyViewHolder.mHistoryMapView);
        return historyViewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        HistoryEventCoordinates historyEventCoordinates = mHistoryEventCoordinates.get(position);

        holder.itemView.setTag(historyEventCoordinates);
        holder.mTitle.setText(historyEventCoordinates.name);
        holder.mDescription.setText(historyEventCoordinates.center.latitude + " " + historyEventCoordinates.center.longitude);

        holder.setHistoryEventLocation(historyEventCoordinates);

    }

    @Override
    public int getItemCount() {
        return mHistoryEventCoordinates == null ? 0: mHistoryEventCoordinates.size();
    }

    public HashSet<MapView> getmMapViews(){
        return mMapViews;
    }
}