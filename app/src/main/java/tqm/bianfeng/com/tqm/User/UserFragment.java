package tqm.bianfeng.com.tqm.User;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import tqm.bianfeng.com.tqm.User.Presenter.ILoginRegisterPresenter;
import tqm.bianfeng.com.tqm.User.Presenter.ILoginRegisterPresenterImpl;
import tqm.bianfeng.com.tqm.User.Presenter.IUserWorkPresenter;
import tqm.bianfeng.com.tqm.User.Presenter.IUserWorkPresenterImpl;
import tqm.bianfeng.com.tqm.User.View.ILoginAndRegistered;
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
    @BindView(R.id.bank_activity_num_txt)
    TextView bankActivityNumTxt;
    @BindView(R.id.bank_activity_lin)
    LinearLayout bankActivityLin;
    @BindView(R.id.bank_make_money_num_txt)
    TextView bankMakeMoneyNumTxt;
    @BindView(R.id.bank_make_money_lin)
    LinearLayout bankMakeMoneyLin;
    @BindView(R.id.bank_loan_num_txt)
    TextView bankLoanNumTxt;
    @BindView(R.id.bank_loan_lin)
    LinearLayout bankLoanLin;
    @BindView(R.id.browsing_history_num_txt)
    TextView browsingHistoryNumTxt;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.browsing_history_lin)
    LinearLayout browsingHistoryLin;
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

    public static UserFragment newInstance(String param1) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({user_circle_img, R.id.user_login_registered_btn, R.id.bank_activity_lin, R.id.bank_make_money_lin, R.id.bank_loan_lin, R.id.browsing_history_lin, R.id.user_feedback_lin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bank_activity_lin:
                if (realm.where(User.class).findFirst() == null) {
                    Toast.makeText(getActivity(), "请您先登录后使用", Toast.LENGTH_SHORT).show();
                } else {
                    mListener.changeActivity(MyBankActivityActivity.class);
                }
                break;
            case R.id.bank_make_money_lin:
                if (realm.where(User.class).findFirst() == null) {
                    Toast.makeText(getActivity(), "请您先登录后使用", Toast.LENGTH_SHORT).show();
                } else {
                    mListener.changeActivity(MyBankMakeMoneyActivity.class);
                }
                break;
            case R.id.bank_loan_lin:
                if (realm.where(User.class).findFirst() == null) {
                    Toast.makeText(getActivity(), "请您先登录后使用", Toast.LENGTH_SHORT).show();
                } else {
                    mListener.changeActivity(MyBankLoanActivity.class);
                }
                break;
            case R.id.browsing_history_lin:
                break;
            case R.id.user_feedback_lin:
                mListener.changeActivity(UserFeedbackActivity.class);
                break;
            case R.id.user_login_registered_btn:
                showDialog();
                break;
            case R.id.user_circle_img:
                //弹出popuwindow，修改头像
                if (realm.where(User.class).findFirst() == null) {
                    Toast.makeText(getActivity(), "请您先登录后使用", Toast.LENGTH_SHORT).show();
                } else {
                    mListener.changeUserHeadImg();
                }
                break;
        }
    }

    public interface mListener {
        public void changeActivity(
                Class activityClass);

        public void changeUserHeadImg();
    }

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        iLoginRegisterPresenter = new ILoginRegisterPresenterImpl(this);
        iUserWorkPresenter = new IUserWorkPresenterImpl(this);
        //判断用户是否登录
        initView();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initView();
        }
    }

    public void initView() {
        if (realm.where(User.class).findFirst() != null && userRegisterPhoneNumTxt.getText().equals("")) {
            mUser = realm.where(User.class).findFirst();
            userRegisterPhoneNumTxt.setVisibility(View.VISIBLE);
            userRegisterPhoneNumTxt.setText(mUser.getUserPhone());
            userPhongNumEdi.setVisibility(View.GONE);
            userLoginRegisteredBtn.setVisibility(View.INVISIBLE);
            userTopRel.setBackgroundResource(R.drawable.user_top_bg);
            resetUserHeadImg(true);
        }
    }

    //重置用户头像
    public void resetUserHeadImg(boolean isChange) {
        if (isChange) {
            mUser = realm.where(User.class).findFirst();
            if (!mUser.getUserAvatar().equals("")) {
                Picasso.with(getContext()).load(mUser.getUserAvatar())
                        .placeholder(R.drawable.ic_user_head_img)
                        .error(R.drawable.ic_user_head_img)
                        .into(userCircleImg);
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
                iLoginRegisterPresenter.loginOrRegister(ediTxt, code);
            }

            @Override
            public void getCode(boolean isGet) {
                //Presenter层获取验证码
                iLoginRegisterPresenter.setOldCode(isGet);
            }
        });
    }

    //设置关注数量01-活动;02-理财;02-贷款
    public void setTextNum(int num1, int num2, int num3, int num4) {
        bankActivityNumTxt.setText(num1 + "");
        bankMakeMoneyNumTxt.setText(num2 + "");
        bankLoanNumTxt.setText(num3 + "");
    }

    //获取主界面传递头像图片
    public void setUserHeadImg(File bitmap) {
        //上传并显示
        iUserWorkPresenter.uploadUserHeadImg(bitmap, realm.where(User.class).findFirst().getUserId());
        Bitmap bm = BitmapFactory.decodeFile(bitmap.getAbsolutePath());
        userCircleImg.setImageBitmap(bm);
    }

    //注册登录后返回
    public void loginOrRegisteredResult(boolean isSuccess, String msg) {
        toastType.showToastWithImg(getActivity(), isSuccess, msg);
        if (isSuccess) {
            if (dialogFragment != null) {
                //成功后关闭dialog
                dialogFragment.closeDialog();
            }
        }
    }
}
