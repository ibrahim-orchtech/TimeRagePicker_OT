package com.OrchTech.timerangpicker_ot;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ibrahim.ali
 * Date 05,December,2019
 * Company OrchTech
 */
public class TimePicker extends RelativeLayout {
    static final int TEXT_ID1 = 0X001;
    static final int TEXT_ID2 = 0X002;
    static final int FIRST_BALL_ID = 0;
    static final int SECOND_BALL_ID = 1;
    static final int NUM_OF_HOURS = 24;
    static final String GRAY_COLOR = "#CCCCCC";
    static final String STRING_12 = "12:00";
    static final String STRING_12_AM = "12:00 AM";

    RelativeLayout layout;
    CustomScrollView scrollView;
    int[] hoursPos = new int[2];
    View view;
    TextView textView1;
    TextView textView2;
    int oldHeight = -1;
    int difInHeight = 0;
    private HoursViewGroup hoursViewGroup;
    private ShapeView shapeView;
    private Context context;
    private int step = 0;
    private float stepRatio = 0.25f;
    private int numOfStepPerCell = 4;
    private int cellHeight = 157;
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
    private int shapeStrokeColor = Color.parseColor(GRAY_COLOR);
    private int shapeRadius = 30;
    private int selectedCell = 4;
    private int selectedIndexFirstBall = -1;
    private int selectedIndexSecondBall = -1;
    private int topSpace = 100;
    MoveInterface moveInterface = new MoveInterface() {
        @Override
        public void onMove(Canvas canvas, int ballId, int x1, int y1, int x2, int y2) {
            scrollView.setScrollingEnabled(false);
            // top ball
            if (ballId == FIRST_BALL_ID) {
                int dif = Math.abs(y1 - (shapeView.getBalls().get(0).getY()));
                step = (int) (cellHeight * stepRatio);
                if (dif < step) return;

                //Move Up
                if (y1 < 0) {
                    moveFirstBallUp(x2);
                }
                // Move Down
                else {
                    moveFirstBallDown(x2);
                }

            }
            // second Ball
            else {
                int dif = Math.abs(y2 - shapeView.getBalls().get(1).getY() - shapeView.getBalls().get(1).getHeightOfCircle());
                step = (int) (cellHeight * stepRatio);
                if (dif < step) return;
                // ball 2 scroll down
                if (y2 > shapeView.getHeight()) {
                    moveSecondBallDown(x2, y2);
                } else {
                    moveSecondBallUp(x2);
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
    };
    private int circleImage = R.mipmap.icon;
    DrawerInterface drawerInterface = new DrawerInterface() {
        @Override
        public void onDrawCell(int index, int x, int y) {

            //  here we save positions of each cell added to hoursGroup
            if (index <= positions.size()) {
                if (index == 0) {
                    positions.add(new Pair<>(x, 0));
                } else if (index == 1) {
                    int currentY = positions.get(index - 1).second
                            + hoursViewGroup.getItems().get(0).getHorBorderHeight()
                            + hoursViewGroup.getItems().get(0).getHorViewPosition();
                    positions.add(new Pair<>(x, currentY));
                } else {
                    int currentY = positions.get(index - 1).second + hoursViewGroup.getItemHeight();
                    positions.add(new Pair<>(x, currentY));
                    if (index == 23) {
                        int lastY = positions.get(index).second + hoursViewGroup.getItemHeight();
                        positions.add(new Pair<>(x, lastY));
                    }
                }
            }
            // draw shape after finish build hoursGroup
            if (index == NUM_OF_HOURS - 1) {
                calculateCellHeight();
                buildShape();
                setSelectedCell(selectedCell);
            }
        }

        @Override
        public void onClickCell(int index) {
            if (index == 0) return;
            setOnReSelectCell(index);
        }
    };
    private int textColor = Color.parseColor(GRAY_COLOR);
    private int selectedTextColor = Color.parseColor(GRAY_COLOR);
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
        super(context, attributeSet);
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
        textView1.setId(TEXT_ID1);
        textView2.setId(TEXT_ID2);
        textView1.setGravity(Gravity.CENTER);
        textView2.setGravity(Gravity.CENTER);
        textView1.setX(10);
        textView1.setTextSize(textSize - 2);
        textView2.setX(10);
        textView2.setTextSize(textSize - 2);
    }

    private void buildShape() {
        shapeView = new ShapeView(context, moveInterface);
        shapeView.setRaduis(shapeRadius);
        shapeView.setCircleHeight(circleHeight);
        shapeView.setStrokeWidth(shapeStroke);
        shapeView.setStrockColor(shapeStrokeColor);
        shapeView.setCircleImage(circleImage);

        shapeView.invalidate();

    }

    private void buildHoursLayout() {
        HoursViewGroup.Builder builder = new HoursViewGroup.Builder(context, drawerInterface);
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

    public void setSelectedIndex(int index) {
        selectedCell = index;
    }

    public void setSelectedCell(int cellIndex) {
        selectedCell = cellIndex;
        int top;
        int bottom;
        moveBall1 = moveBall2 = countBall1 = countBall2 = 0;
        if (selectedCell == 0) {
            top = positions.get(0).second;
            bottom = positions.get(1).second;
        } else {
            top = positions.get(4).second;
            bottom = positions.get(5).second;
        }
        if (selectedIndexFirstBall != -1) {
            hoursViewGroup.getItems().get(selectedIndexFirstBall).setSelected(false);
        }
        if (selectedIndexSecondBall != -1) {
            hoursViewGroup.getItems().get(selectedIndexSecondBall).setSelected(false);

        }
        selectedIndexFirstBall = selectedCell - 1;
        selectedIndexSecondBall = selectedCell;
        layout.removeView(shapeView);
        buildShape();
        LinearLayout.LayoutParams shapeLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (bottom - top) + (shapeView.getCircleHeight()));
        oldHeight = shapeLayoutParams.height;
        if (getResources().getConfiguration().locale.equals(new Locale("ar"))) {
            shapeView.setX(0);
            shapeLayoutParams.setMargins(0, 0, 140, 0);
        } else {
            shapeView.setX(hoursViewGroup.getTextWidth() + 30);
            shapeLayoutParams.setMargins(0, 0, 0, 0);
        }
        shapeView.setY(positions.get(selectedCell).second - (shapeView.getCircleHeight() / 2));
        shapeView.setLayoutParams(shapeLayoutParams);
        shapeView.invalidate();
        layout.addView(shapeView);
        layout.removeView(textView1);
        layout.removeView(textView2);
        if (selectedIndexFirstBall != -1) {
            hoursViewGroup.getItems().get(selectedIndexFirstBall).setSelected(true);
        }
        hoursViewGroup.getItems().get(selectedIndexSecondBall).setSelected(true);
        if (selectedIndexFirstBall != -1) {
            textView1.setText(hoursViewGroup.getItems().get(selectedIndexFirstBall).getText());
        }
        textView2.setText(hoursViewGroup.getItems().get(selectedIndexSecondBall).getText());

    }

    public void setOnReSelectCell(int cellIndex) {
        int height = (shapeView.getBalls().get(1).getY() + (shapeView.getBalls().get(1).getHeightOfCircle() / 2))
                - (shapeView.getBalls().get(0).getY() + (shapeView.getBalls().get(0).getHeightOfCircle() / 2));
        int numOfMovies = height / step;
        int numOfCells = (int) (numOfMovies * stepRatio);
        selectedCell = cellIndex;
        if (cellIndex - 1 + numOfCells > 23) {
            int dif = cellIndex - 1 + numOfCells - 23;
            setOnReSelectCell(cellIndex - dif);
            return;
        }
        if (selectedIndexFirstBall != -1) {
            hoursViewGroup.getItems().get(selectedIndexFirstBall).setSelected(false);
        }
        if (selectedIndexSecondBall != -1) {
            hoursViewGroup.getItems().get(selectedIndexSecondBall).setSelected(false);
        }
        selectedIndexFirstBall = cellIndex - 1;
        selectedIndexSecondBall = cellIndex - 1 + numOfCells;
        oldHeight = shapeView.getLayoutParams().height;
        layout.removeView(shapeView);
        buildShape();
        LinearLayout.LayoutParams shapeLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                oldHeight);
        shapeView.setLayoutParams(shapeLayoutParams);
        shapeView.setX(hoursViewGroup.getTextWidth() + 30);
        shapeView.setY(positions.get(selectedCell).second - (shapeView.getCircleHeight() / 2));
        shapeView.invalidate();
        layout.addView(shapeView);
        layout.removeView(textView1);
        layout.removeView(textView2);
        if (numOfMovies % ((int) (1 / stepRatio)) == 0) {
            hoursViewGroup.getItems().get(selectedIndexSecondBall).setSelected(true);
            textView2.setText(hoursViewGroup.getItems().get(selectedIndexSecondBall).getText());
        } else {
            String hour = hoursViewGroup.getItems().get(selectedIndexSecondBall).getText();
            moveBall2 = (numOfMovies % ((int) (1 / stepRatio)));
            textView2.setText(hour.substring(0, 3) +
                    fraction[Math.abs(moveBall2 + 4) % 4] + hour.substring(5));
            int y = positions.get(selectedIndexSecondBall + 1).second + ((moveBall2) * topSpace / 5);
            textView2.setY(y);
            textView2.invalidate();
            layout.addView(textView2);
            layout.invalidate();
        }
        if (selectedIndexFirstBall == -1) selectedIndexFirstBall = 0;
        hoursViewGroup.getItems().get(selectedIndexFirstBall).setSelected(true);
        textView1.setText(hoursViewGroup.getItems().get(selectedIndexFirstBall).getText());

    }

    void calculateCellHeight() {
        if (positions != null && positions.size() > 2) {
            cellHeight = positions.get(2).second - positions.get(1).second;
            step = (int) (stepRatio * cellHeight);
            difInHeight = cellHeight - (numOfStepPerCell * step);
            // min time is 0.5 hour
            minShapeHeight = circleHeight + (3 * step);

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
            shapeStrokeColor = a.getColor(R.styleable.TimePicker_shapeBorderColor, Color.parseColor(GRAY_COLOR));
            circleImage = a.getInt(R.styleable.TimePicker_circleImage, circleImage);
            shapeStroke = a.getInt(R.styleable.TimePicker_shapeBorderWidth, shapeStroke);
            shapeRadius = a.getInt(R.styleable.TimePicker_shapeRadius, shapeRadius);
            textColor = a.getColor(R.styleable.TimePicker_textColor, textColor);
            selectedTextColor = a.getColor(R.styleable.TimePicker_selectedTextColor, selectedTextColor);
            verLineWidth = a.getColor(R.styleable.TimePicker_verLineWidth, verLineWidth);
            horLineHeight = a.getColor(R.styleable.TimePicker_horLineHeight, horLineHeight);
            is24Hour = a.getBoolean(R.styleable.TimePicker_hourFormat, false);
        } finally {
            a.recycle();
        }

    }

    public void showTime(int ball, boolean isUp, int y) {
        if (ball == FIRST_BALL_ID) {
            moveFirstBall(isUp, y);
        } else {
            moveSecondBall(isUp, y);
        }
    }
    private void updateFirstBallMove(boolean isUp){
        layout.removeView(textView1);
        if (isUp) moveBall1--;
        else
            moveBall1++;
        if (selectedIndexFirstBall != -1) {
            hoursViewGroup.getItems().get(selectedIndexFirstBall).setSelected(false);
        }

    }
    private void markSelectedCellForTopCircle(){
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
            String hour = STRING_12_AM;
            if (hoursViewGroup.is24Hours()) hour = STRING_12;

            textView1.setText(hour);
            textView1.setY(0);
            textView1.setVisibility(VISIBLE);
            layout.addView(textView1);

        }
        moveBall1 = 0;



    }
    private void displayFractionForTopCircle(boolean isUp,int y){
        if (selectedIndexFirstBall != -1) {
            hoursViewGroup.getItems().get(selectedIndexFirstBall).setSelected(false);
        }
        textView1.setVisibility(VISIBLE);
        layout.removeView(textView1);
        String hour = STRING_12_AM;
        if (selectedIndexFirstBall >= 0) {
            if (isUp) {
                if (selectedIndexFirstBall == 0) {
                    if (hoursViewGroup.is24Hours()) hour = STRING_12;
                } else {
                    hour = hoursViewGroup.getItems().get(selectedIndexFirstBall - 1).getText();
                }
            } else
                hour = hoursViewGroup.getItems().get(selectedIndexFirstBall).getText();

        } else if (hoursViewGroup.is24Hours()) hour = STRING_12;
        updateTextView1(y,hour);

    }
    private void moveFirstBall(boolean isUp, int y) {
        updateFirstBallMove(isUp);
        if (Math.abs(moveBall1) == 4) {
            markSelectedCellForTopCircle();
        }
        else if (moveBall1 == 0) {
            hoursViewGroup.getItems().get(selectedIndexFirstBall).setSelected(true);

        }
        else {
            displayFractionForTopCircle(isUp,y);
        }

    }
    private void updateTextView1(int y,String hour){

        textView1.setText(hour.substring(0, 3) +
                fraction[Math.abs(moveBall1 + 4) % 4] + hour.substring(5));
        textView1.setLayoutParams(textView1.getLayoutParams());
        textView1.setY(y - (topSpace / 5));
        textView1.invalidate();
        layout.addView(textView1);
        layout.invalidate();
    }
    private void moveSecondBall(boolean isUp, int y) {

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
        }
        else if (moveBall2 == 0) {
            hoursViewGroup.getItems().get(selectedIndexSecondBall).setSelected(true);
        }
        else {
            displayFractionForBottomCircle(isUp,y);
        }
    }
    void displayFractionForBottomCircle(boolean isUp,int y){
        textView2.setVisibility(VISIBLE);
        if (selectedIndexSecondBall != -1)
            hoursViewGroup.getItems().get(selectedIndexSecondBall).setSelected(false);
        String hour = hoursViewGroup.getItems().get(selectedIndexSecondBall).getText();
        if (isUp) {
            if (selectedIndexSecondBall == 0) {
                if (hoursViewGroup.is24Hours()) hour = STRING_12;
                else
                    hour = STRING_12_AM;
            } else {
                hour = hoursViewGroup.getItems().get(selectedIndexSecondBall - 1).getText();
            }
        }
        textView2.setText(hour.substring(0, 3) +
                fraction[Math.abs(moveBall2 + 4) % 4] + hour.substring(5));
        textView2.setY(y - (topSpace / 5));
        textView2.invalidate();
        layout.addView(textView2);
        layout.invalidate();

    }
    void moveFirstBallUp(int x2) {
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

        showTime(FIRST_BALL_ID, true, (int) shapeView.getY() + (shapeView.getCircleHeight() / 2));

    }

    void moveFirstBallDown(int x2) {
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

        showTime(FIRST_BALL_ID, false, (int) (shapeView.getY() + (shapeView.getCircleHeight() / 2)));

    }

    void moveSecondBallDown(int x2, int y2) {
        countBall2++;
        if (countBall2 % numOfStepPerCell == 0)
            step += difInHeight;
        if (y2 > layout.getHeight()) return;
        if (Math.abs(shapeView.getY() + shapeView.getHeight() - (shapeView.getCircleHeight() / 2)
                - positions.get(24).second) < step)
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

        showTime(SECOND_BALL_ID, false, (int) (shapeView.getY() + shapeView.getLayoutParams().height - (shapeView.getCircleHeight() / 2)));

    }

    void moveSecondBallUp(int x2) {

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
        showTime(SECOND_BALL_ID, true, (int) (shapeView.getY() + shapeView.getLayoutParams().height - (shapeView.getCircleHeight() / 2)));

    }
}
