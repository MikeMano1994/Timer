package com.tryking.EasyList.fragment.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tryking.EasyList.R;
import com.tryking.EasyList.activity.LoginAndRegisterActivity;
import com.tryking.EasyList.activity.PIMSActivity;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.utils.TT;
import com.tryking.EasyList.widgets.marqueeView.MarqueeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tryking on 2016/5/13.
 */
public class IcFragment extends Fragment {


    private static final int REQUEST_PIM = 0X00;

    @Bind(R.id.head_portrait)
    ImageView headPortrait;
    @Bind(R.id.aboutUs)
    LinearLayout aboutUs;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_ic, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @OnClick({R.id.aboutUs, R.id.ll_PIM, R.id.account_setting, R.id.common_setting, R.id.recommend})
    void click(View v) {
        switch (v.getId()) {
            case R.id.aboutUs:
                TT.showShort(getActivity(), "关于我们，正在开发...");
                break;
            case R.id.ll_PIM:
                if (SystemInfo.getInstance(getActivity()).isLogin()) {
                    startActivity(new Intent(getActivity(), PIMSActivity.class));
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginAndRegisterActivity.class), REQUEST_PIM);
                    getActivity().finish();
                }
                break;
            case R.id.account_setting:
                TT.showShort(getActivity(), "账号设置，正在开发...");
                break;
            case R.id.common_setting:
                TT.showShort(getActivity(), "通用设置，正在开发...");
                break;
            case R.id.recommend:
                TT.showShort(getActivity(), "推荐给好友，正在开发...");
                break;
            default:
                break;
        }
    }

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
        if (SystemInfo.getInstance(getActivity()).isLogin()) {
            icAccount.setText(SystemInfo.getInstance(getActivity()).getAccount());
            icSignature.setVisibility(View.VISIBLE);
            icSignature.setText(SystemInfo.getInstance(getActivity()).getSignature());
        } else {
            icAccount.setText("试用账号");
            icSignature.setVisibility(View.GONE);
        }

        List<String> info = new ArrayList<>();
        info.add("和时间做朋友_1");
        info.add("和时间做朋友_2");
        info.add("和时间做朋友_3");
        info.add("和时间做朋友_4");
        info.add("和时间做朋友_5");
        info.add("和时间做朋友_6");
        mvNotice.startWithList(info);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}

