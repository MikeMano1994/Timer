package com.tryking.EasyList.fragment.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tryking.EasyList.R;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.test.TestActivity;
import com.tryking.EasyList.activity.LoginAndRegisterActivity;
import com.tryking.EasyList.activity.PIMSActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tryking on 2016/5/13.
 */
public class IcFragment extends Fragment {


    @Bind(R.id.head_portrait)
    SimpleDraweeView headPortrait;
    @Bind(R.id.tv_aboutUs)
    TextView tvAboutUs;
    @Bind(R.id.ll_PIM)
    LinearLayout llPIM;
    @Bind(R.id.ic_account)
    TextView icAccount;
    @Bind(R.id.ic_signature)
    TextView icSignature;
//    @Bind(R.id.bt_logout)
//    Button btLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_ic, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @OnClick({R.id.tv_aboutUs, R.id.ll_PIM})
    void click(View v) {
        switch (v.getId()) {
            case R.id.tv_aboutUs:
                startActivity(new Intent(getActivity(), TestActivity.class));
                break;
            case R.id.ll_PIM:
                if (SystemInfo.getInstance(getActivity()).isLogin()) {
                    startActivity(new Intent(getActivity(), PIMSActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginAndRegisterActivity.class));
                    getActivity().finish();
                }
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
        Uri uri = Uri.parse(SystemInfo.getInstance(getActivity()).getPortraitUrl());
        headPortrait.setImageURI(uri);
        if (SystemInfo.getInstance(getActivity()).isLogin()) {
            icAccount.setText(SystemInfo.getInstance(getActivity()).getAccount());
            icSignature.setVisibility(View.VISIBLE);
            icSignature.setText(SystemInfo.getInstance(getActivity()).getSignature());
//            btLogout.setVisibility(View.VISIBLE);
        } else {
            icAccount.setText("未登陆");
            icSignature.setVisibility(View.GONE);
//            btLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

