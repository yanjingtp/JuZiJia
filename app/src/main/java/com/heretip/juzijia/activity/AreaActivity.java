package com.heretip.juzijia.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.heretip.juzijia.Common;
import com.heretip.juzijia.R;
import com.heretip.juzijia.adapter.MyGridAreaAdapter;
import com.heretip.juzijia.bean.AreaNameBean;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaActivity extends AppCompatActivity {

    private View layoutTitle;
    private android.widget.GridView gvArea;
    private List<AreaNameBean> mList;
    private Button btnAdd;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);
        this.btnAdd = (Button) findViewById(R.id.btnAdd);
        this.gvArea = (GridView) findViewById(R.id.gvArea);
        this.layoutTitle = (View) findViewById(R.id.layoutTitle);

        TextView tvTitle = layoutTitle.findViewById(R.id.tv_title);
        final Button btnBack = layoutTitle.findViewById(R.id.btn_back);

        mQueue = Volley.newRequestQueue(AreaActivity.this);

        tvTitle.setText("小区");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //加载框
        final AlertDialog loadingDialog = new AlertDialog.Builder(this).create();
        loadingDialog.setMessage("玩命加载中……");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        initData(loadingDialog);

        //新增小区
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog mDialog = new AlertDialog.Builder(AreaActivity.this)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", null)
                        .create();
                View view = View.inflate(AreaActivity.this, R.layout.layout_dialog_add_area, null);
                final EditText etAddArea = view.findViewById(R.id.etAddArea);

                mDialog.setView(view);
                mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        //确定按键
                        mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                for (AreaNameBean areaNameBean : mList) {
                                    if (areaNameBean.getName().equals(etAddArea.getText().toString())) {

                                        Toast.makeText(AreaActivity.this, "该小区已存在", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }

                                addArea(etAddArea, loadingDialog);
                                mDialog.dismiss();

                            }

                        });
                        //取消按键
                        mDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        });
                    }
                });


                mDialog.show();


            }
        });




        //gridView 短点击，进入
        gvArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AreaActivity.this, RoomActivity.class);
                intent.putExtra("area_name", mList.get(position).getName());
                startActivity(intent);
            }
        });

        //gridView 长点击，删除
        gvArea.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(AreaActivity.this);
                deleteDialog.setTitle("提示");
                deleteDialog.setMessage("是否删除小区" + mList.get(position).getName());
                deleteDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {

                        deleteArea(position, loadingDialog);
                    }
                });

                deleteDialog.show();


                return true;
            }
        });

    }

    //添加小区
    private void addArea(final EditText etAddArea, final AlertDialog dialog) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.addAreaUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("1")) {
                    initData(dialog);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("area_name", etAddArea.getText().toString());
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    //删除小区
    private void deleteArea(final int position, final AlertDialog dialog) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.deleteAreaUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("0")) {
                    Toast.makeText(AreaActivity.this, "该小区有租户，无法删除", Toast.LENGTH_SHORT).show();
                } else {
                    initData(dialog);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("area_name", mList.get(position).getName());
                return map;
            }

        };

        mQueue.add(stringRequest);
    }


    //获取小区列表
    private void initData(final AlertDialog dialog) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Common.areaListUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                JsonParser parser = new JsonParser();
                JsonArray array = parser.parse(jsonArray.toString()).getAsJsonArray();

                Gson gson = new Gson();
                mList = new ArrayList<>();
                for (JsonElement name : array) {
                    AreaNameBean areaNameBean = gson.fromJson(name, AreaNameBean.class);
                    mList.add(areaNameBean);
                }

                //显示数据
                MyGridAreaAdapter myGridAreaAdapter = new MyGridAreaAdapter(AreaActivity.this, mList);
                gvArea.setAdapter(myGridAreaAdapter);
                //关闭加载框
                dialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        mQueue.add(jsonArrayRequest);
    }
}
