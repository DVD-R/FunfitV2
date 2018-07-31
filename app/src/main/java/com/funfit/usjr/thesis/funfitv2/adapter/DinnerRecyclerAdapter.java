package com.funfit.usjr.thesis.funfitv2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Meal;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by victor on 1/12/2016.
 */
public class DinnerRecyclerAdapter extends RecyclerView.Adapter<DinnerRecyclerAdapter.ViewHolder> {

    private List<Meal> mealList;
    private int size;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.foodNameTxt)
        TextView mFoodName;
        @BindView(R.id.rdiTxt)
        TextView mRdi;
        @BindView(R.id.kCalTxt)
        TextView mKCal;
        @BindView(R.id.search_serving_layout)
        LinearLayout mServingLayout;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public DinnerRecyclerAdapter(List<Meal> mealList) {
        this.mealList = mealList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_serving_adapter_layout, parent, false);
        context = parent.getContext();
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (Utils.getCluster(context).equals("impulse"))
            holder.mServingLayout.setBackgroundColor(context.getResources().getColor(R.color.filter_impulse));
        else
            holder.mServingLayout.setBackgroundColor(context.getResources().getColor(R.color.filter_velocity));
        holder.mFoodName.setText(mealList.get(position).getName());
        holder.mKCal.setText(String.valueOf(mealList.get(position).getCalories()) + " kcal");
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }
}