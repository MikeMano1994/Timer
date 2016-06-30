package com.tryking.timer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tryking.timer.R;
import com.tryking.timer.base.SystemInfo;
import com.tryking.timer.bean.thirdInfo.QQUserInfo;
import com.tryking.timer.utils.TT;
//import com.umeng.socialize.UMAuthListener;
//import com.umeng.socialize.UMShareAPI;
//import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

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
    ImageView btQqLogin;
    @Bind(R.id.bt_wechat_login)
    ImageView btWechatLogin;
    @Bind(R.id.bt_sina_login)
    ImageView btSinaLogin;
//    private UMShareAPI mShareAPI;//友盟三方登陆授权
//    private SHARE_MEDIA platform;//分享平台

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
//        mShareAPI = UMShareAPI.get(this);
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
                if (btLogin.getText().toString().equals("注册")) {
//                    //打开注册页面
//                    final RegisterPage registerPage = new RegisterPage();
//                    registerPage.setRegisterCallback(new EventHandler() {
//                        public void afterEvent(int event, int result, Object data) {
//// 解析注册结果
//                            if (result == SMSSDK.RESULT_COMPLETE) {
//                                @SuppressWarnings("unchecked")
//                                HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
//                                String country = (String) phoneMap.get("country");
//                                String phone = (String) phoneMap.get("phone");
//                                Logger.e(country + "::" + phone);
//// 提交用户信息
////                                registerUser(country, phone);
//                            }
//                        }
//                    });
//                    registerPage.show(this);
                } else {
                    SystemInfo.getInstance(getApplicationContext()).setAccount("");
                    SystemInfo.getInstance(getApplicationContext()).setPortraitUrl("");
                    SystemInfo.getInstance(getApplicationContext()).setQQ("");
                    startActivity(new Intent(LoginAndRegisterActivity.this, MainActivity.class));
                    finish();
                }
                break;
            case R.id.bt_qq_login:
//                platform = SHARE_MEDIA.QQ;
//                mShareAPI.doOauthVerify(this, platform, umLoginAuthListener);
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
//                platform = SHARE_MEDIA.SINA;
//                mShareAPI.doOauthVerify(this, platform, umLoginAuthListener);
                break;

            default:
                break;
        }
    }

//    /*
//    友盟三方登陆
//     */
//    private UMAuthListener umLoginAuthListener = new UMAuthListener() {
//        @Override
//        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//            Logger.e("complete" + "i:" + i + ":map:" + map.toString());
//            switch (share_media) {
//                case QQ:
//                    TT.showShort(getApplicationContext(), "QQ登陆成功");
//                    getUserInfo(share_media);
//                    break;
//                case WEIXIN:
//                    TT.showShort(getApplicationContext(), "WEIXIN succeed");
//                    break;
//                case SINA:
//                    TT.showShort(getApplicationContext(), "新浪微博登陆成功");
//                    getUserInfo(share_media);
//                    break;
//            }
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//            Logger.e("error");
//            switch (share_media) {
//                case QQ:
//                    TT.showShort(getApplicationContext(), "QQ登陆错误");
//                    break;
//                case WEIXIN:
//                    TT.showShort(getApplicationContext(), "微信登陆错误");
//                    break;
//                case SINA:
//                    TT.showShort(getApplicationContext(), "新浪登陆错误");
//                    break;
//            }
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA share_media, int i) {
//            switch (share_media) {
//                case QQ:
//                    TT.showShort(getApplicationContext(), "取消QQ授权");
//                    break;
//                case WEIXIN:
//                    TT.showShort(getApplicationContext(), "取消微信授权");
//                    break;
//                case SINA:
//                    TT.showShort(getApplicationContext(), "取消新浪微博授权");
//                    break;
//            }
//        }
//    };
//
//    /*
//    获取QQ用户信息
//     */
//    private void getUserInfo(SHARE_MEDIA share_media) {
//        mShareAPI.getPlatformInfo(LoginAndRegisterActivity.this, share_media, umGetInfoAuthListener);
//    }
//
//    /*
//    友盟三方获取用户信息
//     */
//    private UMAuthListener umGetInfoAuthListener = new UMAuthListener() {
//        @Override
//        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//            Logger.e(map.toString());
//            switch (share_media) {
//                case QQ:
//                    QQUserInfo qqUserInfo = new QQUserInfo();
//                    SystemInfo.getInstance(getApplicationContext()).setQQ(map.get("openid"));
//                    SystemInfo.getInstance(getApplicationContext()).setQQName(map.get("screen_name"));
//                    SystemInfo.getInstance(getApplicationContext()).setAccount(map.get("screen_name"));
//                    SystemInfo.getInstance(getApplicationContext()).setPortraitUrl(map.get("profile_image_url"));
//                    break;
//                case SINA:
//                    try {
//                        String result = map.get("result");
//                        JSONObject sinaUserInfo = null;
//                        sinaUserInfo = new JSONObject(result);
//                        SystemInfo.getInstance(getApplicationContext()).setSina((String) sinaUserInfo.get("idstr"));
//                        SystemInfo.getInstance(getApplicationContext()).setSinaName((String) sinaUserInfo.get("screen_name"));
//                        SystemInfo.getInstance(getApplicationContext()).setAccount((String) sinaUserInfo.get("screen_name"));
//                        SystemInfo.getInstance(getApplicationContext()).setPortraitUrl((String) sinaUserInfo.get("profile_image_url"));
//                        SystemInfo.getInstance(getApplicationContext()).setSignature((String) sinaUserInfo.get("description"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//            startActivity(new Intent(LoginAndRegisterActivity.this, MainActivity.class));
//            finish();
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA share_media, int i) {
//
//        }
//    };
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        mShareAPI.onActivityResult(requestCode, resultCode, data);
//    }
}
