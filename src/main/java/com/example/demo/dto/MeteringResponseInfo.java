package com.example.demo.dto;

import java.util.List;

public class MeteringResponseInfo {
    Integer pcode;
    String pname;
    Integer agent;
    Integer host;
    Double mcore;
    Integer urls;
    String dateTime;
    List<OID> oids;
    
    public MeteringResponseInfo() {
    }
    

    public MeteringResponseInfo(Integer pcode, String pname, Integer agent, Integer host, Double mcore, Integer urls,
            String dateTime, List<OID> oids) {
        this.pcode = pcode;
        this.pname = pname;
        this.agent = agent;
        this.host = host;
        this.mcore = mcore;
        this.urls = urls;
        this.dateTime = dateTime;
        this.oids = oids;
    }


    public Integer getPcode() {
        return pcode;
    }

    public void setPcode(Integer pcode) {
        this.pcode = pcode;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Integer getAgent() {
        return agent;
    }

    public void setAgent(Integer agent) {
        this.agent = agent;
    }

    public Integer getHost() {
        return host;
    }

    public void setHost(Integer host) {
        this.host = host;
    }

    public Double getMcore() {
        return mcore;
    }

    public void setMcore(Double mcore) {
        this.mcore = mcore;
    }

    public Integer getUrls() {
        return urls;
    }

    public void setUrls(Integer urls) {
        this.urls = urls;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<OID> getOids() {
        return oids;
    }

    public void setOids(List<OID> oids) {
        this.oids = oids;
    }
    


    
    
}
