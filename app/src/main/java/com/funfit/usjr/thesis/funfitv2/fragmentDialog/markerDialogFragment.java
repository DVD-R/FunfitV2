package com.funfit.usjr.thesis.funfitv2.fragmentDialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.funfit.usjr.thesis.funfitv2.R;

import java.util.zip.Inflater;

/**
 * Created by ocabafox on 2/7/2016.
 */
public class markerDialogFragment extends DialogFragment {

    Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marker_dialog, container, false);

        button = (Button) view.findViewById(R.id.btnInvade);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        getDialog().setTitle("Marker Title");
        return view;
    }
}
