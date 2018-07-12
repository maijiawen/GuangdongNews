package com.example.guangdongnews.activity;

import android.app.Activity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.guangdongnews.R;

import uk.co.senab.photoview.PhotoView;

public class ShowImageActivity extends Activity {

    private String url;
    private RequestOptions options;//glide加载图片时默认显示的图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimage);

        //Glide加载图片或者加载失败时默认显示的图片
        options = new RequestOptions()
                .placeholder(R.drawable.news_pic_default).error(R.drawable.news_pic_default);
        url = getIntent().getStringExtra("url");

        PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);

//        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
        Glide.with(this).load(url).apply(options).into(photoView);
    }
}
