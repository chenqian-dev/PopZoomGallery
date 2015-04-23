package me.phelps.zoomgallery;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import me.phelps.library.PopZoomGallery;
import me.phelps.library.ZoomGalleryAdapter;
import me.phelps.library.ZoomImageModel;
import uk.co.senab.photoview.PhotoView;


public class MainActivity extends AppCompatActivity {

    private GridView mGridView;

    private GridViewAdapter mAdapter;

    private ArrayList<String> mImageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);


        mGridView = (GridView) findViewById(R.id.grid_view);
        mAdapter = new GridViewAdapter();
        mGridView.setAdapter(mAdapter);

        mImageList.add("http://h.hiphotos.baidu.com/image/pic/item/ac345982b2b7d0a213987e5cc9ef76094a369a99.jpg");
        mImageList.add("http://d.hiphotos.baidu.com/image/pic/item/0ff41bd5ad6eddc4aa295b333bdbb6fd53663328.jpg");
        mImageList.add("http://h.hiphotos.baidu.com/image/pic/item/ac345982b2b7d0a213987e5cc9ef76094a369a99.jpg");
        mImageList.add("http://d.hiphotos.baidu.com/image/pic/item/3c6d55fbb2fb4316365895b222a4462308f7d3b7.jpg");
        mImageList.add("http://c.hiphotos.baidu.com/image/pic/item/d1a20cf431adcbef546504c7aeaf2edda2cc9f99.jpg");
        mImageList.add("http://f.hiphotos.baidu.com/image/pic/item/caef76094b36acaf0bb1ec3d7ed98d1000e99c99.jpg");
        mImageList.add("http://b.hiphotos.baidu.com/image/w%3D230/sign=4cb875147f899e51788e3d1772a6d990/377adab44aed2e73f29620c58301a18b86d6fad8.jpg");
        mImageList.add("http://b.hiphotos.baidu.com/image/pic/item/8718367adab44aed2f8e87e3b11c8701a08bfbf0.jpg");
        mImageList.add("http://c.hiphotos.baidu.com/image/pic/item/d8f9d72a6059252d964f2503369b033b5bb5b958.jpg");


        mAdapter.update(mImageList);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<ZoomImageModel> zoomImageArrayList = new ArrayList<>();
                for (int i=0;i<mGridView.getCount();i++){
                    View child = mGridView.getChildAt(i);

                    ZoomImageModel imageScale = new ZoomImageModel();
                    if(child != null){
                        int[] xy = new int[2];
                        child.getLocationInWindow(xy);
                        Rect rect = new Rect(xy[0],xy[1],xy[0]+ child.getWidth(),xy[1]+child.getHeight());

                        imageScale.rect = rect;
                    }else {
                        imageScale.rect = new Rect();
                    }

                    imageScale.smallImagePath = mImageList.get(i);
                    imageScale.bigImagePath = mImageList.get(i);
                    zoomImageArrayList.add(imageScale);
                }

                PopZoomGallery popZoomGallery = new PopZoomGallery(MainActivity.this,zoomImageArrayList,new ZoomGalleryAdapter.ZoomGalleryInstantiateItem() {
                    @Override
                    public void onItemInstantiate(ViewGroup container, int position, PhotoView view, ZoomImageModel model) {
                        Glide.with(MainActivity.this)
                                .load(model.bigImagePath)
                                .centerCrop()
                                .into(view);
                    }
                });
                popZoomGallery.showPop(mGridView,position);
            }
        });
    }

}
