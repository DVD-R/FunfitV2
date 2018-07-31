package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.DailyCal;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dj on 3/4/2016.
 */
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {
    private static final String LOG_TAG = WeeklyAdapter.class.getSimpleName();

    static List<DailyCal> mList;
    double rdi;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_start)
        TextView mTextStart;
        @BindView(R.id.text_end)
        TextView mTextEnd;
        @BindView(R.id.text_month)
        TextView mTextMonth;
        @BindView(R.id.text_cal_consumed)
        TextView mTextCalConsumed;
        @BindView(R.id.text_cal_burned)
        TextView mTextCalBurned;
        @BindView(R.id.text_rdi)
        TextView mTextRdi;
        @BindView(R.id.layout_weekly)
        RelativeLayout mLayoutWeekly;
        Context context;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            context = v.getContext();
            ButterKnife.bind(this, v);
        }
    }

    public DailyAdapter(List<DailyCal> list, double rdi) {
        mList = list;
        this.rdi = rdi;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewGroup instanceof RecyclerView) {
            int layoutId = R.layout.list_item_daily;

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            view.setFocusable(true);
            return new ViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        double cal = mList.get(position).getConsumedCalories();

        viewHolder.mTextStart.setText(mList.get(position).getDay());
        viewHolder.mTextEnd.setText("");
        viewHolder.mTextMonth.setText(mList.get(position).getMonth());

        viewHolder.mTextCalConsumed.setText("Calories Consumed this day: " +
                Utils.roundOneDecimal(mList.get(position).getConsumedCalories()) + "");
        viewHolder.mTextCalBurned.setText("Calories Burned this day: " +
                Utils.roundOneDecimal(mList.get(position).getBurnedCalories()) + "");
        if (rdi > cal) {
            viewHolder.mTextRdi.setText(Utils.roundOneDecimal(rdi - cal) +
                    " Calories lacking");
            viewHolder.mTextRdi.setTextColor(Color.parseColor("#d32f2f"));
        } else {
            viewHolder.mTextRdi.setText(Math.abs(Utils.roundOneDecimal(rdi - cal)) +
                    " Calories exceeded");
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}