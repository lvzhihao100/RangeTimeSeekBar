package com.eqdd.yiqidian.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.eqdd.yiqidian.utils.DensityUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author吕志豪 .
 * @date 18-1-10  下午2:38.
 * Github :https://github.com/lvzhihao100
 * E-Mail：1030753080@qq.com
 * 简书 :http://www.jianshu.com/u/6e525b929aac
 * 时间段选择器
 */

public class RangeTimeSeekBar extends View {

    private Paint showPaint;
    private Paint underPaint;
    private Paint pressPaint;
    private int fullHeight;
    private int fullWidth;
    private int blNWidth;
    private int blNHeight;
    private int blPWidth;
    private int blPHeight;
    private boolean isPressed = false;
    private float movedX;
    private float oldX;
    private int ruleHeight;
    private float startPos;
    private float endPos;
    private boolean isStart = false;
    private int originBlNWidth;
    private int originBlNHeight;
    private int originBlPWidth;
    private int originBlPHeight;
    private Paint textPaint;
    private Paint.FontMetricsInt fontMetrics;
    private float distance;
    private int startHour = 9;
    private int startMin = 0;
    private int endHour = 18;
    private int endMin = 0;
    private String startTime;
    private String endTime;
    private int minSpace=6;

    public RangeTimeSeekBar(Context context) {
        super(context);
        init();
    }


    public RangeTimeSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RangeTimeSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        showPaint = new Paint();
        showPaint.setStyle(Paint.Style.FILL);
        showPaint.setColor(Color.GREEN);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(3);
        textPaint.setTextSize(30);
        fontMetrics = textPaint.getFontMetricsInt();


        underPaint = new Paint();
        underPaint.setStyle(Paint.Style.FILL);
        underPaint.setColor(Color.GRAY);

        pressPaint = new Paint();
        pressPaint.setColor(Color.RED);
        pressPaint.setStyle(Paint.Style.FILL);

        originBlNWidth = 150;
        originBlNHeight = 50;
        originBlPWidth = 200;
        originBlPHeight = 70;

        blNWidth = 150;
        blNHeight = 50;
        blPWidth = 200;
        blPHeight = 70;
        ruleHeight = 10;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        fullWidth = getMySize(DensityUtil.percentW(500), widthMeasureSpec);
        fullHeight = getMySize(DensityUtil.percentW(200), heightMeasureSpec);
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = size;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                movedX = 0;
                oldX = event.getX();
                if (oldX > (startPos + endPos) / 2) {
                    isStart = false;
                } else {
                    isStart = true;
                }
                pressAnimToBig();
                break;
            case MotionEvent.ACTION_MOVE:
                float newX = event.getX();
                movedX = newX - oldX;
                System.out.println(movedX + "movedX");
                oldX = newX;
                break;
            case MotionEvent.ACTION_UP:
                movedX = 0;
                pressAnimToSmall();
                break;
        }
        invalidate();
        return true;
    }

    private void pressAnimToSmall() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(originBlPWidth, originBlNWidth);
        valueAnimator.addUpdateListener(animation -> {
            blPWidth = (int) animation.getAnimatedValue();
            blPHeight = originBlNHeight + (originBlPHeight - originBlNHeight) * ((int) animation.getAnimatedValue() - originBlNWidth) / (originBlPWidth - originBlNWidth);
            invalidate();
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isPressed = false;
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(500);
        valueAnimator.start();
    }

    private void pressAnimToBig() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(originBlNWidth, originBlPWidth);
        valueAnimator.addUpdateListener(animation -> {
            blPWidth = (int) animation.getAnimatedValue();
            blPHeight = originBlNHeight + (originBlPHeight - originBlNHeight) * ((int) animation.getAnimatedValue() - originBlNWidth) / (originBlPWidth - originBlNWidth);
            invalidate();
        });
        valueAnimator.setDuration(500);
        valueAnimator.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int startX = getPaddingLeft();
        int endX = fullWidth - getPaddingRight();
        System.out.println(endX + "endX");
        int startY = getPaddingTop();
        int halfBlPWidth = blPWidth >> 1;
        int doubleBlPHeight = blPHeight << 1;
        if (startPos == 0) {
            startPos = originBlPWidth / 2 + startX;
            endPos = endX - originBlPWidth / 2;
            distance = (endPos - startPos) / 60 / 24;
            startPos = startPos + distance * (startHour * 60 + startMin * 10);
            endPos = originBlPWidth / 2 + startX + distance * (endHour * 60 + endMin * 10);
        }
        float minDistance = minSpace*distance*10;
        if (isPressed && isStart) {
            startPos = Math.max(startPos += movedX, originBlPWidth / 2 + startX);
            startPos = Math.min(startPos, endX - originBlPWidth / 2 - minDistance);
            if (startPos + minDistance > endPos) {
                endPos = startPos + minDistance;
            }
        }
        if (isPressed && !isStart) {
            endPos = Math.max(endPos += movedX, originBlPWidth / 2 + startX + minDistance);
            endPos = Math.min(endPos, endX - originBlPWidth / 2);
            if (startPos + minDistance > endPos) {
                startPos = endPos - minDistance;
            }
        }

        //绘制刻度尺灰色
        canvas.drawRect(startX + (originBlPWidth >> 1), startY + doubleBlPHeight,
                endX - (originBlPWidth >> 1), startY + doubleBlPHeight + ruleHeight, underPaint);

        //绘制刻度尺绿色
        canvas.drawRect(startPos, startY + doubleBlPHeight,
                endPos, startY + doubleBlPHeight + ruleHeight, showPaint);

        //绘制上方显示块
        RectF topRect;
        if (isPressed && isStart) {//按压状态
            topRect = new RectF(startPos - halfBlPWidth, startY,
                    startPos + halfBlPWidth, startY + blPHeight);
            canvas.drawRect(topRect, pressPaint);

        } else {
            topRect = new RectF(startPos - blNWidth / 2, startY + blPHeight - blNHeight,
                    startPos + blNWidth / 2, startY + blPHeight);
            canvas.drawRect(topRect, showPaint);
        }
        //绘制上方文字
        int baseline = (int) ((topRect.top + topRect.bottom - fontMetrics.bottom - fontMetrics.top) / 2);
        canvas.drawText(startTime = getTime(startPos), startPos, baseline, textPaint);
        //绘制上方线
        canvas.drawLine(startPos, topRect.bottom, startPos, startY + doubleBlPHeight, showPaint);


        //绘制下方显示块
        RectF bottomRect;

        if (isPressed && !isStart) {//按压状态
            bottomRect = new RectF(endPos - halfBlPWidth, startY + blPHeight * 3 + ruleHeight,
                    endPos + halfBlPWidth, startY + (blPHeight << 2) + ruleHeight);
            canvas.drawRect(bottomRect, pressPaint);
        } else {
            bottomRect = new RectF(endPos - blNWidth / 2, startY + blPHeight * 3 + ruleHeight,
                    endPos + blNWidth / 2, startY + blPHeight * 3 + blNHeight + ruleHeight);
            canvas.drawRect(bottomRect, showPaint);
        }
        //绘制下方文字
        baseline = (int) ((bottomRect.top + bottomRect.bottom - fontMetrics.bottom - fontMetrics.top) / 2);
        canvas.drawText(endTime = getTime(endPos), endPos, baseline, textPaint);
        //绘制下方线
        canvas.drawLine(endPos, startY + doubleBlPHeight + ruleHeight, endPos, bottomRect.top, showPaint);


    }

    private String getTime(float startPos) {
        startPos = startPos - originBlPWidth / 2 - getPaddingLeft();
        int extraMin = (int) (startPos / distance % 60);
        int hour = 0;
        int min = 0;
        if (extraMin > 55) {
            hour = (int) (startPos / distance / 60) + 1;
            min = 0;
        } else {
            hour = (int) (startPos / distance / 60);
            min = (((int) (startPos / distance % 60 + 5) / 10) % 6)*10;
        }
        Calendar instance = Calendar.getInstance();
        instance.set(1970, 1, 1, hour, min);
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        return sf.format(instance.getTime());
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    /**设置起点时间点
     * @param startHour 0~24
     * @param startMin  0~6
     */
    public RangeTimeSeekBar setStartTime(int startHour, int startMin) {

        this.startHour = startHour;
        this.startMin = startMin;
        if (startPos == 0) {
            return this;
        }
        int startX = getPaddingLeft();
        int endX = fullWidth - getPaddingRight();
        startPos = originBlPWidth / 2 + startX;
        endPos = endX - originBlPWidth / 2;
        distance = (endPos - startPos) / 60 / 24;
        startPos = startPos + distance * (startHour * 60 + startMin * 10);
        endPos = originBlPWidth / 2 + startX + distance * (endHour * 60 + endMin * 10);
        invalidate();
        return this;
    }

    /**
     * 设置末端时间点
     * @param endHour 小时数
     * @param endMin 分钟数 例如30分钟 endMin设置为3
     * @return
     */
    public RangeTimeSeekBar setEndTime(int endHour, int endMin) {
        this.endHour = endHour;
        this.endMin = endMin;
        if (startPos == 0) {
            return this;
        }
        int startX = getPaddingLeft();
        int endX = fullWidth - getPaddingRight();
        startPos = originBlPWidth / 2 + startX;
        endPos = endX - originBlPWidth / 2;
        distance = (endPos - startPos) / 60 / 24;
        startPos = startPos + distance * (startHour * 60 + startMin * 10);
        endPos = originBlPWidth / 2 + startX + distance * (endHour * 60 + endMin * 10);
        invalidate();
        return this;
    }

    /**
     * 设置时间段最短间隔,
     * @param minute 例如设置1小时的间隔,minute设置6,
     * @return
     */
    public RangeTimeSeekBar setMinSpace(int minute){
        this.minSpace=minute;
        invalidate();
        return this;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //解决BottomSheetDialog跟随上下滑动
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }
}
