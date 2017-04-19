package tqm.bianfeng.com.tqm.Institutions.listener;

import android.support.design.widget.AppBarLayout;
import android.util.Log;

/**
 * Created by johe on 2017/4/10.
 */

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        Log.i("gqf",i+"--onOffsetChanged"+appBarLayout.getTotalScrollRange()+appBarLayout.getHeight());
        if (i >- appBarLayout.getTotalScrollRange()/3*2) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else  {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}
