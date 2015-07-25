package com.leili.imhere.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leili.imhere.R;

import java.util.List;

/**
 * Created by Lei.Li on 7/24/15 10:57 AM.
 */
public class SearchResultAdapter extends BaseAdapter {
    private Context context;
    private List<Position> positions;

    public SearchResultAdapter (Context context, List<Position> positions) {
        this.context = context;
        this.positions = positions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.search_result_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.address);
            viewHolder.ivLike = (ImageView) convertView.findViewById(R.id.add_like);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(positions.get(position).getTitle());
        viewHolder.tvAddress.setText(positions.get(position).getAddress());
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return positions.get(position);
    }

    @Override
    public int getCount() {
        return positions.size();
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvAddress;
        ImageView ivLike;
    }
}
