package tqm.bianfeng.com.tqm.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by johe on 2017/5/13.
 */

public class LoadMoreView extends TextView {

    public LoadMoreView(Context context) {
        super(context);

    }
    public LoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public LoadMoreView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    public void loadMoreViewAnim(int statu) {
        //操作加载更多动画

        this.setVisibility(View.VISIBLE);
        switch (statu) {
            case 1://动画开始
                this.setText("加载中...");
                break;
            case 2://加载完成恢复初始状态
                this.setText("加载更多");
                break;
            case 3://没有更多
                this.setText("没有更多");
                break;
            case 4://不显示
                this.setVisibility(View.GONE);
                break;
        }


    }

}
