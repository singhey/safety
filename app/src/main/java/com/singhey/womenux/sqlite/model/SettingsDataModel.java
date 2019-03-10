package com.singhey.womenux.sqlite.model;

public class SettingsDataModel {

    int policeStationCall;
    int sendLocation;
    int batteryDrain;

    private String sosMessage;
    private String locationMessage;
    private String batteryDrainMessage;

    public String getSosMessage() {
        return sosMessage;
    }

    public void setSosMessage(String sosMessage) {
        this.sosMessage = sosMessage;
    }

    public String getLocationMessage() {
        return locationMessage;
    }

    public void setLocationMessage(String locationMessage) {
        this.locationMessage = locationMessage;
    }

    public String getBatteryDrainMessage() {
        return batteryDrainMessage;
    }

    public void setBatteryDrainMessage(String batteryDrainMessage) {
        this.batteryDrainMessage = batteryDrainMessage;
    }

    public int getPoliceStationCall() {
        return policeStationCall;
    }

    public void setPoliceStationCall(int policeStationCall) {
        this.policeStationCall = policeStationCall;
    }

    public int getSendLocation() {
        return sendLocation;
    }

    public void setSendLocation(int sendLocation) {
        this.sendLocation = sendLocation;
    }

    public int getBatteryDrain() {
        return batteryDrain;
    }

    public void setBatteryDrain(int batteryDrain) {
        this.batteryDrain = batteryDrain;
    }

    public int getPowerFeature() {
        return powerFeature;
    }

    public void setPowerFeature(int powerFeature) {
        this.powerFeature = powerFeature;
    }

    int powerFeature;

    private String location;
    private String name;
    private String email;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "SettingsDataModel{" +
                "policeStationCall=" + policeStationCall +
                ", sendLocation=" + sendLocation +
                ", batteryDrain=" + batteryDrain +
                ", powerFeature=" + powerFeature +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
