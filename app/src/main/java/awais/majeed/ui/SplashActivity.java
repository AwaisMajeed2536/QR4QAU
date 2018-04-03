package awais.majeed.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import awais.majeed.R;
import awais.majeed.services.ValidateMacAddressService;
import awais.majeed.services.core.Result;
import awais.majeed.utils.UtilHelpers;

public class SplashActivity extends AppCompatActivity implements Result<String> {

    private final int PERMISSIONS_REQUEST_KEY = 239;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getPermissions();

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String macAddress = "02:00:00:00:00:00";
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            macAddress = UtilHelpers.getMacAddr();
        }
        if (macAddress.equals("02:00:00:00:00:00")) {
            new AlertDialog.Builder(this).setTitle("Failed!").setMessage("could't find device MAC address")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            onBackPressed();
                        }
                    }).create().show();
        } else {
            ValidateMacAddressService.newInstance(this, false, this).callService(macAddress);
        }
    }

    public void getPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_KEY);
    }

    @Override
    public void onSuccess(String data, int requestId) {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }

    @Override
    public void onFailure(String message, int requestId) {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("RESTRICTED ACCESS!")
                .setIcon(R.drawable.warning_icon)
                .setMessage("Your device cannot access this app.")
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create().show();
    }

    @Override
    public void onError(Throwable throwable, int requestId) {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("Error!")
                .setIcon(R.drawable.warning_icon)
                .setMessage(throwable.getMessage())
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create().show();
    }
}
