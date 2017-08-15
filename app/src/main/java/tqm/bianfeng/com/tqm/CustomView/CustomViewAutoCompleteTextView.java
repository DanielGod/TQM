package tqm.bianfeng.com.tqm.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by 王九东 on 2017/8/14.
 */

public class CustomViewAutoCompleteTextView extends AutoCompleteTextView {
    public CustomViewAutoCompleteTextView(Context context) {
        super(context);
    }

    public CustomViewAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
