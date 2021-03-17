package org.kashmirworldfoundation.snowleopardapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Prey implements Parcelable {
    private String Note;
    private String latitudeS;
    private String longitudeS;
    private String pic;
    private String org;
    private String prey;
    private Timestamp Posted;
    private String member;
    private String author;

    protected Prey(Parcel in) {
        Note = in.readString();
        latitudeS = in.readString();
        longitudeS = in.readString();
        pic = in.readString();
        org = in.readString();
        prey=in.readString();
        Posted=in.readParcelable(getClass().getClassLoader());
        member=in.readString();
        author=in.readString();
    }
    public Prey(){

    }
    public static final Creator<Prey> CREATOR = new Creator<Prey>() {
        @Override
        public Prey createFromParcel(Parcel in) {
            return new Prey(in);
        }

        @Override
        public Prey[] newArray(int size) {
            return new Prey[size];
        }
    };

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getPrey() {
        return prey;
    }

    public void setPrey(String prey) {
        this.prey = prey;
    }

    public Timestamp getPosted() {
        return Posted;
    }

    public void setPosted(Timestamp posted) {
        Posted = posted;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String Author) {
        author = Author;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Note);
        dest.writeString(latitudeS);
        dest.writeString(longitudeS);
        dest.writeString(pic);
        dest.writeString(org);
        dest.writeString(prey);
        dest.writeParcelable(Posted,0);
        dest.writeString(member);
        dest.writeString(author);
    }
}
