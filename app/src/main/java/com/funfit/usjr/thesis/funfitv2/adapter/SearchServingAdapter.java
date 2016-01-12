package com.funfit.usjr.thesis.funfitv2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Food;
import com.funfit.usjr.thesis.funfitv2.model.FoodServing;

import java.util.List;


/**
 * Created by victor on 1/12/2016.
 */
public class SearchServingAdapter extends RecyclerView.Adapter<SearchServingAdapter.ViewHolder>{

    private List<FoodServing> foodList;
    private int size;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_serving_adapter_layout, parent, false);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    public SearchServingAdapter(int size){
        this.size = size;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return size;
    }
}
