package bc.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iscod.
 * Time:2016/9/19-15:31.
 * 封装的通用的RecyclerView的Adapter
 * 可以采用匿名类的方式去给RecycleView设置adapter。
 */
public abstract class RecyclerViewAdapter<E> extends RecyclerView.Adapter<ViewHolder> {
    private List<E> mList = new ArrayList<E>();
    private int itemId;
    protected Context mContext;
    protected LayoutInflater mInflater;
    private View mHeader;
    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_NORMAL = 1;

    public RecyclerViewAdapter(Context context, int itemId) {
        //Log.d("CID", "RecyclerViewAdapter");
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.itemId = itemId;
    }

    public RecyclerViewAdapter(Context context, List<E> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    public RecyclerViewAdapter(Context context, int itemId, List<E> list) {
        mContext = context;
        mList = list;
        this.itemId = itemId;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d("CID", "onCreateViewHolder");
        if (mHeader != null && viewType == ITEM_TYPE_HEADER) {
            return ViewHolder.get(mHeader);//实际上是创建了一个控ViewHolder
        } else {
            return ViewHolder.get(mContext, itemId);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Log.d("CID", "onBindViewHolder");
        if (getItemViewType(position) == ITEM_TYPE_HEADER) return;
        final int finPos = getFinalPosition(holder);
        if (listener != null) {
            holder.itemView.setOnClickListener(v -> listener.ItemOnClick(finPos));
        }
        setItemData(holder, mList.get(finPos), finPos);
    }

    private int getFinalPosition(ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeader == null ? position : position - 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeader == null) return ITEM_TYPE_NORMAL;
        if (position == 0) return ITEM_TYPE_HEADER;
        return ITEM_TYPE_NORMAL;
    }

    public void addData(List<E> list) {
        if (list == null) return;
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void setData(List<E> list) {
        if (list == null) return;
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public E getItemData(int position) {
        return mList.get(position);
    }

    public List<E> getList() {
        return mList;
    }

    public void remove(int position) {
        int size = mList.size();
        if (position >= size) return;
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(E e) {
        if (mList.contains(e)) {
            mList.remove(e);
            notifyDataSetChanged();
        }
    }

    public void addHeader(View header) {
        mHeader = header;
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return mHeader == null ? mList.size() : mList.size() + 1;
    }

    public OnItemClickListener listener;

    public RecyclerViewAdapter<E> setOnItemClickListener(OnItemClickListener listener) {
        //Log.d("CID", "setOnItemClickListener");
        this.listener = listener;
        return this;
    }

    protected abstract void setItemData(ViewHolder holder, E itemData, int position);
}
