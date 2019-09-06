package com.example.demo.daojishi;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;


import com.example.demo.R;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * author：余智强
 * Date:2019-eight-five
 * DIRECTION:自定义倒计时控件
 */
public class MyView_Ceshi extends View   {
    /**
     * 画圆弧的画笔
     */
    private Paint circlePaint;
    /**
     * 圆弧的宽度
     */
    private int circleWidth;
    /**
     * 底部圆弧的颜色，默认为Color.LTGRAY
     */
    private int firstColor;
    /**
     * 圆弧是否渐变
     */
    private boolean isShowGradient;

    /**
     * 进度条圆弧块的颜色
     */
    private int secondColor;
    /**
     * 每次扫过的角度，用来设置进度条圆弧所对应的圆心角，alphaAngle=(currentValue/maxValue)*360
     */
    private float alphaAngle;
    /**
     * 进度条最大值
     */
    private int maxValue = 36*176*10;

    /**
     * 当前进度值
     */
    private int[] colorArray = new int[]{Color.parseColor("#FF8F52"),
            Color.parseColor("#FFC75F"), Color.parseColor("#FFC75F")};

    private ValueAnimator animator;
    /**
     * 画文字的画笔
     */
    private Paint textPaint;
    //需要绘画默认的圆弧

    //需要绘画倒计时的效果
    public MyView_Ceshi(Context context) {
        this(context, null);
    }

    long haomiao;//剩余时间
    Timer timer;
    TimerTask timerTask;
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    String percent;//当前剩余时间
    long total_time;//总时长
    public MyView_Ceshi(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView_Ceshi(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CountDownProgressBar, defStyleAttr, 0);
        initPaint(ta);
    }
    public void setcolor(int []colorss){
        colorArray = colorss;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 分别获取期望的宽度和高度，并取其中较小的尺寸作为该控件的宽和高,并且不超过屏幕宽高
        int widthPixels = this.getResources().getDisplayMetrics().widthPixels;//获取屏幕宽
        int heightPixels = this.getResources().getDisplayMetrics().heightPixels;//获取屏幕高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int hedight = MeasureSpec.getSize(heightMeasureSpec);
        int minWidth = Math.min(widthPixels, width);
        int minHedight = Math.min(heightPixels, hedight);
        setMeasuredDimension(Math.min(minWidth, minHedight), Math.min(minWidth, minHedight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = this.getWidth() / 2;
        int radius = center - circleWidth / 2;
        drawDefaultCirclr(canvas, center, radius);//绘制默认的圆
        drawText();
    }

    void initPaint(TypedArray ta) {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true); // 抗锯齿
        circlePaint.setDither(true); // 防抖动
        circlePaint.setStrokeWidth(circleWidth);//画笔宽度

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);


        circleWidth = ta.getDimensionPixelSize(R.styleable.CountDownProgressBar_countDown_circleWidth, (int) dip2px(6f));

        firstColor = ta.getColor(R.styleable.CountDownProgressBar_countDown_firstColor, Color.WHITE); // 默认底色为亮灰色

        isShowGradient = ta.getBoolean(R.styleable.CountDownProgressBar_countDown_isShowGradient, false); // 默认不适用渐变色

        secondColor = ta.getColor(R.styleable.CountDownProgressBar_countDown_secondColor, Color.WHITE); // 默认进度条颜色为蓝色
    }


    //绘画默认的圆弧
    void drawDefaultCirclr(Canvas canvas, int center, int radius) {

        circlePaint.setShader(null);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.STROKE);//绘制空心圆
        circlePaint.setStrokeWidth(circleWidth);
        canvas.drawCircle(center, center, radius, circlePaint);
        //*******************至此圆环画完了*********************//
        RectF oval = new RectF(center - radius, center - radius,center + radius, center + radius); // 圆的外接正方形
        circlePaint.reset();
        circlePaint.setShader(null);
        circlePaint.setStyle(Paint.Style.STROKE);//绘制空心圆
        circlePaint.setStrokeWidth(circleWidth);
       // circlePaint.setColor(secondColor); // 设置圆弧的颜色
        circlePaint.setStrokeCap(Paint.Cap.ROUND); // 把每段圆弧改成圆角的

        LinearGradient linearGradientt = new LinearGradient(circleWidth, circleWidth, getMeasuredWidth() - circleWidth, getMeasuredHeight() - circleWidth, colorArray, null, Shader.TileMode.MIRROR);
        circlePaint.setShader(linearGradientt);

        float lucheng = (maxValue*1.0f / (total_time/1000))*((total_time/1000)-(haomiao/1000));
        alphaAngle =   -  ((maxValue-lucheng) * 360.0f /  maxValue * 1.0f);

        canvas.drawArc(oval, -90, -alphaAngle, false, circlePaint);


   }

    /**
     * 按照进度显示百分比
     */
    public void setDuration(long duration, OnFinishListener listener, OnChangeListener onChangeListener) {
        this.listener = listener;
        this.onChangeListener = onChangeListener;
        this.haomiao = duration;
        this.total_time = duration;
        drawText();
        if (timer == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    myHandler.sendMessage(message);
                }
            };
            timer.schedule(timerTask, 0, 1000);
        }
    }


    MyHandler myHandler = new MyHandler(this);
    static class MyHandler extends Handler {
         WeakReference<MyView_Ceshi> myView_ceshiWeakReference;
         MyHandler(MyView_Ceshi myView_ceshi){
         myView_ceshiWeakReference = new WeakReference<MyView_Ceshi>(myView_ceshi);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyView_Ceshi myView_ceshi = myView_ceshiWeakReference.get();
            if (msg.what == 1) {
                myView_ceshi.invalidate();
                myView_ceshi.haomiao = myView_ceshi.haomiao - 1000;
                if (myView_ceshi.haomiao < 0) {
                    myView_ceshi.listener.onFinish();
                    if (myView_ceshi.timer != null) {
                        myView_ceshi.timer.cancel();  //将原任务从队列中移除
                        myView_ceshi.timer.purge();
                        myView_ceshi.timer = null;
                    }
                }
                if (myView_ceshi.onChangeListener != null) {
                    myView_ceshi.onChangeListener.onChange(myView_ceshi.percent);
                }
            }
        }
    }

    private void drawText() {
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+one:one"));
        if (maxValue == -1) {
            percent = "完成";
        } else {
            percent = formatter.format(haomiao);
        }
    }

    OnFinishListener listener;
    OnChangeListener onChangeListener;


    public void stop() {
        if(timer!=null){
            timer.cancel();  //将原任务从队列中移除
            timer.purge();
            timer = null;
        }
    }

    public interface OnFinishListener {
        void onFinish();
    }

    public interface OnChangeListener {
        void onChange(String dsa);
    }

    public static float dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (dipValue * scale + 0.5f);
    }
}

