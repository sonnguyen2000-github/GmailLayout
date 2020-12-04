package com.example.gmaillayout;

import java.util.Date;

public class Gmail{
    private int avatar;
    private String fullName, emailAddress, mailContent, address;
    private Date date;
    private boolean starred;

    public int getAvatar(){
        return avatar;
    }

    public void setAvatar(int avatar){
        this.avatar = avatar;
    }

    public String getFullName(){
        return fullName;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }

    public String getMailContent(){
        return mailContent;
    }

    public void setMailContent(String mailContent){
        this.mailContent = mailContent;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public boolean isStarred(){
        return starred;
    }

    public void setStarred(boolean starred){
        this.starred = starred;
    }
}
