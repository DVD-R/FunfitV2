package com.funfit.usjr.thesis.funfitv2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.fragmentDialog.FoodInfoFragment;
import com.funfit.usjr.thesis.funfitv2.model.Food;
import com.funfit.usjr.thesis.funfitv2.model.FoodServing;
import com.funfit.usjr.thesis.funfitv2.views.ISearchFragmentView;

import java.util.List;


/**
 * Created by victor on 1/12/2016.
 */
public class SearchServingAdapter extends RecyclerView.Adapter<SearchServingAdapter.ViewHolder>{

    private List<FoodServing> foodList;
    private int size;
    Context context;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(final View itemView) {
            super(itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_serving_adapter_layout, parent, false);
        context = parent.getContext();
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
