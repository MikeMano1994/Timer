package com.tryking.timer.fragment.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tryking.timer.R;
import com.tryking.timer.test.TestActivity;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_ic, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @OnClick(R.id.tv_aboutUs)
    void click(View v) {
        switch (v.getId()) {
            case R.id.tv_aboutUs:
                startActivity(new Intent(getActivity(), TestActivity.class));
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
        Uri uri = Uri.parse("http://www.tryking.top/images/2.jpg");
        headPortrait.setImageURI(uri);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

