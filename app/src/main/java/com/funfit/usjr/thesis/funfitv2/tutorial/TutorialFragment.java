package com.funfit.usjr.thesis.funfitv2.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.funfit.usjr.thesis.funfitv2.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ocabafox on 1/15/2016.
 */
public class TutorialFragment extends Fragment {

//    @Bind(R.id.frogName)
//    TextView name;
//    @Bind(R.id.frogDescription)
//    TextView desc;
//    @Bind(R.id.frogImage)
//    TextView img;

    public final static String TUTORIAL_NAME = "tutorial name";
    public final static String TUTORIAL_DESCRIPTION = "tutorial description";
    public final static String TUTORIAL_IMAGE = "tutorial image";

    public TutorialFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        //ButterKnife.bind(getActivity());
        Bundle args = getArguments();
        if(args != null){
            setViews(view, args);
        }
        return view;
    }

    private void setViews(View view, Bundle args) {
        TextView frogName = (TextView) view.findViewById(R.id.frogName);
        TextView frogDescription = (TextView) view.findViewById(R.id.frogDescription);
        ImageView frogImage = (ImageView) view.findViewById(R.id.frogImage);

        frogName.setText(args.getString(TUTORIAL_NAME));
        frogDescription.setText(args.getString(TUTORIAL_DESCRIPTION));
        frogImage.setImageResource(args.getInt(TUTORIAL_IMAGE));
    }

}
