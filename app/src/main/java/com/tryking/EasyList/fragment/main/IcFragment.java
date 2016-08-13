package com.tryking.EasyList.fragment.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.orhanobut.logger.Logger;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.AppVersionReturnBean;
import com.tryking.EasyList._bean.ShareUrlRetrunBean;
import com.tryking.EasyList.activity.CommonSettingActivity;
import com.tryking.EasyList.activity.LoginActivity;
import com.tryking.EasyList.activity.PIMSActivity;
import com.tryking.EasyList.activity.about_easylist.AboutEasyListActivity;
import com.tryking.EasyList.base.BaseFragment;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.global.InterfaceURL;
import com.tryking.EasyList.network.JsonBeanRequest;
import com.tryking.EasyList.utils.AppUtils;
import com.tryking.EasyList.utils.SPUtils;
import com.tryking.EasyList.utils.TT;
import com.tryking.EasyList.widgets.UpdateDialog;
import com.tryking.EasyList.widgets.marqueeView.MarqueeView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.social.UMPlatformData;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tryking on 2016/5/13.
 */
public class IcFragment extends BaseFragment {

    @Bind(R.id.head_portrait)
    ImageView headPortrait;
    @Bind(R.id.aboutEasyList)
    LinearLayout aboutEasyList;
    @Bind(R.id.ll_PIM)
    LinearLayout llPIM;
    @Bind(R.id.ic_account)
    TextView icAccount;
    @Bind(R.id.ic_signature)
    TextView icSignature;
    @Bind(R.id.mv_notice)
    MarqueeView mvNotice;
    @Bind(R.id.account_setting)
    LinearLayout accountSetting;
    @Bind(R.id.common_setting)
    LinearLayout commonSetting;
    @Bind(R.id.recommend)
    LinearLayout recommend;
    @Bind(R.id.update_new)
    TextView updateNew;


    private String shareUrl;

    final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_ic, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @OnClick({R.id.aboutEasyList, R.id.ll_PIM, R.id.common_setting, R.id.recommend})
    void click(View v) {
        switch (v.getId()) {
            case R.id.aboutEasyList:
                startActivity(new Intent(getActivity(), AboutEasyListActivity.class));
                break;
            case R.id.ll_PIM:
                if (SystemInfo.getInstance(getContext()).isLogin()) {
                    startActivity(new Intent(getActivity(), PIMSActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
                break;
            case R.id.common_setting:
                startActivity(new Intent(getActivity(), CommonSettingActivity.class));
                break;
            case R.id.recommend:
                shareUrl = (String) SPUtils.get(getActivity(), Constants.SP_SHARE_URL, "");
                if (shareUrl == null || shareUrl.equals("")) {
                    shareUrl = Constants.default_share_url;
                }
                new ShareAction(getActivity()).setDisplayList(displaylist)
                        .setListenerList(listener)//这个貌似和callback一样
                        .withTitle(getString(R.string.share_title))
                        .withText(getString(R.string.share_text))
                        .withMedia(new UMImage(getActivity(), R.mipmap.ic_launcher))
                        .withTargetUrl(shareUrl)
                        .open();
                break;
            default:
                break;
        }
    }

    private UMShareListener listener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            //QQ的就是QQ
            String sharePlatform = platform.name();
            //社交统计
            UMPlatformData pform = null;
            String userId = "";
            if (SystemInfo.getInstance(getContext()).getAccount() == null || SystemInfo.getInstance(getContext()).getAccount().equals("")) {
                userId = Constants.TRY_OUT_ACCOUNT;
            } else {
                userId = SystemInfo.getInstance(getContext()).getAccount();
            }
            if (platform == SHARE_MEDIA.WEIXIN) {//enum类型用==可以判断
                sharePlatform = "微信";
                pform = new UMPlatformData(UMPlatformData.UMedia.WEIXIN_FRIENDS, userId);
            } else if (platform.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {//.equals也能判断
                sharePlatform = "朋友圈";
                pform = new UMPlatformData(UMPlatformData.UMedia.WEIXIN_CIRCLE, userId);
            } else if (platform.equals(SHARE_MEDIA.QZONE)) {
                sharePlatform = "QQ空间";
                pform = new UMPlatformData(UMPlatformData.UMedia.TENCENT_QZONE, userId);
            } else if (platform.equals(SHARE_MEDIA.SINA)) {
                sharePlatform = "新浪微博";
                pform = new UMPlatformData(UMPlatformData.UMedia.SINA_WEIBO, userId);
            } else if (platform.equals(SHARE_MEDIA.QQ)) {
                sharePlatform = "QQ";
                pform = new UMPlatformData(UMPlatformData.UMedia.TENCENT_QQ, userId);
            }
            TT.showShort(getActivity(), sharePlatform + "分享成功啦");

            if (SystemInfo.getInstance(getContext()).getGender() != null) {
                if (SystemInfo.getInstance(getContext()).getGender().equals("0")) {
                    pform.setGender(UMPlatformData.GENDER.FEMALE); //optional
                } else {
                    pform.setGender(UMPlatformData.GENDER.MALE);
                }
            }
            //社交统计
            MobclickAgent.onSocialEvent(getActivity(), pform);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable throwable) {
            String sharePlatform = platform.name();

            if (platform == SHARE_MEDIA.WEIXIN) {//enum类型用==可以判断
                sharePlatform = "微信";
            } else if (platform.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {//.equals也能判断
                sharePlatform = "朋友圈";
            } else if (platform.equals(SHARE_MEDIA.QZONE)) {
                sharePlatform = "QQ空间";
            } else if (platform.equals(SHARE_MEDIA.SINA)) {
                sharePlatform = "新浪微博";
            }
            TT.showShort(getActivity(), sharePlatform + " 分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            String sharePlatform = platform.name();

            if (platform == SHARE_MEDIA.WEIXIN) {//enum类型用==可以判断
                sharePlatform = "微信";
            } else if (platform.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {//.equals也能判断
                sharePlatform = "朋友圈";
            } else if (platform.equals(SHARE_MEDIA.QZONE)) {
                sharePlatform = "QQ空间";
            } else if (platform.equals(SHARE_MEDIA.SINA)) {
                sharePlatform = "新浪微博";
            }
            TT.showShort(getActivity(), sharePlatform + " 分享取消啦");
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        if (SystemInfo.getInstance(getActivity()).getPortraitUrl() == "") {
            headPortrait.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_circle_black));
        } else {
            Uri uri = Uri.parse(SystemInfo.getInstance(getActivity()).getPortraitUrl());
            Glide.with(this).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(headPortrait) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                    roundedBitmapDrawable.setCircular(true);
                    headPortrait.setImageDrawable(roundedBitmapDrawable);
                }
            });
        }

        List<String> info = new ArrayList<>();
        info.add("重复言说多半是一种时间上的损失。 \n—— 培根");
        info.add("最聪明的人是最不愿浪费时间的人。 \n—— 但丁");
        info.add("对时间的慷慨，就等于慢性自杀。 \n—— 奥斯特洛夫斯基");
        info.add("在无限的时间的河流里，人生仅仅是微小又微小的波浪。 \n—— 郭小川");
        info.add("节省时间；也就是使一个人的有限生命，更加有效而也即等于延长了人的生命。 \n—— 鲁迅");
        info.add("忍耐和时间，往往比力量和愤怒更有效。 \n—— 拉封丹");
        mvNotice.startWithList(info);
        if (SystemInfo.getInstance(getActivity()).isLogin()) {
            getShareUrlFromServer();
        }

        checkNewVersion();
    }

    private void checkNewVersion() {
        Logger.e("开始获取版本信息");
        Map<String, String> params = new HashMap<>();
        params.put("deviceType", "android");
        params.put("appName", getString(R.string.app_name_english));
        String url = InterfaceURL.appVersion;
        JsonBeanRequest<AppVersionReturnBean> appVersionRequest = new JsonBeanRequest<>(url, params, AppVersionReturnBean.class,
                new Response.Listener<AppVersionReturnBean>() {
                    @Override
                    public void onResponse(AppVersionReturnBean response) {
                        Message msg = new Message();
                        if (response.getResult().equals("1")) {
                            msg.what = Constants.AboutEasyList.GET_APP_VERSION_SUCCESS;
                            msg.obj = response;
                        }
                        mHandler.sendMessage(msg);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e(error.getMessage());
            }
        });
        addToRequestQueue(appVersionRequest);
    }

    private void getShareUrlFromServer() {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", SystemInfo.getInstance(getActivity()).getMemberId());
        String url = InterfaceURL.getShareUrl;
        JsonBeanRequest<ShareUrlRetrunBean> shareRequest = new JsonBeanRequest<>(url, params, ShareUrlRetrunBean.class, new Response.Listener<ShareUrlRetrunBean>() {
            @Override
            public void onResponse(ShareUrlRetrunBean response) {
                if (response.getResult().equals("1")) {
                    Message msg = new Message();
                    msg.what = Constants.Share.GET_SHARE_URL_SUCCESS;
                    msg.obj = response;
                    mHandler.sendMessage(msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        addToRequestQueue(shareRequest);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.Share.GET_SHARE_URL_SUCCESS:
                    ShareUrlRetrunBean shareBean = (ShareUrlRetrunBean) msg.obj;
                    if (shareBean.getIsChange().equals("1")) {
                        SPUtils.put(getActivity(), Constants.SP_SHARE_URL, shareBean.getShareUrl());
                    }
                    break;
                case Constants.AboutEasyList.GET_APP_VERSION_SUCCESS:
                    AppVersionReturnBean appVersionReturnBean = (AppVersionReturnBean) msg.obj;
                    int appVersionNum = Integer.parseInt(appVersionReturnBean.getAppVersionNum());
                    if (appVersionNum > AppUtils.getVersionCode(getActivity())) {
                        //更新
                        updateNew.setVisibility(View.VISIBLE);
                        SPUtils.put(getActivity(), Constants.SP_HAS_NEW_VERSION, true);
                    } else {
                        updateNew.setVisibility(View.GONE);
                        SPUtils.put(getActivity(), Constants.SP_HAS_NEW_VERSION, false);
                    }
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //友盟统计：由Activity和Fragment构成的页面需要这样写
    public void onResume() {
        super.onResume();
        //这个放到这里是因为如果用户在PIMSActivity中修改了的话这里要变
        if (SystemInfo.getInstance(getActivity()).isLogin()) {
            icAccount.setText(SystemInfo.getInstance(getActivity()).getAccount());
            icSignature.setText(SystemInfo.getInstance(getActivity()).getSignature());
        } else {
            icAccount.setText("未登陆");
            icSignature.setText("点击登录");
        }

        MobclickAgent.onPageStart(getString(R.string.main_ic));
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.main_ic));
    }

}

