package com.tryking.EasyList.activity.about_easylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.tryking.EasyList.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.fragment.FeedbackFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedbackActivity extends AppCompatActivity {

//    @Bind(R.id.toolBar)
//    Toolbar toolBar;
    @Bind(R.id.main_content)
    FrameLayout mainContent;
    private FeedbackFragment mFeedbackFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        initToolBar();

//        if (savedInstanceState == null) {
        // Create the detail fragment and add it to the activity
        // using a fragment transaction.
        String conversation_id = getIntent().getStringExtra(FeedbackFragment.BUNDLE_KEY_CONVERSATION_ID);
        mFeedbackFragment = FeedbackFragment.newInstance(conversation_id);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, mFeedbackFragment)
                .commit();
//        }
    }

    private void initToolBar() {
//        toolBar.setNavigationIcon(R.drawable.ic_action_arrow_left_white_24dp);
//        toolBar.setTitleTextColor(getResources().getColor(R.color.white));
//        toolBar.setTitle(R.string.feedback);
//        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FeedbackActivity.this.finish();
//            }
//        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
        //复写
        mFeedbackFragment.refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计：Activity自己实现的页面需要这样写(不包含Fragment)
        MobclickAgent.onPageStart(getString(R.string.feedback));//统计页面
        MobclickAgent.onResume(this);//统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.feedback));//统计页面
        MobclickAgent.onPause(this);//统计时长
    }
}
