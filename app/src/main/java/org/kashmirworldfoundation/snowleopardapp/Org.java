package org.kashmirworldfoundation.snowleopardapp;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class Org {
    private  String OrgEmail;
    private String OrgWebsite;
    private String OrgName;
    private String OrgPhone;
    private String OrgCountry;
    private String OrgRegion;


    public Org(String orgEmail, String orgWebsite, String orgName, String orgPhone, String orgCountry, String orgRegion) {
        OrgEmail = orgEmail;
        OrgWebsite = orgWebsite;
        OrgName = orgName;
        OrgPhone = orgPhone;
        OrgCountry = orgCountry;
        OrgRegion = orgRegion;
    }

    public Org(){

    }

    public String getOrgEmail() {
        return OrgEmail;
    }

    public void setOrgEmail(String orgEmail) {
        OrgEmail = orgEmail;
    }

    public String getOrgWebsite() {
        return OrgWebsite;
    }

    public void setOrgWebsite(String orgWebsite) {
        OrgWebsite = orgWebsite;
    }

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String orgName) {
        OrgName = orgName;
    }

    public String getOrgPhone() {
        return OrgPhone;
    }

    public void setOrgPhone(String orgPhone) {
        OrgPhone = orgPhone;
    }

    public String getOrgCountry() {
        return OrgCountry;
    }

    public void setOrgCountry(String orgCountry) {
        OrgCountry = orgCountry;
    }

    public String getOrgRegion() {
        return OrgRegion;
    }

    public void setOrgRegion(String orgRegion) {
        OrgRegion = orgRegion;
    }
}
