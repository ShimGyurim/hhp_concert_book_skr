package io.hhplus.cleancode.presentation.HttpDto;

public class SugangRequest {
    private long sugangId;
    private long studentId;
    private long availNum;
    private String classDate;
    private String className;

    public long getSugangId() {
        return sugangId;
    }

    public long getStudentId() {
        return studentId;
    }

    public long getAvailNum() {
        return availNum;
    }

    public String getClassDate() {
        return classDate;
    }

    public String getClassName() {
        return className;
    }
}
