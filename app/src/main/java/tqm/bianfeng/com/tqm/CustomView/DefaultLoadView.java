package tqm.bianfeng.com.tqm.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import tqm.bianfeng.com.tqm.R;

/**
 * Created by johe on 2017/5/15.
 */

public class DefaultLoadView extends LinearLayout {
    LoadingIndicator indicator;
    TextView LodingTxt;
    public DefaultLoadView(Context context) {
        super(context, null);
    }

    public DefaultLoadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View view= LayoutInflater.from(context).inflate( R.layout.loading_layout, null, false);
        indicator=(LoadingIndicator) view.findViewById(R.id.indicator);
        LodingTxt=(TextView) view.findViewById(R.id.loding_txt);
        LodingTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Daniel","点击刷新");
            }
        });
        addView(view);

    }

    public void lodingIsFailOrSucess(int i) {
        if(LodingTxt!=null&&indicator!=null) {
            if (i == 1) {
                //加载中
                LodingTxt.setVisibility(View.VISIBLE);
                LodingTxt.setText("");
                indicator.showLoading();
            } else if (i == 2) {
                //加载成功
                //借书动画
                LodingTxt.setVisibility(View.GONE);
                indicator.hideLoading();
                Log.i("gqf", "onClose2");
            } else if (i == 3) {
                //没有数据
                LodingTxt.setVisibility(View.VISIBLE);
                LodingTxt.setText("没有查询到数据");
                //YBJLoding.setImageResource(R.drawable.ic_no_city);
                indicator.hideLoading();
                LodingTxt.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_no_city), null, null);
                Log.i("gqf", "onClose3");
            } else {

            }
        }
    }
}
