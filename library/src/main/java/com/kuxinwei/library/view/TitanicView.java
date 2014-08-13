package com.kuxinwei.library.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by kuxinwei on 2014/8/13.
 */
public class TitanicView extends View {


	public void setSinking(boolean sinking) {
		this.sinking = sinking;
	}

	public boolean isSetUp() {
		return isSetup;
	}

	public void setAnimationSetupCallback(AnimationSetupCallback animationSetupCallback) {
		this.mAnimationSetupCallBack = animationSetupCallback;
	}

	public interface AnimationSetupCallback {
		public void onSetupAnimation(TitanicView mTitanicView);
	}

	private AnimationSetupCallback mAnimationSetupCallBack;

	private float maskX;

	public float getMaskY() {
		return maskY;
	}

	public void setMaskY(float maskY) {
		this.maskY = maskY;
		invalidate();
	}

	public float getMaskX() {
		return maskX;
	}

	public void setMaskX(float maskX) {
		this.maskX = maskX;
		invalidate();
	}

	public boolean isSetup() {
		return isSetup;
	}

	public void setSetup(boolean isSetup) {
		this.isSetup = isSetup;
	}

	public boolean isSinking() {
		return sinking;
	}

	private float maskY;

	private boolean sinking;

	private boolean isSetup;

	private BitmapShader mWaveShader;

	private Matrix mShaderMatrix;

	private Paint mPaint;

	private Drawable mWaveDrawable;

	public TitanicView(Context context) {
		this(context, null);
	}

	public TitanicView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TitanicView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mShaderMatrix = new Matrix();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.FILL);
		createShader();
	}

	private void createShader() {
		if (mWaveDrawable == null) {
			mWaveDrawable = getResources().getDrawable(R.drawable.wave);
		}

		int waveH = mWaveDrawable.getIntrinsicHeight();
		int waveW = mWaveDrawable.getIntrinsicWidth();

		Bitmap b = Bitmap.createBitmap(waveW, waveH, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		c.drawColor(Color.WHITE);

		mWaveDrawable.setBounds(0, 0, waveW, waveH);
		ColorFilter filter = new LightingColorFilter(Color.BLUE, 1);

		mWaveDrawable.setColorFilter(filter);
		mWaveDrawable.setAlpha(128);
		mWaveDrawable.draw(c);

		mWaveShader = new BitmapShader(b, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
		mPaint.setShader(mWaveShader);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		createShader();
		if (!isSetup) {
			isSetup = true;
			if (mAnimationSetupCallBack != null) {
				mAnimationSetupCallBack.onSetupAnimation(this);
			}
		}
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (sinking && mWaveShader != null) {
			mShaderMatrix.setTranslate(maskX, maskY);
			mWaveShader.setLocalMatrix(mShaderMatrix);
		} else {
			mPaint.setShader(null);
		}
		canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
	}


}

