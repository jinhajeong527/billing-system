package com.example.demo.dto;

import java.util.List;

public class BillingPayload {

    List<MeteringInfo> meteringList;
    Integer calculatedTotalHourPerCore;
    Double totalFee;
    Integer totlaCore = 0;
    
    public List<MeteringInfo> getMeteringList() {
        return meteringList;
    }

    public void setMeteringList(List<MeteringInfo> meteringList) {
        this.meteringList = meteringList;
    }

    public Integer getCalculatedTotalHourPerCore() {
        return calculatedTotalHourPerCore;
    }

    public void setCalculatedTotalHourPerCore(Integer calculatedTotalHourPerCore) {
        this.calculatedTotalHourPerCore = calculatedTotalHourPerCore;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getTotlaCore() {
        return totlaCore;
    }

    public void setTotlaCore(Integer totlaCore) {
        this.totlaCore = totlaCore;
    }
        
}
    

