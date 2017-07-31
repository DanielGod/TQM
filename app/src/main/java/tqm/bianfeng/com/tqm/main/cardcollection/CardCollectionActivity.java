package tqm.bianfeng.com.tqm.main.cardcollection;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;
import tqm.bianfeng.com.tqm.CustomView.ShowDialogAndLoading;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.main.cardcollection.fragment.CardSelectFragment;
import tqm.bianfeng.com.tqm.main.cardcollection.fragment.CardUploadFragment;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

/**
 * Created by 王九东 on 2017/7/13.
 */

public class CardCollectionActivity extends BaseActivity implements CardUploadFragment.mListener {
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1234;
    @BindView(R.id.release_toolbar)
    Toolbar releaseToolbar;
    @BindView(R.id.fragment)
    FrameLayout fragment;
    CardSelectFragment cardSelectFragment;
    CardUploadFragment cardUploadFragment;
    private FragmentTransaction mFragmentTransaction;//fragment事务
    private FragmentManager mFragmentManager;//fragment管理者
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardcollection);
        ButterKnife.bind(this);
        setToolbar(releaseToolbar, "名片收集");
        initView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constan.REQUEST_CODE_CAMERA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    MultiImageSelector multiImageSelector = MultiImageSelector.create(CardCollectionActivity.this)
                            .showCamera(true) // show camera or not. true by default
                            // max select image size, 9 by default. used width #.multi()
                            .single();// single mode

                    multiImageSelector.start(CardCollectionActivity.this, Constan.REQUEST_cardcollaction_IMAGE);
                }else {
                    ShowDialogAndLoading.Show.showDialogAndLoading.showWarningDialog(CardCollectionActivity.this,"","此权限用于上传名片，建议开启！");
                }
                break;

            default:
                break;
        }
    }

    private void initView() {
        mFragmentManager = getSupportFragmentManager();//获取到fragment的管理对象
        setClick(0);//默认进去显示cardSelectFragment
    }

    private void setClick(int i) {
        //开启事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        //显示之前将所有的fragment都隐藏起来,在去显示我们想要显示的fragment
        hideFragment(mFragmentTransaction);
        switch (i){
            case 0:
                Log.e(Constan.LOGTAGNAME,"显示cardSelectFragment:");
                if (cardSelectFragment==null){
                    cardSelectFragment = new CardSelectFragment();
                    mFragmentTransaction.add(R.id.fragment, cardSelectFragment);
                }else {
                    mFragmentTransaction.show(cardSelectFragment);
                }
                break;
            case 1:
                Log.e(Constan.LOGTAGNAME,"显示cardUploadFragment:");
                if (cardUploadFragment==null){
                    cardUploadFragment = new CardUploadFragment();
                    mFragmentTransaction.add(R.id.fragment, cardUploadFragment);
                }else {
                    mFragmentTransaction.show(cardUploadFragment);
                }
                break;
        }
        //提交事务
        mFragmentTransaction.commit();
    }
    private void hideFragment(FragmentTransaction mFragmentTransaction) {
        //如果此fragment不为空的话就隐藏起来
        if (cardUploadFragment != null) {
            mFragmentTransaction.hide(cardUploadFragment);
        }
        if (cardSelectFragment != null) {
            mFragmentTransaction.hide(cardSelectFragment);
        }
    }
    Intent resultIntent;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(Constan.LOGTAGNAME,"requestCode:"+requestCode);
        Log.e(Constan.LOGTAGNAME,"resultCode:"+resultCode);
        Log.e(Constan.LOGTAGNAME,"data:"+data);
        if (requestCode == Constan.REQUEST_cardcollaction_IMAGE) {
            if (resultCode == RESULT_OK) {
                setClick(1);
                resultIntent = data;
            }
        }
    }

    @Override
    public void setImg() {
        cardUploadFragment.setImgInView(resultIntent);
    }
}
