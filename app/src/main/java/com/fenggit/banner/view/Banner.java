package com.fenggit.banner.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.fenggit.banner.R;

import java.util.List;

/**
 * Author: river
 * Date: 2017/4/24 18:11
 * Description: 方式二：无限循环广告轮播图(不推荐，伪循环滑动)
 */
public class Banner extends FrameLayout {
    private ViewPager mViewPager;
    private LinearLayout mLayoutDots;

    private BannerAdapter mBannerAdapter;

    /**
     * 图片链接
     */
    private List<String> mNetImage;


    public Banner(Context context) {
        super(context);
        intViews();
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        intViews();
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intViews();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Banner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        intViews();
    }


    private void intViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.banner, this, true);
        mViewPager = (ViewPager) view.findViewById(R.id.banner_pager);
        mLayoutDots = (LinearLayout) view.findViewById(R.id.banner_dots);
    }

    /**
     * 设置图片数据
     *
     * @param imageUrl
     */
    public void setNetImage(List<String> imageUrl) {
        this.mNetImage = imageUrl;
        initViewPager();
        if (imageUrl.size() > 1) {
            createDots();
        }
    }


    private void initViewPager() {
        mBannerAdapter = new BannerAdapter();
        mViewPager.setAdapter(mBannerAdapter);
        // 设置起始位置在中间
        mViewPager.setCurrentItem(1000000 * mNetImage.size());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateDot(position % mNetImage.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 创建指示点
     */
    private void createDots() {
        for (int i = 0, size = mNetImage.size(); i < size; i++) {
            ImageView dot = new ImageView(getContext());
            if (i == 0) {
                dot.setImageResource(R.mipmap.ic_point_select);
            } else {
                dot.setImageResource(R.mipmap.ic_point_normal);
            }
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.setMargins(10, 0, 0, 0);
            mLayoutDots.addView(dot, p);
        }
    }

    /**
     * 更新指示点
     *
     * @param position
     */
    private void updateDot(int position) {
        for (int i = 0, size = mLayoutDots.getChildCount(); i < size; i++) {
            ImageView dot = (ImageView) mLayoutDots.getChildAt(i);
            if (position == i) {
                dot.setImageResource(R.mipmap.ic_point_select);
            } else {
                dot.setImageResource(R.mipmap.ic_point_normal);
            }
        }
    }

    public class BannerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            // 设置无限循环，只有1张图片不循环
            if (mNetImage.size() > 1) {
                return Integer.MAX_VALUE;
            }

            return mNetImage.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(getContext());
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // 加载图片
            Glide.with(getContext()).load(mNetImage.get(position % mNetImage.size())).into(image);

            container.addView(image);

            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            if (object != null) {
                object = null;
            }
        }
    }
}
