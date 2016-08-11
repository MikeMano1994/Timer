package com.tryking.EasyList.adapter.viewhistory;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.ViewHistoryHandlerData;
import com.tryking.EasyList._bean.viewHistoryBean.ViewHistoryChildData;
import com.tryking.EasyList._bean.viewHistoryBean.ViewHistoryGroupData;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.utils.CommonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 26011 on 2016/8/3.
 */
public class ViewHistoryExpandableAdapter extends AbstractExpandableItemAdapter<ViewHistoryExpandableAdapter.MyGroupViewHolder, ViewHistoryExpandableAdapter.MyChildViewHolder> {


    private List<ViewHistoryGroupData> mGroupDatas;
    private List<List<ViewHistoryChildData>> mChildDatas;
    private Context mContext;
    private Handler mHandler;


    // NOTE: Make accessible with short name
    private interface Expandable extends ExpandableItemConstants {
    }

    public ViewHistoryExpandableAdapter(Context context, Handler handler, List<ViewHistoryGroupData> groupDatas, List<List<ViewHistoryChildData>> childDatas) {
        mGroupDatas = groupDatas;
        mChildDatas = childDatas;
        setHasStableIds(true);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public MyGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_view_history_list_group, parent, false);
        return new MyGroupViewHolder(view);
    }

    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        if (viewType == ViewHistoryChildData.TYPE_NO_EVENT) {
            view = inflater.inflate(R.layout.item_today_event_no_event, parent, false);
        } else {
            view = inflater.inflate(R.layout.item_today_event_have_event, parent, false);
        }
        return new MyChildViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(MyGroupViewHolder holder, final int groupPosition, int viewType) {
        holder.groupDate.setText(mGroupDatas.get(groupPosition).getDate());
        holder.groupOneWord.setText(mGroupDatas.get(groupPosition).getOneWord().equals("") ? "和时间做朋友" : mGroupDatas.get(groupPosition).getOneWord());
        holder.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                ViewHistoryHandlerData viewHistoryHandlerData = new ViewHistoryHandlerData();
                viewHistoryHandlerData.setGroupData(mGroupDatas.get(groupPosition));
                viewHistoryHandlerData.setChildData(mChildDatas.get(groupPosition));
                msg.obj = viewHistoryHandlerData;
                msg.what = Constants.ViewHistory.SHARE_HANDLER_DATA;
                mHandler.sendMessage(msg);
            }
        });

        final int expandStateFlags = holder.getExpandStateFlags();
        if ((expandStateFlags & ExpandableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
            int mainBgResId;
            int arrowBgResId;
            boolean isExpanded;
//            boolean animateIndicator = ((expandStateFlags & Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((expandStateFlags & Expandable.STATE_FLAG_IS_EXPANDED) != -0) {
                mainBgResId = R.drawable.bg_group_item_expanded_state;
//                arrowBgResId = R.drawable.ic_expand_more_to_expand_less;
                //不用上面的原因是因为有小米bug
                arrowBgResId = R.drawable.ic_keyboard_arrow_up_grey_500_18dp;
                isExpanded = false;
            } else {
                mainBgResId = R.drawable.bg_group_item_normal_state;
//                arrowBgResId = R.drawable.ic_expand_more;
                arrowBgResId = R.drawable.ic_keyboard_arrow_down_grey_500_18dp;
                isExpanded = false;
            }

            //这里可能需要根据是否打开来设置背景
            holder.mContainer.setBackgroundResource(mainBgResId);
//            holder.mIndicator.setExpandedState(isExpanded, animateIndicator);
            holder.mIndicator.setBackgroundResource(arrowBgResId);
        }

    }

    @Override
    public void onBindChildViewHolder(MyChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
        switch (mChildDatas.get(groupPosition).get(childPosition).getDataType()) {
            case ViewHistoryChildData.TYPE_AMUSE:
                holder.llParent.setBackgroundResource(R.drawable.pressed_amuse);
                break;
            case ViewHistoryChildData.TYPE_LIFE:
                holder.llParent.setBackgroundResource(R.drawable.pressed_life);
                break;
            case ViewHistoryChildData.TYPE_STUDY:
                holder.llParent.setBackgroundResource(R.drawable.pressed_study);
                break;
            case ViewHistoryChildData.TYPE_WORK:
                holder.llParent.setBackgroundResource(R.drawable.pressed_work);
                break;
            default:
                break;
        }

        String durationTime = CommonUtils.durationTime(mChildDatas.get(groupPosition).get(childPosition).getStartTime(), mChildDatas.get(groupPosition).get(childPosition).getEndTime());
        int durationMinutes = CommonUtils.durationMinutes(mChildDatas.get(groupPosition).get(childPosition).getStartTime(), mChildDatas.get(groupPosition).get(childPosition).getEndTime());
        holder.startTime.setText(CommonUtils.addSignToStr(mChildDatas.get(groupPosition).get(childPosition).getStartTime()));
        holder.endTime.setText(CommonUtils.addSignToStr(mChildDatas.get(groupPosition).get(childPosition).getEndTime()));
        holder.specificEvent.setText(mChildDatas.get(groupPosition).get(childPosition).getSpecificEvent());
        holder.tvDuration.setText(durationTime);

        ViewGroup.LayoutParams layoutParams = holder.llParent.getLayoutParams();
        if (durationMinutes <= 60) {
            layoutParams.height = 170;
        } else if (durationMinutes <= 360) {
            layoutParams.height = 170 + (durationMinutes - 60) * 250 / 300;
        } else {
            layoutParams.height = 420;
        }
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }

    public static class MyGroupViewHolder extends AbstractExpandableItemViewHolder {
        @Bind(R.id.group_date)
        TextView groupDate;
        @Bind(R.id.group_one_word)
        TextView groupOneWord;
        @Bind(R.id.m_container)
        LinearLayout mContainer;
        @Bind(R.id.indicator)
        ImageView mIndicator;
        @Bind(R.id.ll_share)
        LinearLayout llShare;

        public MyGroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class MyChildViewHolder extends AbstractExpandableItemViewHolder {
        @Bind(R.id.tv_duration)
        TextView tvDuration;
        @Bind(R.id.start_time)
        TextView startTime;
        @Bind(R.id.end_time)
        TextView endTime;
        @Bind(R.id.specific_event)
        TextView specificEvent;
        @Bind(R.id.ll_parent)
        LinearLayout llParent;

        public MyChildViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public int getGroupCount() {
        return mGroupDatas.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mChildDatas.get(groupPosition).size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mGroupDatas.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mChildDatas.get(groupPosition).get(childPosition).getId();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 5;
    }

}
