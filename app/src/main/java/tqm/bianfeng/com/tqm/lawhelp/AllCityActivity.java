package tqm.bianfeng.com.tqm.lawhelp;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.lawhelp.fragment.AllCityListFragment;
import tqm.bianfeng.com.tqm.lawhelp.fragment.SearchCityListFragment;

public class AllCityActivity extends BaseActivity implements AllCityListFragment.mListener ,SearchCityListFragment.mListener{


    @BindView(R.id.allcity_toolbar)
    Toolbar allcityToolbar;
    @BindView(R.id.ac_bottom_frag_show)
    FrameLayout acBottomFragShow;

    AllCityListFragment allCityListFragment;

    SearchCityListFragment searchCityListFragment;
    @BindView(R.id.search_city_edi)
    EditText searchCityEdi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allcity);
        ButterKnife.bind(this);
        setToolbar(allcityToolbar, "选择城市");
        allCityListFragment = new AllCityListFragment();
        searchCityListFragment = new SearchCityListFragment();
        initFragemnt(0);
        initEdi();

    }

    public void CloseActivity() {
        onBackPressed();
    }


    public void initEdi() {
        searchCityEdi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==0){
                    Log.i("gqf","edi");
                    initFragemnt(1);
                }else{
                    Log.i("gqf","edi"+editable.toString());
                    initFragemnt(2);
                    searchCityListFragment.SearchData(editable.toString());
                }

            }
        });
    }
    public void initFragemnt(int statu){
        if(statu==0){
            //初始化
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ac_bottom_frag_show, searchCityListFragment).commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ac_bottom_frag_show, allCityListFragment).commit();
            initFragemnt(1);
        }else if(statu==1){
            //显示所有城市列表
            getSupportFragmentManager().beginTransaction().hide(searchCityListFragment).commit();
            getSupportFragmentManager().beginTransaction().show(allCityListFragment).commit();
        }else{
            //显示搜索城市列表
            getSupportFragmentManager().beginTransaction().hide(allCityListFragment).commit();
            getSupportFragmentManager().beginTransaction().show(searchCityListFragment).commit();
        }
    }


}
