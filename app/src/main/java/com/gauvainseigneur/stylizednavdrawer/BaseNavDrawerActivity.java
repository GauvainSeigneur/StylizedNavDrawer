package com.gauvainseigneur.stylizednavdrawer;


import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gauvainseigneur.stylizednavdrawer.menulist.NavMenuItemObject;
import com.gauvainseigneur.stylizednavdrawer.menulist.NavMenuRVAdapter;
import com.gauvainseigneur.stylizednavdrawer.menulist.NavMenuViewHolder;
import com.gauvainseigneur.stylizednavdrawer.utils.ItemClickSupport;
import com.gauvainseigneur.stylizednavdrawer.utils.Utils;

import java.util.ArrayList;
import java.util.List;



public class BaseNavDrawerActivity extends AppCompatActivity {

    public DrawerLayout mDrawerLayout;
    public NavigationView navigationView;
    public RelativeLayout childActivityParentContainer;
    public FrameLayout childActivityContainer;
    public View fakeShadow;
    //public AppBarLayout mAppBarLayout;
    public ActionBarDrawerToggle drawerToggle;
    public Toolbar toolbar;
    public boolean isToolbarLayout;
    public boolean DrawerOpen;
    public int shadowTranslationOffset;
    public int activityPaddingLeft;
    LinearLayoutManager navigationMenuLayoutManager;
    public RecyclerView navigationViewMenu;
    public List<NavMenuItemObject> rowListNavMenuItem;
    public NavMenuRVAdapter mNavMenuRVAdapter;
    public Window window;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        shadowTranslationOffset = (int) Utils.convertDpToPixel(40,this);
        activityPaddingLeft = (int) Utils.convertDpToPixel(32,this);
        /**
         * Root Layout for extend activities
         */

        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base_nav_drawer, null);
        /**
         * RelativeLayout which include FrameLayout for child's view
         */
        childActivityParentContainer = (RelativeLayout) mDrawerLayout.findViewById(R.id.activity_parent_layout);
        /**
         * FrameLayout to inflate the child's view.
         */
        childActivityContainer = (FrameLayout) mDrawerLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, childActivityContainer, true);
        //Set content view -- we pass the view in child's actvity
        super.setContentView(mDrawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        fakeShadow = findViewById(R.id.fakeShadow);
       // mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.base_nav_coordinator_layout);
        setSupportActionBar(toolbar);
        setUpNavView(toolbar);


    }

    @Override
    public void onResume() {
        super.onResume();
        mDrawerLayout.closeDrawer(Gravity.LEFT, false);
    }

    /**
     * Helper method to allow child classes to opt-out of having the
     * hamburger menu.
     * @return
     */
    protected boolean useDrawerToggle() {
        return true;
    }

    public void setUpNavView(Toolbar toolbar) {
        setUpNavMenu();
        // use the hamburger menu
        drawerToggle = new ActionBarDrawerToggle(
                this,                           // host Activity
                mDrawerLayout,                 // DrawerLayout object
                toolbar,                      // nav drawer image to replace 'Up' caret
                R.string.nav_drawer_opened,  // "open drawer" description for accessibility
                R.string.nav_drawer_closed  // "close drawer" description for accessibility
        )
            {
            public void onDrawerClosed(View view) {
                //getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                DrawerOpen=false;
            }

            public void onDrawerOpened(View drawerView) {
                //getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                DrawerOpen=true;

            }
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_DRAGGING && isDrawerOpen() == false) {
                    // this where Drawer start opening by swipe of user;
                }
                if (newState == DrawerLayout.STATE_DRAGGING && isDrawerOpen() == true) {
                    // this where Drawer start closing by swipe of user;
                }
                if (newState == DrawerLayout.STATE_DRAGGING) {
                }
                if (newState == DrawerLayout.STATE_SETTLING) {
                }
                if (newState == DrawerLayout.STATE_IDLE && isDrawerOpen() == false) {
                    // this where Drawer start is closed
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                complexNavDrawerAnim(drawerView,slideOffset);
                //belowNavDrawer(drawerView,slideOffset);
                //fixedMenu(drawerView,slideOffset);
                moveMenu(drawerView,slideOffset);

            }

        };


        //Use a dedicated Icon to Open NavDrawer
        // comment or delete if you want to use traditional icon
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setHomeAsUpIndicator(R.drawable.ic_round_menu__black_24dp);
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // drawer layout treats fitsSystemWindows specially so we have to handle insets ourselves
        mDrawerLayout.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {

                // we place a background behind the status bar to combine with it's semi-transparent
                // color to get the desired appearance.  Set it's height to the status bar height

                View statusBarBackground = findViewById(R.id.status_bar_background);
                FrameLayout.LayoutParams lpStatus = (FrameLayout.LayoutParams)
                        statusBarBackground.getLayoutParams();
                lpStatus.height = insets.getSystemWindowInsetTop();
                statusBarBackground.setLayoutParams(lpStatus);


                return insets.consumeSystemWindowInsets();
            }
        });
    }

    protected void setUpNavMenu() {
        navigationViewMenu = (RecyclerView) findViewById(R.id.rv_navdrawer_menu);
        //navigationViewMenu.setNestedScrollingEnabled(false);
        navigationMenuLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        navigationViewMenu.setLayoutManager(navigationMenuLayoutManager);
        rowListNavMenuItem = setNavMenuList();
        mNavMenuRVAdapter = new NavMenuRVAdapter(this, rowListNavMenuItem);
        navigationViewMenu.setAdapter(mNavMenuRVAdapter);

        ItemClickSupport.addTo(navigationViewMenu).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //fullLayout.closeDrawer(GravityCompat.START); //to close navDrawer on click
                switch(position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;


                }
            }
        });
    }

    private List<NavMenuItemObject> setNavMenuList(){
        List<NavMenuItemObject> allItems = new ArrayList<NavMenuItemObject>();
        allItems.add(new NavMenuItemObject(Color.parseColor(getString(R.string.nav_drawer_item_1)),R.drawable.ic_warning_black_24dp, "Add a mean of payment"));
        allItems.add(new NavMenuItemObject(Color.parseColor(getString(R.string.nav_drawer_item_2)),R.drawable.ic_warning_black_24dp, "Find the best offer for your holiday"));
        allItems.add(new NavMenuItemObject(Color.parseColor(getString(R.string.nav_drawer_item_3)),R.drawable.ic_warning_black_24dp, "Need help ?"));
        allItems.add(new NavMenuItemObject(Color.parseColor(getString(R.string.nav_drawer_item_4)),R.drawable.ic_warning_black_24dp, "help us, give feedback"));
        allItems.add(new NavMenuItemObject(Color.parseColor(getString(R.string.nav_drawer_item_5)),R.drawable.ic_warning_black_24dp, "Contact"));
        return allItems;
}


    public void goToActivity (Class activityClass) {
        Intent goToActivityintent = new Intent(BaseNavDrawerActivity.this, activityClass);
        BaseNavDrawerActivity.this.startActivity(goToActivityintent);
        ActivityOptions.makeSceneTransitionAnimation(BaseNavDrawerActivity.this).toBundle();
    }

    protected Boolean isActivityRunning(Class activityClass) {
        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }

        return false;
    }

    public boolean isDrawerOpen() {
        return DrawerOpen;
    }


    public void windowNoLimit () {
        //this value needs to be combined with style to works fine...
        mDrawerLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Methods to animate DrawerLayout & FrameLayout
     */
    //NavigationDrawer push activity
    protected void pushNavDrawer(View drawerView, float slideOffset){
        childActivityParentContainer.setTranslationX(slideOffset * drawerView.getWidth());
        mDrawerLayout.setDrawerShadow(R.drawable.no_navdrawer_shadow, GravityCompat.START);
        mDrawerLayout.setDrawerElevation(0f);
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        mDrawerLayout.requestLayout();
    }

    //NavigationDrawer seems below activity thanks to a shadow around the activity container
    protected void belowNavDrawer(View drawerView, float slideOffset){
        float min = 0.8f;
        float max = 1.0f;
        float minShadow = 0.83f;
        float scaleFactor = (max - ((max - min) * slideOffset));
        float scaleFactorShadow = (max - ((max - minShadow) * slideOffset));
        //View moving
        childActivityParentContainer.setTranslationX(slideOffset * (drawerView.getWidth()-activityPaddingLeft));
        childActivityParentContainer.setScaleX(scaleFactor);
        childActivityParentContainer.setScaleY(scaleFactor);
        //Shadow
        fakeShadow.setTranslationX(slideOffset * (drawerView.getWidth()-shadowTranslationOffset));
        fakeShadow.setScaleX(scaleFactorShadow);
        fakeShadow.setScaleY(scaleFactorShadow);
        //disbale shadow, elevation and change fade color into transparent when nav drawer is open.
        mDrawerLayout.setDrawerShadow(R.drawable.no_navdrawer_shadow, GravityCompat.START);
        mDrawerLayout.setDrawerElevation(0f);
        mDrawerLayout.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    //NavigationDrawer move with complex animation
    protected void complexNavDrawerAnim(View drawerView, float slideOffset){
        //todo : define the bottom of activity
        int bottomMargin = (int) Utils.convertDpToPixel(128,this);
        final float minXYZ = 0;
        final float MAX_ROTATION_X = 45; //90 //45
        final float MAX_ROTATION_Y = 5; // 90
        final float MAX_ROTATION_Z = 65; //360
        final float CAMERA_DISTANCE = 6000f;
        float min = 0.40f; //0.8f
        float max = 1.0f;
        float minShadow = 0.42f;
        float scaleFactor = (max - ((max - min) * slideOffset));
        float scaleFactorShadow = (max - ((max - minShadow) * slideOffset));
        float rotationFactorX = (minXYZ - ((minXYZ - MAX_ROTATION_X)) * slideOffset);
        float rotationFactorY = (minXYZ - ((minXYZ - MAX_ROTATION_Y) * slideOffset));
        float rotationFactor = (minXYZ - ((minXYZ - MAX_ROTATION_Z) * slideOffset));
        //Transaltion
        //mCoordinatorLayout.setTranslationX(slideOffset * (drawerView.getWidth()-activityPaddingLeft));
        childActivityParentContainer.setTranslationY(slideOffset * (drawerView.getWidth()-bottomMargin));
        childActivityParentContainer.setCameraDistance((CAMERA_DISTANCE * getResources().getDisplayMetrics().density));
        //Rotation
        childActivityParentContainer.setRotationX(rotationFactorX);
        childActivityParentContainer.setRotationY(rotationFactorY);
        childActivityParentContainer.setRotation(rotationFactor);
        //Scale
        childActivityParentContainer.setScaleX(scaleFactor);
        childActivityParentContainer.setScaleY(scaleFactor);
        //Shadow
        //fakeShadow.setVisibility(View.GONE);
        fakeShadow.setTranslationY(slideOffset * (drawerView.getWidth()-bottomMargin));
        fakeShadow.setCameraDistance((CAMERA_DISTANCE * getResources().getDisplayMetrics().density));
        fakeShadow.setScaleX(scaleFactorShadow);
        fakeShadow.setScaleY(scaleFactorShadow);
        fakeShadow.setRotationX(rotationFactorX);
        fakeShadow.setRotationY(rotationFactorY);
        fakeShadow.setRotation(rotationFactor);
        //disbale shadow, elevation and change fade color into transparent when nav drawer is open.
        mDrawerLayout.setDrawerShadow(R.drawable.no_navdrawer_shadow, GravityCompat.START);
        mDrawerLayout.setDrawerElevation(0f);
        mDrawerLayout.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    /**
     * Methods to animate Menu
     *
     * to be mixed with the Drawer & FrameLayout animation
     */
    //Menu seems fixed -- good mixed with BelowNavDrawer
    protected void fixedMenu(View drawerView, float slideOffset){
        float InverseNavDrawerOffset=((slideOffset-1)*-1)*(drawerView.getWidth());
        //navigationViewMenu.setAlpha(1+(slideOffset-1));
        navigationViewMenu.setTranslationX(InverseNavDrawerOffset);
    }

    //Menu moves on drag
    protected void moveInverseMenu(View drawerView, float slideOffset){
        float InverseNavDrawerOffsetDouble=((slideOffset-1)*-1)*(drawerView.getWidth()*2);
        navigationViewMenu.setAlpha(1+(slideOffset-1));

        //Option 1 : move all layout according to slideoffset : here I Move it at the inverse of slide.
        navigationViewMenu.setTranslationX(InverseNavDrawerOffsetDouble);

        //Option 2 : move each item separatly according to slideoffset
        /*int firstVisibleItemPosition = navigationMenuLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = navigationMenuLayoutManager.findLastVisibleItemPosition();
        for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
            NavMenuViewHolder holderForAdapterPosition = (NavMenuViewHolder) navigationViewMenu.findViewHolderForAdapterPosition(i);
            View itemView = holderForAdapterPosition.itemView;
            float translateItem = ((slideOffset-1)*-1)*(drawerView.getWidth()*(i+2)*(0.6f));//add +2 to i to make first item move too
            itemView.setTranslationX(translateItem);
        }*/

    }

    protected void moveMenu(View drawerView, float slideOffset){

    }

    public void fullNavDrawer() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = metrics.widthPixels;
        navigationView.setLayoutParams(params);
    }


}
