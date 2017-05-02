package tqm.bianfeng.com.tqm.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.CustomView.MyScrollview;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;

/**
 * Created by johe on 2017/5/2.
 */

public class DemoFragemnt extends BaseFragment {


    @BindView(R.id.home_slider)
    SliderLayout homeSlider;
    @BindView(R.id.my_scrollview)
    MyScrollview myScrollview;

    int scrollHeight = 0;
    int sliderHeight = 1;

    public interface mListener {

        public void setToolBarColorBg(int a);
    }

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_demo, container, false);
        ButterKnife.bind(this, view);
        DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
        textSliderView.image(R.drawable.home_top_slider1)
                .setScaleType(BaseSliderView.ScaleType.Fit);
        homeSlider.addSlider(textSliderView);
        myScrollview.setOnScollChangedListener(new MyScrollview.OnScollChangedListener() {
            @Override
            public void onScrollChanged(MyScrollview scrollView, int x, int y, int oldx, int oldy) {
                scrollHeight = y;
                if (sliderHeight != 1) {
                    mListener.setToolBarColorBg(getAlph(scrollHeight, sliderHeight));

                }
            }
        });
        return view;
    }

    public int getAlph(int h1, int h2) {
        if (h1 > h2) {
            return 255;
        } else {
            return (int) ((float) h1 / h2 * 255);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewTreeObserver vto = homeSlider.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                homeSlider.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sliderHeight = homeSlider.getHeight();


            }
        });
        mListener.setToolBarColorBg(getAlph(scrollHeight, sliderHeight));

    }
}
