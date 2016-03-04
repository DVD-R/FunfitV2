package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Monthly;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;

import java.text.ParseException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dj on 3/4/2016.
 */
public class MonthlyAdapter extends RecyclerView.Adapter<MonthlyAdapter.ViewHolder> {
    private static final String LOG_TAG = MonthlyAdapter.class.getSimpleName();

    List<Monthly> mList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.text_month)
        TextView mTextMonth;
        @Bind(R.id.text_year)
        TextView mTextYear;
        @Bind(R.id.text_cal_consumed)
        TextView mTextCalConsumed;
        @Bind(R.id.text_cal_burned)
        TextView mTextCalBurned;
        @Bind(R.id.layout_monthly)
        RelativeLayout mLayoutMonthly;
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

    public MonthlyAdapter(List<Monthly> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewGroup instanceof RecyclerView) {
            int layoutId = R.layout.list_item_monthly;

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            view.setFocusable(true);
            return new ViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (position == 0) {
            viewHolder.mTextMonth.setTextColor(Color.parseColor("#FFFFFF"));
            viewHolder.mTextYear.setTextColor(Color.parseColor("#FFFFFF"));
            viewHolder.mTextCalConsumed.setTextColor(Color.parseColor("#FFFFFF"));
            viewHolder.mTextCalBurned.setTextColor(Color.parseColor("#FFFFFF"));
            if (Utils.getCluster(viewHolder.context).equals("velocity"))
                viewHolder.mLayoutMonthly.setBackgroundColor(Color.parseColor("#2980b9"));
            else
                viewHolder.mLayoutMonthly.setBackgroundColor(Color.parseColor("#c0392b"));
        }
        viewHolder.mTextMonth.setText(mList.get(position).getMonth());
        viewHolder.mTextYear.setText(mList.get(position).getYear() + "");

        viewHolder.mTextCalConsumed.setText("Calories Consumed this month: " +
                Utils.roundOneDecimal(mList.get(position).getConsumedCalories()) + "");
        viewHolder.mTextCalBurned.setText("Calories Burned this month: " +
                Utils.roundOneDecimal(mList.get(position).getBurnedCalories()) + "");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}