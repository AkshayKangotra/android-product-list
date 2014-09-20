package com.walmartlabs.productlist.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    private int visibleThreshold = 10;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private OnLoadMoreListener onLoadMoreListener;

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

    private void init() {
        setOnScrollListener(this);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        Log.d("Scroll", "onScroll currentPage = " + currentPage + " visibleItemCount ="
                + visibleItemCount + " totalItemCount =" + totalItemCount +
                " firstVisibleItem =" + firstVisibleItem);

        if (loading) {
            if (totalItemCount > previousTotal) {
                Log.d("Scroll", "NewPage currentPage = " + currentPage + " visibleItemCount ="
                + visibleItemCount + " totalItemCount =" + totalItemCount +
                        " firstVisibleItem =" + firstVisibleItem);
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }

        if (!loading
                && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {

            Log.d("Scroll", "LoadMore currentPage = " + currentPage + " visibleItemCount ="
                    + visibleItemCount + " totalItemCount =" + totalItemCount +
                    " firstVisibleItem =" + firstVisibleItem);

            if (onLoadMoreListener != null) {
                onLoadMoreListener.onLoadMore(previousTotal, currentPage);
            }

            loading = true;
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int previousTotal, int currentPage);
    }
}
