package com.funfit.usjr.thesis.funfitv2.history.StaticData;

/**
 * Created by ocabafox on 1/8/2016.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.funfit.usjr.thesis.funfitv2.R;

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {
    private ItemData[] itemsData;

    public myAdapter(ItemData[] itemsData) {
        this.itemsData = itemsData;
    }

    @Override
    public myAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);


        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {


        viewHolder.txtViewUsername.setText(itemsData[position].getUsername());
        viewHolder.txtViewEvent.setText(itemsData[position].getEvent());
        viewHolder.txtViewArea.setText(itemsData[position].getArea());
        viewHolder.txtViewTimeDate.setText(itemsData[position].getTimeDate());

        viewHolder.giveMeThat(itemsData[position].getUsername(),
                itemsData[position].getEvent(),
                itemsData[position].getArea(),
                itemsData[position].getTimeDate());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtViewUsername;
        public TextView txtViewEvent;
        public TextView txtViewArea;
        public TextView txtViewTimeDate;

        String user;
        String event;
        String area;
        String timeDate;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewUsername = (TextView) itemLayoutView.findViewById(R.id.item_user);
            txtViewEvent = (TextView) itemLayoutView.findViewById(R.id.item_event);
            txtViewArea = (TextView) itemLayoutView.findViewById(R.id.item_area);
            txtViewTimeDate = (TextView) itemLayoutView.findViewById(R.id.item_timeDate);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        public void giveMeThat(String user, String event, String area, String timeDate) {
            this.user = user;
            this.event = event;
            this.area = area;
            this.timeDate = timeDate;
        }
    }

    @Override
    public int getItemCount() {
        return itemsData.length;
    }
}
