package com.fenggit.banner.view2;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.fenggit.banner.R;

import java.util.LinkedList;
import java.util.List;


/**
 * Author: river
 * Date: 2017/4/24 18:11
 * Description: 方式二：无限循环广告轮播图（推荐）
 * 实现原理：通过在原数据上面，添加一头一尾数据
 * 比如数据为:
 * 真实数据：      0       1       2
 * 实际数据：2     0       1       2       0
 */
public class Banner extends FrameLayout {
    private ViewPager mViewPager;
    private LinearLayout mLayoutDots;

    /**
     * 图片链接
     */
    private LinkedList<String> mNetImage;
    /**
     * 显示的第一页
     */
    public static final int FIRST_PAGE = 1;
    /**
     * 当前滑动的位置
     */
    private int currentPosition = FIRST_PAGE;

    private Handler mHandler = new Handler();

    /**
     * 自动播放时间
     */
    private final static int mAutoPlayTime = 2000;
    /**
     * 是否自动轮播
     */
    private boolean isAutoPlay = true;

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
        this.mNetImage = new LinkedList<>();

        this.mNetImage.addAll(imageUrl);

        if (imageUrl.size() > 1) {
            // 添加2条数据,一头一尾
            this.mNetImage.addFirst(imageUrl.get(imageUrl.size() - 1));
            this.mNetImage.addLast(imageUrl.get(0));

            createDots();
        }

        initViewPager();

        if (imageUrl.size() > 1 && isAutoPlay) {
            startAutoPlay();
        }
    }


    private void initViewPager() {
        mViewPager.setAdapter(new BannerAdapter());
        mViewPager.setCurrentItem(FIRST_PAGE);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //  * 真实数据：      0       1       2
                //  * 实际数据：2     0       1       2       0
                //  最后一个0切换到前面的0页面位置
                if (position == mNetImage.size() - 1) {
                    currentPosition = FIRST_PAGE;

                } else if (position == 0) {
                    currentPosition = mNetImage.size() - 2;
                } else {
                    currentPosition = position;
                }

                // 指示点只有原数据的数目: 0       1       2
                updateDot(currentPosition - 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 页面滑动静止状态，偷天换日，换位置
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    // smoothScroll: 设置平稳滑动
                    mViewPager.setCurrentItem(currentPosition, false);
                }

            }
        });
    }

    /**
     * 创建指示点
     */
    private void createDots() {
        for (int i = 0, size = mNetImage.size() - 2; i < size; i++) {
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

    /**
     * 外部调用：开始轮播
     */
    public void start() {
        isAutoPlay = true;
        startAutoPlay();
    }

    /**
     * 外部调用： 停止轮播
     */
    public void stop() {
        isAutoPlay = false;
        stopAutoPlay();
    }

    /**
     * 开始轮播
     */
    private void startAutoPlay() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPosition++;

                mViewPager.setCurrentItem(currentPosition);

                startAutoPlay();
            }
        }, mAutoPlayTime);
    }

    /**
     * 停止轮播
     */
    private void stopAutoPlay() {
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 设置轮播标志
     *
     * @param autoPlay
     */
    public void setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isAutoPlay) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    startAutoPlay();
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public class BannerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
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
            Glide.with(getContext()).load(mNetImage.get(position)).into(image);

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
