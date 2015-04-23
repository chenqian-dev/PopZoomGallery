package me.phelps.library;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class ZoomGalleryAdapter extends PagerAdapter {

    private ArrayList<ZoomImageModel> mZoomImageList;

    private ZoomGalleryTapListener mListener;
    private ZoomGalleryInstantiateItem mItemListener;

    public ZoomGalleryAdapter(ZoomGalleryTapListener listener,ZoomGalleryInstantiateItem itemListener){
        mListener = listener;
        mItemListener = itemListener;
    }

    public void update(ArrayList<ZoomImageModel> zoomImageList){
        mZoomImageList = zoomImageList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mZoomImageList == null?0:mZoomImageList.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {

        PhotoView imageView = new PhotoView(container.getContext());
        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v2) {
                if (mListener != null) {
                    mListener.tap(position);
                }
            }
        });
        imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v2) {
                if (mListener != null) {
                    mListener.tap(position);
                }
            }
        });

        if(mItemListener != null){
            mItemListener.onItemInstantiate(container,position,imageView,mZoomImageList.get(position));
        }

        container.addView(imageView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public interface ZoomGalleryTapListener{
        public void tap(int position);
    }

    public interface ZoomGalleryInstantiateItem{
        void onItemInstantiate(ViewGroup container, final int position, PhotoView view,ZoomImageModel model);
    }

}
