package io.hhplus.cleancode.application.dto;

public class SugangSelectDto {
    private long sugangId;
    private long studentId;
    private String classDate;

    public long getSugangId() {
        return sugangId;
    }

    public long getStudentId() {
        return studentId;
    }

    public String getClassDate() {
        return classDate;
    }

    public void setSugangId(long sugangId) {
        this.sugangId = sugangId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }
}


