package tqm.bianfeng.com.tqm.User;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/5/9.
 */

public class CorrectOrReportActivity extends BaseActivity {

    @BindView(R.id.correct_report_toolbar)
    Toolbar correctReportToolbar;
    @BindView(R.id.feedback_txt_edi)
    EditText feedbackTxtEdi;
    @BindView(R.id.feedback_commit)
    Button feedbackCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_report);
        ButterKnife.bind(this);
        setToolbar(correctReportToolbar, "纠错举报");


    }

    @OnClick({R.id.report_radio, R.id.correct_radio})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.report_radio:
                break;
            case R.id.correct_radio:
                break;
        }
    }
}
