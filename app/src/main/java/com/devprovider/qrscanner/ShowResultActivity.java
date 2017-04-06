package com.devprovider.qrscanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowResultActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SCAN_RESULT_KEY = "scan_result_key";
    protected TextView showResultTextview;
    protected Button scanAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_show_result);
        initView();
        String scanResult = getIntent().getExtras().getString(SCAN_RESULT_KEY);
        showResultTextview.setText(scanResult);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.scan_again_button) {
            finish();
        }
    }

    private void initView() {
        showResultTextview = (TextView) findViewById(R.id.show_result_textview);
        scanAgainButton = (Button) findViewById(R.id.scan_again_button);
        scanAgainButton.setOnClickListener(ShowResultActivity.this);
    }
}
