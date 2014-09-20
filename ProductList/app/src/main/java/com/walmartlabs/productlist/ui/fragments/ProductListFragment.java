package com.walmartlabs.productlist.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.bean.ProductBean;
import com.walmartlabs.productlist.util.LoadMoreListView;

public class ProductListFragment extends ListFragment {

    private OnProductListActionListener productListActionListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LoadMoreListView loadMoreListView = (LoadMoreListView) view.findViewById(R.id.listview_product);

        loadMoreListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int previousTotal, int currentPage) {

            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            productListActionListener = (OnProductListActionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        productListActionListener = null;
    }

    public interface OnProductListActionListener {
        public void onClickProduct(ProductBean productBean);
    }

}
