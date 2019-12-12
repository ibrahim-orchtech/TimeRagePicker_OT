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

/**
 * Created by ibrahim.ali
 * Date 05,December,2019
 * Company OrchTech
 */
public class ShapeView extends View {

    MoveInterface listener;
    private Point point1;
    private Point point2;
    private Context context;
    private ArrayList<Circle> balls = new ArrayList<>();
    private int balID = 0;
    private Paint paint;
    private Canvas canvas;
    private boolean is_first_draw = true;
    private int circleHeight = 50;
    private int strokeWidth = 5;
    private int raduis = 30;
    private int strockColor = Color.parseColor("#CCCCCC");
    private int circleImage = R.mipmap.icon;
    public ShapeView(Context context, MoveInterface listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        //setBackgroundColor(Color.parseColor("#CCCCCC"));
    }

    public int getCircleHeight() {
        return circleHeight;
    }
    public void setCircleHeight(int value){
        circleHeight = value;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getRaduis() {
        return raduis;
    }

    public void setRaduis(int raduis) {
        this.raduis = raduis;
    }

    public int getStrockColor() {
        return strockColor;
    }

    public void setStrockColor(int strockColor) {
        this.strockColor = strockColor;
    }

    public int getCircleImage() {
        return circleImage;
    }

    public void setCircleImage(int circleImage) {
        this.circleImage = circleImage;
    }

    public ArrayList<Circle> getBalls() {
        return balls;
    }

    // the method that draws the balls
    @Override
    protected void onDraw(Canvas canvas) {


        if (is_first_draw) {

            setUpDots();
            is_first_draw = false;
        }
        drawShape(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (eventaction) {

            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                int y = (int) event.getY();
                listener.onFinishMove(y);
                break;
            case MotionEvent.ACTION_DOWN:
                balID = -1;
                for (Circle ball : balls) {
                    int centerX = ball.getX() + ball.getHeightOfCircle();
                    int centerY = ball.getY() + ball.getHeightOfCircle();
                    paint.setColor(Color.CYAN);
                    if (Math.abs(X - centerX) <= ball.getHeightOfCircle() && Math.abs(Y - centerY) <= ball.getHeightOfCircle()) {
                        balID = ball.getID();
                        listener.onStartMove();
                        break;
                    } else {
                        balID = -1;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (balID > -1) {
                    if (balID == 0) {
                        listener.onMove(canvas, 0, X, Y, balls.get(1).getX(), balls.get(1).getY());
                    } else if (balID == 1) {
                        listener.onMove(canvas, 1, balls.get(0).getX(), balls.get(0).getY(), X, Y);
                    }

                    invalidate();
                }
                break;
        }
        invalidate();
        return true;

    }

    private void setUpDots() {
        paint = new Paint();
        setFocusable(true);
        Circle circle1 = new Circle(context);
        Circle circle2 = new Circle(context);
        circle1.setHeight(circleHeight);
        circle2.setHeight(circleHeight);
        circle1.setResource(circleImage);
        circle2.setResource(circleImage);
        canvas = new Canvas();
        point1 = new Point();
        point1.x = 100;
        point1.y = 0;
        point2 = new Point();
        point2.x = (int) (this.getWidth() - this.getX() - getCircleHeight()-50);
        point2.y = this.getHeight() - circle1.getHeightOfCircle();
        circle1.setPoint(point1);
        circle2.setPoint(point2);
        circle1.setHeight(circleHeight);
        circle2.setHeight(circleHeight);
        balls.add(circle1);
        balls.add(circle2);

    }

    private void drawShape(Canvas canvas) {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(strockColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(strokeWidth);


        canvas.drawRoundRect(new RectF(getLeft()+50,
                balls.get(0).getY() + balls.get(0).getHeightOfCircle() / 2,
                getWidth() - getX() -30,
                balls.get(1).getY() + balls.get(0).getHeightOfCircle() / 2), raduis, raduis, paint);
        for (Circle ball : balls) {
            canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
                    new Paint());
        }
    }

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
    public int getCenterOfSecondBall(){
        return balls.get(1).getY()+(balls.get(1).getHeightOfCircle()/2);
    }
    public int getCenterOfFirstBall(){
        return balls.get(0).getY()+balls.get(0).getHeightOfCircle();
    }

}
