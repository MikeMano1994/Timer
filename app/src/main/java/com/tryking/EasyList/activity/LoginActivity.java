package com.tryking.EasyList.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.loginBean.Event;
import com.tryking.EasyList._bean.loginBean.LoginReturnBean;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.global.ApplicationGlobal;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.global.InterfaceURL;
import com.tryking.EasyList.network.JsonBeanRequest;
import com.tryking.EasyList.utils.SPUtils;
import com.tryking.EasyList.utils.TT;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.bt_qq_login)
    ImageView btQqLogin;
    @Bind(R.id.bt_wechat_login)
    ImageView btWechatLogin;
    @Bind(R.id.bt_sina_login)
    ImageView btSinaLogin;
    @Bind(R.id.bt_no_login)
    Button btNoLogin;

    private UMShareAPI mShareAPI;//友盟三方登陆授权
    private SHARE_MEDIA platform;//分享平台
    private String portraitUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mShareAPI = UMShareAPI.get(this);
    }

    @OnClick({R.id.bt_qq_login, R.id.bt_wechat_login, R.id.bt_sina_login, R.id.bt_no_login})
    void click(View v) {
        switch (v.getId()) {
            case R.id.bt_qq_login:
                platform = SHARE_MEDIA.QQ;
                mShareAPI.doOauthVerify(this, platform, umLoginAuthListener);
                break;
            case R.id.bt_wechat_login:
                //微信登陆需要认证，一年300元
                TT.showShort(getApplicationContext(), "暂时不支持微信登陆，请耐心等待...");
                break;
            case R.id.bt_sina_login:
                platform = SHARE_MEDIA.SINA;
                mShareAPI.doOauthVerify(this, platform, umLoginAuthListener);
                break;
            case R.id.bt_no_login:
                SystemInfo.getInstance(getApplicationContext()).setMemberId(Constants.TRY_OUT_ACCOUNT);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                break;
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
            ChangeWidgetEnable(false);
            switch (share_media) {
                case QQ:
                    TT.showShort(getApplicationContext(), "QQ授权成功，正在登陆");
                    getUserInfo(share_media);
                    break;
                case WEIXIN:
                    TT.showShort(getApplicationContext(), "微信授权成功，正在登陆");
                    break;
                case SINA:
                    TT.showShort(getApplicationContext(), "新浪微博授权成功，正在登陆");
                    getUserInfo(share_media);
                    break;
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Logger.e("error");
            switch (share_media) {
                case QQ:
                    TT.showShort(getApplicationContext(), "QQ登陆失败");
                    break;
                case WEIXIN:
                    TT.showShort(getApplicationContext(), "微信登陆失败");
                    break;
                case SINA:
                    TT.showShort(getApplicationContext(), "新浪登陆失败");
                    break;
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            switch (share_media) {
                case QQ:
                    TT.showShort(getApplicationContext(), "取消QQ登陆");
                    break;
                case WEIXIN:
                    TT.showShort(getApplicationContext(), "取消微信登陆");
                    break;
                case SINA:
                    TT.showShort(getApplicationContext(), "取消新浪微博登陆");
                    break;
            }
        }
    };

    /*
    让控件不能用
     */
    private void ChangeWidgetEnable(boolean b) {
        btQqLogin.setEnabled(b);
        btSinaLogin.setEnabled(b);
        btWechatLogin.setEnabled(b);
        btNoLogin.setEnabled(b);
    }

    /*
    获取用户信息
     */
    private void getUserInfo(SHARE_MEDIA share_media) {
        mShareAPI.getPlatformInfo(LoginActivity.this, share_media, umGetInfoAuthListener);
    }

    /*
    友盟三方获取用户信息
     */
    private UMAuthListener umGetInfoAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Logger.e(map.toString());
            switch (share_media) {
                case QQ:
                    //友盟统计账号来源
                    MobclickAgent.onProfileSignIn("QQ", map.get("openid"));
                    //这里先不要设置，防止用户没有经过服务器验证直接登录成功
//                    SystemInfo.getInstance(getApplicationContext()).setQQ(map.get("openid"));
//                    SystemInfo.getInstance(getApplicationContext()).setQQName(map.get("screen_name"));
//                    SystemInfo.getInstance(getApplicationContext()).setMemberId(map.get("openid"));
//                    SystemInfo.getInstance(getApplicationContext()).setAccount(map.get("screen_name"));
//                    SystemInfo.getInstance(getApplicationContext()).setPortraitUrl(map.get("profile_image_url"));
                    portraitUrl = map.get("profile_image_url");
                    serverLogin(map.get("openid"), map.get("screen_name"), map.get("openid"),
                            map.get("screen_name"), "", "", "");
                    break;
                case SINA:
                    try {
                        String result = map.get("result");
                        JSONObject sinaUserInfo = null;
                        sinaUserInfo = new JSONObject(result);

                        //友盟统计账号来源
                        MobclickAgent.onProfileSignIn("SinaWeibo", String.valueOf(sinaUserInfo.get("idstr")));
                        //这里先不要设置，防止用户没有经过服务器验证直接登录成功
//                        SystemInfo.getInstance(getApplicationContext()).setSina((String) sinaUserInfo.get("idstr"));
//                        SystemInfo.getInstance(getApplicationContext()).setMemberId((String) sinaUserInfo.get("idstr"));
//                        SystemInfo.getInstance(getApplicationContext()).setSinaName((String) sinaUserInfo.get("screen_name"));
//                        SystemInfo.getInstance(getApplicationContext()).setAccount((String) sinaUserInfo.get("screen_name"));
//                        SystemInfo.getInstance(getApplicationContext()).setPortraitUrl((String) sinaUserInfo.get("profile_image_url"));
//                        SystemInfo.getInstance(getApplicationContext()).setSignature((String) sinaUserInfo.get("description"));
                        portraitUrl = (String) sinaUserInfo.get("profile_image_url");
                        serverLogin((String) sinaUserInfo.get("idstr"), (String) sinaUserInfo.get("screen_name"), "", "",
                                (String) sinaUserInfo.get("idstr"), (String) sinaUserInfo.get("screen_name"),
                                (String) sinaUserInfo.get("description"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    /*
    服务器登录验证
     */
    private void serverLogin(String memberId, String account, String qq, String qqName, String sina, String sinaName, String signature) {

        Logger.e("开始登录");
        showLoadingDialog();
        Map<String, String> params = new HashMap<>();
        params.put("memberid", memberId);
        params.put("account", account);
        params.put("qq", qq);
        params.put("qqname", qqName);
        params.put("sina", sina);
        params.put("sinaname", sinaName);
        params.put("signature", signature);

        String url = InterfaceURL.login;
        Logger.e("url:" + url);
        JsonBeanRequest<LoginReturnBean> loginRequest = new JsonBeanRequest<>(url, params, LoginReturnBean.class, new Response.Listener<LoginReturnBean>() {
            @Override
            public void onResponse(LoginReturnBean response) {
                Message msg = new Message();
                if (response.getResult().equals("1")) {
                    msg.obj = response;
                    msg.what = Constants.Login.loginSuccess;
                } else {
                    msg.obj = response.getMsg();
                    msg.what = Constants.Login.loginFailure;
                }
                mHandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e("Error:" + error);
                Message msg = new Message();
                msg.what = Constants.requestException;
                msg.obj = error.toString();
                mHandler.sendMessage(msg);
            }
        });
        addToRequestQueue(loginRequest);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissLoadingDialog();
            switch (msg.what) {
                case Constants.Login.loginSuccess:
                    LoginReturnBean login = (LoginReturnBean) msg.obj;
                    Logger.e(login.toString());
                    //现在对新旧用户的操作是一样的，后期可能会加东西
                    if (login.isNewAccount()) {
                        SystemInfo.getInstance(getApplicationContext()).setMemberId(login.getUser().getMemberid());
                        SystemInfo.getInstance(getApplicationContext()).setAccount(login.getUser().getAccount());
                        SystemInfo.getInstance(getApplicationContext()).setQQ(login.getUser().getQq());
                        SystemInfo.getInstance(getApplicationContext()).setQQName(login.getUser().getQqname());
                        SystemInfo.getInstance(getApplicationContext()).setSina(login.getUser().getSina());
                        SystemInfo.getInstance(getApplicationContext()).setSinaName(login.getUser().getSinaname());
                        SystemInfo.getInstance(getApplicationContext()).setSignature(login.getUser().getSignature());
                        SystemInfo.getInstance(getApplicationContext()).setPortraitUrl(portraitUrl);
                        SPUtils.put(getApplicationContext(), Constants.SP_TODAY_ONE_WORD, login.getDayEvent().getOneWord());
                    } else {
                        SystemInfo.getInstance(getApplicationContext()).setMemberId(login.getUser().getMemberid());
                        SystemInfo.getInstance(getApplicationContext()).setAccount(login.getUser().getAccount());
                        SystemInfo.getInstance(getApplicationContext()).setQQ(login.getUser().getQq());
                        SystemInfo.getInstance(getApplicationContext()).setQQName(login.getUser().getQqname());
                        SystemInfo.getInstance(getApplicationContext()).setSina(login.getUser().getSina());
                        SystemInfo.getInstance(getApplicationContext()).setSinaName(login.getUser().getSinaname());
                        SystemInfo.getInstance(getApplicationContext()).setSignature(login.getUser().getSignature());
                        SystemInfo.getInstance(getApplicationContext()).setPortraitUrl(portraitUrl);
                    }
                    List<Event> eventList = login.getDayEvent().getEventList();
                    String startTimes = "";
                    String endTimes = "";
                    String eventTypes = "";
                    for (int i = 0; i < eventList.size(); i++) {
                        SPUtils.put(getApplicationContext(), eventList.get(i).getStarttime(), eventList.get(i).getRecord());
                        if (i == 0) {
                            startTimes = eventList.get(i).getStarttime();
                            endTimes = eventList.get(i).getEndtime();
                            eventTypes = "000" + eventList.get(i).getEventtypes();
                        } else {
                            startTimes = startTimes + "," + eventList.get(i).getStarttime();
                            endTimes = endTimes + "," + eventList.get(i).getEndtime();
                            eventTypes = eventTypes + ",000" + eventList.get(i).getEventtypes();
                        }
                    }

                    SPUtils.put(getApplicationContext(), ApplicationGlobal.START_TIMES, startTimes);
                    SPUtils.put(getApplicationContext(), ApplicationGlobal.END_TIMES, endTimes);
                    SPUtils.put(getApplicationContext(), ApplicationGlobal.EVENT_TYPES, eventTypes);


                    TT.showShort(getApplicationContext(), "登陆成功");

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    break;
                case Constants.Login.loginFailure:
                    TT.showShort(getApplicationContext(), "登陆失败");
                    ChangeWidgetEnable(true);
                    break;
                case Constants.requestException:
                    TT.showShort(LoginActivity.this, "服务器开小差啦");
                    ChangeWidgetEnable(true);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计：Activity自己实现的页面需要这样写(不包含Fragment)
        MobclickAgent.onPageStart(getString(R.string.login));//统计页面
        MobclickAgent.onResume(this);//统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.login));//统计页面
        MobclickAgent.onPause(this);//统计时长
    }
}
