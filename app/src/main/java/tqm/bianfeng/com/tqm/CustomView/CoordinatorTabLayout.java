package tqm.bianfeng.com.tqm.CustomView;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import tqm.bianfeng.com.tqm.Institutions.listener.AppBarStateChangeListener;
import tqm.bianfeng.com.tqm.Institutions.listener.LoadHeaderImagesListener;
import tqm.bianfeng.com.tqm.R;

/**
 * @author hugeterry(http://hugeterry.cn)
 */

public class CoordinatorTabLayout extends CoordinatorLayout {

    private int[] mImageArray, mColorArray;
    private AppBarLayout appBarLayout;
    private Context mContext;
    private Toolbar mToolbar;
    private ActionBar mActionbar;
    private TabLayout mTabLayout;
    private ImageView mImageView;
    private LinearLayout lin_view;
    private View mHeaderView;
    private boolean isOpen=true;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private LoadHeaderImagesListener mLoadHeaderImagesListener;

    public interface Linsener{
        public void changActivity(int id);
    }
    Linsener mLinsener;

    boolean isHaveMenu=false;
    public void setmLinsener(Linsener Linsener) {
        this.mLinsener = Linsener;
        mToolbar.inflateMenu(R.menu.error_report);
        isHaveMenu=true;
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.correct_report_light||item.getItemId()==R.id.correct_report_dark){
                    mLinsener.changActivity(R.id.correct_report);
                }
                return false;
            }
        });
    }

    public CoordinatorTabLayout(Context context) {
        super(context);
        mContext = context;
    }

    public CoordinatorTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (!isInEditMode()) {
            initView(context);
            initWidget(context, attrs);
        }
    }

    public CoordinatorTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        if (!isInEditMode()) {
            initView(context);
            initWidget(context, attrs);
        }
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate( R.layout.myview_coordinatortablayout, this, true);
        initToolbar();
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById( R.id.collapsingtoolbarlayout);
        mTabLayout = (TabLayout) findViewById( R.id.tabLayout);

        //mImageView = (ImageView) findViewById( R.id.imageview);
        //mHeaderView=(View) findViewById(R.id.view);
        lin_view=(LinearLayout) findViewById(R.id.lin_view);
        appBarLayout=(AppBarLayout)findViewById(R.id.AppBarLayout);
        ViewTreeObserver vto = appBarLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                appBarLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Log.i("gqf","appBarLayout"+appBarLayout.getHeight());
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                //Log.d("STATE", state.name());
                if( state == State.EXPANDED ) {
                    //展开状态
                    if(!isOpen) {
                        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
                        //mTabLayout.setBackgroundColor(getResources().getColor(R.color.whitesmoke));
                        mTabLayout.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.colorPrimary));
                        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
                        //mActionbar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_dark);
                        mToolbar.setNavigationIcon(R.drawable.ic_back_arrow_dark);
                        isOpen=true;
                        if(isHaveMenu) {
                            mToolbar.getMenu().getItem(0).setVisible(false);
                            mToolbar.getMenu().getItem(1).setVisible(true);
                        }
                    }
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    if(isOpen) {
                        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
                        //mTabLayout.setBackgroundColor(getResources().getColor(R.color.max_transparent));
                        mTabLayout.setTabTextColors(getResources().getColor(R.color.whitesmoke), getResources().getColor(R.color.white));
                        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
                        //mActionbar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_write);
                        mToolbar.setNavigationIcon(R.drawable.ic_back_arrow_write);
                        isOpen=false;
                        if(isHaveMenu){
                            mToolbar.getMenu().getItem(0).setVisible(true);
                            mToolbar.getMenu().getItem(1).setVisible(false);
                        }

                    }
                }
                if(myLinsener!=null) {
                    myLinsener.openOrClose(isOpen,mTabLayout.getSelectedTabPosition());
                }
                setTabBgColor(isOpen);
            }
        });

    }
    ValueAnimator animator;
    private void setTabBgColor(boolean isOpen){
        if(isOpen) {
            //透明变灰
            animator = ValueAnimator.ofInt(0x00000000, 0xfff4f4f4);
        }else{
            if(mColorArray!=null) {
                animator = ValueAnimator.ofInt(mColorArray[mTabLayout.getSelectedTabPosition()], 0x00000000);
            }
        }
        if(animator!=null) {
            animator.setEvaluator(new ArgbEvaluator());
            animator.setDuration(500);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int curValue = (int) animation.getAnimatedValue();

                    mTabLayout.setBackgroundColor(curValue);
                    mToolbar.setBackgroundColor(curValue);
                }
            });
            animator.start();
        }
    }

    private void initWidget(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs
                ,  R.styleable.CoordinatorTabLayout);

        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute( R.attr.colorPrimary, typedValue, true);
        int contentScrimColor = typedArray.getColor(
                 R.styleable.CoordinatorTabLayout_contentScrim, typedValue.data);
        mCollapsingToolbarLayout.setContentScrimColor(contentScrimColor);

//        int tabIndicatorColor = typedArray.getColor( R.styleable.CoordinatorTabLayout_tabIndicatorColor, Color.GRAY);
//        mTabLayout.setSelectedTabIndicatorColor(tabIndicatorColor);
//
//        int tabTextColor = typedArray.getColor( R.styleable.CoordinatorTabLayout_tabTextColor, Color.WHITE);
//        mTabLayout.setTabTextColors(ColorStateList.valueOf(tabTextColor));
        typedArray.recycle();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById( R.id.toolbar);
        //((AppCompatActivity) mContext).setSupportActionBar(mToolbar);
        //mActionbar = ((AppCompatActivity) mContext).getSupportActionBar();

    }

    /**
     * 设置Toolbar标题
     *
     * @param title 标题
     * @return
     */
    public  CoordinatorTabLayout setTitle(String title) {
//        if (mActionbar != null) {
//            mActionbar.setTitle(title);
//        }
        if(mToolbar!=null){
            mToolbar.setTitle(title);
        }
        return this;
    }

    public void setTabInColor(int color){
        mTabLayout.setSelectedTabIndicatorColor(color);
        mTabLayout.setSelectedTabIndicatorHeight(0);
    }
    /**
     * 设置Toolbar显示返回按钮及标题
     *
     * @param canBack 是否返回
     * @return
     */
    public  CoordinatorTabLayout setBackEnable(Boolean canBack) {
//        if (canBack && mActionbar != null) {
//            mActionbar.setDisplayHomeAsUpEnabled(true);
//            mActionbar.setHomeAsUpIndicator( R.drawable.ic_back_arrow_dark);
//        }
        if(canBack&&mToolbar!=null){
            mToolbar.setNavigationIcon(R.drawable.ic_back_arrow_dark);
            mToolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(myLinsener!=null){
                        myLinsener.onBack();
                    }
                }
            });
        }
        return this;
    }

    /**
     * 设置每个tab对应的头部图片
     *
     * @param imageArray 图片数组
     * @return
     */
    public  CoordinatorTabLayout setImageArray(@NonNull int[] imageArray) {
        mImageArray = imageArray;
        setupTabLayout();
        return this;
    }

    /**
     * 设置每个tab对应的头部照片和ContentScrimColor
     *
     * @param imageArray 图片数组
     * @param colorArray ContentScrimColor数组
     * @return
     */
    public  CoordinatorTabLayout setImageArray(@NonNull int[] imageArray, @NonNull int[] colorArray) {
        mImageArray = imageArray;
        mColorArray = colorArray;
        setupTabLayout();
        return this;
    }

    /**
     * 设置每个tab对应的ContentScrimColor
     *
     * @param colorArray 图片数组
     * @return
     */
    public  CoordinatorTabLayout setContentScrimColorArray(@NonNull int[] colorArray) {
        mColorArray = colorArray;
        return this;
    }

    private void setupTabLayout() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //mImageView.startAnimation(AnimationUtils.loadAnimation(mContext,  R.anim.anim_dismiss));
                //mHeaderView.startAnimation(AnimationUtils.loadAnimation(mContext,  R.anim.anim_dismiss));
                if (mLoadHeaderImagesListener == null) {
                    if (mImageArray != null) {
                        //mImageView.setImageResource(mImageArray[tab.getPosition()]);
                    }
                } else {
                    //mLoadHeaderImagesListener.loadHeaderImages(mImageView, tab);
                    mLoadHeaderImagesListener.loadHeaderImages(mHeaderView, tab);
                }
                if (mColorArray != null) {
                    mCollapsingToolbarLayout.setContentScrimColor(
                            ContextCompat.getColor(
                                    mContext, mColorArray[tab.getPosition()]));
                }
                if(myLinsener!=null){
                    myLinsener.openOrClose(isOpen,tab.getPosition());
                }
                //mImageView.setAnimation(AnimationUtils.loadAnimation(mContext,  R.anim.anim_show));
                //mHeaderView.setAnimation(AnimationUtils.loadAnimation(mContext,  R.anim.anim_show));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * 设置与该组件搭配的ViewPager
     *
     * @param viewPager 与TabLayout结合的ViewPager
     * @return
     */
    public  CoordinatorTabLayout setupWithViewPager(ViewPager viewPager) {
        mTabLayout.setupWithViewPager(viewPager);
        return this;
    }

    /**
     * 获取该组件中的ActionBar
     */
    public ActionBar getActionBar() {
        return mActionbar;
    }

    /**
     * 获取该组件中的TabLayout
     */
    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    /**
     * 获取该组件中的ImageView
     */
    public ImageView getImageView() {
        return mImageView;
    }

    /**
     * 设置LoadHeaderImagesListener
     *
     * @param loadHeaderImagesListener 设置LoadHeaderImagesListener
     * @return
     */
    public  CoordinatorTabLayout setLoadHeaderImagesListener(LoadHeaderImagesListener loadHeaderImagesListener) {
        mLoadHeaderImagesListener = loadHeaderImagesListener;
        setupTabLayout();
        return this;
    }

    public CoordinatorTabLayout setHeaderView(View view){
        mHeaderView=view;
        if(lin_view.getChildAt(0)!=null){
            lin_view.removeViewAt(0);
        }
        lin_view.addView(mHeaderView);

        return this;
    }
    public View getHeaderView() {
        return mHeaderView;
    }

    MyLinsener myLinsener;
    public void setMyLinsener(MyLinsener myLinsener){
        this.myLinsener=myLinsener;
    }
    public interface MyLinsener{
        public void openOrClose(boolean isOpen,int index);
        public void onBack();
    }
}