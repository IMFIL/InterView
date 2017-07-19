package com.uottawa.interviewapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

/**
 * Created by filipslatinac on 2017-07-16.
 */

class QuestionsFragment extends Fragment {

    View rootView;
    ListView questionPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){

        rootView = inflater.inflate(R.layout.questions_list_view, container,
                false);
        questionPager = (ListView) rootView.findViewById(R.id.questionsAvailable);
        questionPager.setAdapter(new QuestionPostAdapter((QuestionPageActivity)getActivity(),getArguments().getStringArray("Questions"),(QuestionPost []) getArguments().getSerializable("QuestionPosts")));

        return rootView;

    }

}
