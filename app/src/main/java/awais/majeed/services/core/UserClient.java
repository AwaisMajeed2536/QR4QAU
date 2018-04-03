package awais.majeed.services.core;

import awais.majeed.models.InfromationTrackModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

/**
 * Created by Bisma Tariq on 4/10/2017.
 */

public interface UserClient {
    @FormUrlEncoded
    @POST("QRServices/checkMac.php")
    Call<String> validateMacAddress(@Field("macAddress") String macAddress);

    @FormUrlEncoded
    @POST("QRServices/checkValidity.php")
    Call<String> validateRegNo(@Field("regNo") String regNo);

    @POST("QRServices/saveInformationTracks.php")
    Call<String> saveInformationTracks(@Body InfromationTrackModel data);

}
