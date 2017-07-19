package com.uottawa.interviewapp;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by filipslatinac on 2017-06-26.
 */

public class AnswersFragment extends android.support.v4.app.Fragment {
    private View rootView;

    Typeface sanFran;
    Typeface sanFranBolder;
    Typeface sanFranMedium;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.single_answer_list_element, container,
                false);

        sanFran = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SanFranciscoDisplay-Ultralight.otf");
        sanFranBolder = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SanFranciscoDisplay-Light.otf");
        sanFranMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SanFranciscoDisplay-Regular.otf");

        TextView answerId = (TextView) rootView.findViewById(R.id.answerIdText);
        answerId.setText("Answer " + (getArguments().getInt("AnswerID")+1));
        answerId.setTypeface(sanFran);

        TextView answerText = (TextView) rootView.findViewById(R.id.answerPreviewText);
        answerText.setText(getArguments().getString("Answer"));
        answerText.setTypeface(sanFranMedium);


        return rootView;
    }



}
