package com.orane.icliniq.Model;

public class ItemCons {
    private String id;
    private String patient;
    private String notes;
    private String status;
    private String apptdt;
    private String appttype;
    private String testid;
    private String testname;
    private String disease;
    private String price;
    private String testcount;
    private String normalvalue;

    public String getId() {
        return id;
    }

    //----------------Labtest------------------------------
    public void setTestid(String testid) {
        this.testid = testid;
    }
    public String getTestid() {
        return testid;
    }
    public void setTestname(String testname) {
        this.testname = testname;
    }
    public String getTestname() {
        return testname;
    }
    public void setDisease(String disease) {
        this.disease = disease;
    }
    public String getDisease() {
        return disease;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getPrice() {
        return price;
    }
    public void setTestCount(String testcount) {
        this.testcount = testcount;
    }
    public String getTestCount() {
        return testcount;
    }

    public void setNormalValue(String normalvalue) {
        this.normalvalue = normalvalue;
    }
    public String getNormalValue() {
        return normalvalue;
    }
    //----------------Labtest------------------------------

    public void setId(String id) {
        this.id = id;
    }

    //----------------------------------------------
    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getPatient() {
        return patient;
    }

    //--------------------------------------------------------
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    //--------------------------------------------------------
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
    //--------------------------------------------------------
    //--------------------------------------------------------
    public void setAppttype(String appttype) {
        this.appttype = appttype;
    }

    public String getAppttype() {
        return appttype;
    }
    //--------------------------------------------------------
    //--------------------------------------------------------
    public void setApptdt(String apptdt) {
        this.apptdt = apptdt;
    }

    public String getApptdt() {
        return apptdt;
    }
    //--------------------------------------------------------
}
