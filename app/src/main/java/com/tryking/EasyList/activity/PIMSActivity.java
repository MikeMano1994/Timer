package com.tryking.EasyList.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.orhanobut.logger.Logger;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.BaseBean;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.base.String4Broad;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.global.InterfaceURL;
import com.tryking.EasyList.network.JsonBeanRequest;
import com.tryking.EasyList.utils.CommonUtils;
import com.tryking.EasyList.utils.TT;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PIMSActivity extends BaseActivity {

    @Bind(R.id.head_portrait)
    ImageView headPortrait;
    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.tv_nickName)
    TextView tvNickName;
    @Bind(R.id.rl_nickName)
    RelativeLayout rlNickName;
    @Bind(R.id.tv_QQ)
    TextView tvQQ;
    @Bind(R.id.rl_boundQQ)
    RelativeLayout rlBoundQQ;
    @Bind(R.id.tv_Sina)
    TextView tvSina;
    @Bind(R.id.rl_boundSina)
    RelativeLayout rlBoundSina;
    @Bind(R.id.tv_gender)
    TextView tvGender;
    @Bind(R.id.rl_gender)
    RelativeLayout rlGender;
    @Bind(R.id.tv_signature)
    TextView tvSignature;
    @Bind(R.id.rl_signature)
    RelativeLayout rlSignature;
    @Bind(R.id.bt_logout)
    Button btLogout;
    @Bind(R.id.toolBarLayout)
    CollapsingToolbarLayout toolBarLayout;

    TextInputEditText etSignature;
    TextInputEditText etNickname;
    /********************/
    /*******NickName*****/
    /********************/
    //输入表情前的光标位置
    private int nickNameCursorPos;
    //输入表情前EditText中的文本
    private String nickNameInputAfterText;
    //是否重置了EditText的内容
    private boolean nickNameResetText;
    /********************/
    /*******Signature*****/
    /********************/
    //输入表情前的光标位置
    private int SignatureCursorPos;
    //输入表情前EditText中的文本
    private String SignatureInputAfterText;
    //是否重置了EditText的内容
    private boolean SignatureResetText;


    @OnClick({R.id.bt_logout, R.id.rl_gender, R.id.rl_signature, R.id.rl_nickName})
    void click(View v) {
        switch (v.getId()) {
            case R.id.bt_logout:
                new AlertDialog.Builder(this)
                        .setTitle("退出登陆")
                        .setMessage("是否确认退出当前账号？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //如果登陆了就让用户登出，把本地的数据清理掉。如果没有登录就不登出，防止用户再次无账号登录时没数据
                                if (SystemInfo.getInstance(getApplicationContext()).isLogin()) {
                                    SystemInfo.getInstance(getApplicationContext()).logout();
                                }
                                startActivity(new Intent(PIMSActivity.this, LoginActivity.class));
                                dialog.dismiss();
                                //给MainActivity发送广播使其finish
                                Intent intent_exitMain = new Intent(String4Broad.ExitMainActivity);
                                sendBroadcast(intent_exitMain);
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.rl_gender:
                final CharSequence[] items = new CharSequence[]{"女", "男"};
                boolean isMan = tvGender.getText().toString().equals("男");
                new AlertDialog.Builder(this)
                        .setTitle("性别")
                        .setSingleChoiceItems(items, isMan ? 1 : 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        tvGender.setText(items[0]);
                                        break;
                                    case 1:
                                        tvGender.setText(items[1]);
                                        break;
                                }
                                changeGenderToServer(which);
                                SystemInfo.getInstance(getApplicationContext()).setGender(String.valueOf(which));
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.rl_signature:
                View signature = LayoutInflater.from(PIMSActivity.this).inflate(R.layout.textview_change_signature, null);
                etSignature = (TextInputEditText) signature.findViewById(R.id.et_signature);
                AlertDialog.Builder builder = new AlertDialog.Builder(PIMSActivity.this)
                        .setTitle("个性签名")
                        .setView(signature);
                setSignaturePosAndNegButton(builder);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                etSignature.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                });
                SignatureCursorPos = etSignature.getSelectionEnd();
                etSignature.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        if (!SignatureResetText) {
                            SignatureCursorPos = etSignature.getSelectionEnd();
                            //这里用s.toString()而不直接用s是因为如果用s，
                            // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                            // inputAfterText也就改变了，那么表情过滤就失败了
                            SignatureInputAfterText = s.toString();

                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!SignatureResetText) {
                            if (before != 0) {
                                return;
                            }
                            if (count >= 2) {//表情符号的字符长度最小为2
                                CharSequence input = s.subSequence(SignatureCursorPos, SignatureCursorPos + count);
                                if (CommonUtils.containsEmoji(input.toString())) {
                                    SignatureResetText = true;
                                    TT.showShort(PIMSActivity.this, "不支持输入Emoji表情符号哦~");
                                    //是表情符号就将文本还原为输入表情符号之前的内容
                                    etSignature.setText(SignatureInputAfterText);
                                    CharSequence text = etSignature.getText();
                                    if (text instanceof Spannable) {
                                        Spannable spanText = (Spannable) text;
                                        Selection.setSelection(spanText, text.length());
                                    }
                                }
                            }
                        } else {
                            SignatureResetText = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                break;
            case R.id.rl_nickName:
                View nickName = LayoutInflater.from(PIMSActivity.this).inflate(R.layout.textview_change_nickname, null);
                etNickname = (TextInputEditText) nickName.findViewById(R.id.et_nickname);
                AlertDialog.Builder b = new AlertDialog.Builder(PIMSActivity.this)
                        .setTitle("昵称")
                        .setView(nickName);
                setNickNamePosAndNegButton(b);
                final AlertDialog nameDialog = b.create();
                nameDialog.show();
                etNickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        nameDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                });

                nickNameCursorPos = etNickname.getSelectionEnd();
                etNickname.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        if (!nickNameResetText) {
                            nickNameCursorPos = etNickname.getSelectionEnd();
                            //这里用s.toString()而不直接用s是因为如果用s，
                            // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                            // inputAfterText也就改变了，那么表情过滤就失败了
                            nickNameInputAfterText = s.toString();

                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!nickNameResetText) {

                            if (before != 0) {

                                return;

                            }
                            if (count >= 2) {//表情符号的字符长度最小为2
                                CharSequence input = s.subSequence(nickNameCursorPos, nickNameCursorPos + count);
                                if (CommonUtils.containsEmoji(input.toString())) {
                                    nickNameResetText = true;
                                    TT.showShort(PIMSActivity.this, "不支持输入Emoji表情符号哦~");
                                    //是表情符号就将文本还原为输入表情符号之前的内容
                                    etNickname.setText(nickNameInputAfterText);
                                    CharSequence text = etNickname.getText();
                                    if (text instanceof Spannable) {
                                        Spannable spanText = (Spannable) text;
                                        Selection.setSelection(spanText, text.length());
                                    }
                                }
                            }
                        } else {
                            nickNameResetText = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                break;
            default:
                break;
        }
    }

    private void setNickNamePosAndNegButton(AlertDialog.Builder builder) {
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!etNickname.getText().toString().trim().equals("")) {
                    tvNickName.setText(etNickname.getText().toString().trim());
                    changeNicknameToServer(etNickname.getText().toString().trim());
                    SystemInfo.getInstance(getApplicationContext()).setAccount(etNickname.getText().toString().trim());
                }
            }
        })
                .setNegativeButton("取消", null);
    }

    private void changeNicknameToServer(String nickName) {
        Logger.e("开始改昵称");
        Map<String, String> params = new HashMap<>();
        params.put("memberId", SystemInfo.getInstance(getApplicationContext()).getMemberId());
        params.put("type", Constants.ChangeInformation.TYPE_ACCOUNT);
        params.put("param", nickName);
        String url = InterfaceURL.updateParam;
        JsonBeanRequest<BaseBean> signatureRequest = new JsonBeanRequest<>(url, params, BaseBean.class, new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (response.getResult().equals("1")) {
                    //这里没必要做后续操作了
                } else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e(error.toString());
            }
        });
        addToRequestQueue(signatureRequest);
    }

    /*
   给alert设置按钮
    */
    private void setSignaturePosAndNegButton(final AlertDialog.Builder builder) {
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if (!etSignature.getText().toString().trim().equals("")) {//个性签名可以设为空
                    tvSignature.setText(etSignature.getText().toString().trim());
                    changeSignatureToServer(etSignature.getText().toString().trim());
                    SystemInfo.getInstance(getApplicationContext()).setSignature(etSignature.getText().toString().trim());
//                }
            }
        })
                .setNegativeButton("取消", null);
    }

    private void changeSignatureToServer(String signature) {
        Logger.e("开始改签名");
        Map<String, String> params = new HashMap<>();
        params.put("memberId", SystemInfo.getInstance(getApplicationContext()).getMemberId());
        params.put("type", Constants.ChangeInformation.TYPE_SIGNATURE);
        params.put("param", signature);
        String url = InterfaceURL.updateParam;
        JsonBeanRequest<BaseBean> signatureRequest = new JsonBeanRequest<>(url, params, BaseBean.class, new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (response.getResult().equals("1")) {
                    //这里没必要做后续操作了
                } else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e(error.toString());
            }
        });
        addToRequestQueue(signatureRequest);
    }


    /*
     *改变性别：
     */
    private void changeGenderToServer(int gender) {
        Logger.e("开始改性别");
        Map<String, String> params = new HashMap<>();
        params.put("memberId", SystemInfo.getInstance(getApplicationContext()).getMemberId());
        params.put("type", Constants.ChangeInformation.TYPE_GENDER);
        params.put("param", String.valueOf(gender));
//        Logger.e(params.toString());
        String url = InterfaceURL.updateParam;
        JsonBeanRequest<BaseBean> genderRequest = new JsonBeanRequest<>(url, params, BaseBean.class, new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (response.getResult().equals("1")) {
                    //这里没必要做后续操作了
                } else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e(error.toString());
            }
        });
        addToRequestQueue(genderRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pims);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        initToolBar();
        if (SystemInfo.getInstance(getApplicationContext()).getPortraitUrl() == "") {
            headPortrait.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_circle_black));
        } else {
            Uri uri = Uri.parse(SystemInfo.getInstance(getApplicationContext()).getPortraitUrl());
            Glide.with(this).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(headPortrait) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                    roundedBitmapDrawable.setCircular(true);
                    headPortrait.setImageDrawable(roundedBitmapDrawable);
                }
            });
        }
        tvNickName.setText(SystemInfo.getInstance(getApplicationContext()).getAccount().equals("") ? "试用账号" : SystemInfo.getInstance(getApplicationContext()).getAccount());
        tvQQ.setText(SystemInfo.getInstance(getApplicationContext()).getQQName().equals("") ? "未绑定" : SystemInfo.getInstance(getApplicationContext()).getQQName());
        tvSina.setText(SystemInfo.getInstance(getApplicationContext()).getSinaName().equals("") ? "未绑定" : SystemInfo.getInstance(getApplicationContext()).getSinaName());
        tvSignature.setText(SystemInfo.getInstance(getApplicationContext()).getSignature().equals("") ? "未设置个性签名" : SystemInfo.getInstance(getApplicationContext()).getSignature());
        tvGender.setText(SystemInfo.getInstance(getApplicationContext()).getGender().equals("0") ? "女" : "男");
    }

    /*
    初始化ToolBar
     */
    private void initToolBar() {
        toolBarLayout.setTitle(getResources().getString(R.string.individual_center));
        toolBarLayout.setExpandedTitleColor(getResources().getColor(R.color.float_transparent));
        toolBarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        toolBar.setNavigationIcon(R.drawable.ic_action_arrow_left_white_24dp);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PIMSActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计：Activity自己实现的页面需要这样写(不包含Fragment)
        MobclickAgent.onPageStart(getString(R.string.individual_center));//统计页面
        MobclickAgent.onResume(this);//统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.individual_center));//统计页面
        MobclickAgent.onPause(this);//统计时长
    }
}
