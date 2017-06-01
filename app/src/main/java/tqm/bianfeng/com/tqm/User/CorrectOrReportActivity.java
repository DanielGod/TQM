package tqm.bianfeng.com.tqm.User;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.ShowDialogAndLoading;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ErrorReport;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;

/**
 * Created by johe on 2017/5/9.
 */
//举报纠错
public class CorrectOrReportActivity extends BaseActivity {

    public static final String objectId = "objectId";
    public static final String objectModule = "objectModule";
    public static final String objectTitle = "objectTitle";


    @BindView(R.id.correct_report_toolbar)
    Toolbar correctReportToolbar;
    @BindView(R.id.feedback_txt_edi)
    EditText feedbackTxtEdi;
    @BindView(R.id.feedback_commit)
    Button feedbackCommit;

    ErrorReport errorReport;
    @BindView(R.id.report_radio)
    RadioButton reportRadio;
    @BindView(R.id.correct_radio)
    RadioButton correctRadio;

    ShowDialogAndLoading showDialogAndLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_report);
        ButterKnife.bind(this);
        setToolbar(correctReportToolbar, "纠错举报");
        errorReport=new ErrorReport();
        errorReport.setObjectId(getIntent().getIntExtra(objectId, 0));
        if (realm.where(User.class).findFirst() != null) {
            errorReport.setUserId(realm.where(User.class).findFirst().getUserId());
        }
        errorReport.setObjectModule(getIntent().getStringExtra(objectModule));
        errorReport.setObjectTitle(getIntent().getStringExtra(objectTitle));
        reportRadio.setChecked(true);
        errorReport.setType("02");
        showDialogAndLoading= ShowDialogAndLoading.Show.showDialogAndLoading;
        showDialogAndLoading.setmLinsener(new ShowDialogAndLoading.Linsener() {
            @Override
            public void showBefore() {

            }

            @Override
            public void showAfter() {
                onBackPressed();
            }
        });

    }

    @OnClick({R.id.report_radio, R.id.correct_radio,R.id.feedback_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.report_radio:
                errorReport.setType("02");
                break;
            case R.id.correct_radio:
                errorReport.setType("01");
                break;
            case R.id.feedback_commit:
                if(feedbackTxtEdi.getText().toString().equals("")){
                    feedbackTxtEdi.setError("请填入纠错/举报内容");
                }else {
                    save();
                }
                break;
        }
    }
    public void save(){
        Gson gson=new Gson();
        errorReport.setContent(feedbackTxtEdi.getText().toString());
        showDialogAndLoading.showLoading("正在提交。。。",this);
        String json=gson.toJson(errorReport);
        Log.i("gqf","json"+json);
        Subscription subscription = NetWork.getUserService().saveErrorReport(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","onError"+e.toString());
                        showDialogAndLoading.stopLoaading();
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {
                        Log.i("gqf","onNext"+resultCode.toString());
                        showDialogAndLoading.stopLoaading();
                        if(resultCode.getCode()==ResultCode.SECCESS){
                            showDialogAndLoading.showAfterDialog(CorrectOrReportActivity.this,"提交成功","十分感谢您的反馈","确定");
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }




}
