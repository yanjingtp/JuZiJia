package com.heretip.juzijia.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.heretip.juzijia.Common;
import com.heretip.juzijia.R;
import com.heretip.juzijia.adapter.MyListTenantAdapter;
import com.heretip.juzijia.bean.TenantInfoBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TenantActivity extends AppCompatActivity {

    private View layoutTitle;
    private android.widget.ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant);
        this.lv = (ListView) findViewById(R.id.lv);
        this.layoutTitle = (View) findViewById(R.id.layoutTitle);

        TextView tvTitle = layoutTitle.findViewById(R.id.tv_title);
        Button btnBack = layoutTitle.findViewById(R.id.btn_back);

        final String area_name = getIntent().getStringExtra("area_name");
        final  String building_no = getIntent().getStringExtra("building_no");
        final  String unit = getIntent().getStringExtra("unit");
        final  String floor = getIntent().getStringExtra("floor");
        final  String doorplate = getIntent().getStringExtra("doorplate");

        tvTitle.setText("房客");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //加载框
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setCancelable(false);
        dialog.setMessage("玩命加载中……");
        dialog.show();
        initData(area_name, building_no, unit, floor, doorplate, dialog);


    }

    private void initData(final String area_name, final String building_no, final String unit, final String floor, final String doorplate, final AlertDialog dialog) {
        //获取住户详情
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.tenantListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(s).getAsJsonArray();

                Gson gson = new Gson();
                List<TenantInfoBean> mList = new ArrayList<>();
                for (JsonElement tenantInfo: jsonArray) {
                    TenantInfoBean tenantInfoBean = gson.fromJson(tenantInfo, TenantInfoBean.class);
                    mList.add(tenantInfoBean);
                }


                lv.setAdapter(new MyListTenantAdapter(TenantActivity.this, mList));

                //关闭加载框
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("area_name", area_name);
                map.put("building_no", building_no);
                map.put("unit", unit);
                map.put("floor", floor);
                map.put("doorplate", doorplate);

                return map;
            }
        };

        mQueue.add(stringRequest);
    }
}
