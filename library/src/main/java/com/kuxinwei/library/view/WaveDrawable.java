package com.kuxinwei.library.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by kuxinwei on 2014/8/13.
 * An drawable would show an wave drawable
 */
public class WaveDrawable extends Drawable implements Animatable {

    private static final Interpolator X_MASK_INTERPOLARTOR = new LinearInterpolator();
    private static final Interpolator Y_MASK_INTERPOLARTOR = new DecelerateInterpolator();
    private static final int X_ANIMATION_DURATION = 1000;
    private static final int Y_ANIMATION_DURATION = 10000;
    private ObjectAnimator mXObjectAnimator;
    private ObjectAnimator mYObjectAnimator;

    private Paint mPaint;
    private Drawable mWaveDrawable;
    private Matrix mWaveMatrix;
    private float maskX;
    private float maskY;

    private RectF fBounds = new RectF();

    private boolean isSinking;
    private BitmapShader mBitmapShader;

    public WaveDrawable(Context context) {
        isSinking = true;
        mWaveDrawable = context.getResources().getDrawable(R.drawable.wave);
        mWaveMatrix = new Matrix();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setUpBitmapShader();
        setUpAnimation();
    }

    /**
     * Set up the init bitmapshader.
     * <p>
     * Step 1: Get the wave bitmap's width and height, and create an empty bitmap
     * with {@link android.graphics.Bitmap#createBitmap(int, int, android.graphics.Bitmap.Config)}<br>
     * Step 2: Create a new Canvas with the empty bitmap.
     * Set the color of Canvas, then draw the wave bitmap on it
     * with {@link android.graphics.drawable.Drawable#draw(android.graphics.Canvas)} <br>
     * Step 3:Create a BitmapShader with privous created bitmap, with option {@code TileMode.REPEAT}
     * and {@code TileMode.CLAMP}<br>
     * Optional:
     * If you want to set the default color of wave, you can use {@code Drawable.setColorFilter()}
     * before you create BitmapShader.
     * </p>
     */
    private void setUpBitmapShader() {
        int mWaveH = mWaveDrawable.getIntrinsicHeight();
        int mWaveW = mWaveDrawable.getIntrinsicWidth();

        Bitmap b = Bitmap.createBitmap(mWaveW, mWaveH, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.drawColor(Color.WHITE);

        mWaveDrawable.setBounds(0, 0, mWaveW, mWaveH);
        ColorFilter cf = new LightingColorFilter(Color.BLUE, 1);
        mWaveDrawable.setColorFilter(cf);
        mWaveDrawable.setAlpha(127);
        mWaveDrawable.draw(c);
        mBitmapShader = new BitmapShader(b, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mPaint.setShader(mBitmapShader);
    }

    private void setUpAnimation() {
        setUpMaskXAnimtion();
        setUpMaskYAnimation(fBounds);
    }

    /**
     * Set up the animation in Y axis<br>
     * When the bounds of drawable changed, the object value of View will change according to new bounds
     *
     * @param fBounds
     */
    private void setUpMaskYAnimation(RectF fBounds) {
        float h;
        if (mYObjectAnimator == null) {
            h = mWaveDrawable.getIntrinsicHeight();
            mYObjectAnimator = ObjectAnimator.ofFloat(this, "maskY", h, -h / 2);
            mYObjectAnimator.setDuration(Y_ANIMATION_DURATION);
            mYObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mYObjectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            mYObjectAnimator.setInterpolator(Y_MASK_INTERPOLARTOR);
        } else {
            h = fBounds.bottom - fBounds.top;
            // Very import! If you want the wave can start from the start of screen
            // Remeber to minues the origin height of bitmap
            mYObjectAnimator.setFloatValues(h, -mWaveDrawable.getIntrinsicHeight() / 2);
        }
    }

    /**
     * Set up the animation in X axis
     */
    private void setUpMaskXAnimtion() {
        mXObjectAnimator = ObjectAnimator.ofFloat(this, "maskX", 0, 200);
        mXObjectAnimator.setDuration(X_ANIMATION_DURATION);
        mXObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mXObjectAnimator.setInterpolator(X_MASK_INTERPOLARTOR);
    }

    /**
     * Rember to change the current bound of bitmap, or the bitmap wouldn't show
     *
     * @param bounds
     */
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        fBounds.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
        setUpMaskYAnimation(fBounds);
    }

    @Override
    public void draw(Canvas canvas) {
        if (isSinking) {
            mWaveMatrix.setTranslate(maskX, maskY);
            mBitmapShader.setLocalMatrix(mWaveMatrix);
        } else {
            mPaint.setShader(null);
        }
        // draw the rect with the paint with bitmapshader
        canvas.drawRect(fBounds, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }


    public float getMaskX() {
        return maskX;
    }

    public void setMaskX(float maskX) {
        this.maskX = maskX;
        invalidateSelf();
    }

    public float getMaskY() {
        return maskY;
    }

    public void setMaskY(float maskY) {
        this.maskY = maskY;
        invalidateSelf();
    }

    public Paint getPaint() {
        return mPaint;
    }

    public boolean isSinking() {
        return isSinking;
    }

    public void setSinking(boolean isSinking) {
        this.isSinking = isSinking;
    }


    boolean mRunning = false;

    @Override
    public void start() {
        if (isRunning())
            return;
        mRunning = true;
        mXObjectAnimator.start();
        mYObjectAnimator.start();
        invalidateSelf();
    }

    @Override
    public void stop() {
        if (!isRunning())
            return;
        mRunning = false;
        mXObjectAnimator.cancel();
        mYObjectAnimator.cancel();
        invalidateSelf();
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }
}
