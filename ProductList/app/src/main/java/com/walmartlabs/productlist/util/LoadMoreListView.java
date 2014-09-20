package com.walmartlabs.productlist.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.walmartlabs.productlist.R;

import java.util.concurrent.atomic.AtomicBoolean;

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    private AtomicBoolean loading = new AtomicBoolean(false);
    private OnLoadMoreListener onLoadMoreListener;
    private View loadView;

    public LoadMoreListView(Context context) {
        super(context);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        setOnScrollListener(this);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        LayoutInflater inflater = LayoutInflater.from(super.getContext());
        loadView = inflater.inflate(R.layout.load_more_view, null);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    //Based on http://www.survivingwithandroid.com/2013/10/android-listview-endless-adapter.html
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (getAdapter() == null) {
            return;
        }
        if (getAdapter().getCount() == 0) {
            return;
        }

        int lastVisibleItemIndex = visibleItemCount + firstVisibleItem;

        if (lastVisibleItemIndex >= totalItemCount && loading.compareAndSet(false,true)) {
            Log.d("Request Products", "LoadMore");
            this.addFooterView(loadView);
            onLoadMoreListener.onLoadMore(totalItemCount);
        }
    }

    public void loadMoreCompleted() {
        this.removeFooterView(loadView);
        loading.set(false);
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int previousTotal);
    }
}
