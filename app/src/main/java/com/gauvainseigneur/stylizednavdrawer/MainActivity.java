package com.gauvainseigneur.stylizednavdrawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends BaseNavDrawerActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpNavigationDrawer(toolbar);
        fullNavDrawer();
        fakeShadow.setVisibility(View.VISIBLE);
        closeNavDrawerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUpNavigationAnimation(View drawerView, float slideOffset){
        complexNavDrawerAnim(drawerView,slideOffset);
    }



}
