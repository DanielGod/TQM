package tqm.bianfeng.com.tqm.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tqm.bianfeng.com.tqm.R;


public class CatHomeFragment extends Fragment {


    public static CatHomeFragment newInstance() {
        CatHomeFragment fragment = new CatHomeFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_institutions_in, container, false);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}
