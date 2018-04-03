package awais.majeed.services;

import android.content.Context;

import com.google.gson.Gson;

import awais.majeed.models.InfromationTrackModel;
import awais.majeed.services.core.BaseService;
import awais.majeed.services.core.Result;
import awais.majeed.services.core.RetrofitClient;
import awais.majeed.services.core.UserClient;

/**
 * Created by Awais Majeed on 08/11/2017.
 */

public class SaveInformationTracksService extends BaseService {
    private SaveInformationTracksService(Context context, boolean runInBackground, Result<String> result) {
        super(context, runInBackground, result);
    }

    private SaveInformationTracksService(Context context, boolean runInBackground, int requestId, Result<String> result) {
        super(context, runInBackground, requestId, result);
    }

    public static SaveInformationTracksService newInstance(Context context, boolean runInBackground, Result<String> result){
        return new SaveInformationTracksService(context, runInBackground, result);
    }

    public static SaveInformationTracksService newInstance(Context context, boolean runInBackground, int requestId, Result<String> result){
        return new SaveInformationTracksService(context, runInBackground, requestId, result);
    }

    public void callService(String regNo, String dateTime, String latLong, String deviceMac){
        InfromationTrackModel data = new InfromationTrackModel(regNo, dateTime, latLong, deviceMac);
//        String json = new Gson().toJson(data);
        RetrofitClient.getRetrofit().create(UserClient.class).saveInformationTracks(data).enqueue(this);
    }
}
