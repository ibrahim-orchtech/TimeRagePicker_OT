package com.OrchTech.timerangpicker_ot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ibrahim.ali
 * Date 05,December,2019
 * Company OrchTech
 * this class to draw rectangle shape
 */
public class ShapeView extends View {

    MoveInterface listener;
    private Context context;
    private List<Circle> balls = new ArrayList<>();
    private int balID = 0;
    private Paint paint;
    private Canvas canvas;
    private boolean isFirstDraw = true;
    private int circleHeight = 50;
    private int strokeWidth = 5;
    private int raduis = 30;
    private int strockColor = Color.parseColor("#CCCCCC");
    private int circleImage = R.mipmap.icon;

    public ShapeView(Context context, MoveInterface listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    public int getCircleHeight() {
        return circleHeight;
    }

    public void setCircleHeight(int value) {
        circleHeight = value;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }


    public void setRaduis(int raduis) {
        this.raduis = raduis;
    }


    public void setStrockColor(int strockColor) {
        this.strockColor = strockColor;
    }

    public void setCircleImage(int circleImage) {
        this.circleImage = circleImage;
    }

    public List<Circle> getBalls() {
        return balls;
    }

    // the method that draws the balls
    @Override
    protected void onDraw(Canvas canvas) {


        if (isFirstDraw) {

            setUpDots();
            isFirstDraw = false;
        }
        drawShape(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();

        int eventX = (int) event.getX();
        int eventY = (int) event.getY();

        switch (eventAction) {

            case MotionEvent.ACTION_UP:
                int y = (int) event.getY();
                listener.onFinishMove(y);
                break;
            case MotionEvent.ACTION_DOWN:
                pickUpCircle(eventX,eventY);
                break;
                default:
                if (balID > -1) {
                    if (balID == 0) {
                        listener.onMove(canvas, 0, eventX, eventY, balls.get(1).getX(), balls.get(1).getY());
                    } else if (balID == 1) {
                        listener.onMove(canvas, 1, balls.get(0).getX(), balls.get(0).getY(), eventX, eventY);
                    }

                    invalidate();
                }
                break;
        }
        invalidate();
        return true;

    }
    private void pickUpCircle(int eventX,int eventY){
        balID = -1;
        for (Circle ball : balls) {
            int centerX = ball.getX() + ball.getHeightOfCircle();
            int centerY = ball.getY() + ball.getHeightOfCircle();
            paint.setColor(Color.CYAN);
            if (Math.abs(eventX - centerX) <= ball.getHeightOfCircle() && Math.abs(eventY - centerY) <= ball.getHeightOfCircle()) {
                balID = ball.getID();
                listener.onStartMove();
                break;
            } else {
                balID = -1;
            }
        }
    }

    /**
     * create first circle and put its x and y
     * set id for first ball to 0
     */
    private void createFirstCircle(){
        Circle circle1 = new Circle(context, 0);
        circle1.setHeight(circleHeight);
        circle1.setResource(circleImage);
        Point point1 = new Point();
        point1.x = 100;
        point1.y = 0;
        circle1.setPoint(point1);
        circle1.setHeight(circleHeight);
        balls.add(circle1);
    }

    /**
     * create second Circle and put its x and y
     * circle_id will be 1
     */
    private void createSecondCircle(){

        Circle circle2 = new Circle(context, 1);
        circle2.setHeight(circleHeight);
        circle2.setResource(circleImage);
        canvas = new Canvas();
        Point point2 = new Point();
        point2.x = (int) (this.getWidth() - this.getX() - getCircleHeight() - 50);
        point2.y = this.getHeight() - balls.get(0).getHeightOfCircle();
        circle2.setPoint(point2);
        circle2.setHeight(circleHeight);
        balls.add(circle2);
    }

    /**
     * setUpDots it will create two Circles
     */
    private void setUpDots() {
        paint = new Paint();
        setFocusable(true);
        createFirstCircle();
        createSecondCircle();
    }

    /**
     * @param canvas
     * this method will draw shape by using paint and add stroke and color
     */
    private void drawShape(Canvas canvas) {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(strockColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(strokeWidth);


        canvas.drawRoundRect(new RectF(getLeft() + 50,
                balls.get(0).getY() + balls.get(0).getHeightOfCircle() / 2,
                getWidth() - getX() - 30,
                balls.get(1).getY() + balls.get(0).getHeightOfCircle() / 2), raduis, raduis, paint);
        for (Circle ball : balls) {
            canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
                    new Paint());
        }
    }

    /**
     * @param x2
     * @param y2
     * this method will update rectangle by new position
     */
    public void updateViews(int x2, int y2) {
        paint.setColor(Color.CYAN);
        if (balID == 0) {
            balls.get(1).setY(y2 - balls.get(1).getHeightOfCircle());
            balls.get(1).setX(x2);
        } else if (balID == 1) {

            balls.get(1).setY(y2 - balls.get(1).getHeightOfCircle());
            balls.get(1).setX(balls.get(1).getX());
        }
        invalidate();
    }

}
