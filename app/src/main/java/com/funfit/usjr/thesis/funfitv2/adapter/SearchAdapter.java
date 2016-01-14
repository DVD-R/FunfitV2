package com.funfit.usjr.thesis.funfitv2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Food;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by victor on 1/12/2016.
 */
public class SearchAdapter  extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private List<Food> foodList;
//    private int size;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.foodNameTxt)TextView foodNameTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_adapter_layout, parent, false);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    public SearchAdapter(List<Food> foodList){
        this.foodList = foodList;
    }


//    public SearchAdapter(int size){
//        this.size = size;
//    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.foodNameTxt.setText(foodList.get(position).getFood_name());
    }

    @Override
    public int getItemCount() {

        return foodList.size();
//    return size;
    }

}
