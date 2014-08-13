package com.kuxinwei.library.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.view.animation.LinearInterpolator;

/**
 * Created by kuxinwei on 2014/8/13.
 */
public class TitanicViewAnimation {

	private AnimatorSet animatorSet;
	private Animator.AnimatorListener animatorListener;

	public Animator.AnimatorListener getAnimatorListener() {
		return animatorListener;
	}

	public void setAnimatorListener(Animator.AnimatorListener animatorListener) {
		this.animatorListener = animatorListener;
	}

	public void start(final TitanicView view) {

		final Runnable animate = new Runnable() {
			@Override
			public void run() {

				view.setSinking(true);

				// horizontal animation. 200 = wave.png width
				ObjectAnimator maskXAnimator = ObjectAnimator.ofFloat(view, "maskX", 0, 200);
				maskXAnimator.setRepeatCount(ValueAnimator.INFINITE);
				maskXAnimator.setDuration(1000);
				maskXAnimator.setStartDelay(0);

				int h = view.getHeight();

				// vertical animation
				// maskY = 0 -> wave vertically centered
				// repeat mode REVERSE to go back and forth
				ObjectAnimator maskYAnimator = ObjectAnimator.ofFloat(view, "maskY", h/2, 0);
				maskYAnimator.setRepeatCount(ValueAnimator.INFINITE);
				maskYAnimator.setRepeatMode(ValueAnimator.REVERSE);
				maskYAnimator.setDuration(10000);
				maskYAnimator.setStartDelay(0);

				// now play both animations together
				animatorSet = new AnimatorSet();
				animatorSet.playTogether(maskXAnimator, maskYAnimator);
				animatorSet.playTogether(maskXAnimator);
				animatorSet.setInterpolator(new LinearInterpolator());
				animatorSet.addListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						view.setSinking(false);

						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
							view.postInvalidate();
						} else {
							view.postInvalidateOnAnimation();
						}

						animatorSet = null;
					}

					@Override
					public void onAnimationCancel(Animator animation) {

					}

					@Override
					public void onAnimationRepeat(Animator animation) {

					}
				});


				if (animatorListener != null) {
					animatorSet.addListener(animatorListener);
				}

				animatorSet.start();
			}
		};

		if (!view.isSetUp()) {
			view.setAnimationSetupCallback(new TitanicView.AnimationSetupCallback() {
				@Override
				public void onSetupAnimation(final TitanicView target) {
					animate.run();
				}
			});
		} else {
			animate.run();
		}
	}

	public void cancel() {
		if (animatorSet != null) {
			animatorSet.cancel();
		}
	}
}
