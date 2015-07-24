package com.leili.imhere.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Lei.Li on 7/24/15 11:17 AM.
 */
public class ViewUtils {
    private ViewUtils() {}

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
