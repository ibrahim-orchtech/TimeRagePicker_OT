package com.OrchTech.rectangleshape;

import android.content.Context;
import android.graphics.Canvas;
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

public class TimePicker extends RelativeLayout {
    RelativeLayout layout;
    CustomScrollView scrollView;
    int[] hoursPos = new int[2];
    boolean is_scroll = false;
    int scroll_value = 0;
    View view;
    private HoursViewGroup hoursViewGroup;
    private ShapeView shapeView;
    private Context context;
    private int selected_item = -1;
    private int step = 0;
    private float stepRatio = 0.25f;
    private int cell_height = 100;
    private ArrayList<Pair<Integer, Integer>> positions = new ArrayList<>();
    private int circle_width =100;
    private int countBall2=0;
    private int countBall1=0;
    TextView textView1;
    TextView textView2;
    final int textId1 = 0X001;
    final int textId2 = 0X002;
    private String [] fraction = new String []{"00","15","30","45"};
    int moveBall1= 0;
    int moveBall2=0;
    public TimePicker(Context context) {
        super(context);
        this.context = context;
        positions = new ArrayList<>();
        init();
    }

    public void init() {

        view = LayoutInflater.from(context).inflate(R.layout.time_picker_layout, this);

        layout = view.findViewById(R.id.layout);
        scrollView = view.findViewById(R.id.scrollView);
        //scrollView.setScrollingEnabled(false);
        RelativeLayout.LayoutParams textParam1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                100);
        RelativeLayout.LayoutParams textParam2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                100);

        textView1 = new TextView(context);
        textView2 = new TextView(context);
        textView1.setLayoutParams(textParam1);
        textView2.setLayoutParams(textParam2);
        textView1.setId(textId1);
        textView2.setId(textId2);
        textView1.setGravity(Gravity.CENTER);
        textView2.setGravity(Gravity.CENTER);
        textView1.setX(10);
        textView1.setTextSize(12);
        textView2.setX(10);
        textView2.setTextSize(12);

        shapeView = new ShapeView(context, new MoveInterface() {
            @Override
            public void onMove(Canvas canvas, int ball_id, int x1, int y1, int x2, int y2) {
                scrollView.setScrollingEnabled(false);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (ball_id == 0) {
                    int dif = y1-(shapeView.getColorballs().get(0).getY());

                    Log.e("dif",dif+"");
                    if(dif<0)dif*=-1;
                    if(dif<step)return;

                    if (y1 < 0) {
                        Log.e("ball_1","Up");

                        countBall1--;
                        step = (int)(cell_height*stepRatio);
                        if(countBall1%4==0)step++;
                        if (shapeView.getY() <= 0) return;

                        scrollView.scrollBy(0, -step);

                        shapeView.setY(shapeView.getY() - step);
                        shapeView.getLayoutParams().height = shapeView.getHeight() + step;
                        shapeView.setLayoutParams(shapeView.getLayoutParams());
                        shapeView.updateViews(canvas, x1, y1, x2, shapeView.getHeight()+step);
                        showTime(0,true,stepRatio,x1,(int)shapeView.getY());
                    }
                    else {

                        Log.e("ball_1","down");
                        Log.e("height_old",shapeView.getHeight()+"");
                        if (shapeView.getHeight() <= 150) {
                            return;
                        }

                        countBall1++;
                        step = (int)(cell_height*stepRatio);
                        if(countBall1%4==0)step++;
                        shapeView.setY(shapeView.getY() + step);
                        shapeView.getLayoutParams().height = shapeView.getHeight() - step;

                        shapeView.setLayoutParams(shapeView.getLayoutParams());
                        Log.e("height",shapeView.getHeight()+"");

                        shapeView.updateViews(canvas, x1, (int)shapeView.getY(), x2, shapeView.getHeight()-step);
                        showTime(0,false,stepRatio,x1,(int)(shapeView.getY()));


                    }

                } else {
                    int dif = y2-shapeView.getColorballs().get(1).getY()-shapeView.getColorballs().get(1).getHeightOfCircle();
                    if(dif<0)dif*=-1;
                    if(dif<step)return;

                    // ball 2 scroll down
                    if (y2 > shapeView.getHeight()) {
                        //cell_height++;
                        countBall2++;
                        step = (int)(cell_height*stepRatio);
                        if(countBall2%4==0)step++;
                        if (y2 > layout.getHeight()) return;
                        if (shapeView.getY() + shapeView.getHeight() >= layout.getY() + layout.getHeight()+25)
                            return;
                        if (shapeView.getY() + shapeView.getHeight() >=
                                scrollView.getY() + scrollView.getHeight()) {
                            scrollView.scrollBy(0, step);
                        }
                        shapeView.setY(shapeView.getY() );
                        shapeView.getLayoutParams().height = shapeView.getHeight() + step;
                        shapeView.setLayoutParams(shapeView.getLayoutParams());
                        Log.e("height_",shapeView.getHeight()+"");
                        shapeView.updateViews(canvas, x1, y1, x2, shapeView.getHeight()+step);
                        shapeView.invalidate();
                        showTime(1,false,stepRatio,x1,(int)(shapeView.getY()+shapeView.getHeight()+step-shapeView.getCircleWidth()));


                    } else {
                        // ball 2 scroll up
                        // width of circle * 3/2

                        if (shapeView.getHeight() <= 150) {
                            return;
                        }
                        //cell_height--;
                        countBall2--;
                        step = (int)(cell_height*stepRatio);
                        if(countBall2%4==0)step++;
                        shapeView.getLayoutParams().height = shapeView.getHeight() - step;
                        shapeView.setLayoutParams(shapeView.getLayoutParams());

                        shapeView.updateViews(canvas, x1, y1, x2, shapeView.getHeight()-step);
                        showTime(1,true,stepRatio,x1,(int)(shapeView.getHeight()-step+shapeView.getY())-shapeView.getCircleWidth());

                    }
                }
                shapeView.invalidate();
            }

            @Override
            public void onStartMove() {
                Log.e("event", "start");
                scrollView.setScrollingEnabled(false);
            }

            @Override
            public void onFinishMove(int y) {
                Log.e("event", "finish");

                scrollView.setScrollingEnabled(true);

            }
        });
        HoursViewGroup.Builder builder = new HoursViewGroup.Builder(context, new DrawerInterface() {
            @Override
            public void onDrawCell(int index, int x, int y) {

                if (index <= positions.size()){
                    if(index==0){
                        positions.add(new Pair<Integer, Integer>(x,0));
                    }

                    else if(index==1){
                        int current_y = positions.get(index-1).second
                                +hoursViewGroup.getItems().get(0).getHorBorderHeight()
                                +hoursViewGroup.getItems().get(0).getHorViewPosition();
                        positions.add(new Pair<>(x,current_y));
                    }
                    else{
                        int current_y = positions.get(index-1).second+hoursViewGroup.getItemHeight();
                        positions.add(new Pair<>(x,current_y));
                        if(index==23){
                            int last_y = positions.get(index).second+hoursViewGroup.getItemHeight();
                            positions.add(new Pair<>(x,last_y));
                        }
                    }
                    //positions.add(new Pair<>(x, y));
                }
                if (index == 23) {
                    RelativeLayout.LayoutParams shapeLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            positions.get(2).second - positions.get(1).second+(shapeView.getCircleWidth()));
                    shapeView.setX(200);
                    shapeView.setY(positions.get(14).second-(shapeView.getCircleWidth()/2));
                    shapeLayoutParams.setMargins(100, 0, 0, 0);
                    shapeView.setLayoutParams(shapeLayoutParams);

                    cell_height = positions.get(2).second-positions.get(1).second;
                    step = (int)(cell_height*stepRatio);
                    shapeView.setStep(step);
                    layout.addView(shapeView);
                    for(int i = 0 ;i<hoursViewGroup.getItems().size();i++){
                        Log.e("index_pos"+i,hoursViewGroup.getItems().get(i).getText()+"");
                    }
                }
            }

        }

        );
        hoursViewGroup = builder.build();
        hoursViewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                hoursViewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                hoursViewGroup.getLocationOnScreen(hoursPos);


            }
        });
        RelativeLayout.LayoutParams hoursLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        hoursViewGroup.setLayoutParams(hoursLayoutParams);

        layout.addView(hoursViewGroup);

        //textView1.setVisibility(GONE);
        //textView2.setVisibility(GONE);
        layout.addView(textView1);
        layout.addView(textView2);
        layout.invalidate();
        //addView(shapeView);
    }
    private void drawText(int y){
        //hoursViewGroup.showTime();
    }
    public void showTime(int ball,boolean isUp,float stepRation,int x,int y){

        int selected_index = -1;

        int midOfCircle = y+(shapeView.getCircleWidth()/2);
        for(int i = 0;i<positions.size();i++){
            if(positions.get(i).second>midOfCircle){
                selected_index = i-1;
                break;
            }
        }
        if(selected_index==-1)selected_index = 24;

        if(ball==0){
            if(isUp) moveBall1--;
            else moveBall1++;
            moveBall1 = (moveBall1+4)%4;
            Log.e("moveBall1",moveBall1+"");
            if(moveBall1==0){
                textView1.setVisibility(GONE);

                if(isUp){
                    if(selected_index!=0){
                        hoursViewGroup.getItems().get(selected_index-1).setSelected(true);
                    }
                    else{
                        hoursViewGroup.getItems().get(selected_index).setSelected(true);
                    }

                }
                else{
                    if(selected_index!=0){
                        hoursViewGroup.getItems().get(selected_index-1).setSelected(true);
                    }
                    else{
                        hoursViewGroup.getItems().get(selected_index).setSelected(true);

                    }

                }
            }
            else{
                textView1.setVisibility(VISIBLE);
                String hour  = "";
                if(selected_index!=0) {
                    hour = hoursViewGroup.getItems().get(selected_index - 1).getText();
                    if(isUp){
                        Log.e("isUp_ball1",selected_index+"");
                        hoursViewGroup.getItems().get(selected_index).setSelected(false);
                    }
                    else {
                        hoursViewGroup.getItems().get(selected_index - 1).setSelected(false);
                    }

                }
                else {
                    hour = "12:00 Am";
                    hoursViewGroup.getItems().get(0).setSelected(false);

                }

                textView1.setText(hour.substring(0,3)+
                        fraction[moveBall1]+hour.substring(5));
                textView1.setY(y);
                textView1.invalidate();


            }
        }
        else{
            if(isUp) moveBall2--;
            else moveBall2++;
            moveBall2 = (moveBall2+4)%4;
            Log.e("moveBall1",moveBall2+"");
            if(moveBall2==0){
                textView2.setVisibility(GONE);
                if(isUp){
                    hoursViewGroup.getItems().get(selected_index).setSelected(false);
                    hoursViewGroup.getItems().get(selected_index-1).setSelected(true);

                }
                else{
                    hoursViewGroup.getItems().get(selected_index-2).setSelected(false);
                    hoursViewGroup.getItems().get(selected_index-1).setSelected(true);
                }
            }
            else{
                textView2.setVisibility(VISIBLE);
                if(isUp)
                hoursViewGroup.getItems().get(selected_index).setSelected(false);
                else
                    hoursViewGroup.getItems().get(selected_index-1).setSelected(false);
                textView2.setText(hoursViewGroup.getItems().get(selected_index-1).getText().substring(0,3)+
                        fraction[moveBall2]+hoursViewGroup.getItems().
                        get(selected_index-1).getText().substring(5));
                textView2.setY(y);
                textView2.invalidate();
                layout.invalidate();

            }
        }





            /*else {

                if (isUp){
                        hoursViewGroup.getItems().get(selected_index).setSelected(false);
                }

                else {
                    if(selected_index!=0) {
                        hoursViewGroup.getItems().get(selected_index).setSelected(false);
                    }
                }
                //textView1.setVisibility(VISIBLE);
            }*/

        /*if(ball==0) {
            if (isUp) {
                moveBall1--;
            } else {
                moveBall1++;
            }

            moveBall1 = (moveBall1 + 4) % 4;
            if (moveBall1 == 0) {

                textView1.setVisibility(GONE);
                if(selected_index!=0)
                hoursViewGroup.getItems().get(selected_index).setSelected(true);

            } else {

                if (isUp)
                    if(selected_index!=0)
                    hoursViewGroup.getItems().get(selected_index).setSelected(false);
                else {
                    if(selected_index!=0)
                    hoursViewGroup.getItems().get(selected_index - 1).setSelected(false);
                }
                textView1.setVisibility(VISIBLE);
            }

            textView1.setX(10);
            String hour  = "";
            if(selected_index!=0)
                hour = hoursViewGroup.getItems().get(selected_index-1).getText();
            else
                hour = "12:00 Am";
            textView1.setText(hour.substring(0,3)+
                    fraction[moveBall1]+hour.substring(5));
            textView1.setX(10);
            textView1.setY(y);
            textView1.setTextSize(12);
            textView1.invalidate();
            layout.invalidate();
        }
        else{
            if(isUp){
                moveBall2--;
            }
            else{
                moveBall2++;
            }


            moveBall2 = (moveBall2+4)%4;

            int index = selected_index-1;
            if(index<0)index+=hoursViewGroup.getItems().size();
            if(moveBall2==0){
                textView2.setVisibility(GONE);
                hoursViewGroup.getItems().get(index).setSelected(true);
            }
            else {
                if(isUp)
                hoursViewGroup.getItems().get(index+1).setSelected(false);
                else
                    hoursViewGroup.getItems().get(index).setSelected(false);

                textView2.setVisibility(VISIBLE);
            }
            textView2.setX(10);
            textView2.setText(hoursViewGroup.getItems().get(index).getText().substring(0,3)+
                    fraction[moveBall2]+hoursViewGroup.getItems().
                    get(index).getText().substring(5));
            textView2.setY(y);
            textView2.setTextSize(12);
            textView2.invalidate();
            layout.invalidate();

        }*/
        invalidate();
    }
    public String getStart(){
        return textView1.getText().toString();
    }
    public String getLast(){
        return textView2.getText().toString();
    }


    public interface DrawerInterface {
        void onDrawCell(int index, int x, int y);
    }

    public interface MoveInterface {

        void onMove(Canvas canvas, int ball_id, int x1, int y1, int x2, int y2);

        void onStartMove();

        void onFinishMove(int y);
    }
}
