package com.heretip.juzijia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heretip.juzijia.R;

/**
 * Created by Administrator on 2018/2/28 0028.
 */

public class MyGridMainAdapter extends BaseAdapter {
    private Context context;
    private String[] items;


    public MyGridMainAdapter(Context context, String[] items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.layout_grid_item, null);
            viewHolder.tv = convertView.findViewById(R.id.tv);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv.setText(items[position]);
        viewHolder.tv.setTextSize(30);
        viewHolder.tv.setTextColor( context.getResources().getColor(R.color.colorAccent) );


        return convertView;
    }

    private class ViewHolder{
        TextView tv;
    }
}
