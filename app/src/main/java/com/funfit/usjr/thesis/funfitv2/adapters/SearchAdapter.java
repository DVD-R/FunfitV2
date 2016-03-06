package com.funfit.usjr.thesis.funfitv2.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private String food_id;
    private FatSecretGetPresenter fatSecretGetPresenter;
    private Context activityContext;
    private List<FoodServing> items;
    private String mealName;
    private String mealTime;
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
    public int getFoodId() {
        return Integer.parseInt(food_id);
    }

    @Override
    public Context getContext() {
        return activityContext;
    }

    @Override
    public void setList(List<FoodServing> items) {
        this.items = items;
        if (items != null) {
            fatSecretGetPresenter.saveMeal();
        }
    }

    @Override
    public List<FoodServing> getList() {
        return items;
    }

    @Override
    public String getMealName() {
        return mealName;
    }

    @Override
    public String getMealTime() {
        return mealTime;
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
                    food_id = foodList.get(position).getFood_id();
                    mealName = foodList.get(position).getFood_name();
                    fatSecretGetPresenter.searchFoodWithServings(true);
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

    public SearchAdapter(List<Food> foodList, Context activityContext, String mealTime){
        this.foodList = foodList;
        this.activityContext = activityContext;
        this.mealTime = mealTime;
        fatSecretGetPresenter = new FatSecretGetPresenter(this, activityContext);
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
                food_id = foodList.get(pos).getFood_id();
                mealName = foodList.get(pos).getFood_name();
                fatSecretGetPresenter.searchFoodWithServings(false);
                foodList.get(pos).setIsSelected(checkBox.isChecked());
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