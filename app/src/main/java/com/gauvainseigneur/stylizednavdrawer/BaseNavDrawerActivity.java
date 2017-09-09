package com.gauvainseigneur.stylizednavdrawer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gauvainseigneur.stylizednavdrawer.ComplexAnimActivity;
import com.gauvainseigneur.stylizednavdrawer.Interpolators.CircInOut;
import com.gauvainseigneur.stylizednavdrawer.Interpolators.ExpoIn;
import com.gauvainseigneur.stylizednavdrawer.Interpolators.ExpoOut;
import com.gauvainseigneur.stylizednavdrawer.Interpolators.QuadInOut;
import com.gauvainseigneur.stylizednavdrawer.Interpolators.QuintInOut;
import com.gauvainseigneur.stylizednavdrawer.MainActivity;
import com.gauvainseigneur.stylizednavdrawer.PushMenuActivity;
import com.gauvainseigneur.stylizednavdrawer.R;
import com.gauvainseigneur.stylizednavdrawer.menulist.NavMenuItemObject;
import com.gauvainseigneur.stylizednavdrawer.menulist.NavMenuRVAdapter;
import com.gauvainseigneur.stylizednavdrawer.menulist.NavMenuViewHolder;
import com.gauvainseigneur.stylizednavdrawer.utils.CustomViewOutlineProvider;
import com.gauvainseigneur.stylizednavdrawer.utils.ItemClickSupport;
import com.gauvainseigneur.stylizednavdrawer.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.TRANSLATION_X;
import static android.view.View.TRANSLATION_Y;

public class BaseNavDrawerActivity extends AppCompatActivity {

    public DrawerLayout mDrawerLayout;
    public NavigationView navigationView;
    public View closeNavDrawerView;
    public RelativeLayout childActivityParentContainer;
    public FrameLayout childActivityContainer;
    public View fakeShadow;
    public ActionBarDrawerToggle drawerToggle;
    public Toolbar toolbar;
    public boolean DrawerOpen;
    public int shadowTranslationOffset;
    public int activityPaddingLeft;
    LinearLayoutManager navigationMenuLayoutManager;
    public RecyclerView navigationViewMenu;
    public List<NavMenuItemObject> rowListNavMenuItem;
    public NavMenuRVAdapter mNavMenuRVAdapter;
    public Window window;
    boolean isOutAnimationRunning=false;
    boolean OutAnimationFinished=false;
    String animateOncreate="animateOnCarte";
    //Params for Complex animations
    private int OUT_COMPLEX_ANIMATION_DURATION=500;
    private int MOVE_Y =10;
    private float MIN_TARGET_SCALE= 0.40f;
    private float MAX_TARGET_SCALE= 1f;
    private float MIN_ROTATION_XYZ= 0f;
    private float MAX_ROTATION_X =45;
    private float MAX_ROTATION_Y = 5;
    private float MAX_ROTATION_Z = 65;
    private float CAMERA_DISTANCE = 6000f;

    private AnimatorSet complexAnimatorSet;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        shadowTranslationOffset = (int) Utils.convertDpToPixel(40,this);//todo -to delete after find better way
        activityPaddingLeft = (int) Utils.convertDpToPixel(32,this);//todo -to delete after find better way
        /*********************************************
         * Root Layout for extend activities
         ********************************************/
        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base_nav_drawer, null);
        /***************************************************************
         * RRelativeLayout which include FrameLayout for child's view
         **************************************************************/
        childActivityParentContainer = (RelativeLayout) mDrawerLayout.findViewById(R.id.activity_parent_layout);
        /***************************************************************
         * FrameLayout to inflate the child's view.
         **************************************************************/
        childActivityContainer = (FrameLayout) mDrawerLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, childActivityContainer, true);
        //Set content view -- we pass the view in child's actvity
        super.setContentView(mDrawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        fakeShadow = findViewById(R.id.fakeShadow);
        // mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.base_nav_coordinator_layout);
        setSupportActionBar(toolbar);
        setUpNavigationDrawer(toolbar);
    }

    /*********************************************
     * set up navigationDrawer
     ********************************************/
    public void setUpNavigationDrawer(Toolbar toolbar) {
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
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                DrawerOpen=false;
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                DrawerOpen=true;

            }
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_DRAGGING && isDrawerOpen() == false) {
                    //Start opening by swipe of user;
                }
                if (newState == DrawerLayout.STATE_DRAGGING && isDrawerOpen() == true) {
                    //Start closing by swipe of user;
                }
                if (newState == DrawerLayout.STATE_DRAGGING) {
                }
                if (newState == DrawerLayout.STATE_SETTLING) {
                }
                if (newState == DrawerLayout.STATE_IDLE && isDrawerOpen() == false) {
                    //closed
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                setNavDrawerSlideAnimation(drawerView, slideOffset);
            }

        };

        //Use a dedicated Icon to Open NavDrawer
        // comment or delete if you want to use traditional icon
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setHomeAsUpIndicator(R.drawable.ic_round_menu_white_24dp);
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    //Set menu list and manage menu item click
    private void setUpNavMenu() {
        navigationViewMenu = (RecyclerView) findViewById(R.id.rv_navdrawer_menu);
        closeNavDrawerView = findViewById(R.id.closeNavDrawerView);
        closeNavDrawerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeNavDrawer();
            }
        });
        //navigationViewMenu.setNestedScrollingEnabled(false);
        navigationMenuLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        navigationViewMenu.setLayoutManager(navigationMenuLayoutManager);
        rowListNavMenuItem = setNavMenuList();
        mNavMenuRVAdapter = new NavMenuRVAdapter(this, rowListNavMenuItem);
        navigationViewMenu.setAdapter(mNavMenuRVAdapter);

        ItemClickSupport.addTo(navigationViewMenu).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                switch(position) {
                    case 0:
                        startOutComplexAnimation(OUT_COMPLEX_ANIMATION_DURATION,MainActivity.class);
                        break;
                    case 1:
                        intentToActivity(PushMenuActivity.class);
                        break;
                    case 2:
                        startOutComplexAnimation(OUT_COMPLEX_ANIMATION_DURATION,ComplexAnimActivity.class);
                        break;
                    case 3:
                        intentToActivity(ComplexAnimActivity.class);
                        break;


                }
            }
        });
    }

    //Dummy list of item for the menu
    private List<NavMenuItemObject> setNavMenuList(){
        List<NavMenuItemObject> allItems = new ArrayList<NavMenuItemObject>();
        allItems.add(new NavMenuItemObject(Color.parseColor(getString(R.string.nav_drawer_item_1)),
                R.drawable.ic_warning_black_24dp, "Add a mean of payment"));
        allItems.add(new NavMenuItemObject(Color.parseColor(getString(R.string.nav_drawer_item_2)),
                R.drawable.ic_warning_black_24dp, "Find the best offer for your holiday"));
        allItems.add(new NavMenuItemObject(Color.parseColor(getString(R.string.nav_drawer_item_3)),
                R.drawable.ic_warning_black_24dp, "Need help ?"));
        allItems.add(new NavMenuItemObject(Color.parseColor(getString(R.string.nav_drawer_item_4)),
                R.drawable.ic_warning_black_24dp, "help us, give feedback"));
        allItems.add(new NavMenuItemObject(Color.parseColor(getString(R.string.nav_drawer_item_5)),
                R.drawable.ic_warning_black_24dp, "Contact"));
        return allItems;
    }

    /*********************************************
     * Manage Intent to go to aother activity
     ********************************************/
    //intent to start activity on men item click
    private void intentToActivity (Class activityClass) {
        Intent goToActivityintent = new Intent(BaseNavDrawerActivity.this, activityClass);
        BaseNavDrawerActivity.this.startActivity(goToActivityintent);
        ActivityOptions.makeSceneTransitionAnimation(BaseNavDrawerActivity.this).toBundle();
    }

    private void intentToActivityWithoutAnimation (Class activityClass) {
        Intent ii = new Intent(this, activityClass);
        ii.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        String strName = null;
        ii.putExtra(animateOncreate, strName);
        startActivityForResult(ii, 0);
        overridePendingTransition(0,0);
    }

    //check if activity is currently running
    private Boolean isActivityRunning(Class activityClass) {
        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }

        return false;
    }

    /**************************************************************************
     * Methods to animate DrawerLayout & FrameLayout according to slide offset
     **************************************************************************/
    //NavigationDrawer seems to push the actvity activity
    protected void pushNavDrawer(View drawerView, float slideOffset) {
        childActivityParentContainer.setTranslationX(slideOffset * drawerView.getWidth());
        noShadowNavDrawer();
    }

    //NavigationDrawer seems below activity thanks to a shadow around the activity container
    protected void pushScaleNavDrawer(View drawerView, float slideOffset){
        float min = 0.8f;
        float max = 1.0f;
        float minShadow = 0.83f;
        float scaleFactor = (max - ((max - min) * slideOffset));
        float scaleFactorShadow = (max - ((max - minShadow) * slideOffset));
        //View moving
        childActivityParentContainer.setTranslationX(slideOffset*
                (drawerView.getWidth()-activityPaddingLeft));
        childActivityParentContainer.setScaleX(scaleFactor);
        childActivityParentContainer.setScaleY(scaleFactor);
        //Shadow
        fakeShadow.setVisibility(View.VISIBLE);
        fakeShadow.setTranslationX(slideOffset * (drawerView.getWidth()-shadowTranslationOffset));
        fakeShadow.setScaleX(scaleFactorShadow);
        fakeShadow.setScaleY(scaleFactorShadow);
        noShadowNavDrawer();
    }

    //NavigationDrawer move with complex animation
    protected void complexNavDrawerAnim(View drawerView, float slideOffset) {
        complexAnimateView(childActivityParentContainer, slideOffset, 0);
        complexAnimateView(fakeShadow, slideOffset, 0.045f);//todo - some issues on drag view on startIntroAnim(target) with
        noShadowNavDrawer();
    }

    private void complexAnimateView(View view, float slideOffset, float AdditionalScaleValue){
        int bottomMargin = (int) Utils.convertDpToPixel((128), this);
        float bottomOfScreen = getResources().getDisplayMetrics().heightPixels;
        float scaleFactorX = scaleOnSlide(MIN_TARGET_SCALE, AdditionalScaleValue*(1.8f),slideOffset);
        float scaleFactorY = scaleOnSlide(MIN_TARGET_SCALE, AdditionalScaleValue,slideOffset);
        float rotationFactorX = rotateOnSlide(MAX_ROTATION_X,slideOffset);
        float rotationFactorY = rotateOnSlide(MAX_ROTATION_Y,slideOffset);
        float rotationZ = rotateOnSlide(MAX_ROTATION_Z,slideOffset);
        //Translation
        view.setTranslationY((slideOffset * (bottomOfScreen-(bottomMargin))*MIN_TARGET_SCALE));
        view.setCameraDistance((CAMERA_DISTANCE * getResources().getDisplayMetrics().density));
        //Rotation
        view.setRotationX(rotationFactorX);
        view.setRotationY(rotationFactorY);
        view.setRotation(rotationZ);
        //Scale
        view.setScaleX(scaleFactorX);
        view.setScaleY(scaleFactorY);
        //OutlineProvider
        float cornerOnSlide = rotateOnSlide(32, slideOffset);
        view.setOutlineProvider(new CustomViewOutlineProvider((int)cornerOnSlide));
        view.setClipToOutline(true);

    }

    private float rotateOnSlide(float xyzValue, float slideOffset) {
        float rotateFloat = (MIN_ROTATION_XYZ - ((MIN_ROTATION_XYZ - xyzValue)) * slideOffset);
        return rotateFloat;
    }

    private float scaleOnSlide(float minTargetScale, float AdditionalScaleValue, float slideOffset){
        float scaleFloat = (MAX_TARGET_SCALE - ((MAX_TARGET_SCALE - (minTargetScale +
                AdditionalScaleValue)) * slideOffset));
        return scaleFloat;
    }

    /**************************************************************************
     * Methods to animate Menu according to slide offset
     *
     * To be mixed with the Drawer & FrameLayout animation
     **************************************************************************/
    //Menu seems fixed -- cool effect when mixed with pushScaleNavDrawer()
    protected void fixedMenu(View drawerView, float slideOffset){
        float InverseNavDrawerOffset=((slideOffset-1)*-1)*(drawerView.getWidth());
        //navigationViewMenu.setAlpha(1+(slideOffset-1));
        navigationViewMenu.setTranslationX(InverseNavDrawerOffset);
    }

    //Menu moves as inverse the swipe direction of drawerlayout
    protected void moveInverseMenu(View drawerView, float slideOffset){
        float InverseNavDrawerOffsetDouble=((slideOffset-1)*-1)*(drawerView.getWidth()*2);
        navigationViewMenu.setAlpha(1+(slideOffset-1));
        //move all layout according to slideoffset : here I Move it at the inverse of slide.
        navigationViewMenu.setTranslationX(InverseNavDrawerOffsetDouble);

    }

    //Menu Item moves separately as inverse the swipe direction of drawerlayout
    protected void moveInverseItemMenu(View drawerView, float slideOffset){
        navigationViewMenu.setAlpha(1+(slideOffset-1));

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

    //Menu moves as the swipe direction of drawerlayout
    protected void moveMenu(View drawerView, float slideOffset){
    }

    /**************************************************************************
     * Todo - for demo purpose only
     * Select which animation you want to set uo when navigation drawer is sliding
     *
     * Method to be Overidden in child activities
     **************************************************************************/
    protected void setNavDrawerSlideAnimation(View drawerView, float slideOffset){
    }

    /**************************************************************************
     * Manage Navigation Drawer State
     **************************************************************************/
    //Make navigation fullwidth
    protected void fullNavDrawer() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams)
                navigationView.getLayoutParams();
        params.width = metrics.widthPixels;
        navigationView.setLayoutParams(params);
    }

    //Remove navigationdrawer shadow & scrim
    protected void noShadowNavDrawer() {
        //disable shadow, elevation and change fade color into transparent when nav drawer is open.
        mDrawerLayout.setDrawerShadow(R.drawable.no_navdrawer_shadow, GravityCompat.START);
        mDrawerLayout.setDrawerElevation(0f);
        mDrawerLayout.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    //close navigation drawer
    protected void closeNavDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    /**************************************************************************
     * Manage navigation drawer state trough the activity life cycle
     **************************************************************************/
    public boolean isDrawerOpen() {
        return DrawerOpen;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mDrawerLayout.closeDrawer(Gravity.START, false);
        //todo - hotfix to manage back with complexAnimatorSet
        if (complexAnimatorSet!=null) {
            for (final Animator anim : complexAnimatorSet.getChildAnimations()) {
                ((ObjectAnimator) anim).reverse();
                anim.end();
                anim.removeAllListeners();
                //open and close navdrawer to reset totaly state
                mDrawerLayout.openDrawer(Gravity.START, false);
                mDrawerLayout.closeDrawer(Gravity.START, false);
            }
        }
    }


    /**************************************************************************
     * Example of additional complex animations when whe open new activity
     * from menu
     *
     * To be used in addition of complexNavDrawerAnim
     **************************************************************************/
    public void startIntroAnim(View target) {
        inComplexAnimation(target,MIN_TARGET_SCALE,0);
    }

    private ObjectAnimator inComplexAnimation(final View target, final float targetScale,
                                              final float moveY) {
        float TARGET_ROTATION = 45;
        float TARGET_ROTATION_X = 45;

        target.setCameraDistance(CAMERA_DISTANCE * target.getResources().getDisplayMetrics().density);

        ObjectAnimator translationY2 = ObjectAnimator.ofFloat(target, TRANSLATION_Y,
                target.getResources().getDisplayMetrics().heightPixels,
                -moveY * target.getResources().getDisplayMetrics().density).setDuration(800);
        translationY2.setInterpolator(new ExpoOut());
        translationY2.setStartDelay(0);
        translationY2.start();
        target.setTranslationY(target.getResources().getDisplayMetrics().heightPixels);

        ObjectAnimator translationX2 = ObjectAnimator.ofFloat(target, TRANSLATION_X,
                -target.getResources().getDisplayMetrics().widthPixels, 0).setDuration(500);
        translationX2.setInterpolator(new ExpoOut());
        translationX2.setStartDelay(0 );
        translationX2.start();
        target.setTranslationX(-target.getResources().getDisplayMetrics().widthPixels);

        ObjectAnimator rotationX = ObjectAnimator.ofFloat(target, View.ROTATION_X,
                TARGET_ROTATION_X, 0).setDuration(700);
        rotationX.setInterpolator(new QuintInOut());
        rotationX.setStartDelay(0 + 300);
        rotationX.start();
        target.setRotationX(TARGET_ROTATION_X);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, targetScale,
                target.getScaleX()).setDuration(700);
        scaleX.setInterpolator(new CircInOut());
        scaleX.setStartDelay(0 + 300);
        scaleX.start();
        target.setScaleX(targetScale);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, targetScale,
                target.getScaleY()).setDuration(700);
        scaleY.setInterpolator(new CircInOut());
        scaleY.setStartDelay(0 + 300);
        scaleY.start();
        target.setScaleY(targetScale);

        //Rotation
        ObjectAnimator rotation = ObjectAnimator.ofFloat(target, View.ROTATION,
                TARGET_ROTATION, 0).setDuration(400);
        rotation.setInterpolator(new QuadInOut());
        rotation.setStartDelay(300);
        rotation.start();
        target.setRotation(TARGET_ROTATION);

        return scaleY;

    }

    private void startOutComplexAnimation(int duration, final Class activityClass){
        complexAnimatorSet = new AnimatorSet();
        complexAnimatorSet.playTogether(slideYOut(childActivityParentContainer, MOVE_Y,0),
                slideXOut(childActivityParentContainer, MOVE_Y, 0),
                slideYOut(fakeShadow, MOVE_Y,0),
                slideXOut(fakeShadow, MOVE_Y, 0),
                slideXOut(navigationViewMenu, -MOVE_Y, 0));
        complexAnimatorSet.setDuration(duration);
        complexAnimatorSet.start();

        complexAnimatorSet.addListener(new AnimatorSet.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                intentToActivityWithoutAnimation(activityClass);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });

    }

    private ObjectAnimator slideXOut(View target,int moveY, int delay) {
        float x = target.getX();
        ObjectAnimator translateX = ObjectAnimator.ofFloat(target, TRANSLATION_X, x,
                moveY * target.getResources().getDisplayMetrics().heightPixels);
        translateX.setDuration(OUT_COMPLEX_ANIMATION_DURATION);;
        translateX.setDuration(OUT_COMPLEX_ANIMATION_DURATION);
        translateX.setInterpolator(new ExpoIn());
        translateX.setStartDelay(delay);
        return translateX;
    }

    private ObjectAnimator slideYOut(View target, float moveY, int delay) {
        float y = target.getY();

        ObjectAnimator translateY = ObjectAnimator.ofFloat(target, TRANSLATION_Y, y,
                -moveY * target.getResources().getDisplayMetrics().widthPixels);
        translateY.setDuration(OUT_COMPLEX_ANIMATION_DURATION);
        translateY.setInterpolator(new ExpoIn());
        translateY.setStartDelay(delay);
        return translateY;
    }

}
