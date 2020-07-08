package org.kashmirworldfoundation.snowleopardapp;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

public class Member {
    private String Email;
    private String Fullname;
    private String Job;
    private String Phone;
    private Boolean Admin;
    private DocumentReference Org;
    private String Profile;
    public Member(){



    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }



    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }


    public void setAdmin(Boolean admin) {
        Admin = admin;
    }

    public Boolean getAdmin() {
        return Admin;
    }

    public DocumentReference getOrg() {
        return Org;
    }

    public void setOrg(DocumentReference org) {
        Org = org;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }
}

