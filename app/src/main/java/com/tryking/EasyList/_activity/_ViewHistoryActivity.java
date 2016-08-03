package com.tryking.EasyList._activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tryking.EasyList.R;
import com.tryking.EasyList._fragment.viewhistory._ViewHistoryFragment;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.utils.TT;
import com.tryking.EasyList.widgets.DateChooseDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class _ViewHistoryActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.chose_month)
    LinearLayout choseMonth;
    @Bind(R.id.arrow)
    ImageView arrow;
    @Bind(R.id.main_content)
    FrameLayout mainContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history_);
        ButterKnife.bind(this);

        init();
    }

    @OnClick({R.id.chose_month})
    void click(View v) {
        switch (v.getId()) {
            case R.id.chose_month:
                arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_up_white_18dp));
                DateChooseDialog dateChooseDialog = new DateChooseDialog(this, mHandler, "2015", "2016", "1", "8");
                dateChooseDialog.show();
                dateChooseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_down_white_18dp));
                    }
                });
                break;
            default:
                break;
        }
    }

    private void init() {
        initToolBar();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, new _ViewHistoryFragment());
        transaction.commit();
    }

    /*
       初始化ToolBar
        */
    private void initToolBar() {
        toolBar.setNavigationIcon(R.drawable.ic_action_arrow_left_white_24dp);

        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _ViewHistoryActivity.this.finish();
            }
        });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissLoadingDialog();
            switch (msg.what) {
                case Constants.ViewHistory.DAY_CHOSE_DATE:
                    Bundle data = msg.getData();
                    String choseMonth = data.getString(Constants.HANDLER_CHOSE_MONTH);
                    String choseYear = data.getString(Constants.HANDLER_CHOSE_YEAR);
                    TT.showShort(getApplicationContext(), "Y:" + choseYear + "|||M:" + choseMonth);
                    break;
                default:
                    break;
            }
        }
    };
}
