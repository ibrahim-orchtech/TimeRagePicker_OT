package com.OrchTech.rectangleshape;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.OrchTech.timerangpicker_ot.ShapeView;
import com.OrchTech.timerangpicker_ot.TimePicker;

public class MainActivity extends AppCompatActivity {

    HoursViewGroup hoursViewGroup;
    ShapeView shapeView;
    ScrollView scrollView;
    TimePicker timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.btn_get);
        final TextView from = findViewById(R.id.from);
        final TextView to = findViewById(R.id.to);
        timePicker = findViewById(R.id.timePicker);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from.setText("from: "+ timePicker.getStartTime());
                to.setText("To: "+ timePicker.getEndTime());

            }
        });
    }
}