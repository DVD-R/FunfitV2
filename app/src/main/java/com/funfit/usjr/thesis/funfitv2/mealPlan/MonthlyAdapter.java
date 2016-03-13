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
import com.funfit.usjr.thesis.funfitv2.model.Monthly;
import com.funfit.usjr.thesis.funfitv2.model.MonthlyCal;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dj on 3/4/2016.
 */
public class MonthlyAdapter extends RecyclerView.Adapter<MonthlyAdapter.ViewHolder> {
    private static final String LOG_TAG = MonthlyAdapter.class.getSimpleName();

    private static List<MonthlyCal> mList;
    private static double rmi;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.text_month)
        TextView mTextMonth;
        @Bind(R.id.text_year)
        TextView mTextYear;
        @Bind(R.id.text_cal_consumed)
        TextView mTextCalConsumed;
        @Bind(R.id.text_cal_burned)
        TextView mTextCalBurned;
        @Bind(R.id.text_rdi)
        TextView mTextRdi;
        @Bind(R.id.layout_monthly)
        RelativeLayout mLayoutMonthly;
        Context context;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, MonthlyGraphActivity.class);
                    if (getPosition() == 0)
                        i.putExtra(Constants.IS_FIRST, true);
                    else
                        i.putExtra(Constants.IS_FIRST, false);

                    i.putExtra(Constants.MONTH, mList.get(getPosition()).getMonth());
                    i.putExtra(Constants.YEAR, mList.get(getPosition()).getYear());
                    i.putExtra(Constants.CAL_CONSUMED, Utils.roundOneDecimal(mList.get(getPosition()).getConsumedCalories()));
                    i.putExtra(Constants.CAL_BURNED, Utils.roundOneDecimal(mList.get(getPosition()).getBurnedCalories()));
                    i.putExtra(Constants.CONSUMED_TIME, mList.get(getPosition()).getMonthlyConsumedWeek());
                    i.putExtra(Constants.BURNED_TIME, mList.get(getPosition()).getMonthlyBurnedWeek());
                    i.putExtra(Constants.CONSUMED_VALUE, mList.get(getPosition()).getMonthlyConsumedValue());
                    i.putExtra(Constants.BURNED_VALUE, mList.get(getPosition()).getMonthlyBurnedValue());
                    i.putExtra(Constants.RDI_LAPSE, rmi);

                    context.startActivity(i);
                }
            });
            context = v.getContext();
            ButterKnife.bind(this, v);
        }
    }

    public MonthlyAdapter(List<MonthlyCal> list, double rdi) throws ParseException {
        mList = list;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Utils.getYearNumber(list.get(0).getMonth() + "-" + list.get(0).getYear()));
        calendar.set(Calendar.MONTH, Utils.getMonthNumber(list.get(0).getMonth() + "-" + list.get(0).getYear()));
        int daysQty = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        rmi = rdi * daysQty;
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
        double cal = mList.get(position).getConsumedCalories();
        viewHolder.mTextMonth.setText(mList.get(position).getMonth());
        viewHolder.mTextYear.setText(mList.get(position).getYear() + "");

        viewHolder.mTextCalConsumed.setText("Calories Consumed this month: " +
                Utils.roundOneDecimal(mList.get(position).getConsumedCalories()) + "");
        viewHolder.mTextCalBurned.setText("Calories Burned this month: " +
                Utils.roundOneDecimal(mList.get(position).getBurnedCalories()) + "");

        if (rmi > cal) {
            viewHolder.mTextRdi.setText(Utils.roundOneDecimal(rmi - cal) +
                    " Calories lacking");
            viewHolder.mTextRdi.setTextColor(Color.parseColor("#d32f2f"));
        }
        else {
            viewHolder.mTextRdi.setText(Math.abs(Utils.roundOneDecimal(rmi - cal)) +
                    " Calories exceeded");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}