package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.model.WeeklyCal;
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

    static List<WeeklyCal> mList;

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
                    try {
                        Intent i = new Intent(context, WeeklyGraphActivity.class);
                        if(getPosition() == 0)
                            i.putExtra(Constants.IS_FIRST, true);
                        else
                            i.putExtra(Constants.IS_FIRST, false);

                        i.putExtra(Constants.START_DAY, Utils.getDay(mList.get(getPosition()).getStartDate()));
                        i.putExtra(Constants.END_DAY, Utils.getDay(mList.get(getPosition()).getEndDate()));
                        i.putExtra(Constants.MONTH, Utils.getMonth(mList.get(getPosition()).getEndDate()));
                        i.putExtra(Constants.CAL_CONSUMED, Utils.roundOneDecimal(mList.get(getPosition()).getConsumedCalories()));
                        i.putExtra(Constants.CAL_BURNED, Utils.roundOneDecimal(mList.get(getPosition()).getBurnedCalories()));
                        i.putExtra(Constants.CONSUMED_TIME, mList.get(getPosition()).getWeeklyConsumedDay());
                        i.putExtra(Constants.BURNED_TIME, mList.get(getPosition()).getWeeklyBurnedDay());
                        i.putExtra(Constants.CONSUMED_VALUE, mList.get(getPosition()).getWeeklyConsumedValue());
                        i.putExtra(Constants.BURNED_VALUE, mList.get(getPosition()).getWeeklyBurnedValue());

                        context.startActivity(i);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
            context = v.getContext();
            ButterKnife.bind(this, v);
        }
    }

    public WeeklyAdapter(List<WeeklyCal> list) {
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
            viewHolder.mTextStart.setText(Utils.getDay(mList.get(position).getStartDate()));
            viewHolder.mTextEnd.setText(Utils.getDay(mList.get(position).getEndDate()));
            viewHolder.mTextMonth.setText(Utils.getMonth(mList.get(position).getEndDate()));

            viewHolder.mTextCalConsumed.setText("Calories Consumed this week: " +
                    Utils.roundOneDecimal(mList.get(position).getConsumedCalories()) + "");
            viewHolder.mTextCalBurned.setText("Calories Burned this week: " +
                    Utils.roundOneDecimal(mList.get(position).getBurnedCalories()) + "");

        } catch (ParseException e) {
            Log.e(LOG_TAG, "Parse Exception");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}