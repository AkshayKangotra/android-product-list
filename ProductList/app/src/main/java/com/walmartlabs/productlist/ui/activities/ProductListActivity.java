package com.walmartlabs.productlist.ui.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.bean.ProductBean;
import com.walmartlabs.productlist.ui.fragments.ProductListFragment;


public class ProductListActivity extends ActionBarActivity implements ProductListFragment.OnProductListActionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickProduct(ProductBean productBean) {

    }
}
