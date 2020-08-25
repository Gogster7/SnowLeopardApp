package org.kashmirworldfoundation.snowleopardapp;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class CameraStation  {
    private String pic;
    private String stationId;
    private String watershedid;
    private String latitudeS;
    private String longitudeS;
    private String altitude;
    private String cameraId;

    private String terrain;
    private String habitat;
    private String lureType;
    private String substrate;
    private String potential;
    private String author;
    private String org;
    private Timestamp Posted;
    public CameraStation(){

    }


    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getWatershedid() {
        return watershedid;
    }

    public void setWatershedid(String watershedid) {
        this.watershedid = watershedid;
    }

    public String getLatitudeS() {
        return latitudeS;
    }

    public void setLatitudeS(String latitudeS) {
        this.latitudeS = latitudeS;
    }

    public String getLongitudeS() {
        return longitudeS;
    }

    public void setLongitudeS(String longitudeS) {
        this.longitudeS = longitudeS;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getLureType() {
        return lureType;
    }

    public void setLureType(String lureType) {
        this.lureType = lureType;
    }

    public String getSubstrate() {
        return substrate;
    }

    public void setSubstrate(String substrate) {
        this.substrate = substrate;
    }

    public String getPotential() {
        return potential;
    }

    public void setPotential(String potential) {
        this.potential = potential;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Timestamp getPosted() {
        return Posted;
    }

    public void setPosted(Timestamp posted) {
        Posted = posted;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }
}
