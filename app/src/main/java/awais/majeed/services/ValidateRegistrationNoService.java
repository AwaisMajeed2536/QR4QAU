package awais.majeed.services;

import android.content.Context;

import awais.majeed.services.core.BaseService;
import awais.majeed.services.core.Result;
import awais.majeed.services.core.RetrofitClient;
import awais.majeed.services.core.UserClient;

/**
 * Created by Devprovider on 03/07/2017.
 */

public class ValidateRegistrationNoService extends BaseService {
    private ValidateRegistrationNoService(Context context, boolean runInBackground, Result<String> result) {
        super(context, runInBackground, result);
    }

    private ValidateRegistrationNoService(Context context, boolean runInBackground, int requestId, Result<String> result) {
        super(context, runInBackground, requestId, result);
    }

    public static ValidateRegistrationNoService newInstance(Context context, boolean runInBackground, Result<String> result){
        return new ValidateRegistrationNoService(context, runInBackground, result);
    }

    public static ValidateRegistrationNoService newInstance(Context context, boolean runInBackground, int requestId, Result<String> result){
        return new ValidateRegistrationNoService(context, runInBackground, requestId, result);
    }

    public void callService(String regNo){
        RetrofitClient.getRetrofit().create(UserClient.class).validateRegNo(regNo).enqueue(this);
    }
}
