package com.gauvainseigneur.stylizednavdrawer;

import android.content.Intent;
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
        fakeShadow.setVisibility(View.VISIBLE);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null) {
            startIntroAnim(childActivityContainer);
            startIntroAnim(fakeShadow);
        }

    }

    @Override
    public void setNavDrawerSlideAnimation(View drawerView, float slideOffset){
        complexNavDrawerAnim(drawerView,slideOffset);
    }
}
