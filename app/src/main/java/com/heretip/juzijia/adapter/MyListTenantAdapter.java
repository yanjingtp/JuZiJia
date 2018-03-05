package com.heretip.juzijia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heretip.juzijia.R;
import com.heretip.juzijia.bean.TenantInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/1 0001.
 */

public class MyListTenantAdapter extends BaseAdapter {
    private Context context;
    private List<TenantInfoBean> mList;

    public MyListTenantAdapter(Context context, List<TenantInfoBean> mList) {
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
            convertView = View.inflate(context, R.layout.layout_tenant_list_item, null);
            viewHolder.room_no = convertView.findViewById(R.id.tvRoomNo);
            viewHolder.tenant_name = convertView.findViewById(R.id.tvName);
            viewHolder.gender = convertView.findViewById(R.id.tvGender);
            viewHolder.phone = convertView.findViewById(R.id.tvPhone);
            viewHolder.rent = convertView.findViewById(R.id.tvRent);
            viewHolder.property = convertView.findViewById(R.id.tvProperty);
            viewHolder.foregift = convertView.findViewById(R.id.tvForegift);
            viewHolder.ammeter = convertView.findViewById(R.id.tvAmmeter);
            viewHolder.in_date = convertView.findViewById(R.id.tvInDate);
            viewHolder.next_rent = convertView.findViewById(R.id.tvNextRent);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String room_no = "房间" + mList.get(position).getRoom_no();
        String teant_name = mList.get(position).getTenant_name();
        String gender = mList.get(position).getGender();
        String phone = mList.get(position).getPhone();
        String rent = "房租" + mList.get(position).getRent() + "/月";
        String in_date = "入住时间:" + mList.get(position).getIn_date();
        String property = "物业费:" + mList.get(position).getProperty();
        String next_rent = "下次交房租时间:" + mList.get(position).getNext_rent();
        String foregift = "押金:" + mList.get(position).getForegift();
        String ammeter = "电表:" + mList.get(position).getAmmeter();


        viewHolder.room_no.setText(room_no);
        viewHolder.tenant_name.setText(teant_name);
        viewHolder.gender.setText(gender);
        viewHolder.phone.setText(phone);
        viewHolder.rent.setText(rent);
        viewHolder.in_date.setText(in_date);
        viewHolder.property.setText(property);
        viewHolder.next_rent.setText(next_rent);
        viewHolder.foregift.setText(foregift);
        viewHolder.ammeter.setText(ammeter);

        return convertView;
    }

    private class ViewHolder {

        TextView room_no;
        TextView tenant_name;
        TextView gender;
        TextView phone;
        TextView rent;
        TextView property;
        TextView foregift;
        TextView ammeter;
        TextView in_date;
        TextView next_rent;

    }
}
