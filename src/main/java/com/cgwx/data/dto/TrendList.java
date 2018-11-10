package com.cgwx.data.dto;

import java.util.List;
//CPU、内存、进程历史记录返回前台的类型
public class TrendList {
    List<String> clocks;
    List<String> values;             //平均值
    public void setClocks(List<String> clocks) {
        this.clocks = clocks;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public List<String> getClocks() {
        return clocks;
    }

    public List<String> getValues() {
        return values;
    }



}
