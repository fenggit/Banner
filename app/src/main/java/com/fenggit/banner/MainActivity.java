package com.fenggit.banner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fenggit.banner.view.Banner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Banner mBanner;
    com.fenggit.banner.view2.Banner mBanner02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> imgesUrl = new ArrayList<>();
        imgesUrl.add("http://ooyeq5vjz.bkt.clouddn.com/banner_01.jpg");
        imgesUrl.add("http://ooyeq5vjz.bkt.clouddn.com/banner_02.jpg");
        imgesUrl.add("http://ooyeq5vjz.bkt.clouddn.com/banner_03.jpg");

        mBanner = (Banner) findViewById(R.id.banner);
        mBanner.setNetImage(imgesUrl);

        mBanner02 = (com.fenggit.banner.view2.Banner) findViewById(R.id.banner2);
        mBanner02.setNetImage(imgesUrl);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mBanner02.stop();
    }
}
