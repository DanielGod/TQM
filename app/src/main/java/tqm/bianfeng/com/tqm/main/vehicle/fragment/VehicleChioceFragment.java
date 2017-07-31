package tqm.bianfeng.com.tqm.main.vehicle.fragment;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.DefaultLoadView;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.main.vehicle.adapter.CarSeriesAdapter;
import tqm.bianfeng.com.tqm.main.vehicle.adapter.VehicleChioceAdapter;
import tqm.bianfeng.com.tqm.main.vehicle.adapter.VehicleChiocelEvent;
import tqm.bianfeng.com.tqm.main.vehicle.adapter.VehicleModelAdapter;
import tqm.bianfeng.com.tqm.main.vehicle.bean.Car;
import tqm.bianfeng.com.tqm.main.vehicle.bean.ChexingListBean;
import tqm.bianfeng.com.tqm.main.vehicle.bean.DataBean;
import tqm.bianfeng.com.tqm.main.vehicle.bean.UsedCar;
import tqm.bianfeng.com.tqm.main.vehicle.bean.VehicleModelRequest;
import tqm.bianfeng.com.tqm.main.vehicle.bean.VehicleRequest;
import tqm.bianfeng.com.tqm.main.vehicle.bean.XilieBean;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;


/**
 * Created by 王九东 on 2017/7/1.
 */

public class VehicleChioceFragment extends BaseFragment {
    private static final String TAG = "Daniel";
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.carSeries_rv)
    RecyclerView carSeriesRv;
    @BindView(R.id.vehicleModel_rv)
    RecyclerView vehicleModelRv;
    @BindView(R.id.fragmentClose_tv)
    TextView fragmentCloseTv;
    @BindView(R.id.fragmentTitle_tv)
    TextView fragmentTitleTv;
    @BindView(R.id.default_loadview)
    DefaultLoadView defaultLoadview;

    private List<UsedCar> mUsedCarList;
    List<XilieBean> xilieBeanList;//车系
    List<ChexingListBean> chexingListBeanList;//车型

    //文件名称
    private final static String fileName = "usedcar.json";
    //页面标记
    public static Integer flag = 1;
    //记录选择的品牌Id
    public static Integer carId;
    Unbinder unbind;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehiclechioce, container, false);
        unbind = ButterKnife.bind(this, view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        defaultLoadview.lodingIsFailOrSucess(Constan.LOADING);
        mUsedCarList = new ArrayList<>();//车辆数据源集合
        xilieBeanList = new ArrayList<>();//车辆系列
        chexingListBeanList = new ArrayList<>();//车型
        return view;
    }

    private void setFragmentTilt(Integer flag) {
        switch (flag) {
            case 1:
                fragmentTitleTv.setText("选品牌");
                VehicleChioceFragment.flag = 1;
                break;
            case 2:
                fragmentTitleTv.setText("选车系");
                VehicleChioceFragment.flag = 2;
                break;
            case 3:
                fragmentTitleTv.setText("选车型");
                VehicleChioceFragment.flag = 3;
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VehicleChiocelEvent event) {
        defaultLoadview.lodingIsFailOrSucess(Constan.LOADING);
        //切换到车系选择选择页面
        if ("carSeries".equals(event.getAction())) {
            Log.e(Constan.LOGTAGNAME, "---carSeries---");
            VehicleChioceFragment.flag = 2;
            setFragmentTilt(flag);
            recyclerView.setVisibility(View.GONE);
            carSeriesRv.setVisibility(View.VISIBLE);
            vehicleModelRv.setVisibility(View.GONE);
            //获取品牌下的车系
            loadCarSeriesData(event.getId());
        }
        //切换到车型选择选择页面
        if ("ToVehicleModel".equals(event.getAction())) {
            Log.e(Constan.LOGTAGNAME, "---vehicleModel---");
            VehicleChioceFragment.flag = 3;
            setFragmentTilt(flag);
            recyclerView.setVisibility(View.GONE);
            carSeriesRv.setVisibility(View.GONE);
            vehicleModelRv.setVisibility(View.VISIBLE);
            //获取车系下的车型
            loadvehicleModelData(event.getId());
        }

    }

    /**
     * 获取车型
     *
     * @param id
     */
    private void loadvehicleModelData(Integer id) {
        Log.e(Constan.LOGTAGNAME, "---loadvehicleModelData---");
        Log.e(Constan.LOGTAGNAME, "---id---" + id);
        Subscription subscription = NetWork.getJuHeService().car(Constan.JHKEY, id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<VehicleModelRequest>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_FAILED);
                Log.e(Constan.LOGTAGNAME, "" + e.toString());
            }

            @Override
            public void onNext(VehicleModelRequest vehicleModelRequest) {
                defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_SUCCESS);
                Log.e(Constan.LOGTAGNAME, "" + vehicleModelRequest.getResult().getData().toString());
                setVehicleModelRvAdapter(vehicleModelRequest.getResult().getData());
            }
        });
        compositeSubscription.add(subscription);

    }

    VehicleModelAdapter vehicleModelAdapter;

    //车型适配器
    private void setVehicleModelRvAdapter(List<DataBean> data) {
        Log.e(Constan.LOGTAGNAME, "---年份个数---" + data.size());
        if (vehicleModelAdapter == null) {
            vehicleModelAdapter = new VehicleModelAdapter(mContext, data);
            vehicleModelRv.setLayoutManager(new LinearLayoutManager(mContext));
            vehicleModelRv.setAdapter(vehicleModelAdapter);
        } else {
            vehicleModelAdapter.notifyData(data);
        }

    }

    private void loadCarSeriesData(Integer id) {
        Log.e(Constan.LOGTAGNAME, "---loadCarSeriesData---");
        Log.e(Constan.LOGTAGNAME, "---id---" + id);
        Subscription subscription = NetWork.getJuHeService().series(Constan.JHKEY, id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<VehicleRequest>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                Log.e(Constan.LOGTAGNAME, "" + e.toString());
                defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_FAILED);
            }

            @Override
            public void onNext(VehicleRequest vehicleRequest) {
                defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_SUCCESS);
                for (VehicleRequest.ResultBean.PinpaiListBean pinpaiListBean : vehicleRequest.getResult().getPinpai_list()) {
                    for (XilieBean xilieBean : pinpaiListBean.getXilie()) {
                        xilieBeanList.add(xilieBean);
                    }
                }
                Log.e(Constan.LOGTAGNAME, "" + xilieBeanList.toString());
                setCarSeriesRvAdapter(xilieBeanList);
            }
        });
        compositeSubscription.add(subscription);

    }

    CarSeriesAdapter carSeriesAdapter;

    /**
     * 车系适配器
     *
     * @param xilieBeanList
     */
    private void setCarSeriesRvAdapter(List<XilieBean> xilieBeanList) {
        if (carSeriesAdapter == null) {
            carSeriesAdapter = new CarSeriesAdapter(getActivity(), xilieBeanList);
            carSeriesRv.setLayoutManager(new LinearLayoutManager(getActivity()));
            carSeriesRv.setAdapter(carSeriesAdapter);
        }

    }

    //读取方法
    public String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = getActivity().getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private void loadData() {
        String json = getJson(fileName);
        Log.e(TAG, "车辆数据源json：" + json);
        try {
            JSONObject obj = JSONObject.parseObject(json);
            Set<String> sets = obj.keySet();
            String[] arr = new String[sets.size()];
            sets.toArray(arr);
            Arrays.sort(arr);
            for (int i = 0; i < arr.length; i++) {
                Log.e(TAG, "车辆数据str：" + arr[i]);
                UsedCar usedCar = new UsedCar();
                usedCar.setPinName(arr[i]);
                List<Car> cars = JSONObject.parseArray(obj.get(arr[i]).toString(), Car.class);
                Log.e(TAG, "车辆数据cars：" + cars.toString());
                usedCar.setCarList(cars);
                mUsedCarList.add(usedCar);
            }
            Log.e(TAG, "车辆数据源：" + mUsedCarList.size());
            Log.e(TAG, "车辆数据源：" + mUsedCarList.toString());
            defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_SUCCESS);
            setAdapter(mUsedCarList);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    VehicleChioceAdapter vehicleChioceAdapter;

    private void setAdapter(List<UsedCar> mUsedCarList) {
        Log.e(Constan.LOGTAGNAME, "---setAdapter--");
        if (vehicleChioceAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            vehicleChioceAdapter = new VehicleChioceAdapter(getActivity(), mUsedCarList);
            recyclerView.setAdapter(vehicleChioceAdapter);
        } else {
            vehicleChioceAdapter.notifyData(mUsedCarList);
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        VehicleChioceFragment.flag = 1;
        setFragmentTilt(flag);
        defaultLoadview.lodingIsFailOrSucess(Constan.LOADING);
        loadData();//获取车辆数据源
    }

    @OnClick(R.id.fragmentClose_tv)
    public void onViewClicked() {
        switch (flag) {
            case 1:
                //返回的编辑页面
                VehicleChioceFragment.flag = 1;
                setFragmentTilt(flag);
                EventBus.getDefault().post("ToVehicleEvaluationFragment");
                break;
            case 2:
                //返回的选品牌页面
                VehicleChioceFragment.flag = 1;
                setFragmentTilt(flag);
                recyclerView.setVisibility(View.VISIBLE);
                carSeriesRv.setVisibility(View.GONE);
                vehicleModelRv.setVisibility(View.GONE);

                break;
            case 3:
                VehicleChioceFragment.flag = 2;
                setFragmentTilt(flag);
                EventBus.getDefault().post(new VehicleChiocelEvent("carSeries", carId));
                break;
        }

    }
}
