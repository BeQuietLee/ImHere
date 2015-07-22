package com.leili.imhere.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.leili.imhere.R;
import com.leili.imhere.fragment.AboutFragment;
import com.leili.imhere.fragment.MapFragment;

/**
 * Created by Lei.Li on 7/22/15 6:41 PM.
 */
public class MainActivity extends Activity implements View.OnClickListener {
    // Fragments
    private MapFragment mapFragment;
    private AboutFragment aboutFragment;
    private FragmentManager fragmentManager;
    // Container
    private int containerId = R.id.content;
    // Buttons
    private View mapViewTab;
    private View aboutViewTab;

    // OnClickListeners
    private View.OnClickListener tabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mapViewTab) {
                switchToTab(0);
            } else if (v == aboutViewTab) {
                switchToTab(1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initViews();
        fragmentManager = getFragmentManager();
    }

    private void initViews() {
        mapViewTab = super.findViewById(R.id.map_view);
        mapViewTab.setOnClickListener(tabOnClickListener);
        aboutViewTab = super.findViewById(R.id.about_view);
        aboutViewTab.setOnClickListener(tabOnClickListener);
    }

    private void switchToTab(int i) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragments(transaction);
        switch (i) {
            case 0: // map view
                if (mapFragment == null) {
                    mapFragment = new MapFragment();
                    transaction.add(containerId, mapFragment);
                } else {
                    transaction.show(mapFragment);
                }
                break;
            case 1: // about view
                if (aboutFragment == null) {
                    aboutFragment = new AboutFragment();
                    transaction.add(containerId, aboutFragment);
                } else {
                    transaction.show(aboutFragment);
                }
            default:
                // do nothing

        }
        transaction.commit();

    }

    private void hideAllFragments(FragmentTransaction transaction) {
        if (mapFragment != null) {
            transaction.hide(mapFragment);
        }
        if (aboutFragment != null) {
            transaction.hide(aboutFragment);
        }

    }

    @Override
    public void onClick(View v) {

    }
}
