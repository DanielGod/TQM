package tqm.bianfeng.com.tqm.CustomView;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import tqm.bianfeng.com.tqm.R;

/**
 * Created by johe on 2017/3/13.
 */

public class ToastType {
    Toast toast;
    public void showToastWithImg(Context context,boolean isSuccess,String msg){
        toast = Toast.makeText(context,
                msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(context);
        if(isSuccess){
            imageCodeProject.setImageResource(R.drawable.ic_successful);
        }else{
            imageCodeProject.setImageResource(R.drawable.ic_failure);
        }
        toastView.addView(imageCodeProject, 0);
        toast.show();
    }

}
