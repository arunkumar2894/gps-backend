package com.example.secrets.model;

public class Secrets {
    private String mapsApiKey;
    private String deviceUuid;

    public Secrets() {}

    public Secrets(String mapsApiKey, String deviceUuid) {
        this.mapsApiKey = mapsApiKey;
        this.deviceUuid = deviceUuid;
    }

    public String getMapsApiKey() {
        return mapsApiKey;
    }

    public void setMapsApiKey(String mapsApiKey) {
        this.mapsApiKey = mapsApiKey;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }
}
