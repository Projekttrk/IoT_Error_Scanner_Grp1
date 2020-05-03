package com.example.iot_error_scanner_grp1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;


public class MainActivity extends AppCompatActivity {


    Button btnToScan;
    TextView TVresult;

    // we don't need this number, can be any int > 0
    public static final int REQUEST_CODE = 100;
    // 200 is usage permission code
    public static final int PERMISSION_REQUEST = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnToScan = findViewById(R.id.buttonToScan);
        TVresult  = findViewById(R.id.resultTextView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        btnToScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toScanActivity = new Intent(MainActivity.this, ScanActivity.class);
                startActivityForResult(toScanActivity, REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null)  {
            final Barcode barcode = data.getParcelableExtra("barcode");
            TVresult.post(new Runnable() {
                @Override
                public void run() {
                    TVresult.setText(barcode.displayValue);
                }
            });
        }
    }
}
