package com.leili.imhere.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leili.imhere.R;

/**
 * Created by Lei.Li on 7/23/15 8:41 PM.
 */
public class LikeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.like_fragment, container, false);
    }
}
