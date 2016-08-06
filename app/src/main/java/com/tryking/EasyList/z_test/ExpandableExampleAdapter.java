package com.tryking.EasyList.z_test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.tryking.EasyList.R;

/**
 * Created by 26011 on 2016/8/3.
 */
public class ExpandableExampleAdapter extends AbstractExpandableItemAdapter<ExpandableExampleAdapter.MyGroupViewHolder, ExpandableExampleAdapter.MyChildViewHolder> {
    private AbstractExpandableDataProvider mProvider;

    // NOTE: Make accessible with short name
    private interface Expandable extends ExpandableItemConstants {
    }

    public ExpandableExampleAdapter(AbstractExpandableDataProvider dataProvider) {
        mProvider = dataProvider;
        setHasStableIds(true);
    }

    public static abstract class MyBaseViewHolder extends AbstractExpandableItemViewHolder {
        public FrameLayout mContainer;
        public TextView mTextView;

        public MyBaseViewHolder(View view) {
            super(view);
            mContainer = (FrameLayout) view.findViewById(R.id.container);
            mTextView = (TextView) view.findViewById(android.R.id.text1);
        }
    }

    public static class MyGroupViewHolder extends MyBaseViewHolder {
//        public ExpandableItemIndicator mIndicator;

        public MyGroupViewHolder(View itemView) {
            super(itemView);
//            mIndicator = (ExpandableItemIndicator) itemView.findViewById(R.id.indicator);
        }
    }

    public static class MyChildViewHolder extends MyBaseViewHolder {

        public MyChildViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getGroupCount() {
        return mProvider.getGroupCount();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mProvider.getChildCount(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mProvider.getGroupItem(groupPosition).getGroupId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mProvider.getChildItem(groupPosition, childPosition).getChildId();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public MyGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_group_item, parent, false);
        return new MyGroupViewHolder(v);
    }

    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item, parent, false);
        return new MyChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(MyGroupViewHolder holder, int groupPosition, int viewType) {
        AbstractExpandableDataProvider.GroupData groupItem = mProvider.getGroupItem(groupPosition);

        holder.mTextView.setText(groupItem.getText());
        holder.itemView.setClickable(true);

        int expandStateFlags = holder.getExpandStateFlags();
        if ((expandStateFlags & ExpandableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;
            boolean isExpanded;
            boolean animateIndicator = ((expandStateFlags & Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((expandStateFlags & Expandable.STATE_FLAG_IS_EXPANDED) != -0) {
                bgResId = R.drawable.bg_group_item_expanded_state;
                isExpanded = false;
            } else {
                bgResId = R.drawable.bg_group_item_normal_state;
                isExpanded = false;
            }

            holder.mContainer.setBackgroundResource(bgResId);
//            holder.mIndicator.setExpandedState(isExpanded, animateIndicator);
        }
    }

    @Override
    public void onBindChildViewHolder(MyChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
        AbstractExpandableDataProvider.ChildData childItem = mProvider.getChildItem(groupPosition, childPosition);

        holder.mTextView.setText(childItem.getText());

        int bgResId;
        bgResId = R.drawable.bg_item_normal_state;
        holder.mContainer.setBackgroundResource(bgResId);
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {

        if (mProvider.getGroupItem(groupPosition).isPinned()) {
            return false;
        }
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }
        return true;
    }
}
