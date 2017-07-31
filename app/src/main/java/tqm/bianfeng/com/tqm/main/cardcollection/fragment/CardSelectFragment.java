package tqm.bianfeng.com.tqm.main.cardcollection.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.nereo.multi_image_selector.MultiImageSelector;
import tqm.bianfeng.com.tqm.CustomView.RequestPermissions;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

/**
 * Created by 王九东 on 2017/7/13.
 */

public class CardSelectFragment extends BaseFragment {
    @BindView(R.id.pictureUpload_lin)
    LinearLayout pictureUploadLin;
    @BindView(R.id.photoUpload_lin)
    LinearLayout photoUploadLin;
    Unbinder unbinder;
    RequestPermissions requestPermissions;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cardselect, container, false);
        unbinder = ButterKnife.bind(this, view);
        requestPermissions = new RequestPermissions(getActivity());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.pictureUpload_lin, R.id.photoUpload_lin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pictureUpload_lin:
//                addImg();
                if(ContextCompat.checkSelfPermission(
                        mContext, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions.requestCamera();
                }else {
                    MultiImageSelector multiImageSelector = MultiImageSelector.create(getActivity())
                            .showCamera(true) // show camera or not. true by default
                            // max select image size, 9 by default. used width #.multi()
                            .single();// single mode

                    multiImageSelector.start(getActivity(), Constan.REQUEST_cardcollaction_IMAGE);
                }

                break;
            case R.id.photoUpload_lin:

                break;
        }
    }

}
