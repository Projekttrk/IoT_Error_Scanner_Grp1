package com.example.iot_error_scanner_grp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PopupActivity extends AppCompatActivity {

    TextView possibleSolutionTV;
    TextView problemTV;
    Button solvedButton;
    Button furtherHelpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        possibleSolutionTV = findViewById(R.id.solutionTV);
        problemTV = findViewById(R.id.problemTV);

        final String problemText = getIntent().getStringExtra("errorDescription");
        final String possibleSolution = getIntent().getStringExtra("solution");
        final String number = getIntent().getStringExtra("number");
        final String color = getIntent().getStringExtra("color");

        // url stuff
        final String URL_FRONT = getIntent().getStringExtra("URL_FRONT");
        final String baseURL = getIntent().getStringExtra("baseURL");
        final String PORT = getIntent().getStringExtra("PORT");
        final String URL_EXTENSION = getIntent().getStringExtra("URL_EXTENSION");

        possibleSolutionTV.setText(possibleSolution);
        problemTV.setText("Problem: " + problemText);


        solvedButton = findViewById(R.id.solvedButton);
        furtherHelpButton = findViewById(R.id.furtherHelpButton);

        solvedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = "{\"number\":" + "false" + ",\"color\":\"" + color + "\"" + ",\"is_first\":false}";

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

                            if (jsonObject.getBoolean("request_ok")) {
                                possibleSolutionTV.setText("Problem logged as solved!");
                            } else {
                                possibleSolutionTV.setText("Internal server error, contact backend!");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        furtherHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = "{\"number\":\"" + number + "\",\"color\":\"" + color + "\"" + ",\"is_first\":false}";

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

                            if (jsonObject.getBoolean("request_ok")) {
                                possibleSolutionTV.setText("Problem forwarded to Prisma!");
                            } else {
                                possibleSolutionTV.setText("Internal server error, contact backend!");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }
}