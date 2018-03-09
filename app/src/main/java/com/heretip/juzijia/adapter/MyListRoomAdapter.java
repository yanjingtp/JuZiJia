package com.heretip.juzijia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heretip.juzijia.R;
import com.heretip.juzijia.bean.RoomInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/1 0001.
 */

public class MyListRoomAdapter extends BaseAdapter {
   private Context context;
    private List<RoomInfoBean> mList;

    public MyListRoomAdapter(Context context, List<RoomInfoBean> mList) {
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
            convertView = View.inflate(context, R.layout.layout_room_list_item, null);
            viewHolder.tv = convertView.findViewById(R.id.tv);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String area_name = mList.get(position).getArea_name();
        String building_no = mList.get(position).getBuilding_no();
        String uni =  mList.get(position).getUnit();
        String floor =  mList.get(position).getFloor();
        String doorplate =  mList.get(position).getDoorplate();

        viewHolder.tv.setText(area_name+building_no+"号楼"+uni+"单元"+floor+"楼"+doorplate);
        viewHolder.tv.setTextSize(20);
        viewHolder.tv.setTextColor( context.getResources().getColor(R.color.colorAccent) );

        return convertView;
    }

    private class ViewHolder{
        TextView tv;
    }
}
