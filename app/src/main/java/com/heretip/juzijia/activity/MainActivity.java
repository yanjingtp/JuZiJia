package com.heretip.juzijia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.heretip.juzijia.R;
import com.heretip.juzijia.adapter.MyGridMainAdapter;

public class MainActivity extends AppCompatActivity {

    private View layoutTitle;
    private android.widget.GridView gvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.gvMain = (GridView) findViewById(R.id.gvMain);
        this.layoutTitle = (View) findViewById(R.id.layoutTitle);

        String[] items = {"房屋管理", "快速添加", "快捷搜索", "个人中心"};

        TextView tvTitle = layoutTitle.findViewById(R.id.tv_title);
        Button btnBack = layoutTitle.findViewById(R.id.btn_back);

        tvTitle.setTextSize(20);
        tvTitle.setText("主页");
        btnBack.setVisibility(View.INVISIBLE);

        //gridView添加数据
        MyGridMainAdapter myGridAdapter = new MyGridMainAdapter(this, items);
        gvMain.setAdapter(myGridAdapter);

        //gridView 短点击，进入
        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(MainActivity.this,AreaActivity.class));
                }
            }
        });

        //gridView长点击,删除
        gvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });

    }
}
