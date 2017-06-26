package tqm.bianfeng.com.tqm.bank.quickloan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;

/**
 * Created by Daniel on 2017/6/23.
 */

public class BasicInformationFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_for_credit, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
