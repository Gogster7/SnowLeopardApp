package org.kashmirworldfoundation.snowleopardapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Study implements Parcelable {
    String title;
    String org;
    String mission;
    String  location;
    Timestamp start;
    Timestamp end;

    public Study(Parcel in) {
        title = in.readString();
        org = in.readString();
        mission = in.readString();
        location = in.readString();
        start=in.readParcelable(getClass().getClassLoader());
        end=in.readParcelable(getClass().getClassLoader());
    }
    public  Study(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(org);
        dest.writeString(location);
        dest.writeString(mission);
        dest.writeParcelable(start,0);
        dest.writeParcelable(end,0);

    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Study createFromParcel(Parcel in) {
            return new Study(in);
        }

        public Study[] newArray(int size) {
            return new Study[size];
        }
    };
}
