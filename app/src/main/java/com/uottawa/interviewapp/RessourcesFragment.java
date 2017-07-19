package com.uottawa.interviewapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



/**
 * Created by filipslatinac on 2017-07-16.
 */

class RessourcesFragment extends Fragment {

    View rootView;
    ViewGroup container;
    LayoutInflater inflater;

    ViewPager youtubeVideos;
    ViewPager books;
    ViewPager courses;

    TextView nextVideo;
    TextView previousVideo;
    TextView videoResourcesTextView;

    TextView nextBook;
    TextView previousBook;
    TextView bookResourcesTextView;

    TextView nextCourse;
    TextView previousCourse;
    TextView courseResourcesTextView;

    Typeface sanFran;
    Typeface sanFranBolder;
    Typeface sanFranMedium;
    Typeface fontAwesome;

    LearningResources [] videosReceived;
    LearningResources [] booksReceived;
    LearningResources [] coursesReceived;



    int numberOfVideos;
    int numberOfBooks;
    int numberOfCourses;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){
    this.inflater = inflater;

        this.container = container;
        rootView = inflater.inflate(R.layout.resource_page, container,
                false);

        fontAwesome = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        sanFran = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SanFranciscoDisplay-Ultralight.otf");
        sanFranBolder = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SanFranciscoDisplay-Heavy.otf");
        sanFranMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SanFranciscoDisplay-Regular.otf");

        videosReceived = (LearningResources[]) getArguments().get("Videos");
        booksReceived = (LearningResources[]) getArguments().get("Books");
        coursesReceived = (LearningResources[]) getArguments().get("Courses");



        numberOfVideos = videosReceived.length;
        numberOfBooks = booksReceived.length;
        numberOfCourses = coursesReceived.length;


        if(numberOfVideos < 1){
            youtubeVideos = (ViewPager) rootView.findViewById(R.id.youtubeVideos);
            youtubeVideos.setAdapter(new emptyResourceAdapter(getActivity().getSupportFragmentManager()));
        }

        else{
            youtubeVideos = (ViewPager) rootView.findViewById(R.id.youtubeVideos);
            youtubeVideos.setAdapter(new youtubeAdapter(getActivity().getSupportFragmentManager(),videosReceived));
        }

        if(numberOfBooks < 1){
            books = (ViewPager) rootView.findViewById(R.id.books);
            books.setAdapter(new emptyResourceAdapter(getActivity().getSupportFragmentManager()));
        }

        else{
            books = (ViewPager) rootView.findViewById(R.id.books);
            books.setAdapter(new booksAdapter(getActivity().getSupportFragmentManager(),booksReceived));
        }

        if(numberOfCourses < 1){
            courses = (ViewPager) rootView.findViewById(R.id.courses);
            courses.setAdapter(new emptyResourceAdapter(getActivity().getSupportFragmentManager()));
        }

        else{
            courses = (ViewPager) rootView.findViewById(R.id.courses);
            courses.setAdapter(new courseAdapter(getActivity().getSupportFragmentManager(),coursesReceived));
        }








        videoResourcesTextView = (TextView)rootView.findViewById(R.id.videoResourcesView);
        videoResourcesTextView.setTypeface(sanFranMedium);
        videoResourcesTextView.setTextColor(Color.parseColor("#33AAFF"));

        bookResourcesTextView = (TextView)rootView.findViewById(R.id.bookResourcesView);
        bookResourcesTextView.setTypeface(sanFranMedium);
        bookResourcesTextView.setTextColor(Color.parseColor("#33AAFF"));

        courseResourcesTextView = (TextView)rootView.findViewById(R.id.courseResourcesView);
        courseResourcesTextView.setTypeface(sanFranMedium);
        courseResourcesTextView.setTextColor(Color.parseColor("#33AAFF"));



        nextVideo = (TextView) rootView.findViewById(R.id.nextVideoTextVideo);
        nextVideo.setTypeface(fontAwesome);
        nextVideo.setTextSize(30);
        nextVideo.setText("\uf054");

        previousVideo = (TextView) rootView.findViewById(R.id.previousVideoTextVideo);
        previousVideo.setTypeface(fontAwesome);
        previousVideo.setTextSize(30);
        previousVideo.setText("\uf053");

        nextVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youtubeVideos.setCurrentItem(youtubeVideos.getCurrentItem()+1);
            }
        });

        previousVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youtubeVideos.setCurrentItem(youtubeVideos.getCurrentItem()-1);
            }
        });

        previousVideo.setText("");
        previousVideo.setClickable(false);
        if (numberOfVideos <= 1){
            nextVideo.setText("");
            nextVideo.setClickable(false);
        }



        youtubeVideos.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0 && position < numberOfVideos-1){
                    previousVideo.setText("");
                    previousVideo.setClickable(false);
                    nextVideo.setText("\uf054");
                    nextVideo.setClickable(true);
                }

                else if(position > 0 && position < numberOfVideos-1){
                    previousVideo.setText("\uf053");
                    previousVideo.setClickable(true);
                    nextVideo.setText("\uf054");
                    nextVideo.setClickable(true);
                }

                else if(position > 0 && position == numberOfVideos-1){
                    previousVideo.setText("\uf053");
                    previousVideo.setClickable(true);
                    nextVideo.setText("");
                    nextVideo.setClickable(false);
                }
            }



            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        nextBook = (TextView) rootView.findViewById(R.id.nextBookTextBook);
        nextBook.setTypeface(fontAwesome);
        nextBook.setTextSize(30);
        nextBook.setText("\uf054");

        previousBook = (TextView) rootView.findViewById(R.id.previousBookTextBook);
        previousBook.setTypeface(fontAwesome);
        previousBook.setTextSize(30);
        previousBook.setText("\uf053");

        nextBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                books.setCurrentItem(books.getCurrentItem()+1);
            }
        });

        previousBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                books.setCurrentItem(books.getCurrentItem()-1);
            }
        });

        previousBook.setText("");
        previousBook.setClickable(false);
        if (numberOfBooks <= 1){
            nextBook.setText("");
            nextBook.setClickable(false);
        }



        books.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0 && position < numberOfBooks - 1) {
                    previousBook.setText("");
                    previousBook.setClickable(false);
                    nextBook.setText("\uf054");
                    nextBook.setClickable(true);
                } else if (position > 0 && position < numberOfBooks - 1) {
                    previousBook.setText("\uf053");
                    previousBook.setClickable(true);
                    nextBook.setText("\uf054");
                    nextBook.setClickable(true);
                } else if (position > 0 && position == numberOfBooks - 1) {
                    previousBook.setText("\uf053");
                    previousBook.setClickable(true);
                    nextBook.setText("");
                    nextBook.setClickable(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        nextCourse = (TextView) rootView.findViewById(R.id.nextCourseTextCourse);
        nextCourse.setTypeface(fontAwesome);
        nextCourse.setTextSize(30);
        nextCourse.setText("\uf054");

        previousCourse = (TextView) rootView.findViewById(R.id.previousCourseTextCourse);
        previousCourse.setTypeface(fontAwesome);
        previousCourse.setTextSize(30);
        previousCourse.setText("\uf053");

        nextCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courses.setCurrentItem(courses.getCurrentItem()+1);
            }
        });

        previousCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courses.setCurrentItem(courses.getCurrentItem()-1);
            }
        });

        previousCourse.setText("");
        previousCourse.setClickable(false);
        if (numberOfCourses <= 1){
            nextCourse.setText("");
            nextCourse.setClickable(false);
        }



        courses.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0 && position < numberOfCourses - 1) {
                    previousCourse.setText("");
                    previousCourse.setClickable(false);
                    nextBook.setText("\uf054");
                    nextBook.setClickable(true);
                } else if (position > 0 && position < numberOfCourses - 1) {
                    previousCourse.setText("\uf053");
                    previousCourse.setClickable(true);
                    nextCourse.setText("\uf054");
                    nextCourse.setClickable(true);
                } else if (position > 0 && position == numberOfCourses - 1) {
                    previousCourse.setText("\uf053");
                    previousCourse.setClickable(true);
                    nextCourse.setText("");
                    nextCourse.setClickable(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return rootView;

    }


    private class youtubeAdapter extends FragmentStatePagerAdapter {

        LearningResources [] videos;
        public youtubeAdapter(FragmentManager fm, LearningResources [] videos) {
            super(fm);
            this.videos = videos;
        }

        @Override
        public Fragment getItem(int position) {
            youtubeFragment fragment = new youtubeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url",videos[position].getUrl());
            bundle.putString("title",videos[position].getTitle());
            bundle.putString("image",videos[position].getImage());
            bundle.putString("description",videos[position].getDescription());
            fragment.setArguments(bundle);
            return fragment;

        }
        @Override
        public int getCount() {
            return  videos.length;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    private class booksAdapter extends FragmentStatePagerAdapter {
        LearningResources [] books;
        public booksAdapter( FragmentManager fm, LearningResources[] booksReceived) {
            super(fm);
            books = booksReceived;
        }

        @Override
        public Fragment getItem(int position) {
            bookFragment fragment = new bookFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url",books[position].getUrl());
            bundle.putString("title",books[position].getTitle());
            bundle.putString("image",books[position].getImage());
            bundle.putString("description",books[position].getDescription());
            bundle.putStringArray("authors",books[position].getAuthor());
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return books.length;
        }
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    private class courseAdapter extends FragmentStatePagerAdapter {
        public courseAdapter(FragmentManager fm, LearningResources[] coursesReceived) {
            super(fm);
        }

        @Override
        public int getCount() {
            return coursesReceived.length;
        }

        @Override
        public Fragment getItem(int position) {
            courseFragment fragment = new courseFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url",coursesReceived[position].getUrl());
            bundle.putString("title",coursesReceived[position].getTitle());
            bundle.putString("image",coursesReceived[position].getImage());
            bundle.putString("description",coursesReceived[position].getDescription());
            fragment.setArguments(bundle);
            return fragment;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private class emptyResourceAdapter extends FragmentStatePagerAdapter {

        public emptyResourceAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new emptyResourceFragment();
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
}
