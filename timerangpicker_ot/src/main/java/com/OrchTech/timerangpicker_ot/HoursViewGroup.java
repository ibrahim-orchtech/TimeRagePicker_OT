package com.OrchTech.timerangpicker_ot;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * this class used for create 24 view for all hours of day
 *
 */
class HoursViewGroup extends LinearLayout {
    private ArrayList<HourLine> items = new ArrayList<>();
    private ArrayList<Pair<Integer,Integer>> positions = new ArrayList<>();
    private int firstHour = 0;
    private int lastHour = 24;
    private int verticalLineWidth = 1;
    private int horizontalLineHeight = 1;
    private int textSize = 14;
    private String selectedTextColor = "#CCCCCC";
    private String textColor = "#CCCCCC";
    private Context context;
    private boolean is_24_hour = false;
    private String hourStrings[];
    private int topSpace = 100;
    private int textHeight = 30;
    DrawerInterface listener;
    int count = 0;
    int first_ball_index = -1;
    int second_ball_index = -1;
    public HoursViewGroup(Context context) {
        super(context);
        this.context = context;
        setOrientation(VERTICAL);
    }
    public HoursViewGroup(Context context, DrawerInterface listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        setOrientation(VERTICAL);
    }

    public HoursViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(VERTICAL);

    }

    public HoursViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setOrientation(VERTICAL);
    }
    public int getItemHeight(){
        if(items==null||items.size()==0)return -1;
        return items.get(1).getHeight();
    }
    String getHourName(int i){
        return hourStrings[i];
    }
    boolean is_first_hour = true;
    private void addHour(String name){
        HourLine hourLine = new HourLine(context);
        hourLine.setText(name);
        hourLine.setVerticalBorderWidth(verticalLineWidth);
        hourLine.setHorBorderHeight(horizontalLineHeight);
        if(is_first_hour) {
            // 50 is the height of text
            hourLine.setTopSpace(topSpace+50);
            is_first_hour = false;
        }
        else{
            hourLine.setTopSpace(topSpace);

        }
        count++;
        items.add(hourLine);
        addView(hourLine);
    }
    public Boolean is24Hours(){
        return is_24_hour;
    }
    public int getHorizontalLineHeight(){
        return horizontalLineHeight;
    }
    public int getHorizontalLinePosition(){
        if(items==null||items.size()==0)return -1;
        return items.get(0).getHorViewPosition();
    }
    public int getTextWidth(){
        if(items==null||items.size()==0)return -1;
        return items.get(0).getTextWidth();
    }

    public ArrayList<HourLine> getItems() {
        return items;
    }
    public void setFirstBallPos(int index){
        first_ball_index = index;
    }
    public void setSecondBallPos(int index){
        first_ball_index = index;
    }
    public void removeFirstBallText(){
        if(first_ball_index!=-1)
        items.get(first_ball_index).setSelected(false);
    }
    public void removeSecondBallText(){
        if(second_ball_index!=-1)
        items.get(second_ball_index).setSelected(false);
    }
    public ArrayList<Pair<Integer, Integer>> getPositions() {
        return positions;
    }
    public static class Builder{
        private HoursViewGroup hoursViewGroup;
        private Context context;
        public Builder(Context context,DrawerInterface listener){
            hoursViewGroup = new HoursViewGroup(context,listener);
            this.context = context;
        }
        public Builder setTextColor(String color){
            hoursViewGroup.textColor = color;
            return this;
        }
        public Builder setSelectedColor(String color){
            hoursViewGroup.selectedTextColor = color;
            return this;
        }
        public Builder setFontSize(int size){
            hoursViewGroup.textSize = size;
            return this;
        }
        public Builder setHorizontalViewHeight(int size){
            hoursViewGroup.horizontalLineHeight = size;
            return this;
        }
        public Builder setVerticalViewWidth(int size){
            hoursViewGroup.verticalLineWidth = size;
            return this;
        }
        public Builder setStartHour( int value){
            hoursViewGroup.firstHour = value;
            return this;
        }
        public Builder setLastHour(int value){
            hoursViewGroup.lastHour = value;
            return this;
        }
        public Builder is24Hour(boolean flag){
            hoursViewGroup.is_24_hour = flag;
            return this;
        }
        public Builder setTopSpace(int value){
            hoursViewGroup.topSpace = value;
            return this;
        }
        public HoursViewGroup build(){
            if(!hoursViewGroup.is_24_hour)
                hoursViewGroup.hourStrings = hoursViewGroup.context.getResources().getStringArray(R.array.hours_12);
            else
                hoursViewGroup.hourStrings = hoursViewGroup.context.getResources().getStringArray(R.array.hours_24);
            for(int i = hoursViewGroup.firstHour;i<hoursViewGroup.lastHour;i++) {
                hoursViewGroup.addHour(hoursViewGroup.getHourName(i));
                hoursViewGroup.getItems().get(i).invalidate();
                final ViewTreeObserver vto = hoursViewGroup.getChildAt(i).getViewTreeObserver();
                final int finalI = i;
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        hoursViewGroup.getChildAt(finalI).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int[] posXY = new int[2];
                        hoursViewGroup.getChildAt(finalI).getLocationOnScreen(posXY);
                        hoursViewGroup.listener.onDrawCell(finalI,posXY[0],posXY[1]);
                    }
                });
            }
            return hoursViewGroup;
        }
    }
}
