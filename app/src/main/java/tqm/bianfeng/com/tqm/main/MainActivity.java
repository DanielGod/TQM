package tqm.bianfeng.com.tqm.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.UserFragment;

public class MainActivity extends AppCompatActivity implements UserFragment.mListener{
    private static final String HOME_TAG = "home_flag";
    private static final String LAWHELP_TAG = "lawhelp_flag";
    private static final String INSTITUTIONSIN_TAG = "institutionsin_flag";
    private static final String CATHOME_TAG = "cathome_flag";
    private static final int CONTENT_HOME = 1;
    private static final int CONTENT_LAWHELP = 2;
    private static final int CONTENT_INSTITUTIONSIN = 3;
    private static final int CONTENT_CATHOME = 4;
    @BindView(R.id.bottomBar)
    BottomNavigationBar bottomBar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setContent(CONTENT_HOME);
        //设置底部栏
        initBottomBar();

    }

    /**
     * 设置底部栏
     */

    private void initBottomBar() {
        bottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomBar
                .setActiveColor(R.color.colorPrimary)
                .setInActiveColor(R.color.bottom_img)
                .setBarBackgroundColor(R.color.whitesmoke);
        bottomBar.addItem(new BottomNavigationItem(R.drawable.home, R.string.home))
                .addItem(new BottomNavigationItem(R.drawable.law_help,  R.string.lawHelp))
                .addItem(new BottomNavigationItem(R.drawable.institutions_in, R.string.institutionsIn))
                .addItem(new BottomNavigationItem(R.drawable.cat_home,  R.string.catHome))
                .initialise();

        bottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        setContent(CONTENT_HOME);
                        break;
                    case 1:
                        setContent(CONTENT_LAWHELP);

                        break;
                    case 2:
                        setContent(CONTENT_INSTITUTIONSIN);
                        break;
                    case 3:
                        setContent(CONTENT_CATHOME);
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    /**
     * 页面切换
     *
     * @param contentHome
     */
    HomeFragment homeFragment;
    LawHelpFragment lawHelpFragment;
    InstitutionsInFragment institutionsInFragment;
    UserFragment userFragemnt;

    public void setContent(int contentHome) {
        switch (contentHome) {
            case CONTENT_HOME:
                String home_str = getResources().getString(R.string.home);
                toolbarTitle.setText(home_str);
                homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HOME_TAG);
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance();
                }
                setFragment(homeFragment, HOME_TAG);
                break;
            case CONTENT_LAWHELP:
                String lawHelp_str = getResources().getString(R.string.lawHelp);
                toolbarTitle.setText(lawHelp_str);
                lawHelpFragment = (LawHelpFragment) getSupportFragmentManager().findFragmentByTag(LAWHELP_TAG);
                if (lawHelpFragment == null) {
                    lawHelpFragment = LawHelpFragment.newInstance();
                }
                setFragment(lawHelpFragment, LAWHELP_TAG);
                break;
            case CONTENT_INSTITUTIONSIN:
                String institutionsIn_str = getResources().getString(R.string.institutionsIn);
                toolbarTitle.setText(institutionsIn_str);
                institutionsInFragment = (InstitutionsInFragment) getSupportFragmentManager().findFragmentByTag(INSTITUTIONSIN_TAG);
                if (institutionsInFragment == null) {
                    institutionsInFragment = InstitutionsInFragment.newInstance();
                }
                setFragment(institutionsInFragment, INSTITUTIONSIN_TAG);
                break;
            case CONTENT_CATHOME:
                String catHome_str = getResources().getString(R.string.catHome);
                toolbarTitle.setText(catHome_str);
                userFragemnt = (UserFragment) getSupportFragmentManager().findFragmentByTag(CATHOME_TAG);
                if (userFragemnt == null) {
                    userFragemnt = UserFragment.newInstance("猫舍");
                }
                setFragment(userFragemnt, CATHOME_TAG);
                break;
        }

    }
    /**
     * 设置fragment
     *
     * @param fragment
     */
    @DebugLog
    private void setFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void changeActivity(
            Class activityClass){
        startActivity(new Intent(MainActivity.this,activityClass));
    }
}
