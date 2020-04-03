package org.kashmirworldfoundation.snowleopardapp;

import java.util.ArrayList;

public class Org {
    private  String OrgEmail;
    private String OrgWebsite;
    private String Orgname;
    private String Orgphone;
    private String Country;
    private String Region;
    private ArrayList<Member> members;




    public Org(){

    }


    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
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

    public String getOrgname() {
        return Orgname;
    }

    public void setOrgname(String orgname) {
        Orgname = orgname;
    }

    public String getOrgphone() {
        return Orgphone;
    }

    public void setOrgphone(String orgphone) {
        Orgphone = orgphone;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setMembers() {
        ArrayList<Member> list = new ArrayList<Member>();
        this.members =list;
    }
    public void addMembers(Member member){
        this.members.add(member);
    }
}
