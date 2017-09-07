package com.gauvainseigneur.stylizednavdrawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ComplexAnimActivity extends BaseNavDrawerActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complex_anim);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpNavigationDrawer(toolbar);
        fullNavDrawer();
        closeNavDrawerView.setVisibility(View.VISIBLE);
        startIntroAnim(childActivityContainer);
    }

    @Override
    public void setUpNavigationAnimation(View drawerView, float slideOffset){
        complexNavDrawerAnim(drawerView,slideOffset);
    }
}
