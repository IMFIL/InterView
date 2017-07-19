package com.uottawa.interviewapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by filipslatinac on 2017-07-18.
 */

class emptyResourceFragment extends Fragment {
    View rootView;
    ViewGroup container;
    LayoutInflater inflater;

    Typeface fontAwesome;

    TextView nothingHereText;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){
        this.inflater = inflater;

        this.container = container;
        rootView = inflater.inflate(R.layout.empty_resource, container,
                false);

        fontAwesome = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");

        nothingHereText = (TextView) rootView.findViewById(R.id.noSuchResource);
        nothingHereText.setTypeface(fontAwesome);

        return rootView;

    }
}
