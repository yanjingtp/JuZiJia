package com.heretip.juzijia.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button btnAddTenant;
    private RequestQueue mQueue;
    private List<TenantInfoBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant);
        this.btnAddTenant = (Button) findViewById(R.id.btnAddTenant);
        this.lv = (ListView) findViewById(R.id.lv);
        this.layoutTitle = (View) findViewById(R.id.layoutTitle);
        mQueue = Volley.newRequestQueue(this);

        TextView tvTitle = layoutTitle.findViewById(R.id.tv_title);
        Button btnBack = layoutTitle.findViewById(R.id.btn_back);

        final String area_name = getIntent().getStringExtra("area_name");
        final String building_no = getIntent().getStringExtra("building_no");
        final String unit = getIntent().getStringExtra("unit");
        final String floor = getIntent().getStringExtra("floor");
        final String doorplate = getIntent().getStringExtra("doorplate");

        tvTitle.setText("房客");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //加载框
        final AlertDialog dialogLoading = new AlertDialog.Builder(this).create();
        dialogLoading.setCancelable(false);
        dialogLoading.setMessage("玩命加载中……");

        initData(area_name, building_no, unit, floor, doorplate, dialogLoading);

        //删除房客
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(TenantActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("是否删除房客:" + mList.get(position).getTenant_name());
                dialog.setCancelable(false);

                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteTenant(position, area_name, building_no, unit, floor, doorplate);
                        initData(area_name, building_no, unit, floor, doorplate, dialogLoading);
                    }
                });

                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });

        //新增房客
        btnAddTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialogAdd = new AlertDialog.Builder(TenantActivity.this)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", null)
                        .create();
                dialogAdd.setCancelable(false);

                View view = View.inflate(TenantActivity.this, R.layout.layout_dialog_add_tenant, null);
                dialogAdd.setView(view);
                final EditText etRoomNo = view.findViewById(R.id.etRoomNo);
                final EditText etTenantName = view.findViewById(R.id.etTenantName);
                final EditText etGender = view.findViewById(R.id.etGender);
                final EditText etPhone = view.findViewById(R.id.etPhone);
                final EditText etRent = view.findViewById(R.id.etRent);
                final EditText etForegift = view.findViewById(R.id.etForegift);
                final EditText etProperty = view.findViewById(R.id.etProperty);
                final EditText etAmmeter = view.findViewById(R.id.etAmmeter);
                final EditText etInDate = view.findViewById(R.id.etInDate);
                final EditText etNextRent = view.findViewById(R.id.etNextRent);

                dialogAdd.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        //确定
                        dialogAdd.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if (TextUtils.isEmpty(etRoomNo.getText())
                                        && TextUtils.isEmpty(etTenantName.getText())
                                        && TextUtils.isEmpty(etGender.getText())
                                        && TextUtils.isEmpty(etPhone.getText())
                                        && TextUtils.isEmpty(etRent.getText())
                                        && TextUtils.isEmpty(etForegift.getText())
                                        && TextUtils.isEmpty(etProperty.getText())
                                        && TextUtils.isEmpty(etAmmeter.getText())
                                        && TextUtils.isEmpty(etInDate.getText())
                                        && TextUtils.isEmpty(etNextRent.getText())
                                        ) {

                                    Toast.makeText(TenantActivity.this, "请填写租户信息", Toast.LENGTH_SHORT).show();

                                } else {
                                    addTenant(area_name, building_no, unit, floor, doorplate, etRoomNo, etTenantName, etGender, etPhone, etRent, etProperty, etForegift, etAmmeter, etInDate, etNextRent);
                                    initData(area_name, building_no, unit, floor, doorplate, dialogLoading);
                                    dialogAdd.dismiss();
                                }
                            }
                        });

                        //取消
                        dialogAdd.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogAdd.dismiss();
                            }
                        });


                    }
                });
                dialogAdd.show();
            }
        });


    }

    //删除房客
    private void deleteTenant(final int position, final String area_name, final String building_no, final String unit, final String floor, final String doorplate) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.deleteTenantUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("area_name", area_name);
                map.put("building_no", building_no);
                map.put("unit", unit);
                map.put("floor", floor);
                map.put("doorplate", doorplate);
                map.put("tenant_name", mList.get(position).getTenant_name());
                map.put("phone", mList.get(position).getPhone());
                return map;
            }
        };
        mQueue.add(stringRequest);
    }


    //添加房客
    private void addTenant(final String area_name, final String building_no, final String unit, final String floor, final String doorplate, final EditText etRoomNo, final EditText etTenantName, final EditText etGender, final EditText etPhone, final EditText etRent, final EditText etProperty, final EditText etForegift, final EditText etAmmeter, final EditText etInDate, final EditText etNextRent) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.addTenantUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("------", s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);

                map.put("area_name", area_name);
                map.put("building_no", building_no);
                map.put("unit", unit);
                map.put("floor", floor);
                map.put("doorplate", doorplate);
                map.put("room_no", etRoomNo.getText().toString());
                map.put("tenant_name", etTenantName.getText().toString());
                map.put("gender", etGender.getText().toString());
                map.put("phone", etPhone.getText().toString());
                map.put("rent", etRent.getText().toString());
                map.put("property", etProperty.getText().toString());
                map.put("foregift", etForegift.getText().toString());
                map.put("ammeter", etAmmeter.getText().toString());
                map.put("in_date", etInDate.getText().toString());
                map.put("next_rent", etNextRent.getText().toString());
                map.put("modifier", sp.getString("user_name", ""));
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    //获取住户详情
    private void initData(final String area_name, final String building_no, final String unit, final String floor, final String doorplate, final AlertDialog dialog) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.tenantListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(s).getAsJsonArray();

                Gson gson = new Gson();
                mList = new ArrayList<>();
                for (JsonElement tenantInfo : jsonArray) {
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
        }) {
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
