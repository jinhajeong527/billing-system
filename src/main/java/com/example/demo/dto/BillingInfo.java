package com.example.demo.dto;

import java.util.List;

public class BillingInfo {

    List<MeteringInfo> meteringList;
    
    public List<MeteringInfo> getMeteringList() {
        return meteringList;
    }

    public void setMeteringList(List<MeteringInfo> meteringList) {
        this.meteringList = meteringList;
    }
}
    

