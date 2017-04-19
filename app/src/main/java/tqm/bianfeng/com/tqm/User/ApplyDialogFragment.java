package tqm.bianfeng.com.tqm.User;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tqm.bianfeng.com.tqm.R;

/**
 * Created by johe on 2017/4/13.
 */

public class ApplyDialogFragment extends DialogFragment {


    boolean isApplyForWell = false;
    @BindView(R.id.apply_for_dialog_lin)
    LinearLayout applyForDialogLin;

    public void setApplyForWell(boolean isApplyFor) {
        isApplyForWell = isApplyFor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_apply_for_result, container);
        ButterKnife.bind(this, view);
        return view;
    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mLinsener.OnDismiss();
    }

    @OnClick(R.id.apply_for_dialog_lin)
    public void onClick() {
        this.dismiss();

    }

    public interface MLinsener{
        public void OnDismiss();
    }
    MLinsener mLinsener;

    public void setmLinsener(MLinsener mLinsener) {
        this.mLinsener = mLinsener;
    }
}
