package tqm.bianfeng.com.tqm.lawhelp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.lawhelp.adapter.allCityListAdapter;
import tqm.bianfeng.com.tqm.lawhelp.tools.InternetImpl;
import tqm.bianfeng.com.tqm.lawhelp.tools.ThreeAddTools;
import tqm.bianfeng.com.tqm.pojo.LawAdd;
import tqm.bianfeng.com.tqm.pojo.cityInfo;

/**
 * Created by johe on 2017/4/1.
 */

public class SearchCityListFragment extends BaseFragment {

    @BindView(R.id.search_city_lisy)
    RecyclerView searchCityLisy;
    @BindView(R.id.no_search_txt)
    TextView noSearchTxt;

    public interface mListener {
        public void CloseActivity();
    }

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;

    }


    ThreeAddTools threeAddTools;
    InternetImpl internet;

    List<cityInfo> datas;
    List<cityInfo> searchDatas;
    allCityListAdapter allCityListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_city_list, container, false);
        threeAddTools = ThreeAddTools.getTools();
        internet = InternetImpl.createInternetImpl();
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    public void initData(){
        datas = threeAddTools.readAllCity(getActivity());
        //整理数据
        datas = internet.sortingData(datas);
    }

    public void SearchData(String search) {
        searchDatas=internet.searchCity(datas,search);
        if(searchDatas.size()==0){
            noSearchTxt.setVisibility(View.VISIBLE);
        }else{
            noSearchTxt.setVisibility(View.GONE);
        }

        if(allCityListAdapter==null){
            allCityListAdapter=new allCityListAdapter(getActivity(),searchDatas);
            searchCityLisy.setLayoutManager(new LinearLayoutManager(getActivity()));
            searchCityLisy.setAdapter(allCityListAdapter);
            allCityListAdapter.setMyItemClickListener(new allCityListAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    Toast.makeText(getActivity(),searchDatas.get(position).getCity(),Toast.LENGTH_SHORT).show();
                    LawAdd lawAdd=realm.where(LawAdd.class).findFirst();
                    realm.beginTransaction();
                    lawAdd.setCity(searchDatas.get(position).getCity());
                    lawAdd.setProvince(searchDatas.get(position).getProvince());
                    realm.copyToRealmOrUpdate(lawAdd);
                    realm.commitTransaction();
                    mListener.CloseActivity();
                }
            });
        }else{
            allCityListAdapter.updateListView(searchDatas);
        }

    }



}
