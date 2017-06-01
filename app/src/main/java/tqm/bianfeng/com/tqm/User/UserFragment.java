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
import android.widget.ImageView;
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
import tqm.bianfeng.com.tqm.User.applyforactivity.ApplyForStatusActivity;
import tqm.bianfeng.com.tqm.User.release.MyReleaseActivity;
import tqm.bianfeng.com.tqm.User.release.ReleaseActivity;
import tqm.bianfeng.com.tqm.Util.AppUtils;
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
    @BindView(R.id.my_release_lin)
    LinearLayout myReleaseLin;
    @BindView(R.id.user_release_lin)
    LinearLayout userReleaseLin;
    @BindView(R.id.apply_for_status_txt)
    TextView applyForStatusTxt;
    @BindView(R.id.ic_company_img)
    ImageView icCompanyImg;
    @BindView(R.id.ic_company_lin)
    LinearLayout icCompanyLin;
    @BindView(R.id.user_attestation_num)
    TextView userAttestationNum;
    @BindView(R.id.user_fouces_num)
    TextView userFoucesNum;
    @BindView(R.id.user_collection_num)
    TextView userCollectionNum;
    @BindView(R.id.user_read_num)
    TextView userReadNum;

    public static UserFragment newInstance(String param1) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.my_release_lin, R.id.user_release_lin, R.id.bank_collection_lin, R.id.bank_focuse_lin, R.id.bank_browse_lin, R.id.user_apply_for_lin, user_circle_img, R.id.user_login_registered_btn, R.id.user_feedback_lin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_release_lin:

                //跳转我的发布
                mListener.changeActivity(MyReleaseActivity.class);

                break;
            case R.id.user_release_lin:

                //跳转发布选择界面
                mListener.changeActivity(ReleaseActivity.class);

                break;
            case R.id.user_apply_for_lin:
                if (isLogin()) {
                    if (mUser.getApplyForStatu().equals("00") || mUser.getApplyForStatu().equals("01") || mUser.getApplyForStatu().equals("02")) {
                        //查看审核状态
                        mListener.changeActivity(ApplyForStatusActivity.class);
                    } else {
                        //跳转入驻选择界面
                        mListener.changeActivity(ApplyForChooseActivity.class);
                    }
                }
                break;
            case R.id.user_feedback_lin:
                //用户反馈
                mListener.changeActivity(UserFeedbackActivity.class);
                break;
            case R.id.user_login_registered_btn:
                //显示验证码登录
                showDialog();
                break;
            case R.id.user_circle_img:
                Log.i("gqf", "user_circle_img");
                if (isLogin()) {
                    //修改头像
                    mListener.changeUserHeadImg();
                }

                break;
            case R.id.bank_collection_lin:
                if (isLogin()) {
                    //我的收藏
                    mListener.changeActivity(MyCollectionActivity.class);
                }
                break;
            case R.id.bank_focuse_lin:
                if (isLogin()) {
                    //我的关注
                    mListener.changeActivity(MyFocusActivity.class);
                }
                break;
            case R.id.bank_browse_lin:
                if (isLogin()) {
                    //浏览记录
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
        if (realm.where(User.class).findFirst() != null) {
            //iUserWorkPresenter.getUserMsg(mUser.getUserPhone(),AppUtils.getChanel(getActivity()));
            //刷新用户入驻标识
            iUserWorkPresenter.getAuditCode(mUser.getUserId());
            //刷新数量标识
            iUserWorkPresenter.getNum(mUser.getUserId());
            resetUserHeadImg(true);
        }

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
            Log.i("gqf", "mUser" + mUser.toString());
            //开起信息同步
            iUserWorkPresenter.getUserMsg(mUser.getUserPhone(),AppUtils.getChanel(getActivity()));
            iUserWorkPresenter.getNum(mUser.getUserId());
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

        if (mUser.getUserType() != null) {
            if (mUser.getUserType().equals("1001") || mUser.getUserType().equals("1002") || mUser.getUserType().equals("2001") || mUser.getUserType().equals("2002")) {
                userReleaseLin.setVisibility(View.VISIBLE);
                myReleaseLin.setVisibility(View.VISIBLE);
                icCompanyLin.setVisibility(View.VISIBLE);
                if (mUser.getUserType().equals("1001")) {
                    icCompanyImg.setImageResource(R.drawable.ic_company1001);

                } else if (mUser.getUserType().equals("1002")) {
                    icCompanyImg.setImageResource(R.drawable.ic_company1002);
                } else if (mUser.getUserType().equals("2001")) {
                    icCompanyImg.setImageResource(R.drawable.ic_company2001);
                } else if (mUser.getUserType().equals("2002")) {
                    icCompanyImg.setImageResource(R.drawable.ic_company2002);
                }
            } else {
                icCompanyLin.setVisibility(View.GONE);
                userReleaseLin.setVisibility(View.GONE);
                myReleaseLin.setVisibility(View.GONE);
            }
        } else {
            userReleaseLin.setVisibility(View.GONE);
            myReleaseLin.setVisibility(View.GONE);
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
                iLoginRegisterPresenter.loginOrRegister(ediTxt, code, AppUtils.getChanel(getActivity()));
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

    public void showStatus(String code) {
        if (code.equals("00")) {
            applyForStatusTxt.setText("待审");
        } else if (code.equals("01")) {
            applyForStatusTxt.setText("");
            //userApplyForLin.setVisibility(View.GONE);
            userReleaseLin.setVisibility(View.VISIBLE);
            myReleaseLin.setVisibility(View.VISIBLE);
        } else if (code.equals("02")) {
            applyForStatusTxt.setText("未通过");
        }
        realm.beginTransaction();
        mUser.setApplyForStatu(code);
        realm.copyToRealmOrUpdate(mUser);
        realm.commitTransaction();
    }

    //提示是否打开网络设置
    public void shouNetWorkActivity() {
        mListener.shouNetWorkActivity();
    }

    //数量标识
    public void setNum(int num1, int num2, int num3, int num4) {
        if(num1>0){
            userAttestationNum.setVisibility(View.VISIBLE);
            userAttestationNum.setText(num1+"");

        }else{
            userAttestationNum.setVisibility(View.GONE);
        }
        if(num2>0){
            userFoucesNum.setVisibility(View.VISIBLE);
            userFoucesNum.setText(num2+"");

        }else{
            userFoucesNum.setVisibility(View.GONE);
        }
        if(num3>0){
            userCollectionNum.setVisibility(View.VISIBLE);
            userCollectionNum.setText(num3+"");

        }else{
            userCollectionNum.setVisibility(View.GONE);
        }
        if(num4>0){
            userReadNum.setVisibility(View.VISIBLE);
            userReadNum.setText(num4+"");
        }else{
            userReadNum.setVisibility(View.GONE);
        }
    }
}
