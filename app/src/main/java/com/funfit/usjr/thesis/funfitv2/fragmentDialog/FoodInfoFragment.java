package com.funfit.usjr.thesis.funfitv2.fragmentDialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.fatSecretImplementation.FoodInfoPresenter;
import com.funfit.usjr.thesis.funfitv2.model.FoodServing;
import com.funfit.usjr.thesis.funfitv2.views.IFoodInfoView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by victor on 1/13/2016.
 */
public class FoodInfoFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, IFoodInfoView {

    @BindView(R.id.schedDateBtn)
    Button mScheduleDate;
    @BindView(R.id.foodNameTxt)
    TextView mFoodName;
    @BindView(R.id.servingSizeEdt)
    EditText mServingSize;
    @BindView(R.id.servingSpnr)
    Spinner mServingDescription;
    @BindView(R.id.calSizeTxt)
    TextView mCalSize;
    @BindView(R.id.fatSizeTxt)
    TextView mFatSize;
    @BindView(R.id.carbSizeTxt)
    TextView mCarbSize;
    @BindView(R.id.proteinSizeTxt)
    TextView mProteinSize;
    private boolean mBroadcastInfoRegistered;
    private List<FoodServing> foodInfoList;
    private FoodInfoPresenter foodInfoPresenter;
    private List<String> mSpinnerServingItems;
    private Activity activity;
    private int mPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.food_info_layout, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        ButterKnife.bind(this, view);
        activity = getActivity();
        foodInfoPresenter = new FoodInfoPresenter(this);

        Calendar nowDate = Calendar.getInstance();
        String stringDate = nowDate.get(Calendar.DAY_OF_MONTH) + "/" + nowDate.get(Calendar.MONTH) + "/" + nowDate.get(Calendar.YEAR);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date currentDate = dateFormat.parse(stringDate);
            mScheduleDate.setText(currentDate.toString());
        } catch (ParseException e) {
            Log.e("Parse Error", String.valueOf(e));
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mBroadcastInfoRegistered) {
            activity.registerReceiver(foodInfoListReceiver, new IntentFilter(getString(R.string.broadcastInfo)));
            mBroadcastInfoRegistered = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBroadcastInfoRegistered) {
            getActivity().unregisterReceiver(foodInfoListReceiver);
            mBroadcastInfoRegistered = false;
        }
    }

    @Override
    public void populateViews() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, mSpinnerServingItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mServingDescription.setAdapter(adapter);
        mServingSize.setText(foodInfoList.get(0).getNumber_of_units().substring(0, 1));
        mCalSize.setText(foodInfoList.get(0).getCalories());
        mFatSize.setText(foodInfoList.get(0).getFat());
        mCarbSize.setText(foodInfoList.get(0).getCarbohydrate());
        mProteinSize.setText(foodInfoList.get(0).getProtein());
    }

    @Override
    public void setUpListener() {
        mServingDescription.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                foodInfoPresenter.updateNutritionInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void setSpinnerItem(List<String> items) {
        mSpinnerServingItems = items;
    }

    @Override
    public List<FoodServing> getFoodInfoList() {
        return foodInfoList;
    }

    @Override
    public void updateNutritionInfo(int position) {
        mServingSize.setText(foodInfoList.get(position).getNumber_of_units().substring(0, 1));
        mCalSize.setText(foodInfoList.get(position).getCalories());
        mFatSize.setText(foodInfoList.get(position).getFat());
        mCarbSize.setText(foodInfoList.get(position).getCarbohydrate());
        mProteinSize.setText(foodInfoList.get(position).getProtein());
    }

    @Override
    public int getPosition() {
        return mPosition;
    }


    private BroadcastReceiver foodInfoListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                foodInfoList = (List<FoodServing>) intent.getExtras().getSerializable("broadcastList");
                Log.i("Serving Description", foodInfoList.get(0).getMeasurement_description());
                foodInfoPresenter.onResume();
            } catch (Exception e) {
                Log.e("Broadcast Error", String.valueOf(e));
            }
        }
    };

    @OnClick(R.id.close_imgBtn)
    public void close() {
        dismiss();
    }

    @OnClick(R.id.schedDateBtn)
    public void scheduleDate() {
        Calendar nowDate = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                nowDate.get(Calendar.YEAR),
                nowDate.get(Calendar.MONTH),
                nowDate.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setThemeDark(true);
        dpd.vibrate(true);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }
}