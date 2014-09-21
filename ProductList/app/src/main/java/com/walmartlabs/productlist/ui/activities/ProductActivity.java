package com.walmartlabs.productlist.ui.activities;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;

import com.walmartlabs.productlist.R;
import com.walmartlabs.productlist.bean.ProductBean;
import com.walmartlabs.productlist.controller.ProductController;
import com.walmartlabs.productlist.dao.ProductDBManager;
import com.walmartlabs.productlist.dao.ProductSQLHelper;
import com.walmartlabs.productlist.tracker.MixPanelDelegate;
import com.walmartlabs.productlist.ui.fragments.ProductFragment;
import com.walmartlabs.productlist.util.Constants;

public class ProductActivity extends ActionBarActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        //Lateral navigation configuration
        mViewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);

        new GetProductPositionTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MixPanelDelegate.flush();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private static final int NOT_INITIALIZED = -1;
        private int mCount = NOT_INITIALIZED;
        private SparseArray<Fragment> mPageReferenceMap;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            mPageReferenceMap = new SparseArray<Fragment>();
        }

        public ProductFragment getFragment(int position){
            return (ProductFragment) mPageReferenceMap.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new ProductFragment();

            Cursor cursor = ProductDBManager.getInstance(getApplicationContext())
                    .getAllItems(ProductSQLHelper.TABLE_PRODUCTS, null);

            if (cursor != null) {
                cursor.moveToPosition(position);
                ProductBean productBean = new ProductController(getApplicationContext()).getProductBeanFromCursor(cursor);
                cursor.close();

                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    bundle.putString(Constants.PRODUCT_ID_INTENT_EXTRA, productBean.productId);
                }

                fragment.setArguments(bundle);
            }
            mPageReferenceMap.put(position, fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            if(mCount == NOT_INITIALIZED) {
                Cursor cursor = ProductDBManager.getInstance(getApplicationContext())
                        .getAllItems(ProductSQLHelper.TABLE_PRODUCTS, null);
                if (cursor != null) {
                    mCount = cursor.getCount();
                    cursor.close();
                }
            }
            return mCount;
        }
    }

    private class GetProductPositionTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            int position = 0;

            if (getIntent() != null) {
                String productId = getIntent().getStringExtra(Constants.PRODUCT_ID_INTENT_EXTRA);
                if (productId != null) {
                    position = ProductDBManager.getInstance(ProductActivity.this.getApplicationContext())
                            .getPositionById(ProductSQLHelper.TABLE_PRODUCTS, productId);
                }
            }
            return position;
        }

        @Override
        protected void onPostExecute(Integer position) {
            mViewPager.setCurrentItem(position);
        }
    }
}
