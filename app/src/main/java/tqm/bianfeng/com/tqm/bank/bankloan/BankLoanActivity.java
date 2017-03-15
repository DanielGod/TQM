package tqm.bianfeng.com.tqm.bank.bankloan;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

public class BankLoanActivity extends AppCompatActivity implements BankLoanAdapter.BankLoanItemClickListener {
    @BindView(R.id.recyclerview_bank)
    RecyclerView recyclerviewBank;
    @BindView(R.id.toolbar)
    Toolbar toolbar;



    private CompositeSubscription mCompositeSubscription;
    private Unbinder unbinder;
    private int pagNum = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_financing);
        unbinder = ButterKnife.bind(this);
        setToolBar(getResources().getString(R.string.bankloan));
        mCompositeSubscription = new CompositeSubscription();
        initDate(pagNum);

    }
    private void setToolBar(String s) {
        toolbar.setTitle(s);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void initDate(int pagNum) {
        Subscription getBankFinancItem_subscription =  NetWork.getBankService()
                .getBankLoanItem(null, Constan.HOMESHOW_FALSE,pagNum,Constan.PAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankLoanItem>>() {
                    @Override
                    public void onCompleted() {

                    }
                    @DebugLog
                    @Override
                    public void onError(Throwable e) {

                    }
                    @DebugLog
                    @Override
                    public void onNext(List<BankLoanItem> bankloanItems) {
                        setAdapter(bankloanItems);

                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }

    private void setAdapter(List<BankLoanItem> bankloanItems) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerviewBank.setNestedScrollingEnabled(false);
        BankLoanAdapter bankLoanAdapter = new BankLoanAdapter(bankloanItems,BankLoanActivity.this);
        recyclerviewBank.setLayoutManager(linearLayoutManager);
        recyclerviewBank.setAdapter(bankLoanAdapter);
        bankLoanAdapter.setOnItemClickListener(this);
    }

    private void initPopuwindow() {
        //PopupWindow----START-----这里开始到下面标记的地方是实现点击头像弹出PopupWindow，实现用户从PopupWindow中选择更换头像的方式
        backgroundAlpha(0.3f);
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.popu_window, null);
        TextView tv = (TextView) view.findViewById(R.id.left_popuwindow);
        final PopupWindow popupWindow = new PopupWindow(view, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        //获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        popupWindow.setWidth(dm.widthPixels);
        popupWindow.setAnimationStyle(R.style.popuwindow);
        //显示位置
        popupWindow.showAtLocation(view, Gravity.RIGHT, 0, 0);
        popupWindow.setOnDismissListener(new poponDismissListener());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        //PopupWindow-----END


    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @OnClick({R.id.ll_filter})
    public void onClick() {
        initPopuwindow();

    }

    @Override
    public void onItemClick(View view, int postion) {
        Toast.makeText(this, ""+postion, Toast.LENGTH_SHORT).show();

    }

    /**
     * 添加PopupWindow关闭的事件，主要是为了将背景透明度改回来
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mCompositeSubscription.unsubscribe();
    }
}
