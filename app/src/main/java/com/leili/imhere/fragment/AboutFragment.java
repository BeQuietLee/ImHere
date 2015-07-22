package com.leili.imhere.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leili.imhere.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Lei.Li on 7/22/15 6:40 PM.
 */
public class AboutFragment extends Fragment {
    private static final String TAG = AboutFragment.class.getSimpleName();
    private String releaseLog;
    // Views
    private TextView tvReleaseLog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvReleaseLog = (TextView) getView().findViewById(R.id.releaselog_tv);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        releaseLog = readReleaseLog(getResources().openRawResource(R.raw.release_log));
        tvReleaseLog.setText(releaseLog);
    }

    private String readReleaseLog(InputStream is) {
        String res;
        try {
            byte[] buf = new byte[is.available()];
            is.read(buf);
            res = new String(buf, "UTF8");
            is.close();
        } catch (IOException e) {
            Log.w(TAG, "fail to read log", e);
            res = "";
        }
        return res;
    }
}
