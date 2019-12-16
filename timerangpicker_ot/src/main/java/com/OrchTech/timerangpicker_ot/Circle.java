package com.OrchTech.timerangpicker_ot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class Circle {
    private  int count = 0;
    private Bitmap bitmap;
    private Context mContext;
    private Point point = new Point();
    private int id;
    private int resourceId = R.mipmap.icon;
    private int x, y;
    private int height = 100;


    public Circle(Context context,int id){
        mContext = context;
        this.id = id;
        bitmap = BitmapFactory.decodeResource(context.getResources(),
                resourceId);
        bitmap = Bitmap.createScaledBitmap(bitmap, height, height, false);
    }
    public Circle setX(int x){
        point.x = x;
        return this;
    }
    public Circle setY(int y){
        point.y = y;
        return this;
    }
    public void setPoint(Point point){
        this.point = point;
    }
    public void setResource(int resourceId){
        this.resourceId = resourceId;
        resizeBitmap();
    }
    public void setHeight(int height){
        this.height = height;
        resizeBitmap();
    }
    private void resizeBitmap(){
        bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                resourceId);
        bitmap = Bitmap.createScaledBitmap(bitmap, height, height, false);
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
}
