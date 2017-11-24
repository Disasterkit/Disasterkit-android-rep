package com.android.rivchat.ui.DisplayMap;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;


public class Utils {

    public static Bitmap viewToBitmap(Context c, View view) {
        view.measure(View.MeasureSpec.getSize(view.getMeasuredWidth()),
                View.MeasureSpec.getSize(view.getMeasuredHeight()));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Drawable drawable = new BitmapDrawable(c.getResources(),
                android.graphics.Bitmap.createBitmap(view.getDrawingCache()));
        view.setDrawingCacheEnabled(false);
        return AndroidGraphicFactory.convertToBitmap(drawable);
    }

}
