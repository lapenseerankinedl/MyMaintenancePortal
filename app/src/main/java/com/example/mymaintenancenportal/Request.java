package com.example.mymaintenancenportal;

public class Request {
    private String requestID;
    private String tenantName;
    private String requestText;
    private String urgency;
    private String landlordEmail;
    private String image;
    private String status;
    private String cancelReason;

    public String getRequestID() {
        return requestID;
    }

    public String getTenantName() {
        return tenantName;
    }

    public String getRequestText() {
        return requestText;
    }

    public String getUrgency() {
        return urgency;
    }

    public String getLandlordEmail() { return landlordEmail; }

    public String getImage() { return image; }

    public String getStatus() { return status; }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public void setLandlordEmail(String landlordEmail) {
        this.landlordEmail = landlordEmail;
    }

    public void setImage(String image) { this.image = image; }

    public void setStatus(String status) { this.status = status; }

    public Request() {

    }

    public Request(String requestID, String requestText, String tenantName, String urgency, String landlordEmail, String image) {
        this.requestID = requestID;
        this.requestText = requestText;
        this.tenantName = tenantName;
        this.urgency = urgency;
        this.landlordEmail = landlordEmail;
        this.image = image;
        this.status = "Landlord has not seen the request";
        this.cancelReason = "";
    }
}
