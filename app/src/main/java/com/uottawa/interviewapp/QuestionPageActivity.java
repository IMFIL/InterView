package com.uottawa.interviewapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by filipslatinac on 2017-06-26.
 */

public class QuestionPageActivity extends AppCompatActivity {

    QuestionPost [] questionPosts;
    String [] questions;

    TextView companyLabel;

    EditText searchBar;
    ImageButton searchButton;

    String companyName;
    Typeface sanFranMedium;
    Typeface sanFran;

    ImageView logo;

    JSONObject questionResults;

    Context context = this;

    TabLayout tabs;
    ViewPager currentSelectionView;

    String [] tagArray = new String[4];

    String tag1;
    String tag2;
    String tag3;
    String tag4;


    QuestionPost [] questionsPosts;

    String sc;

    HashMap<String,LearningResources []> ressourcesMap = new HashMap<String,LearningResources []>();

    PopupWindow pw;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_page);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        sanFranMedium = Typeface.createFromAsset(getAssets(), "fonts/SanFranciscoDisplay-Regular.otf");
        sanFran = Typeface.createFromAsset(getAssets(), "fonts/SanFranciscoDisplay-Ultralight.otf");


        Bundle packageReceived = getIntent().getExtras();
        questionPosts = (QuestionPost[]) packageReceived.getSerializable("QuestionPosts");
        companyName  = packageReceived.getString("CompanyName");

        companyLabel = (TextView) findViewById(R.id.companyNameLabel);
        companyLabel.setTypeface(sanFranMedium);
        companyLabel.setTextColor(Color.parseColor("#2d3142"));

        currentSelectionView = (ViewPager) findViewById(R.id.questionsAvailablePager);
        ressourcesMap = (HashMap<String,LearningResources []>) packageReceived.getSerializable("Resources");
        currentSelectionView.setAdapter(new currentSelectionAdapter(getSupportFragmentManager(),getApplicationContext()
                ,ressourcesMap));

        updateContent(questionPosts,companyName);


        tabs = (TabLayout) findViewById(R.id.tabLayoutViews);
        tabs.setupWithViewPager(currentSelectionView);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentSelectionView.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                currentSelectionView.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                currentSelectionView.setCurrentItem(tab.getPosition());
            }
        });

        searchBar = (EditText) findViewById(R.id.searchBarQuestionPage);
        searchBar.setTypeface(sanFran);

        searchButton = (ImageButton) findViewById(R.id.searchQuestionsQuestionPage);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sc = searchBar.getText().toString();
                sc = sc.replace(" ","-");
                final String searchedCompany = sc;


                if (searchedCompany.length() != 0){
                    pw = spinnerLauch();
                    RequestQueue queue = Volley.newRequestQueue(QuestionPageActivity.this);
                    String url ="http://mighty-falls-66731.herokuapp.com/jobs?companyTitle=" + searchedCompany;

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
                            Log.d("ERROR",error.toString());
                        }
                    });
                    queue.add(stringRequest);

                }

                else {

                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter the name of the company", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        logo = (ImageView) findViewById(R.id.logoQuestion);
        new RetrieveFeedTask().execute("https://logo.clearbit.com/"+companyName.toLowerCase()+".com");

    }



    class RetrieveFeedTask extends AsyncTask<String, Void, Drawable> {

        protected Drawable doInBackground(String... url) {
            try {
                InputStream is = (InputStream) new URL(url[0]).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");
                return d;
            } catch (Exception e) {
                System.out.println(e);
                return ContextCompat.getDrawable(context,R.drawable.questionmark);
            }
        }

        protected void onPostExecute(Drawable x) {

            logo.setImageDrawable(x);
            logo.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private QuestionPost [] getQuestionPosts(JSONObject jsonArray,String sc){
        QuestionPost [] questions = new QuestionPost[jsonArray.length()-1];

        try {
            JSONArray jsonTags = jsonArray.getJSONArray("Tags");
            for(int i=0;i<jsonTags.length();i++){
                tagArray[i] = jsonTags.get(i).toString();
            }
        } catch (JSONException e) {
            tagArray = new String[]{"software", "coding",sc };
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



    private void updateContent(QuestionPost [] questionPosts, String companyName){
        questions = new String [questionPosts.length];
        companyName = companyName.trim().replace("-"," ");

        for (int i=0; i<questionPosts.length;i++){
            String question = questionPosts[i].getQuestion();

            if (question.length() > 60){
                question = question.substring(0,60) + "...";
            }

            questions[i] = question;
        }

        companyLabel.setText(companyName.substring(0,1).toUpperCase() + companyName.substring(1));
        currentSelectionView.destroyDrawingCache();
        currentSelectionView.getAdapter().notifyDataSetChanged();
        currentSelectionView.setAdapter(new currentSelectionAdapter(getSupportFragmentManager(),getApplicationContext(),ressourcesMap));
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

        pw.showAtLocation(findViewById(R.id.question_page), Gravity.CENTER, 0, 0);

        return pw;
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

    private class currentSelectionAdapter extends FragmentStatePagerAdapter {
        HashMap<String,LearningResources[]> map;

        public currentSelectionAdapter(FragmentManager supportFragmentManager, Context applicationContext,HashMap<String,LearningResources[]> map) {
            super(supportFragmentManager);
            this.map = map;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    QuestionsFragment fragment = new QuestionsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("Questions",questions);
                    bundle.putSerializable("QuestionPosts",questionPosts);
                    fragment.setArguments(bundle);
                    return fragment;
                case 1:
                    RessourcesFragment fragmentResource = new RessourcesFragment();
                    Bundle bundleResources = new Bundle();
                    bundleResources.putSerializable("Videos",map.get("youtubeVideos"));
                    bundleResources.putSerializable("Books",map.get("books"));
                    bundleResources.putSerializable("Courses",map.get("courses"));
                    fragmentResource.setArguments(bundleResources);
                    return fragmentResource;
                default:
                    QuestionsFragment fragmentDefault = new QuestionsFragment();
                    Bundle bundleDefault = new Bundle();
                    bundleDefault.putStringArray("Questions",questions);
                    bundleDefault.putSerializable("QuestionPosts",questionPosts);
                    fragmentDefault.setArguments(bundleDefault);
                    return fragmentDefault;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position){
            if(position == 0){
                return "Questions Available";
            }

            else{
                return "Learning Resources";
            }
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private void assignAdapterForLearningResources(JSONObject response){

        final HashMap<String,LearningResources []> content = new HashMap<String,LearningResources []>();

        final ArrayList<LearningResources> youtubeVideosReceived = new ArrayList<LearningResources>();
        final ArrayList<LearningResources> booksReceived = new ArrayList<LearningResources>();
        final ArrayList<LearningResources> coursesReceived = new ArrayList<LearningResources>();

        questionsPosts = getQuestionPosts(questionResults,sc);


        tag1 = tagArray[0];
        tag2 = tagArray[1];
        tag3 = tagArray[2];
        tag4 = tagArray[3];

        RequestQueue queue = Volley.newRequestQueue(QuestionPageActivity.this);
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
                            ressourcesMap = content;
                            updateContent(questionsPosts,sc);
                            new RetrieveFeedTask().execute("https://logo.clearbit.com/"+sc.toLowerCase()+".com");
                            pw.dismiss();


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
}
