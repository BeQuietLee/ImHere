package com.leili.imhere.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.leili.imhere.R;
import com.leili.imhere.database.DatabaseHelper;
import com.leili.imhere.entity.ILikePosition;
import com.leili.imhere.entity.Position;
import com.leili.imhere.entity.PositionAdapter;
import com.leili.imhere.event.Event;
import com.leili.imhere.utils.ViewUtils;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Lei.Li on 7/23/15 8:41 PM.
 */
public class LikeFragment extends Fragment implements ILikePosition {
    private DatabaseHelper databaseHelper;
    private ListView lvLikePositions;
    private List<Position> likedPositions;
    private PositionAdapter positionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.like_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvLikePositions = (ListView) getView().findViewById(R.id.like_positions_lv);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
        likedPositions = databaseHelper.loadLikedPositions();
        positionAdapter = new PositionAdapter(getActivity(), likedPositions, this);
        lvLikePositions.setAdapter(positionAdapter);
        lvLikePositions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new Event.LocatePositionEvent(likedPositions.get(position)));
            }
        });
        lvLikePositions.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("确认删除？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deletePosition(likedPositions.get(position));
                        refreshLikedPositions();
                    }
                }).setNegativeButton("否", null);
                builder.show();
                return true;
            }
        });
    }

    @Override
    public void loadMore(int offset, int pageSize) {
        List<Position> morePositions = databaseHelper.loadLikedPositions(pageSize, offset);
        likedPositions.addAll(morePositions);
        positionAdapter.notifyDataSetChanged();
    }

    @Override
    public void likePosition(Position position) {
        ViewUtils.toast(getActivity(), "错误：收藏页面不可点击星星！");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread (Event.LikeEvent event) {
        refreshLikedPositions();
    }

    private void refreshLikedPositions() {
        likedPositions.clear();
        likedPositions.addAll(databaseHelper.loadLikedPositions());
        positionAdapter.notifyDataSetChanged();
    }
}
