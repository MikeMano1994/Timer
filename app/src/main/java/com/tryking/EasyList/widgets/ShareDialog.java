package com.tryking.EasyList.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.orhanobut.logger.Logger;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.ViewHistoryHandlerData;
import com.tryking.EasyList._bean.viewHistoryBean.ViewHistoryChildData;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.global.ApplicationGlobal;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.utils.CommonUtils;
import com.tryking.EasyList.utils.SPUtils;
import com.tryking.EasyList.utils.TT;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 26011 on 2016/8/9.
 */
public class ShareDialog extends Dialog {
    @Bind(R.id.showPieChart)
    PieChart showPieChart;
    @Bind(R.id.account)
    TextView account;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.qq_share)
    ImageView qqShare;
    @Bind(R.id.sina_share)
    ImageView sinaShare;
    @Bind(R.id.wechat_share)
    ImageView wechatShare;
    @Bind(R.id.share_main)
    LinearLayout shareMain;
    @Bind(R.id.weCircle_share)
    ImageView weCircleShare;
    @Bind(R.id.qZone_share)
    ImageView qZoneShare;

    private Context mContext;
    private View mShareDialog;
    private ViewHistoryHandlerData mData;
    private String dir;

    private int workTime;
    private int amuseTime;
    private int lifeTime;
    private int studyTime;
    private PieData chartData;

    private final AnimationSet appearAnim;
    private final AnimationSet disAppearAnim;
    String[] mParties = new String[]{
            "工作", "娱乐", "生活", "学习"
    };
    private String shareUrl;

    public ShareDialog(Context context, ViewHistoryHandlerData data) {
        super(context, R.style.share_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        mContext = context;
        mShareDialog = LayoutInflater.from(context).inflate(R.layout.dialog_share, null);
        ButterKnife.bind(this, mShareDialog);

        mData = data;

        init();

        appearAnim = (AnimationSet) AnimationUtils.loadAnimation(mContext, R.anim.anim_loading_dialog_appear);
        disAppearAnim = (AnimationSet) AnimationUtils.loadAnimation(mContext, R.anim.anim_loading_dialog_disappear);
    }

    @OnClick({R.id.qq_share, R.id.wechat_share, R.id.sina_share, R.id.weCircle_share, R.id.qZone_share})
    void click(View v) {
        //点击了分享的时候再生成图片，不然获取不到view的宽高
        generatePicFromView();
        UMImage umImage = new UMImage(mContext, BitmapFactory.decodeFile(dir + "/" + Constants.share_pic_name));
        //只有朋友圈，qq空间，微博，别的分享没有意义，因为图片看不见
        switch (v.getId()) {
            case R.id.qq_share:
                new ShareAction((Activity) mContext)
                        //qq也是只分享图片
                        .setPlatform(SHARE_MEDIA.QQ)
                        .setCallback(listener)
//                        .withText(mContext.getString(R.string.share_text))
//                        .withTitle(mContext.getString(R.string.share_title))
                        .withMedia(umImage)
//                        .withMedia(new UMImage(mContext, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher)))
//                        .withTargetUrl(shareUrl)
                        .share();
                break;
            case R.id.sina_share:
                //新浪可以分享一堆
                new ShareAction((Activity) mContext)
                        .setPlatform(SHARE_MEDIA.SINA)
                        .setCallback(listener)
                        .withText(mContext.getString(R.string.share_text))
                        .withTitle(mContext.getString(R.string.share_title))
                        .withMedia(umImage)
                        .withTargetUrl(shareUrl)
                        .share();
                break;
            case R.id.wechat_share:
                new ShareAction((Activity) mContext)
                        //微信也是只分享图片
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .setCallback(listener)
//                        .withText(mContext.getString(R.string.share_text))
//                        .withTitle(mContext.getString(R.string.share_title))
                        .withMedia(umImage)
//                        .withTargetUrl(shareUrl)
                        .share();
                break;
            case R.id.weCircle_share:
                //朋友圈只分享图片，这样才有意义（如果带字的话是小图）
                new ShareAction((Activity) mContext)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(listener)
//                        .withText(mContext.getString(R.string.share_text))
//                        .withTitle(mContext.getString(R.string.share_title))
                        .withMedia(umImage)
//                        .withTargetUrl(shareUrl)
                        .share();
                break;
            case R.id.qZone_share:
                new ShareAction((Activity) mContext)
                        .setPlatform(SHARE_MEDIA.QZONE)
                        .setCallback(listener)
                        .withText(mContext.getString(R.string.share_text))
                        .withTitle(mContext.getString(R.string.share_title))
                        .withTargetUrl(shareUrl)
                        .withTitle(mContext.getString(R.string.app_name))
                        .share();
                break;
            default:
                break;
        }
    }

    UMShareListener listener = new UMShareListener() {
        //现在只有新浪的不能正常回调（不知道原因）
        @Override
        public void onResult(SHARE_MEDIA platform) {
            //QQ的就是QQ
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
            TT.showShort(mContext, sharePlatform + "分享成功啦");
            dismiss();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
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
            TT.showShort(mContext, sharePlatform + "分享失败啦");
            dismiss();
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
            TT.showShort(mContext, sharePlatform + "分享取消了");
            dismiss();
        }
    };


    private void init() {

        initChart();

        account.setText(SystemInfo.getInstance(mContext).getAccount() + "\t\t的日常");
        date.setText(mData.getmGroupData().getDate());

        // 这里没有判断储存卡是否存在，有空要判断
        dir = ApplicationGlobal.CACHE_DIR;
        File mFile = new File(dir);
        if (!mFile.exists()) {
            mFile.mkdirs();
        }

        shareUrl = (String) SPUtils.get(mContext, Constants.SP_SHARE_URL, "");
        if (shareUrl == null || shareUrl.equals("")) {
            shareUrl = Constants.default_share_url;
        }
    }

    private void initChart() {
        List<ViewHistoryChildData> viewHistoryChildDatas = mData.getmChildData();
        workTime = 0;
        amuseTime = 0;
        lifeTime = 0;
        studyTime = 0;
        for (int i = 0; i < viewHistoryChildDatas.size(); i++) {
            switch (viewHistoryChildDatas.get(i).getDataType()) {
                case ViewHistoryChildData.TYPE_WORK:
                    workTime += CommonUtils.durationMinutes(viewHistoryChildDatas.get(i).getStartTime(), viewHistoryChildDatas.get(i).getEndTime());
                    break;
                case ViewHistoryChildData.TYPE_AMUSE:
                    amuseTime += CommonUtils.durationMinutes(viewHistoryChildDatas.get(i).getStartTime(), viewHistoryChildDatas.get(i).getEndTime());
                    break;
                case ViewHistoryChildData.TYPE_LIFE:
                    lifeTime += CommonUtils.durationMinutes(viewHistoryChildDatas.get(i).getStartTime(), viewHistoryChildDatas.get(i).getEndTime());
                    break;
                case ViewHistoryChildData.TYPE_STUDY:
                    studyTime += CommonUtils.durationMinutes(viewHistoryChildDatas.get(i).getStartTime(), viewHistoryChildDatas.get(i).getEndTime());
                    break;
                default:
                    break;
            }
        }

        float[] eventData = new float[]{workTime, amuseTime, lifeTime, studyTime};
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < 4; i++) {
            entries.add(new PieEntry(eventData[i], mParties[i % mParties.length]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "颜色标识");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(mContext.getResources().getColor(R.color.work));
        colors.add(mContext.getResources().getColor(R.color.amuse));
        colors.add(mContext.getResources().getColor(R.color.life));
        colors.add(mContext.getResources().getColor(R.color.study));

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        chartData = new PieData(dataSet);
        chartData.setValueFormatter(new PercentFormatter());
        chartData.setValueTextSize(11f);
        chartData.setValueTextColor(Color.WHITE);

        showPieChart.setUsePercentValues(true);
        showPieChart.setDescription("");
        showPieChart.setExtraOffsets(5, 10, 5, 5);
        showPieChart.setDragDecelerationFrictionCoef(0.95f);

        showPieChart.setDrawHoleEnabled(true);
        showPieChart.setHoleColor(Color.WHITE);

        showPieChart.setTransparentCircleColor(Color.WHITE);
        showPieChart.setTransparentCircleAlpha(110);

        showPieChart.setHoleRadius(58f);
        showPieChart.setTransparentCircleRadius(61f);

//        showPieChart.setDrawCenterText(true);
//        showPieChart.setCenterText("昨日");

        showPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        showPieChart.setRotationEnabled(true);
        showPieChart.setHighlightPerTapEnabled(true);

        // ((HeaderViewHolder)holder).headerPieChart.setUnit(" €");
        // ((HeaderViewHolder)holder).headerPieChart.setDrawUnitsInChart(true);

        // add a selection listener
//            showPieChart.setOnChartValueSelectedListener(this);

        showPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = showPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        showPieChart.setEntryLabelColor(Color.WHITE);
        showPieChart.setEntryLabelTextSize(12f);

        //这个应当是和字体有关的
//        data.setValueTypeface(mTfLight);
        showPieChart.setData(chartData);
        showPieChart.highlightValues(null);

        showPieChart.invalidate();
    }


    private void generatePicFromView() {
        int height = shareMain.getHeight();
        int width = shareMain.getWidth();
        //必须设置这个，不然背景是黑色的
        shareMain.setBackgroundResource(R.color.white);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas cs = new Canvas(bitmap);
        shareMain.draw(cs);
        saveCroppedImage(bitmap);

    }

    private void saveCroppedImage(Bitmap bmp) {
        File file = new File(dir, Constants.share_pic_name);
//        if (file.exists()) {
//            file.delete();
//            Logger.e(file.toString());
//        }
//        String fileName = file.getName();
//        String mName = fileName.substring(0, fileName.lastIndexOf("."));
//        String sName = fileName.substring(fileName.lastIndexOf("."));

        // /sdcard/myFolder/temp_cropped.jpg
//        String newFilePath = "/sdcard/" + Constants.SHARED_PREFERENCE_NAME + "/" + mName + "_cropped" + sName;
//        file = new File(newFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void show() {
        super.show();
        mShareDialog.startAnimation(appearAnim);
        setContentView(mShareDialog);
    }

    @Override
    public void dismiss() {
        mShareDialog.startAnimation(disAppearAnim);
        super.dismiss();
    }
}
