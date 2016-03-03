package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Weekly;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;

import java.text.ParseException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dj on 3/4/2016.
 */
public class WeeklyAdapter extends RecyclerView.Adapter<WeeklyAdapter.ViewHolder> {
    private static final String LOG_TAG = WeeklyAdapter.class.getSimpleName();

    List<Weekly> mList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.text_start)
        TextView mTextStart;
        @Bind(R.id.text_end)
        TextView mTextEnd;
        @Bind(R.id.text_month)
        TextView mTextMonth;
        @Bind(R.id.text_cal_consumed)
        TextView mTextCalConsumed;
        @Bind(R.id.text_cal_burned)
        TextView mTextCalBurned;
        @Bind(R.id.layout_weekly)
        RelativeLayout mLayoutWeekly;
        Context context;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "Element " + getPosition() + " clicked.");
                }
            });
            context = v.getContext();
            ButterKnife.bind(this, v);
        }
    }

    public WeeklyAdapter(List<Weekly> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewGroup instanceof RecyclerView) {
            int layoutId = R.layout.list_item_weekly;

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            view.setFocusable(true);
            return new ViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        try {
            if(position == 0){
                viewHolder.mTextStart.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.mTextEnd.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.mTextMonth.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.mTextCalConsumed.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.mTextCalBurned.setTextColor(Color.parseColor("#FFFFFF"));
                if(Utils.getCluster(viewHolder.context).equals("velocity"))
                    viewHolder.mLayoutWeekly.setBackgroundColor(Color.parseColor("#2980b9"));
                else
                    viewHolder.mLayoutWeekly.setBackgroundColor(Color.parseColor("#c0392b"));
            }
            viewHolder.mTextStart.setText(Utils.getDay(mList.get(position).getStartDate()));
            viewHolder.mTextEnd.setText(Utils.getDay(mList.get(position).getStartDate()));
            viewHolder.mTextMonth.setText(Utils.getMonth(mList.get(position).getEndDate()));
            viewHolder.mTextCalConsumed.setText(mList.get(position).getConsumedCalories());
            viewHolder.mTextCalBurned.setText(mList.get(position).getBurnedCalories());

            Log.v(LOG_TAG,Utils.getDay(mList.get(position).getStartDate()));
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Parse Exception");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}