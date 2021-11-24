package com.nhomduan.quanlydathang_admin.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhomduan.quanlydathang_admin.R;

import java.util.Timer;
import java.util.TimerTask;

public class FlashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(FlashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };

        timer.schedule(task, 1500);
    }
}
