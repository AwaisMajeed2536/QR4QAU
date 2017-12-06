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
import android.util.Log;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import awais.majeed.R;
import awais.majeed.services.ValidateMacAddressService;
import awais.majeed.services.core.Result;
import awais.majeed.utils.UtilHelpers;

public class SplashActivity extends AppCompatActivity implements Result<String> {

    private final int LOCATOIN_PERMISSION_REQUEST_KEY = 239;
    private final int GPS_ENABLE_REQUEST_KEY = 176;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String macAddress = "02:00:00:00:00:00";
        askLocationPermission();
        if (wifiManager.isWifiEnabled()) {
            macAddress = getMacAddr();
        }
        if(macAddress.equals("02:00:00:00:00:00")){
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

    private void askToEnableGPS() {
        AlertDialog gpsEnableDialog = new AlertDialog.Builder(this).setTitle("Location Service Disabled")
                .setMessage("This app needs location service to function properly")
                .setPositiveButton("okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(gpsOptionsIntent, GPS_ENABLE_REQUEST_KEY);
                    }
                }).setCancelable(false).create();
        gpsEnableDialog.show();
    }

    public void askLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this, permissions, LOCATOIN_PERMISSION_REQUEST_KEY);
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            Log.v("==>>", ex.getMessage());
        }
        return "02:00:00:00:00:00";
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
