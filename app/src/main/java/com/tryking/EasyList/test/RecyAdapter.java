package com.tryking.EasyList.test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tryking.EasyList.bean.TodayEventData;
import com.tryking.EasyList.utils.CommonUtils;
import com.tryking.timer.R;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tryking on 2016/5/23.
 */
public class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.ViewHolder> {
    private WeakReference<Context> mContext;
    private LayoutInflater mInflater;
    private List<TodayEventData> mData;

    public RecyAdapter(WeakReference<Context> context, List data) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext.get());
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = mInflater.inflate(R.layout.item_today_event_no_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.hint.setText(CommonUtils.addSignToStr(mData.get(position).getStartTime()) + "-" + CommonUtils.addSignToStr(mData.get(position).getEndTime()) + "::" + mData.get(position).getSpecificEvent());
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.hint)
        TextView hint;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


   class myCallback extends ItemTouchHelper.Callback{

       @Override
       public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
           return 0;
       }

       @Override
       public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
           return false;
       }

       @Override
       public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

       }
   }
}
