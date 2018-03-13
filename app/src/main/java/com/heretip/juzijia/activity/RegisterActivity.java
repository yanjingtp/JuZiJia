package com.heretip.juzijia.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.heretip.juzijia.util.CodeUtils;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private View layoutTitle;
    private android.widget.EditText etUserName;
    private android.widget.EditText etPassword;
    private android.widget.Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.btnLogin = (Button) findViewById(R.id.btn_login);
        this.etPassword = (EditText) findViewById(R.id.et_password);
        this.etUserName = (EditText) findViewById(R.id.et_user_name);
        this.layoutTitle = (View) findViewById(R.id.layout_title);
        final Button btnPwdSwitch = findViewById(R.id.btnPwdSwitch);
        final EditText etCode = findViewById(R.id.etCode);
        final ImageView ivCode = findViewById(R.id.ivCode);


        TextView tvTitle = layoutTitle.findViewById(R.id.tv_title);
        Button btnBack = layoutTitle.findViewById(R.id.btn_back);
        tvTitle.setText("注册");
        tvTitle.setTextSize(20);
        //显示验证码
        ivCode.setImageBitmap(CodeUtils.getInstance().createBitmap());
        ivCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivCode.setImageBitmap(CodeUtils.getInstance().createBitmap());
            }
        });

        //密码的明文及密文切换
        btnPwdSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {

                    //明文显示
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    btnPwdSwitch.setSelected(true);

                } else {

                    //密文显示
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnPwdSwitch.setSelected(false);
                }


                //光标在最后
                etPassword.setSelection(etPassword.length());

            }
        });


        //返回
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //注册
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //校验验证码
                if (!etCode.getText().toString().toLowerCase().equals(CodeUtils.getInstance().getCode().toLowerCase())) {
                    Toast.makeText(RegisterActivity.this, "验证码错误，请重新输入", Toast.LENGTH_SHORT).show();
                    ivCode.setImageBitmap(CodeUtils.getInstance().createBitmap());
                } else {

                    if (TextUtils.isEmpty(etUserName.getText()) || TextUtils.isEmpty(etPassword.getText())) {
                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.login_empty), Toast.LENGTH_SHORT).show();
                    } else {
                        RequestQueue mQueue = Volley.newRequestQueue(RegisterActivity.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.registerUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                if (s.equals("2")) {  //用户名已存在
                                    Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                                } else if (s.equals("1")) {  //注册成功
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    finish();
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
                                map.put("user_name", etUserName.getText().toString());   //用户名
                                map.put("password", etPassword.getText().toString());    //密码
                                return map;
                            }
                        };

                        mQueue.add(stringRequest);
                    }
                }
            }
        });


    }
}
