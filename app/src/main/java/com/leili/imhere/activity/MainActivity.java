package com.leili.imhere.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.leili.imhere.R;
import com.leili.imhere.event.Event;
import com.leili.imhere.fragment.AboutFragment;
import com.leili.imhere.fragment.LikeFragment;
import com.leili.imhere.fragment.MapFragment;
import com.leili.imhere.fragment.SearchFragment;

import de.greenrobot.event.EventBus;

/**
 * Created by Lei.Li on 7/22/15 6:41 PM.
 */
public class MainActivity extends Activity implements View.OnClickListener {
    // Fragments
    private MapFragment mapFragment;
    private SearchFragment searchFragment;
    private LikeFragment likeFragment;
    private AboutFragment aboutFragment;
    private FragmentManager fragmentManager;
    // Container
    private int containerId = R.id.content;
    // Buttons
    private View mapViewTab;
    private View searchViewTab;
    private View likeViewTab;
    private View aboutViewTab;

    // OnClickListeners
    private View.OnClickListener tabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mapViewTab) {
                switchToTab(0);
            } else if (v == searchViewTab) {
                switchToTab(1);
            } else if (v == likeViewTab) {
                switchToTab(2);
            } else if (v == aboutViewTab) {
                switchToTab(3);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initViews();
        fragmentManager = getFragmentManager();
        switchToTab(0);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(Event.LocatePositionEvent event) {
        switchToTab(0);
    }

    private void initViews() {
        mapViewTab = super.findViewById(R.id.map_view);
        mapViewTab.setOnClickListener(tabOnClickListener);
        searchViewTab = super.findViewById(R.id.search_view);
        searchViewTab.setOnClickListener(tabOnClickListener);
        likeViewTab = super.findViewById(R.id.like_view);
        likeViewTab.setOnClickListener(tabOnClickListener);
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
            case 1: // search view
                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                    transaction.add(containerId, searchFragment);
                } else {
                    transaction.show(searchFragment);
                }
                break;
            case 2: // like view
                if (likeFragment == null) {
                    likeFragment = new LikeFragment();
                    transaction.add(containerId, likeFragment);
                } else {
                    transaction.show(likeFragment);
                }
                break;
            case 3: // about view
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
        if (searchFragment != null) {
            transaction.hide(searchFragment);
        }
        if (likeFragment != null) {
            transaction.hide(likeFragment);
        }
        if (aboutFragment != null) {
            transaction.hide(aboutFragment);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
