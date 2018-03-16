package com.heretip.juzijia.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class MyViewPagerAdapter extends PagerAdapter {

    List<View> viewList;
    String[] titleItems;

    public MyViewPagerAdapter(List<View> viewList, String[] titleItems) {
        this.viewList = viewList;
        this.titleItems = titleItems;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleItems[position];
    }


}
