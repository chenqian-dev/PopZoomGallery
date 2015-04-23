package me.phelps.zoomgallery;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import me.phelps.library.RectImageView;

/**
 * Created by apple on 15/4/23.
 */
public class GridViewAdapter extends BaseAdapter {

    private ArrayList<String> imageList;

    public void update(ArrayList<String> list){
        imageList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageList == null?0:imageList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View currentView, ViewGroup parent) {
        if(currentView == null){
            currentView = new RectImageView(parent.getContext());
        }

        Glide.with(parent.getContext())
                .load(imageList.get(position))
                .centerCrop()
                .placeholder(R.drawable.image_stub)
                .crossFade()
                .into((ImageView) currentView);



        return currentView;
    }
}
