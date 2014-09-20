package com.walmartlabs.productlist.ui.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.bean.ProductBean;
import com.walmartlabs.productlist.controller.ProductController;
import com.walmartlabs.productlist.dao.ProductDBManager;
import com.walmartlabs.productlist.dao.ProductSQLHelper;
import com.walmartlabs.productlist.ui.fragments.ProductFragment;
import com.walmartlabs.productlist.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class ProductActivity extends ActionBarActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        int position = 0;

        if (getIntent() != null) {
            String productId = getIntent().getStringExtra(Constants.PRODUCT_ID_INTENT_EXTRA);
            if (productId != null) {
                position = ProductDBManager.getInstance(this.getApplicationContext())
                        .getPositionById(ProductSQLHelper.TABLE_PRODUCTS, productId);
            }
        }

        //Lateral navigation configuration
        mViewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);

        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onStart() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product, menu);
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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private static final int NOT_INITIALIZED = -1;
        private int mCount = NOT_INITIALIZED;
        private Map<Integer, Fragment> mPageReferenceMap;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            mPageReferenceMap = new HashMap<Integer, Fragment>();
        }

        public ProductFragment getFragment(int position){
            return (ProductFragment) mPageReferenceMap.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new ProductFragment();

            Cursor cursor = ProductDBManager.getInstance(getApplicationContext()).getAllItems(ProductSQLHelper.TABLE_PRODUCTS, null);

            if (cursor != null) {
                cursor.moveToPosition(position);
                ProductBean productBean = new ProductController(getApplicationContext()).getProductBeanFromCursor(cursor);

                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    bundle.putString(Constants.PRODUCT_ID_INTENT_EXTRA, productBean.productId);
                }

                fragment.setArguments(bundle);
                cursor.close();
            }
            mPageReferenceMap.put(position, fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            if(mCount == NOT_INITIALIZED) {
                Cursor cursor = ProductDBManager.getInstance(getApplicationContext()).getAllItems(ProductSQLHelper.TABLE_PRODUCTS, null);
                if (cursor != null) {
                    mCount = cursor.getCount();
                    cursor.close();
                }
            }
            return mCount;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }
    }
}
