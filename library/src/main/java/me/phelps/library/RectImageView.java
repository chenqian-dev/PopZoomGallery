package me.phelps.library;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by phelps on 2015/3/9.
 */
public class RectImageView extends ImageView {

    public RectImageView(Context context) {
        this(context, null);
    }

    public RectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),getMeasuredWidth());
    }
}
