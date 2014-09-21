package com.walmartlabs.productlist.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.bean.ProductBean;
import com.walmartlabs.productlist.controller.ProductController;
import com.walmartlabs.productlist.dao.SharedPreferencesHelper;
import com.walmartlabs.productlist.ui.fragments.ProductFragment;
import com.walmartlabs.productlist.ui.fragments.ProductListFragment;
import com.walmartlabs.productlist.util.Constants;

import java.util.List;


public class ProductListActivity extends ActionBarActivity implements ProductListFragment.OnProductListActionListener{

    ProductListFragment mProductListFragment;
    ProductFragment mProductFragment;

    BroadcastReceiver loadFinishedBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!intent.hasExtra(Constants.LIST_SIZE_INTENT_EXTRA)) {
                Toast.makeText(ProductListActivity.this, getString(R.string.load_products_error), Toast.LENGTH_LONG).show();
            }
            if (mProductListFragment != null) {
                mProductListFragment.loadCompleted();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        mProductListFragment = (ProductListFragment) getSupportFragmentManager().findFragmentById(R.id.product_list_fragment);
        mProductFragment = (ProductFragment) getSupportFragmentManager().findFragmentById(R.id.product_detail_fragment);

        LocalBroadcastManager.getInstance(this).registerReceiver(loadFinishedBroadcastReceiver, new IntentFilter(Constants.LOAD_FINISHED_ACTION));

        new ProductController(getApplicationContext()).updateProducts();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loadFinishedBroadcastReceiver);
    }

    @Override
    public void onClickProduct(ProductBean productBean) {
        if (getResources().getBoolean(R.bool.is_tablet)) {
            findViewById(R.id.container_details).setVisibility(View.VISIBLE);
            mProductFragment.setLayoutValues(productBean);
        } else {
            Intent intent = new Intent(this, ProductActivity.class);
            intent.putExtra(Constants.PRODUCT_ID_INTENT_EXTRA, productBean.productId);
            startActivity(intent);
        }
    }

}
