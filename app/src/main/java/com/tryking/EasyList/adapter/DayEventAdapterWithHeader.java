package com.tryking.EasyList.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.TodayEventData;
import com.tryking.EasyList.utils.AppUtils;
import com.tryking.EasyList.utils.CommonUtils;
import com.tryking.EasyList.widgets.NumberPickerPopupWindow;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tryking on 2016/5/16.
 */
public class DayEventAdapterWithHeader extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TodayEventData> mData;
    private LayoutInflater mInflater;
    private Context mCtx;
    private boolean isNeedAnim = true;
    private int HEADER_VIEW_TYPE = 6;
    private int workTime;
    private int amuseTime;
    private int lifeTime;
    private int studyTime;

    String[] mParties = new String[]{
            "工作", "娱乐", "生活", "学习"
    };
    private PieData chartData;
    private PieData headerData;

    /**
     * @param
     * @param ctx  这个因为运行动画需要
     * @param data
     */
    public DayEventAdapterWithHeader(Context ctx, List<TodayEventData> data, boolean needAnim) {
        mData = data;
        mInflater = LayoutInflater.from(ctx);
        mCtx = ctx;
        isNeedAnim = needAnim;

        handleData(data);
    }

    private void handleData(List<TodayEventData> datas) {
        for (int i = 0; i < datas.size(); i++) {
            switch (datas.get(i).getDataType()) {
                case TodayEventData.TYPE_WORK:
                    workTime += CommonUtils.durationMinutes(datas.get(i).getStartTime(), datas.get(i).getEndTime());
                    break;
                case TodayEventData.TYPE_AMUSE:
                    amuseTime += CommonUtils.durationMinutes(datas.get(i).getStartTime(), datas.get(i).getEndTime());
                    break;
                case TodayEventData.TYPE_LIFE:
                    lifeTime += CommonUtils.durationMinutes(datas.get(i).getStartTime(), datas.get(i).getEndTime());
                    break;
                case TodayEventData.TYPE_STUDY:
                    studyTime += CommonUtils.durationMinutes(datas.get(i).getStartTime(), datas.get(i).getEndTime());
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

        colors.add(mCtx.getResources().getColor(R.color.work));
        colors.add(mCtx.getResources().getColor(R.color.amuse));
        colors.add(mCtx.getResources().getColor(R.color.life));
        colors.add(mCtx.getResources().getColor(R.color.study));

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        headerData = new PieData(dataSet);
        headerData.setValueFormatter(new PercentFormatter());
        headerData.setValueTextSize(11f);
        headerData.setValueTextColor(Color.WHITE);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == HEADER_VIEW_TYPE) {
            view = mInflater.inflate(R.layout.header_yesterday_add, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == TodayEventData.TYPE_NO_EVENT) {
            view = mInflater.inflate(R.layout.item_today_event_no_event, parent, false);
            return new NoEventViewHolder(view);
        } else {
            view = mInflater.inflate(R.layout.item_today_event_have_event, parent, false);
            return new HaveEventViewHolder(view);
        }
    }

    public interface onNoEventItemClickListener {
        void onNoEventItemClick(int position, String hint);
    }

    private onNoEventItemClickListener mNoEventListener;

    public void setOnNoEventItemClickListener(onNoEventItemClickListener listener) {
        mNoEventListener = listener;
    }

    public interface onHaveEventItemClickListener {
        void onHaveEventItemClick(int position, String hint);
    }

    private onHaveEventItemClickListener mHaveEventListener;

    public void setOnHaveEventItemClickListener(onHaveEventItemClickListener listener) {
        mHaveEventListener = listener;
    }

    public interface onHaveEventItemLongClickListener {
        void onHaveEventItemLongClick(int position, String startTime, String endTime);
    }

    private onHaveEventItemLongClickListener mHaveEventItemLongClickListener;

    public void setOnHaveEventItemLongClickListener(onHaveEventItemLongClickListener listener) {
        mHaveEventItemLongClickListener = listener;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        RealmBindViewHolder(holder, position - 1);
    }

    private void RealmBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (isNeedAnim) {
            runEnterAnimation(holder.itemView, position);
        }
        if (holder instanceof HaveEventViewHolder) {
            switch (mData.get(position).getDataType()) {
                case TodayEventData.TYPE_WORK:
                    ((HaveEventViewHolder) holder).llParent.setBackgroundResource(R.drawable.pressed_work);
                    break;
                case TodayEventData.TYPE_AMUSE:
                    ((HaveEventViewHolder) holder).llParent.setBackgroundResource(R.drawable.pressed_amuse);
                    break;
                case TodayEventData.TYPE_LIFE:
                    ((HaveEventViewHolder) holder).llParent.setBackgroundResource(R.drawable.pressed_life);
                    break;
                case TodayEventData.TYPE_STUDY:
                    ((HaveEventViewHolder) holder).llParent.setBackgroundResource(R.drawable.pressed_study);
                    break;
                default:
                    break;
            }
            String durationTime = CommonUtils.durationTime(mData.get(position).getStartTime(), mData.get(position).getEndTime());
            int durationMinutes = CommonUtils.durationMinutes(mData.get(position).getStartTime(), mData.get(position).getEndTime());
            ((HaveEventViewHolder) holder).startTime.setText(CommonUtils.addSignToStr(mData.get(position).getStartTime()));
            ((HaveEventViewHolder) holder).endTime.setText(CommonUtils.addSignToStr(mData.get(position).getEndTime()));
            ((HaveEventViewHolder) holder).specificEvent.setText(mData.get(position).getSpecificEvent());
            ((HaveEventViewHolder) holder).durationTime.setText(durationTime);
            ((HaveEventViewHolder) holder).llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHaveEventListener != null) {
                        mHaveEventListener.onHaveEventItemClick(position, mData.get(position).getSpecificEvent());
                    }
                }
            });
            ViewGroup.LayoutParams layoutParams = ((HaveEventViewHolder) holder).llParent.getLayoutParams();
            if (durationMinutes <= 60) {
                layoutParams.height = 150;
            } else if (durationMinutes <= 360) {
                layoutParams.height = 150 + (durationMinutes - 60) * 250 / 300;
            } else {
                layoutParams.height = 400;
            }

            ((HaveEventViewHolder) holder).llParent.setLayoutParams(layoutParams);
            ((HaveEventViewHolder) holder).llParent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mHaveEventItemLongClickListener != null) {
                        mHaveEventItemLongClickListener.onHaveEventItemLongClick(position, mData.get(position).getStartTime(), mData.get(position).getEndTime());
                    }
                    return true;
                }
            });
        } else if (holder instanceof NoEventViewHolder) {
            ((NoEventViewHolder) holder).hint.setText(CommonUtils.addSignToStr(mData.get(position).getStartTime()) + "\t\t-\t\t" + CommonUtils.addSignToStr(mData.get(position).getEndTime()) + "\t\t\t\t" + mData.get(position).getSpecificEvent());
            ((NoEventViewHolder) holder).hint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mNoEventListener != null) {
                        mNoEventListener.onNoEventItemClick(position, mData.get(position).getSpecificEvent());
                    }
                }
            });
        } else {
            ((HeaderViewHolder) holder).headerPieChart.setUsePercentValues(true);
            ((HeaderViewHolder) holder).headerPieChart.setDescription("");
            ((HeaderViewHolder) holder).headerPieChart.setExtraOffsets(5, 10, 5, 5);
            ((HeaderViewHolder) holder).headerPieChart.setDragDecelerationFrictionCoef(0.95f);

            ((HeaderViewHolder) holder).headerPieChart.setDrawHoleEnabled(true);
            ((HeaderViewHolder) holder).headerPieChart.setHoleColor(Color.WHITE);

            ((HeaderViewHolder) holder).headerPieChart.setTransparentCircleColor(Color.WHITE);
            ((HeaderViewHolder) holder).headerPieChart.setTransparentCircleAlpha(110);

            ((HeaderViewHolder) holder).headerPieChart.setHoleRadius(58f);
            ((HeaderViewHolder) holder).headerPieChart.setTransparentCircleRadius(61f);

            ((HeaderViewHolder) holder).headerPieChart.setDrawCenterText(true);

            ((HeaderViewHolder) holder).headerPieChart.setRotationAngle(0);
            // enable rotation of the chart by touch
            ((HeaderViewHolder) holder).headerPieChart.setRotationEnabled(true);
            ((HeaderViewHolder) holder).headerPieChart.setHighlightPerTapEnabled(true);

            // ((HeaderViewHolder)holder).headerPieChart.setUnit(" €");
            // ((HeaderViewHolder)holder).headerPieChart.setDrawUnitsInChart(true);

            // add a selection listener
//            ((HeaderViewHolder) holder).headerPieChart.setOnChartValueSelectedListener(this);

            ((HeaderViewHolder) holder).headerPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            // mChart.spin(2000, 0, 360);

            Legend l = ((HeaderViewHolder) holder).headerPieChart.getLegend();
            l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

            // entry label styling
            ((HeaderViewHolder) holder).headerPieChart.setEntryLabelColor(Color.WHITE);
            ((HeaderViewHolder) holder).headerPieChart.setEntryLabelTextSize(12f);

            //这个应当是和字体有关的
//        data.setValueTypeface(mTfLight);
            ((HeaderViewHolder) holder).headerPieChart.setData(headerData);
            ((HeaderViewHolder) holder).headerPieChart.highlightValues(null);

            ((HeaderViewHolder) holder).headerPieChart.invalidate();
        }
    }

    private void runEnterAnimation(View view, int position) {
        view.setTranslationY(AppUtils.getScreenHeight(mCtx));
        view.animate()
                .translationY(0)
                .setStartDelay(10 * position)
                .setInterpolator(new DecelerateInterpolator(3.0f))
                .setDuration(400)
                .start();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_VIEW_TYPE;
        }
        return mData.get(position - 1).getDataType();
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    class HaveEventViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ll_parent)
        LinearLayout llParent;
        @Bind(R.id.start_time)
        TextView startTime;
        @Bind(R.id.end_time)
        TextView endTime;
        @Bind(R.id.specific_event)
        TextView specificEvent;
        @Bind(R.id.tv_duration)
        TextView durationTime;

        public HaveEventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class NoEventViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.hint)
        TextView hint;

        public NoEventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.header_pieChart)
        PieChart headerPieChart;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void refresh(List<TodayEventData> data) {
        mData = data;
        notifyDataSetChanged();
    }

}
