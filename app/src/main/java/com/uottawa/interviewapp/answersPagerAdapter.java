package com.uottawa.interviewapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by filipslatinac on 2017-06-27.
 */

public class answersPagerAdapter extends FragmentStatePagerAdapter {

    private String [] answers;

    public answersPagerAdapter(FragmentManager fm, String [] answers){
        super(fm);

        this.answers = answers;
    }


    @Override
    public Fragment getItem(int position) {
        AnswersFragment fragment = new AnswersFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Answer",answers[position]);
        bundle.putInt("AnswerID",position);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public int getCount() {
        return answers.length;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }



}
