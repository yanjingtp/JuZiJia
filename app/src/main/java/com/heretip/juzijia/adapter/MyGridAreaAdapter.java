package com.heretip.juzijia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heretip.juzijia.R;
import com.heretip.juzijia.bean.AreaNameBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/1 0001.
 */

public class MyGridAreaAdapter extends BaseAdapter {
    private Context context;
    private List<AreaNameBean> mList;

    public MyGridAreaAdapter(Context context, List<AreaNameBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
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

        viewHolder.tv.setText(mList.get(position).getName());
        viewHolder.tv.setTextSize(30);
        viewHolder.tv.setTextColor( context.getResources().getColor(R.color.colorAccent) );
        return convertView;
    }


    private class ViewHolder {
        TextView tv;
    }
}
