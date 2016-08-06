package com.tryking.EasyList.fragment.viewhistory;

import android.annotation.SuppressLint;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.viewHistoryBean.ViewHistoryChildData;
import com.tryking.EasyList._bean.viewHistoryBean.ViewHistoryGroupData;
import com.tryking.EasyList._bean.viewHistoryBean.ViewMonthReturnBean;
import com.tryking.EasyList.adapter.viewhistory.ViewHistoryExpandableAdapter;
import com.tryking.EasyList.utils.CommonUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 26011 on 2016/8/3.
 */
@SuppressLint("ValidFragment")
public class ViewHistoryFragment extends Fragment implements RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;
    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";
    private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    private RecyclerView.Adapter mWrapperAdapter;

    private ViewMonthReturnBean mViewMonthReturnBean;

    List<ViewHistoryGroupData> mGroupDatas = new ArrayList<>();
    List<List<ViewHistoryChildData>> mChildDatas = new ArrayList<>();

    /**
     * 这样写不是很规范
     *
     * @param viewMonthReturnBean
     */
    @SuppressLint("ValidFragment")
    public ViewHistoryFragment(ViewMonthReturnBean viewMonthReturnBean) {
        mViewMonthReturnBean = viewMonthReturnBean;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_list_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLayoutManager = new LinearLayoutManager(getContext());

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        mRecyclerViewExpandableItemManager.setOnGroupCollapseListener(this);
        mRecyclerViewExpandableItemManager.setOnGroupExpandListener(this);

        getData();
        final ViewHistoryExpandableAdapter myItemAdapter = new ViewHistoryExpandableAdapter(mGroupDatas, mChildDatas);
        mWrapperAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(myItemAdapter);
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        animator.setSupportsChangeAnimations(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrapperAdapter);
        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setHasFixedSize(false);

        if (supportsViewElevation()) {

        } else {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }
        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));
        mRecyclerViewExpandableItemManager.attachRecyclerView(mRecyclerView);
    }

    private void getData() {
        //数据
//        for (int i = 0; i < 20; i++) {
//            ViewHistoryGroupData parentData = new ViewHistoryGroupData("201" + i, "一句话", i);
//            mGroupDatas.add(parentData);
//            ArrayList<ViewHistoryChildData> childDatas = new ArrayList<>();
//            for (int j = 0; j < 5; j++) {
//                ViewHistoryChildData childData = new ViewHistoryChildData(1, "1012", "1123", "具体是", j);
//                childDatas.add(childData);
//            }
//            mChildDatas.add(childDatas);
//        }

        for (int i = 0; i < mViewMonthReturnBean.getMonthEvents().size(); i++) {
            ViewHistoryGroupData viewHistoryGroupData = new ViewHistoryGroupData(CommonUtils.addZitoDate(mViewMonthReturnBean.getMonthEvents().get(i).getDate()), "", i);
            mGroupDatas.add(viewHistoryGroupData);
            ArrayList<ViewHistoryChildData> childDatas = new ArrayList<>();
            for (int j = 0; j < mViewMonthReturnBean.getMonthEvents().get(i).getEventList().size(); j++) {
                ViewHistoryChildData childData = new ViewHistoryChildData(Integer.parseInt(mViewMonthReturnBean.getMonthEvents().get(i).getEventList().get(j).getEventtypes()),
                        mViewMonthReturnBean.getMonthEvents().get(i).getEventList().get(j).getStarttime(),
                        mViewMonthReturnBean.getMonthEvents().get(i).getEventList().get(j).getEndtime(),
                        mViewMonthReturnBean.getMonthEvents().get(i).getEventList().get(j).getRecord(), j);
                childDatas.add(childData);
            }
            mChildDatas.add(childDatas);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRecyclerViewExpandableItemManager != null) {
            outState.putParcelable(
                    SAVED_STATE_EXPANDABLE_ITEM_MANAGER,
                    mRecyclerViewExpandableItemManager.getSavedState());
        }
    }

    @Override
    public void onDestroy() {
        if (mRecyclerViewExpandableItemManager != null) {
            mRecyclerViewExpandableItemManager.release();
            mRecyclerViewExpandableItemManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrapperAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrapperAdapter);
            mWrapperAdapter = null;
        }

        mLayoutManager = null;
        super.onDestroy();
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser) {

    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser) {
        if (fromUser) {
            adjustScrollPositionOnGroupExpanded(groupPosition);
        }
    }

    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
        int childItemHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.list_item_height);
        int topMargin = (int) (getActivity().getResources().getDisplayMetrics().density * 16);
        int bottomMargin = topMargin;
        mRecyclerViewExpandableItemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
    }


    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    //友盟统计：由Activity和Fragment构成的页面需要这样写
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.view_history));
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.view_history));
    }
}
