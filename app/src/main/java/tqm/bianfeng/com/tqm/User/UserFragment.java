package tqm.bianfeng.com.tqm.User;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
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

import com.google.gson.Gson;
import com.meituan.android.walle.WalleChannelReader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.MyScrollview;
import tqm.bianfeng.com.tqm.CustomView.ShowDialogAndLoading;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.Fragment.LoginRegisteredDialogFragment;
import tqm.bianfeng.com.tqm.User.Presenter.ILoginRegisterPresenter;
import tqm.bianfeng.com.tqm.User.Presenter.ILoginRegisterPresenterImpl;
import tqm.bianfeng.com.tqm.User.Presenter.IUserWorkPresenter;
import tqm.bianfeng.com.tqm.User.Presenter.IUserWorkPresenterImpl;
import tqm.bianfeng.com.tqm.User.View.ILoginAndRegistered;
import tqm.bianfeng.com.tqm.User.applyforactivity.MyApplyForActivity;
import tqm.bianfeng.com.tqm.User.order.OrderActivity;
import tqm.bianfeng.com.tqm.User.release.MyReleaseActivity;
import tqm.bianfeng.com.tqm.User.release.ReleaseActivity;
import tqm.bianfeng.com.tqm.Util.PhoneUtils;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.main.vehicle.bean.Contact;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.AddressBook;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.YwDksq;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

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
    @BindView(R.id.user_feedback_lin)
    LinearLayout userFeedbackLin;
    @BindView(R.id.user_top_rel)
    RelativeLayout userTopRel;
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
    @BindView(R.id.user_attestation_num)
    TextView userAttestationNum;
    @BindView(R.id.user_fouces_num)
    TextView userFoucesNum;
    @BindView(R.id.user_collection_num)
    TextView userCollectionNum;
    @BindView(R.id.user_read_num)
    TextView userReadNum;
    @BindView(R.id.my_scrollview)
    MyScrollview myScrollview;

    @BindView(R.id.layout_unCommon_user_lin)
    LinearLayout layoutUnCommonUserLin;
    @BindView(R.id.order_unReceive_num_tv)
    TextView orderUnReceiveNumTv;
    @BindView(R.id.order_receive_num_tv)
    TextView orderReceiveNumTv;
    @BindView(R.id.release_img)
    ImageView releaseImg;
    @BindView(R.id.collection_img)
    ImageView collectionImg;
    @BindView(R.id.focuse_img)
    ImageView focuseImg;
    @BindView(R.id.read_img)
    ImageView readImg;
    @BindView(R.id.loginSuccess_lin)
    LinearLayout loginSuccessLin;
    @BindView(R.id.user_unreceived_lin)
    LinearLayout userUnreceivedLin;
    @BindView(R.id.user_received_lin)
    LinearLayout userReceivedLin;
    @BindView(R.id.user_mmGame_lin)
    LinearLayout userMmGameLin;
    @BindView(R.id.user_apply_for_lin)
    LinearLayout userApplyForLin;
    @BindView(R.id.user_integralIndiana_lin)
    LinearLayout userIntegralIndianaLin;
    @BindView(R.id.user_setting_lin)
    LinearLayout userSettingLin;
    @BindView(R.id.user_aboutUs_lin)
    LinearLayout userAboutUsLin;
    @BindView(R.id.user_shapeAPP_lin)
    LinearLayout userShapeAPPLin;


    ILoginRegisterPresenter iLoginRegisterPresenter; //登录注册接口
    IUserWorkPresenter iUserWorkPresenter; //登录成功后信息更新结构
    LoginRegisteredDialogFragment dialogFragment; //验证码弹出框
    User mUser;
    @BindView(R.id.user_signIn_img)
    ImageView userSignInImg;
    @BindView(R.id.user_point_tv)
    TextView userPointTv;

    ShowDialogAndLoading showDialogAndLoading;
    @BindView(R.id.user_second_lin)
    LinearLayout userSecondLin;

    public static UserFragment newInstance(String param1) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.user_contact_lin,R.id.user_mmGame_lin,R.id.user_transactoinrecords_lin,R.id.user_correcterror_lin,R.id.user_cardrecode_lin,R.id.user_integralIndiana_lin, R.id.user_signIn_img, R.id.my_release_lin, R.id.user_release_lin, R.id.bank_collection_lin, R.id.bank_focuse_lin, R.id.bank_browse_lin, R.id.user_apply_for_lin, user_circle_img, R.id.user_login_registered_btn, R.id.user_feedback_lin, R.id.user_unreceived_lin, R.id.user_received_lin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_contact_lin:
                //通讯录
                if (isLogin()) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        //当前Activity没有获得READ_CONTACTS权限时
                        Log.e(Constan.LOGTAGNAME, "Activity没有获得READ_CONTACTS权限");
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.GET_ACCOUNTS}, Constan.REQUEST_CODE_PERMISSION_CONTACTS);
                    }else {
                        showDialogAndLoading.showLoading("上传中。。", getActivity());
                        int userId = realm.where(User.class).findFirst().getUserId();
                        List<HashMap<String, String>> list= PhoneUtils.getPhoneContacts(
                               getActivity());
                        List<Contact> contacts = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            Contact contact = new Contact();
                            contact.setContact(list.get(i).get("name"));
                            contact.setContactNumber(list.get(i).get("phone"));
                            contact.setUserId(userId);
                            Log.e(Constan.LOGTAGNAME, "联系人："+contact.toString());
                            contacts.add(contact);
                            Constan.mContacts=contacts;
                        }
                        saveAddressBook();
                    }
                }
                break;
            case R.id.user_mmGame_lin:
                //下载中心
                mListener.changeActivity(DownloaderCenter.class);
                break;
            case R.id.user_transactoinrecords_lin:
                //交易记录
                startActivity(new Intent(getActivity(), UserIntegralActivity.class).putExtra("tag", 2));
                break;
            case R.id.user_correcterror_lin:
                //纠错记录
                startActivity(new Intent(getActivity(), UserIntegralActivity.class).putExtra("tag", 1));
                break;
            case R.id.user_cardrecode_lin:
                //名片收集

                startActivity(new Intent(getActivity(), UserIntegralActivity.class).putExtra("tag", 0));
                break;
            case R.id.user_integralIndiana_lin:
                //积分
                if (isLogin()) {
                    startActivity(new Intent(getActivity(), UserIntegralActivity.class).putExtra("tag", 3));
                }
                break;
            case R.id.user_signIn_img:
                //签到
                signIn();
                break;
            case R.id.user_unreceived_lin:
                //未领取
                startActivity(new Intent(getActivity(), OrderActivity.class).putExtra("orderType", "01"));
                //                mListener.changeActivity(OrderActivity.class);
                break;
            case R.id.user_received_lin:
                //已领取
                startActivity(new Intent(getActivity(), OrderActivity.class).putExtra("orderType", "02"));

                break;
            case R.id.my_release_lin:
                //跳转我的发布
                if (isLogin()) {
                    mListener.changeActivity(MyReleaseActivity.class);
                }
                break;
            case R.id.user_release_lin:
                //跳转发布选择界面
                if (isLogin()) {
                    if ("01".equals(mUser.getUserType())){
                        Toast.makeText(mContext, "认证后开放此功能！", Toast.LENGTH_SHORT).show();
                    }else {
                        mListener.changeActivity(ReleaseActivity.class);
                    }
                }
                break;
            case R.id.user_apply_for_lin:
                if (isLogin()) {
                    //我的申请
//                    mListener.changeActivity(ApplyForSudelChooseActivity.class);
                    mListener.changeActivity(MyApplyForActivity.class);
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

    private void saveAddressBook() {
        AddressBook adderssBooks = new AddressBook();
        adderssBooks.setAddressBooks(Constan.mContacts);
        String addressBook = new Gson().toJson(adderssBooks);
        Subscription subscription = NetWork.getUserService().saveAddressBook(addressBook)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResultCode>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Constan.log("上传通讯录异常："+e.toString());
                showDialogAndLoading.stopLoaading();
                showDialogAndLoading.showFailureDialog(getActivity(), "", "上传失败！");
            }

            @Override
            public void onNext(ResultCode resultCode) {
                showDialogAndLoading.stopLoaading();
                Constan.log("上传通讯录结果"+resultCode.toString());
                if (resultCode.getCode() == 10001) {
                    showDialogAndLoading.showSuccessDialog(getActivity(), "", "上传成功！");
                }else {
                    showDialogAndLoading.showFailureDialog(getActivity(), "", "上传失败！");
                }
            }
        });
        compositeSubscription.add(subscription);
    }

    private void signIn() {
        showDialogAndLoading.showLoading("签到中。。", getActivity());
        Subscription subscription = NetWork.getUserService().sign(realm.where(User.class).findFirst().getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResultCode>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                showDialogAndLoading.stopLoaading();
            }

            @Override
            public void onNext(ResultCode resultCode) {
                showDialogAndLoading.stopLoaading();
                if (resultCode.getCode() == 10001) {
                    showDialogAndLoading.showAfterDialog(getActivity(), "", "签到成功！", "确定");
                }
            }
        });
        compositeSubscription.add(subscription);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    public interface mListener {
        public void changeActivity(Class activityClass);

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
        initView();
    }

    private void setAuditCode() {
        if (mUser.getUserType() != null) {
            Log.e(Constan.LOGTAGNAME, "认证标识：" + mUser.getUserType());
            if (!mUser.getUserType().equals("01")) {

                //我的发布
                myReleaseLin.setVisibility(View.VISIBLE);
                //订单中心
                layoutUnCommonUserLin.setVisibility(View.VISIBLE);

                if (mUser.getUserType().equals("02")) {
                    icCompanyImg.setImageResource(R.drawable.min);
                } else if (mUser.getUserType().equals("03")) {
                    icCompanyImg.setImageResource(R.drawable.zhong);
                } else if (mUser.getUserType().equals("04")) {
                    icCompanyImg.setImageResource(R.drawable.zi);
                } else if (mUser.getUserType().equals("05")) {
                    icCompanyImg.setImageResource(R.drawable.ge);
                } else if (mUser.getUserType().equals("06")) {
                    icCompanyImg.setImageResource(R.drawable.xin);
                }
            } else {
                icCompanyImg.setImageResource(R.drawable.putong);
                myReleaseLin.setVisibility(View.GONE);
                layoutUnCommonUserLin.setVisibility(View.GONE);
            }
        } else {
            Log.e(Constan.LOGTAGNAME, "认证标识为空");
            //竖隔线防止偏移
            myReleaseLin.setVisibility(View.GONE);
        }
    }

    Unbinder unbind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user2, container, false);
        unbind = ButterKnife.bind(this, view);
        showDialogAndLoading = ShowDialogAndLoading.Show.showDialogAndLoading;
        iLoginRegisterPresenter = new ILoginRegisterPresenterImpl(this);
        iUserWorkPresenter = new IUserWorkPresenterImpl(this);
        showDialogAndLoading.setmLinsener(new ShowDialogAndLoading.Linsener() {
            @Override
            public void showBefore() {

            }

            @Override
            public void showAfter() {
                //更换签到图标
                userSignInImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.yqd));
                userSignInImg.setEnabled(false);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //        initScroll();
        //        initView();
    }

    int scrollHeight = 0;
    int sliderHeight = 1;
    boolean isNetWork = true;
    //    public void initScroll() {
    //        myScrollview.setOnScollChangedListener(new MyScrollview.OnScollChangedListener() {
    //            @Override
    //            public void onScrollChanged(MyScrollview scrollView, int x, int y, int oldx, int oldy) {
    //                Log.i("gqf", "x" + x + "y" + y + "oldx" + oldx + "oldy" + oldy);
    //                scrollHeight = y;
    //                if (sliderHeight != 1) {
    //                    if (isNetWork) {
    //                        try {
    //                            mListener.setToolBarColorBg(getAlph(scrollHeight, sliderHeight));
    //                        } catch (Exception e) {
    //                        }
    //
    //                    }
    //                }
    //            }
    //        });
    //    }


    //初始化显示数据
    public void initView() {
        Log.i("Daniel", "用户信息是否空：：" + realm.where(User.class).findFirst());
        if (realm.where(User.class).findFirst() != null) {
            mUser = realm.where(User.class).findFirst();
            Log.e("Daniel", "initView用户信息:" + mUser.toString());
            //开起信息同步
            // TODO: 2017/8/3 渠道号

//            iUserWorkPresenter.getUserMsg(mUser.getUserPhone(),"afwl001");
            iUserWorkPresenter.getUserMsg(mUser.getUserPhone(), WalleChannelReader.getChannel(getActivity()));
            //刷新数量标识
            iUserWorkPresenter.getNum(mUser.getUserId());
            //显示账户信息
            loginSuccessLin.setVisibility(View.VISIBLE);
            userRegisterPhoneNumTxt.setText(mUser.getUserPhone());
            userPhongNumEdi.setVisibility(View.GONE);
            userLoginRegisteredBtn.setVisibility(View.GONE);
            //userTopRel.setBackgroundResource(R.drawable.user_top_bg);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) userCircleImg.getLayoutParams();
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            userCircleImg.setLayoutParams(lp);
            LinearLayout.LayoutParams linlp = (LinearLayout.LayoutParams) userTopRel.getLayoutParams();
            linlp.height = (int) getResources().getDimension(R.dimen.hugehxxxxxxxdp);
            userTopRel.setLayoutParams(linlp);
            //显示头像
            resetUserHeadImg(true);
            //iUserWorkPresenter.getUserMsg(mUser.getUserPhone(),AppUtilsBd.getChanel(getActivity()));
            //刷新用户入驻标识
            setAuditCode();
            //发布信息,纠错记录，名片收集，交易记录
            userSecondLin.setVisibility(View.VISIBLE);
            //请求订单数目
            requestOrderNum("01", mUser.getUserId());
            requestOrderNum("02", mUser.getUserId());
        }else {
            userSecondLin.setVisibility(View.GONE);
        }
    }

    private void requestOrderNum(final String s, int userId) {
        Log.e("Daniel", "订单信息请求参数：mOrderType：" + s + "userId:" + userId);
        Subscription subscription = NetWork.getUserService().getItem(s, userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<YwDksq>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Daniel", "订单信息：" + e.toString());
            }

            @DebugLog
            @Override
            public void onNext(List<YwDksq> ywDksqs) {
                Log.e("Daniel", "订单信息：" + ywDksqs.toString());
                if ("01".equals(s)) {
                    if (ywDksqs.size() == 0) {
                        orderUnReceiveNumTv.setVisibility(View.GONE);
                    } else {
                        orderUnReceiveNumTv.setVisibility(View.VISIBLE);
                        orderUnReceiveNumTv.setText("" + ywDksqs.size());
                    }
                } else {
                    if (ywDksqs.size() == 0) {
                        orderReceiveNumTv.setVisibility(View.GONE);
                    } else {
                        orderReceiveNumTv.setVisibility(View.VISIBLE);
                        orderReceiveNumTv.setText("" + ywDksqs.size());
                    }
                }

            }
        });
        compositeSubscription.add(subscription);


    }

    //重置用户头像
    public void resetUserHeadImg(boolean isChange) {
        if (isChange) {
            mUser = realm.where(User.class).findFirst();
            if (mUser.getUserAvatar() != null) {
                if (!mUser.getUserAvatar().equals("")) {
                    Picasso.with(getContext()).load(mUser.getUserAvatar()).placeholder(R.drawable.ic_user_head_img).error(R.drawable.ic_user_head_img).into(userCircleImg);
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
//                iLoginRegisterPresenter.loginOrRegister(ediTxt, code, "afwl001");
                // TODO: 2017/8/3 渠道号
                iLoginRegisterPresenter.loginOrRegister(ediTxt, code, WalleChannelReader.getChannel(getActivity()));
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
    public void setNum(int num1, int num2, int num3, int num4, int num5, int num6) {
        //发布
        userAttestationNum.setText(num1 > 0 ? num1 + "" : 0 + "");
        //关注
        userFoucesNum.setText(num2 > 0 ? num2 + "" : 0 + "");
        //收藏数
        userCollectionNum.setText(num3 > 0 ? num3 + "" : 0 + "");
        //浏览量
        userReadNum.setText(num4 > 0 ? num4 + "" : 0 + "");
        //判断签到
        if (num5 == 0) {
            userSignInImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.r2));
            userSignInImg.setEnabled(true);
        } else {
            userSignInImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.yqd));
            userSignInImg.setEnabled(false);
        }
        Log.e(Constan.LOGTAGNAME,"---积分--"+num6);
        userPointTv.setText("积分：" + num6);
    }
}
