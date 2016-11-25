package cn.ml.base.recycleView;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ml.base.image.ImageLoader;


/**
 * Created by iscod.
 * Time:2016/9/19-17:07.
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    private View mItemView;
    private SparseArrayCompat<View> mViews;
    private Context mContext;

    public ViewHolder(Context context, View itemView) {
        super(itemView);
        if (context == null) return;
        mItemView = itemView;
        mContext = context;
        mViews = new SparseArrayCompat<View>();
    }

    public static ViewHolder get(View headerView) {
        return new ViewHolder(null, headerView);
    }

    public static ViewHolder get(Context context, int itemId) {
        return new ViewHolder(context, LayoutInflater.from(context).inflate(itemId, null));
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public TextView setTextView(int viewId, String content) {
        TextView textView = getView(viewId);
        textView.setText(content);
        return textView;
    }

    public View setVisibility(int viewId, int state) {
        View view = getView(viewId);
        view.setVisibility(state);
        return view;
    }

    public void setImageView(int viewId, String url) {
        ImageView imageView = getView(viewId);
        ImageLoader.loadImage(mContext, url, imageView);
    }

    public ImageView setImageResource(int viewId, int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return imageView;
    }

    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}
