package tqm.bianfeng.com.tqm.User;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import tqm.bianfeng.com.tqm.CustomView.ToastType;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.Presenter.ILoginRegisterPresenter;
import tqm.bianfeng.com.tqm.User.Presenter.ILoginRegisterPresenterImpl;
import tqm.bianfeng.com.tqm.User.View.ILoginAndRegistered;

/**
 * Created by johe on 2017/3/13.
 */

public class UserFragment extends Fragment implements ILoginAndRegistered{

    Realm realm;
    @BindView(R.id.user_circle_img)
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
    ToastType toastType;
    LoginRegisteredDialogFragment dialogFragment;
    public static UserFragment newInstance(String param1) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.user_login_registered_btn,R.id.bank_activity_lin, R.id.bank_make_money_lin, R.id.bank_loan_lin, R.id.browsing_history_lin, R.id.user_feedback_lin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bank_activity_lin:
                mListener.changeActivity(MyBankActivityActivity.class);
                break;
            case R.id.bank_make_money_lin:
                mListener.changeActivity(MyBankMakeMoneyActivity.class);
                break;
            case R.id.bank_loan_lin:
                mListener.changeActivity(MyBankLoanActivity.class);
                break;
            case R.id.browsing_history_lin:
                break;
            case R.id.user_feedback_lin:
                mListener.changeActivity(MyBankActivityActivity.class);
                break;
            case R.id.user_login_registered_btn:
                showDialog();
                break;
        }
    }

    public interface mListener {
        public void changeActivity(
                Class activityClass);
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
        realm = Realm.getDefaultInstance();
        iLoginRegisterPresenter=new ILoginRegisterPresenterImpl(this);
        toastType=new ToastType();
        //判断用户是否登录


        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        iLoginRegisterPresenter.onClose();
    }
    public void showDialog() {
        dialogFragment = new LoginRegisteredDialogFragment();
        dialogFragment.setPhoneNum(userPhongNumEdi.getText().toString());
        dialogFragment.setStyle( DialogFragment.STYLE_NORMAL, R.style.Transparent);
        dialogFragment.show(getChildFragmentManager(),"LoginRegisteredDialogFragment");
        dialogFragment.setDimssLinsener(new LoginRegisteredDialogFragment.DimssLinsener() {
            @Override
            public void fragmentDimss() {
                //dialog消失

            }

            @Override
            public void onOk(String ediTxt, String code) {
                iLoginRegisterPresenter.loginOrRegister(ediTxt,code);
            }

            @Override
            public void getCode(boolean isGet) {
                //Presenter层获取验证码
                iLoginRegisterPresenter.setOldCode(isGet);
            }
        });
    }
    public void loginOrRegisteredResult(boolean isSuccess,String msg){
        toastType.showToastWithImg(getActivity(),isSuccess,msg);
        if(isSuccess){
            if(dialogFragment!=null){
                //成功后关闭dialog
                dialogFragment.closeDialog();
            }
        }
    }
}
