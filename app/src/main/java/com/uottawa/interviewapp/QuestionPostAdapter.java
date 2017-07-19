package com.uottawa.interviewapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by filipslatinac on 2017-06-26.
 */

public class QuestionPostAdapter extends ArrayAdapter<String> {


    private final Activity context;

    private QuestionPost [] questionPosts;
    private String [] questionPreviews;

    Typeface sanFran;
    Typeface sanFranBolder;
    Typeface sanFranMedium;
    Typeface fontAwesome;

    JSONObject answerResults;



    public QuestionPostAdapter(Activity context, String [] posts, QuestionPost [] questionsPosts) {
        super(context, R.layout.single_question_list_element,posts);

        questionPreviews = posts;
        this.questionPosts = questionsPosts;
        this.context = context;

        sanFran = Typeface.createFromAsset(context.getAssets(), "fonts/SanFranciscoDisplay-Ultralight.otf");
        sanFranBolder = Typeface.createFromAsset(context.getAssets(), "fonts/SanFranciscoDisplay-Heavy.otf");
        sanFranMedium = Typeface.createFromAsset(context.getAssets(), "fonts/SanFranciscoDisplay-Regular.otf");
        fontAwesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");



    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView=inflater.inflate(R.layout.single_question_list_element, null, true);
        rowView.setTag(new Integer(position));

        TextView questionId = (TextView) rowView.findViewById(R.id.questionIdText);
        questionId.setText("Question " + String.valueOf(position + 1));
        questionId.setTypeface(sanFranMedium);
        questionId.setTextColor(Color.parseColor("#33AAFF"));

        TextView questionText = (TextView) rowView.findViewById(R.id.questionPreviewText);
        questionText.setText(questionPreviews[position]);
        questionText.setTypeface(sanFranMedium);
        questionText.setTextColor(Color.BLACK);


        TextView fullQuestionText = (TextView) rowView.findViewById(R.id.fullQuestionText);

        if (questionPreviews[position].length() > 60){
            fullQuestionText.setText("See Full Question");
            fullQuestionText.setTypeface(sanFran);
            fullQuestionText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideShowQuestion(rowView);
                }
            });
        }

        else{
            fullQuestionText.setText("");
        }

        TextView seeAnswersText = (TextView) rowView.findViewById(R.id.seeAnswersText);
        seeAnswersText.setText("See Answers");
        seeAnswersText.setTypeface(sanFran);
        seeAnswersText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seeAnswers(rowView);
            }
        });


        return rowView;

    };

    private void hideShowQuestion(View view){

       int i = (Integer)view.getTag();

        TextView questionId;
        TextView fullQuestion;
        TextView backButton;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        final PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.full_question_window, null, true),
                (int) (width * 1), (int) (height * 1), true);

        questionId = (TextView) pw.getContentView().findViewById(R.id.questionIDPopUp);
        questionId.setText(((TextView) view.findViewById(R.id.questionIdText)).getText().toString());
        questionId.setTypeface(sanFranMedium);
        questionId.setTextColor(Color.parseColor("#33AAFF"));


        fullQuestion = (TextView) pw.getContentView().findViewById(R.id.fullQuestionPopUp);
        fullQuestion.setText(questionPosts[i].getQuestion());
        fullQuestion.setTypeface(sanFranMedium);


        backButton = (TextView) pw.getContentView().findViewById(R.id.backButtonPopUpFullQuestion);
        backButton.setTypeface(fontAwesome);
        backButton.setText("\uf053");
        backButton.setTextSize(24);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });

        pw.showAtLocation(context.findViewById(R.id.questionsAvailable), Gravity.CENTER, 0, 0);





//        TextView questionText = (TextView) view.findViewById(R.id.questionPreviewText);
//        TextView fullQuestionText = (TextView) view.findViewById(R.id.fullQuestionText);
//        TextView questionId = (TextView) view.findViewById(R.id.questionIdText);
//        String questionIdString = questionId.getText().toString();
//
//        int i = (Integer)view.getTag();
//
//        String question = questionPosts[i].getQuestion();
//
//        if (listViewTouched.containsKey(view)){
//            if(listViewTouched.get(view)){
//                if (question.length() > 60){
//                    question = question.substring(0,60)+"...";
//                }
//                questionText.setText(question);
//                fullQuestionText.setText("See Full Question");
//                listViewTouched.put(view,false);
//            }
//            else{
//                questionText.setText(questionPosts[i].getQuestion());
//                fullQuestionText.setText("Hide Full Question");
//                listViewTouched.put(view,true);
//            }
//        }
//
//        else{
//            questionText.setText(question);
//            fullQuestionText.setText("Hide Full Question");
//
//            listViewTouched.put(view,true);
//        }

    }

    private void seeAnswers(View view){
        final PopupWindow pw = spinnerLauch();

        TextView questionId = (TextView) view.findViewById(R.id.questionIdText);
        final String questionIdString = questionId.getText().toString();

        int i = (Integer)view.getTag();

        final String question = questionPosts[i].getQuestion();
        String answer = questionPosts[i].getAnswerUrl();

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="http://mighty-falls-66731.herokuapp.com/jobs/answers?answerPath=" + answer;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            answerResults = new JSONObject(response);
                            String [] answers = getAnswersFromURL(answerResults);

                            Intent intent = new Intent(context.getApplicationContext(),AnswerPageActivity.class);
                            intent.putExtra("Answers",answers);
                            intent.putExtra("Question",question);
                            intent.putExtra("QuestionID", questionIdString);
                            pw.dismiss();
                            context.startActivity(intent);
                        }
                        catch (JSONException e) {
                            pw.dismiss();
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pw.dismiss();
                Log.d("ERROR",error.toString());
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private String [] getAnswersFromURL(JSONObject results){
        ArrayList<String> answers = new ArrayList<String>();
        try {
            JSONObject result = results.getJSONObject("Answers");
            JSONArray resultArray = result.getJSONArray("Answer");

            for (int i=0;i<resultArray.length();i++){
                if(!resultArray.get(i).toString().equals("")){
                    answers.add(resultArray.get(i).toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (answers.toArray(new String [answers.size()]));
    }

    private PopupWindow spinnerLauch(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        final PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.loading_circle, null, true),
                (int) (width * 1), (int) (height * 1), true);

        final CircleProgressBar progressBar = (CircleProgressBar)pw.getContentView().findViewById(R.id.circleBarSpinner);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setColorSchemeColors(Color.parseColor("#33AAFF"));

        pw.showAtLocation(context.findViewById(R.id.question_page), Gravity.CENTER, 0, 0);

        return pw;
    }
}

