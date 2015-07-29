package com.leili.imhere.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leili.imhere.R;
import com.leili.imhere.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 位置对象Adapter<br>
 * Created by Lei.Li on 7/24/15 10:57 AM.
 */
public class PositionAdapter extends BaseAdapter {
    private Context context;
    private List<Position> positions;
    private ILikePosition likePositionStub;
    private Object showMoreObj = new Object();

    public PositionAdapter(Context context, List<Position> positions, ILikePosition likePositionStub) {
        this.context = context;
        this.positions = positions;
        this.likePositionStub = likePositionStub;
    }

    public void appendPositions(List<Position> newPositions) {
        if (positions == null) {
            positions = new ArrayList<>();
        }
        positions.addAll(newPositions);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.search_result_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.vPositionInfo = convertView.findViewById(R.id.position_info);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.address);
            viewHolder.ivLike = (ImageView) convertView.findViewById(R.id.add_like);
            viewHolder.tvShowMore = (TextView) convertView.findViewById(R.id.show_more);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position) != showMoreObj) {
            viewHolder.tvTitle.setText(positions.get(position).getTitle());
            viewHolder.tvAddress.setText(positions.get(position).getAddress());
            if (likePositionStub != null) {
                viewHolder.ivLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        likePositionStub.likePosition(positions.get(position));
                    }
                });
            }
            viewHolder.tvShowMore.setVisibility(View.GONE);
            viewHolder.vPositionInfo.setVisibility(View.VISIBLE);
        } else {
            viewHolder.vPositionInfo.setVisibility(View.GONE);
            viewHolder.tvShowMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likePositionStub.loadMore(position, SearchFragment.DEFAULT_PAGE_SIZE);
                }
            });
            viewHolder.tvShowMore.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position == positions.size() ? showMoreObj : positions.get(position);
    }

    @Override
    public int getCount() {
        return positions.isEmpty() ? 0 : 1 + positions.size();
    }

    class ViewHolder {
        View vPositionInfo;
        TextView tvTitle;
        TextView tvAddress;
        ImageView ivLike;
        TextView tvShowMore;
    }
}
