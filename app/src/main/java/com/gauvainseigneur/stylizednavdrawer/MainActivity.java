package com.gauvainseigneur.stylizednavdrawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends BaseNavDrawerActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //windowNoLimit();
        fullNavDrawer();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpNavView(toolbar);
    }
}
