package com.funfit.usjr.thesis.funfitv2.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.fatSecretImplementation.FatSecretGetPresenter;
import com.funfit.usjr.thesis.funfitv2.fragmentDialog.FoodInfoFragment;
import com.funfit.usjr.thesis.funfitv2.model.Food;
import com.funfit.usjr.thesis.funfitv2.model.FoodServing;
import com.funfit.usjr.thesis.funfitv2.services.FoodInfoService;
import com.funfit.usjr.thesis.funfitv2.views.ISearchAdapterView;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by victor on 1/12/2016.
 */
public class SearchAdapter  extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements ISearchAdapterView{

    private List<Food> foodList;
    private Intent intent;
    private Context context;
    private int position;
    private FatSecretGetPresenter fatSecretGetPresenter;
    @Override
    public void sendList(List<FoodServing> items) {
        android.app.FragmentManager manager = ((Activity) context).getFragmentManager();
        FoodInfoFragment foodInfoFragment = new FoodInfoFragment();
        foodInfoFragment.show(manager, "Food Info");
        intent = new Intent(context, FoodInfoService.class);
        intent.putExtra("FoodInfoList", (Serializable) items);
        context.startService(intent);
    }

    @Override
    public Long getFoodId() {
        return Long.parseLong(foodList.get(position).getFood_id());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.foodNameTxt)TextView foodNameTxt;
        @Bind(R.id.selectChkBox)CheckBox mSelectChk;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getPosition();
                    fatSecretGetPresenter.searchFoodWithServings();
                }
            });
            ButterKnife.bind(this,itemView);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_adapter_layout, parent, false);
        view.setFocusable(true);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    public SearchAdapter(List<Food> foodList){
        this.foodList = foodList;
        fatSecretGetPresenter = new FatSecretGetPresenter(this);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final int pos = position;

        holder.foodNameTxt.setText(foodList.get(position).getFood_name());

        holder.mSelectChk.setChecked(foodList.get(position).isSelected());
        holder.mSelectChk.setTag(foodList.get(position));

        holder.mSelectChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                Food food = (Food) checkBox.getTag();

                food.setIsSelected(checkBox.isChecked());
                foodList.get(pos).setIsSelected(checkBox.isChecked());
//                Toast.makeText(v.getContext(), food.getFood_name(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return foodList.size();
        }catch (Exception e){
            return 0;
        }
   }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public List<Food> getFoodList(){
        return foodList;
    }

}