package com.tryking.timer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tryking.timer.R;
import com.tryking.timer.bean.thirdInfo.QQUserInfo;
import com.tryking.timer.utils.TT;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

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
    private UMShareAPI mShareAPI;//友盟三方登陆授权
    private SHARE_MEDIA platform;//分享平台

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mShareAPI = UMShareAPI.get(this);
    }

    @OnClick({R.id.bt_forget_password, R.id.bt_have_account, R.id.bt_establish_account,
            R.id.bt_login, R.id.bt_qq_login, R.id.bt_wechat_login, R.id.bt_sina_login})
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
                break;
            case R.id.bt_qq_login:
//                if (mShareAPI.isAuthorize(this, SHARE_MEDIA.QQ)) {
//                    TT.showShort(getApplicationContext(), "QQ已授权");
//                } else {
                platform = SHARE_MEDIA.QQ;
                mShareAPI.doOauthVerify(this, platform, umLoginAuthListener);
//                }
                break;
            case R.id.bt_wechat_login:
//                if (mShareAPI.isAuthorize(this, SHARE_MEDIA.WEIXIN)) {
//                    TT.showShort(getApplicationContext(), "微信已授权");
//                } else {
//                    platform = SHARE_MEDIA.WEIXIN;
//                    mShareAPI.doOauthVerify(this, platform, umAuthListener);
//                }
                //微信登陆需要认证，一年300元
                TT.showShort(getApplicationContext(), "暂时不支持微信登陆，请耐心等待...");
                break;
            case R.id.bt_sina_login:
                if (mShareAPI.isAuthorize(this, SHARE_MEDIA.SINA)) {
                    TT.showShort(getApplicationContext(), "新浪已授权");
                } else {
                    platform = SHARE_MEDIA.SINA;
                    mShareAPI.doOauthVerify(this, platform, umLoginAuthListener);
                }
            default:
                break;
        }
    }

    /*
    友盟三方登陆
     */
    private UMAuthListener umLoginAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Logger.e("complete" + "i:" + i + ":map:" + map.toString());
            switch (share_media) {
                case QQ:
                    TT.showShort(getApplicationContext(), "QQ succeed");
                    getQQUserInfo();
                    break;
                case WEIXIN:
                    TT.showShort(getApplicationContext(), "WEIXIN succeed");
                    break;
                case SINA:
                    TT.showShort(getApplicationContext(), "SINA succeed");
                    break;
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Logger.e("error");
            switch (share_media) {
                case QQ:
                    TT.showShort(getApplicationContext(), "QQ error");
                    break;
                case WEIXIN:
                    TT.showShort(getApplicationContext(), "WEIXIN error");
                    break;
                case SINA:
                    TT.showShort(getApplicationContext(), "SINA error");
                    break;
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Logger.e("cancel");
            switch (share_media) {
                case QQ:
                    TT.showShort(getApplicationContext(), "QQ cancel");
                    break;
                case WEIXIN:
                    TT.showShort(getApplicationContext(), "WEIXIN cancel");
                    break;
                case SINA:
                    TT.showShort(getApplicationContext(), "SINA cancel");
                    break;
            }
        }
    };

    /*
    获取QQ用户信息
     */
    private void getQQUserInfo() {
        mShareAPI.getPlatformInfo(LoginAndRegisterActivity.this, SHARE_MEDIA.QQ, umGetInfoAuthListener);
    }

    /*
    友盟三方获取用户信息
     */
    private UMAuthListener umGetInfoAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            switch (share_media) {
                case QQ:
                    QQUserInfo qqUserInfo = new QQUserInfo();
                    qqUserInfo.setScreen_name(map.get("screen_name"));
                    qqUserInfo.setGender(map.get("gender"));
                    qqUserInfo.setProfile_image_url(map.get("profile_image_url"));
                    break;
                case SINA:
                    break;
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }
}
