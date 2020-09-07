package com.orane.icliniq.Model;

public class Item {
    private String id;
    private String title;
    private String askedname;
    private String pubdate;
    private String link;
    private String price;
    private String speciality;
    private String fupcode;
    private String geo;
    private String label;
    private String hid;
    private String dt,fav;
    private String ty,feed;
    private boolean selected;
    private String amt;
    private String des;
    private String currlable;
    private String qid;
    private String question;
    private String str_status;
    private String status, docurl;
    private String datetime;

    private String docid;
    private String docimage;
    private String docname;
    private String docedu;
    private String docspec;
    private String cfee;
    private String qfee,code,pri, spec,name,cdate, ctime, ctz, cnotes, cdname, cdurl;
    private String bquery, bcdate, bstrtrange, btz, bctype, blang, bstatus, bapptid, bid, bstrstatus;
    private String hlname, hlphoto_url, hledu, hlsp, hlfee3, hlis_subscribed, hlid, hotline_status;
    private String stitle, sdesc, surl, sphoto_url, sspeciality, slabel, sitemid, sitemtype;
    private String wdatetime, wdesc, wamt, wtype, pproof, location, country, zip, state, city, street;

    private String patient;
    private String notes;
    private String apptdt;
    private String appttype;
    private String testid;
    private String testname;
    private String disease;
    private String testcount;
    private String normalvalue;
    private String arturl;
    private String artdocname;
    private String arttitle;
    private String artabs;
    private String artimgurl,isadded,nhash,dhash,tprice;


    //----------------Labtest------------------------------
    public void setArturl(String arturl) {
        this.arturl = arturl;
    }

    public String getArturl() {
        return arturl;
    }

    public void setArtDocname(String artdocname) {
        this.artdocname = artdocname;
    }

    public String getArtDocname() {
        return artdocname;
    }

    public void setArtTitle(String arttitle) {
        this.arttitle = arttitle;
    }

    public String getArtTitle() {
        return arttitle;
    }


    public void setArtAbs(String artabs) {
        this.artabs = artabs;
    }

    public String getArtAbs() {
        return artabs;
    }

    public void setArtimgurl(String artimgurl) {
        this.artimgurl = artimgurl;
    }

    public String getArtimgurl() {
        return artimgurl;
    }

    //----------------Labtest------------------------------


    //----------------Labtest------------------------------
    public void setTestid(String testid) {
        this.testid = testid;
    }
    public String getTestid() {
        return testid;
    }

    public void setTprice(String tprice) {
        this.tprice = tprice;
    }
    public String getTprice() {
        return tprice;
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


    public void setDhash(String dhash) {
        this.dhash = dhash;
    }

    public String getDhash() {
        return dhash;
    }


    public void setNhash(String nhash) {
        this.nhash = nhash;
    }

    public String getNhash() {
        return nhash;
    }

    public void setIsadded(String isadded) {
        this.isadded = isadded;
    }

    public String getIsadded() {
        return isadded;
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


    //--------- Search Results-------------------------
    public void setStitle(String stitle) {
        this.stitle = stitle;
    }

    public String getStitle() {
        return stitle;
    }

    //----------------------------------------------------
    public void setSdesc(String sdesc) {
        this.sdesc = sdesc;
    }

    public String getSdesc() {
        return sdesc;
    }

    //----------------------------------------------------
    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getSurl() {
        return surl;
    }

    //----------------------------------------------------
    public void setSphoto_url(String sphoto_url) {
        this.sphoto_url = sphoto_url;
    }

    public String getSphoto_url() {
        return sphoto_url;
    }

    //----------------------------------------------------
    public void setSspeciality(String sspeciality) {
        this.sspeciality = sspeciality;
    }

    public String getSspeciality() {
        return sspeciality;
    }

    //----------------------------------------------------
    public void setSlabel(String slabel) {
        this.slabel = slabel;
    }

    public String getSlabel() {
        return slabel;
    }

    //----------------------------------------------------
    public void setSitemid(String sitemid) {
        this.sitemid = sitemid;
    }

    public String getSitemid() {
        return sitemid;
    }

    //----------------------------------------------------
    public void setSitemtype(String sitemtype) {
        this.sitemtype = sitemtype;
    }

    public String getSitemtype() {
        return sitemtype;
    }
    //----------------------------------------------------


    //--------- Hotlione Doctors -------------------------
    public void setHlid(String hlid) {
        this.hlid = hlid;
    }

    public String getHlid() {
        return hlid;
    }
    //----------------------------------

    //--------- Hotlione Doctors -------------------------
    public void setHotline_status(String hotline_status) {
        this.hotline_status = hotline_status;
    }

    public String getHotline_status() {
        return hotline_status;
    }
    //----------------------------------


    public void setHlname(String hlname) {
        this.hlname = hlname;
    }

    public String getHlname() {
        return hlname;
    }

    //----------------------------------
    public void setHlphoto_url(String hlphoto_url) {
        this.hlphoto_url = hlphoto_url;
    }

    public String getHlphoto_url() {
        return hlphoto_url;
    }

    //----------------------------------
    public void setHledu(String hledu) {
        this.hledu = hledu;
    }

    public String getHledu() {
        return hledu;
    }

    //----------------------------------
    public void setHlsp(String hlsp) {
        this.hlsp = hlsp;
    }

    public String getHlsp() {
        return hlsp;
    }

    //----------------------------------
    public void setHlfee3(String hlfee3) {
        this.hlfee3 = hlfee3;
    }

    public String getHlfee3() {
        return hlfee3;
    }

    //----------------------------------
    public void setHlis_subscribed(String hlis_subscribed) {
        this.hlis_subscribed = hlis_subscribed;
    }

    public String getHlis_subscribed() {
        return hlis_subscribed;
    }

    //----------------------------------
    public void setCurrlable(String currlable) {
        this.currlable = currlable;
    }

    public String getCurrlable() {
        return currlable;
    }  public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    //----------------------------------


    //--------- Booking -------------------------
    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBid() {
        return bid;
    }

    //----------------------------------
    public void setBquery(String bquery) {
        this.bquery = bquery;
    }

    public String getBquery() {
        return bquery;
    }

    //----------------------------------
    public void setBcdate(String bcdate) {
        this.bcdate = bcdate;
    }

    public String getBcdate() {
        return bcdate;
    }

    //----------------------------------
    public void setBstrtrange(String bstrtrange) {
        this.bstrtrange = bstrtrange;
    }

    public String getBstrtrange() {
        return bstrtrange;
    }

    //----------------------------------
    //----------------------------------
    public void setBtz(String btz) {
        this.btz = btz;
    }

    public String getBtz() {
        return btz;
    }

    //----------------------------------
    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }



    //----------------------------------
    public void setBctype(String bctype) {
        this.bctype = bctype;
    }

    public String getBctype() {
        return bctype;
    }

    //----------------------------------
    public void setBlang(String blang) {
        this.blang = blang;
    }

    public String getBlang() {
        return blang;
    }

    //----------------------------------
    public void setBstatus(String bstatus) {
        this.bstatus = bstatus;
    }

    public String getBstatus() {
        return bstatus;
    }

    //----------------------------------
    public void setBstrstatus(String bstrstatus) {
        this.bstrstatus = bstrstatus;
    }

    public String getBstrstatus() {
        return bstrstatus;
    }

    //----------------------------------
    public void setBapptid(String bapptid) {
        this.bapptid = bapptid;
    }

    public String getBapptid() {
        return bapptid;
    }
    //----------------------------------


    //--------- Consultation -------------------------
    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtz(String Ctz) {
        this.ctz = ctz;
    }

    public String getCtz() {
        return ctz;
    }

    public void setCnotes(String cnotes) {
        this.cnotes = cnotes;
    }

    public String getCnotes() {
        return cnotes;
    }

    public void setCdname(String cdname) {
        this.cdname = cdname;
    }

    public String getCdname() {
        return cdname;
    }

    public void setCdurl(String cdurl) {
        this.cdurl = cdurl;
    }

    public String getCdurl() {
        return cdurl;
    }

    public void setCfee(String cfee) {
        this.cfee = cfee;
    }

    public void setQfee(String qfee) {
        this.qfee = qfee;
    }

    public String getFav() {
        return fav;
    }
    public void setFav(String fav) {
        this.fav = fav;
    }






    public String getCfee() {
        return cfee;
    }

    public String getQfee() {
        return qfee;
    }


    //------------------------------------------------------------------------

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public void setDocimage(String docimage) {
        this.docimage = docimage;
    }

    public void setDocurl(String docurl) {
        this.docurl = docurl;
    }

    public String getDocurl() {
        return docurl;
    }


    public void setDocname(String docname) {
        this.docname = docname;
    }

    public void setDocedu(String docedu) {
        this.docedu = docedu;
    }

    public void setDocspec(String docspec) {
        this.docspec = docspec;
    }
//------------------------------------------------------------------------


    public String getDocimage() {
        return docimage;
    }

    public String getDocid() {
        return docid;
    }

    public String getDocname() {
        return docname;
    }

    //---------- Comments ----------------------
    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    //---------- Comments ----------------------


    public String getDocedu() {
        return docedu;
    }

    public String getDocspec() {
        return docspec;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStr_status() {
        return str_status;
    }

    public void setStr_status(String str_status) {
        this.str_status = str_status;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getTy() {
        return ty;
    }

    public void setTy(String ty) {
        this.ty = ty;
    }

    public String getPri() {
        return pri;
    }

    public void setPri(String pri) {
        this.pri = pri;
    }


    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSpec() {
        return spec;
    }
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }


    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getFupcode() {
        return fupcode;
    }

    public void setFupcode(String fupcode) {
        this.fupcode = fupcode;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAskedName() {
        return askedname;
    }

    public void seAskedName(String askedname) {
        this.askedname = askedname;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    //--------- Wallet Transaction List-------------------------
    public void setPproof(String pproof) {
        this.pproof = pproof;
    }

    public void setWtype(String wtype) {
        this.wtype = wtype;
    }

    public String getWtype() {
        return wtype;
    }

    public void setWdatetime(String wdatetime) {
        this.wdatetime = wdatetime;
    }

    public String getWdatetime() {
        return wdatetime;
    }

    public void setWdesc(String wdesc) {
        this.wdesc = wdesc;
    }

    public String getWdesc() {
        return wdesc;
    }

    public void setWamt(String wamt) {
        this.wamt = wamt;
    }

    public String getWamt() {
        return wamt;
    }
    //--------- Wallet Transaction List-------------------------


}
