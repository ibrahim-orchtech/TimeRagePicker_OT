package com.OrchTech.rectangleshape;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.OrchTech.timerangpicker_ot.TimePicker;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        Button button = findViewById(R.id.btn_get);
        final TextView from = findViewById(R.id.from);
        final TextView to = findViewById(R.id.to);
        timePicker = findViewById(R.id.timePicker);
        timePicker.invalidate();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from.setText(timePicker.getStartTime());
                to.setText(timePicker.getEndTime());
                //     startActivity(new Intent(MainActivity.this, MainActivity.class));

            }
        });
        //timePicker.setSelectedCell(2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        timePicker.setSelectedIndex(5);
    }
}
