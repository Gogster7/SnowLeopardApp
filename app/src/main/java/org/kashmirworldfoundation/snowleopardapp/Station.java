package org.kashmirworldfoundation.snowleopardapp;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Station implements Serializable {


    private String altitude;
    private String author;
    private String cameraId;
    private String country;
    private String habitat;
    private String IDate;
    private String lattitudeS;
    private String longitudeS;
    private String lureType;
    private String org;
    private String pic;
    private String potential;
    private String region;
    private String posted;
    private String stationId;
    private String substrate;
    private String terrain;
    private String watershedid;

    public Station() {
        this.altitude ="null";
        this.author = "null";
        this.cameraId = "null";
        this.country = "null";
        this.habitat = "null";
        this.IDate = "null";
        this.lattitudeS = "null";
        this.longitudeS = "null";
        this.lureType = "null";
        this.org = "null";
        this.pic = "null";
        this.potential = "null";
        this.region = "null";
        this.posted = null;
        this.stationId = "null";
        this.substrate = "null";
        this.terrain = "null";
        this.watershedid = "null";
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getIDate() {
        return IDate;
    }

    public void setIDate(String IDate) {
        this.IDate = IDate;
    }

    public String getLattitudeS() {
        return lattitudeS;
    }

    public void setLattitudeS(String lattitudeS) {
        this.lattitudeS = lattitudeS;
    }

    public String getLongitudeS() {
        return longitudeS;
    }

    public void setLongitudeS(String longitudeS) {
        this.longitudeS = longitudeS;
    }

    public String getLureType() {
        return lureType;
    }

    public void setLureType(String lureType) {
        this.lureType = lureType;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPotential() {
        return potential;
    }

    public void setPotential(String potential) {
        this.potential = potential;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        region = region;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getSubstrate() {
        return substrate;
    }

    public void setSubstrate(String substrate) {
        this.substrate = substrate;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public String getWatershedid() {
        return watershedid;
    }

    public void setWatershedid(String watershedid) {
        this.watershedid = watershedid;
    }









}
