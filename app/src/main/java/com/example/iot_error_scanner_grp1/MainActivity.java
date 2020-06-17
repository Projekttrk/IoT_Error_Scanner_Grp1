package com.example.iot_error_scanner_grp1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {


    Button btnToScan;
    TextView TVresult;
    EditText TVbackendIP;

    public static final String URL_EXTENSION = "/api";
    public static final String URL_FRONT = "http://";
    // change port in production -> python flask uses 5000 by default for dev server
    public static final String PORT = ":5000";


    public static final int REQUEST_CODE = 100;
    // 200 is usage permission code
    public static final int PERMISSION_REQUEST = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnToScan = findViewById(R.id.buttonToScan);
        TVresult  = findViewById(R.id.resultTextView);



        // ask for camera permission
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
    protected void onActivityResult(int requestCode, final int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TVbackendIP = findViewById(R.id.backendIpTextView);
        Editable editableURL = TVbackendIP.getText();
        String baseURL = editableURL.toString();

        if (data != null)  {
            final Barcode barcode = data.getParcelableExtra("barcode");

            // HTTP request starts
            String json = "{\"id\":1,\"error\":\"" + barcode.displayValue + "\"}";

            Log.d("MYURL", URL_FRONT + baseURL + PORT + URL_EXTENSION);
            OkHttpClient httpClient = new OkHttpClient();

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"), json);

            Request request = new Request.Builder()
                    .url(URL_FRONT + baseURL + PORT + URL_EXTENSION)
                    .post(body)
                    .build();

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull final IOException e) {

                    Log.d("HTTP_CONN", e.toString());
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {

                    String jsonData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        if (jsonObject.getBoolean("processed")) {
                            TVresult.setText(jsonObject.getString("msg"));
                        }
                        Log.d("HTTP_CONN", jsonObject.getString("msg"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }
}

