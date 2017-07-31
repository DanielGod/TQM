package tqm.bianfeng.com.tqm.main.vehicle;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseAppCompatActivity;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.main.vehicle.fragment.VehicleChioceFragment;
import tqm.bianfeng.com.tqm.main.vehicle.fragment.VehicleEvaluationFragment;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

/**
 * 车辆评估
 * Created by johe on 2017/3/15.
 */

public class VehicleEvaluationActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    FrameLayout container;

    VehicleEvaluationFragment vehicleEvaluationFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicleevaluation);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setToolbar(toolbar,getResources().getString(R.string.vehicle_EvaluationLoan));
        addFragment();
    }

    private void addFragment() {
        if (vehicleEvaluationFragment==null){
            vehicleEvaluationFragment=new VehicleEvaluationFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, vehicleEvaluationFragment).commit();
    }
    private void changeFragment(BaseFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String str) {
        if ("ToVehicleChioceFragment".equals(str)){
            //切换到品牌选择页面
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new VehicleChioceFragment()).commit();
        }
        if ("ToVehicleEvaluationFragment".equals(str)){
            //切换到编辑页面
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, vehicleEvaluationFragment).commit();
            Log.e(Constan.LOGTAGNAME,"-VehicleEvaluationActivity---onEventMainThread--");
        }
    }

}
