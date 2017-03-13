package tqm.bianfeng.com.tqm.User;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.R;

/**
 * Created by johe on 2017/3/13.
 */

public class UserFeedbackActivity extends AppCompatActivity {

    Realm realm;
    CompositeSubscription compositeSubscription;
    @BindView(R.id.my_initiate_sign_toolbar)
    Toolbar myInitiateSignToolbar;
    @BindView(R.id.phone_num_edi)
    EditText phoneNumEdi;
    @BindView(R.id.feedback_txt_edi)
    EditText feedbackTxtEdi;
    @BindView(R.id.feedback_commit)
    Button feedbackCommit;

    private void setToolbar(String toolstr) {
        myInitiateSignToolbar.setTitle(toolstr);
        myInitiateSignToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myInitiateSignToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        setSupportActionBar(myInitiateSignToolbar);
        myInitiateSignToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        compositeSubscription = new CompositeSubscription();
        setToolbar("用户反馈");
        initBtn();
    }

    public void initBtn(){
        Observable<CharSequence> usernameOs = RxTextView.textChanges(phoneNumEdi).skip(1);
        Observable<CharSequence> passwordOs = RxTextView.textChanges(feedbackTxtEdi).skip(1);
        Subscription etSc = Observable.combineLatest(usernameOs, passwordOs, new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2) {
                boolean usernameBl = !TextUtils.isEmpty(charSequence);
                boolean passwordBl = !TextUtils.isEmpty(charSequence2);
                if (!usernameBl) {
                    phoneNumEdi.setError("请您的手机号");
                } else {
                    phoneNumEdi.setError(null);
                }

                if (!passwordBl)
                    feedbackTxtEdi.setError("请您的意见或者建议");
                else
                    feedbackTxtEdi.setError(null);

                return usernameBl && passwordBl;
            }
        }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

                feedbackCommit.setEnabled(aBoolean);
            }
        });

        compositeSubscription.add(etSc);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        compositeSubscription.unsubscribe();
    }

    @OnClick(R.id.feedback_commit)
    public void onClick() {
        //提交反馈

    }
}
