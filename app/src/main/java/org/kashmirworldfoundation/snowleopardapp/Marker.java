package org.kashmirworldfoundation.snowleopardapp;

public class Marker {
    private String author;
    private String org;
    private String latitude;
    private String longitude;
    private String elevation;
    private String uid;
    private String markerName;

    Marker(){

    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public void setAuthor(String author){
        this.author=author;
    }
    public void setOrg(String org){
        this.org=org;
    }
    public void setLongitude(String longitude){
        this.longitude=longitude;

    }
    public void setLatitude(String latitude){
        this.latitude=latitude;
    }

    public String getAuthor() {
        return author;
    }

    public String getOrg() {
        return org;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
