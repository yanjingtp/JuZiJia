package com.heretip.juzijia.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.heretip.juzijia.Common;
import com.heretip.juzijia.R;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private android.widget.EditText etUserName;
    private android.widget.EditText etPassword;
    private android.widget.Button btnLogin;
    private android.widget.CheckBox cbRemember;
    private Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.btnCreate = (Button) findViewById(R.id.btn_create);
        this.cbRemember = (CheckBox) findViewById(R.id.cb_remember);
        this.btnLogin = (Button) findViewById(R.id.btn_login);
        this.etPassword = (EditText) findViewById(R.id.et_password);
        this.etUserName = (EditText) findViewById(R.id.et_user_name);
        final Button btnPwdSwitch = findViewById(R.id.btnPwdSwitch);

        //密码的明文及密文切换
        btnPwdSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    //切换到明文
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    btnPwdSwitch.setSelected(true);
                } else {
                    //切换到密文
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnPwdSwitch.setSelected(false);
                }
                //光标显示到最后
                etPassword.setSelection(etPassword.length());
            }
        });


        //登录
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etUserName.getText()) || TextUtils.isEmpty(etPassword.getText())) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_empty), Toast.LENGTH_SHORT).show();
                } else {

                    //登录
                    RequestQueue mQueue = Volley.newRequestQueue(LoginActivity.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.loginUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (s.equals("1")) {

                                SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
                                //若是记住密码
                                if (cbRemember.isChecked()) {
                                    sp.edit().putBoolean("isFirstLogin", false).commit();
                                }
                                //保存用户名
                                sp.edit().putString("user_name", etUserName.getText().toString()).commit();

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else if (s.equals("2")) {
                                Toast.makeText(LoginActivity.this, "用户名不存在，请注册", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_error), Toast.LENGTH_SHORT).show();
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
                            map.put("user_name", etUserName.getText().toString());  //用户名
                            map.put("password", etPassword.getText().toString());   //密码

                            return map;
                        }
                    };

                    mQueue.add(stringRequest);

                }
            }
        });

        //注册
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            }
        });
    }
}
