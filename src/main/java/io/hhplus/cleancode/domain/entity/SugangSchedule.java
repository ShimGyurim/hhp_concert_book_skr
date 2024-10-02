package io.hhplus.cleancode.domain.entity;

import jakarta.persistence.*;

public class SugangSchedule {

    private Long scheduleId;

    private Sugang sugang;

    private String classDate;

    private Long availNum;

    public Long getScheduleId() {
        return scheduleId;
    }

    public Sugang getSugang() {
        return sugang;
    }

    public void setSugang(Sugang sugang) {
        this.sugang = sugang;
    }

    public String getClassDate() {
        return classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public Long getAvailNum() {
        return availNum;
    }

    public void setAvailNum(Long availNum) {
        this.availNum = availNum;
    }

    @Override
    public String toString() {
        return "SugangSchedule{" +
                "scheduleId=" + scheduleId +
                ", sugang=" + sugang +
//                ", student=" + student +
                ", classDate='" + classDate + '\'' +
                ", availNum=" + availNum +
                '}';
    }
}
