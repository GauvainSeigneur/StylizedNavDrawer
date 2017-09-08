package com.gauvainseigneur.stylizednavdrawer.utils;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * Created by gse on 08/09/2017.
 */

public class CustomViewOutlineProvider extends ViewOutlineProvider {

    int roundCorner;

    public CustomViewOutlineProvider(int round) {
        roundCorner = round;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), roundCorner);
    }
}