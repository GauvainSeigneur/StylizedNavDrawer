package com.gauvainseigneur.stylizednavdrawer;


import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gauvainseigneur.stylizednavdrawer.menulist.NavMenuItemObject;
import com.gauvainseigneur.stylizednavdrawer.menulist.NavMenuRVAdapter;
import com.gauvainseigneur.stylizednavdrawer.menulist.NavMenuViewHolder;
import com.gauvainseigneur.stylizednavdrawer.utils.ItemClickSupport;
import com.gauvainseigneur.stylizednavdrawer.utils.Utils;

import java.util.ArrayList;
import java.util.List;



public class BaseNavDrawerActivity extends AppCompatActivity {

    public NavigationView navigationView;
    public DrawerLayout fullLayout;
    public View headerView;
    public View fakeShadow;
    public CoordinatorLayout mCoordinatorLayout;
    //public AppBarLayout mAppBarLayout;
    public ActionBarDrawerToggle drawerToggle;
    public int selectedNavItemId;
    public int statusBarHeight;
    public boolean isStatusBarTransparent = true, isGrownAnim = false;
    public Toolbar toolbar;
    public boolean isToolbarLayout;
    public LinearLayout BottomSheetView;
    public TextView notification;
    public TabLayout tabs;
    public boolean layoutTypeScrolling=true;
    public ActionBarDrawerToggle mDrawerToggle;
    public boolean DrawerOpen;
    public int activityPadding;
    public int shadowTranslationOffset;
    public int activityPaddingLeft;
    public int ShadowMarginTop;
    public ImageView headerBackground;
    public int toolBarId;
    static int id = 1;
    int fID = 0;
    Context context;
    LinearLayoutManager navigationMenuLayoutManager;
    LinearLayout userShortCutView;

    public RecyclerView navigationViewMenu;
    public List<NavMenuItemObject> rowListNavMenuItem;
    public NavMenuRVAdapter mNavMenuRVAdapter;
    public Window window;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        shadowTranslationOffset = (int) Utils.convertDpToPixel(40,this);
        activityPaddingLeft = (int) Utils.convertDpToPixel(32,this);


        /**
         * getSharedprefrences
         */

        /**
         *
         * Root Layout for extend activities
         *
         * if you add isCollpasingToolbarLayout in OnCreate (before inflate view) in
         * the child activity you will have a Scrollingtoolbar,
         * if not, a simple layout
         */
        if (isToolbarLayout) {
            fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base_nav_drawer_no_toolbar, null);
        } else {
            fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base_nav_drawer_no_toolbar, null);
        }

        /**
         * {@link FrameLayout} to inflate the child's view.
         * We could also use a {@link android.view.ViewStub}
         */
        FrameLayout activityContainer = (FrameLayout) fullLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        //Set content view -- we pass the view in child's actvity
        super.setContentView(fullLayout);
        //make app fullscreen : recolor StatusBar
        //makeAppFullscreen();
        //setStatusBarTranslucent();
        //mAppBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout); //todo verifiy - check if it will make troub
        //toolbar = (Toolbar) findViewById(R.id.toolbar); //todo verifiy - check if it will make trouble
        //navigation view
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        //navigation view --> User shortcut
        //userShortCutView = (LinearLayout) findViewById(R.id.user_schort_cut);
        //fakeShadow behind CoordinatorLayout
        fakeShadow = findViewById(R.id.fakeShadow);
        //Coordinaotor Layout
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.base_nav_coordinator_layout);
        //mCoordinatorLayout.setFitsSystemWindows(false);
        setSupportActionBar(toolbar);
        //set NavigationView
        setUpNavView(toolbar);

    }

    @Override
    public void onResume() {
        super.onResume();
        //navdrawer close on Resume - in case we go back after click on nav drawer item...
        //fullLayout.closeDrawer(GravityCompat.START);
        fullLayout.closeDrawer(Gravity.LEFT, false);
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
        //navigationView.setNavigationItemSelectedListener(this);

        setUpNavMenu();
        if( useDrawerToggle()) {
            // use dedicated icon to open navdrawer on toolbar

            // use the hamburger menu
            drawerToggle = new ActionBarDrawerToggle(
                    this,                           // host Activity
                    fullLayout,                    // DrawerLayout object
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
                       //mAppBarLayout.setBackgroundColor(getResources().getColor(R.color.md_amber_600));
                       //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        //window.setStatusBarColor(getResources().getColor(R.color.md_red_500));
                    }
                    if (newState == DrawerLayout.STATE_SETTLING) {
                       // mAppBarLayout.setBackgroundColor(getResources().getColor(R.color.md_amber_600));
                       // window.setStatusBarColor(getResources().getColor(R.color.md_red_500));
                        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    }
                    if (newState == DrawerLayout.STATE_IDLE && isDrawerOpen() == false) {
                        // this where Drawer start is closed
                       // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        //fakehadow.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, slideOffset);
                    ComplexNavDrawerAnim(drawerView,slideOffset);
                    //BelowNavDrawer(drawerView,slideOffset);
                    //FixedNavDrawer(drawerView,slideOffset);
                    MoveNavDrawer(drawerView,slideOffset);

                }

            };

            //Uncomment if you want to use traditonnal hamburger Icon
           /* fullLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();*/

            //Use a dedicated Icon to Open NavDrawer
            drawerToggle.setDrawerIndicatorEnabled(false);
            drawerToggle.setHomeAsUpIndicator(R.drawable.ic_round_menu__black_24dp);
            drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fullLayout.openDrawer(GravityCompat.START);
                }
            });

            fullLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();


        } else if(getSupportActionBar() != null) {
            // Use home/back button instead of hamburger menu
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // if you want to use a drawable to set up back button :
            /*getSupportActionBar().setHomeAsUpIndicator(getResources()
                    .getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));*/
        }

        // drawer layout treats fitsSystemWindows specially so we have to handle insets ourselves
        fullLayout.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {

                // we place a background behind the status bar to combine with it's semi-transparent
                // color to get the desired appearance.  Set it's height to the status bar height

                /*View statusBarBackground = findViewById(R.id.status_bar_background);
                FrameLayout.LayoutParams lpStatus = (FrameLayout.LayoutParams)
                        statusBarBackground.getLayoutParams();
                lpStatus.height = insets.getSystemWindowInsetTop();
                statusBarBackground.setLayoutParams(lpStatus);*/


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



    protected void PushNavDrawer(View drawerView, float slideOffset){
        mCoordinatorLayout.setTranslationX(slideOffset * drawerView.getWidth());
        fullLayout.setDrawerShadow(R.drawable.no_navdrawer_shadow, GravityCompat.START);
        fullLayout.setDrawerElevation(0f);
        fullLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        fullLayout.requestLayout();
    }

    protected void ComplexNavDrawerAnim(View drawerView, float slideOffset){

        int bottomMargin = (int) Utils.convertDpToPixel(128,this);

        final float minXYZ = 0;
        final float MAX_ROTATION_X = 45; //90 //45
        final float MAX_ROTATION_Y = 5; // 90
        final float MAX_ROTATION_Z = 65; //360

        final float CAMERA_DISTANCE = 6000f;

        //mCoordinatorLayout.setRotationX(-MAX_ROTATION_X + (MAX_ROTATION_X * 2f) * ((float) progress / (float) seekBar.getMax()));
        //mCoordinatorLayout.setRotationY(-MAX_ROTATION_Y + (MAX_ROTATION_Y * 2f) * ((float) progress / (float) seekBar.getMax()))
        //mCoordinatorLayout.setRotation(-MAX_ROTATION_Z * ((float) progress / (float) seekBar.getMax()));

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
        mCoordinatorLayout.setTranslationY(slideOffset * (drawerView.getWidth()-bottomMargin));
        mCoordinatorLayout.setCameraDistance((CAMERA_DISTANCE * getResources().getDisplayMetrics().density));
        //Rotation
        mCoordinatorLayout.setRotationX(rotationFactorX);
        mCoordinatorLayout.setRotationY(rotationFactorY);
        mCoordinatorLayout.setRotation(rotationFactor);
        //Scale
        mCoordinatorLayout.setScaleX(scaleFactor);
        mCoordinatorLayout.setScaleY(scaleFactor);
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
        fullLayout.setDrawerShadow(R.drawable.no_navdrawer_shadow, GravityCompat.START);
        fullLayout.setDrawerElevation(0f);
        fullLayout.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    protected void BelowNavDrawer(View drawerView, float slideOffset){
        float min = 0.8f;
        float max = 1.0f;
        float minShadow = 0.83f;
        float scaleFactor = (max - ((max - min) * slideOffset));
        float scaleFactorShadow = (max - ((max - minShadow) * slideOffset));
        //View moving
        mCoordinatorLayout.setTranslationX(slideOffset * (drawerView.getWidth()-activityPaddingLeft));
        mCoordinatorLayout.setScaleX(scaleFactor);
        mCoordinatorLayout.setScaleY(scaleFactor);
        //Shadow
        fakeShadow.setTranslationX(slideOffset * (drawerView.getWidth()-shadowTranslationOffset));
        fakeShadow.setScaleX(scaleFactorShadow);
        fakeShadow.setScaleY(scaleFactorShadow);
        //disbale shadow, elevation and change fade color into transparent when nav drawer is open.
        fullLayout.setDrawerShadow(R.drawable.no_navdrawer_shadow, GravityCompat.START);
        fullLayout.setDrawerElevation(0f);
        fullLayout.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    protected void FixedNavDrawer(View drawerView, float slideOffset){
        float InverseNavDrawerOffset=((slideOffset-1)*-1)*(drawerView.getWidth());
        //navigationViewMenu.setAlpha(1+(slideOffset-1));
        //userShortCutView.setTranslationX(InverseNavDrawerOffset);
        //use this make some UI issue (elevation) so we call child view to perform the animation instead of it:
        //navigationView.setTranslationX(InverseNavDrawerOffset);
        navigationViewMenu.setTranslationX(InverseNavDrawerOffset);
    }

    protected void MoveNavDrawer(View drawerView, float slideOffset){
        float InverseNavDrawerOffsetDouble=((slideOffset-1)*-1)*(drawerView.getWidth()*2);
        navigationViewMenu.setAlpha(1+(slideOffset-1));

        //Option 1 : move all layout according to slideoffset : here I Move it at the inverse of slide.
        //navigationViewMenu.setTranslationX(InverseNavDrawerOffsetDouble);

        //Option 2 : move each item separatly according to slideoffset
        int firstVisibleItemPosition = navigationMenuLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = navigationMenuLayoutManager.findLastVisibleItemPosition();
        for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
            NavMenuViewHolder holderForAdapterPosition = (NavMenuViewHolder) navigationViewMenu.findViewHolderForAdapterPosition(i);
            View itemView = holderForAdapterPosition.itemView;
            float translateItem = ((slideOffset-1)*-1)*(drawerView.getWidth()*(i+2)*(0.6f));//add +2 to i to make first item move too
            itemView.setTranslationX(translateItem);
        }

    }

    //use inside activity that you want to have a collapsing ToolbarLayout
    public boolean isToolbarLayout() {
        return isToolbarLayout=true;
    }

    public boolean isDrawerOpen() {
        return DrawerOpen;
    }

    //if you don't want to see navdrawer visible and add an arrow instead of burger icon
    public void Hidemenu() {
        fullLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //disable
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
        //Set Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void makeAppFullscreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void rootFitsSystemWindows(boolean fitSystemWindows){
        View view = findViewById(R.id.base_nav_coordinator_layout);
        if (fitSystemWindows==false) {
            view.setFitsSystemWindows(false);
            //view.setPadding(0, 0, 0, 0);
        }
        else {
            view.setFitsSystemWindows(true);
        }

    }

    public void windowNoLimit () {
        //this value needs to be combined with style to works fine...
        fullLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

    }

    @Override
    public void onBackPressed() {
        if (fullLayout.isDrawerOpen(GravityCompat.START)) {
            fullLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
