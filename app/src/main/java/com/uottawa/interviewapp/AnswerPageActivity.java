package com.uottawa.interviewapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by filipslatinac on 2017-06-26.
 */

public class AnswerPageActivity extends AppCompatActivity {

    String[] answers;

    String question;
    String questionId;

    ViewPager answerPager;

    TextView answerQuestionId;
    TextView answerQuestion;
    TextView backButton;
    TextView nextAnswer;
    TextView previousAnswer;


    Typeface sanFran;
    Typeface sanFranBolder;
    Typeface sanFranMedium;
    Typeface fontAwesome;

    int numberOfAnswers;

    answersPagerAdapter answerAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_page);

        sanFran = Typeface.createFromAsset(getAssets(), "fonts/SanFranciscoDisplay-Ultralight.otf");
        sanFranBolder = Typeface.createFromAsset(getAssets(), "fonts/SanFranciscoDisplay-Heavy.otf");
        sanFranMedium = Typeface.createFromAsset(getAssets(), "fonts/SanFranciscoDisplay-Regular.otf");
        fontAwesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");



        Bundle packageReceived = getIntent().getExtras();
        answers = packageReceived.getStringArray("Answers");
        question = packageReceived.getString("Question");
        questionId = packageReceived.getString("QuestionID");


        answerQuestionId = (TextView) findViewById(R.id.questionIDPopUpAnswer);
        answerQuestionId.setText(questionId);
        answerQuestionId.setTypeface(sanFranMedium);
        answerQuestionId.setTextColor(Color.parseColor("#33AAFF"));

        answerQuestion = (TextView) findViewById(R.id.fullQuestionPopUpAnswer);
        answerQuestion.setText(question);
        answerQuestion.setTypeface(sanFranMedium);


        backButton = (TextView) findViewById(R.id.backButtonPopUpAnswer);
        backButton.setTypeface(fontAwesome);
        backButton.setText("\uf053");
        backButton.setTextSize(24);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        answerAdapter = new answersPagerAdapter(getSupportFragmentManager(),answers);

        answerPager = (ViewPager) findViewById(R.id.answersPopUp);
        answerPager.setAdapter(answerAdapter);


        if(answers.length < 1){
            answerPager = (ViewPager) findViewById(R.id.answersPopUp);
            answerPager.setAdapter(new emptyAdapter(getSupportFragmentManager()));

        }

        else{
            answerPager = (ViewPager) findViewById(R.id.answersPopUp);
            answerPager.setAdapter(answerAdapter);
        }

        numberOfAnswers = answerAdapter.getCount();

        nextAnswer = (TextView) findViewById(R.id.swipeForNextAnswer);
        nextAnswer.setTypeface(fontAwesome);
        nextAnswer.setText("\uf054");
        nextAnswer.setTextSize(22);

        previousAnswer = (TextView) findViewById(R.id.swipeForPreviousAnswer);
        previousAnswer.setTypeface(fontAwesome);
        previousAnswer.setText("\uf053");
        previousAnswer.setTextSize(22);


        nextAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerPager.setCurrentItem(answerPager.getCurrentItem()+1);
            }
        });

        previousAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerPager.setCurrentItem(answerPager.getCurrentItem()-1);
            }
        });

        previousAnswer.setText("");
        previousAnswer.setClickable(false);
        if (numberOfAnswers <= 1){
            nextAnswer.setText("");
            nextAnswer.setClickable(false);
        }


        answerPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        answerPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0 && position < numberOfAnswers-1){
                    previousAnswer.setText("");
                    previousAnswer.setClickable(false);
                    nextAnswer.setText("\uf054");
                    nextAnswer.setClickable(true);
                }

                else if(position > 0 && position < numberOfAnswers-1){
                    previousAnswer.setText("\uf053");
                    previousAnswer.setClickable(true);
                    nextAnswer.setText("\uf054");
                    nextAnswer.setClickable(true);
                }

                else if(position > 0 && position == numberOfAnswers-1){
                    previousAnswer.setText("\uf053");
                    previousAnswer.setClickable(true);
                    nextAnswer.setText("");
                    nextAnswer.setClickable(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class emptyAdapter extends FragmentStatePagerAdapter {

        public emptyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment frag = new emptyFragment();
            Bundle bundle = new Bundle();
            bundle.putString("String","No Answers");
            frag.setArguments(bundle);
            return frag;
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
}