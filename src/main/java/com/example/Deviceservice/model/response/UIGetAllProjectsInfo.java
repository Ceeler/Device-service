package com.example.Deviceservice.model.response;

import java.util.List;

public class UIGetAllProjectsInfo {

    public UIGetAllProjectsInfo(int id, String projectName, List<String> devices){
        this.id = id;
        this.projectName = projectName;
        this.devices = devices;
    }

    int id;
    String projectName;
    Stats stats;
    List<String> devices;

    public void setStats(int deviceCount, int deviceWithErrors, int stableDevices) {
        this.stats = new Stats(deviceCount, deviceWithErrors, stableDevices);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public List<String> getDevices() {
        return devices;
    }

    public void setDevices(List<String> devices) {
        this.devices = devices;
    }
}

class Stats{

    public Stats(int deviceCount, int deviceWithErrors, int stableDevices){
        this.deviceCount = deviceCount;
        this.deviceWithErrors = deviceWithErrors;
        this.stableDevices = stableDevices;
    }
    int deviceCount;
    int deviceWithErrors;
    int stableDevices;

    public int getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }

    public int getDeviceWithErrors() {
        return deviceWithErrors;
    }

    public void setDeviceWithErrors(int deviceWithErrors) {
        this.deviceWithErrors = deviceWithErrors;
    }

    public int getStableDevices() {
        return stableDevices;
    }

    public void setStableDevices(int stableDevices) {
        this.stableDevices = stableDevices;
    }
}