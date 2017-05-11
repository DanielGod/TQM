package tqm.bianfeng.com.tqm.User;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.Fragment.LoginRegisteredDialogFragment;
import tqm.bianfeng.com.tqm.User.Presenter.ILoginRegisterPresenter;
import tqm.bianfeng.com.tqm.User.Presenter.ILoginRegisterPresenterImpl;
import tqm.bianfeng.com.tqm.User.Presenter.IUserWorkPresenter;
import tqm.bianfeng.com.tqm.User.Presenter.IUserWorkPresenterImpl;
import tqm.bianfeng.com.tqm.User.View.ILoginAndRegistered;
import tqm.bianfeng.com.tqm.User.applyforactivity.ApplyForChooseActivity;
import tqm.bianfeng.com.tqm.User.release.MyReleaseActivity;
import tqm.bianfeng.com.tqm.User.release.ReleaseActivity;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.pojo.User;

import static tqm.bianfeng.com.tqm.R.id.user_circle_img;

/**
 * Created by johe on 2017/3/13.
 */

public class UserFragment extends BaseFragment implements ILoginAndRegistered {


    @BindView(user_circle_img)
    CircleImageView userCircleImg;
    @BindView(R.id.user_register_phone_num_txt)
    TextView userRegisterPhoneNumTxt;
    @BindView(R.id.user_phong_num_edi)
    EditText userPhongNumEdi;
    @BindView(R.id.user_login_registered_btn)
    Button userLoginRegisteredBtn;

    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.user_feedback_lin)
    LinearLayout userFeedbackLin;
    ILoginRegisterPresenter iLoginRegisterPresenter;
    IUserWorkPresenter iUserWorkPresenter;
    LoginRegisteredDialogFragment dialogFragment;
    User mUser;
    @BindView(R.id.user_top_rel)
    RelativeLayout userTopRel;
    @BindView(R.id.user_apply_for_lin)
    LinearLayout userApplyForLin;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    @BindView(R.id.bank_collection_lin)
    LinearLayout bankCollectionLin;
    @BindView(R.id.bank_focuse_lin)
    LinearLayout bankFocuseLin;
    @BindView(R.id.bank_browse_lin)
    LinearLayout bankBrowseLin;

    public static UserFragment newInstance(String param1) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.my_release_lin,R.id.user_release_lin,R.id.bank_collection_lin, R.id.bank_focuse_lin, R.id.bank_browse_lin,R.id.user_apply_for_lin, user_circle_img, R.id.user_login_registered_btn, R.id.user_feedback_lin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_release_lin:

                mListener.changeActivity(MyReleaseActivity.class);

                break;
            case R.id.user_release_lin:

                mListener.changeActivity(ReleaseActivity.class);

                break;
            case R.id.user_apply_for_lin:

                mListener.changeActivity(ApplyForChooseActivity.class);

                break;

            case R.id.user_feedback_lin:
                mListener.changeActivity(UserFeedbackActivity.class);
                break;
            case R.id.user_login_registered_btn:
                showDialog();
                break;
            case R.id.user_circle_img:
                Log.i("gqf", "user_circle_img");
                if (isLogin()) {
                    mListener.changeUserHeadImg();
                }

                break;
            case R.id.bank_collection_lin:
                if(isLogin()){
                    mListener.changeActivity(MyCollectionActivity.class);
                }
                break;
            case R.id.bank_focuse_lin:
                if(isLogin()){
                    mListener.changeActivity(MyFocusActivity.class);
                }
                break;
            case R.id.bank_browse_lin:
                if(isLogin()){
                    mListener.changeActivity(MyBrowseActivity.class);
                }
                break;
        }
    }



    public interface mListener {
        public void changeActivity(
                Class activityClass);

        public void changeUserHeadImg();

        public void shouNetWorkActivity();
    }

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;

    }

    //登录判断
    public boolean isLogin() {
        if (realm.where(User.class).findFirst() == null) {
            Toast.makeText(getActivity(), "请您先登录后使用该功能", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        iLoginRegisterPresenter = new ILoginRegisterPresenterImpl(this);
        iUserWorkPresenter = new IUserWorkPresenterImpl(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    //初始化显示数据
    public void initView() {
        if (realm.where(User.class).findFirst() != null) {
            mUser = realm.where(User.class).findFirst();
            //开起信息同步
            iUserWorkPresenter.getUserMsg(mUser.getUserPhone());
            //显示账户信息
            userRegisterPhoneNumTxt.setVisibility(View.VISIBLE);
            userRegisterPhoneNumTxt.setText(mUser.getUserPhone());
            userPhongNumEdi.setVisibility(View.GONE);
            userLoginRegisteredBtn.setVisibility(View.GONE);
            //userTopRel.setBackgroundResource(R.drawable.user_top_bg);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) userCircleImg.getLayoutParams();
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            userCircleImg.setLayoutParams(lp);
            LinearLayout.LayoutParams linlp = (LinearLayout.LayoutParams) userTopRel.getLayoutParams();
            linlp.height = (int) getResources().getDimension(R.dimen.hugehxxxxxxdp);
            userTopRel.setLayoutParams(linlp);
            //显示头像
            resetUserHeadImg(true);
        }
    }

    //重置用户头像
    public void resetUserHeadImg(boolean isChange) {
        if (isChange) {
            mUser = realm.where(User.class).findFirst();
            if (mUser.getUserAvatar() != null) {
                if (!mUser.getUserAvatar().equals("")) {
                    Picasso.with(getContext()).load(mUser.getUserAvatar())
                            .placeholder(R.drawable.ic_user_head_img)
                            .error(R.drawable.ic_user_head_img)
                            .into(userCircleImg);
                }
            }
        } else {
            Toast.makeText(getActivity(), "上传头像失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iLoginRegisterPresenter.onClose();
        iUserWorkPresenter.onClose();
    }

    //显示登录注册界面
    public void showDialog() {
        dialogFragment = new LoginRegisteredDialogFragment();
        dialogFragment.setPhoneNum(userPhongNumEdi.getText().toString());
        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Transparent);
        dialogFragment.show(getChildFragmentManager(), "LoginRegisteredDialogFragment");
        dialogFragment.setDimssLinsener(new LoginRegisteredDialogFragment.DimssLinsener() {
            @Override
            public void fragmentDimss() {
                //dialog消失

            }

            @Override
            public void onOk(String ediTxt, String code) {
                //验证并注册或登录
                iLoginRegisterPresenter.loginOrRegister(ediTxt, code);
            }

            @Override
            public void getCode(String phone, boolean isGet) {
                //Presenter层获取验证码
                iLoginRegisterPresenter.setOldCode(phone, isGet);
            }
        });
    }


    //获取主界面传递头像图片
    public void setUserHeadImg(File bitmap) {
        Bitmap bm = BitmapFactory.decodeFile(bitmap.getAbsolutePath());
        if (bm == null) {
            Log.i("gqf", "bm==null");
        }
        userCircleImg.setImageBitmap(bm);
        //上传并显示
        iUserWorkPresenter.uploadUserHeadImg(bitmap, realm.where(User.class).findFirst().getUserId());

    }

    //注册登录后返回
    public void loginOrRegisteredResult(int type, boolean isSuccess, String msg) {
        if (type == 0) {
            toastType.showToast(getActivity(), msg);
        } else {
            toastType.showToastWithImg(getActivity(), isSuccess, msg);
        }
        if (isSuccess) {
            if (dialogFragment != null) {
                //成功后关闭dialog
                dialogFragment.closeDialog();
                initView();
            }
        }
    }

    //提示是否打开网络设置
    public void shouNetWorkActivity() {
        mListener.shouNetWorkActivity();
    }
}
