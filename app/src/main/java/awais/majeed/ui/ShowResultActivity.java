package awais.majeed.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import awais.majeed.R;
import awais.majeed.services.SaveInformationTracksService;
import awais.majeed.services.ValidateRegistrationNoService;
import awais.majeed.services.core.Result;
import awais.majeed.utils.UtilHelpers;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.rx.ObservableFactory;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class ShowResultActivity extends AppCompatActivity implements View.OnClickListener, Result<String> {

    private final int GPS_ENABLE_REQUEST_KEY = 176;
    private final int LOCATOIN_PERMISSION_REQUEST_KEY = 239;
    private final int VALIDATE_REGISTRATION_NO_KEY = 563;
    private final int SAVE_INFORMATION_TRACKS_KEY = 345;
    public static final String SCAN_RESULT_KEY = "scan_result_key";
    protected Button scanAgainButton;
    protected ImageView cardOwnerImageIv;
    protected TextView cardOwnerNameTv;
    protected TextView cardOwnerOccupationTv;
    protected TextView cardOwnerContactTv;
    protected TextView cardOwnerAddressTv;
    protected TextView cardOwnerRegistrationTv;
    protected TextView cardOwnerEmailTv;
    private String regNo, latLong;
    private int shouldCall = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_show_result);
        UtilHelpers.hideUI(this);
        getLocation();
        if (getIntent().getExtras() != null) {
            String scanResult = getIntent().getExtras().getString(SCAN_RESULT_KEY);
            ValidateRegistrationNoService.newInstance(this, false, VALIDATE_REGISTRATION_NO_KEY,
                    this).callService(scanResult);
        }
        initView();
    }

    private void retrieveAndSetData(JSONObject responseData) {
        try {
            String imageUrl = responseData.getString("imageUrl");
            Picasso.with(this).load(imageUrl).error(R.drawable.passport).into(cardOwnerImageIv);
            String name = responseData.getString("name");
            cardOwnerNameTv.setText(name);
            String type = responseData.getString("type");
            cardOwnerOccupationTv.setText(type);
            String contactNo = responseData.getString("contactNo");
            cardOwnerContactTv.setText("Contact No: \n" + contactNo);
            String address = responseData.getString("address");
            cardOwnerAddressTv.setText("Address : \n" + address);
            regNo = responseData.getString("registrationNo");
            if (type.equalsIgnoreCase("Student")) {
                cardOwnerRegistrationTv.setText("Registration No : \n" + regNo);
            } else if (type.equalsIgnoreCase("Teacher")) {
                cardOwnerRegistrationTv.setText("Employee No : \n" + regNo);
            }
            String email = responseData.getString("email");
            cardOwnerEmailTv.setText("Email : \n" + email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveInformation() {
        if (++shouldCall == 2)
            SaveInformationTracksService.newInstance(this, false, SAVE_INFORMATION_TRACKS_KEY, this)
                    .callService(regNo, UtilHelpers.getDateTimeNow(), latLong, UtilHelpers.getMacAddr());
    }

    @Override
    public void onSuccess(String data, int requestId) {
        if (requestId == VALIDATE_REGISTRATION_NO_KEY)
            try {
                JSONObject responseObject = new JSONObject(data);
                JSONObject responseData = responseObject.getJSONObject("0");
                retrieveAndSetData(responseData);
                saveInformation();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        else if (requestId == SAVE_INFORMATION_TRACKS_KEY) {
            //do nothing
        }
    }

    @Override
    public void onFailure(String message, int requestId) {
        if (requestId == VALIDATE_REGISTRATION_NO_KEY) {
            UtilHelpers.showAlertDialog(this, "Invalid Registration No", "no records found against this registration no...");
        }
    }

    @Override
    public void onError(Throwable throwable, int requestId) {
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.scan_again_button) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initView() {
        cardOwnerImageIv = (ImageView) findViewById(R.id.card_owner_image_iv);
        cardOwnerNameTv = (TextView) findViewById(R.id.card_owner_name_tv);
        cardOwnerOccupationTv = (TextView) findViewById(R.id.card_owner_occupation_tv);
        cardOwnerContactTv = (TextView) findViewById(R.id.card_owner_contact_tv);
        cardOwnerAddressTv = (TextView) findViewById(R.id.card_owner_address_tv);
        cardOwnerRegistrationTv = (TextView) findViewById(R.id.card_owner_registration_tv);
        cardOwnerEmailTv = (TextView) findViewById(R.id.card_owner_email_tv);
        scanAgainButton = (Button) findViewById(R.id.scan_again_button);
        scanAgainButton.setOnClickListener(ShowResultActivity.this);
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


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ShowResultActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askLocationPermission();
            return;
        }
        Observable<Location> locationObservable = ObservableFactory.from(SmartLocation.with(this).location());
        locationObservable.subscribe(new Consumer<Location>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Location location) throws Exception {
                try {
                    latLong = location.getLatitude() + ":" + location.getLongitude();
                    saveInformation();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void askLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this, permissions, LOCATOIN_PERMISSION_REQUEST_KEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_ENABLE_REQUEST_KEY) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATOIN_PERMISSION_REQUEST_KEY) {
            try {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    requestLocationUpdates();
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        getLocation();
                    } else {
                        askToEnableGPS();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
