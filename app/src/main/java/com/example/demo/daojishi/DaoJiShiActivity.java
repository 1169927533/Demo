package com.example.demo.daojishi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.demo.R;

public class DaoJiShiActivity extends AppCompatActivity {
MyView_Ceshi cpb_countdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dao_ji_shi);
        cpb_countdown = findViewById(R.id.cpb_countdown);


        cpb_countdown.setDuration(10*60*1000, new MyView_Ceshi.OnFinishListener() {
            @Override
            public void onFinish() {

            }
        }, new MyView_Ceshi.OnChangeListener() {
            @Override
            public void onChange(String dsa) {

            }
        });
    }
}
