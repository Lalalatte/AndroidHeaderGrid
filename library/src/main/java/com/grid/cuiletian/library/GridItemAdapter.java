package com.grid.cuiletian.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cuiletian on 15/10/8.
 */
public class GridItemAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private ViewHolder holder;
    private final Context mContext;
    private final List<GridContent> contentList;
//    List<Boolean> selectList;

    public GridItemAdapter(Context context, List<GridContent> list) {
        this.contentList = list;
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView contentTv;
        ImageView selectImg;
    }


    @Override
    public int getCount() {
        return contentList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.grid_item, parent, false);
            convertView.setLayoutParams(new GridView.LayoutParams(ScreenUtil.dip2px(mContext, 70), ScreenUtil.dip2px(mContext, 65)));

            holder.contentTv = (TextView) convertView.findViewById(R.id.content);
            holder.selectImg = (ImageView) convertView.findViewById(R.id.ic_checkbox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GridContent item = contentList.get(position);
        holder.contentTv.setText(item.getContentStr());

        if (item.isSelect()) {
            holder.selectImg.setVisibility(View.VISIBLE);
        } else {
            holder.selectImg.setVisibility(View.GONE);
        }
        holder.contentTv.setBackgroundColor(item.getBgColor());
        return convertView;
    }

    public void setStateSelected(int position, boolean isSelect) {

        contentList.get(position).setIsSelect(isSelect);
        if (isSelect){
            holder.contentTv.setBackgroundColor(mContext.getResources().getColor(R.color.state_header_booking));
        }
        else{
            holder.contentTv.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        notifyDataSetChanged();
    }
}
