package com.OrchTech.rectangleshape;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class Circle {
    private static int count = 0;
    private Bitmap bitmap;
    private Context mContext;
    private Point point = new Point();
    private int id;
    private int resourceId = R.mipmap.icon;
    private int x, y;
    private int radius = 100;


    public Circle(Context context){
        mContext = context;
        id = count++;
        bitmap = BitmapFactory.decodeResource(context.getResources(),
                resourceId);
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);

    }
    public Circle setX(int x){
        point.x = x;
        return this;
    }
    public Circle setY(int y){
        point.y = y;
        return this;
    }
    public Circle setPoint(Point point){
        this.point = point;
        return this;
    }
    public Circle setResource(int resourceId){
        this.resourceId = resourceId;
        resizeBitmap();
        return this;
    }
    public Circle setRadius(int radius){
        this.radius = radius;
        resizeBitmap();

        return this;
    }
    private void resizeBitmap(){
        bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                resourceId);
        bitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);

    }
    public int getWidthOfCircle() {

        return bitmap.getWidth();
    }

    public int getHeightOfCircle() {
        return bitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return point.x;
    }

    public int getY() {
        return point.y;
    }

    public int getID() {
        return id;
    }

    public Point getPoint() {
        return point;
    }

    public int getId() {
        return id;
    }

    public int getResourceId() {
        return resourceId;
    }

    public int getRadius() {
        return radius;
    }
}
