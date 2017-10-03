package awais.majeed.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import awais.majeed.R;
import awais.majeed.services.ValidateRegistrationNoService;
import awais.majeed.services.core.Result;
import awais.majeed.utils.UtilHelpers;

public class ShowResultActivity extends AppCompatActivity implements View.OnClickListener, Result<String> {

    public static final String SCAN_RESULT_KEY = "scan_result_key";
    protected Button scanAgainButton;
    protected ImageView cardOwnerImageIv;
    protected TextView cardOwnerNameTv;
    protected TextView cardOwnerOccupationTv;
    protected TextView cardOwnerContactTv;
    protected TextView cardOwnerAddressTv;
    protected TextView cardOwnerRegistrationTv;
    protected TextView cardOwnerEmailTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_show_result);
        UtilHelpers.hideUI(this);
        if (getIntent().getExtras() != null) {
            String scanResult = getIntent().getExtras().getString(SCAN_RESULT_KEY);
            ValidateRegistrationNoService.newInstance(this, false, this).callService(scanResult);
        }
        initView();
    }


    @Override
    public void onSuccess(String data, int requestId) {
        try {
            JSONObject responseObject = new JSONObject(data);
            JSONObject responseData = responseObject.getJSONObject("0");
            retrieveAndSetData(responseData);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            String regNo = responseData.getString("registrationNo");
            cardOwnerRegistrationTv.setText("Registration No : \n" + regNo);
            String email = responseData.getString("email");
            cardOwnerEmailTv.setText("Email : \n" + email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String message, int requestId) {
        UtilHelpers.showAlertDialog(this, "Invalid Registration No","no records found against this registration no...");
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
}
