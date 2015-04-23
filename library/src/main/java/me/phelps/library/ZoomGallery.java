package me.phelps.library;

import android.animation.Animator;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import java.util.ArrayList;


public class ZoomGallery extends RelativeLayout{

    private PopZoomGallery mPop;

    private ViewPager mViewPager;
    private ImageView mBackground;
    private PointIndicator mIndicator;

    private ZoomGalleryAdapter mAdapter;

    private ArrayList<ZoomImageModel> mZoomImageList;

    private ZoomGalleryAdapter.ZoomGalleryInstantiateItem mItemListener;

    public ZoomGallery(Context context,PopZoomGallery popZoomGallery, ZoomGalleryAdapter.ZoomGalleryInstantiateItem itemListener) {
        super(context);
        mPop = popZoomGallery;
        mItemListener = itemListener;
        init(context);
    }

    private void init(Context context){
        //设置背景
        mBackground = new ImageView(getContext());
        mBackground.setBackgroundColor(getResources().getColor(R.color.black_tttt));
        LayoutParams layoutParams1 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        addView(mBackground,layoutParams1);

        //view pager
        mViewPager = new HackyViewPager(context);
        LayoutParams layoutParams2 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        addView(mViewPager,layoutParams2);
        mAdapter = new ZoomGalleryAdapter(new ZoomGalleryAdapter.ZoomGalleryTapListener() {
            @Override
            public void tap(int position) {
                close();
            }
        },mItemListener);
        mViewPager.setAdapter(mAdapter);

        //indicator
        mIndicator = new PointIndicator(context);
        mIndicator.setViewPager(mViewPager);
        LayoutParams layoutParams3 = new LayoutParams(LayoutParams.MATCH_PARENT,ScreenUtil.dip2px(context,8));
        layoutParams3.bottomMargin = ScreenUtil.dip2px(context,40);
        layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(mIndicator,layoutParams3);
    }

    public void open(ArrayList<ZoomImageModel> zoomImageList, final int position){
        mZoomImageList = zoomImageList;
        mAdapter.update(mZoomImageList);
        mIndicator.notifyDataSetChanged();
        mViewPager.setCurrentItem(position);

        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                ZoomImageUtil.zoomImageFromThumb(mZoomImageList.get(position).rect, mBackground, mViewPager);
            }
        });
    }

    public void close(){
        ZoomImageUtil.closeZoomAnim(mZoomImageList.get(mViewPager.getCurrentItem()).rect, mBackground, mViewPager, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                if(mPop != null){
                    try {
                        mPop.dismiss();
                    }catch (Exception e){}
                }
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

    }

}
