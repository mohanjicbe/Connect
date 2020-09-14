package com.orane.icliniq.utils.walletModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchDoctorModel {

@SerializedName("id")
@Expose
private String id;
@SerializedName("name")
@Expose
private String name;
@SerializedName("edu")
@Expose
private String edu;
@SerializedName("speciality")
@Expose
private String speciality;
@SerializedName("doctor_url")
@Expose
private String doctorUrl;
@SerializedName("photo_url")
@Expose
private String photoUrl;
@SerializedName("is_fav")
@Expose
private Integer isFav;
@SerializedName("qfee")
@Expose
private String qfee;
@SerializedName("cfee")
@Expose
private String cfee;
@SerializedName("page")
@Expose
private String page;
@SerializedName("avg_rating")
@Expose
private String avgRating;
@SerializedName("rating_lbl")
@Expose
private String ratingLbl;
@SerializedName("is_star")
@Expose
private String isStar;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getEdu() {
return edu;
}

public void setEdu(String edu) {
this.edu = edu;
}

public String getSpeciality() {
return speciality;
}

public void setSpeciality(String speciality) {
this.speciality = speciality;
}

public String getDoctorUrl() {
return doctorUrl;
}

public void setDoctorUrl(String doctorUrl) {
this.doctorUrl = doctorUrl;
}

public String getPhotoUrl() {
return photoUrl;
}

public void setPhotoUrl(String photoUrl) {
this.photoUrl = photoUrl;
}

public Integer getIsFav() {
return isFav;
}

public void setIsFav(Integer isFav) {
this.isFav = isFav;
}

public String getQfee() {
return qfee;
}

public void setQfee(String qfee) {
this.qfee = qfee;
}

public String getCfee() {
return cfee;
}

public void setCfee(String cfee) {
this.cfee = cfee;
}

public String getPage() {
return page;
}

public void setPage(String page) {
this.page = page;
}

public String getAvgRating() {
return avgRating;
}

public void setAvgRating(String avgRating) {
this.avgRating = avgRating;
}

public String getRatingLbl() {
return ratingLbl;
}

public void setRatingLbl(String ratingLbl) {
this.ratingLbl = ratingLbl;
}

public String getIsStar() {
return isStar;
}

public void setIsStar(String isStar) {
this.isStar = isStar;
}

}
