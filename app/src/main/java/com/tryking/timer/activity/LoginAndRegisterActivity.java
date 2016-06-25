package com.tryking.timer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tryking.timer.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginAndRegisterActivity extends AppCompatActivity {

    @Bind(R.id.et_login_phone)
    EditText etLoginPhone;
    @Bind(R.id.et_login_password)
    EditText etLoginPassword;
    @Bind(R.id.ll_login)
    LinearLayout llLogin;
    @Bind(R.id.et_register_phone)
    EditText etRegisterPhone;
    @Bind(R.id.tv_register_get_verification)
    TextView tvRegisterGetVerification;
    @Bind(R.id.et_register_verification_code)
    EditText etRegisterVerificationCode;
    @Bind(R.id.et_register_password)
    EditText etRegisterPassword;
    @Bind(R.id.ll_register)
    LinearLayout llRegister;
    @Bind(R.id.bt_login)
    Button btLogin;
    @Bind(R.id.bt_forget_password)
    Button btForgetPassword;
    @Bind(R.id.bt_have_account)
    Button btHaveAccount;
    @Bind(R.id.bt_establish_account)
    Button btEstablishAccount;
    @Bind(R.id.bt_qq_login)
    Button btQqLogin;
    @Bind(R.id.bt_wechat_login)
    Button btWechatLogin;
    @Bind(R.id.bt_sina_login)
    Button btSinaLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_forget_password, R.id.bt_have_account, R.id.bt_establish_account, R.id.bt_login})
    void click(View v) {
        switch (v.getId()) {
            case R.id.bt_have_account:
                llLogin.setVisibility(View.VISIBLE);
                llRegister.setVisibility(View.GONE);
                btLogin.setText("登陆");
                btHaveAccount.setVisibility(View.GONE);
                btEstablishAccount.setVisibility(View.VISIBLE);
                btForgetPassword.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_establish_account:
                llLogin.setVisibility(View.GONE);
                llRegister.setVisibility(View.VISIBLE);
                btLogin.setText("注册");
                btHaveAccount.setVisibility(View.VISIBLE);
                btEstablishAccount.setVisibility(View.GONE);
                btForgetPassword.setVisibility(View.GONE);
                break;
            case R.id.bt_login:
                startActivity(new Intent(LoginAndRegisterActivity.this, MainActivity.class));
                finish();
            default:
                break;
        }
    }
}
