package awais.majeed.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.Result;

import awais.majeed.R;
import awais.majeed.utils.UtilHelpers;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String TAG = "MainActivity";
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        UtilHelpers.hideUI(this);
        if(mScannerView== null)
            return;
        mScannerView.setResultHandler(this); // Register as a handler for scan results.
        mScannerView.startCamera();     // Start camera on resume
        mScannerView.resumeCameraPreview(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        if(mScannerView== null)
            return;
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        Intent intent = new Intent(MainActivity.this, ShowResultActivity.class);
        intent.putExtra(ShowResultActivity.SCAN_RESULT_KEY, rawResult.getText().toString());
        Log.d(TAG, rawResult.getText().toString());
        startActivity(intent);
        finish();
        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }

}
