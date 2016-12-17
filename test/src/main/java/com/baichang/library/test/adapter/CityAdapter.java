package com.baichang.library.test.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baichang.android.widget.recycleView.OnItemClickListener;
import com.baichang.library.test.R;
import com.baichang.library.test.model.CityData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iscod.
 * Time:2016/9/18-18:29.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private List<CityData> mList;
    private LayoutInflater mInflater;

    public CityAdapter(Context context, List<CityData> list) {
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityViewHolder(mInflater.inflate(R.layout.item_select_city, null));
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        CityData city = mList.get(position);
        //如果是第一数据显示分组头。或者当前的item跟上一个item的首字母不是一样的也显示
        //否则就一组的，隐藏头部，此方法适用于数据已经排序，不然会出现多个分组的现象。
        if (position == 0 || !mList.get(position - 1).index.equals(city.index)) {
            holder.tvIndex.setVisibility(View.VISIBLE);
            holder.tvIndex.setText(city.index);
        } else {
            holder.tvIndex.setVisibility(View.GONE);
        }
        holder.tvName.setText(mList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class CityViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_select_city_tv_name)
        TextView tvName;
        @BindView(R.id.item_select_city_tv_index)
        TextView tvIndex;
        @BindView(R.id.item_select_city_item)
        FrameLayout item;

        public CityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            item.setOnClickListener(v -> {
                if (listener == null) return;
                listener.ItemOnClick(getAdapterPosition());
            });
        }
    }
}
