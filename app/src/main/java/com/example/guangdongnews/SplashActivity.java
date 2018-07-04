package com.example.guangdongnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.guangdongnews.activity.GuideActivity;
import com.example.guangdongnews.utils.CacheUtils;


/**
 * 闪屏页，用于过渡到引导页或者主页
 */
public class SplashActivity extends Activity {

    /**
     * 静态常量
     */
    public static final String START_MAIN = "start_main";
    private RelativeLayout rl_splahs_root;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rl_splahs_root = (RelativeLayout) findViewById(R.id.rl_splahs_root);
        //渐变动画
        AlphaAnimation aa = new AlphaAnimation(0,1);
        //动画终止时停留在最后一帧~不然会回到没有执行之前的状态,
        aa.setFillAfter(true);
        //缩放动画
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setFillAfter(true);
        //旋转动画
        RotateAnimation ra = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ra.setFillAfter(true);
        AnimationSet set = new AnimationSet(false);
        //添加三个动画没有先后顺序,便于同时播放动画
        set.addAnimation(aa);
        set.addAnimation(sa);
        set.addAnimation(ra);
        set.setDuration(2000);//动画持续时间
        rl_splahs_root.startAnimation(set);//开始动画
        set.setAnimationListener(new MyAnimationListener());//设置动画监听
    }

    class MyAnimationListener implements Animation.AnimationListener {

        /**
         * 当动画开始播放的时候回调这个方法
         *
         * @param animation
         */
        @Override
        public void onAnimationStart(Animation animation) {

        }

        /**
         * 当动画播放结束的时候回调这个方法
         *
         * @param animation
         */
        @Override
        public void onAnimationEnd(Animation animation) {

            //判断是否进入过主页面
            boolean isStartMain = CacheUtils.getBoolean(SplashActivity.this, START_MAIN);
            Intent intent = null;
            if(isStartMain){
//                如果进入过主页面，直接进入主页面
//                2.跳转到主页面
//                intent = new Intent(SplashActivity.this,MainActivity.class);

            }else{
//                如果没有进入过主页面，进入引导页面
                intent = new Intent(SplashActivity.this,GuideActivity.class);
            }
            startActivity(intent);

//            关闭Splash页面
            finish();


//            Toast.makeText(SplashActivity.this, "动画播放完成了", Toast.LENGTH_SHORT).show();

        }

        /**
         * 当动画重复播放的时候回调这个方法
         *
         * @param animation
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
