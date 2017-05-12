package tqm.bianfeng.com.tqm.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by johe on 2017/5/12.
 */

public class SliderBottomView extends View {

    private Paint mPaint;
    private Path mPath;
    private int mItemWaveLength = 20;
    public SliderBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mItemWaveLength=getHeight();
        mPath.reset();
        int halfWaveLen = mItemWaveLength/2;
        mPath.moveTo(0,getHeight());

        mPath.rQuadTo(getWidth()/4,-getHeight(),getWidth()/2,-getHeight());
        mPath.rQuadTo(getWidth()/4,0,getWidth()/2,getHeight());
        mPath.lineTo(0,getHeight());

        mPath.close();
        Log.i("gqf",getWidth()+"onDraw"+getHeight());
        canvas.drawPath(mPath,mPaint);
    }
}
