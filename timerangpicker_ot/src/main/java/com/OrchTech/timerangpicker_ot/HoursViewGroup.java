package com.OrchTech.timerangpicker_ot;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * this class used for create 24 view for all hours of day
 */
class HoursViewGroup extends LinearLayout {
    DrawerInterface listener;
    int count = 0;
    boolean isFirstHour = true;
    int textSize;
    String selectedTextColor;
    String textColor;
    private List<HourLine> items = new ArrayList<>();
    private int firstHour = 0;
    private int lastHour = 24;
    private int verticalLineWidth = 1;
    private int horizontalLineHeight = 1;
    private Context context;
    private boolean is24Hour = false;
    private String[] hourStrings;
    private int topSpace = 100;

    public HoursViewGroup(Context context, DrawerInterface listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        setOrientation(VERTICAL);
    }


    public int getItemHeight() {
        if (items == null || items.isEmpty()) return -1;
        return items.get(1).getHeight();
    }

    String getHourName(int i) {
        return hourStrings[i];
    }

    void addHour(String name, final int index) {
        HourLine hourLine = new HourLine(context);
        hourLine.setText(name);
        hourLine.setVerticalBorderWidth(verticalLineWidth);
        hourLine.setHorBorderHeight(horizontalLineHeight);
        if (isFirstHour) {
            // 50 is the height of text
            hourLine.setTopSpace(topSpace + 50);
            isFirstHour = false;
        } else {
            hourLine.setTopSpace(topSpace);

        }
        hourLine.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onClickCell(index);
            }
        });
        count++;
        items.add(hourLine);
        addView(hourLine);
    }

    public boolean is24Hours() {
        return is24Hour;
    }

    public int getTextWidth() {
        if (items == null || items.isEmpty()) return -1;
        return items.get(0).getTextWidth();
    }

    public List<HourLine> getItems() {
        return items;
    }

    public static class Builder {
        private HoursViewGroup hoursViewGroup;

        public Builder(Context context, DrawerInterface listener) {
            hoursViewGroup = new HoursViewGroup(context, listener);
        }

        public Builder setTextColor(String color) {
            hoursViewGroup.textColor = color;
            return this;
        }

        public Builder setSelectedColor(String color) {
            hoursViewGroup.selectedTextColor = color;
            return this;
        }

        public Builder setFontSize(int size) {
            hoursViewGroup.textSize = size;
            return this;
        }

        public Builder setHorizontalViewHeight(int size) {
            hoursViewGroup.horizontalLineHeight = size;
            return this;
        }

        public Builder setVerticalViewWidth(int size) {
            hoursViewGroup.verticalLineWidth = size;
            return this;
        }

        public Builder is24Hour(boolean flag) {
            hoursViewGroup.is24Hour = flag;
            return this;
        }

        public Builder setTopSpace(int value) {
            hoursViewGroup.topSpace = value;
            return this;
        }

        public HoursViewGroup build() {
            if (!hoursViewGroup.is24Hour)
                hoursViewGroup.hourStrings = hoursViewGroup.context.getResources().getStringArray(R.array.hours_12);
            else
                hoursViewGroup.hourStrings = hoursViewGroup.context.getResources().getStringArray(R.array.hours_24);
            for (int i = hoursViewGroup.firstHour; i < hoursViewGroup.lastHour; i++) {
                hoursViewGroup.addHour(hoursViewGroup.getHourName(i), i);
                hoursViewGroup.getItems().get(i).invalidate();
                final ViewTreeObserver vto = hoursViewGroup.getChildAt(i).getViewTreeObserver();
                final int finalI = i;
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        hoursViewGroup.getChildAt(finalI).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int[] posXY = new int[2];
                        hoursViewGroup.getChildAt(finalI).getLocationOnScreen(posXY);
                        hoursViewGroup.listener.onDrawCell(finalI, posXY[0], posXY[1]);
                    }
                });
            }
            return hoursViewGroup;
        }
    }
}
