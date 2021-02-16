package xyz.imaginarycrisis.wanandroidapp;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class BannerViewPager extends ViewPager {
    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerViewPager(@NonNull Context context){
        super(context);

    }
    //默认10s播放时间
    private int showTime = 10000;
    //默认向左
    private ScrollDirection direction = ScrollDirection.LEFT;
    //方向的enum
    private enum ScrollDirection{LEFT,RIGHT}
    //设置播放时间
    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }
    //设置方向
    public void setDirection(ScrollDirection direction) {
        this.direction = direction;
    }

    public void start(){
        stop();
        postDelayed(player,showTime);
    }

    public void stop(){
        removeCallbacks(player);
    }

    public void next(){}

    public void previous(){}

    private Runnable player = new Runnable() {
        @Override
        public void run() {
            play(direction);
        }
    };
    
    private void play(ScrollDirection direction){
        PagerAdapter pagerAdapter = getAdapter();
        if (pagerAdapter != null) {// 判空
            // Item数量
            int count = pagerAdapter.getCount();
            // ViewPager现在显示的第几个？
            int currentItem = getCurrentItem();
            switch (direction) {
                case LEFT:// 如是向左滚动的，currentItem+1播放下一个
                    currentItem++;

                    // 如果+到最后一个了，就到第一个
                    if (currentItem >= count)
                        currentItem = 0;
                    break;
                case RIGHT:// 如是向右滚动的，currentItem-1播放上一个
                    currentItem--;

                    // 如果-到低一个了，就到最后一个
                    if (currentItem < 0)
                        currentItem = count - 1;
                    break;
            }
            setCurrentItem(currentItem);// 播放根据方向计算出来的position的item
        }

        // 这就是当可以循环播放的重点，每次播放完成后，再次开启一个定时任务
        start();
    }

    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == SCROLL_STATE_IDLE)
                    start();
                else if (state == SCROLL_STATE_DRAGGING)
                    stop();
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        removeCallbacks(player);
        super.onDetachedFromWindow();
    }
}
