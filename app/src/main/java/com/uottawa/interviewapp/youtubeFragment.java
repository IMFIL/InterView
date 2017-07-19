package com.uottawa.interviewapp;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import static android.support.v4.view.PagerAdapter.POSITION_NONE;



/**
 * Created by filipslatinac on 2017-07-17.
 */

class youtubeFragment extends Fragment {
    View rootView;


    Typeface sanFran;
    Typeface sanFranBolder;
    Typeface sanFranMedium;
    Typeface fontAwesome;

    YouTubePlayerSupportFragment youtubeFrag;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){

        rootView = inflater.inflate(R.layout.youtube_fragment, container,
                false);



        sanFran = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SanFranciscoDisplay-Ultralight.otf");
        sanFranBolder = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SanFranciscoDisplay-Light.otf");
        sanFranMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SanFranciscoDisplay-Regular.otf");
        fontAwesome = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");



        youtubeFrag = new youtubeView(getArguments().getString("url"));

        if (getUserVisibleHint()) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.youtubeVideosFrame, youtubeFrag).commit();
        }


        return rootView;

    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (!isVisibleToUser && youtubeFrag!=null ) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.remove(youtubeFrag).commit();
            }
            if (isVisibleToUser && youtubeFrag != null) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.youtubeVideosFrame, youtubeFrag).commit();
            }
        }

}
