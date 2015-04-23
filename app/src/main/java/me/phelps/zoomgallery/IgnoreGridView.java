package me.phelps.zoomgallery;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class IgnoreGridView extends GridView {
	public IgnoreGridView(Context context) {
		super(context);
	}

	public IgnoreGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
