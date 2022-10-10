package com.example.Deviceservice.response;

public class ProjectInfoView {

    public ProjectInfoView(int id, String serialNumber, int projectId, boolean hasErrors){
        this.id = id;
        this.serialNumber = serialNumber;
        this.projectId = projectId;
        this.hasErrors = hasErrors;
    }

    int id;
    String serialNumber;
    int projectId;
    boolean hasErrors;
    SummaryInfo summaryInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public SummaryInfo getSummaryInfo() {
        return summaryInfo;
    }

    public void setSummaryInfo(int eventCount, int warningCount, int errorCount) {
        this.summaryInfo = new SummaryInfo(eventCount, warningCount, errorCount);
    }
}

class SummaryInfo{

    public SummaryInfo(int eventCount, int warningCount, int errorCount){
        this.eventCount = eventCount;
        this.warningCount = warningCount;
        this.errorCount = errorCount;
    }
    int eventCount;
    int warningCount;
    int errorCount;

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public int getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(int warningCount) {
        this.warningCount = warningCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

}