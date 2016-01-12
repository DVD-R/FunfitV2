package com.funfit.usjr.thesis.funfitv2.searchFragment;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.adapter.SearchAdapter;
import com.funfit.usjr.thesis.funfitv2.adapter.SearchServingAdapter;
import com.funfit.usjr.thesis.funfitv2.fatSecretImplementation.FatSecretGetPresenter;
import com.funfit.usjr.thesis.funfitv2.listener.RecyclerItemClickListener;
import com.funfit.usjr.thesis.funfitv2.model.Food;
import com.funfit.usjr.thesis.funfitv2.model.FoodServing;
import com.funfit.usjr.thesis.funfitv2.views.ISearchFragmentView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by victor on 1/6/2016.
 */
public class SearchFragment extends Fragment implements ISearchFragmentView{

    private boolean mBroadcastInfoRegistered;
    private List<Food> foodList;
    private FatSecretGetPresenter fatSecretGetPresenter;
    private Long id;
    @Bind(R.id.recyclerview_list)RecyclerView mRecyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.bind(this, view);
        fatSecretGetPresenter = new FatSecretGetPresenter(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mBroadcastInfoRegistered){
            getActivity().registerReceiver(foodListReceiver, new IntentFilter(getString(R.string.broadcast_info)));
            mBroadcastInfoRegistered = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBroadcastInfoRegistered){
            getActivity().unregisterReceiver(foodListReceiver);
            mBroadcastInfoRegistered = false;
        }
    }

    private BroadcastReceiver foodListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                foodList = (List<Food>) intent.getExtras().getSerializable("list");
                populateFoodList();
            }catch (Exception e){
                Log.e("Broadcast Error", String.valueOf(e));
            }
        }
    };


    public void populateFoodList(){
        SearchAdapter searchAdapter = new SearchAdapter(foodList);
        mRecyclerView.setAdapter(searchAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                SearchServingAdapter searchAdapter = new SearchServingAdapter(7);
//                mRecyclerView.setAdapter(searchAdapter);
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//                mRecyclerView.setLayoutManager(linearLayoutManager);
                id = Long.parseLong(foodList.get(position).getFood_id());
                fatSecretGetPresenter.searchFoodWithServings();

            }
        }));
    }

    @Override
    public void setItem(List<FoodServing> items) {

    }

    @Override
    public Long getFoodId() {
        return id;
    }

    public static class SearchService extends Service{

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {return null;}

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            List<Food> list = (List<Food>) intent.getExtras().getSerializable("FoodList");
            Intent i = new Intent(getString(R.string.broadcast_info));
            i.putExtra("list", (Serializable) list);
            sendBroadcast(i);
            return START_NOT_STICKY;
        }
    }
}