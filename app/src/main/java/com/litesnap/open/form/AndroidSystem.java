package com.litesnap.open.form;

import android.content.Context;

/**
 * Created by ZTMIDGO on 2018/2/9.
 */

public class AndroidSystem {

    public static float px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return pxValue / scale + 0.5f;
    }

    public static float dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dipValue * scale + 0.5f;
    }
}
