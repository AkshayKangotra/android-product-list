package com.walmartlabs.productlist.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.bitmap.BitmapInfo;
import com.koushikdutta.ion.bitmap.IonBitmapCache;
import com.walmartlabs.productlist.R;

public class ImageLoadUtil {

    public static void loadImage(Context context, final ImageView imageView, final String url, final IonBitmapCache ionBitmapCache) {
        Ion.with(context).load(url).withBitmap().asBitmap()
                .setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {
                        if (result != null) {
                            BitmapInfo info = new BitmapInfo(AndroidUtil.sha256String(url),
                                    Constants.ION_CACHE_IMAGE_TYPE, new Bitmap[]{result}, null);
                            ionBitmapCache.put(info);
                            imageView.setImageBitmap(result);
                        } else {
                            imageView.setImageResource(R.drawable.ic_launcher);
                        }
                    }
                });
    }

}
