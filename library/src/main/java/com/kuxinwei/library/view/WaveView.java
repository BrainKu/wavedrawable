package com.kuxinwei.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kuxinwei on 2014/8/13.
 */
public class WaveView extends View {

    private WaveDrawable mWaveDrawable;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mWaveDrawable = new WaveDrawable(getContext());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWaveDrawable.draw(canvas);
        mWaveDrawable.setCallback(this);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (View.VISIBLE == visibility) {
            mWaveDrawable.start();
        } else {
            mWaveDrawable.stop();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWaveDrawable.setBounds(0, 0, w, h);
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return mWaveDrawable == who || super.verifyDrawable(who);
    }
}
