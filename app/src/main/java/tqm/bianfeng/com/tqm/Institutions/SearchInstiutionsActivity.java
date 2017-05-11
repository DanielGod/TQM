package tqm.bianfeng.com.tqm.Institutions;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.Institutions.adapter.LawFirmOrInstitutionListAdapter;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.InstitutionItem;

/**
 * Created by johe on 2017/5/10.
 */

public class SearchInstiutionsActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar searchToolbar;
    @BindView(R.id.search_city_edi)
    EditText searchCityEdi;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.search_recyclerView)
    RecyclerView searchRecyclerView;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.search_btn)
    Button searchBtn;
    @BindView(R.id.no_search_txt)
    TextView noSearchTxt;
    @BindView(R.id.indicator)
    AVLoadingIndicatorView indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_instiutions);
        ButterKnife.bind(this);
        //setToolbar(searchToolbar, "搜索机构");
        //searchToolbar.
        searchToolbar.setTitle("搜索机构");
        searchToolbar.setNavigationIcon(R.drawable.ic_back_arrow_dark);
        searchToolbar.setTitleTextColor(getResources().getColor(R.color.font_black_1));
        searchToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        indicator.hide();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.font_black_7));

    }

    LawFirmOrInstitutionListAdapter lawFirmOrInstitutionListAdapter;
    Intent intent;
    List<InstitutionItem> datas;

    public void initData(String searchName) {
        searchBtn.setEnabled(false);
        showLoading(0);
        Subscription getBankFinancItem_subscription = NetWork.getInstitutionService().getInstitutionItem("0" + (0 + 1), 0 + 1, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<InstitutionItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        searchBtn.setEnabled(true);
                        showLoading(1);
                        showSearchResult();
                    }

                    @Override
                    public void onNext(List<InstitutionItem> institutionItems) {
                        datas = institutionItems;
                        Log.i("gqf", "institutionItems" + institutionItems.toString());
                        initList();
                        searchBtn.setEnabled(true);
                        showSearchResult();
                        showLoading(1);
                    }
                });

        compositeSubscription.add(getBankFinancItem_subscription);
    }

    public void showSearchResult() {
        if (datas.size() == 0) {
            noSearchTxt.setVisibility(View.VISIBLE);
        } else {
            noSearchTxt.setVisibility(View.GONE);
        }
    }

    CountDownTimer countDownTimer;
    int progressIndex = 6;

    public void showLoading(int index) {
        if (index == 0) {
            //开始
            indicator.show();
            countDownTimer = new CountDownTimer(1000 * 100, 1000) {
                public void onTick(long millisUntilFinished) {
                    // you can change the progress bar color by ProgressHelper every 800 millis
                    progressIndex++;
                    switch (progressIndex % 6) {
                        case 0:
                            indicator.setIndicatorColor(getResources().getColor(R.color.blue_btn_bg_color));
                            break;
                        case 1:
                            indicator.setIndicatorColor(getResources().getColor(R.color.material_deep_teal_50));
                            break;
                        case 2:
                            indicator.setIndicatorColor(getResources().getColor(R.color.success_stroke_color));
                            break;
                        case 3:
                            indicator.setIndicatorColor(getResources().getColor(R.color.material_deep_teal_20));
                            break;
                        case 4:
                            indicator.setIndicatorColor(getResources().getColor(R.color.material_blue_grey_80));
                            break;
                        case 5:
                            indicator.setIndicatorColor(getResources().getColor(R.color.warning_stroke_color));
                            break;
                        case 6:
                            indicator.setIndicatorColor(getResources().getColor(R.color.success_stroke_color));
                            break;
                    }
                }

                public void onFinish() {
                    progressIndex = 6;
                }
            }.start();

        } else {
            //结束
            indicator.hide();
            countDownTimer.onFinish();
        }
    }

    public void initList() {
        if (lawFirmOrInstitutionListAdapter == null) {
            lawFirmOrInstitutionListAdapter = new LawFirmOrInstitutionListAdapter(this, datas);
            lawFirmOrInstitutionListAdapter.setOnItemClickListener(new LawFirmOrInstitutionListAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    intent = new Intent(SearchInstiutionsActivity.this, CompanyInfoActivity.class);
                    intent.putExtra("InstitutionId", datas.get(position).getInstitutionId());
                    startActivity(intent);

                }
            });

            searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            searchRecyclerView.setAdapter(lawFirmOrInstitutionListAdapter);
        } else {
            lawFirmOrInstitutionListAdapter.update(datas);

        }
    }

    @OnClick(R.id.search_btn)
    public void onClick() {
        if (searchCityEdi.getText().toString().equals("")) {
            Toast.makeText(this, "搜索名称不能为空", Toast.LENGTH_SHORT).show();

        } else {
            datas=new ArrayList<>();
            initList();
            initData(searchCityEdi.getText().toString());
        }
    }
}
