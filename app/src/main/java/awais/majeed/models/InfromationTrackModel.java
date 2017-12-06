package awais.majeed.models;

import java.io.Serializable;

/**
 * Created by Awais Majeed on 09/11/2017.
 */

public class InfromationTrackModel implements Serializable{
    private String regNo, dateTime, latLong, deviceMac;

    public InfromationTrackModel() {
    }

    public InfromationTrackModel(String regNo, String dateTime, String latLong, String deviceMac) {
        this.regNo = regNo;
        this.dateTime = dateTime;
        this.latLong = latLong;
        this.deviceMac = deviceMac;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }
}
