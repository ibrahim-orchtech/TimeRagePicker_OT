package com.OrchTech.timerangpicker_ot;

import android.graphics.Canvas;


public interface MoveInterface {
    void onMove(Canvas canvas, int ball_id, int x1, int y1, int x2, int y2);
    void onStartMove();
    void onFinishMove(int y);
}
