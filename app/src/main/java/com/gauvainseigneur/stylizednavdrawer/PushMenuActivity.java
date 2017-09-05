package com.gauvainseigneur.stylizednavdrawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class PushMenuActivity extends BaseNavDrawerActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpNavigationDrawer(toolbar);
    }

    @Override
    public void setUpNavigationAnimation(View drawerView, float slideOffset){
        pushNavDrawer(drawerView,slideOffset);
        moveInverseItemMenu(drawerView,slideOffset);
    }
}
