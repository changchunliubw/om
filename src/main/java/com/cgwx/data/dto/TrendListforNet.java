package com.cgwx.data.dto;

import java.util.List;
//网络流量历史记录返回前台的类型
public class TrendListforNet {
    List<String> clocks;
    List<String> valuesin;           //上行流量
    List<String> valuesout;          //下行流量

    public void setClocks(List<String> clocks) {
        this.clocks = clocks;
    }

    public void setValuesin(List<String> valuesin) {
        this.valuesin = valuesin;
    }

    public void setValuesout(List<String> valuesout) {
        this.valuesout = valuesout;
    }

    public List<String> getClocks() {
        return clocks;
    }

    public List<String> getValuesin() {
        return valuesin;
    }

    public List<String> getValuesout() {
        return valuesout;
    }
}
