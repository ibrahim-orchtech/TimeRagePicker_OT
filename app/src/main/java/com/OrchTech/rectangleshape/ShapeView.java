package com.OrchTech.rectangleshape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class ShapeView extends View {

    private Point point1;
    private Point point2;
    private int count = 1;
    private Context context;
    private ArrayList<Circle> colorballs = new ArrayList<>();
    private int balID = 0;
    private Paint paint;
    private Canvas canvas;
    private boolean is_first_draw = true;
    TimePicker.MoveInterface listener;
    private int step = 10;
    private int old_ball = -1;
    private int old_Y = -1;
    public ShapeView(Context context, TimePicker.MoveInterface listener) {
        super(context);
        this.context = context;
        this.listener = listener;

    }

    public ShapeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public ShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    public int getCircleWidth(){
        return 100 ;
    }
    public void setStep(int step){
        this.step = step;
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
                int x = (int)event.getX();
                int y = (int)event.getY();
                listener.onFinishMove(y);
                break;
            case MotionEvent.ACTION_DOWN:
                balID = -1;
                for (Circle ball : colorballs) {
                    int centerX = ball.getX() + ball.getWidthOfCircle();
                    int centerY = ball.getY() + ball.getWidthOfCircle();
                    paint.setColor(Color.CYAN);
                    if (Math.abs(X - centerX) <= ball.getWidthOfCircle() && Math.abs(Y - centerY) <= ball.getHeightOfCircle()) {
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

                        /*int dif = Y-colorballs.get(0).getY();

                        Log.e("dif",dif+"");
                        if(dif<0)dif*=-1;
                        if(dif<step)break;*/
                        listener.onMove(canvas,0,X,Y,colorballs.get(1).getX(),colorballs.get(1).getY());

                    } else if (balID == 1) {
                        /*int dif = Y-colorballs.get(1).getY()-colorballs.get(1).getHeightOfCircle();
                        if(dif<0)dif*=-1;
                        if(dif<step)break;
                        */listener.onMove(canvas,1,colorballs.get(0).getX(),colorballs.get(0).getY(),X,Y);
                    }

                    invalidate();
                }
                //listener.onFinishMove();
                break;
        }
        // redraw the canvas
        invalidate();
        return true;

    }

    void setUpDots() {
        paint = new Paint();
        setFocusable(true);
        Circle circle1 = new Circle(context);
        Circle circle2 = new Circle(context);
        canvas = new Canvas();
        point1 = new Point();
        point1.x = 100;
        point1.y = 0 ;
        point2 = new Point();
        point2.x = (int) (this.getWidth() - this.getX() -100);
        point2.y = (int) (this.getHeight()-(circle1.getHeightOfCircle()) );
        circle1.setPoint(point1);
        circle2.setPoint(point2);
        colorballs.add(circle1);
        colorballs.add(circle2);

    }

    void drawShape(Canvas canvas) {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#CCCCCC"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(10);



        canvas.drawRoundRect(new RectF(point1.x -50,
                point1.y + colorballs.get(0).getWidthOfCircle() / 2,
                getWidth() - getX() + 50,
                point2.y + colorballs.get(0).getWidthOfCircle() / 2), 30, 30, paint);
        for (Circle ball : colorballs) {
            canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
                    new Paint());
        }

    }
    public void updateViews(Canvas canvas,int x1,int y1,int x2,int y2){

        paint.setColor(Color.CYAN);
        if (balID == 0) {

            colorballs.get(1).setY(y2-colorballs.get(1).getHeightOfCircle());
            colorballs.get(1).setX(x2);
            Log.e("ColorBall2",colorballs.get(1).getY()+"");
        } else if (balID == 1) {

            colorballs.get(1).setY(y2-colorballs.get(1).getHeightOfCircle());
            colorballs.get(1).setX(colorballs.get(1).getX());
        }
        invalidate();
    }

    public ArrayList<Circle>getColorballs(){
        return colorballs;
    }
}
