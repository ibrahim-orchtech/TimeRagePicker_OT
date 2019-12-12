package com.OrchTech.rectangleshape;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class HourLine extends ConstraintLayout {
    View view;
    Context context;
    TextView textView;
    View vwHorizontal;
    View vwVertical;
    String selectedTextColor = "#000000";
    String textColor = "#CCCCCC";
    int horBorderheight = 1;
    int verticalBorderWidth = 1;
    int topSpace = 30;
    public HourLine(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public HourLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
    }

    public HourLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init(){
        view = LayoutInflater.from(context).inflate(R.layout.time_item,this);
        textView = view.findViewById(R.id.text);
        vwHorizontal = view.findViewById(R.id.vw_horizontal);
        vwVertical = view.findViewById(R.id.vw_vertical);
        updateView();
    }

    public int getHorViewPosition(){
        return (int)vwHorizontal.getY();
    }
    private void updateView(){
        textView.setTextColor(Color.parseColor(textColor));
        vwVertical.setBackgroundColor(Color.parseColor(textColor));
        vwHorizontal.setBackgroundColor(Color.parseColor(textColor));
    }
    public void setSelected(boolean selected){
        if(selected) {
            textView.setTextColor(Color.parseColor(selectedTextColor));
        }
        else {
            textView.setTextColor(Color.parseColor("#FFFFFF"));
        }

        //vwHorizontal.setBackgroundColor(Color.parseColor(selectedTextColor));
        invalidate();

    }
    public void setText(String name){
        textView.setText(name);
    }
    public String getText(){return textView.getText().toString();}
    public String getSelectedTextColor() {
        return selectedTextColor;
    }

    public void setSelectedTextColor(String selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
        textView.setTextColor(Color.parseColor(textColor));
    }

    public int getHorBorderHeight() {
        return horBorderheight;
    }

    public void setHorBorderHeight(int horBorderWidth) {
        this.horBorderheight = horBorderWidth;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)vwHorizontal.getLayoutParams();
        layoutParams.height = horBorderWidth;
        vwHorizontal.setLayoutParams(layoutParams);
    }

    public int getVerticalBorderWidth() {
        return verticalBorderWidth;
    }

    public void setVerticalBorderWidth(int verticalBorderWidth) {
        this.verticalBorderWidth = verticalBorderWidth;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)vwVertical.getLayoutParams();
        layoutParams.width = verticalBorderWidth;
        vwVertical.setLayoutParams(layoutParams);
        invalidate();
    }
    public void setTopSpace(int space){
        this.topSpace = space;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)textView.getLayoutParams();
        layoutParams.topMargin = space;
        textView.setLayoutParams(layoutParams);
        invalidate();
    }
}
