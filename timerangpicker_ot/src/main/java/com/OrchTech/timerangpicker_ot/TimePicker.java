package com.OrchTech.timerangpicker_ot;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ibrahim.ali
 * Date 05,December,2019
 * Company OrchTech
 */
public class TimePicker extends RelativeLayout {
    final int textId1 = 0X001;
    final int textId2 = 0X002;
    final int firstBallId = 0;
    final int secondBallId = 1;
    private final int numOfHours = 24;
    RelativeLayout layout;
    CustomScrollView scrollView;
    int[] hoursPos = new int[2];
    View view;
    TextView textView1;
    TextView textView2;
    private HoursViewGroup hoursViewGroup;
    private ShapeView shapeView;
    private Context context;
    private int step = 0;
    private float stepRatio = 0.25f;
    private int numOfStepPerCell = 4;
    private int cell_height = 157;
    private ArrayList<Pair<Integer, Integer>> positions = new ArrayList<>();
    private int countBall2 = 0;
    private int countBall1 = 0;
    private int moveBall1 = 0;
    private int moveBall2 = 0;
    private String[] fraction = new String[]{"00", "15", "30", "45"};
    private int textSize = 14;
    private int minShapeHeight = 200;
    private int shapeStroke = 5;
    private int circleHeight = 50;
    private int shapeStrokeColor = Color.parseColor("#CCCCCC");
    private int shapeRadius = 30;
    private int selected_cell = 4;
    private int selectedIndexFirstBall = -1;
    private int selectedIndexSecondBall = -1;
    private int topSpace = 100;
    private int circleImage = R.mipmap.icon;
    private int textColor = Color.parseColor("#CCCCCC");
    private int selectedTextColor = Color.parseColor("#CCCCCC");
    private int verLineWidth = 1;
    private int horLineHeight = 1;
    private boolean is24Hour = false;
    public TimePicker(Context context) {
        super(context);
        this.context = context;
        positions = new ArrayList<>();
        init();
    }

    public TimePicker(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        this.context = context;
        positions = new ArrayList<>();
        getAttributes(attributeSet);
        init();
    }

    private void init() {
        view = LayoutInflater.from(context).inflate(R.layout.time_picker_layout, this);
        layout = view.findViewById(R.id.layout);
        scrollView = view.findViewById(R.id.scrollView);
        buildTexts();
        buildHoursLayout();
    }

    private void buildTexts() {
        LayoutParams textParam1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParams textParam2 = new LayoutParams(LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textView1 = new TextView(context);
        textView2 = new TextView(context);
        textView1.setLayoutParams(textParam1);
        textView2.setLayoutParams(textParam2);
        textView1.setId(textId1);
        textView2.setId(textId2);
        textView1.setGravity(Gravity.CENTER);
        textView2.setGravity(Gravity.CENTER);
        textView1.setX(10);
        textView1.setTextSize(textSize - 2);
        textView2.setX(10);
        textView2.setTextSize(textSize - 2);
    }

    private void buildShape() {
        shapeView = new ShapeView(context, new MoveInterface() {
            @Override
            public void onMove(Canvas canvas, int ball_id, int x1, int y1, int x2, int y2) {
                scrollView.setScrollingEnabled(false);
                // top ball
                if (ball_id == firstBallId) {
                    int dif = y1 - (shapeView.getBalls().get(0).getY());
                    if (dif < 0) dif *= -1;
                    step = (int) (cell_height * stepRatio);
                    if (dif < step) return;

                    //Move Up
                    if (y1 < 0) {
                        countBall1--;
                        if (countBall1 % numOfStepPerCell == 0)
                            step += difInHeight;
                        if (shapeView.getY() <= 0) return;

                        if (shapeView.getY() <= scrollView.getScrollY() + step) {
                            scrollView.scrollBy(0, -step);
                        }

                        shapeView.setY(shapeView.getY() - step);
                        shapeView.getLayoutParams().height = shapeView.getHeight() + step;
                        shapeView.setLayoutParams(shapeView.getLayoutParams());
                        shapeView.updateViews(x2, shapeView.getHeight() + step);
                        shapeView.invalidate();
                        Log.e("shapeView_height", shapeView.getY() + " " + shapeView.getLayoutParams().height);

                        showTime(firstBallId, true, (int) shapeView.getY() + (shapeView.getCircleHeight() / 2));
                    }
                    // Move Down
                    else {

                        // min height to avoid first ball exceed second ball
                        if (shapeView.getHeight() <= minShapeHeight) {
                            return;
                        }
                        if (shapeView.getY() + shapeView.getCircleHeight() + step > scrollView.getScrollY() + scrollView.getHeight()) {
                            scrollView.scrollBy(0, step);
                        }
                        countBall1++;
                        if (countBall1 % numOfStepPerCell == 0)
                            step += difInHeight;
                        shapeView.setY(shapeView.getY() + step);
                        shapeView.getLayoutParams().height = shapeView.getHeight() - step;
                        shapeView.setLayoutParams(shapeView.getLayoutParams());
                        shapeView.updateViews(x2, shapeView.getHeight() - step);
                        shapeView.invalidate();
                        Log.e("shapeView", shapeView.getY() + " " + shapeView.getLayoutParams().height);

                        showTime(firstBallId, false, (int) (shapeView.getY() + (shapeView.getCircleHeight() / 2)));
                    }

                }
                // second Ball
                else {
                    int dif = y2 - shapeView.getBalls().get(1).getY() - shapeView.getBalls().get(1).getHeightOfCircle();
                    if (dif < 0) dif *= -1;
                    step = (int) (cell_height * stepRatio);
                    if (dif < step) return;
                    // ball 2 scroll down
                    if (y2 > shapeView.getHeight()) {

                        countBall2++;
                        if (countBall2 % numOfStepPerCell == 0)
                            step += difInHeight;
                        if (y2 > layout.getHeight()) return;
                        if (Math.abs(shapeView.getY() + shapeView.getHeight() - (shapeView.getCircleHeight() / 2)
                                -positions.get(24).second)<step)
                            return;
                        // to scroll down
                        if (shapeView.getY() + shapeView.getHeight() + step >=
                                scrollView.getScrollY() + scrollView.getHeight()) {
                            scrollView.scrollBy(0, step);
                        }
                        shapeView.setY(shapeView.getY());
                        shapeView.getLayoutParams().height = shapeView.getHeight() + step;
                        shapeView.setLayoutParams(shapeView.getLayoutParams());
                        shapeView.updateViews(x2, shapeView.getHeight() + step);
                        shapeView.invalidate();
                        Log.e("shapeView", shapeView.getY() + " " + shapeView.getLayoutParams().height);

                        showTime(secondBallId, false, (int) (shapeView.getY() + shapeView.getLayoutParams().height - (shapeView.getCircleHeight() / 2)));

                    } else {
                        // ball 2 scroll up
                        if (shapeView.getHeight() <= minShapeHeight) {
                            return;
                        }
                        if (shapeView.getY() + shapeView.getHeight() - scrollView.getScrollY() - step <= 0) {
                            scrollView.scrollBy(0, -1 * step);
                        }

                        countBall2--;
                        if (countBall2 % numOfStepPerCell == 0)
                            step += difInHeight;
                        shapeView.getLayoutParams().height = shapeView.getHeight() - step;
                        shapeView.setLayoutParams(shapeView.getLayoutParams());
                        shapeView.updateViews(x2, shapeView.getHeight() - step);
                        shapeView.invalidate();
                        Log.e("shapeView", shapeView.getY() + " " + shapeView.getLayoutParams().height);
                        showTime(secondBallId, true, (int) (shapeView.getY() + shapeView.getLayoutParams().height - (shapeView.getCircleHeight() / 2)));
                    }
                }
                shapeView.invalidate();
            }

            @Override
            public void onStartMove() {
                scrollView.setScrollingEnabled(false);
            }

            @Override
            public void onFinishMove(int y) {
                scrollView.setScrollingEnabled(true);

            }
        });
        shapeView.setRaduis(shapeRadius);
        shapeView.setCircleHeight(circleHeight);
        shapeView.setStrokeWidth(shapeStroke);
        shapeView.setStrockColor(shapeStrokeColor);
        shapeView.setCircleImage(circleImage);

        shapeView.invalidate();

    }

    private void buildHoursLayout() {
        HoursViewGroup.Builder builder = new HoursViewGroup.Builder(context, new DrawerInterface() {
            @Override
            public void onDrawCell(int index, int x, int y) {

                //  here we save positions of each cell added to hoursGroup
                if (index <= positions.size()) {
                    if (index == 0) {
                        positions.add(new Pair<>(x, 0));
                    } else if (index == 1) {
                        int current_y = positions.get(index - 1).second
                                + hoursViewGroup.getItems().get(0).getHorBorderHeight()
                                + hoursViewGroup.getItems().get(0).getHorViewPosition();
                        positions.add(new Pair<>(x, current_y));
                    } else {
                        int current_y = positions.get(index - 1).second + hoursViewGroup.getItemHeight();
                        positions.add(new Pair<>(x, current_y));
                        if (index == 23) {
                            int last_y = positions.get(index).second + hoursViewGroup.getItemHeight();
                            positions.add(new Pair<>(x, last_y));
                        }
                    }
                }
                // draw shape after finish build hoursGroup
                if (index == numOfHours - 1) {
                    calculateCellHeight();
                    buildShape();
                    setSelectedCell(selected_cell);
                }
            }

        }
        );
        builder.is24Hour(is24Hour);
        builder.setFontSize(textSize);
        builder.setHorizontalViewHeight(horLineHeight);
        builder.setVerticalViewWidth(verLineWidth);
        hoursViewGroup = builder.build();
        hoursViewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                hoursViewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                hoursViewGroup.getLocationOnScreen(hoursPos);
            }
        });
        LayoutParams hoursLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        hoursViewGroup.setLayoutParams(hoursLayoutParams);
        layout.addView(hoursViewGroup);
        layout.addView(textView1);
        layout.addView(textView2);
        layout.invalidate();
    }

    public void setSelectedCell(int cell_index) {
        selected_cell = cell_index;
        int top, bottom;
        if (selected_cell == 0) {
            top = positions.get(0).second;
            bottom = positions.get(1).second;
        } else {
            top = positions.get(4).second;
            bottom = positions.get(5).second;
        }
        selectedIndexFirstBall = selected_cell - 1;
        selectedIndexSecondBall = selected_cell;
        Log.e("first_selection", selectedIndexFirstBall + " " + selectedIndexSecondBall);
        layout.removeView(shapeView);
        LayoutParams shapeLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (bottom - top) + (shapeView.getCircleHeight()));
        shapeView.setX(hoursViewGroup.getTextWidth() + 30);
        shapeLayoutParams.setMargins(0, 0, 0, 0);
        shapeView.setY(positions.get(selected_cell).second - (shapeView.getCircleHeight() / 2));
        shapeView.setLayoutParams(shapeLayoutParams);
        shapeView.invalidate();
        Log.e("shapeView", shapeView.getY() + (shapeView.getCircleHeight() / 2) + "");
        for (int i = 0; i < 24; i++) {
            Log.e("shapeView_" + i, positions.get(i).second + " ");
        }
        layout.addView(shapeView);
        layout.removeView(textView1);
        layout.removeView(textView2);
        hoursViewGroup.getItems().get(selectedIndexFirstBall).setSelected(true);
        hoursViewGroup.getItems().get(selectedIndexSecondBall).setSelected(true);
        textView1.setText(hoursViewGroup.getItems().get(selectedIndexFirstBall).getText());
        textView2.setText(hoursViewGroup.getItems().get(selectedIndexSecondBall).getText());

    }

    int difInHeight = 0;
    private void calculateCellHeight() {
        if (positions != null && positions.size() > 2) {
            cell_height = positions.get(2).second - positions.get(1).second;
            step = (int)(stepRatio*cell_height);
            difInHeight = cell_height-(numOfStepPerCell*step);

        }
    }

    public String getStartTime() {
        return textView1.getText().toString();
    }

    public String getEndTime() {
        return textView2.getText().toString();
    }

    private void getAttributes(AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TimePicker, 0, 0);
        try {
            circleHeight = a.getInt(R.styleable.TimePicker_circleHeight, circleHeight);
            topSpace = a.getInt(R.styleable.TimePicker_hourSpace, topSpace);
            if (topSpace < 100) {
                topSpace = 100;
            }
            shapeStrokeColor = a.getColor(R.styleable.TimePicker_shapeBorderColor, Color.parseColor("#CCCCCC"));
            circleImage = a.getInt(R.styleable.TimePicker_circleImage, circleImage);
            shapeStroke = a.getInt(R.styleable.TimePicker_shapeBorderWidth, shapeStroke);
            shapeRadius = a.getInt(R.styleable.TimePicker_shapeRadius, shapeRadius);
            textColor = a.getColor(R.styleable.TimePicker_textColor, textColor);
            selectedTextColor = a.getColor(R.styleable.TimePicker_selectedTextColor, selectedTextColor);
            verLineWidth = a.getColor(R.styleable.TimePicker_verLineWidth, verLineWidth);
            horLineHeight = a.getColor(R.styleable.TimePicker_horLineHeight, horLineHeight);
            is24Hour = a.getBoolean(R.styleable.TimePicker_hourFormat,false);
        } finally {
            a.recycle();
        }

    }

    public void showTime(int ball, boolean isUp, int y) {
        if (ball == firstBallId) {
            moveFirstBall(isUp,y);
        }
        else{
            moveSecondBall(isUp,y);
        }
    }
    private void moveFirstBall(boolean isUp, int y){

        layout.removeView(textView1);
        if (isUp) moveBall1--;
        else
            moveBall1++;
        if (selectedIndexFirstBall != -1) {
            hoursViewGroup.getItems().get(selectedIndexFirstBall).setSelected(false);
        }

        if (Math.abs(moveBall1) == 4) {
            if (selectedIndexFirstBall != -1)
                hoursViewGroup.getItems().get(selectedIndexFirstBall).setSelected(false);
            if (moveBall1 < 0) selectedIndexFirstBall -= 1;
            else selectedIndexFirstBall += 1;
            if (selectedIndexFirstBall != -1) {
                hoursViewGroup.getItems().get(selectedIndexFirstBall).setSelected(true);
                textView1.setVisibility(GONE);
                textView1.setText(hoursViewGroup.getItems().get(selectedIndexFirstBall).getText());
                layout.removeView(textView1);
            } else {
                String hour = "";
                if (hoursViewGroup.is24Hours()) hour = "12:00";
                else
                    hour = "12:00 Am";
                textView1.setText(hour);
                textView1.setY(0);
                textView1.setVisibility(VISIBLE);
                layout.addView(textView1);

            }
            moveBall1 = 0;


        } else if (moveBall1 == 0) {
            hoursViewGroup.getItems().get(selectedIndexFirstBall).setSelected(true);

        } else {
            if (selectedIndexFirstBall != -1) {
                hoursViewGroup.getItems().get(selectedIndexFirstBall).setSelected(false);
            }
            textView1.setVisibility(VISIBLE);
            layout.removeView(textView1);
            String hour = "";
            if (selectedIndexFirstBall >= 0) {
                if (isUp){
                    if (selectedIndexFirstBall == 0) {
                        if (hoursViewGroup.is24Hours()) hour = "12:00";
                        else
                            hour = "12:00 Am";
                    } else {
                        hour = hoursViewGroup.getItems().get(selectedIndexFirstBall - 1).getText();
                    }
                }
                else
                    hour = hoursViewGroup.getItems().get(selectedIndexFirstBall).getText();

            } else {
                if (hoursViewGroup.is24Hours()) hour = "12:00";
                else
                    hour = "12:00 Am";
            }

            textView1.setText(hour.substring(0, 3) +
                    fraction[Math.abs(moveBall1 + 4) % 4] + hour.substring(5));
            textView1.setLayoutParams(textView1.getLayoutParams());
            textView1.setY(y - (topSpace / 5));
            textView1.invalidate();
            layout.addView(textView1);
            layout.invalidate();
        }

    }
    private void moveSecondBall(boolean isUp,int y){

        layout.removeView(textView2);
        if (isUp) moveBall2--;
        else
            moveBall2++;
        if (selectedIndexSecondBall != -1) {
            hoursViewGroup.getItems().get(selectedIndexSecondBall).setSelected(false);
        }

        if (Math.abs(moveBall2) == 4) {
            if (selectedIndexSecondBall != -1)
                hoursViewGroup.getItems().get(selectedIndexSecondBall).setSelected(false);
            if (moveBall2 < 0) selectedIndexSecondBall -= 1;
            else selectedIndexSecondBall += 1;
            if (selectedIndexSecondBall != -1) {
                hoursViewGroup.getItems().get(selectedIndexSecondBall).setSelected(true);
                textView2.setVisibility(GONE);
                textView2.setText(hoursViewGroup.getItems().get(selectedIndexSecondBall).getText());
            }
            moveBall2 = 0;
        } else if (moveBall2 == 0) {
            hoursViewGroup.getItems().get(selectedIndexSecondBall).setSelected(true);
        }
        else {
            textView2.setVisibility(VISIBLE);
            if (selectedIndexSecondBall != -1)
                hoursViewGroup.getItems().get(selectedIndexSecondBall).setSelected(false);
            String hour ="";
            if(isUp){
                hour =hoursViewGroup.getItems().get(selectedIndexSecondBall-1).getText();
            }
            else{
                hour= hoursViewGroup.getItems().get(selectedIndexSecondBall).getText();
            }
            textView2.setText(hour.substring(0, 3) +
                    fraction[Math.abs(moveBall2+4)%4] + hour.substring(5));
            textView2.setY(y-(topSpace/5));
            textView2.invalidate();
            layout.addView(textView2);
            layout.invalidate();
        }
    }
}