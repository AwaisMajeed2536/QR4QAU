package awais.majeed.services;

import android.content.Context;

import awais.majeed.services.core.BaseService;
import awais.majeed.services.core.Result;
import awais.majeed.services.core.RetrofitClient;
import awais.majeed.services.core.UserClient;

/**
 * Created by Devprovider on 02/07/2017.
 */

public class ValidateMacAddressService extends BaseService {
    private ValidateMacAddressService(Context context, boolean runInBackground, Result<String> result) {
        super(context, runInBackground, result);
    }

    private ValidateMacAddressService(Context context, boolean runInBackground, int requestId, Result<String> result) {
        super(context, runInBackground, requestId, result);
    }

    public static ValidateMacAddressService newInstance(Context context, boolean runInBackground, Result<String> result){
        return new ValidateMacAddressService(context, runInBackground, result);
    }

    public static ValidateMacAddressService newInstance(Context context, boolean runInBackground, int requestId,
                                                        Result<String> result){
        return new ValidateMacAddressService(context, runInBackground, requestId, result);
    }

    public void callService(String macAddress){
        RetrofitClient.getRetrofit().create(UserClient.class).validateMacAddress(macAddress).enqueue(this);
    }

}
