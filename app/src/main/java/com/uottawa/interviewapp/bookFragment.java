package com.uottawa.interviewapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

/**
 * Created by filipslatinac on 2017-07-18.
 */

class bookFragment extends Fragment {

    View rootView;
    ViewGroup container;
    LayoutInflater inflater;

    Typeface sanFran;
    Typeface sanFranBolder;
    Typeface sanFranMedium;
    Typeface fontAwesome;

    TextView authors;
    TextView title;

    TextView authorsTag;
    TextView titleTag;

    ImageView bookImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){
        this.inflater = inflater;

        this.container = container;
        rootView = inflater.inflate(R.layout.book_view, container,
                false);

        fontAwesome = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        sanFran = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SanFranciscoDisplay-Ultralight.otf");
        sanFranBolder = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SanFranciscoDisplay-Heavy.otf");
        sanFranMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SanFranciscoDisplay-Regular.otf");


        bookImage = (ImageView) rootView.findViewById(R.id.bookImage);
        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWebView();
            }
        });
        new RetrieveFeedTask().execute(getArguments().getString("image"));

        authors = (TextView) rootView.findViewById(R.id.bookAuthor);
        title  = (TextView) rootView.findViewById(R.id.bookTitle);

        authorsTag = (TextView) rootView.findViewById(R.id.bookAuthorTag);
        titleTag  = (TextView) rootView.findViewById(R.id.bookTitleTag);

        authorsTag.setTypeface(sanFranBolder);
        titleTag.setTypeface(sanFranBolder);


        title.setTypeface(sanFranMedium);
        title.setText(" "+getArguments().getString("title"));



        authors.setTypeface(sanFranMedium);

        StringBuffer authorsString = new StringBuffer();

        String [] authorsReceived = getArguments().getStringArray("authors");

        for(int i=0;i<authorsReceived.length;i++){
            authorsString.append(authorsReceived[i]);
            if(i != authorsReceived.length-1){
                authorsString.append(" ");
            }
        }

        if(authorsReceived.length > 1){
            authors.setText(" "+authorsString.toString());
        }

        else{
            authorsTag.setText("Author:");
            authors.setText(" "+authorsString.toString());
        }


        return rootView;

    }

    private void showWebView(){

        TextView WWtitle;
        WebView mWebView;
        TextView backButton;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        final PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.web_view, null, true),
                (int) (width * 1), (int) (height * 1), true);

        WWtitle = (TextView) pw.getContentView().findViewById(R.id.titleOfWebView);
        WWtitle.setTypeface(sanFranMedium);
        WWtitle.setTextColor(Color.parseColor("#33AAFF"));

        if(getArguments().getString("title").length() >= 10){
            WWtitle.setText(" "+getArguments().getString("title").substring(0,10)+"...");
        }
        else{
            WWtitle.setText(" "+getArguments().getString("title"));
        }


        backButton = (TextView) pw.getContentView().findViewById(R.id.backButtonWebView);
        backButton.setTypeface(fontAwesome);
        backButton.setText("\uf053");
        backButton.setTextSize(24);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });

        mWebView = (WebView) pw.getContentView().findViewById(R.id.webview);
        mWebView.loadUrl(getArguments().getString("url"));

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        pw.showAtLocation(getActivity().findViewById(R.id.tabLayoutContainer), Gravity.CENTER, 0, 0);
    }


    class RetrieveFeedTask extends AsyncTask<String, Void, Drawable> {

        protected Drawable doInBackground(String... url) {
            try {
                InputStream is = (InputStream) new URL(url[0]).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");
                return d;
            } catch (Exception e) {
                System.out.println(e);
                return ContextCompat.getDrawable(getActivity(),R.drawable.questionmark);
            }
        }

        protected void onPostExecute(Drawable x) {
            bookImage.setImageDrawable(x);
        }
    }
}
