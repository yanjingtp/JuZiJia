package com.heretip.juzijia.activity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.heretip.juzijia.R;
import com.heretip.juzijia.adapter.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private android.support.v4.view.ViewPager viewPager;
    private android.support.design.widget.TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);

        String[] titleItems = {"房屋管理","个人中心"};


        LocalActivityManager localActivityManager = new LocalActivityManager(this, true);
        localActivityManager.dispatchCreate(savedInstanceState);
        View viewArea = localActivityManager.startActivity("1", new Intent(MainActivity.this, AreaActivity.class)).getDecorView();






      //  View viewArea = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_room, null);
        View viewCenter = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_my_center, null);

        //显示页面list
        List<View> viewList = new ArrayList<>();
        viewList.add(viewArea);
        viewList.add(viewCenter);




        //添加tabLayou的选项卡
        tabLayout.addTab(tabLayout.newTab().setText(titleItems[0]));
        tabLayout.addTab(tabLayout.newTab().setText(titleItems[1]));

        viewPager.setAdapter(new MyViewPagerAdapter(viewList,titleItems));
        //管理tabLayout及viewPager
        tabLayout.setupWithViewPager(viewPager);








    }
}
