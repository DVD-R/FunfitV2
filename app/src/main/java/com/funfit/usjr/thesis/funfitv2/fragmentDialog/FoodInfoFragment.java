package com.funfit.usjr.thesis.funfitv2.fragmentDialog;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.funfit.usjr.thesis.funfitv2.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by victor on 1/13/2016.
 */
public class FoodInfoFragment extends DialogFragment implements  DatePickerDialog.OnDateSetListener{

    @Bind(R.id.schedDateBtn)Button mScheduleDate;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.food_info_layout, null);

        builder.setView(dialogView);
        ButterKnife.bind(this, dialogView);

        Calendar nowDate = Calendar.getInstance();
        String stringDate = nowDate.get(Calendar.DAY_OF_MONTH)+"/"+nowDate.get(Calendar.MONTH)+"/"+nowDate.get(Calendar.YEAR);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date currentDate = dateFormat.parse(stringDate);
            mScheduleDate.setText(currentDate.toString());
        } catch (ParseException e) {
            Log.e("Parse Error", String.valueOf(e));
        }
        return builder.create();
    }

    @OnClick(R.id.schedDateBtn)
    public void scheduleDate(){
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