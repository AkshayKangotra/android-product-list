package com.walmartlabs.productlist.ui.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.costum.android.widget.LoadMoreListView;
import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.adapters.ProductAdapter;
import com.walmartlabs.productlist.bean.ProductBean;
import com.walmartlabs.productlist.controller.ProductController;

public class ProductListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ProductAdapter mProductAdapter;
    private LoadMoreListView mLoadMoreListView;
    private ProgressBar mProgressBarFirstLoad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mProductAdapter = new ProductAdapter(getActivity(), null);
        getLoaderManager().initLoader(0, null, this);

        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mProgressBarFirstLoad = (ProgressBar) view.findViewById(R.id.prg_bar_first_load);
        mLoadMoreListView = (LoadMoreListView) view.findViewById(R.id.listview_product);
        mLoadMoreListView.setAdapter(mProductAdapter);

        mLoadMoreListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                ProductController productController = new ProductController(getActivity());
                productController.loadProducts();
            }
        });
    }

    //Loader methods
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        ProductController productController = new ProductController(getActivity().getApplicationContext());
        return productController.getProductLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mProductAdapter.swapCursor(cursor);

        if (cursor != null && cursor.getCount() > 0) {
            mProgressBarFirstLoad.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mProductAdapter.swapCursor(null);
    }

    public void loadCompleted() {
        if (mLoadMoreListView != null) {
            mLoadMoreListView.onLoadMoreComplete();
        }
    }

    public interface OnProductListActionListener {
        public void onClickProduct(ProductBean productBean);
    }

}
