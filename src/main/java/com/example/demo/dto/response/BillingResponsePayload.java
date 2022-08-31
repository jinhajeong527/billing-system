package com.example.demo.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BillingResponsePayload {

    List<MeteringResponsePayload> meteringList;
    Integer calculatedTotalHourPerCore;
    Double usedCorePerHour;
    BigDecimal totalFee;
    BigDecimal totalCore;

    public List<MeteringResponsePayload> getMeteringList() {
        return meteringList;
    }
    public void setMeteringList(List<MeteringResponsePayload> meteringList) {
        this.meteringList = meteringList;
    }
    public Integer getCalculatedTotalHourPerCore() {
        return calculatedTotalHourPerCore;
    }
    public void setCalculatedTotalHourPerCore(Integer calculatedTotalHourPerCore) {
        this.calculatedTotalHourPerCore = calculatedTotalHourPerCore;
    }
    public Double getUsedCorePerHour() {
        return usedCorePerHour;
    }
    public void setUsedCorePerHour(Double usedCorePerHour) {
        this.usedCorePerHour = usedCorePerHour;
    }
    public BigDecimal getTotalFee() {
        return totalFee;
    }
    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }
    public BigDecimal getTotalCore() {
        return totalCore;
    }
    public void setTotalCore(BigDecimal totalCore) {
        this.totalCore = totalCore;
    }
    public void add(MeteringResponsePayload meteringResponseInfo) { 
        if(meteringList == null) {
            meteringList = new ArrayList<>();
        }
        meteringList.add(meteringResponseInfo);
    }
    
    
}
