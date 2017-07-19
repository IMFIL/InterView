package com.uottawa.interviewapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import java.util.concurrent.ThreadLocalRandom;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Typeface sanFran;
    Typeface sanFranBolder;
    Typeface sanFranMedium;

    TextView companyNameLabel;
    TextView pegasus;

    EditText searchBar;

    ImageButton search;

    JSONObject questionResults;

    String searchedCompany = "";

    PopupWindow pw;

    String [] tagArray = new String[4];

    String tag1;
    String tag2;
    String tag3;
    String tag4;

    QuestionPost [] questionsPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sanFran = Typeface.createFromAsset(getAssets(), "fonts/SanFranciscoDisplay-Ultralight.otf");
        sanFranBolder = Typeface.createFromAsset(getAssets(), "fonts/SanFranciscoDisplay-Light.otf");
        sanFranMedium = Typeface.createFromAsset(getAssets(), "fonts/SanFranciscoDisplay-Regular.otf");



        companyNameLabel = (TextView) findViewById(R.id.searchCompanyLabel);
        companyNameLabel.setTypeface(sanFran);

        pegasus = (TextView) findViewById(R.id.pegasusLabel);
        pegasus.setTypeface(sanFran);
        pegasus.setTextColor(Color.parseColor("#33AAFF"));

        searchBar = (EditText) findViewById(R.id.searchBar);
        searchBar.setTypeface(sanFranBolder);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchedCompany = searchBar.getText().toString();
                searchedCompany = searchedCompany.trim().replace(" ","-");
            }
        });

        search = (ImageButton) findViewById(R.id.searchQuestions);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (searchedCompany.length() != 0){
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    String url ="http://mighty-falls-66731.herokuapp.com/jobs?companyTitle=" + searchedCompany;

                    pw = spinnerLauch();

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        questionResults = new JSONObject(response);
                                        assignAdapterForLearningResources(questionResults);
                                    }
                                    catch (JSONException e) {
                                        pw.dismiss();
                                        e.printStackTrace();
                                        Toast toast = Toast.makeText(getApplicationContext(), "Company Not Found", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pw.dismiss();
                            Toast toast = Toast.makeText(getApplicationContext(), "Try again, server is not awake yet!", Toast.LENGTH_SHORT);
                            toast.show();
                            Log.d("ERROR",error.toString());
                        }
                    });
// Add the request to the RequestQueue.
                    queue.add(stringRequest);

                }

                else {
                    //Make this nicer !

                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter the name of the company", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private QuestionPost [] getQuestionPosts(JSONObject jsonArray){
        QuestionPost [] questions = new QuestionPost[jsonArray.length()-1];

        try {
            JSONArray jsonTags = jsonArray.getJSONArray("Tags");
            for(int i=0;i<jsonTags.length();i++){
                tagArray[i] = jsonTags.get(i).toString();
            }
        } catch (JSONException e) {
            tagArray = new String[]{"software", "coding", searchedCompany};
        }


        for (int i = 0; i < jsonArray.length()-1;i++){
            String postNumber = "Post"+String.valueOf(i+1);
            try {
                JSONObject object = jsonArray.getJSONObject(postNumber);
                String answerUrl = object.getString("Answers");
                String question = object.getJSONArray("Question").get(0).toString();
                questions[i] = new QuestionPost(question,answerUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return questions;
    }

    private void assignAdapterForLearningResources(JSONObject response){

        final HashMap<String,LearningResources []> content = new HashMap<String,LearningResources []>();

        final ArrayList<LearningResources> youtubeVideosReceived = new ArrayList<LearningResources>();
        final ArrayList<LearningResources> booksReceived = new ArrayList<LearningResources>();
        final ArrayList<LearningResources> coursesReceived = new ArrayList<LearningResources>();

        questionsPosts = getQuestionPosts(questionResults);

        tag1 = tagArray[0];
        tag2 = tagArray[1];
        tag3 = tagArray[2];
        tag4 = tagArray[3];


        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url ="http://mighty-falls-66731.herokuapp.com/resources?tag1="+tag1+"&tag2="+tag2+"&tag3="+tag3+"&tag4="+tag4;
        Log.d("URL",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            JSONObject youtubeVideos = (JSONObject) responseObject.get("YoutubeVideos");

                            for(int i=0;i<youtubeVideos.length();i++){
                                JSONObject video = (JSONObject) youtubeVideos.get("Video"+i);
                                youtubeVideosReceived.add(new LearningResources(video.getString("url"),video.getString("title"),video.getString("image"),video.getString("description")));
                            }

                            content.put("youtubeVideos",youtubeVideosReceived.toArray(new LearningResources[youtubeVideosReceived.size()]));

                            JSONObject books = (JSONObject) responseObject.get("Books");

                            for(int i=0;i<books.length();i++){
                                JSONObject book = (JSONObject) books.get("Book"+i);
                                LearningResources LR = new LearningResources(book.getString("url"),book.getString("title"),book.getString("image"),book.getString("description"));
                                JSONArray jsonAuthors = book.getJSONArray("author");
                                String [] authors = new String [jsonAuthors.length()];
                                for(int author=0;author<authors.length;author++){
                                    authors[author] = jsonAuthors.get(author).toString();
                                }
                                LR.setAuthor(authors);
                                booksReceived.add(LR);

                            }

                            content.put("books", booksReceived.toArray(new LearningResources[booksReceived.size()]));

                            JSONObject courses = (JSONObject) responseObject.get("Courses");

                            for(int i=0;i<courses.length();i++){
                                JSONObject course = (JSONObject) courses.get("Course"+i);
                                coursesReceived.add(new LearningResources(course.getString("url"),course.getString("title"),course.getString("image"),course.getString("description")));
                            }

                            content.put("courses", coursesReceived.toArray(new LearningResources[coursesReceived.size()]));

                            Intent intent = new Intent (getApplicationContext(),QuestionPageActivity.class);
                            intent.putExtra("QuestionPosts", questionsPosts);
                            intent.putExtra("CompanyName",searchedCompany);
                            intent.putExtra("Resources",content);
                            pw.dismiss();
                            startActivity(intent);


                        } catch (JSONException e) {
                            pw.dismiss();
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pw.dismiss();
                        Log.d("ERROR",error.toString());
                    }
                });
        queue.add(stringRequest);

    }

    private PopupWindow spinnerLauch(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        final PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.loading_circle, null, true),
                (int) (width * 1), (int) (height * 1), true);

        final CircleProgressBar progressBar = (CircleProgressBar)pw.getContentView().findViewById(R.id.circleBarSpinner);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setColorSchemeColors(Color.parseColor("#33AAFF"));

        pw.showAtLocation(findViewById(R.id.activity_main), Gravity.CENTER, 0, 0);

        return pw;
    }
}
