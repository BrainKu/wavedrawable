package com.kuxinwei.sample.sampleview;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuxinwei.library.view.TitanicView;
import com.kuxinwei.library.view.TitanicViewAnimation;
import com.kuxinwei.library.view.WaveDrawable;


/**
 * A simple fragment which use wavedrawable as background
 */
public class WaveBGFragment extends Fragment {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wave_bg, container, false);
        WaveDrawable mDrawable = new WaveDrawable(getActivity());
        root.setBackground(mDrawable);
        mDrawable.setCallback(root);
        mDrawable.start();
        return root;
    }


}
