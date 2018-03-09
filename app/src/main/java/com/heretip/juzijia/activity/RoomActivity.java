package com.heretip.juzijia.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
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
import com.heretip.juzijia.adapter.MyListRoomAdapter;
import com.heretip.juzijia.bean.RoomInfoBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomActivity extends AppCompatActivity {

    private View layoutTitle;
    private android.widget.ListView lv;
    private List<RoomInfoBean> mList;
    private RequestQueue mQueue;
    private Button btnAddRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        this.btnAddRoom = (Button) findViewById(R.id.btnAddRoom);
        this.lv = (ListView) findViewById(R.id.lv);
        this.layoutTitle = (View) findViewById(R.id.layoutTitle);

        mQueue = Volley.newRequestQueue(this);
        final String area_name = getIntent().getStringExtra("area_name");

        TextView tvTitle = layoutTitle.findViewById(R.id.tv_title);
        tvTitle.setText("房间");
        Button btnBack = layoutTitle.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //加载框
        final AlertDialog loadingDialog = new AlertDialog.Builder(this).create();
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage("玩命加载中……");
        initData(area_name, loadingDialog);

        //添加房间
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog mDialog = new AlertDialog.Builder(RoomActivity.this)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", null)
                        .create();
                View view = View.inflate(RoomActivity.this, R.layout.layout_dialog_add_room, null);
                final EditText etBuildingNo = view.findViewById(R.id.etBuildingNO);
                final EditText etUnit = view.findViewById(R.id.etUnit);
                final EditText etFloor = view.findViewById(R.id.etFloor);
                final EditText etDoorplate = view.findViewById(R.id.etDoorplate);

                mDialog.setView(view);


                mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        //确定
                        mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (TextUtils.isEmpty(etBuildingNo.getText().toString())
                                        && TextUtils.isEmpty(etUnit.getText().toString())
                                        && TextUtils.isEmpty(etFloor.getText().toString())
                                        && TextUtils.isEmpty(etDoorplate.getText().toString())
                                        ) {
                                    Toast.makeText(RoomActivity.this, "请填写房间信息", Toast.LENGTH_SHORT).show();
                                } else {
                                    addRoom(area_name, loadingDialog, etBuildingNo, etUnit, etFloor, etDoorplate);
                                    mDialog.dismiss();
                                }
                            }
                        });

                        //取消
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


        //listView 短点击，进入
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RoomActivity.this, TenantActivity.class);
                intent.putExtra("area_name", area_name);
                intent.putExtra("building_no", mList.get(position).getBuilding_no());
                intent.putExtra("unit", mList.get(position).getUnit());
                intent.putExtra("floor", mList.get(position).getFloor());
                intent.putExtra("doorplate", mList.get(position).getDoorplate());

                startActivity(intent);
            }
        });


        //listView 长点击，删除
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(RoomActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("是否删除");
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRoom(area_name, loadingDialog, position);
                    }
                });

                dialog.show();

                return true;
            }
        });


    }

    //删除房间
    private void deleteRoom(final String area_name, final AlertDialog loadingDialog, final int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.deleteRoomUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("0")) {
                    Toast.makeText(RoomActivity.this, "该房间有租户，无法删除", Toast.LENGTH_SHORT).show();
                } else {
                    initData(area_name, loadingDialog);
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
                map.put("area_name", area_name);
                map.put("building_no", mList.get(position).getBuilding_no());
                map.put("unit", mList.get(position).getUnit());
                map.put("floor", mList.get(position).getFloor());
                map.put("doorplate", mList.get(position).getDoorplate());
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    //添加房间
    private void addRoom(final String area_name, final AlertDialog loadingDialog, final EditText etBuildingNo, final EditText etUnit, final EditText etFloor, final EditText etDoorplate) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.addRoomUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                if (s.equals("1")) {
                    initData(area_name, loadingDialog);
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
                map.put("area_name", area_name);
                map.put("building_no", etBuildingNo.getText().toString());
                map.put("unit", etUnit.getText().toString());
                map.put("floor", etFloor.getText().toString());
                map.put("doorplate", etDoorplate.getText().toString());
                return map;
            }
        };

        mQueue.add(stringRequest);
    }

    private void initData(final String area_name, final AlertDialog dialog) {
        //获取房间详情
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.roomListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(s).getAsJsonArray();
                Gson gson = new Gson();
                mList = new ArrayList<>();
                for (JsonElement roomInfo : jsonArray) {

                    RoomInfoBean roomInfoBean = gson.fromJson(roomInfo, RoomInfoBean.class);
                    mList.add(roomInfoBean);

                }
                MyListRoomAdapter myListRoomAdapter = new MyListRoomAdapter(RoomActivity.this, mList);
                lv.setAdapter(myListRoomAdapter);

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
                return map;
            }
        };

        mQueue.add(stringRequest);
    }
}
