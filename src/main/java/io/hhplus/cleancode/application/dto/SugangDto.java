package io.hhplus.cleancode.application.dto;

public class SugangDto {
    private long sugangId;
    private long studentId;
    private long availNum;
    private String classDate;
    private String className;

    public SugangDto() {
    }

    public SugangDto(long sugangId, long studentId, long availNum, String classDate, String className) {
        this.sugangId = sugangId;
        this.studentId = studentId;
        this.availNum = availNum;
        this.classDate = classDate;
        this.className = className;
    }

    public long getSugangId() {
        return sugangId;
    }

    public void setSugangId(long sugangId) {
        this.sugangId = sugangId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getAvailNum() {
        return availNum;
    }

    public void setAvailNum(long availNum) {
        this.availNum = availNum;
    }

    public String getClassDate() {
        return classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}


