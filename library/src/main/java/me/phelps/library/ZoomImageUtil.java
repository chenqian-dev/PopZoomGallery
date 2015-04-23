package me.phelps.library;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class ZoomImageUtil {

    public static final int DURATION = 300;

    //开始缩放动画
    public static void zoomImageFromThumb(final Rect thumbRect, View background,View destView) {

        final Rect startBounds = thumbRect;
        final Rect finalBounds = new Rect();
        Point globalOffset = new Point();
        destView.getGlobalVisibleRect(finalBounds,globalOffset);

        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale = set_Center_crop(startBounds,finalBounds);

        destView.setPivotX(0);
        destView.setPivotY(0);

        AnimatorSet set = new AnimatorSet();
        set.play(
                ObjectAnimator.ofFloat(destView, "x", startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(destView, "y", startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(destView, "scaleX", startScale, 1f))
                .with(ObjectAnimator.ofFloat(destView, "scaleY", startScale, 1f));

        set.setDuration(DURATION);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();

        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(background,"alpha",0f,1.0f);
        alphaAnim.setDuration(DURATION);
        alphaAnim.setInterpolator(new DecelerateInterpolator());
        alphaAnim.start();
    }

    private static float set_Center_crop(Rect startBounds, Rect finalBounds) {
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height() > (float)startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }
        return startScale;
    }

    //关闭缩放动画
    public static void closeZoomAnim(Rect thumbRect, View background, View destView,Animator.AnimatorListener animatorListener) {

        AnimatorSet set = new AnimatorSet();

        final Rect finalBounds = thumbRect;
        final Rect startBounds = new Rect();
        Point globalOffset = new Point();

        destView.getGlobalVisibleRect(startBounds,globalOffset);

        float startScaleFinal = set_Center_crop(finalBounds,startBounds);

        destView.setPivotX(0);
        destView.setPivotY(0);

        if(thumbRect.left == 0 && thumbRect.top == 0 && thumbRect.right == 0&& thumbRect.bottom == 0){
            ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(destView,"alpha",1f,0f);
            alphaAnim.setDuration(DURATION);
            alphaAnim.setInterpolator(new DecelerateInterpolator());
            alphaAnim.start();
        }else {
            set.play(ObjectAnimator.ofFloat(destView, "x", finalBounds.left))
                    .with(ObjectAnimator.ofFloat(destView, "y", finalBounds.top))
                    .with(ObjectAnimator.ofFloat(destView, "scaleX", startScaleFinal))
                    .with(ObjectAnimator.ofFloat(destView, "scaleY", startScaleFinal));

            set.setDuration(DURATION);
            set.setInterpolator(new DecelerateInterpolator());
            set.start();
        }

        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(background,"alpha",1f,0f);
        alphaAnim.setDuration(DURATION);
        alphaAnim.setInterpolator(new DecelerateInterpolator());
        alphaAnim.addListener(animatorListener);
        alphaAnim.start();
    }
}
