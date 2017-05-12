package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func4;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.pojo.YwApplyEnter;

/**
 * Created by johe on 2017/5/12.
 */

public class ApplyForPersonalFragment extends BaseFragment {

    YwApplyEnter ywApplyEnter;
    @BindView(R.id.private_capital_radio)
    RadioButton privateCapitalRadio;
    @BindView(R.id.mediation_radio)
    RadioButton mediationRadio;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.phone_num_edi)
    EditText phoneNumEdi;
    @BindView(R.id.id_num_edi)
    EditText idNumEdi;

    int selectRadio = 0;
    @BindView(R.id.user_first_name)
    EditText userFirstName;

    @OnClick({R.id.private_capital_radio, R.id.mediation_radio})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.private_capital_radio:
                selectRadio = 0;
                break;
            case R.id.mediation_radio:
                selectRadio = 1;
                break;
        }
    }


    public interface mListener {
        public void setCommitBtn(boolean is);
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
        View view = inflater.inflate(R.layout.fragment_apply_for_personal, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ywApplyEnter = new YwApplyEnter();
        privateCapitalRadio.setChecked(true);
        iniEdi();
    }

    public void iniEdi() {
        Observable<CharSequence> CharSequence1 = RxTextView.textChanges(userName).skip(1);
        Observable<CharSequence> CharSequence2 = RxTextView.textChanges(phoneNumEdi).skip(1);
        Observable<CharSequence> CharSequence3 = RxTextView.textChanges(idNumEdi).skip(1);
        Observable<CharSequence> CharSequence4 = RxTextView.textChanges(userFirstName).skip(1);
        Subscription etSc = Observable.combineLatest(CharSequence1, CharSequence2, CharSequence3,CharSequence4, new Func4<CharSequence, CharSequence, CharSequence,CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3,CharSequence charSequence4) {
                boolean Bl = !TextUtils.isEmpty(charSequence);
                boolean B2 = !TextUtils.isEmpty(charSequence2);
                boolean B3 = !TextUtils.isEmpty(charSequence3);
                boolean B4 = !TextUtils.isEmpty(charSequence4);
                return Bl && B2 && B3 && B4;
            }
        }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @DebugLog
            @Override
            public void onNext(Boolean aBoolean) {
                mListener.setCommitBtn(aBoolean);
            }
        });
        compositeSubscription.add(etSc);
    }

    public YwApplyEnter getYwApplyEnter() {

        if (selectRadio == 0) {
            ywApplyEnter.setApplyType("2001");
        } else {
            ywApplyEnter.setApplyType("2002");
        }
        ywApplyEnter.setApplyName(userFirstName.getText().toString());
        ywApplyEnter.setProposer(userName.getText().toString());
        ywApplyEnter.setContact(phoneNumEdi.getText().toString());
        ywApplyEnter.setIdCard(idNumEdi.getText().toString());

        return ywApplyEnter;
    }
    public void setYwApplyEnter(YwApplyEnter data){
        ywApplyEnter=data;
        if(ywApplyEnter.getApplyType().equals("2001")){
            privateCapitalRadio.setChecked(true);
        }else{
            mediationRadio.setChecked(true);
        }
        ywApplyEnter.setAuditCode("00");
        userFirstName.setText(ywApplyEnter.getApplyName());
        userName.setText(ywApplyEnter.getProposer());
        phoneNumEdi.setText(ywApplyEnter.getContact());
        idNumEdi.setText(ywApplyEnter.getIdCard());


    }
}
