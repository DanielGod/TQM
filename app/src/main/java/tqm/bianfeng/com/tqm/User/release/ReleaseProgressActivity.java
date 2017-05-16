package tqm.bianfeng.com.tqm.User.release;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.pojo.ReleaseActivityItem;
import tqm.bianfeng.com.tqm.pojo.ReleaseLoanItem;

/**
 * Created by johe on 2017/5/16.
 */

public class ReleaseProgressActivity extends BaseActivity {


    public static final int activity_type = 0;
    public static final int loan_type = 1;
    public static final String RELEASE_TYPE = "release_type";
    public static final String RELEASE_JSON = "release_json";
    public int releseType;

    ReleaseActivityItem releaseActivityItem;
    ReleaseLoanItem releaseLoanItem;
    Gson gson;

    ReleaseLoanFragment releaseLoanFragment;
    ReleaseActivityFragment releaseActivityFragment;
    @BindView(R.id.release_progress_toolbar)
    Toolbar releaseProgressToolbar;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.no_audit_view)
    View noAuditView;
    @BindView(R.id.audit_end_img)
    ImageView auditEndImg;
    @BindView(R.id.audit_end_txt)
    TextView auditEndTxt;
    @BindView(R.id.audit_end_lin)
    LinearLayout auditEndLin;
    @BindView(R.id.audit_remark_txt)
    TextView auditRemarkTxt;
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.no_audit_lin)
    LinearLayout noAuditLin;
    @BindView(R.id.apply_statu_scroll)
    ScrollView applyStatuScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_progress);
        ButterKnife.bind(this);
        releseType = getIntent().getIntExtra(RELEASE_TYPE, -1);
        gson=new Gson();
        if (releseType == 0) {
            releaseActivityFragment = new ReleaseActivityFragment();
            releaseActivityItem=gson.fromJson(getIntent().getStringExtra(RELEASE_JSON),ReleaseActivityItem.class);
            releaseActivityFragment.setReleaseActivityItem(releaseActivityItem);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, releaseActivityFragment).commit();
        } else if (releseType == 1) {
            releaseLoanFragment = new ReleaseLoanFragment();
            releaseLoanItem=gson.fromJson(getIntent().getStringExtra(RELEASE_JSON),ReleaseLoanItem.class);
            releaseLoanFragment.setReleaseLoanItem(releaseLoanItem);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, releaseLoanFragment).commit();
        }


    }

}
