package com.kuxinwei.sample.sampleview;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kuxinwei.library.view.TitanicView;
import com.kuxinwei.library.view.TitanicViewAnimation;


/**
 * A simple {@link Fragment} subclass.
 */
public class TitanicFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_titanic, container, false);
        TitanicView tv = (TitanicView) root.findViewById(R.id.titan_view);
        new TitanicViewAnimation().start(tv);
        return root;
    }


}
