package com.funfit.usjr.thesis.funfitv2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funfit.usjr.thesis.funfitv2.R;


/**
 * Created by victor on 1/12/2016.
 */
public class SearchAdapter  extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private int size;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_adapter_layout, parent, false);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    public SearchAdapter(int size){
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
