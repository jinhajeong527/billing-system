package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class BillingResponsePayload {

    List<MeteringResponseInfo> meteringList;
    Integer calculatedTotalHourPerCore;
    Double totalFee;
    Integer totalCore;

    public List<MeteringResponseInfo> getMeteringList() {
        return meteringList;
    }
    public void setMeteringList(List<MeteringResponseInfo> meteringList) {
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
    public Integer getTotalCore() {
        return totalCore;
    }
    public void setTotalCore(Integer totalCore) {
        this.totalCore = totalCore;
    }
    public void add(MeteringResponseInfo meteringResponseInfo) { 
        if(meteringList == null) {
            meteringList = new ArrayList<>();
        }
        meteringList.add(meteringResponseInfo);
    }

    
    
}
