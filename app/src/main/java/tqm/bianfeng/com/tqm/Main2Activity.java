package tqm.bianfeng.com.tqm;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.demo.DemoFragemnt;

/**
 * Created by johe on 2017/4/25.
 */

public class Main2Activity extends BaseActivity implements DemoFragemnt.mListener{

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    DemoFragemnt demoFragemnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark));
        toolbarTitle.setText("首页");
         demoFragemnt=new DemoFragemnt();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, demoFragemnt).commit();

    }
    public void setToolBarColorBg(int a){
        toolbar.getBackground().setAlpha(a);
        //StatusBarUtil.setColor(this,2323232);
        //StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark),a);

        int sysBarColor= Color.argb(a,255,111,32);
        //StatusBarUtil.setColor(this,sysBarColor);
        //StatusBarUtil.setColorForSwipeBack(this,getResources().getColor(R.color.colorPrimaryDark),a);
        //StatusBarUtil.setColorNoTranslucent(this,sysBarColor);
        //StatusBarUtil.hideFakeStatusBarView(this);
        //StatusBarUtil.setColorForSwipeBack(this,getResources().getColor(R.color.blue),a);
        //StatusBarUtil.setTranslucentForCoordinatorLayout(this,a);
        //StatusBarUtil.setTranslucentForCoordinatorLayout(this,a);
        //StatusBarUtil.setTranslucentForImageViewInFragment(this,a,null);
        //StatusBarUtil.setTranslucentForImageViewInFragment(this,null);
        //StatusBarUtil.setColor(this, getResources().getColor(R.color.blue));
    }

}
