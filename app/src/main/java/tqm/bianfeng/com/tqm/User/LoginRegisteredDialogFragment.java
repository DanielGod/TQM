package tqm.bianfeng.com.tqm.User;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tqm.bianfeng.com.tqm.R;

/**
 * Created by johe on 2017/3/13.
 */

public class LoginRegisteredDialogFragment extends DialogFragment {


    @BindView(R.id.close_fragment)
    ImageButton closeFragment;
    @BindView(R.id.phone_num_edi)
    EditText phoneNumEdi;
    @BindView(R.id.get_verification_code_btn)
    Button getVerificationCodeBtn;
    @BindView(R.id.verification_code_edi)
    EditText verificationCodeEdi;
    @BindView(R.id.login_registered_btn)
    Button loginRegisteredBtn;

    String phoneNum="";

    boolean isRun=true;
    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @OnClick({R.id.close_fragment, R.id.get_verification_code_btn, R.id.login_registered_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_fragment:
                //关闭
                closeDialog();
                break;
            case R.id.get_verification_code_btn:
                //获取验证码
                dimssLinsener.getCode(phoneNumEdi.getText().toString(),true);
                timeRun();
                break;
            case R.id.login_registered_btn:
                //提交
                dimssLinsener.onOk(phoneNumEdi.getText().toString(),verificationCodeEdi.getText().toString());
                break;
        }
    }

    public void closeDialog(){
        isRun=false;
        dimssLinsener.fragmentDimss();
        this.dismiss();
    }

    public void timeRun(){
        getVerificationCodeBtn.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=120;
                while(i>=0&&isRun){
                    i--;
                    Message msg=new Message();
                    msg.what=1;
                    msg.arg1=i;
                    mHandler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    }catch (Exception e){

                    }
                }
            }
        }).start();
    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                if(msg.arg1==0){
                    getVerificationCodeBtn.setEnabled(true);
                    getVerificationCodeBtn.setText("点击获取验证码");
                    dimssLinsener.getCode("",false);
                }else{
                    getVerificationCodeBtn.setText("重试（"+msg.arg1+"）");
                }
            }
        }
    };

    public interface DimssLinsener {
        public void fragmentDimss();
        public void onOk(String ediTxt,String code);
        public void getCode(String phone,boolean isGet);
    }

    DimssLinsener dimssLinsener;

    public void setDimssLinsener(DimssLinsener dimssLinsene) {
        dimssLinsener = dimssLinsene;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.Transparent);
        View view = inflater.inflate(R.layout.fragment_login_registered, container);
        ButterKnife.bind(this, view);
        phoneNumEdi.setText(phoneNum);
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        dimssLinsener.getCode("",false);
    }
}
