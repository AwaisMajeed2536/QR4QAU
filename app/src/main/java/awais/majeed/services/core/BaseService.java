package awais.majeed.services.core;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeoutException;

import awais.majeed.utils.UtilHelpers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.data;


public class BaseService implements Callback<String> {

    private Result<String> result;
    private boolean runInBackground;
    private Context context;
    private int requestId;

    public BaseService(Context context, boolean runInBackground, Result<String> result) {
        this.result = result;
        this.requestId = 0;
        this.context = context;
        this.runInBackground = runInBackground;
    }

    public BaseService(Context context, boolean runInBackground, int requestId, Result<String> result) {
        this.result = result;
        this.requestId = requestId;
        this.context = context;
        this.runInBackground = runInBackground;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        hideDailog();
        if (response.isSuccessful()) {
            String message = response.body();
            try {
                JSONObject responseObject = new JSONObject(message);
                if (responseObject.getString("foundResult").equalsIgnoreCase("true")) {
                    result.onSuccess(message, requestId);
                } else {
                    result.onFailure("Failed!", requestId);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        hideDailog();
        if (t instanceof NoInternetException) {
            if (!runInBackground) {
                result.onError(t, requestId);
            }
        } else if (t instanceof TimeoutException) {
            if (!runInBackground) {
                result.onError(t, requestId);
            }
        } else {
            if (!runInBackground) {
                result.onError(t, requestId);
            }
        }
        result.onError(t, requestId);
    }

    protected void showDialog() {
        if (!runInBackground) {
            UtilHelpers.showWaitDialog(context, "Processing", "please wait...");
        }
    }

    private void hideDailog() {
        if (!runInBackground) {
            UtilHelpers.dismissWaitDialog();
        }
    } // hideDailog

} // BaseService
