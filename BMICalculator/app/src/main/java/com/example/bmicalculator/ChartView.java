package com.example.bmicalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChartView extends View {
    private Context mContext;
    //绘制坐标轴的画笔
    private Paint mAxisPaint;
    //绘制曲线的画笔
    private Paint mPaint;
    //绘制X轴上方的画笔
    private Paint mXAxisLinePaint;
    private Paint mPaintText;
    //向上的曲线图的绘制起点(px)
    private int startX;
    private int startY;
    //向下的曲线图的绘制起点(px)
    private int downStartX;
    private int downStartY;
    //上方Y轴每单位刻度所占的像素值
    private float YAxisUpUnitValue;
    //下方Y轴每单位刻度所占的像素值
    private float YAxisDownUnitValue;
    //根据具体传入的数据，在坐标轴上绘制点
    private Point[] mPoints;
    //传入的数据，决定绘制的纵坐标值
    private List<Float> mData = new ArrayList<>();
    //Y轴刻度集合
    private List<Float> mYAxisList = new ArrayList<>();
    //X轴刻度集合
    private List<String> mXAxisList = new ArrayList<>();
    //X轴的绘制距离
    private int mXAxisMaxValue;
    //Y轴的绘制距离
    private int mYAxisMaxValue;
    //Y轴刻度间距(px)
    private int yAxisSpace = 120;
    //X轴刻度间距(px)
    private int xAxisSpace = 200;
    //Y轴刻度线宽度
    private int mScaleWidth = 20;
    private float ScaleTextSize = 20;
    //刻度值距离坐标的padding距离
    private int textPadding = 10;
    //Y轴递增的实际值
    private float yIncreaseValue;
    //true：绘制曲线 false：折线
    private boolean isCurve = false;
    private Rect mYMaxTextRect;
    private Rect mXMaxTextRect;
    private int mMaxTextHeight;
    private int mMaxTextWidth;

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setWillNotDraw(false);
        initData();
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = (mYAxisList.size() - 1) * yAxisSpace + mMaxTextHeight * 2 + textPadding * 2;
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = startX + (mData.size() - 1) * xAxisSpace + mMaxTextWidth;
        }
        //保存测量结果
        setMeasuredDimension(widthSize, heightSize);
//        Log.e("onMeasure", "widthSize=" + widthSize + "--heightSize=" + heightSize);
    }

    private void initView() {
        //初始化画笔
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.MaterialTeal));
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        //绘制X,Y轴坐标的画笔
        mAxisPaint = new Paint();
        mAxisPaint.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        mAxisPaint.setStrokeWidth(2);
        mAxisPaint.setAntiAlias(true);
        mAxisPaint.setStyle(Paint.Style.STROKE);
        //绘制坐标轴上方的横线的画笔
        mXAxisLinePaint = new Paint();
        mXAxisLinePaint.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        mXAxisLinePaint.setStrokeWidth(1);
        mXAxisLinePaint.setAntiAlias(true);
        mXAxisLinePaint.setStyle(Paint.Style.STROKE);
        //绘制刻度值文字的画笔
        mPaintText = new Paint();
        mPaintText.setTextSize(ScaleTextSize);
        mPaintText.setColor(ContextCompat.getColor(mContext, R.color.MaterialBlueGray));
        mPaintText.setAntiAlias(true);
        mPaintText.setStrokeWidth(1);

        mYMaxTextRect = new Rect();
        mXMaxTextRect = new Rect();
        mPaintText.getTextBounds(Float.toString(mYAxisList.get(mYAxisList.size() - 1)), 0,
                Float.toString(mYAxisList.get(mYAxisList.size() - 1)).length(), mYMaxTextRect);
        mPaintText.getTextBounds(mXAxisList.get(mXAxisList.size() - 1), 0,
                mXAxisList.get(mXAxisList.size() - 1).length(), mXMaxTextRect);
        //绘制的刻度文字的最大值所占的宽高
        mMaxTextWidth = Math.max(mYMaxTextRect.width(), mXMaxTextRect.width());
        mMaxTextHeight = Math.max(mYMaxTextRect.height(), mXMaxTextRect.height());

        //指定绘制的起始位置
        startX = mMaxTextWidth + textPadding + mScaleWidth;
        //坐标原点Y的位置（+1的原因：X轴画笔的宽度为2 ; +DP2PX.dip2px(mContext, 5)
        // 原因：为刻度文字所占的超出的高度 ）——>解决曲线画到最大刻度值时，显示高度不够，曲线显示扁扁的问题
        startY = yAxisSpace * (mYAxisList.size() - 1) + mMaxTextHeight;

        if (mYAxisList.size() >= 2) {
            yIncreaseValue = mYAxisList.get(1) - mYAxisList.get(0);
        }
        //X轴绘制距离
        mXAxisMaxValue = (mData.size() - 1) * xAxisSpace;
        //Y轴绘制距离
        mYAxisMaxValue = (mYAxisList.size() - 1) * yAxisSpace;

        //坐标起始点Y轴高度=(startY+mScaleWidth)  下方文字所占高度 = dp2px(mContext, ScaleTextSize)
//        int viewHeight = startY + 2 * mScaleWidth + dp2px(mContext, ScaleTextSize);
//        Log.e("TAG", "viewHeight=" + viewHeight);
    }

    private static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private Point[] initPoint() {
        Point[] points = new Point[mData.size()];
        for (int i = 0; i < mData.size(); i++) {
            Float ybean = mData.get(i);
            int drawHeight = (int) (startY * 1.0 - (ybean * yAxisSpace * 1.0 / yIncreaseValue));
            int startx = startX + xAxisSpace * i;
            points[i] = new Point(startx, drawHeight);
        }
        return points;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPoints = initPoint();
//        Log.e("TAG", "startX=" + startX + "---startY=" + startY);
        for (int i = 0; i < mYAxisList.size(); i++) {
            //Y轴方向递增的高度
            int yAxisHeight = startY - yAxisSpace * i;
            //绘制X轴和上方横线
            canvas.drawLine(startX - mScaleWidth, yAxisHeight,
                    startX + (mData.size() - 1) * xAxisSpace,
                    yAxisHeight, mXAxisLinePaint);
            //绘制文字时,Y轴方向递增的高度
            int yTextHeight = startY - yAxisSpace * i;
            //绘制Y轴刻度旁边的刻度文字值,10为刻度线与文字的间距
            mPaintText.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(mYAxisList.get(i) + "",
                    (startX - mScaleWidth) - textPadding,
                    yTextHeight, mPaintText);
        }
        //绘制Y轴
        canvas.drawLine(startX, startY, startX, startY - mYAxisMaxValue, mAxisPaint);

        //绘制X轴下面显示的文字
        for (int i = 0; i < mXAxisList.size(); i++) {
            int xTextWidth = startX + xAxisSpace * i - mScaleWidth;
            //设置从起点位置的左边对齐绘制文字
            mPaintText.setTextAlign(Paint.Align.LEFT);
            Rect rect = new Rect();
            mPaintText.getTextBounds(mXAxisList.get(i), 0, mXAxisList.get(i).length(), rect);
            canvas.drawText(mXAxisList.get(i), startX - rect.width() / 2 + xAxisSpace * i,
                    startY + rect.height() + textPadding, mPaintText);
        }

        //连接所有的数据点,画曲线
        if (isCurve) {
            //画曲线
            drawScrollLine(canvas);
        } else {
            //画折线
            drawLine(canvas);
        }
    }

    private void drawScrollLine(Canvas canvas) {
        Point start;
        Point end;
        int i = 0;
        DecimalFormat df = new DecimalFormat("0.0");
        for (i = 0; i < mPoints.length - 1; i++) {
            start = mPoints[i];
            end = mPoints[i + 1];
            canvas.drawText(df.format(mData.get(i)),
                    start.x, start.y - textPadding, mPaintText);
            int wt = (start.x + end.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = start.y;
            p3.x = wt;
            p4.y = end.y;
            p4.x = wt;
            Path path = new Path();
            path.moveTo(start.x, start.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, end.x, end.y);
            canvas.drawPath(path, mPaint);
        }
        canvas.drawText(df.format(mData.get(i)),
                mPoints[i].x, mPoints[i].y - textPadding, mPaintText);
    }

    private void drawLine(Canvas canvas) {
        Point start;
        Point end;
        int i = 0;
        DecimalFormat df = new DecimalFormat("0.0");
        for (i = 0; i < mPoints.length - 1; i++) {
            start = mPoints[i];
            end = mPoints[i + 1];
            canvas.drawText(df.format(mData.get(i)),
                    start.x, start.y - textPadding, mPaintText);
            canvas.drawLine(start.x, start.y, end.x, end.y, mPaint);
        }
        canvas.drawText(df.format(mData.get(i)),
                mPoints[i].x, mPoints[i].y - textPadding, mPaintText);
    }
    
    private void initData() {
        //外界传入的数据，即为绘制曲线的每个点
        mData.add((float) 0);
        mData.add((float) 10);
        mData.add((float) 5);
        mData.add((float) 20);
        mData.add((float) 15);

        float[] mYAxisData = new float[]{0, 10, 20, 30, 40};
        for (float mYAxisDatum : mYAxisData) {
            mYAxisList.add(mYAxisDatum);
        }

        //X轴数据
        mXAxisList.add("5/1");
        mXAxisList.add("5/2");
        mXAxisList.add("5/3");
        mXAxisList.add("5/4");
        mXAxisList.add("5/5");
    }

    public void setData(List<Float> data,
                        List<String> xAxisData) {
        this.mData = data;
        this.mXAxisList = xAxisData;
        initView();
        postInvalidate();
    }

    public void setData(List<Float> data,
                        List<String> xAxisData,
                        List<Float> yAxisData) {
        this.mData = data;
        this.mXAxisList = xAxisData;
        this.mYAxisList = yAxisData;
        initView();
        postInvalidate();
    }
}
