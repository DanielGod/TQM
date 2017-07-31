package tqm.bianfeng.com.tqm.User.order;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import tqm.bianfeng.com.tqm.R;


/**
 * Created by Daniel on 2016/12/6.
 */

public class ApplyDialogFragment extends DialogFragment  {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_dialog, null);

        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        return view;
    }



}
