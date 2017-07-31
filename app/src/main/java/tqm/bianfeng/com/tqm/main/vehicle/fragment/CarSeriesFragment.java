package tqm.bianfeng.com.tqm.main.vehicle.fragment;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.main.vehicle.adapter.VehicleChioceAdapter;
import tqm.bianfeng.com.tqm.main.vehicle.bean.Car;
import tqm.bianfeng.com.tqm.main.vehicle.bean.UsedCar;


/**
 * Created by 王九东 on 2017/7/1.
 */

public class CarSeriesFragment extends BaseFragment {
    private static final String TAG = "Daniel";
    @BindView(R.id.rv)
    RecyclerView recyclerView;

    private Context mContext;

    private List<UsedCar> mUsedCarList;

    private List<String> carNameList;

    //文件名称
    private final static String fileName = "usedcar.json";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehiclechioce, container, false);
        unbind = ButterKnife.bind(this, view);
        mContext = getActivity();
        mUsedCarList = new ArrayList<>();//车辆数据源集合
        carNameList = new ArrayList<>();
        loadData();//获取车辆数据源
        return view;
    }
    //读取方法
    public String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = getActivity().getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
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
        Log.e(TAG,"车辆数据源json："+json);
        try {
            //            InputStream inputStream = CarSelectActivity.this.getAssets().open("province.json");
            //            String json= new Gson().toJson(new InputStreamReader(inputStream));
            JSONObject obj = JSONObject.parseObject(json);
            Log.e(TAG,"车辆数据obj："+obj);
            Log.e(TAG,"车辆数据obj："+obj.keySet());
            Set<String> sets = obj.keySet();
            String[] arr = new String[sets.size()];
            sets.toArray(arr);
            Arrays.sort(arr);
            for (int i = 0; i < arr.length; i++) {

                Log.e(TAG,"车辆数据str："+arr[i]);
                UsedCar usedCar = new UsedCar();
                usedCar.setPinName(arr[i]);
                List<Car> cars = JSONObject.parseArray(obj.get(arr[i]).toString(), Car.class);
                Log.e(TAG,"车辆数据cars："+cars.toString());
//                for (Car car : cars) {
//                    carNameList.add(car.getBig_ppname());
//                }
                usedCar.setCarList(cars);
                mUsedCarList.add(usedCar);
            }
            Log.e(TAG,"车辆数据源："+mUsedCarList.size());
            Log.e(TAG,"车辆数据源："+mUsedCarList.toString());

            setAdapter(mUsedCarList);

            //            provinceBean = new GsonBuilder().create().fromJson(new InputStreamReader(inputStream), ProvinceBean.class);

        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
    VehicleChioceAdapter vehicleChioceAdapter;
    private void setAdapter(List<UsedCar> mUsedCarList) {
        if (vehicleChioceAdapter==null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            vehicleChioceAdapter = new VehicleChioceAdapter(getActivity(),mUsedCarList);
            recyclerView.setAdapter(vehicleChioceAdapter);
        }else {
            vehicleChioceAdapter.notifyData(mUsedCarList);
        }


    }

    Unbinder unbind;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }


}
