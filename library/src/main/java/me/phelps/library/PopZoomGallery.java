package me.phelps.library;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.util.ArrayList;

/**
 * Created by phelps on 2015/3/9.
 */
public class PopZoomGallery extends PopupWindow {

    private Context mContext;
    private ZoomGallery mGallery;

    private ArrayList<ZoomImageModel> mZoomImageList;

    private ZoomGalleryAdapter.ZoomGalleryInstantiateItem mItemListener;

    public PopZoomGallery(Context context, ArrayList<ZoomImageModel> zoomImageList,ZoomGalleryAdapter.ZoomGalleryInstantiateItem itemListener) {
        super(context);
        mContext = context;
        mZoomImageList = zoomImageList;
        mItemListener = itemListener;
        init();
    }

    private void init() {
        initView();
        setupPop();
    }

    private void initView() {
        mGallery = new ZoomGallery(mContext,this,mItemListener);

    }

    private void setupPop(){
        setContentView(mGallery);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());// 给popupWindow设置背景
        setAnimationStyle(0);
    }

    //显示
    public void showPop(View view,int position){
        showAtLocation(view, Gravity.BOTTOM,0,0);

        mGallery.open(mZoomImageList,position);

    }

}
