package com.funfit.usjr.thesis.funfitv2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.FoodServing;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by victor on 1/12/2016.
 */
public class SearchServingAdapter extends RecyclerView.Adapter<SearchServingAdapter.ViewHolder> {

    private List<FoodServing> foodList;
    private int size;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.foodNameTxt)
        TextView mFoodName;
        @BindView(R.id.kCalTxt)
        TextView mKCal;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public SearchServingAdapter(String foodName, List<FoodServing> foodList) {
        this.foodList = foodList;
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
        holder.mFoodName.setText(foodList.get(position).getMeasurement_description());
        Log.i("Measurement", foodList.get(1).getMeasurement_description());
        Log.i("Calories", foodList.get(1).getCalories());
        Log.i("Carb", foodList.get(1).getCarbohydrate());
        Log.i("Protein", foodList.get(1).getProtein());
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}