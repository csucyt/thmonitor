package com.example.thmonitor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/4/4.
 */


public class MyPanel extends View {

    private boolean firstFlag;

    private Paint highTempPaint;       //高温范围画笔
    private Paint lowTempPaint;        //低温范围画笔
    private Paint normTempPaint;       //正常范围画笔
    private Paint centerCirclePaint;   //指针圆形底画笔
    private Paint bottomPaint;         //仪表盘下方文字画笔
    private Paint pointerPaint;        //指针画笔
    private Paint scalePaint;          //刻度画笔

    private RectF tempRectF;           //仪表盘所在矩形
    private PointF centerPoint = new PointF();       //仪表盘中点
    private int centerCircleRaduis = 30;             //指针圆形底半径
    private int progressRaduis;                      //仪表盘半径
    private int viewHeight;
    private int viewWidth;
    private int scaleRange = 30;                     //刻度盘宽度

    private float minTempRange = 20f;                //低温范围，这里表示-20-0
    private float normTempRange = 30f;               //正常温度范围,这里表示0-30
    private float maxTempRange = 30f;                //高温范围，这里表示30-60
    private float minTempStartAngle = 135f;          //低温开始角度
    private float normTempStartAngle = 202.5f;       //正常温度开始角度
    private float maxTempStartAngle = 306f;          //高温开始角度
    private float anglePerTemp = 3.3f;               //每一度代表的角度

    private int panelWidth = 60;                     //仪表盘颜色填充区域宽度


    public void setCurrentTemp(float currentTemp) {
        this.currentTemp = currentTemp;
    }

    private float currentTemp = -20;                       //当前温度

    public MyPanel(Context context) {
        this(context, null);
    }

    public MyPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public float getCurrentTemp() {
        return currentTemp;
    }

    /**
     * 初始化函数
     * 创建画笔并设置属性
     */
    private void init() {
        highTempPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        highTempPaint.setColor(Color.rgb(251, 211, 28));
        highTempPaint.setStyle(Paint.Style.STROKE);

        lowTempPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        lowTempPaint.setColor(Color.rgb(24, 186, 249));
        lowTempPaint.setStyle(Paint.Style.STROKE);

        normTempPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        normTempPaint.setColor(Color.rgb(68, 239, 103));
        normTempPaint.setStyle(Paint.Style.STROKE);

        centerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        centerCirclePaint.setColor(Color.rgb(0, 0, 0));   //原(255,255,255)
        centerCirclePaint.setStyle(Paint.Style.STROKE);

        bottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        bottomPaint.setStyle(Paint.Style.STROKE);
        bottomPaint.setTextSize(60);
        bottomPaint.setColor(Color.BLACK);  //原White

        pointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        pointerPaint.setStyle(Paint.Style.STROKE);
        pointerPaint.setColor(Color.BLACK);    //原White
        pointerPaint.setStrokeWidth(10);

        scalePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        scalePaint.setStyle(Paint.Style.STROKE);

        firstFlag = true;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        progressRaduis = (w / 2) * 7 / 10;
        viewHeight = h;
        viewWidth = w;
        centerPoint.set(viewWidth/2, viewHeight/2);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(firstFlag) {
            drawBackground(canvas);
            drawPanel(canvas);
            drawBottomText(canvas);
            drawCenterCircle(canvas);
            drawScale(canvas);
            firstFlag = true;
        }
        drawPointer(canvas);

    }

    /**
     * 绘制仪表盘刻度
     */
    private void drawScale(Canvas canvas) {

        //绘制仪表盘刻度弧线
        scalePaint.setColor(Color.parseColor("#000000"));   //原#e5e5e5
        float scaleRadius = progressRaduis - panelWidth/2 - scaleRange;
        tempRectF = new RectF(centerPoint.x - scaleRadius, centerPoint.y - scaleRadius,
                centerPoint.x + scaleRadius, centerPoint.y + scaleRadius);
        canvas.drawArc(tempRectF, minTempStartAngle, 270, false, scalePaint);

        for(int i = -20; i <= 60; i += 2) {
            canvas.save();
            float rotateAngle = -45f + ((i + 20) * anglePerTemp) - 90;
            float startX = centerPoint.x;
            float startY = centerPoint.y - progressRaduis + panelWidth / 2;;
            float endX = startX;
            float endY = startY + scaleRange;
            canvas.rotate(rotateAngle, centerPoint.x, centerPoint.y);
            scalePaint.setColor(Color.argb(150, 0, 0, 0)); //原(150,255,255,255)
            if(i % 10 == 0) {
                scalePaint.setStrokeWidth(5);
                canvas.drawLine(startX, startY, endX, endY, scalePaint);

                //绘制温度
                scalePaint.setStrokeWidth(1);
                scalePaint.setColor(Color.rgb(0, 0, 0));   //原(255,255,255)
                scalePaint.setTextSize(30);
                String string = i + "";
                Rect rectF = new Rect();
                bottomPaint.getTextBounds(string, 0, string.length(), rectF);
                canvas.drawText(string, endX - rectF.width() / 2, endY + 50, scalePaint);
            } else {
                scalePaint.setStrokeWidth(1);
                canvas.drawLine(startX, startY, endX, endY, scalePaint);
            }
            canvas.restore();
        }

    }

    /**
     * 绘制仪表盘
     */
    private void drawPanel(Canvas canvas) {
        highTempPaint.setStrokeWidth(panelWidth);
        lowTempPaint.setStrokeWidth(panelWidth);
        normTempPaint.setStrokeWidth(panelWidth);

        tempRectF = new RectF(centerPoint.x - progressRaduis, centerPoint.y - progressRaduis,
                centerPoint.x + progressRaduis, centerPoint.y + progressRaduis);

        canvas.drawArc(tempRectF, normTempStartAngle, normTempRange * (anglePerTemp + 0.1f), false, normTempPaint);
        canvas.drawArc(tempRectF, maxTempStartAngle, maxTempRange * anglePerTemp, false, highTempPaint);
        canvas.drawArc(tempRectF, minTempStartAngle, minTempRange * anglePerTemp, false, lowTempPaint);

        canvas.save();
//        canvas.rotate(minTempStartAngle - 180, centerPoint.x, centerPoint.y);
//        PointF leftPoint = new PointF(centerPoint.x - progressRaduis, centerPoint.y);
//        RectF leftArc = new RectF(leftPoint.x - panelWidth / 2, leftPoint.y - panelWidth / 2,
//                leftPoint.x + panelWidth / 2, leftPoint.y + panelWidth / 2);
//        lowTempPaint.setStrokeWidth(1);
//        lowTempPaint.setStyle(Paint.Style.FILL);
//        canvas.drawArc(leftArc, 0, 180, true, lowTempPaint);
//        canvas.rotate(90, centerPoint.x, centerPoint.y);
//        PointF rightPoint = new PointF(centerPoint.x + progressRaduis, centerPoint.y);
//        RectF rightArc = new RectF(rightPoint.x - panelWidth / 2, rightPoint.y - panelWidth / 2,
//                rightPoint.x + panelWidth / 2, rightPoint.y + panelWidth / 2);
//        highTempPaint.setStrokeWidth(1);
//        highTempPaint.setStyle(Paint.Style.FILL);
//        canvas.drawArc(rightArc, 0, 180, true, highTempPaint);
        canvas.restore();

    }

    /**
     * 绘制指针圆形底部
     */
    private void drawCenterCircle(Canvas canvas) {
        centerCirclePaint.setStrokeWidth(5);
        canvas.drawCircle(centerPoint.x, centerPoint.y, centerCircleRaduis, centerCirclePaint);
    }

    /**
     * 设置View背景颜色
     */
    private void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
    }

    /**
     * 绘制仪表盘底部文字
     */
    private void drawBottomText(Canvas canvas) {
        String string = "温度℃";
        Rect rectF = new Rect();
        bottomPaint.getTextBounds(string, 0, string.length(), rectF);
        canvas.drawText(string, centerPoint.x - rectF.width() / 2, centerPoint.y + progressRaduis * 0.8f, bottomPaint);
    }

    /**
     * 绘制指针
     */
    private void drawPointer(Canvas canvas) {

        float rotateAngle = -45f + ((currentTemp + 20) * anglePerTemp);
        canvas.save();
        canvas.rotate(rotateAngle, centerPoint.x, centerPoint.y);
        PointF startPoint = new PointF(centerPoint.x - centerCircleRaduis, centerPoint.y);
        PointF endPont = new PointF(startPoint.x - 200, centerPoint.y);
        canvas.drawLine(startPoint.x, startPoint.y, endPont.x, endPont.y, pointerPaint);
        canvas.restore();
    }

}

