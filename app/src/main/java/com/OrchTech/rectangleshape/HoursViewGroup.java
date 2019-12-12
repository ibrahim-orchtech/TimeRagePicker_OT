package com.OrchTech.rectangleshape;

import android.content.Context;
import android.graphics.Color;
import android.util.Pair;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class HoursViewGroup extends LinearLayout {
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
    private int topSpace = 150;
    TimePicker.DrawerInterface listener;
    int count = 0;
    public HoursViewGroup(Context context, TimePicker.DrawerInterface listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        setOrientation(VERTICAL);
    }
    public int getItemHeight(){
        if(items==null||items.size()==0)return -1;
        return items.get(0).getHeight();
    }
    String getHourName(int i){
        return hourStrings[i];
    }
    public int getTextHeight(){
        if(items==null||items.size()==0)return 0;
        return items.get(0).textView.getHeight();
    }
    public void addHour(String name){
        HourLine hourLine = new HourLine(context);
        hourLine.setText(name);
        hourLine.setVerticalBorderWidth(verticalLineWidth);
        hourLine.setHorBorderHeight(horizontalLineHeight);
        hourLine.setTopSpace(topSpace);
        if(count%2==0)
            hourLine.setBackgroundColor(Color.parseColor("#79505F"));
        else{

            hourLine.setBackgroundColor(Color.parseColor("#00574B"));
        }
        count++;
        items.add(hourLine);
        addView(hourLine);
    }
    public int getHorizontalLineHeight(){
        return horizontalLineHeight;
    }
    public int getHorizontalLinePosition(){
        if(items==null||items.size()==0)return -1;
        return items.get(0).getHorViewPosition();
    }

    public ArrayList<HourLine> getItems() {
        return items;
    }
    public static class Builder{
        private HoursViewGroup hoursViewGroup;
        private Context context;
        public Builder(Context context,TimePicker.DrawerInterface listener){
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
