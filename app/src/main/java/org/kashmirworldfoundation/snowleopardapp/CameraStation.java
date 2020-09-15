package org.kashmirworldfoundation.snowleopardapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class CameraStation implements Parcelable {
    private String pic;
    private String stationId;
    private String watershedid;
    private String latitudeS;
    private String longitudeS;
    private String altitude;
    private String cameraId;
    private String Camerapic1;
    private String camerapic2;
    private String notes;
    private String terrain;
    private String habitat;
    private String lureType;
    private String substrate;
    private String potential;
    private String author;
    private String org;
    private String aName;
    private String SdCard;
    private String FirebaseId;
    private Timestamp Posted;

    public CameraStation(Parcel in){
        pic=in.readString();
        stationId=in.readString();
        watershedid=in.readString();
        latitudeS=in.readString();
        longitudeS=in.readString();
        altitude=in.readString();
        cameraId=in.readString();
        Camerapic1=in.readString();
        camerapic2=in.readString();
        notes=in.readString();
        terrain=in.readString();
        habitat=in.readString();
        lureType=in.readString();
        substrate=in.readString();
        potential=in.readString();
        author=in.readString();
        org=in.readString();
        aName=in.readString();
        SdCard=in.readString();
        FirebaseId=in.readString();
        Posted=in.readParcelable(getClass().getClassLoader());


    }

    public CameraStation(){

    }

    public String getSdCard() {
        return SdCard;
    }

    public void setSdCard(String sdCard) {
        SdCard = sdCard;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public String getCamerapic1() {
        return Camerapic1;
    }

    public void setCamerapic1(String camerapic1) {
        Camerapic1 = camerapic1;
    }

    public String getCamerapic2() {
        return camerapic2;
    }

    public void setCamerapic2(String camerapic2) {
        this.camerapic2 = camerapic2;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public String getFirebaseId() {
        return FirebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        FirebaseId = firebaseId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pic);
        dest.writeString(stationId);
        dest.writeString(watershedid);
        dest.writeString(latitudeS);
        dest.writeString(longitudeS);
        dest.writeString(altitude);
        dest.writeString(cameraId);
        dest.writeString(Camerapic1);
        dest.writeString(camerapic2);
        dest.writeString(notes);
        dest.writeString(terrain);
        dest.writeString(habitat);
        dest.writeString(lureType);
        dest.writeString(substrate);
        dest.writeString(potential);
        dest.writeString(author);
        dest.writeString(org);
        dest.writeString(aName);
        dest.writeString(SdCard);
        dest.writeString(FirebaseId);
        dest.writeParcelable(Posted,0);



    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CameraStation createFromParcel(Parcel in) {
            return new CameraStation(in);
        }

        public CameraStation[] newArray(int size) {
            return new CameraStation[size];
        }
    };


}
